<?php

namespace App\Http\Controllers;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use App\Models\PropertyInquiry;
use App\Models\Property;
use App\Models\UserNotification;
use DB;

class PropertyInquiryController extends Controller
{
    /*
    @ Function : Add Edit Property
    @ Date     : 03-04-2023
    */
    public function addInquiry(Request $request)
    {
        $validate = Validator::make($request->all(), [
            "property_id" => "required"
        ]);

        if ($validate->fails()) {
            $message = $validate->errors()->first();
            if (!empty($message)) {
                return response()->json(["status" => false, "message" => $message]);
            }
        }
        
        $userId = $request->header('userId');

        $data = [];
        $data['user_id'] = $userId;
        $data['property_id'] = $request->property_id;
        $data['name'] = $request->name;
        $data['email'] = $request->email;
        $data['mobile_no'] = $request->mobile_no;
        $data['created_at'] = date('Y-m-d H:i:s');
        PropertyInquiry::create($data);

        $result = Property::with('propertyassets')->where('id',$request->property_id)->first();
        $this->sendPushNotification($result->user_id);
        return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.INQUIRYSENT') ]);
    }

    /*
    @Function : Inquiry Listing
    @Date     : 04-04-2023
    */

    public function inquiryList(Request $request)
    {
        $validate = Validator::make($request->all(), [
            'Limit' => 'nullable|numeric',
            'Page' => 'nullable|numeric',
        ]);

        if ($validate->fails()) {
            $errors = $validate->errors();
            if (!empty($errors)) {
                if ($errors->messages() && !empty($errors->messages())) {
                    return response()->json(["status" => false, "message" => $errors->messages()]);
                }
            }
        }

        $userId = $request->header('userId');

        $limit = (!empty($request->input('Limit'))) ? $request->input('Limit') : 10;
        $page = (!empty($request->input('Page'))) ? $request->input('Page') : 1;
        
        $offset = ceil($page - 1) * $limit;

        $result = PropertyInquiry::select(
                'property_inquiries.id',
                'property_inquiries.property_id',
                'property_inquiries.name',
                'properties.*',
                'users.*'
            )
            ->join("users",function($join) use ($userId){
                $join->on("users.id","=","property_inquiries.user_id");
            })
            ->join("properties",function($join) use ($userId){
                $join->on("properties.id","=","property_inquiries.property_id");
                $join->where("properties.user_id","=",$userId)->whereNull('properties.deleted_at');
            })
            ->orderBy('property_inquiries.id','DESC')
            ->limit($limit)
            ->offset($offset)
            ->get();

        foreach($result as $key => $value){
            $propertyAssets = DB::table('property_assets')->where('property_id',$value->property_id)->get();
            $result[$key]['propertyassets'] = $propertyAssets;
        }

        $totalResult = PropertyInquiry::select(
                'property_inquiries.id',
                'property_inquiries.property_id',
                'property_inquiries.name',
                'properties.*',
                'users.*'
            )->join("users",function($join) use ($userId){
                $join->on("users.id","=","property_inquiries.user_id");
            })->join("properties",function($join) use ($userId){
                $join->on("properties.id","=","property_inquiries.property_id");
                $join->where("properties.user_id","=",$userId);
            })->orderBy('property_inquiries.id','DESC')->count();    
		
        $totalPage = ceil($totalResult / $limit);
        
        return response()->json(['status' => true,'data' => $result,'totalpage' => $totalPage,'totalrecord' => $totalResult]);
    }

    /*
    @Function : Send push notification to property owner
    @Date     : 04-04-2023
    */
    public function sendPushNotification($userId)
    {
        $title = 'Property Inquiry.';
        $body = 'Inquiry initiated for your property.';
        // $tokenData = UserNotification::select('user_id','device_token')
        // ->where('is_active', 1)->where('user_id',$userId)->groupBy('device_token','user_id','id')->orderBy('id','DESC')->first();
        
        $tokenData = UserNotification::select('user_id', 'device_token')
        ->join('users', 'users.id', '=', 'user_notifications.user_id')
        ->where('user_notifications.user_id', $userId)
        ->where('users.is_active', 1)
        ->whereNull('user_notifications.deleted_at')
        ->orderBy('user_notifications.id', 'DESC')
        ->first();

        $notification = array('title'=>$title, 'body'=>$body, 'sound'=>'default', 'badge'=>'1');
        $fcmToken[] = $tokenData->device_token;
        $arrayToSend = array('registration_ids'=>$fcmToken, 'data'=>$notification, 'priority'=>'high');
        $dataString = json_encode($arrayToSend);
        $headers = ['Authorization: key=' . config('app.fcm_key'),'Content-Type: application/json'];
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send');
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $dataString);
        $result = curl_exec($ch);

        DB::table('push_notification_log')->insert(['type'=>'Inquiry','response'=>$result]);
    }
}
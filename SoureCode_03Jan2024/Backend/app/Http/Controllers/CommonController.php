<?php

namespace App\Http\Controllers;
use App\Models\User;
use App\Models\UserNotification;
use App\Models\NotificationList;
use Illuminate\Http\Request;

class CommonController extends Controller
{
    public function staticUrls()
    {
        $data = [];
        $data['privacy-policy-url'] = url('/privacy-policy');
        $data['terms-conditions-url'] = url('/terms-conditions');
        $data['contact-us-url'] = url('/contact-us');
        $data['support-url'] = url('/support');
        return response()->json(['status' => true,  "data" => $data, 'message' => config('constants.'.$this->language .'.GETSUCCESSFULLY')]);
    }

    public function SendNotificationInBackground()
    {
        $title = isset($_POST['title']) ? $_POST['title'] : '';
        $body = isset($_POST['body']) ? $_POST['body'] : '';
        $from_id = isset($_POST['from_id']) ? $_POST['from_id'] : '';
        $to_id = isset($_POST['to_id']) ? $_POST['to_id'] : '';

        $tokenData = UserNotification::select('user_id', 'device_token', 'device_id')->where('user_id', $from_id)->groupBy('device_token','user_id', 'device_id','id')->orderBy('id','DESC')->where(['is_active'=>1])->first();
 
        $insertData = array();
        $insertData['title'] = $title;
        $insertData['body'] = $body;
        $insertData['from_id'] = $from_id;
        $insertData['to_id'] = $to_id;
        NotificationList::create($insertData);
        
        if($tokenData){
            $FcmToken = array();
            array_push($FcmToken, $tokenData->device_token);
            $arrayToSend = [
                "registration_ids" => $FcmToken,
                "content_available" => false,
                "priority" => "high",
                "notification" => [
                    "title" => $title,
                    "body" => $body,
                    "sound"=> "notification.wav",
                    "image"=> null
                ],
                "data" => [
                    "title" => $title,
                    "body" => $body,
                ]
            ];
            
            $dataString = json_encode($arrayToSend);
            $headers = ['Authorization: key=' . env('FCM_KEY'),'Content-Type: application/json'];
            $ch = curl_init();
            curl_setopt($ch, CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send');
            curl_setopt($ch, CURLOPT_POST, true);
            curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
            curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
            curl_setopt($ch, CURLOPT_POSTFIELDS, $dataString);
            $result = curl_exec($ch);
        }
    }
}

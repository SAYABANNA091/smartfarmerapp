<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Validator;
use App\Models\User;
use App\Models\ForgotPassword;
use Illuminate\Support\Facades\Hash;
use Illuminate\Http\Request;
use Illuminate\Support\Str;
use Illuminate\Support\Facades\URL;
use Mail;
use Illuminate\Support\Facades\Auth;
use App\Models\UserAuthentication;
use App\Models\UserNotification;
use App\Models\ChatList;
use App\Models\ChatDetail;
use App\Models\OtpReference;
use App\Models\RecdSearch;
use Carbon\Carbon;
use Illuminate\Support\Facades\DB;
use Illuminate\Validation\Rule;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Facades\File;
use App\Models\Property;
use App\Models\PropertyAssets;
use App\Models\Product;
use App\Models\ProductAssets;
use App\Models\ProductInquiry;
use App\Models\ProductFavourite;

class UserController extends Controller
{
    private $apiToken;

    public function __construct()
    {
        parent::__construct();
        $this->apiToken = uniqid(base64_encode(str::random(60)));
    }

    /*
     @ Function : SignUp(Customer Basic Details)
     @ Date     : 31-03-2023
    */
    public function SignUp(Request $request)
    {
        $validateRequest = Validator::make($request->all(), [
            "phone_number" => [
                "required",
                "max:15",
                Rule::unique('users', 'phone_number')->where(function ($query) {
                    $query->where('is_active', '!=', 2)->where('deleted_at', null);
                }),
            ],
        ]);
        if ($validateRequest->fails()) {
            $message = $validateRequest->errors()->first();
            if (!empty($message)) {
                return response()->json(["status" => false, "message" => $message]);
            }
        }
        OtpReference::where('phone_number', $request->phone_number)->delete();
        $otpData = array();
        $otp = mt_rand(1000, 9999);
        $otpData['phone_number'] = $request->phone_number;
        $otpData['otp'] = $otp;
        $storeOtp = OtpReference::create($otpData);
        // $this->sendOTPService($request->phone_number, $otp);
        if ($storeOtp) {
            return response()->json([
                "status" => true,
                "message" => config('constants.'.$this->language .'.OTPSEND'),
                "otp" => $otp
            ]);
        } else {
            return response()->json(["status" => false, "message" => config('constants.'.$this->language .'.SOMETHINGWRONG') ]);
        }
    }

    /*
     @ Function : SignUp(User Verify OTP Which is get on it's phone number)
     @ Date     : 31-03-2023
    */
    public function verifyOtp(Request $request)
    {
        if (empty($request->header("deviceId")) || empty($request->header("deviceToken"))) {
            return response()->json(["status" => false, "message" => config('constants.'.$this->language .'.HEADERPARAMETER')]);
        }
        $validateRequest = Validator::make($request->all(), [
            "phone_number" => [
                "required",
                "max:15",
                Rule::unique('users', 'phone_number')->where(function ($query) {
                    $query->where('is_active', '!=', 2)->where('deleted_at', null);
                }),
            ],
            "otp" => "required|min:4|max:6",
            "checkNewUse" => "nullable|max:1"
        ]);
        if ($validateRequest->fails()) {
            $message = $validateRequest->errors()->first();
            if (!empty($message)) {
                return response()->json(["status" => false, "message" => $message]);
            }
        }
        $where = [];
        $phoneNo = $request->phone_number;
        if (!empty($phoneNo)) {
            $otp_reference = OtpReference::withTrashed()->where('otp', $request->otp)->where('phone_number', $phoneNo)->first();
        } else {
            $where['otp'] = $request->otp;
            $where['phone_number'] = $phoneNo;
            $otp_reference = OtpReference::withTrashed()->where($where)->first();
        }
        if ($otp_reference == null) {
            return response()->json(["status" => false, "message" => config('constants.'.$this->language .'.OTPNOTMATCHED')]);
        } else if ($otp_reference->deleted_at != null) {
            return response()->json(["status" => false, "message" => config('constants.'.$this->language .'.OTPEXPIRED') ]);
        } else { // OTP match
            OtpReference::where('phone_number', $phoneNo)->delete();
            $data = array();
            $data['phone_number'] = $request->phone_number;
            $data['user_type'] = 2;
            $data['is_active'] = 1;
            $create = User::create($data);
            $userId = $create->id;
            UserNotification::create([
                "user_id" => $userId,
                "device_id" => $request->header('deviceId'),
                "device_token" => $request->header('deviceToken')
            ]);
            UserAuthentication::where('user_id', $userId)->delete();
            UserAuthentication::create([
                "device_id" => $request->header('deviceId'),
                "device_token" => $request->header('deviceToken'),
                "user_id" => $userId,
                "user_token" => $this->apiToken
            ]);
            $userAllData = ["user" => User::getUserData($userId), "token" => $this->apiToken];

            return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.OTPVERIFICATION'), "data" => $userAllData]);
        }
    }

    /*
     @ Function : SignUp(Set his Language)
     @ Date     : 03-04-2023
    */
    public function setLanguage(Request $request)
    {
        if (empty($request->header("deviceId")) || empty($request->header("deviceToken"))) {
            return response()->json(["status" => false, "message" => config('constants.'.$this->language .'.HEADERPARAMETER') ]);
        }

        $validateRequest = Validator::make($request->all(), [
            "phone_number" => "required|max:15",
            "language_id" => "required|max:1",
        ]);

        if ($validateRequest->fails()) {
            $message = $validateRequest->errors()->first();
            if (!empty($message)) {
                return response()->json(["status" => false, "message" => $message]);
            }
        }

        $updateLanguage = User::where('phone_number', $request->phone_number)->where('user_type', 2)->update(['language_id' => $request->language_id]);

        return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.LANGUAGEUPDATE') ]);
    }

    /*
     @ Function : SignIn(Customer login)
     @ Date     : 03-04-2023
    */
    public function SignIn(Request $request)
    {
        if (empty($request->header("deviceId")) || empty($request->header("deviceToken"))) {
            return response()->json(["status" => false, "message" => config('constants.'.$this->language .'.HEADERPARAMETER') ]);
        }
        $validateRequest = Validator::make($request->all(), [
            "password" => "required|max:20|min:8",
        ]);
        if ($validateRequest->fails()) {
            $message = $validateRequest->errors()->first();
            if (!empty($message)) {
                return response()->json(["status" => false, "message" => $message]);
            }
        }
        if (preg_match('/^[0-9]{10}+$/', $request->email_phone)) { /* If User Enter Phone */
            $loginUserDetails = User::where('phone_number', $request->email_phone)->first();
        } else {
            return response()->json(["status" => false, "message" => config('constants.'.$this->language .'.ENTERVALIDEMALPHONE') ]);
        }
        if ($loginUserDetails) {
            if ($loginUserDetails->is_active == 2) { /* User not verify*/
                return response()->json(['status' => false, 'message' => config('constants.'.$this->language .'.USERNOTVERIFY') ]);
            } elseif ($loginUserDetails->is_active == 0) { /* User is block*/
                return response()->json(['status' => false, 'message' => config('constants.'.$this->language .'.ADMINBLOCK') ]);
            } else { /*is_active = 1 User Login */
                $userId = $loginUserDetails->id;
                $checkPass = Hash::check($request->password, $loginUserDetails->password);
                if ($checkPass) {
                    UserNotification::where('user_id', $userId)->update([
                        "device_id" => $request->header('deviceId'),
                        "device_token" => $request->header('deviceToken'),
                        "is_active" => 1,
                    ]);
                    UserAuthentication::where('user_id', $userId)->delete();
                    $apiToken = $this->apiToken;
                    UserAuthentication::create([
                        "device_id" => $request->header('deviceId'),
                        "device_token" => $request->header('deviceToken'),
                        "user_id" => $userId,
                        "user_token" => $apiToken
                    ]);
                    $userAllData = ["user" => User::getUserData($userId), "token" => $apiToken];
                    return response()->json(['status' => true, 'message' => config('constants.'.$this->language .'.SIGNIN') , 'data' => $userAllData]);
                } else {
                    return response()->json(['status' => true, 'message' => config('constants.'.$this->language .'.EMAILMOBILEINVALID')]);
                }
            }
        } else {
            return response()->json(['status' => false, 'message' => config('constants.'.$this->language .'.USERNOTEXIST')]);
        }
    }

    /*
     @ Function : SignIn(Forgot Password)
     @ Date     : 03-04-2023
    */
    public function forgotPassword(Request $request)
    {
        $validateRequest = Validator::make($request->all(), [
            "phone_number" => "nullable|max:15|required_without:email",
            "email" => "nullable|email|max:64|required_without:phone_number"
        ]);

        if ($validateRequest->fails()) {
            $message = $validateRequest->errors()->first();
            if (!empty($message)) {
                return response()->json(["status" => false, "message" => $message]);
            }
        }

        try {
            if ($request->phone_number) {
                $userDetails = User::where('phone_number', $request->phone_number)->where('is_active', '!=', 2)->where('user_type', 2)->first();
            } else if ($request->email) {
                $userDetails = User::where('email', $request->email)->where('is_active', '!=', 2)->where('user_type', 2)->first();
            }
            if ($userDetails) {
                if ($userDetails->is_active == 0) {
                    return response()->json(['status' => false, 'message' => config('constants.'.$this->language .'.USERBLOCKADMIN') ]);
                } else {
                    OtpReference::where('phone_number', $request->phone_number)->orWhere('email', $request->email)->delete(); /* Destroy Previous OTP */
                    $otpData = array();
                    $otp = mt_rand(1000, 9999);
                    $otpData['phone_number'] = $request->phone_number;
                    $otpData['email'] = $request->email;
                    $otpData['otp'] = $otp;
                    $storeOtp = OtpReference::create($otpData);
                    if ($request->phone_number) {
                        $this->sendOTPService($request->phone_number, $otp);
                        return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.OTPSEND'), "otp" => $otp]);
                    } else if ($request->email) {
                        $Subject = "Forgot Password";
                        $fphtml = "<html><head><title>Forgot Password</title></head><body><div style='width:100% !important; float: left !important; text-align: center !important;'><div style='width:100% !important; max-width: 600px !important; display: inline-block !important; padding:15px !important;'><table cellpadding='0' cellspacing='0' width='100%'><tbody><tr><td><div class='terms-conditions-page justify-text' style='font-family: Open Sans, sans-serif; font-size: 15px;width: 90% !important; margin: auto !important; text-align: center !important;'><p style='font-size: 25px;font-weight: 600;font-family: Open Sans, sans-serif; color: #574244 !important;'><strong>Forgot Password</strong></p><p style='font-size: 15px;'>Dear User,</p><p style='font-size: 15px;'>Your OTP is: $otp</p><p style='font-size: 15px;'>Regards,</p><p style='font-size: 15px;'>Soni Team</p></div></td></tr></tbody></table> </div></div></body></html>";
                        $html = array(
                            /* "content" => 'Your OTP : ' . $otp, */
                            "content" => $fphtml,
                            "toEmail" => $request->email,
                            "subject" => $Subject
                        );
                        Mail::send([], $html, function ($message) use ($html) {
                            $message->to($html['toEmail'])
                                ->subject($html['subject'])
                                ->setBody($html['content'], 'text/html'); // for HTML rich messages
                        });
                        return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.OTPSEND'), "otp" => $otp]);
                    }
                }
            } else {
                return response()->json(['status' => false, 'message' => config('constants.'.$this->language .'.USERNOTEXIST') ]);
            }
        } catch (\Exception $e) {
            return response()->json(["status" => false, "message" => $e->getMessage()]);
        }
    }

    /*
     @ Function : SignIn(reset password)
     @ Date     : 03-04-2023
    */
    public function resetPassword(Request $request)
    {
        $validateRequest = Validator::make($request->all(), [
            "email" => "nullable|email|max:64|required_without:phone_number",
            "phone_number" => "nullable|max:15|required_without:email",
            "new_password" => "required|max:20|min:8",
            "otp" => "required|min:4|max:6"
        ]);

        if ($validateRequest->fails()) {
            $message = $validateRequest->errors()->first();
            if (!empty($message)) {
                return response()->json(["status" => false, "message" => $message]);
            }
        }

        try {
            $where = [];
            $phoneNo = $request->phone_number;
            $emailId = $request->email;

            if ($request->phone_number) {
                $where['phone_number'] = $phoneNo;
            } elseif ($request->email) {
                $where['email'] = $emailId;
            }
            $oldOTP = OtpReference::withTrashed()->where($where)->where('otp', $request->otp)->orderBy('id', 'DESC')->first();
            $userDetails = User::where($where)->where('is_active', 1)->where('user_type', 2)->first();

            if ($oldOTP && $userDetails) {
                $userId = $userDetails->id;
                User::where('id', $userId)->update(['password' =>  Hash::make($request->new_password)]);
                return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.USERRESETPASSWORD') ]);
            } else {
                return response()->json(["status" => false, "message" => config('constants.'.$this->language .'.CREDENTIALSNOTMATCH') ]);
            }
        } catch (\Exception $e) {
            return response()->json(["status" => false, "message" => $e->getMessage()]);
        }
    }

    /*
     @ Function : EditProfile(Edit Profile)
     @ Date     : 04-04-2023
    */
    public function EditProfile(Request $request)
    {
        $userId = $request->header("userId");
        $validateRequest = Validator::make($request->all(), [
            "image" => "nullable|max:2048|mimes:jpeg,jpg,png",
            "first_name" => "required",
            "last_name" => "required",
            "email" => [
                "nullable",
                "email",
                "max:64",
                Rule::unique('users', 'email')->where(function ($query) use ($userId) {
                    $query->where('is_active', '!=', 2)
                        ->where('id', '<>', $userId);
                }),
            ],
            "state" => "required|max:20",
            "district" => "required|max:20",
            "taluka" => "required|max:20",
            "village" => "required|max:20",
            "pincode" => "required|max:10",
        ]);
        if ($validateRequest->fails()) {
            $message = $validateRequest->errors()->first();
            if (!empty($message)) {
                return response()->json(["status" => false, "message" => $message]);
            }
        }
        $getUserData = User::where("id", $userId)->first();
        $userToken = $request->header('userToken');
        $data = array();
        $data['first_name'] = $request->first_name ? $request->first_name : '';
		$data['last_name'] = $request->last_name ? $request->last_name : '';
		$data['email'] = $request->email ? $request->email : '';
		$data['state'] = $request->state ? $request->state : '';
		$data['district'] = $request->district ? $request->district : '';
		$data['taluka'] = $request->taluka ? $request->taluka : '';
		$data['village'] = $request->village ? $request->village : '';
		$data['pincode'] = $request->pincode ? $request->pincode : '';
        $data['profile_url'] = $request->hasFile('image') ? $request->file('image') : '';
        if ($request->file('image')) {
            $destinationPath = public_path() . '/user-profile';
            $filename = time() . '-' . $request->file('image')->getClientOriginalName();
            if (!empty($getUserData->profile_url)) { /* Delete OLD profile */
                $oldfilename = Str::after($getUserData->profile_url, 'user-profile/');
                File::delete(public_path('user-profile' . '/' . $oldfilename));
            }
            $request->file('image')->move($destinationPath, $filename);
            $asset_url = config('app.base_url').'/user-profile'.'/'.$filename;
            $data['profile_url'] = $asset_url;
        }
        User::where('id', $userId)->update($data);
        
        return response()->json(["status" => true, 'message' => config('constants.'.$this->language .'.PROFILEUPDATE'), 'data' => User::getUserData($userId)]);
    }

    /*
     @ Function : GetProfileDetails(Get user details)
     @ Date     : 04-04-2023
    */
    public function GetProfileDetails(Request $request)
    {
        $userId = $request->header("userId");
        $userToken = $request->header('userToken');
        $userData = User::getUserData($userId);
        return response()->json(['status' => true, 'data' => $userData]);
    }

    /*
     @ Function : change_password(Change password with old password)
     @ Date     : 04-04-2023
    */
    public function change_password(Request $request)
    {
        $validateRequest = Validator::make($request->all(), [
            'old_password' => 'required|max:20|min:8',
            'new_password' => 'required|max:20|min:8',
            'confirm_password' => 'required|same:new_password',
        ]);
        if ($validateRequest->fails()) {
            $message = $validateRequest->errors()->first();
            if (!empty($message)) {
                return response()->json(["status" => false, "message" => $message]);
            }
        }
        $userId = $request->header("userId");
        $oldpassword = $request->old_password;
        $newpassword = $request->new_password;
        $validate_user = User::where("id", $userId)->first("password");
        $checkpassword = Hash::check($oldpassword, $validate_user->password);
        if ($checkpassword == 1) {
            $NewPassword = Hash::make($newpassword);
            User::where("id", $userId)->update(["password" => $NewPassword]);
            return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.PASSWORDCHANGED') ]);
        } else {
            return response()->json(["status" => false, "message" => config('constants.'.$this->language .'.PASSWORDNOTMATCHED') ]);
        }
    }

    /*
     @ Function : deleteAccount(Delete account)
     @ Date     : 04-04-2023
    */
    public function deleteAccount(Request $request)
    {
        $userId = $request->header("userid");
        User::where('id', $userId)->delete();
        UserAuthentication::where('user_id', $userId)->delete();
        Property::where('user_id', $userId)->delete();
        $product_id = Product::select('id')->where('user_id',$userId)->get();
        if(!empty($product_id)){
            foreach($product_id as $key => $value){
            ProductAssets::where('product_id',$value['id'])->delete();
            }
        }
        Product::where('user_id',$userId)->delete();
        ProductFavourite::where('user_id', $userId)->delete();
        ProductInquiry::where('user_id',$userId)->delete();
        ProductInquiry::where('from_id',$userId)->delete();
        return response()->json(['status' => false, 'message' => config('constants.'.$this->language .'.ACCOUNTDELETED') ]);
    }

    /*
     @ Function : logout(User logout)
     @ Date     : 04-04-2023
    */
    public function logout(Request $request)
    {
        $userId = $request->header("userId");
        $userToken = $request->header('usertoken');
        UserAuthentication::where('user_id', $userId)->where('user_token', $userToken)->delete();
        UserNotification::where('user_id', $userId)->update(["is_active" => 0]);
        return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.LOGOUT') ]);
    }

    public function sendOTPService($mobileNo,$otp)
    {
        $curl = curl_init();
        curl_setopt_array($curl, array(
            CURLOPT_URL => 'http://sms.bulkssms.com/submitsms.jsp?user=SONIAPP&key=fad9176761XX&mobile='.$mobileNo.'&message=Dear%20Customer,%20Your%20OTP%20is%20'.$otp.'%20Please%20do%20not%20share%20this%20OTP.%20Regards&senderid=OTPSSS&accusage=1&entityid=1201159543060917386&tempid=1207161729866691748',
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_ENCODING => '',
            CURLOPT_MAXREDIRS => 10,
            CURLOPT_TIMEOUT => 0,
            CURLOPT_FOLLOWLOCATION => true,
            CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
            CURLOPT_CUSTOMREQUEST => 'GET',
        ));
        $response = curl_exec($curl);
        curl_close($curl);
        return $response;
    }

}

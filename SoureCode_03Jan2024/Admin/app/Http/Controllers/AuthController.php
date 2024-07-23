<?php

namespace App\Http\Controllers;

use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Redirect;
use Illuminate\Support\Facades\Session;

class AuthController extends Controller
{
    public function login(Request $request)
    {
        $email = $request['email'];
        $password = $request['password'];

        $request->validate([
            'email' => 'required',
            'password' => 'required',
        ]);

        $loginUserDetails = User::where('user_type', 1)->where('deleted_at', null)->where('email', $email)->first();

        if (empty($loginUserDetails)) {
            Session::flash('message', 'Username or password is incorrect.!');
            Session::flash('alert-class', 'alert-danger');
            return Redirect::to("/");
        }

        // ckeck user active or not
        if ($loginUserDetails->is_active == 0) {
            Session::flash('message', 'User does not active');
            Session::flash('alert-class', 'alert-danger');
            return Redirect::to("/");
        }

        // if ($loginUserDetails->user_type == 3) {
        //     Session::flash('message', 'User does not active');
        //     Session::flash('alert-class', 'alert-danger');
        //     return Redirect::to("/");
        // }

        $checkemail = $loginUserDetails->email;
        $checkIsdelete = $loginUserDetails->deleted_at;

        $checkPass =  Hash::check($password, $loginUserDetails->password);

        if (empty($checkemail && $checkPass == 'false')) {
            Session::flash('message', 'Username or password is incorrect.!');
            Session::flash('alert-class', 'alert-danger');
            return Redirect::to("/");
        }

        if (!empty($checkemail && $checkPass == 'true' && $checkIsdelete == Null)) {

            $emailresult = $email;
            $loginUser = User::where('email', $emailresult)->first();
            Session::PUT('LoginUser', $loginUser);

            return Redirect::to('/dashboard');
        } else {
            Session::flash('message', 'Unauthorized user.');
            Session::flash('alert-class', 'alert-danger');
            return Redirect::to("/");
        }
    }

    public function logout()
    {
        Session::flush();

        return Redirect::to("/");
    }
}

<?php

namespace App\Http\Middleware;

use Closure;
use DB;
use App\Models\UserAuthentication;

class APIToken
{
    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure  $next
     * @return mixed
     */
    public function handle($request, Closure $next)
    {
        if(empty($request->header("deviceId")) || empty($request->header("deviceToken")) || empty($request->header("userId")) || empty($request->header("userToken"))){
            return response()->json(["status" => false, "message" => "Invalid Header"]);
        }else{
            $authcheck = UserAuthentication::userAuthCheck($request->header("userId"), $request->header("userToken"))->get()->count();

            if($authcheck == 1){
                return $next($request);
            }else{
                return response()->json(['status' => false, 'message' => 'Unauthorized user'],401);
            }
        }
    }
}

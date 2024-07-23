<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use DB;
use Illuminate\Database\Eloquent\SoftDeletes;

class UserAuthentication extends Model
{
    use HasFactory,SoftDeletes;
    protected $fillable = [
        'device_id','device_token','user_token','user_id'
    ];

    public static function userAuthCheck($userId,$userToken)
    {
        return UserAuthentication::select(DB::Raw('IFNULL(user_token,"")as user_token'),DB::Raw('IFNULL(user_id,"")as user_id'))
        ->where('user_id',$userId)
        ->where('user_token',$userToken);
    }
}

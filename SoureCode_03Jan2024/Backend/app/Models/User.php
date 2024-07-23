<?php

namespace App\Models;

use Illuminate\Contracts\Auth\MustVerifyEmail;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\SoftDeletes;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use DB;
class User extends Authenticatable
{
    use HasFactory, Notifiable,SoftDeletes;

    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'first_name','last_name','email','phone_number','profile_url','password','language_id','user_type','state','district','taluka','village','pincode','is_active'
    ];
    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [
        'password', 'remember_token',
    ];

    /**
     * The attributes that should be cast to native types.
     *
     * @var array
     */
    protected $casts = [
        'email_verified_at' => 'datetime',
    ];


    public static function getData($id,$imageUrlData)
    {
        return User::select(DB::Raw('IFNULL(users.id,"")as user_id'),DB::Raw('IFNULL(users.name,"")as name'), DB::Raw('IFNULL(users.email,"")as email'), DB::Raw('IFNULL(users.phone_number,"")as phone_number'), DB::Raw('IFNULL(users.city_id,"")as city_id'),DB::Raw('IFNULL(cities.name,"")as city_name'),DB::Raw('IFNULL(users.google_id,"")as google_id'), DB::Raw('IFNULL(users.apple_id,"")as apple_id'),DB::Raw('IFNULL(users.user_type,"")as user_type'),DB::Raw('IFNULL(users.no_of_reviews,"")as no_of_reviews'),DB::Raw('IFNULL(users.who_are_you,"")as who_are_you'),DB::Raw('IFNULL(users.yelp_url,"")as yelp_url'),DB::Raw('IFNULL(users.google_url,"")as google_url'),DB::Raw('IFNULL(users.tripadvisor_url,"")as tripadvisor_url'),(DB::raw("CONCAT('$imageUrlData','/',users.image) AS image_url")))
        ->leftjoin('cities', 'cities.id', '=', 'users.city_id')
            ->where('users.id',$id)
            ->where('users.is_active', 1);
    }
    // public function tokens()
    // {
    //     return $this->hasMany(UserAuthentication::class);
    // }

    public static function getProfileData($imageUrl,$userId){
        return User::select(
            DB::Raw('IFNULL(users.id,"")as user_id'),
            DB::Raw('IFNULL(users.name,"")as name'),
            DB::Raw('IFNULL(users.email,"")as email'),
            DB::Raw('IFNULL(users.phone_number,"")as phone_number'),
            DB::Raw('IFNULL(users.city_id,"")as city_id'),
            DB::Raw('IFNULL(cities.name,"")as city_name'),
            DB::Raw('IFNULL(users.google_id,"")as google_id'),
            DB::Raw('IFNULL(users.apple_id,"")as apple_id'),
            DB::Raw('IFNULL(users.admin_status,"")as admin_status'),
            DB::Raw('IFNULL(users.no_of_reviews,"")as no_of_reviews'),
            DB::Raw('IFNULL(users.user_type,"")as user_type'),
            DB::Raw('IFNULL(users.who_are_you,"")as who_are_you'),
            DB::Raw('IFNULL(users.yelp_url,"")as yelp_url'),
            DB::Raw('IFNULL(users.google_url,"")as google_url'),
            'users.wallet_balance',
            DB::Raw('IFNULL(users.tripadvisor_url,"")as tripadvisor_url'),
            DB::Raw('IFNULL(recd_searches.is_active,"")as search_is_active'),
            DB::Raw('IFNULL(recd_searches.search_keyword,"")as search_keyword'),
            DB::Raw('IFNULL(recd_searches.city_id,"")as search_city_id'),
            DB::Raw('IFNULL(CS.name,"")as search_city_name'),
            DB::Raw('IFNULL(recd_searches.time,"")as search_time'),
            (DB::raw("CONCAT('$imageUrl','/',users.image) AS image_url"))
            )->leftJoin('cities', 'cities.id', '=', 'users.city_id')
            ->leftJoin('recd_searches', function($join)
            {
                $join->on('recd_searches.user_id', '=', 'users.id');
                $join->where('recd_searches.is_active','=', 1);
            })
            //->leftJoin('recd_searches', 'recd_searches.user_id', '=', 'users.id')
            ->leftJoin('cities as CS', 'recd_searches.city_id', '=', 'CS.id')
            ->where('users.id', $userId)
            ->where('users.is_active', 1);
    }

    public static function getUserData($userId)
    {
        return User::where('id', $userId)->first(['id','first_name', 'last_name', 'email', 'phone_number', 'profile_url', 'language_id', 'user_type', 'state', 'district', 'taluka', 'village', 'pincode']);
    }
}

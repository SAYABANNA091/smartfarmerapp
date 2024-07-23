<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class Property extends Model
{
    use HasFactory,SoftDeletes;

    protected $guarded = [];
    
    public $timestamps = false;

    public function propertyassets()
    {
        return $this->hasMany(PropertyAssets::class,'property_id','id');
    }

    public function userdetails()
    {
        return $this->hasOne(User::class,'id','user_id')->withTrashed();
    }

    public function userdata()
    {
        return $this->hasOne(User::class,'id','user_id')->where('deleted_at', NULL);
    }
}

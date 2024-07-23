<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class UserNotification extends Model
{
    use HasFactory,SoftDeletes;
    protected $fillable = [
        'device_id','device_token','user_id','is_active'
    ];
}

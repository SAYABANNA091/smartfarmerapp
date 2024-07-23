<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class ForgotPassword extends Model
{
    public $table = 'forgot_passwords';

    protected $fillable = [
        'email',
        'token',
    ];
    use HasFactory,SoftDeletes;
}

<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class Employee extends Model
{
    use HasFactory,SoftDeletes;
    public $timestamps = false;
    
    protected $fillable = [
        'employee_name','profile_picture','employee_id','mobile_no','employee_email','department','created_by','created_at'
    ];
}

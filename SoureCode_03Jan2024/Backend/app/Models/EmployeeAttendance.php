<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class EmployeeAttendance extends Model
{
    use HasFactory,SoftDeletes;
    public $timestamps = false;
    
    protected $fillable = [
        'employee_id','attend_date','created_by','created_at'
    ];
}

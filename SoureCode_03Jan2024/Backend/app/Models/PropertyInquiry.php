<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use App\Models\Property;

class PropertyInquiry extends Model
{
    use HasFactory;

    public $timestamps = false;
    
    protected $fillable = [
        'property_id','user_id','name','email','mobile_no','created_at'
    ];

    public function propertydetails()
    {
        return $this->hasMany(Property::class,'id','property_id');
    }
}

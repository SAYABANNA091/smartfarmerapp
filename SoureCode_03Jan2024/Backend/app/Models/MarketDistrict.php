<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class MarketDistrict extends Model
{
    use HasFactory; use HasFactory;
    public $timestamps = false;
    protected $fillable = [
        'name'    
    ]; 
}

<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class MarketData extends Model
{
    use HasFactory; use HasFactory;
    public $table = 'market_datas';
    public $timestamps = false;
    protected $fillable = [
        'name'    
    ]; 
}

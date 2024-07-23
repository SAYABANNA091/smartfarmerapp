<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class PropertyAssets extends Model
{
    use HasFactory,SoftDeletes;
    protected $guarded = [];
    public $timestamps = false;
    // protected $fillable = [
    //     'product_id','asset_url'
    // ];  
}

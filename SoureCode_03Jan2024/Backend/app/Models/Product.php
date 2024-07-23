<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use App\Models\ProductCategory;
use App\Models\ProductAssets;

// use Illuminate\Database\Eloquent\SoftDeletes;


class Product extends Model
{
    use HasFactory;
    public $timestamps = false;

    protected $fillable = [
        'user_id','title','category_id','description','price', 'u_name', 'u_mobile', 'u_pincode','profile_image','latitude','longitude'
    ];   

    public function productassets()
    {
        return $this->hasMany(ProductAssets::class,'product_id','id');
    }
    
    public function categoryData()
    {
        return $this->hasOne(ProductCategory::class, 'id', 'category_id');
    }

    public function productfavorite()
    {
        return $this->hasOne(ProductFavourite::class, 'product_id', 'id')->select(['id', 'product_id']);
    }

    public function userData()
    {
        return $this->hasOne(User::class, 'id', 'user_id')->select(['id','first_name','last_name','phone_number','profile_url']);
    }
    
}

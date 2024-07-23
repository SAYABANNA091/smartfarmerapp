<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;


class ProductInquiry extends Model
{   
    use HasFactory,SoftDeletes;
    public $timestamps = false;
    protected $fillable = [
        'user_id','product_id', 'name', 'email', 'mobile_no', 'quantity', 'from_id','deleted_at',
    ];

    public function productassets()
    {
        return $this->hasMany(ProductAssets::class,'product_id','id');
    }

    public function categoryData()
    {
        return $this->hasOne(ProductCategory::class, 'id', 'category_id')->select(['id', 'name']);
    }

    public function productfavorite()
    {
        return $this->hasOne(ProductFavourite::class, 'product_id', 'id')->select(['id', 'product_id']);
    }

    public function userData()
    {
        return $this->hasOne(User::class, 'id', 'user_id')->select(['id','first_name','last_name','phone_number','profile_url']);
    }

    public function productData()
    {
        return $this->hasOne(Product::class, 'id', 'product_id');
    }
}

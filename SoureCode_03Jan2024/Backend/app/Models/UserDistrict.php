<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class UserDistrict extends Model
{
    use HasFactory,SoftDeletes;
    protected $fillable = ['name', 'states_id'];

    public function state()
    {
        return $this->hasOne(UserState::class, 'id', 'states_id');
    }
}

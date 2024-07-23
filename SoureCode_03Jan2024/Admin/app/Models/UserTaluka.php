<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class UserTaluka extends Model
{
    use HasFactory,SoftDeletes;
    protected $fillable = ['name', 'districts_id', 'states_id'];

    public function district()
    {
        return $this->hasOne(UserDistrict::class, 'id', 'districts_id');
    }

    public function state()
    {
        return $this->hasOne(UserState::class, 'id', 'states_id');
    }
}

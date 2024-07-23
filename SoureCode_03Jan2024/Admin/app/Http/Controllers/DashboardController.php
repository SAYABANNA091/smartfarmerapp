<?php

namespace App\Http\Controllers;

use App\Models\ProductMaster;
use App\Models\User;
use Illuminate\Http\Request;

class DashboardController extends Controller
{
    public function index()
    {
        $userCount = User::where('user_type', 2)->where('deleted_at', null)->count();
        return view('pages.home', compact('userCount'));
    }
}

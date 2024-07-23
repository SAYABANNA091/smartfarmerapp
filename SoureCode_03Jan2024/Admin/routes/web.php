<?php

use App\Http\Controllers\ArticlesManagementControll;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\DashboardController;
use App\Http\Controllers\CategoryManagementController;
use App\Http\Controllers\ProductManagementController;
use App\Http\Controllers\RequestVerificationController;
use App\Http\Controllers\UserManagementController;
use App\Http\Controllers\UserStateController;
use App\Http\Controllers\DistrictController;
use App\Http\Controllers\TalukaController;


use Illuminate\Support\Facades\Route;
use Illuminate\Support\Facades\Session;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('/', function () {
    $value = Session::get('LoginUser');
    if (!(empty($value))) {
        return redirect('/dashboard');
    }
    return view('pages.login');
});


Route::post('/login', [AuthController::class, 'login'])->name('login');
Route::get('/logout', [AuthController::class, 'logout'])->name('logout');

Route::group(['middleware' => 'loginCheck'], function () {
    Route::get('/dashboard', [DashboardController::class, 'index'])->name('dashboard');
    //User Management
    Route::controller(UserManagementController::class)->group(function () {
        Route::get('/userManagement', 'index')->name('userManagement');
        Route::get('/user-list', 'userList')->name('userList');
        Route::get('/deleteUser/{id}', 'deleteUser')->name('deleteUser');
        Route::get('/blockUnblockUser/{id}', 'blockUnblockUser')->name('blockUnblockUser');
        Route::get('/userManagement/viewUserDetail/{id}', 'viewUserDetail')->name('viewUserDetail');
    });
    
    // User States
    Route::controller(UserStateController::class)->group(function () {
        Route::get('/statesList', 'statesList')->name('statesList');
        Route::get('/states-data', 'statesData')->name('statesData');
        Route::get('/deleteState/{id}', 'deleteState')->name('deleteState');
        Route::get('/statesList/add-states-data{Id?}', [UserStateController::class, 'addStates'])->name('addStates');
        Route::match(['get', 'post'], '/add-edit-states/{Id?}', [UserStateController::class, 'addEditStates'])->name('addEditStates');
    });
    // User Districts
    Route::controller(DistrictController::class)->group(function () {
        Route::get('/districtList', 'districtList')->name('districtList');
        Route::get('/district-data', 'districtData')->name('districtData');
        Route::get('/delete-district/{id}', 'deleteDistrict')->name('deleteDistrict');
        Route::get('/districtList/add-district-data{Id?}', [DistrictController::class, 'addDistrict'])->name('addDistrict');
        Route::match(['get', 'post'], '/add-edit-district/{Id?}', [DistrictController::class, 'addEditDistrict'])->name('addEditDistrict');
    });
    // User Taluka
    Route::controller(TalukaController::class)->group(function () {
        Route::get('/talukaList', 'talukaList')->name('talukaList');
        Route::get('/taluka-data', 'talukaData')->name('talukaData');
        Route::get('/delete-taluka/{id}', 'deleteTaluka')->name('deleteTaluka');
        Route::get('/districts/{state_id}', 'getDistricts')->name('getDistricts');
        Route::get('/talukaList/add-taluka-data{Id?}', [TalukaController::class, 'addTaluka'])->name('addTaluka');
        Route::match(['get', 'post'], '/add-edit-taluka/{Id?}', [TalukaController::class, 'addEditTaluka'])->name('addEditTaluka');
    });
    
});

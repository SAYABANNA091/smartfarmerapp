<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\API\UserController;
use App\Http\Controllers\API\PaymentController;
use App\Http\Controllers\CommonController;

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
    return view('welcome');
});
Route::get('/reset-password',[UserController::class,'resetPasswordLoad']);
Route::post('/reset-password',[UserController::class,'resetPassword']);
Route::view('thankyou', 'thankyou');
Route::view('/privacy-policy','privacy-policy');
Route::view('/terms-conditions','terms-conditions');
Route::view('/contact-us','contact-us');
Route::view('/support','support');
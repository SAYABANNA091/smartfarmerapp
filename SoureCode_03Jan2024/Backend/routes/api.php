<?php

use App\Http\Controllers\CommonController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Controllers;
use App\Http\Controllers\UserController;
use App\Http\Controllers\PropertyController;
use App\Http\Controllers\PropertyInquiryController;
use App\Http\Controllers\EmployeeController;
use App\Http\Controllers\ProductController;
use App\Http\Controllers\MarketController;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::middleware('auth:api')->get('/user', function (Request $request) {
    return $request->user();
});

Route::post('/sign-up', [UserController::class, 'SignUp']);
Route::post('/sign-In',[UserController::class,'SignIn']);
Route::post('/verifyOtp', [UserController::class, 'verifyOtp']);
Route::post('/set-language-preferences', [UserController::class, 'setLanguage']);
Route::post('/forgot-password', [UserController::class, 'forgotPassword']);
Route::post('/reset-password', [UserController::class, 'resetPassword']);
Route::get('/static-urls', [CommonController::class, 'staticUrls']);
Route::post('/SendNotificationInBackground', [CommonController::class, 'SendNotificationInBackground']);

Route::middleware(['APIToken'])->group(function () {
    Route::post('/logout',[UserController::class,'logout']);
    Route::post('/edit-profile', [UserController::class, 'EditProfile']);
    Route::post('/get-profile-details', [UserController::class, 'GetProfileDetails']);
    Route::post('/property-types', [PropertyController::class, 'getPropertyTypes']);
    Route::post('/add-edit-property', [PropertyController::class, 'addEditProperty']);
    Route::post('/add-edit-product', [ProductController::class, 'addEditProduct']);
    Route::post('/product-list', [ProductController::class, 'productList']);
    Route::post('/category-product-list', [ProductController::class, 'categoryproductList']);
    Route::post('/get-porudct-details-by-id', [ProductController::class, 'productListById']);
    Route::post('/favourites-product', [ProductController::class, 'favouritesProduct']);
    Route::post('/favourites-product-list', [ProductController::class, 'favouritesProductList']);
    Route::post('/add-product-inquiries', [ProductController::class, 'addproductInquiries']);
    Route::post('/product-inquiry-list', [ProductController::class, 'productInquiryList']);
    Route::post('/update-product-inquiry-status', [ProductController::class, 'updateProductInquiryStatus']);
    Route::post('/home-inquiry-list', [ProductController::class, 'homeInquiryList']);
    Route::post('/delete-product', [ProductController::class, 'productDelete']);
    Route::post('/delete-property', [PropertyController::class, 'DeleteProperty']);
    Route::post('/delete-property-picture', [PropertyController::class, 'deletePropertyPicture']);
    Route::post('/my-property', [PropertyController::class, 'myProperty']);
    Route::post('/search-property', [PropertyController::class, 'searchProperty']);
    Route::post('/add-inquiry', [PropertyInquiryController::class, 'addInquiry']);
    Route::post('/inquiry-list', [PropertyInquiryController::class, 'inquiryList']);
    Route::post('/change-password',[UserController::class,'change_password']);
    Route::post('/delete-account', [UserController::class, 'deleteAccount']);
    Route::post('/add-edit-employee', [EmployeeController::class, 'addEditEmployee']);
    Route::post('/add-attendance-filter', [EmployeeController::class, 'addAttendanceFilter']);
    Route::post('/add-employee-attendance', [EmployeeController::class, 'addEmployeeAttendance']);
    Route::post('/view-attendance-filter', [EmployeeController::class, 'viewAttendanceFilter']);
    Route::post('/employee-details', [EmployeeController::class, 'employeeAttendanceDetails']);
    Route::post('/employee-list', [EmployeeController::class, 'employeeList']);
    Route::post('/delete-employee', [EmployeeController::class, 'deleteEmployee']);
    Route::post('/market-commodity-list', [MarketController::class, 'marketCommodityList']);
    Route::post('/get-market-api-data', [MarketController::class, 'getResponceData']);
    Route::post('/depedent-state-market', [MarketController::class, 'depedentStateMarket']);
    Route::post('/get-market-graph-data', [MarketController::class, 'getMarketGraphData']);
});

Route::get('/add-market-api-data', [MarketController::class, 'addApiData']);
Route::post('/market-state-list', [MarketController::class, 'marketStateList']);
Route::post('/market-district-list', [MarketController::class, 'marketDistrictList']);
Route::post('/market-taluka-list', [MarketController::class, 'marketTalukaList']);

Route::post('/user-state-list', [MarketController::class, 'UserStateList']);
Route::post('/user-district-list', [MarketController::class, 'UserDistrictList']);
Route::post('/user-taluka-list', [MarketController::class, 'UserTalukaList']);

/* Route::get('/test-add-market-api-data', [MarketController::class, 'testAddApiData']); */
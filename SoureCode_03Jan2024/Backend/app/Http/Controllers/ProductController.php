<?php

namespace App\Http\Controllers;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use App\Models\Product;
use App\Models\ProductAssets;
use App\Models\ProductCategory;
use App\Models\ProductFavourite;
use App\Models\ProductInquiry;
use App\Models\PropertyInquiry;
use App\Models\User;
use App\Models\UserNotification;
use DB;

class ProductController extends Controller
{
    public function addEditProduct(Request $request)
    {
        $userId = $request->header('userId');
        $editId = $request->edit_id;
        $validate = Validator::make($request->all(), [
            "category_id" => "required",
            "u_name" => "required",
            "u_mobile" => "required|numeric",
            "u_pincode" => "required|numeric",
            "profile_image" => "image|mimes:jpeg,jpg",
            "Latitude" => "required|numeric",
            "Longitude" => "required|numeric",
            "price" => "required|numeric",
        ]);
        if ($validate->fails()) {
            $message = $validate->errors()->first();
            if (!empty($message)) {
                return response()->json(["status" => false, "message" => $message]);
            }
        }
        $data['user_id'] = $userId;
        $data['category_id'] = $request->category_id;
        $data['title'] = $request->title;
        $data['description'] = $request->description;
        $data['price'] = $request->price;
        $data['u_name'] = $request->u_name;
        $data['u_mobile'] = $request->u_mobile;
        $data['u_pincode'] = $request->u_pincode;
        $data['latitude'] = $request->Latitude;
        $data['longitude'] = $request->Longitude;
        
        if($request->hasFile('product_assets')) {
            $pictures = $request->file('product_assets');
        }
        if($request->hasFile('profile_image')) {
            $profile_image = $request->file('profile_image');
        }
        if($editId){
            $product = Product::where('id',$editId)->update($data);
            if(!empty($pictures)){
                foreach($pictures as $file){
                    $destinationPath = public_path().'/product-images';
                    $filename = 'products-'.time().'-'.$file->getClientOriginalName();
                    $file->move($destinationPath, $filename);
                    $asset_url = config('app.url').'/product-images'.'/'.$filename;  
                    ProductAssets::create(['product_id' => $editId, 'asset_url' => $asset_url]);
                }
            }
            if(!empty($profile_image)){
                $destinationPath = public_path().'/product-user-profile';
                $filename = 'user-product-profile-'.time().'-'.$profile_image->getClientOriginalName();
                $profile_image->move($destinationPath, $filename);
                $asset_url = config('app.url').'product-user-profile'.'/'.$filename;
                Product::where('id',$editId)->update(['profile_image' => $asset_url]);
            }
            return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.PRODUCTUPDATE') ]);
        }else{
            $product = Product::create($data);
                if(!empty($pictures)){
                    foreach($pictures as $file){
                        $destinationPath = public_path().'/product-images';
                        $filename = 'products-'.time().'-'.$file->getClientOriginalName();
                        $file->move($destinationPath, $filename);
                        $asset_url = config('app.url').'/product-images'.'/'.$filename;
                        ProductAssets::create(['product_id' => $product->id, 'asset_url' => $asset_url]);
                    }
                }
                if(!empty($profile_image)){
                    $destinationPath = public_path().'/product-user-profile';
                    $filename = 'user-product-profile-'.time().'-'.$profile_image->getClientOriginalName();
                    $profile_image->move($destinationPath, $filename);
                    $asset_url = config('app.url').'/product-user-profile'.'/'.$filename;
                    Product::where('id', $product->id)->update(['profile_image' => $asset_url]);
                }
            return response()->json(["status" => true, "message" => config('constants.'.$request->header('languageId') .'.PRODUCTSAVED') ]);
        }
    }
    
    public function productList(Request $request)
    {
        $validate = Validator::make($request->all(), [
            'Limit' => 'nullable|numeric',
            'Page' => 'nullable|numeric',
        ]);
        if ($validate->fails()) {
            $errors = $validate->errors();
            if (!empty($errors)) {
                if ($errors->messages() && !empty($errors->messages())) {
                    return response()->json(["status" => false, "message" => $errors->messages()]);
                }
            }
        }
        $limit = (!empty($request->input('Limit'))) ? $request->input('Limit') : 10;
        $page = (!empty($request->input('Page'))) ? $request->input('Page') : 1;
        $offset = ceil($page - 1) * $limit;
        $userId = $request->header('userId');
        $latitude = $request->header('Latitude');
        $longitude = $request->header('Longitude');

        $result = Product::with('productassets', 'categoryData','productfavorite','userData')
        ->select('products.id', 'products.user_id', 'products.title', 'products.category_id' ,'products.description', 'products.price', 'products.u_name', 'products.u_mobile', 'products.u_pincode', 'products.profile_image')
        ->leftJoin("users",function($join) use ($userId){
            $join->on("users.id","=","products.user_id");
        })
        ->where("users.is_active", "=", 1);
        
        if($request->come_from == 'buy'){
            $result = $result->where('user_id', '!=', $userId)
            ->selectRaw(
                "(6371 * acos(cos(radians(?)) * cos(radians(products.latitude)) *
                cos(radians(products.longitude) - radians(?)) +
                sin(radians(?)) * sin(radians(products.latitude)))) AS distance",
                [$latitude, $longitude, $latitude]
            );
        } else {
            $result = $result->where('user_id', $userId);
        }

        if($request->product_id){
            $result = $result->where('id', '=', $request->product_id)->where('user_id', $userId);
        }
        if($request->category_id){
            $result = $result->where('category_id', '=', $request->category_id);
        }
        if($request->product_title){
            $str_name = "%$request->product_title%";
            $result = $result->where('title', 'like', $str_name);
        }
        $getCount = $result->count();
        $totalPage = ceil($getCount / $limit);
        
        if($request->come_from == 'buy'){
            $result = $result->orderBy('distance','Asc')->limit($limit)->offset($offset)->get();
        } else {
            $result = $result->orderBy('products.id','desc')->limit($limit)->offset($offset)->get();
        }

        if($result){
            foreach($result as $key => $value){
                $result[$key]['category_name'] = $value->categoryData->name;
                $check_fav_tag = DB::table('product_favourites')->where('product_favourites.product_id',$value->id)->where('product_favourites.user_id', $userId)->where('product_favourites.deleted_at',null)->first();
                $result[$key]['favourite_flag'] = $check_fav_tag != NULL ? 1 : 0;
                $result[$key]['username'] =  $value->userData == NULL ? NULL : $value->userData->first_name ." ".$value->userData->last_name;
                $result[$key]['phone_number'] =  $value->userData == NULL ? NULL : $value->userData->phone_number;
                $result[$key]['asset_url'] = $value->asset_url == NULL ? $value->userData->asset_url : '';
                unset($value->categoryData);
                unset($value->productfavorite);
                unset($value->userData);
            } 
        }
        if($result){
            return response()->json(['status' => true,'data' => $result, "message" => config('constants.'.$this->language .'.PRODUCTLIST'), 'totalpage' => $totalPage,'totalrecord' => $getCount]);
        }
    }
    
    public function categoryproductList(Request $request)
    {
        $userId = $request->header('userId');           
        $result = ProductCategory::orderBy('id')->get();
        if($result){
            return response()->json(['status' => true, 'data' => $result, "message" => config('constants.'.$this->language .'.PRODUCCATEGORYTLIST') ]);
        }
    }
    
    public function favouritesProduct(Request $request)
    {
        $validate = Validator::make($request->all(), [
            "product_id" => "required"
        ]);
        $userId = $request->header('userId');
        $product_exists = Product::where('id', $request->product_id)->orderBy('id','DESC')->first();
        if(!empty($product_exists)){
            $data['user_id'] = $userId;
            $data['product_id'] = $request->product_id;
            $checkLike = ProductFavourite::withTrashed()->where('user_id', $userId)->where('product_id', $request->product_id)->first();
            if($checkLike) {
                if($checkLike->deleted_at == null){ // Dislike
                    ProductFavourite::where('user_id', '=', $userId)->where('product_id', $request->product_id)->delete();
                    return response()->json(["status" => true, "message" => "Removed from favourites."]);
                } else {
                    ProductFavourite::withTrashed()->where('user_id', $userId)->where('product_id', $request->product_id)->update(['deleted_at' => null]);
                    return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.SUCESSFAVOURITE')]);
                }
            } else { // For Like and insert new value
                $data['deleted_at'] = NULL;
                $fav_product = ProductFavourite::create($data);
                return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.SUCESSFAVOURITE')]);
            }
        }else{
            return response()->json(["status" => false, "message" => config('constants.'.$this->language .'.PRODUCTEXISTS')]);
        }
    }
    
    public function favouritesProductList(Request $request)
    {
        $validate = Validator::make($request->all(), [
            'Limit' => 'nullable|numeric',
            'Page' => 'nullable|numeric',
        ]);
        if ($validate->fails()) {
            $errors = $validate->errors();
            if (!empty($errors)) {
                if ($errors->messages() && !empty($errors->messages())) {
                    return response()->json(["status" => false, "message" => $errors->messages()]);
                }
            }
        }
        $limit = (!empty($request->input('Limit'))) ? $request->input('Limit') : 10;
        $page = (!empty($request->input('Page'))) ? $request->input('Page') : 1;
        $offset = ceil($page - 1) * $limit;
        $userId = $request->header('userId');
        $result = ProductFavourite::with('productassets', 'categoryData','userData','productData')
                                ->select('products.id', 'product_favourites.user_id', 'products.title', 'product_categories.id as category_id' 
                ,'products.description', 'products.price', 'products.u_mobile', 'products.u_pincode', 'product_categories.name as category_name' ,
                'users.phone_number', 'users.profile_url as profile_image',
                DB::raw('CONCAT(users.first_name, users.last_name) AS u_name'))
                ->leftJoin("products",function($join) use ($userId){
                    $join->on("products.id","=","product_favourites.product_id");
                })
                ->leftJoin("product_categories",function($join) use ($userId){
                    $join->on("product_categories.id","=","products.category_id");
                })
                ->leftJoin("users",function($join) use ($userId){
                    $join->on("users.id","=","products.user_id");
                })
                ->where('product_favourites.user_id', $userId)
                ->orderBy('id','DESC')->limit($limit)->offset($offset)->get(); 
        $getCount = $result->count();
        $totalPage = ceil($getCount / $limit);
        foreach($result as $key => $value){
            $result[$key]['favourite_flag'] = $value->productfavorite = 1;
            unset($value->categoryData);
            unset($value->productfavorite);
            unset($value->userData);
            unset($value->productData);
        }
        if($result){
            return response()->json(['status' => true, 'data' => $result, "message" => config('constants.'.$this->language .'.FAVOURITEPRODUCT'), 'totalpage' => $totalPage,'totalrecord' => $getCount]);
        }
    }

    public function addproductInquiries(Request $request)
    {
        $validate = Validator::make($request->all(), [
            'product_id' => 'required|numeric'
        ]);
        if ($validate->fails()) {
            $errors = $validate->errors();
            if (!empty($errors)) {
                if ($errors->messages() && !empty($errors->messages())) {
                    return response()->json(["status" => false, "message" => $errors->messages()]);
                }
            }
        }
        $userId = $request->header('userId');           
        $result = Product::where('id',$request->product_id)->first();
        if(!$result){
            return response()->json(['status' => false, "message" => config('constants.'.$this->language .'.PRODUCTEXISTS') ]);
        }else{
            $postdata = $request->all();
            $result = Product::where('id',$request->product_id)->first();
            $data['user_id'] = $result['user_id'];
            $data['from_id'] = $request->header('userId');
            $data['product_id'] = $postdata['product_id'];
            $data['name'] = $postdata['name'];
            $data['email'] = $postdata['email'];
            $data['mobile_no'] = $postdata['mobile_no'];
            $data['quantity'] = $postdata['quantity'];
            $data = ProductInquiry::create($data);
            if($data){  
                $params_data['title'] = 'Product Inquiry.';
                $params_data['body'] = 'Inquiry Initiated of your Product.';
                $params_data['from_id'] = $result['user_id'];
                $params_data['to_id'] = $request->header('userId');
                $params_data['type'] = 'TIPS-NOTIFICTION-RECER';
                $url = url('/api/SendNotificationInBackground');
                $this->do_in_background($url, $params_data);
                return response()->json(['status' => true,'data' => $data,"message" => config('constants.'.$this->language .'.PRODUCTINQUIRY')]);
            }
        }
    }   
    
    public function productInquiryList(Request $request)
    {
        $validate = Validator::make($request->all(), [
            'Limit' => 'nullable|numeric',
            'Page' => 'nullable|numeric',
        ]);
        if ($validate->fails()) {
            $errors = $validate->errors();
            if (!empty($errors)) {
                if ($errors->messages() && !empty($errors->messages())) {
                    return response()->json(["status" => false, "message" => $errors->messages()]);
                }
            }
        }
        $limit = (!empty($request->input('Limit'))) ? $request->input('Limit') : 10;
        $page = (!empty($request->input('Page'))) ? $request->input('Page') : 1;
        $offset = ceil($page - 1) * $limit;
        $userId = $request->header('userId');
        $result = ProductInquiry::with('productassets', 'categoryData','userData','productData','productfavorite')
              ->select('products.id', 'product_inquiries.id as inquiry_id', 'product_inquiries.user_id' ,'product_inquiries.inquiry_status', 'product_inquiries.quantity', 'products.title', 'products.category_id as category_id','products.description', 'products.price', 'products.u_pincode','product_categories.name as category_name','users.phone_number as u_mobile', 'users.profile_url as profile_image', DB::raw('CONCAT(users.first_name, " ",users.last_name) AS u_name'))
                ->leftJoin("products",function($join) use ($userId){
                    $join->on("products.id","=","product_inquiries.product_id");
                })
                ->leftJoin("product_categories",function($join) use ($userId){
                    $join->on("product_categories.id","=","products.category_id");
                })
                ->join("users",function($join) use ($userId){
                    $join->on("users.id","=","product_inquiries.from_id");
                })
                ->where('product_inquiries.user_id', $userId)
                ->orderBy('inquiry_id','DESC')->limit($limit)->offset($offset)->get();      
                $getCount = $result->count();
                $totalPage = ceil($getCount / $limit);
                foreach($result as $key => $value){
                    $result[$key]['favourite_flag'] = $value->productfavorite != NULL ? 1 : 0;
                    unset($value->categoryData);
                    unset($value->productfavorite);
                    unset($value->userData);
                    unset($value->productData);
                }
        if($result){
            return response()->json(['status' => true,'data' => $result,"message" => config('constants.'.$this->language .'.PRODUCTINQUIRYLIST'),'totalpage' => $totalPage,'totalrecord' => $getCount]);
        }
    }
    
    public function updateProductInquiryStatus(Request $request)
    {
        if($request->inquiry_status == 1){
            $body = 'Your inquiry has been closed by seller.';
        }
        if($request->inquiry_status == 2){
            $body = 'Your inquiry has been completed by seller.';
        }
        $inquiry_user = ProductInquiry::select('from_id')->where('id', $request->inquiry_id)->first();
        $product = ProductInquiry::where('id',$request->inquiry_id)->update(['inquiry_status' => $request->inquiry_status]);
        
        $params_data['title'] = 'Inquiry Status.';
        $params_data['body'] = $body;
        $params_data['from_id'] = $inquiry_user->from_id;
        $params_data['to_id'] = $request->header('userId');
        $params_data['type'] = 'TIPS-NOTIFICTION-RECER';
        $url = url('/api/SendNotificationInBackground');
        $this->do_in_background($url, $params_data);

        if($request->inquiry_status == 1){
            return response()->json(['status' => true, "message" => config('constants.'.$this->language .'.INQUIRYCLOSED')]);
        }else{
            return response()->json(['status' => true, "message" => config('constants.'.$this->language .'.INQUIRYCOMPLETED')]);
        }
    }
    
    public function homeInquiryList(Request $request)
    {
        $userId = $request->header('userId');
        $result = ProductInquiry::with('productassets', 'categoryData','userData','productData','productfavorite')
       ->select('products.id', 'product_inquiries.id as inquiry_id', 'product_inquiries.user_id' ,'product_inquiries.inquiry_status', 'product_inquiries.quantity', 
'products.title', 'products.category_id as category_id','products.description',  'products.u_pincode', 'products.price', 'product_categories.name as category_name','users.phone_number as u_mobile', 'users.profile_url as profile_image', DB::raw('CONCAT(users.first_name, " ",users.last_name) AS u_name'))
        ->leftJoin("products",function($join) use ($userId){
            $join->on("products.id","=","product_inquiries.product_id");
        })
        ->leftJoin("product_categories",function($join) use ($userId){
            $join->on("product_categories.id","=","products.category_id");
        })
        ->join("users",function($join) use ($userId){
            $join->on("users.id","=","product_inquiries.from_id");
        })
        ->where('product_inquiries.user_id', $userId)
        ->orderBy('product_inquiries.id','DESC')->limit(2)->get();
                foreach($result as $key => $value){
                    $result[$key]['favourite_flag'] = $value->productfavorite = 1;
                    unset($value->categoryData);
                    unset($value->productfavorite);
                    unset($value->userData);
                    unset($value->productData);
                }
                $result2 = PropertyInquiry::select(
                    'property_inquiries.id',
                    'property_inquiries.property_id',
                    'property_inquiries.name',
                    'properties.*',
                    'users.phone_number', 'users.profile_url',
                     DB::raw('CONCAT(users.first_name, users.last_name) AS username')
                )
                ->join("users",function($join) use ($userId){
                    $join->on("users.id","=","property_inquiries.user_id");
                })
                ->join("properties",function($join) use ($userId){
                    $join->on("properties.id","=","property_inquiries.property_id");
                    $join->where("properties.user_id","=",$userId)->whereNull('properties.deleted_at');
                })
                ->orderBy('property_inquiries.id','DESC')->limit(2)->get();
            foreach($result2 as $key => $value){
                $propertyAssets = DB::table('property_assets')->where('property_id',$value->property_id)->get();
                $result2[$key]['propertyassets'] = $propertyAssets;
            }
            $data = [
                'Product_Inquiry' => $result,
                'Property_Inquiry' => $result2
            ];
            if($result || $result2 ){
                return response()->json(['status' => true, "message" => config('constants.'.$this->language .'.PRODUCTPROPERTY'), 'data' => $data ]);
            }
    }
    
    public function productDelete(Request $request)
    {
            $validate = Validator::make($request->all(), ["product_id" => "required"]);
            if ($validate->fails()) {
                $errors = $validate->errors();
                if (!empty($errors)) {
                    if ($errors->messages() && !empty($errors->messages())) {
                        return response()->json(["status" => false, "message" => $errors->messages()]);
                    }
                }
            }
            Product::where('id',$request->product_id)->delete();
            ProductAssets::where('product_id',$request->product_id)->delete();
            ProductInquiry::where('product_id',$request->product_id)->delete();
            return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.PRODUCTDELETED')]);
    }
}

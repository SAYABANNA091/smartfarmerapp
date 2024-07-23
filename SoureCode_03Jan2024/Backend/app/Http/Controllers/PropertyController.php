<?php

namespace App\Http\Controllers;
use Illuminate\Support\Facades\Validator;
use App\Models\PropertyType;
use App\Models\Property;
use App\Models\PropertyAssets;
use Illuminate\Http\Request;

class PropertyController extends Controller
{

    /*
     @ Function : Property Type List
     @ Date     : 03-04-2023
    */
    public function getPropertyTypes(Request $request)
    {
        $properTypes = PropertyType::get();
        if ($properTypes) {
            return response()->json(["status" => true, "data"=> $properTypes, "message" => config('constants.'.$this->language .'.GETSUCCESSFULLY')]);
        } else {
            return response()->json(["status" => false, "message" => config('constants.'.$this->language .'.RECORDNOTFOUND')]);
        }
    }

    /*
     @ Function : Add Edit Property
     @ Date     : 03-04-2023
    */
    public function addEditProperty(Request $request)
    {
        $validate = Validator::make($request->all(), [
            "property_type_id" => "required"
        ]);

        if ($validate->fails()) {
            $message = $validate->errors()->first();
            if (!empty($message)) {
                return response()->json(["status" => false, "message" => $message]);
            }
        }

        $userId = $request->header('userId');
        $propertyTypeId = $request->property_type_id;
        $editId = $request->edit_id;

        /* Type : TRACTOR */
        if($propertyTypeId == config('app.TRACTOR')){
            $data['user_id'] = $userId;
            $data['property_type_id'] =$propertyTypeId;
            $data['tractor_title'] = $request->tractor_title;
            $data['tractor_name'] = $request->tractor_name;
            $data['tractor_pincode'] = $request->tractor_pincode;
            $data['tractor_hourse_power'] = $request->tractor_hourse_power;
            $data['tractor_village_name'] = $request->tractor_village_name;
            $data['tractor_price_per_hour'] = $request->tractor_price_per_hour;
            $data['tractor_description'] = $request->tractor_description;
            $data['latitude'] = $request->Latitude;
            $data['longitude'] = $request->Longitude;

            if($editId){
                Property::where('id',$editId)->update($data);
                $this->AddPropertyAssets($request,$editId);
                return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.PROPERTYUPDATED')]);
            }else{
                $property = Property::create($data);
                $this->AddPropertyAssets($request,$property->id);
                return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.PROPERTYSAVED')]);
            }
        }

        /* Type : JCB */
        if($propertyTypeId == config('app.JCB')){
            $data['user_id'] = $userId;
            $data['property_type_id'] =$propertyTypeId;
            $data['jcb_title'] = $request->jcb_title;
            $data['jcb_name'] = $request->jcb_name;
            $data['jcb_pincode'] = $request->jcb_pincode;
            $data['jcb_hourse_power'] = $request->jcb_hourse_power;
            $data['jcb_village_name'] = $request->jcb_village_name;
            $data['jcb_price_per_hour'] = $request->jcb_price_per_hour;
            $data['jcb_description'] = $request->jcb_description;
            $data['latitude'] = $request->Latitude;
            $data['longitude'] = $request->Longitude;

            if($editId){
                Property::where('id',$editId)->update($data);
                $this->AddPropertyAssets($request,$editId);
                return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.PROPERTYUPDATED')]);

            }else{
                $property = Property::create($data);
                $this->AddPropertyAssets($request,$property->id);
                return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.PROPERTYSAVED')]);
            }
        }

        /* Type : FARM */
        if($propertyTypeId == config('app.FARM')){
            $data['user_id'] = $userId;
            $data['property_type_id'] =$propertyTypeId;
            $data['farm_name'] = $request->farm_name;
            $data['farm_title'] = $request->farm_title;
            $data['farm_no_of_acers'] = $request->farm_no_of_acers;
            $data['farm_survey_no'] = $request->farm_survey_no;
            $data['farm_villege_name'] = $request->farm_villege_name;
            $data['farm_pincode'] = $request->farm_pincode;
            $data['farm_description'] = $request->farm_description;
            $data['latitude'] = $request->Latitude;
            $data['longitude'] = $request->Longitude;

            if($editId){
                Property::where('id',$editId)->update($data);
                $this->AddPropertyAssets($request,$editId);
                return response()->json(["status" => true, "message" =>  config('constants.'.$this->language .'.PROPERTYUPDATED')]);

            }else{
                $property = Property::create($data);
                $this->AddPropertyAssets($request,$property->id);
                return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.PROPERTYSAVED')]);
            }
        }

        /* Type : NURSERY */
        if($propertyTypeId == config('app.NURSERY')){
            $data['user_id'] = $userId;
            $data['property_type_id'] =$propertyTypeId;
            $data['nursery_title'] = $request->nursery_title;
            $data['nursery_name'] = $request->nursery_name;
            $data['nursery_village_name'] = $request->nursery_village_name;
            $data['nursery_pincode'] = $request->nursery_pincode;
            $data['nursery_description'] = $request->nursery_description;
            $data['latitude'] = $request->Latitude;
            $data['longitude'] = $request->Longitude;

            if($editId){
                Property::where('id',$editId)->update($data);
                $this->AddPropertyAssets($request,$editId);
                return response()->json(["status" => true, "message" =>  config('constants.'.$this->language .'.PROPERTYUPDATED')]);

            }else{
                $property = Property::create($data);
                $this->AddPropertyAssets($request,$property->id);
                return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.PROPERTYSAVED')]);
            }
        }

        /* Type : VERMICOMPOST */
        if($propertyTypeId == config('app.VERMICOMPOST')){
            $data['user_id'] = $userId;
            $data['property_type_id'] =$propertyTypeId;
            $data['vermicompost_title'] = $request->vermicompost_title;
            $data['vermicompost_name'] = $request->vermicompost_name;
            $data['vermicompost_village_name'] = $request->vermicompost_village_name;
            $data['vermicompost_pincode'] = $request->vermicompost_pincode;
            $data['vermicompost_description'] = $request->vermicompost_description;
            $data['latitude'] = $request->Latitude;
            $data['longitude'] = $request->Longitude;
            
            if($editId){
                Property::where('id',$editId)->update($data);
                $this->AddPropertyAssets($request,$editId);
                return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.PROPERTYUPDATED')]);

            }else{
                $property = Property::create($data);
                $this->AddPropertyAssets($request,$property->id);
                return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.PROPERTYSAVED')]);
            }
        }
    }
    
    /*
     @ Function : Add Property Pictures(Add Edit Property Assets)
     @ Date     : 03-04-2023
    */
    public function AddPropertyAssets($request,$propertyId)
    {
        $allowedfileExtension=['pdf','jpg','png'];

        /* TRACTOR */
        if($request->property_type_id == config('app.TRACTOR') && $request->hasFile('tractor_assets')){
            $pictures = $request->file('tractor_assets');
        }

        /* JCB */
        if($request->property_type_id == config('app.JCB') && $request->hasFile('jcb_assets')){
            $pictures = $request->file('jcb_assets');
        }

        /* Farm */
        if($request->property_type_id == config('app.FARM') && $request->hasFile('farm_assets')){
            $pictures = $request->file('farm_assets');
        }

        /* NURSERY */
        if($request->property_type_id == config('app.NURSERY') && $request->hasFile('nursery_assets')){
            $pictures = $request->file('nursery_assets');
        }

        /* VERMICOMPOST */
        if($request->property_type_id == config('app.VERMICOMPOST') && $request->hasFile('vermicompost_assets')){
            $pictures = $request->file('vermicompost_assets');
        }
        
        if(!empty($pictures)){
            foreach($pictures as $file){
                $destinationPath = public_path().'/property-images' ;
                $filename = 'properties-'.time().'-'.$file->getClientOriginalName();
                $file->move($destinationPath,$filename);
                $asset_url = url('/property-images').'/'.$filename;
                PropertyAssets::create(['property_id' => $propertyId,'asset_url' => $asset_url]);
            }
        }
    }

    /*
     @ Function : My Property
     @ Date     : 03-04-2023
    */
    public function myProperty(Request $request)
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
        $result = Property::with('propertyassets')->where('user_id',$userId)->limit($limit)->orderBy('id','DESC')->offset($offset)->get();
        // $result = Property::with('propertyassets')->where('user_id',$userId)
        // ->leftJoin("users",function($join) use ($userId){
        //     $join->on("users.id","=","products.user_id");
        // })
        // ->where("users.is_active", "=", 1)
        // ->limit($limit)->orderBy('id','DESC')->offset($offset)->get();

        $totalResult = Property::with('propertyassets')->where('user_id',$userId)->orderBy('id','DESC')->count();

        $totalPage = ceil($totalResult / $limit);

        if($result){
            return response()->json(['status' => true,'data' => $result,"message" => config('constants.'.$this->language .'.PROPERTYSAVED'),'totalpage' => $totalPage,'totalrecord' => $totalResult]);
        }
    }

    public function searchProperty(Request $request)
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
        $userId = $request->header('userId');
        $latitude = $request->header('Latitude');
        $longitude = $request->header('Longitude');
        $search_keyword = $request->text;
        $property_type_id = $request->property_type_id;
        $limit = (!empty($request->input('Limit'))) ? $request->input('Limit') : 10;
        $page = (!empty($request->input('Page'))) ? $request->input('Page') : 1;
        $offset = ceil($page - 1) * $limit;
        
        if(!empty($search_keyword)){
            $result = Property::select(
                'properties.id',
                'properties.user_id',
                'properties.property_type_id',
                'properties.tractor_title',
                'properties.tractor_name',
                'properties.tractor_hourse_power',
                'properties.tractor_village_name',
                'properties.tractor_pincode',
                'properties.tractor_price_per_hour',
                'properties.tractor_description',
                'properties.jcb_title',
                'properties.jcb_name',
                'properties.jcb_pincode',
                'properties.jcb_hourse_power',
                'properties.jcb_village_name',
                'properties.jcb_price_per_hour',
                'properties.jcb_description',
                'properties.farm_title',
                'properties.farm_name',
                'properties.farm_no_of_acers',
                'properties.farm_survey_no',
                'properties.farm_villege_name',
                'properties.farm_pincode',
                'properties.farm_description',
                'properties.nursery_title',
                'properties.nursery_name',
                'properties.nursery_village_name',
                'properties.nursery_pincode',
                'properties.nursery_description',
                'properties.vermicompost_title',
                'properties.vermicompost_name',
                'properties.vermicompost_village_name',
                'properties.vermicompost_pincode',
                'properties.vermicompost_description',
            )
            ->with('userdetails','propertyassets')->where('user_id','!=',$userId)->where('tractor_name', 'LIKE', '%'.$search_keyword.'%')->orWhere('jcb_name', 'LIKE', '%' . $search_keyword . '%')->orWhere('farm_name', 'LIKE', '%' . $search_keyword . '%')->orWhere('nursery_name', 'LIKE', '%' . $search_keyword . '%')->orWhere('vermicompost_name', 'LIKE', '%' . $search_keyword . '%')
            ->selectRaw(
                "(6371 * acos(cos(radians(?)) * cos(radians(properties.latitude)) *
                cos(radians(properties.longitude) - radians(?)) +
                sin(radians(?)) * sin(radians(properties.latitude)))) AS distance",
                [$latitude, $longitude, $latitude]
            )->limit($limit)->orderBy('id','DESC')->offset($offset)->get();
            
            $totalResult = Property::with('userdetails','propertyassets')->where('user_id','!=',$userId)->where('tractor_name', 'LIKE', '%'.$search_keyword.'%')->orWhere('jcb_name', 'LIKE', '%' . $search_keyword . '%')->orWhere('farm_name', 'LIKE', '%' . $search_keyword . '%')->orWhere('nursery_name', 'LIKE', '%' . $search_keyword . '%')->orWhere('vermicompost_name', 'LIKE', '%' . $search_keyword . '%')->count();
            
        }else if(!empty($property_type_id)){
            $where['property_type_id'] = $property_type_id;
            if($property_type_id == config('app.TRACTOR')){
                $fileName = 'tractor_name';
            }else if($property_type_id == config('app.JCB')){
                $fileName = 'jcb_name';
            }else if($property_type_id == config('app.FARM')){
                $fileName = 'farm_name';
            }else if($property_type_id == config('app.NURSERY')){
                $fileName = 'nursery_name';
            }else if($property_type_id == config('app.VERMICOMPOST')){
                $fileName = 'vermicompost_name';
            }
            $result = Property::select(
                'properties.id',
                'properties.user_id',
                'properties.property_type_id',
                'properties.tractor_title',
                'properties.tractor_name',
                'properties.tractor_hourse_power',
                'properties.tractor_village_name',
                'properties.tractor_pincode',
                'properties.tractor_price_per_hour',
                'properties.tractor_description',
                'properties.jcb_title',
                'properties.jcb_name',
                'properties.jcb_pincode',
                'properties.jcb_hourse_power',
                'properties.jcb_village_name',
                'properties.jcb_price_per_hour',
                'properties.jcb_description',
                'properties.farm_title',
                'properties.farm_name',
                'properties.farm_no_of_acers',
                'properties.farm_survey_no',
                'properties.farm_villege_name',
                'properties.farm_pincode',
                'properties.farm_description',
                'properties.nursery_title',
                'properties.nursery_name',
                'properties.nursery_village_name',
                'properties.nursery_pincode',
                'properties.nursery_description',
                'properties.vermicompost_title',
                'properties.vermicompost_name',
                'properties.vermicompost_village_name',
                'properties.vermicompost_pincode',
                'properties.vermicompost_description',
            )
            ->with('userdetails', 'propertyassets')
            ->where($fileName, 'LIKE', '%' . $search_keyword . '%')
            ->where($where)
            ->where('user_id', '!=', $userId)
            ->selectRaw(
                "(6371 * acos(cos(radians(?)) * cos(radians(properties.latitude)) *
                cos(radians(properties.longitude) - radians(?)) +
                sin(radians(?)) * sin(radians(properties.latitude)))) AS distance",
                [$latitude, $longitude, $latitude]
            )
            ->limit($limit)
            ->offset($offset)
            ->orderBy('distance', 'ASC')
            ->get();
            $totalResult = Property::with('userdetails','propertyassets')->where($fileName, 'LIKE', '%'.$search_keyword.'%')->where($where)->where('user_id','!=',$userId)->count();
        }else{
            $result = Property::select(
                'properties.id',
                'properties.user_id',
                'properties.property_type_id',
                'properties.tractor_title',
                'properties.tractor_name',
                'properties.tractor_hourse_power',
                'properties.tractor_village_name',
                'properties.tractor_pincode',
                'properties.tractor_price_per_hour',
                'properties.tractor_description',
                'properties.jcb_title',
                'properties.jcb_name',
                'properties.jcb_pincode',
                'properties.jcb_hourse_power',
                'properties.jcb_village_name',
                'properties.jcb_price_per_hour',
                'properties.jcb_description',
                'properties.farm_title',
                'properties.farm_name',
                'properties.farm_no_of_acers',
                'properties.farm_survey_no',
                'properties.farm_villege_name',
                'properties.farm_pincode',
                'properties.farm_description',
                'properties.nursery_title',
                'properties.nursery_name',
                'properties.nursery_village_name',
                'properties.nursery_pincode',
                'properties.nursery_description',
                'properties.vermicompost_title',
                'properties.vermicompost_name',
                'properties.vermicompost_village_name',
                'properties.vermicompost_pincode',
                'properties.vermicompost_description',
            )
            ->with('userdetails','propertyassets')->where('user_id','!=',$userId)
            ->selectRaw(
                "(6371 * acos(cos(radians(?)) * cos(radians(properties.latitude)) *
                cos(radians(properties.longitude) - radians(?)) +
                sin(radians(?)) * sin(radians(properties.latitude)))) AS distance",
                [$latitude, $longitude, $latitude]
            )
            ->limit($limit)->offset($offset)->orderBy('distance', 'ASC')->get();
            $totalResult = Property::with('userdetails','propertyassets')->where('user_id','!=',$userId)->count();
        }
        $totalPage = ceil($totalResult / $limit);
        return response()->json(['status' => true,'data' => $result,'totalpage' => $totalPage,'totalrecord' => $totalResult]);
    }

    /*
     @ Function : Delete Property picture
     @ Date     : 03-04-2023
    */
    public function deletePropertyPicture(Request $request)
    {
        $validate = Validator::make($request->all(), ["id" => "required"]);
        $type = $request->type ?? "";
        if ($validate->fails()) {
            $errors = $validate->errors();
            if (!empty($errors)) {
                if ($errors->messages() && !empty($errors->messages())) {
                    return response()->json(["status" => false, "message" => $errors->messages()]);
                }
            }
        }
        if ($type == 1) {
             PropertyAssets::where('id', $request->id)->delete();
        } else {
             ProductAssets::where('id', $request->id)->delete();
        }
       
        return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.PROPERTYDELETED')]);
    }

    /*
     @ Function : Delete Property
     @ Date     : 03-04-2023
    */
    public function DeleteProperty(Request $request)
    {
        $validate = Validator::make($request->all(), ["id" => "required"]);

        if ($validate->fails()) {
            $errors = $validate->errors();
            if (!empty($errors)) {
                if ($errors->messages() && !empty($errors->messages())) {
                    return response()->json(["status" => false, "message" => $errors->messages()]);
                }
            }
        }
        Property::where('id',$request->id)->delete();
        PropertyAssets::where('property_id',$request->id)->delete();
        return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.PROPERTYDELETED')]);
    }
}

<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use App\Models\MarketState;
use App\Models\MarketCommodity; 
use App\Models\MarketDistrict; 
use App\Models\MarketRecord; 
use App\Models\MarketData; 
use App\Models\MarketTaluka; 
use App\Models\UserState; 
use App\Models\UserDistrict; 
use App\Models\UserTaluka; 


use Carbon\Carbon;
use DB;


class MarketController extends Controller
{
    public function marketStateList(Request $request)
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

        $states_list = MarketState::orderBy('name', 'ASC')->limit($limit)->offset($offset)->get();
        $getCount = MarketState::count();
        $totalPage = ceil($getCount / $limit);
        return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.EMPLOYEESTATES'), "data"=>$states_list ,'totalpage' => $totalPage,'totalrecord' => $getCount]);
    }

    public function UserStateList(Request $request)
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

        $states_list = UserState::orderBy('name', 'ASC')->limit($limit)->offset($offset)->get();
        $getCount = UserState::count();
        $totalPage = ceil($getCount / $limit);
        return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.EMPLOYEESTATES'), "data"=>$states_list ,'totalpage' => $totalPage,'totalrecord' => $getCount]);
    }

    public function marketDistrictList(Request $request)
    {
        $validate = Validator::make($request->all(), [
            'Limit' => 'nullable|numeric',
            'Page' => 'nullable|numeric',
            'states_id' => 'required|numeric',
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
        $district_list = MarketDistrict::orderBy('name', 'ASC')->where('states_id', $request->states_id)->limit($limit)->offset($offset)->get();
        $getCount = MarketDistrict::count();
        $totalPage = ceil($getCount / $limit);
        return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.EMPLOYEEDISTRICTS'), "data"=>$district_list ,'totalpage' => $totalPage,'totalrecord' => $getCount]);
    }

    public function UserDistrictList(Request $request)
    {
        $validate = Validator::make($request->all(), [
            'Limit' => 'nullable|numeric',
            'Page' => 'nullable|numeric',
            'states_id' => 'required|numeric',
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
        $district_list = UserDistrict::orderBy('name', 'ASC')->where('states_id', $request->states_id)->limit($limit)->offset($offset)->get();
        $getCount = UserDistrict::count();
        $totalPage = ceil($getCount / $limit);
        return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.EMPLOYEEDISTRICTS'), "data"=>$district_list ,'totalpage' => $totalPage,'totalrecord' => $getCount]);
    }

    public function marketTalukaList(Request $request)
    { 
        $validate = Validator::make($request->all(), [
            'Limit' => 'nullable|numeric',
            'Page' => 'nullable|numeric',
            'districts_id' => 'required|numeric',
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
        $taluka_list = MarketTaluka::orderBy('name', 'ASC')->where('districts_id', $request->districts_id)->limit($limit)->offset($offset)->get();
        $getCount = MarketTaluka::count();
        $totalPage = ceil($getCount / $limit);
        return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.EMPLOYEETALUKAS'), "data"=> $taluka_list ,'totalpage' => $totalPage,'totalrecord' => $getCount]);
    }

    public function UserTalukaList(Request $request)
    { 
        $validate = Validator::make($request->all(), [
            'Limit' => 'nullable|numeric',
            'Page' => 'nullable|numeric',
            'districts_id' => 'required|numeric',
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
        $taluka_list = UserTaluka::orderBy('name', 'ASC')->where('districts_id', $request->districts_id)->limit($limit)->offset($offset)->get();
        $getCount = UserTaluka::count();
        $totalPage = ceil($getCount / $limit);
        return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.EMPLOYEETALUKAS'), "data"=> $taluka_list ,'totalpage' => $totalPage,'totalrecord' => $getCount]);
    }

    public function marketCommodityList(Request $request)
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
        $commodity_list = MarketCommodity::orderBy('name', 'ASC')->limit($limit)->offset($offset)->get();
        $getCount = MarketCommodity::count();
        $totalPage = ceil($getCount / $limit);
        return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.EMPLOYEECOMMODITY'), "data"=>$commodity_list ,'totalpage' => $totalPage,'totalrecord' => $getCount]);
    }
    
    /*
        @ Function : Market Response With Filter
        @ Date : 23-05-2023
        @ Author : KBA System
    */
    public function getResponceData(Request $request)
    {   
        $limit = (!empty($request->input('Limit'))) ? $request->input('Limit') : 10;
        $page = (!empty($request->input('Page'))) ? $request->input('Page') : 1;
        $offset = ($page - 1) * $limit;
		
		$final_json = DB::select("SELECT final_json FROM final_json_data ORDER BY id DESC LIMIT 1");
        /* $data_list = MarketRecord::select('market_json')->orderBy('id','DESC')->first(); */
        /* $data = json_decode($data_list['market_json'], true);
        $records = $data['records'];
        $getCount = count($records); */
		$records = json_decode($final_json[0]->final_json);
        /* $final_data = array_splice($records, $offset, $limit); */
        $final_data = $records;
        if($request->state){
            $final_data = array_filter($final_data, function ($record) use ($request) {     
                return strtolower($record->state) == strtolower($request->state);
            }); 
        }
        if($request->market){ 
            $final_data = array_filter($final_data, function ($record) use ($request) {     
                return $record->market == $request->market;
            }); 
        }
        if($request->state && $request->market){
            $final_data = array_filter($final_data, function ($record) use ($request) {
                return $record->state == $request->state && $record->market == $request->market;
            });
        }
        if($request->search_text){
            $final_data = array_filter($final_data, function($record) use ($request) {
                return preg_match("/$request->search_text/i", $record->commodity);
            });
        }
        if($request->commodity){
            $final_data = array_filter($final_data, function ($record) use ($request) {
                return $record->commodity == $request->commodity;
            }); 
        }
		$getCount = count($final_data);
		$final_data = array_splice($final_data, $offset, $limit);
        $total_pages = ceil($getCount / $limit);
        $final_data = array_values($final_data);
		if(empty($final_data))
		{
			/* $previous_date = date('Y-m-d', strtotime('-1 day', strtotime(date('Y-m-d')))); */
			/* $get_total_json = DB::select("SELECT DATE(imported_date) as get_dates FROM final_json_data WHERE DATE(imported_date) != '".date('Y-m-d')."' ORDER BY imported_date DESC LIMIT 6"); */
			$get_total_json = DB::select("SELECT DATE(imported_date) as get_dates FROM final_json_data ORDER BY imported_date DESC LIMIT 7");

            foreach($get_total_json as $key => $val)
			{   
			// 	/* echo "SELECT final_json FROM final_json_data WHERE DATE(imported_date) = '".$val->get_dates."'"; */
                $final_json = DB::select("SELECT final_json FROM final_json_data WHERE DATE(imported_date) = '".$val->get_dates."'");
				$records = json_decode($final_json[0]->final_json);
                $final_data = $records;
				if(!empty($final_data))
				{
                    if($request->state){
                        $final_data = array_filter($final_data, function ($record) use ($request) { 
                            return strtolower($record->state) == strtolower($request->state);
                        }); 
                    }
                    if($request->market){ 
                        $final_data = array_filter($final_data, function ($record) use ($request) {     
                            return $record->market == $request->market;
                        }); 
                    }
                    if($request->state && $request->market){
                        $final_data = array_filter($final_data, function ($record) use ($request) {
                            return $record->state == $request->state && $record->market == $request->market;
                        });
                    }
                    if($request->search_text){
                        $final_data = array_filter($final_data, function($record) use ($request) {
                            return preg_match("/$request->search_text/i", $record->commodity);
                        });
                    }
                    if($request->commodity){
                        $final_data = array_filter($final_data, function ($record) use ($request) {
                            return $record->commodity == $request->commodity;
                        }); 
                    }
                    $getCount = count($final_data);
                    $final_data = array_splice($final_data, $offset, $limit);
                    $total_pages = ceil($getCount / $limit);
                    $final_data = array_values($final_data);
                    
                    if(count($final_data) > 0)
                    { 
                        break;
                    }
				}
			}
		}
        return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.EMPLOYEEMARKETS'), "data"=> $final_data , 'totalpage' => $total_pages, 'totalrecord' => $getCount]);
    }

    public function depedentStateMarket(Request $request)
    {
        $validate = Validator::make($request->all(), ["stateName" => "required"]);
            if ($validate->fails()) {
                $errors = $validate->errors();
                if (!empty($errors)) {
                    if ($errors->messages() && !empty($errors->messages())) {
                        return response()->json(["status" => false, "message" => $errors->messages()]);
                    }
                }
            }
        $state_id = MarketState::select('id')->where('name', $request->stateName)->first();
        $state_data = MarketData::where('state_id', $state_id->id)->orderBy('name', 'ASC')->get();
        return response()->json([ "status" => true,  "message" => config('constants.'.$this->language .'.EMPLOYEEMARKETS'), "data"=> $state_data ]);
    }

     public function getMarketGraphData(Request $request)
    {
        $validate = Validator::make($request->all(), ["stateName" => "required", "commodityName" => "required", "marketName" => "required", "varietyName" => "required" ]);
        if ($validate->fails()) {
            $errors = $validate->errors();
            if (!empty($errors)) {
                if ($errors->messages() && !empty($errors->messages())) {
                    return response()->json(["status" => false, "message" => $errors->messages()]);
                }
            }
        }

        $weekRecord = MarketRecord::select('market_json', 'impoted_date')->orderBy('id', 'DESC')->limit(7)->get();
        $stateName = $request->stateName;
        $commodityName = $request->commodityName;
        $varietyName = $request->varietyName;
        $marketName = $request->marketName;
        $freshArray = [];
        $counter = 0;

        foreach ($weekRecord as $record) {
            $data = json_decode($record->market_json, true);
            $records = $data['records'];

            foreach ($records as $bkey => $bvalue) {
                if (
                    $bvalue['state'] == $stateName &&
                    $bvalue['commodity'] == $commodityName &&
                    $bvalue['variety'] == $varietyName &&
                    $bvalue['market'] == $marketName
                ) {
                    $bvalue['impoted_date'] = $record->impoted_date;
                    $freshArray[] = $bvalue;
                    $counter++;
                    if ($counter === 7) {
                        break 2;
                    }
                }
            }
        }
        return response()->json([ "status" => true, "data"=> $freshArray ]);
    }

    
	public function addApiData(Request $request)
    { 
        //DB::table('cron_test')->insert(['coming'=>1]);
        // https://testupdates.com/Soni/Backend/api/add-market-api-data


        $url = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd0000018a9aeb6728e64b437ab51e02a4e5aed6&format=json&offset=0&limit=10000";
        $ch = curl_init();
        curl_setopt($ch,CURLOPT_URL,$url);
        curl_setopt($ch,CURLOPT_RETURNTRANSFER,1);
        // curl_setopt($ch,CURLOPT_CONNECTTIMEOUT, 4);
        $json = curl_exec($ch);
        if(!$json) {
            echo curl_error($ch);
        }
        curl_close($ch);
        $decoded_data = json_decode($json, true);      
        $json_data = $decoded_data['records'];
        $last_data = DB::table('market_commodities')->select('name')->orderBy('id','DESC')->get();
        $last_data = json_decode( json_encode($last_data), true);
        $updated_impoted_date = substr($decoded_data['updated_date'], 0, 10);
       
        /* End : Insert Master Entry for State & Market */
        $last_data = MarketRecord::select('impoted_date')->orderBy('id','DESC')->first();
        $last_impoted_date = substr($last_data['impoted_date'], 0, 10);
        if((date('Y-m-d') > $last_impoted_date) && $last_impoted_date != $updated_impoted_date){ 
            DB::table('market_records')->insert(['market_json'=> $json, 'impoted_date' => $updated_impoted_date ]);
        }
        if($last_impoted_date == $updated_impoted_date){ 
            DB::table('market_records')->where('impoted_date', $updated_impoted_date)->update(['market_json'=> $json, 'impoted_date' => $updated_impoted_date]);
        }
		
		$lastRecord = MarketRecord::orderBy('id', 'desc')->first(); 
        $results = MarketRecord::where('id', '<', $lastRecord->id)
                ->orderBy('id', 'DESC')
                ->limit(7)
                ->get();
        
        foreach ($results as $key => $value) {
            $previousData[$key] =  json_decode($value['market_json'], true);
        }
        foreach($previousData as $key => $value){
            $previous[] =  $value['records'];
        }
        $previousData = array();
        foreach ($previous as $subArray) {
            $previousData = array_merge($previousData, $subArray);
        }
        $data_list = MarketRecord::select('market_json')->orderBy('id','DESC')->first();
        $data = json_decode($data_list['market_json'], true);
        $records = $data['records'];
        $getCount = count($records);
        foreach($records as $key => $current){
            $difference = 0;
            foreach($previousData as $previous){
                if($current['state'] == $previous['state'] && $current['district'] == $previous['district'] && $current['market'] == $previous['market'] && $current['variety'] == $previous['variety'] &&  $current['commodity'] == $previous['commodity']){  
                    $difference = $current['modal_price'] - $previous['modal_price'];
                    break;
                }
            }
            $records[$key]['compare_rate'] = $difference;
        }
        usort($records, function($a, $b) {
            return strcmp($a['commodity'], $b['commodity']);
        });
        $final_data = $records;
        
        $final_data = array_values($final_data);
		/* DB::table('final_json_data')->insert(['final_json'=> json_encode($final_data)]); */
        // $last_data =  DB::table('final_json_data')->select('imported_date')->orderBy('id','DESC')->first();
        $result = DB::table('final_json_data')
        ->select('imported_date')
        ->orderBy('id', 'DESC')
        ->first();
        $last_impoted_date = $result->imported_date;
        $last_impoted_date = substr($last_impoted_date, 0, 10);

        if((date('Y-m-d') > $last_impoted_date) && $last_impoted_date != $updated_impoted_date){  
		    DB::table('final_json_data')->insert(['final_json'=> json_encode($final_data), 'imported_date' => $updated_impoted_date ]);
        }else{  
		    DB::table('final_json_data')->where('imported_date', $updated_impoted_date)->update(['final_json'=> json_encode($final_data), 'imported_date' => $updated_impoted_date ]);
        }

        /* Start : Delete data before week */
            $record = DB::table('market_records')->orderBy('id', 'DESC')->get();
            foreach($record as $key => $value){
                if($key > 6){
                    DB::table('market_records')->where('id',$value->id)->delete();
                }
            }
            
            $fjrecord = DB::table('final_json_data')->orderBy('id', 'DESC')->get();
            foreach($fjrecord as $key => $value){
                if($key > 6){
                    DB::table('final_json_data')->where('id',$value->id)->delete();
                }
            }
        /* End : Delete data before week */
		echo json_encode($final_data);exit;
    }
}
<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use App\Models\Employee;
use App\Models\EmployeeAttendance;

class EmployeeController extends Controller
{
    /*
     @ Function : Add Edit Employee
     @ Date     : 05-04-2023
    */
    public function addEditEmployee(Request $request)
    {
        $validate = Validator::make($request->all(), [
            "employee_name" => "required"
        ]);

        if ($validate->fails()) {
            $errors = $validate->errors();
            if (!empty($errors)) {
                if ($errors->messages() && !empty($errors->messages())) {
                    return response()->json(["status" => false, "message" => $errors->messages()]);
                }
            }
        }

        $editId = $request->edit_id;

        $data['employee_name'] = $request->employee_name;
        $data['employee_id'] = $request->employee_id;
        $data['mobile_no'] = $request->mobile_no;
        $data['employee_email'] = $request->employee_email;
        $data['department'] = $request->department;
        $data['created_by'] = $request->header('userId');

        $pictures = $request->file('profile_picture');
        if(!empty($pictures)){
            $destinationPath = public_path().'/employee-profile-picture' ;
            $filename = 'emp-picture-'.time().'-'.$pictures->getClientOriginalName();
            $pictures->move($destinationPath,$filename);
            $asset_url = url('/employee-profile-picture').'/'.$filename;
            $data['profile_picture'] = $asset_url;
        }

        if($editId){
            /* Unlink File */
                $existFile = Employee::where('id',$editId)->first();
                $filePath = public_path('employee-profile-picture').'/'.basename($existFile->profile_picture);
                // if(file_exists($filePath)){
                //     unlink($filePath);
                // }
            /* Update Employee */
            Employee::where('id',$editId)->update($data);
            return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.EMPLOYEEUPDATED') ]);
        }else{
            $data['created_at'] = date('Y-m-d H:i:s');
            /* Add Employee */
            $employee = Employee::create($data);
            return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.EMPLOYEEADDED') ]);
        }
    }

    /*
     @ Function : Add Employee Attendence
     @ Date     : 05-04-2023
    */
    public function addEmployeeAttendance(Request $request)
    {
        $validate = Validator::make($request->all(), [
            "attend_date" => "required"
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
        
        if(!empty($request->employees_added)){
            $employeesAddedIds = $request->employees_added;
            $addedIds = explode(',',$employeesAddedIds);
            foreach($addedIds as $key => $value){
                EmployeeAttendance::create(['employee_id'=>$value,'attend_date'=>$request->attend_date,'created_by' => $userId, 'created_at' => date('Y-m-d')]);
            }
        }

        if(!empty($request->employees_removed)){
            $employeesRemovedIds = $request->employees_removed;
            $removeddIds = explode(',',$employeesRemovedIds);
            foreach($removeddIds as $key => $value){
                EmployeeAttendance::where(['employee_id'=>$value,'attend_date'=>$request->attend_date,'created_by' => $userId])->delete();
            }
        }

        return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.EMPLOYEEATTENDANCE') ]);
    }

     /*
     @ Function : Add Attendence Filter
     @ Date     : 05-04-2023
    */
    public function addAttendanceFilter(Request $request)
    {
        $validate = Validator::make($request->all(), [
            "attend_date" => 'required|date|before:tomorrow',
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

        if($request->employee_name){
            $employees = Employee::where('employee_name', 'LIKE','%'.$request->employee_name.'%')->where('created_by',$userId)->limit($limit)->offset($offset)->get();
            $totalResult = Employee::where('employee_name', 'LIKE','%'.$request->employee_name.'%')->where('created_by',$userId)->count();
        }else{
            $employees = Employee::limit($limit)->where('created_by',$userId)->offset($offset)->get();
            $totalResult = Employee::where('created_by',$userId)->count();
        }

        foreach($employees as $key => $value){
            $isExist = EmployeeAttendance::where('employee_id',$value->id)->whereDate('attend_date',$request->attend_date)->count();
            if($isExist > 0){
                $employees[$key]['is_attend'] = 1;
            }else{
                $employees[$key]['is_attend'] = 0;
            }
        }
        
        $total_page = ceil($totalResult / $limit);

        return response()->json(["status" => true, "data"=>$employees, 'totalpage' => $total_page,'totalrecord' => $totalResult]);

    }


    /*
     @ Function : View Attendance Filter
     @ Date     : 10-04-2023
    */
    public function viewAttendanceFilter(Request $request)
    {
        $validate = Validator::make($request->all(), [
            "from_date" => 'required',
            "to_date" => 'required|date|before:tomorrow',
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

        $employessIds = $request->employee_ids;
        $from = $request->from_date;
        $to = $request->to_date;

        $empIdArray = explode(',',$employessIds);

        $employees = Employee::whereIn('id',$empIdArray)->limit($limit)->offset($offset)->get();
        $totalResult = Employee::whereIn('id',$empIdArray)->count();

        foreach($employees as $key => $value){
            $presetnCount = EmployeeAttendance::where('employee_id',$value->id)->whereBetween('attend_date', [$from, $to])->count();
            if($presetnCount > 0){
                $employees[$key]['present_days'] = $presetnCount;
            }else{
                $employees[$key]['present_days'] = 0;
            }
        }
        
        $total_page = ceil($totalResult / $limit);

        return response()->json(["status" => true, "data"=>$employees, 'totalpage' => $total_page,'totalrecord' => $totalResult]);
    }


    /*
     @ Function : View Attendance Filter
     @ Date     : 10-04-2023
    */
    public function employeeAttendanceDetails(Request $request)
    {
        $validate = Validator::make($request->all(), [
            "employee_id" => 'required',
            "month" => 'required',
            "year" => 'required'
        ]);

        if ($validate->fails()) {
            $errors = $validate->errors();
            if (!empty($errors)) {
                if ($errors->messages() && !empty($errors->messages())) {
                    return response()->json(["status" => false, "message" => $errors->messages()]);
                }
            }
        }

        $empDetails = Employee::where('id',$request->employee_id)->first();

        $monthDate = $request->year.'-'.$request->month.'-'.'01';
        $from = date("Y-m-01", strtotime($monthDate));
        $to = date("Y-m-t", strtotime($monthDate));


        if($empDetails){
            $result = [];
            $result['employee_details'] = $empDetails;
            $result['employee_details']['attendance'] = EmployeeAttendance::where('employee_id',$request->employee_id)->whereBetween('attend_date', [$from, $to])->get();

            return response()->json(["status" => true, "data"=>$result]);
        }else{
            return response()->json(["status" => false, "message" => config('constants.'.$this->language .'.USERNOTEXIST') ]);
        }
    }


     /*
     @ Function : employee List
     @ Date     : 12-04-2023
    */
    public function employeeList(Request $request)
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
        $userId = $request->header('userId');

        $offset = ceil($page - 1) * $limit;

        $employees = Employee::where('created_by',$userId)->limit($limit)->offset($offset)->get();
        $totalResult = Employee::where('created_by',$userId)->count();

        $total_page = ceil($totalResult / $limit);

        return response()->json(["status" => true, "data"=>$employees, 'totalpage' => $total_page,'totalrecord' => $totalResult]);

    }


        /*
     @ Function : Delete Employee
     @ Date     : 17-04-2023
    */
    public function deleteEmployee(Request $request)
    {
        $validate = Validator::make($request->all(), ["employee_id" => "required"]);

        if ($validate->fails()) {
            $message = $validate->errors()->first();
            if (!empty($message)) {
                return response()->json(["status" => false, "message" => $message]);
            }
        }

        Employee::where(['id'=>$request->employee_id])->delete();
        EmployeeAttendance::where(['employee_id'=>$request->employee_id])->delete();

        return response()->json(["status" => true, "message" => config('constants.'.$this->language .'.EMPLOYEEDELETED') ]);
    }


}


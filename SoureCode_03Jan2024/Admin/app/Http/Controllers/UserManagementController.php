<?php

namespace App\Http\Controllers;

use App\Models\User;
use Illuminate\Http\Request;
use Yajra\DataTables\Facades\DataTables;
use Illuminate\Support\Str;
use DB;

class UserManagementController extends Controller
{
    public function index()
    {
        $data = User::where('deleted_at', null)->where('user_type', 1)->orderBy('id', 'DESC')->get();
        return view('pages.User.index');
    }

    public function userList(Request $request)
    {
        if ($request->ajax()) {
            $data = User::where('deleted_at', null)->where('user_type', 2)->where('is_active', '!=', 2)->orderBy('id', 'DESC')->get();

            return DataTables::of($data)
                ->addIndexColumn()
                ->filter(function ($instance) use ($request) {
                    if (!empty($request->get('search'))) {
                        $instance->collection = $instance->collection->filter(function ($row) use ($request) {
                            if (Str::contains(Str::lower($row['first_name']), Str::lower($request->get('search')))) {
                                return true;
                            } elseif (Str::contains(Str::lower($row['last_name']), Str::lower($request->get('search')))) {
                                return true;
                            } elseif (Str::contains(Str::lower($row['email']), Str::lower($request->get('search')))) {
                                return true;
                            } elseif (Str::contains(Str::lower($row['phone_number']), Str::lower($request->get('search')))) {
                                return true;
                            } elseif (Str::contains(Str::lower($row['state']), Str::lower($request->get('search')))) {
                                return true;
                            } elseif (Str::contains(Str::lower($row['district']), Str::lower($request->get('search')))) {
                                return true;
                            } elseif (Str::contains(Str::lower($row['taluka']), Str::lower($request->get('search')))) {
                                return true;
                            } elseif (Str::contains(Str::lower($row['village']), Str::lower($request->get('search')))) {
                                return true;
                            }  elseif (Str::contains(Str::lower($row['pincode']), Str::lower($request->get('search')))) {
                                return true;
                            }
                        });
                    }
                })
                ->addColumn('first_name', function ($row) {

                    $btn = isset($row->first_name) ? $row->first_name : '-';

                    return $btn;
                })
                ->addColumn('last_name', function ($row) {

                    $btn = isset($row->last_name) ? $row->last_name : '-';

                    return $btn;
                })
                ->addColumn('last_name', function ($row) {

                    $btn = isset($row->last_name) ? $row->last_name : '-';

                    return $btn;
                })
                ->addColumn('email', function ($row) {

                    $btn = isset($row->email) ? $row->email : '-';

                    return $btn;
                })
                ->addColumn('phone', function ($row) {

                    $btn = isset($row->phone_number) ? $row->phone_number : '-';

                    return $btn;
                })
                ->addColumn('state', function ($row) {

                    $btn = isset($row->state) ? $row->state : '-';

                    return $btn;
                })
                ->addColumn('district', function ($row) {

                    $btn = isset($row->district) ? $row->district : '-';

                    return $btn;
                })
                ->addColumn('taluka', function ($row) {

                    $btn = isset($row->taluka) ? $row->taluka : '-';

                    return $btn;
                })
                ->addColumn('village', function ($row) {

                    $btn = isset($row->village) ? $row->village : '-';

                    return $btn;
                })
                ->addColumn('pincode', function ($row) {

                    $btn = isset($row->pincode) ? $row->pincode : '-';

                    return $btn;
                })
                ->addColumn('action', function ($row) {

                    $btn = '<div>';
                    $btn .= '<a title="Remove" href="'. route('deleteUser', base64_encode($row->id)) .'" data-toggle="modal" data-target="#delete" class="close-icon delete-confirm" data-val="remove"> <svg width="15" height="15" viewBox="0 0 15 15" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M13.1253 4.2063C13.1128 4.2063 13.094 4.2063 13.0753 4.2063C9.76903 3.87505 6.46903 3.75005 3.20028 4.0813L1.92528 4.2063C1.66278 4.2313 1.43153 4.0438 1.40653 3.7813C1.38153 3.5188 1.56903 3.2938 1.82528 3.2688L3.10028 3.1438C6.42528 2.8063 9.79403 2.93755 13.169 3.2688C13.4253 3.2938 13.6128 3.52505 13.5878 3.7813C13.569 4.02505 13.3628 4.2063 13.1253 4.2063Z" fill="var(--primary-color)"/>
                    <path d="M5.31272 3.575C5.28772 3.575 5.26272 3.575 5.23147 3.56875C4.98147 3.525 4.80647 3.28125 4.85022 3.03125L4.98772 2.2125C5.08772 1.6125 5.22522 0.78125 6.68147 0.78125H8.31897C9.78147 0.78125 9.91897 1.64375 10.0127 2.21875L10.1502 3.03125C10.194 3.2875 10.019 3.53125 9.76897 3.56875C9.51272 3.6125 9.26897 3.4375 9.23147 3.1875L9.09397 2.375C9.00647 1.83125 8.98772 1.725 8.32522 1.725H6.68772C6.02522 1.725 6.01272 1.8125 5.91897 2.36875L5.77522 3.18125C5.73772 3.4125 5.53772 3.575 5.31272 3.575Z" fill="var(--primary-color)"/>
                    <path d="M9.50649 14.2187H5.49399C3.31274 14.2187 3.22524 13.0125 3.15649 12.0375L2.75024 5.74374C2.73149 5.48749 2.93149 5.26249 3.18774 5.24374C3.45024 5.23124 3.66899 5.42499 3.68774 5.68124L4.09399 11.975C4.16274 12.925 4.18774 13.2812 5.49399 13.2812H9.50649C10.819 13.2812 10.844 12.925 10.9065 11.975L11.3127 5.68124C11.3315 5.42499 11.5565 5.23124 11.8127 5.24374C12.069 5.26249 12.269 5.48124 12.2502 5.74374L11.844 12.0375C11.7752 13.0125 11.6877 14.2187 9.50649 14.2187Z" fill="var(--primary-color)"/>
                    <path d="M8.53779 10.7812H6.45654C6.20029 10.7812 5.98779 10.5688 5.98779 10.3125C5.98779 10.0563 6.20029 9.84375 6.45654 9.84375H8.53779C8.79404 9.84375 9.00654 10.0563 9.00654 10.3125C9.00654 10.5688 8.79404 10.7812 8.53779 10.7812Z" fill="var(--primary-color)"/>
                    <path d="M9.06274 8.28125H5.93774C5.68149 8.28125 5.46899 8.06875 5.46899 7.8125C5.46899 7.55625 5.68149 7.34375 5.93774 7.34375H9.06274C9.31899 7.34375 9.53149 7.55625 9.53149 7.8125C9.53149 8.06875 9.31899 8.28125 9.06274 8.28125Z" fill="var(--primary-color)"/>
                    </svg>
                     </a>/';

                    if($row->is_active == 1){
                        $btn .= '<a title="Block" href="'. route('blockUnblockUser', base64_encode($row->id)) .'" data-toggle="modal" data-target="#delete" class="close-icon delete-confirm" data-val="block"><svg width="15" height="15" viewBox="0 0 15 15" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M9.3125 14.2188H5.6875C5.13125 14.2188 4.41875 13.925 4.03125 13.5312L1.46875 10.9687C1.075 10.575 0.78125 9.8625 0.78125 9.3125V5.6875C0.78125 5.13125 1.075 4.41876 1.46875 4.03126L4.03125 1.46875C4.425 1.075 5.1375 0.78125 5.6875 0.78125H9.3125C9.86875 0.78125 10.5813 1.075 10.9688 1.46875L13.5312 4.03126C13.925 4.42501 14.2188 5.1375 14.2188 5.6875V9.3125C14.2188 9.86875 13.925 10.5812 13.5312 10.9687L10.9688 13.5312C10.575 13.925 9.86875 14.2188 9.3125 14.2188ZM5.6875 1.71875C5.38125 1.71875 4.90625 1.9125 4.69375 2.13125L2.13125 4.69376C1.91875 4.91251 1.71875 5.38125 1.71875 5.6875V9.3125C1.71875 9.61875 1.9125 10.0937 2.13125 10.3062L4.69375 12.8687C4.9125 13.0812 5.38125 13.2812 5.6875 13.2812H9.3125C9.61875 13.2812 10.0938 13.0875 10.3063 12.8687L12.8687 10.3062C13.0812 10.0875 13.2812 9.61875 13.2812 9.3125V5.6875C13.2812 5.38125 13.0875 4.90626 12.8687 4.69376L10.3063 2.13125C10.0875 1.91875 9.61875 1.71875 9.3125 1.71875H5.6875Z" fill="var(--primary-color)"/>
                        <path d="M3.0873 12.3938C2.96855 12.3938 2.8498 12.35 2.75605 12.2563C2.5748 12.075 2.5748 11.775 2.75605 11.5938L11.5936 2.7563C11.7748 2.57505 12.0748 2.57505 12.2561 2.7563C12.4373 2.93755 12.4373 3.23755 12.2561 3.4188L3.41855 12.2563C3.3248 12.35 3.20605 12.3938 3.0873 12.3938Z" fill="var(--primary-color)"/>
                        </svg> </a>';
                    } else {
                        $btn .= '<a title="Unblock" href="'. route('blockUnblockUser', base64_encode($row->id)) .'" data-toggle="modal" data-target="#delete" class="close-icon delete-confirm" data-val="unblock">  <svg width="15" height="15" viewBox="0 0 15 15" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M9.3125 14.2188H5.6875C5.13125 14.2188 4.41875 13.925 4.03125 13.5312L1.46875 10.9687C1.075 10.575 0.78125 9.8625 0.78125 9.3125V5.6875C0.78125 5.13125 1.075 4.41876 1.46875 4.03126L4.03125 1.46875C4.425 1.075 5.1375 0.78125 5.6875 0.78125H9.3125C9.86875 0.78125 10.5813 1.075 10.9688 1.46875L13.5312 4.03126C13.925 4.42501 14.2188 5.1375 14.2188 5.6875V9.3125C14.2188 9.86875 13.925 10.5812 13.5312 10.9687L10.9688 13.5312C10.575 13.925 9.86875 14.2188 9.3125 14.2188ZM5.6875 1.71875C5.38125 1.71875 4.90625 1.9125 4.69375 2.13125L2.13125 4.69376C1.91875 4.91251 1.71875 5.38125 1.71875 5.6875V9.3125C1.71875 9.61875 1.9125 10.0937 2.13125 10.3062L4.69375 12.8687C4.9125 13.0812 5.38125 13.2812 5.6875 13.2812H9.3125C9.61875 13.2812 10.0938 13.0875 10.3063 12.8687L12.8687 10.3062C13.0812 10.0875 13.2812 9.61875 13.2812 9.3125V5.6875C13.2812 5.38125 13.0875 4.90626 12.8687 4.69376L10.3063 2.13125C10.0875 1.91875 9.61875 1.71875 9.3125 1.71875H5.6875Z" fill="var(--primary-color)"/>
                        </svg> </a>';
                    }

                    $btn .= '/<a title="View" href="'. route('viewUserDetail', base64_encode($row->id)) .'" > <svg width="16" height="15" viewBox="0 0 16 15" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M7.50215 10.2062C6.0084 10.2062 4.7959 8.9937 4.7959 7.49995C4.7959 6.0062 6.0084 4.7937 7.50215 4.7937C8.9959 4.7937 10.2084 6.0062 10.2084 7.49995C10.2084 8.9937 8.9959 10.2062 7.50215 10.2062ZM7.50215 5.7312C6.52715 5.7312 5.7334 6.52495 5.7334 7.49995C5.7334 8.47495 6.52715 9.2687 7.50215 9.2687C8.47715 9.2687 9.2709 8.47495 9.2709 7.49995C9.2709 6.52495 8.47715 5.7312 7.50215 5.7312Z" fill="var(--primary-color)"/>
                    <path d="M7.50176 13.1375C5.15176 13.1375 2.93301 11.7625 1.40801 9.37505C0.745508 8.3438 0.745508 6.66255 1.40801 5.62505C2.93926 3.23755 5.15801 1.86255 7.50176 1.86255C9.84551 1.86255 12.0643 3.23755 13.5893 5.62505C14.2518 6.6563 14.2518 8.33755 13.5893 9.37505C12.0643 11.7625 9.84551 13.1375 7.50176 13.1375ZM7.50176 2.80005C5.48301 2.80005 3.55176 4.01255 2.20176 6.1313C1.73301 6.86255 1.73301 8.13755 2.20176 8.8688C3.55176 10.9875 5.48301 12.2 7.50176 12.2C9.52051 12.2 11.4518 10.9875 12.8018 8.8688C13.2705 8.13755 13.2705 6.86255 12.8018 6.1313C11.4518 4.01255 9.52051 2.80005 7.50176 2.80005Z" fill="var(--primary-color)"/>
                    </svg> </a>';

                    $btn .= '</div>';

                    return $btn;
                })
                

                ->rawColumns(['first_name', 'last_name', 'email', 'phone', 'state', 'district', 'taluka', 'village', 'pincode', 'action'])
                ->make(true);
        }
    }

    public function deleteUser($id){
        $id = base64_decode($id);
        $delete = User::where('id', $id)->delete();
        DB::table('properties')->where('user_id', $id)->delete();
        DB::table('user_authentications')->where('user_id', $id)->delete();
        $product_id = DB::table('products')->select('id')->where('user_id', $id)->get();
        $product_data = json_decode( json_encode($product_id), true);
        if(!empty($product_data)){
            foreach($product_data as $key => $value){
                DB::table('product_assets')->where('product_id',$value['id'])->delete();
            }
        }
        DB::table('products')->where('user_id', $id)->delete();
        DB::table('product_favourites')->where('user_id', $id)->delete();
        DB::table('product_inquiries')->where('user_id', $id)->delete();
        if($delete){
            return redirect()->intended('/userManagement')->with('alert-success', 'User deleted successfully!');
        }
    }

    public function blockUnblockUser($id)
    {
        $id = base64_decode($id);

        $checkUser = User::where('id', $id)->first();
        if($checkUser->is_active == 1){
            $update = User::where('id', $id)->update(['is_active' => 0]);
            
            DB::table('user_authentications')->where('user_id', $id)->delete();

            if($update) {
                return redirect()->intended('/userManagement')->with('alert-success', 'User blocked successfully!');
            }
        } else {
            $update = User::where('id', $id)->update(['is_active' => 1]);
            if($update) {
                return redirect()->intended('/userManagement')->with('alert-success', 'User unblock successfully!');
            }

        }

    }

    public function viewUserDetail($id)
    {
        $id = base64_decode($id);
        $userDetail = User::where('id', $id)->first();

        return view('pages.User.view', compact('userDetail'));
    }
}

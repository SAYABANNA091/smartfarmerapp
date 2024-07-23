<?php
namespace App\Http\Controllers;
use Illuminate\Http\Request;
use App\Models\UserState;
use App\Models\User;
use App\Models\UserTaluka;
use App\Models\UserDistrict;
use Yajra\DataTables\Facades\DataTables;
use Illuminate\Support\Str;

class DistrictController extends Controller
{
    public function districtList()
    {
        return view('pages.User.district');
    }
    
    public function districtData(Request $request)
    {
        if ($request->ajax()) {
            $data = UserDistrict::where('deleted_at', null)->with('state')->get();
            return DataTables::of($data)
                ->addIndexColumn()
                ->filter(function ($instance) use ($request) {
                    if (!empty($request->get('search'))) {
                        $instance->collection = $instance->collection->filter(function ($row) use ($request) {
                            if (Str::contains(Str::lower($row['name']), Str::lower($request->get('search')))) {
                                return true;
                            }
                        });
                    }
                })
                ->addColumn('name', function ($row) {
                    $btn = isset($row->name) ? $row->name : '-';
                    return $btn;
                })
                ->addColumn('states_id', function ($row) {
                    $btn = isset($row->state->name) ? $row->state->name : '-';
                    return $btn;
                })
                ->addColumn('action', function ($row) {
                    $btn = '<div>';

                    $btn .= '<a title="Edit" href="'. route('addDistrict', base64_encode($row->id)) .'" class="close-icon" data-val="block"><svg width="15" height="15" viewBox="0 0 25 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M15.002 22.75H9.00195C3.57195 22.75 1.25195 20.43 1.25195 15V9C1.25195 3.57 3.57195 1.25 9.00195 1.25H11.002C11.412 1.25 11.752 1.59 11.752 2C11.752 2.41 11.412 2.75 11.002 2.75H9.00195C4.39195 2.75 2.75195 4.39 2.75195 9V15C2.75195 19.61 4.39195 21.25 9.00195 21.25H15.002C19.612 21.25 21.252 19.61 21.252 15V13C21.252 12.59 21.592 12.25 22.002 12.25C22.412 12.25 22.752 12.59 22.752 13V15C22.752 20.43 20.432 22.75 15.002 22.75Z" fill="var(--primary-color)"/>
                    <path d="M8.50203 17.6901C7.89203 17.6901 7.33203 17.4701 6.92203 17.0701C6.43203 16.5801 6.22203 15.8701 6.33203 15.1201L6.76203 12.1101C6.84203 11.5301 7.22203 10.7801 7.63203 10.3701L15.512 2.49006C17.502 0.500059 19.522 0.500059 21.512 2.49006C22.602 3.58006 23.092 4.69006 22.992 5.80006C22.902 6.70006 22.422 7.58006 21.512 8.48006L13.632 16.3601C13.222 16.7701 12.472 17.1501 11.892 17.2301L8.88203 17.6601C8.75203 17.6901 8.62203 17.6901 8.50203 17.6901ZM16.572 3.55006L8.69203 11.4301C8.50203 11.6201 8.28203 12.0601 8.24203 12.3201L7.81203 15.3301C7.77203 15.6201 7.83203 15.8601 7.98203 16.0101C8.13203 16.1601 8.37203 16.2201 8.66203 16.1801L11.672 15.7501C11.932 15.7101 12.382 15.4901 12.562 15.3001L20.442 7.42006C21.092 6.77006 21.432 6.19006 21.482 5.65006C21.542 5.00006 21.202 4.31006 20.442 3.54006C18.842 1.94006 17.742 2.39006 16.572 3.55006Z" fill="var(--primary-color)"/>
                    <path d="M19.852 9.83003C19.782 9.83003 19.712 9.82003 19.652 9.80003C17.022 9.06003 14.932 6.97003 14.192 4.34003C14.082 3.94003 14.312 3.53003 14.712 3.41003C15.112 3.30003 15.522 3.53003 15.632 3.93003C16.232 6.06003 17.922 7.75003 20.052 8.35003C20.452 8.46003 20.682 8.88003 20.572 9.28003C20.482 9.62003 20.182 9.83003 19.852 9.83003Z" fill="var(--primary-color)"/>
                    </svg>
                     </a> | ';
                    
                    $btn .= '<a title="Remove" href="'. route('deleteDistrict', base64_encode($row->id)) .'" data-toggle="modal" data-target="#District" class="close-icon delete-confirm" data-val="remove"> <svg width="15" height="15" viewBox="0 0 15 15" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M13.1253 4.2063C13.1128 4.2063 13.094 4.2063 13.0753 4.2063C9.76903 3.87505 6.46903 3.75005 3.20028 4.0813L1.92528 4.2063C1.66278 4.2313 1.43153 4.0438 1.40653 3.7813C1.38153 3.5188 1.56903 3.2938 1.82528 3.2688L3.10028 3.1438C6.42528 2.8063 9.79403 2.93755 13.169 3.2688C13.4253 3.2938 13.6128 3.52505 13.5878 3.7813C13.569 4.02505 13.3628 4.2063 13.1253 4.2063Z" fill="var(--primary-color)"/>
                    <path d="M5.31272 3.575C5.28772 3.575 5.26272 3.575 5.23147 3.56875C4.98147 3.525 4.80647 3.28125 4.85022 3.03125L4.98772 2.2125C5.08772 1.6125 5.22522 0.78125 6.68147 0.78125H8.31897C9.78147 0.78125 9.91897 1.64375 10.0127 2.21875L10.1502 3.03125C10.194 3.2875 10.019 3.53125 9.76897 3.56875C9.51272 3.6125 9.26897 3.4375 9.23147 3.1875L9.09397 2.375C9.00647 1.83125 8.98772 1.725 8.32522 1.725H6.68772C6.02522 1.725 6.01272 1.8125 5.91897 2.36875L5.77522 3.18125C5.73772 3.4125 5.53772 3.575 5.31272 3.575Z" fill="var(--primary-color)"/>
                    <path d="M9.50649 14.2187H5.49399C3.31274 14.2187 3.22524 13.0125 3.15649 12.0375L2.75024 5.74374C2.73149 5.48749 2.93149 5.26249 3.18774 5.24374C3.45024 5.23124 3.66899 5.42499 3.68774 5.68124L4.09399 11.975C4.16274 12.925 4.18774 13.2812 5.49399 13.2812H9.50649C10.819 13.2812 10.844 12.925 10.9065 11.975L11.3127 5.68124C11.3315 5.42499 11.5565 5.23124 11.8127 5.24374C12.069 5.26249 12.269 5.48124 12.2502 5.74374L11.844 12.0375C11.7752 13.0125 11.6877 14.2187 9.50649 14.2187Z" fill="var(--primary-color)"/>
                    <path d="M8.53779 10.7812H6.45654C6.20029 10.7812 5.98779 10.5688 5.98779 10.3125C5.98779 10.0563 6.20029 9.84375 6.45654 9.84375H8.53779C8.79404 9.84375 9.00654 10.0563 9.00654 10.3125C9.00654 10.5688 8.79404 10.7812 8.53779 10.7812Z" fill="var(--primary-color)"/>
                    <path d="M9.06274 8.28125H5.93774C5.68149 8.28125 5.46899 8.06875 5.46899 7.8125C5.46899 7.55625 5.68149 7.34375 5.93774 7.34375H9.06274C9.31899 7.34375 9.53149 7.55625 9.53149 7.8125C9.53149 8.06875 9.31899 8.28125 9.06274 8.28125Z" fill="var(--primary-color)"/>
                    </svg>
                     </a>';
                    
                    $btn .= '</div>';
                    return $btn;
                })
                ->rawColumns(['name', 'action'])
                ->make(true);
        }
    }

    public function deleteDistrict($id){
        $id = base64_decode($id);
        $UserTalukasdata = UserTaluka::select('id')->where('districts_id', $id)->where('deleted_at', null)->get();
            foreach ($UserTalukasdata as $value) {
                UserTaluka::where('id', $value['id'])->delete();
            }
        UserDistrict::where('id', $id)->delete();
        return redirect()->intended('/districtList')->with('alert-success', 'District deleted successfully!');
    }

    public function addDistrict(Request $request, $id)
    {
        $id = base64_decode($id);
        $UserStatesdata = UserState::select('*')->where('deleted_at', null)->orderBy('name', 'asc')->get();
        $UserDistrictdata = UserDistrict::select('*')->where('id', $id)->where('deleted_at', null)->orderBy('name', 'asc')->first();
        return view('pages.User.addEditDistrict', compact('id', 'UserStatesdata', 'UserDistrictdata'));
    }
    
    public function addEditDistrict(Request $request, $id)
    { 
        $id = base64_decode($id);
        $this->validate(
            $request,
            [
                'name' => 'required',
                'states_id' => 'required'
            ],
            [
                'name.required' => 'Please enter district name.',
                'states_id.required' => 'Please enter state.'
            ]
        );
        if ($id == 0) {
            $districtData = array();
            $districtData['name'] = $request->name;
            $districtData['states_id'] = $request->states_id;
            $savedistrict = UserDistrict::create($districtData);
            $msg = 'District Added Successfully!';
        }else{ 
            $districtData = array();
            $districtData['name'] = $request->name;
            $districtData['states_id'] = $request->states_id;
            $savedistrict = UserDistrict::where('id', $id)->update($districtData);
            $msg = 'District Updated Successfully!';
        }
            if ($savedistrict) {
                return redirect()->intended('/districtList')->with('alert-success', $msg);
            }
    }
}

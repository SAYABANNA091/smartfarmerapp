@extends('layouts.default')
@section('content')

<div class="content-wrapper">
    <div class="container-xxl flex-grow-1 container-p-y">
        <h4 class="breadcrumb-wrapper py-0 mb-4">
            View User Details
        </h4>

        <div class="">
            <div class="col-xl">
                <div class="card mb-4 bg-primary shadow-none">
                    <div class="card-body m-0 pt-0">
                        <div class="row">
                            <div class="col-md-8">

                                <div class="row   align-items-center border-bottom ">
                                    <label class="col-form-label col-sm-4" for="basic-default-name">First name</label>
                                    <div class="col-sm-8">
                                        <div class=" p-0">
                                           {{isset($userDetail->first_name) ? $userDetail->first_name : '-' }}
                                        </div>
                                    </div>
                                </div>

                                <div class="row   align-items-center border-bottom ">
                                    <label class="col-form-label col-sm-4" for="basic-default-name">Last name</label>
                                    <div class="col-sm-8">
                                        <div class=" p-0">
                                           {{ isset($userDetail->last_name) ? $userDetail->last_name : '-' }}
                                        </div>
                                    </div>
                                </div>

                                <div class="row   align-items-center border-bottom ">
                                    <label class="col-form-label col-sm-4" for="basic-default-name">Phone</label>
                                    <div class="col-sm-8">
                                        <div class=" p-0">
                                           {{ isset($userDetail->phone_number) ? $userDetail->phone_number : '-' }}
                                        </div>
                                    </div>
                                </div>

                                <div class="row  align-items-center border-bottom ">
                                    <label class="col-form-label col-sm-4" for="basic-default-name">Email</label>
                                    <div class="col-sm-8">
                                        <div class=" p-0">
                                           {{ isset($userDetail->email) ? $userDetail->email : '-' }}
                                        </div>
                                    </div>
                                </div>

                                <div class="row  align-items-center border-bottom ">
                                    <label class="col-form-label col-sm-4" for="basic-default-name">State</label>
                                    <div class="col-sm-8">
                                        <div class=" p-0">
                                           {{ isset($userDetail->state) ? $userDetail->state : '-' }}
                                        </div>
                                    </div>
                                </div>

                                <div class="row   align-items-center border-bottom ">
                                    <label class="col-form-label col-sm-4" for="basic-default-name">District</label>
                                    <div class="col-sm-8">
                                        <div class=" p-0">
                                           {{ isset($userDetail->district) ? $userDetail->district : '-' }}
                                        </div>
                                    </div>
                                </div>

                                <div class="row   align-items-center border-bottom ">
                                    <label class="col-form-label col-sm-4" for="basic-default-name">Taluka</label>
                                    <div class="col-sm-8">
                                        <div class=" p-0">
                                           {{ isset($userDetail->taluka) ? $userDetail->taluka : '-' }}
                                        </div>
                                    </div>
                                </div>

                                <div class="row align-items-center border-bottom ">
                                    <label class="col-form-label col-sm-4" for="basic-default-name">Village</label>
                                    <div class="col-sm-8">
                                        <div class=" p-0">
                                           {{ isset($userDetail->village) ? $userDetail->village : '-' }}
                                        </div>
                                    </div>
                                </div>

                                <div class="row   align-items-center border-bottom ">
                                    <label class="col-form-label col-sm-4" for="basic-default-name">Pincode</label>
                                    <div class="col-sm-8">
                                        <div class=" p-0">
                                           {{ isset($userDetail->pincode) ? $userDetail->pincode : '-' }}
                                        </div>
                                    </div>
                                </div>

                                <div class="row justify-content-end mt-3">
                                    <div class="col-sm-12">
                                        <a href="{{ route('userManagement') }}" class="btn-admin">Back</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

@stop

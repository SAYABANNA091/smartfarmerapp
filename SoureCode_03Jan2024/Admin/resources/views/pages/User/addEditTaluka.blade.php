@php
$talukaId  = isset($UserTalukadata->id) ? $UserTalukadata->id : '0';
$stateId = isset($UserTalukadata->states_id) ? $UserTalukadata->states_id : '0';
$districtId  = isset($UserTalukadata->districts_id) ? $UserTalukadata->districts_id : '0';
$talukaName  = isset($UserTalukadata->name) ? $UserTalukadata->name : '';
@endphp

@extends('layouts.default')
@section('content')

<div class="states-wrapper">
    <div class="right-container-body vendor_management-section">
        <div class="w-100 float-left title-column table-heading-row">
            <span class="category-name">
                <h5> {{ $talukaId ? 'Edit Taluka' : 'Add Taluka' }} </h5>
            </span>
        </div>

        @if ($alert = Session::get('alert-danger'))
            <div class="alert alert-danger alert-dismissible mb-2 hide-error-message" role="alert">
                {{ $alert }}
            </div>
        @endif
        @if ($alert = Session::get('alert-success'))
            <div class="alert alert-success alert-dismissible mb-2 hide-error-message" role="alert">
                {{ $alert }}
            </div>
        @endif
            
        <div class="w-100 float-left vendor-table-wrap">
            <div class="container py-3">
            
            <form action="{{ route('addEditTaluka', base64_encode($talukaId)) }}" method="POST" id="store-states">@csrf
                <div class=" form-column-wrapper single-column-form">

                    <div class="input-wrap">

                        <div class="input-label-wrap">
                                <label class="input-label">States Name*</label>
                                <div class="select-input-wrap-icon">
                                    <select name="states_id" class="custom-select input_style" id="states_id" autofocus>
                                         <option value="">Select States</option>
                                          @foreach ($UserStatesdata as $data)  
                                         <option value="{{ $data->id }}" {{ ($data->id == $stateId ? 'selected' : $data->id) }}> {{ !empty($data->name) ? $data->name : '' }}
                                        </option>
                                        @endforeach 
                                    </select>
                                    <span class="input-error-text hide-error-message" style="{{ $errors->has('states_id') ? '' : 'opacity: 0;' }}">{{ $errors->first('states_id') }}
                                    </span>
                                </div>  
                        </div>

                        <div class="input-label-wrap">
                            <label class="input-label">District Name*</label>
                            <div class="select-input-wrap-icon">
                                <select name="districts_id" class="custom-select input_style" id="district" autofocus>
                                     <option value="">Select District</option>
                                     @foreach ($UserDistrictdata as $data) 
                                     <option value="{{ $data->id }}" {{ ($data->id == $districtId ? 'selected' : $data->id) }}> {{ !empty($data->name) ? $data->name : '' }}
                                     </option> 
                                     @endforeach
                                </select>
                                <span class="input-error-text hide-error-message" style="{{ $errors->has('districts_id') ? '' : 'opacity: 0;' }}">{{ $errors->first('districts_id') }}
                                </span>
                            </div>  
                        </div>
                    </div>
                    
                    <div class="input-wrap">
                        <div class="input-label-wrap">
                        <label class="input-label">Taluka Name*<br></label>
                        <div class="input-col">
                            <input type="text" class="col-12" name="name" onkeyup="allowAlphaNumericSpace(this)" value="{{ (isset($talukaName) ? $talukaName : '') }}" maxlength="40" class="input_style category_name" autofocus>
                            <span class="input-error-text hide-error-message " style="{{ $errors->has('name') ? '' : 'opacity: 0;' }}">{{ $errors->first('name') }}</span>
                        </div>    
                        </div>
                    </div>

                </div>
                <div class="input-wrap input-btn-wrap">
                    <div class="input-btn">
                         <button class="form-btn-style"> {{ $talukaId == 0 ? 'Save' : 'Update' }}</button>
                        <a href="{{ url('/talukaList') }}" class="form-btn-style">Cancel</a>
                    </div>
                </div>
            </form> 

        </div>
    </div>
    </div>
</div>

<script type="text/javascript">
    $(function() {
        $("#store-category").validate({
            rules: {
                'name': {
                    required: true
                }
            },
            messages: {
                'name': {
                    required: "Please enter category name."
                }
            },

            submitHandler: function(form) {
                form.submit();
            }
        });
        $('.name').each(function() {
            $(this).rules('add', {
                required: true,
                messages: {
                    required: "Please enter category name.",
                }
            });
        });
    });

    function allowAlphaNumericSpace(thisInput) {
        thisInput.value = thisInput.value.split(/[!(^~`@#$%^&*()+=*/+><.,:;'""''{}?/|')]/).join('');
    }
    
    //$(document).ready(function() {
        $('#states_id').on('change', function() { 
            var stateId = $(this).val();
            if (stateId) {
                $.ajax({
                    url: "{{url('/getdistrict')}}",
                    type: "POST",
                    data: {
                        state_id: stateId,
                        _token: '{{csrf_token()}}'
                    },
                    success: function(data) {
                        $('#district').empty();
                        $('#district').append('<option value="">Select a district</option>');
                        $.each(data, function(key, value) {
                            $('#district').append('<option value="' + value.id + '">' + value.name + '</option>');
                        });
                    }
                });
            }else {
                $('#district').empty();
                $('#district').append('<option value="">Select a district</option>');
            }
        });
    //});
</script>
@stop
@php
$stateData = isset($stateName) ? $stateName : '';
$stateId = isset($UserDistrictdata->states_id) ? $UserDistrictdata->states_id : '0';
$districtId  = isset($UserDistrictdata->id) ? $UserDistrictdata->id : '0';
// print_r($districtId);  die;
@endphp

@extends('layouts.default')
@section('content')

<div class="states-wrapper">
    <div class="right-container-body vendor_management-section">
        <div class="w-100 float-left title-column table-heading-row">
            <span class="category-name">
                <h5> {{ $stateId ? 'Edit District' : 'Add District' }} </h5>
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
            
            <form action="{{ route('addEditDistrict', base64_encode($districtId)) }}" method="POST" id="store-states">@csrf
                <div class=" form-column-wrapper single-column-form">

                    <div class="input-wrap">
                        <div class="input-label-wrap">
                            <label class="input-label">States Name*</label>
                            <div class="select-input-wrap-icon">
                                <select name="states_id" class="custom-select input_style" autofocus>
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
                    </div>
                    
                    <div class="input-wrap">
                        <div class="input-label-wrap">
                        <label class="input-label">District Name*<br></label>
                        <div class="input-col">
                            <input type="text" class="col-12" name="name" onkeyup="allowAlphaNumericSpace(this)" value="{{ (isset($UserDistrictdata->name) ? $UserDistrictdata->name : '') }}" maxlength="40" class="input_style category_name" autofocus>
                            <span class="input-error-text hide-error-message " style="{{ $errors->has('name') ? '' : 'opacity: 0;' }}">{{ $errors->first('name') }}</span>
                        </div>    
                        </div>
                    </div>

                </div>
                <div class="input-wrap input-btn-wrap">
                    <div class="input-btn">
                         <button class="form-btn-style"> {{ $stateId == 0 ? 'Save' : 'Update' }}</button>
                        <a href="{{ url('/districtList') }}" class="form-btn-style">Cancel</a>
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
</script>

@stop

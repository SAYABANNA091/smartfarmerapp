@php
$stateData = isset($stateName) ? $stateName : '';
$stateId = isset($stateData->id) ? $stateData->id : '0';
@endphp

@extends('layouts.default')
@section('content')

<div class="states-wrapper">
    <div class="right-container-body vendor_management-section">
        <div class="w-100 float-left title-column table-heading-row">
            <span class="category-name">
                <h5> {{ $stateId ? 'Edit state' : 'Add state' }} </h5>
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
            
            <form action="{{ route('addEditStates', base64_encode($stateId)) }}" method="POST" id="store-states">@csrf
                <div class=" form-column-wrapper single-column-form">
                    <div class="input-wrap">
                        <div class="input-label-wrap">
                        <label class="input-label">State Name*<br></label>
                        <div class="input-col">
                            <input type="text" class="col-12" name="name" onkeyup="allowAlphaNumericSpace(this)" value="{{ (isset($stateData->name) ? $stateData->name : '') }}" maxlength="40" class="input_style category_name" autofocus>
                            <span class="input-error-text hide-error-message " style="{{ $errors->has('name') ? '' : 'opacity: 0;' }}">{{ $errors->first('name') }}</span>
                        </div>
                    </div>
                    
                </div>
                </div>
                <div class="input-wrap input-btn-wrap">
                    <div class="input-btn">
                         <button class="form-btn-style"> {{ $stateId == 0 ? 'Save' : 'Update' }}</button>
                        <a href="{{ url('/statesList') }}" class="form-btn-style">Cancel</a>
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

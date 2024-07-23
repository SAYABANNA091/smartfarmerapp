<!-- popup section -->
<style>
    .loader_logo {
        position: relative;
        right: 0;
        left: 0;
        position: fixed;
        top: 0;
        bottom: 0;
        margin: auto;
        height: 100vh;
        width: 100%;
        z-index: 9999;
        background: #ffffff73;
    }

    .logo_lodader {
        display: block;
        /* top: -40px; */
        width: 60px;
        height: 60px;
        position: absolute;
        right: 0;
        /* left: -60px; */
        /* margin: auto; */
        bottom: 0;
        z-index: 9;
        left: 50%;
        top: 50%;
        margin: -40px 0 0 -50px;
    }

    #loader {
        display: block;
        position: relative;
        left: 50%;
        top: 50%;
        width: 150px;
        height: 150px;
        margin: -75px 0 0 -75px;
        border-radius: 50%;
        border: 3px solid transparent;
        /* border-top-color: #5d5d5d; */
        border-top-color: #D82228;
        -webkit-animation: spin 2s linear infinite;
        animation: spin 2s linear infinite;
        z-index: 9;
    }

    #loader:before {
        content: "";
        position: absolute;
        top: 5px;
        left: 5px;
        right: 5px;
        bottom: 5px;
        border-radius: 50%;
        border: 3px solid transparent;
        border-top-color: #242424;
        /*  border-top-color: #ad8698;*/
        -webkit-animation: spin 3s linear infinite;
        animation: spin 3s linear infinite;
    }

    #loader:after {
        content: "";
        position: absolute;
        top: 15px;
        left: 15px;
        right: 15px;
        bottom: 15px;
        border-radius: 50%;
        border: 3px solid transparent;
        border-top-color: #D82228;
        /*border-top-color: #365bda;*/
        -webkit-animation: spin 1.5s linear infinite;
        animation: spin 1.5s linear infinite;
    }

    .logo_lodader img {
        width: 80px;
        margin: 12px;
    }

    @-webkit-keyframes spin {
        0% {
            -webkit-transform: rotate(0deg);
            /* Chrome, Opera 15+, Safari 3.1+ */
            -ms-transform: rotate(0deg);
            /* IE 9 */
            transform: rotate(0deg);
            /* Firefox 16+, IE 10+, Opera */
        }

        100% {
            -webkit-transform: rotate(360deg);
            /* Chrome, Opera 15+, Safari 3.1+ */
            -ms-transform: rotate(360deg);
            /* IE 9 */
            transform: rotate(360deg);
            /* Firefox 16+, IE 10+, Opera */
        }
    }

    @keyframes spin {
        0% {
            -webkit-transform: rotate(0deg);
            /* Chrome, Opera 15+, Safari 3.1+ */
            -ms-transform: rotate(0deg);
            /* IE 9 */
            transform: rotate(0deg);
            /* Firefox 16+, IE 10+, Opera */
        }

        100% {
            -webkit-transform: rotate(360deg);
            /* Chrome, Opera 15+, Safari 3.1+ */
            -ms-transform: rotate(360deg);
            /* IE 9 */
            transform: rotate(360deg);
            /* Firefox 16+, IE 10+, Opera */
        }
    }
</style>
<div class="loader_logo" style="display: none;">
    <div class="logo_lodader">
    </div>
    <div id="loader"></div>
</div>

{{-- Delete User Popup --}}
<div class="modal custom_popup_section" id="delete" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="delete_popup_body">


                <div class="delete-detail">
                    <div class="w-100 float-left delete-contain">
                        <h5>Are you sure you want to <span class="deActive-Active" id="append-name"></span> this user ?</h5>
                        <div class="modal-footer">
                            <a class="btn btn-primary confirm-yes" href="#" style="background:red">Confirm</a>
                            <a class="btn btn-secondary confirm-no" data-dismiss="modal" href="#"
                                style="background:red">Cancel</a>
                        </div>
                    </div>
                </div>


            </div>
        </div>
    </div>
</div>

{{-- Delete State Popup --}}
<div class="modal custom_popup_section" id="State" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="State_popup_body">
                <div class="State-detail">
                    <div class="w-100 float-left State-contain">
                        <h5>Are you sure you want to remove<span class="deActive-Active" id="append-name"></span> this state ?</h5>
                        <div class="modal-footer">
                            <a class="btn btn-primary confirm-yes" href="#" style="background:red">Confirm</a>
                            <a class="btn btn-secondary confirm-no" data-dismiss="modal" href="#"
                                style="background:red">Cancel</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

{{-- Delete District Popup --}}
<div class="modal custom_popup_section" id="District" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="District_popup_body">
                <div class="District-detail">
                    <div class="w-100 float-left District-contain">
                        <h5>Are you sure you want to remove<span class="deActive-Active" id="append-name"></span> this district ?</h5>
                        <div class="modal-footer">
                            <a class="btn btn-primary confirm-yes" href="#" style="background:red">Confirm</a>
                            <a class="btn btn-secondary confirm-no" data-dismiss="modal" href="#"
                                style="background:red">Cancel</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

{{-- Delete Taluka Popup --}}
<div class="modal custom_popup_section" id="Taluka" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="Taluka_popup_body">
                <div class="Taluka-detail">
                    <div class="w-100 float-left Taluka-contain">
                        <h5>Are you sure you want to remove<span class="deActive-Active" id="append-name"></span> this taluka ?</h5>
                        <div class="modal-footer">
                            <a class="btn btn-primary confirm-yes" href="#" style="background:red">Confirm</a>
                            <a class="btn btn-secondary confirm-no" data-dismiss="modal" href="#"
                                style="background:red">Cancel</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    setTimeout(() => {
        // $('.hide-error-message').css('opacity', '0');
        $('.alert-success').hide();
        $('.alert-danger').hide();
    }, 3000);

    $('.input-label-wrap .input-col input, .input-label-wrap .input-col select').on('keydown', function() {
        $(this).parent().parent().next('.input-error-text').text('').css('opacity', 0);
    });

    $(document).on("click", ".activeDeactive-confirm", function() {
        var deleteUrl = $(this).attr('href');
        var getSpanTagVal = $(this).attr('id');
        if(getSpanTagVal =='Deactive')
        {
            getSpanTagVal ='active';
        }
        else
        {
            getSpanTagVal ='deactive';
        }
        $('.deActive-Active').html(getSpanTagVal);
        $(".modal-footer .confirm-yes").attr('href', deleteUrl);

        return false;
    });


    window.onpageshow = function(event) {
        if (event.persisted) {
            HideLoader();
        }
    };

    window.onbeforeunload = function() {
        ShowLoader();
    };

    $(document).bind('ajaxStart', function() {
        ShowLoader();
    }).bind('ajaxStop', function() {
        HideLoader();
    }).bind('ajaxError', function(event) {
        window.location.replace("/");
    });

    function ShowLoader() {
        $('.loader_logo').css('display', 'block');
    }

    function HideLoader() {
        $('.loader_logo').css('display', 'none');
    }
</script>

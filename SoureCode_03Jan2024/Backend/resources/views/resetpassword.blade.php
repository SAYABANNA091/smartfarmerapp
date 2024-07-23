@if($errors->any())
    <ul>
        @foreach($errors->all() as $error)
            <li>{{$error}}</li>
        @endforeach
    </ul>
@endif

@if ($message = Session::get('message'))
<div class="alert alert-success alert-block">
	<button type="button" class="close" data-dismiss="alert">×</button>	
        <strong>{{ $message }}</strong>
</div>
@endif
<style>
        label.error {
            color: #dc3545;
            font-size: 14px;
        }
</style>
<link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<!-- <form method="POST" id="reset">
    @csrf
    <input type="hidden" name="id" value="{{$user[0]['id']}}">
    <input type="password" name="password" placeholder="New Password">
    <br><br>
    <input type="password" name="password_confirmation" placeholder="Confirm Password">
    <br><br>
    <input type="submit" class="btn btn-primary">
</form> -->
<div class="container">
  <h2>Password Reset Form</h2>
  <form method="POST" id="reset">
    @csrf
    <input type="hidden" name="id" value="{{$user[0]['id']}}">
    <div class="form-group">
      <label for="password">New Password:</label>
      <input type="password" class="form-control"  placeholder="Enter a password" name="password">
    </div>
    <div class="form-group">
      <label for="pwd">Confirm Password:</label>
      <input type="password" class="form-control"  placeholder="Enter Confirm Password" name="password_confirmation">
    </div>
    <input type="submit" class="btn btn-primary">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.3/jquery.min.js"></script>
<script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.11.1/jquery.validate.min.js"></script>



<script>
    $(document).ready(function(){
        $("#reset").validate({
	rules: {
		password: {
			required: true,
            minlength: 8,
		},
		password_confirmation: {
			required: true,
            // equalTo: "#password",
		},
	},
	messages: {
		password: {
			required: "Please enter your password.",
            minlength: "Password must be at least 8 characters"
		},
		password_confirmation: {
			required: "Please enter your confirm password.",
			// equalTo: "New password and confirm password does not match."
		},
	},
	submitHandler: function (form) {
		form.submit();
	}


    });
});
    
</script>
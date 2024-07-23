<!DOCTYPE html>
<html>

<head>
   <meta charset="utf-8">
   <meta name="viewport" content="width=device-width, initial-scale=1">
   <title>Recommendation App</title>
   <link rel="shortcut icon" href="{{asset('images/rill-fav.png')}}" />
   <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
   <link rel="stylesheet" href="{{asset('css/bootstrap.min.css')}}">
   <link rel="stylesheet" href="{{asset('css/style.css')}}">
   <link href="{{asset('css/loader.css')}}" rel="stylesheet">

   <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
   <script src="{{asset('js/popper.min.js')}}"></script>
   <script src="{{asset('js/bootstrap.bundle.min.js')}}"></script>
   <script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script> 
   <script src="{{asset('js/rocket-loader.min.js')}}" data-cf-settings="6d8b3cf937df4dde05f5921e-|49" defer="" type="text/javascript"></script>
</head>

<body>
   <!-- Set Loader HTML -->
   <div class="loader_logo">
      <div class="logo_lodader">
      </div>
      <div id="loader"></div>
   </div>

   <script>
   /* Set Loader */
   $(window).on('load', function(){
       HideLoader();
   });
   window.onpageshow = function(event) {
         if (event.persisted) {
            HideLoader();
         }
   };
   function ShowLoaderLink(e) {
         if (!e.ctrlKey) {
            $('.loader_logo').css('display', 'block');
         }
   }
   function ShowLoader() {
         $('.loader_logo').css('display', 'block');
   }
   function HideLoader() {
         $('.loader_logo').css('display', 'none');
   }
</script>
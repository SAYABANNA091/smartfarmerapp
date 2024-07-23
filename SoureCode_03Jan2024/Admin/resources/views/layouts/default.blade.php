<!doctype html>
<html>
    <head>
        @include('includes.head')
    </head>
    <body>
        <div class="w-100 float-left main_wrapper-section">
            @include('includes.leftSidebar')

            <div class="right-body-column">
                @include('includes.primaryHeader')
                <div class="body-content-section">
                    @yield('content')
                </div>
            </div>
        </div>
        @include('includes.footer')
    </body>
</html>

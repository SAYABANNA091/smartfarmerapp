$(document).ready(function () {

    $("span.navigation-toggle-btn").click(function () {
        $('.main_wrapper-section').toggleClass('navigation-closed')
    });

    $('.navbar').addClass('js-enabled');
    $('.navbar ul li > ul').each(function () {
        $(this).parent().addClass('child-menu');
    });
    $('.navbar ul li.child-menu > a span.text').after('<div class="child-trigger icon"><i class="fa fa-angle-up" aria-hidden="true"></i></div>');

    $('.navbar ul li.child-menu > a').click(function () {
        $(this).next('ul').slideToggle();
        $(this).toggleClass('child-open');

    });

    $('.navigation-column ul li').click(function () {
        var table = $('#example').DataTable();
        table.state.clear();
    });

    function ShowLoaderLink(e) {
        if (!e.ctrlKey) {
            $('.loader_logo').css('display', 'block');
        }
    }

    $.validator.addMethod("noSpace",
        function (value, element) {
            return value.trim() != "" && value != "";
        }, "Space are not allowed.");


    $(".state-change").on("click", function () {
        $('#example').DataTable().state.clear();
    });

});

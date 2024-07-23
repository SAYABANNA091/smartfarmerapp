@extends('layouts.default')
@section('content')

    <div class="right-container-body vendor_management-section">
        <div class="w-100 float-left title-column table-heading-row">
            <h5>Users</h5>
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
            <table data-order='[[ 0, "desc" ]]' id="example" class="table table-striped ">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>State</th>
                        <th>District</th>
                        <th>Taluka</th>
                        <th>Village</th>
                        <th>Pincode</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
        </div>

    </div>
    <script>
        $(function() {
            $('#example').DataTable({
                "lengthChange": true,
                "scrollY": "calc(100vh - 290px)",
                "language": {
                    paginate: {
                        next: '<i class="fa fa-angle-right" aria-hidden="true"></i>',
                        previous: '<i class="fa fa-angle-left" aria-hidden="true"></i>'
                    },
                    search: "_INPUT_",
                    searchPlaceholder: "Search"
                },
                showNEntries: "false",
                pageLength: 10,
                info: false,
                ordering: true, // set ordering to false
                processing: true,
                serverSide: true,
                "stateSave": true,
                responsive: true,
                ajax: {
                    url: "{{ route('userList') }}",
                    data: function(d) {
                        d.search = $('input[type="search"]').val()
                    }
                },
                columns: [{
                        data: 'id',
                        name: 'id',
                    },
                    {
                        data: 'first_name',
                        name: 'first_name'
                    },
                    {
                        data: 'last_name',
                        name: 'last_name'
                    },
                    {
                        data: 'email',
                        name: 'email',
                    },
                    {
                        data: 'phone',
                        name: 'phone',
                    },
                    {
                        data: 'state',
                        name: 'state',
                    },
                    {
                        data: 'district',
                        name: 'district',
                    },
                    {
                        data: 'taluka',
                        name: 'taluka',
                    },
                    {
                        data: 'village',
                        name: 'village',
                    },
                    {
                        data: 'pincode',
                        name: 'pincode',
                    },
                    {
                        data: 'action',
                        name: 'action',
                        orderable: false,
                        searchable: false,
                    },
                ]

            });
        });

        $(document).on("click", ".delete-confirm", function() {
            var deleteUrl = $(this).attr('href');
            var name = $(this).attr('data-val');
            $('#append-name').html(name);
            $(".modal-footer .confirm-yes").attr('href', deleteUrl);

            return false;
        });
    </script>
@stop

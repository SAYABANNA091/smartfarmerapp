@extends('layouts.default')
@section('content')

<div class="states-wrapper">
    <div class="right-container-body vendor_management-section">
        <div class="w-100 float-left title-column table-heading-row">
            <h5>States</h5>
        </div>

        {{-- <a class="btn custom-btn" href=" {{ route('addStates', base64_encode(0)) }}">Add State</a> --}}
       
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
            <table id="example" class="table table-striped ">
                <thead>
                    <tr>
                        <th>S. No.</th>
                        <th>State</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
        </div>

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
                    url: "{{ route('statesData') }}",
                    data: function(d) {
                        d.search = $('input[type="search"]').val()
                    }
                },
                order: [[1, 'asc']],
                columns: [{
                        data: 'id',
                        name: 'id',
                        render: function(data, type, full, meta) {
                            return meta.row + meta.settings._iDisplayStart + 1;
                        }   
                    },
                    {
                        data: 'name',
                        name: 'name'
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

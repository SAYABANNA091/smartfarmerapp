@php
    $requestRouteName = request()
        ->route()
        ->getName();
@endphp
<div class="left-navigation-column">
    <div class="top-header-column header_logo_column">
        <a href="{{ url('/') }}" class="header-logo">
            <!-- <img src="{{ url('images/logo.png') }}"> -->
            <svg width="100" height="100" viewBox="0 0 79 32" fill="none" xmlns="http://www.w3.org/2000/svg">
                <g clip-path="url(#clip0_5_247)">
                    <path fill-rule="evenodd" clip-rule="evenodd"
                        d="M23.4111 17.7369C23.567 17.7369 27.9535 17.7369 28.096 17.7369C31.6764 17.4223 31.5294 12.203 27.9312 12.1055C26.6709 12.1055 23.8653 12.0701 22.6229 12.0656C22.2186 12.0651 21.8184 11.9851 21.4453 11.8304C21.0721 11.6757 20.7333 11.4492 20.4482 11.164C20.1632 10.8787 19.9376 10.5403 19.7842 10.1682C19.6308 9.79602 19.5528 9.39741 19.5546 8.9952C19.5546 8.76924 19.5189 4.07718 19.5546 3.71387C19.564 2.97592 19.2784 2.26446 18.7606 1.736C18.2428 1.20754 17.5353 0.905378 16.7935 0.895977C16.0518 0.886577 15.3367 1.17071 14.8056 1.68587C14.2744 2.20104 13.9707 2.90503 13.9612 3.64298L13.9612 8.21984C13.9612 14.0594 16.0365 17.7236 21.0509 17.7236L23.4111 17.7369Z"
                        fill="#574244" />
                    <path fill-rule="evenodd" clip-rule="evenodd"
                        d="M7.78455 14.8925C7.62868 14.8925 3.2422 14.8925 3.0997 14.8925C-0.462925 15.207 -0.324875 20.4175 3.26447 20.5327C4.52475 20.5327 7.33031 20.5681 8.56833 20.5725C8.97297 20.5725 9.37364 20.6521 9.74732 20.8065C10.121 20.961 10.4603 21.1874 10.7458 21.4727C11.0313 21.7579 11.2574 22.0965 11.411 22.469C11.5647 22.8414 11.6428 23.2404 11.6411 23.643C11.6411 23.8689 11.6767 28.561 11.6411 28.9243C11.6221 29.2981 11.679 29.6718 11.8086 30.0232C11.9381 30.3745 12.1376 30.6963 12.395 30.9692C12.6525 31.2421 12.9626 31.4605 13.3068 31.6113C13.6511 31.7622 14.0224 31.8424 14.3985 31.8472C14.7747 31.8519 15.1479 31.7812 15.4959 31.6391C15.8439 31.497 16.1595 31.2865 16.4239 31.0203C16.6882 30.754 16.8958 30.4374 17.0343 30.0895C17.1729 29.7415 17.2394 29.3693 17.23 28.9952L17.23 24.4095C17.2344 18.5699 15.1592 14.9057 10.1537 14.9057L7.78455 14.8925Z"
                        fill="#574244" />
                </g>
                <path
                    d="M37.799 22.3973L39.2247 20.1024C39.674 20.467 40.1069 20.7665 40.5236 21.0009C40.9467 21.2287 41.3634 21.398 41.7736 21.5087C42.1837 21.6128 42.6036 21.6649 43.0333 21.6649C43.5411 21.6649 43.9806 21.5933 44.3517 21.4501C44.7228 21.3068 45.0092 21.1018 45.2111 20.8348C45.4194 20.5679 45.5236 20.2587 45.5236 19.9071C45.5236 19.4969 45.3934 19.1519 45.1329 18.8719C44.8725 18.592 44.5307 18.3479 44.1075 18.1395C43.6909 17.9247 43.2384 17.7196 42.7501 17.5243C42.2749 17.329 41.7963 17.1174 41.3146 16.8895C40.8393 16.6552 40.3999 16.3785 39.9962 16.0594C39.5991 15.7404 39.2768 15.3531 39.0294 14.8973C38.7885 14.4351 38.6681 13.8752 38.6681 13.2177C38.6681 12.3583 38.8829 11.6161 39.3126 10.9911C39.7488 10.3661 40.3445 9.88432 41.0997 9.54578C41.8549 9.20723 42.7176 9.03796 43.6876 9.03796C44.3712 9.03796 45.0809 9.12911 45.8165 9.3114C46.5587 9.49369 47.2976 9.77039 48.0333 10.1415L46.8517 12.5731C46.3894 12.2932 45.8914 12.0718 45.3575 11.9091C44.8237 11.7463 44.3224 11.6649 43.8536 11.6649C43.4305 11.6649 43.0594 11.73 42.7404 11.8602C42.4279 11.9904 42.1837 12.1727 42.0079 12.4071C41.8322 12.635 41.7443 12.9052 41.7443 13.2177C41.7443 13.5887 41.8712 13.911 42.1251 14.1844C42.379 14.4514 42.7078 14.689 43.1115 14.8973C43.5216 15.1057 43.9611 15.314 44.4298 15.5223C44.9116 15.7307 45.3966 15.9585 45.8849 16.2059C46.3797 16.4533 46.8289 16.7463 47.2325 17.0848C47.6427 17.4169 47.9715 17.8173 48.2189 18.286C48.4728 18.7483 48.5997 19.3049 48.5997 19.9559C48.5997 20.8153 48.3719 21.5705 47.9161 22.2216C47.4604 22.8661 46.8256 23.3706 46.0118 23.7352C45.198 24.0933 44.2508 24.2723 43.17 24.2723C42.1805 24.2723 41.2332 24.1128 40.3282 23.7938C39.4233 23.4748 38.5802 23.0093 37.799 22.3973ZM55.1329 24.1747C54.1303 24.1747 53.2319 23.9566 52.4376 23.5204C51.6434 23.0777 51.0184 22.4755 50.5626 21.7137C50.1134 20.9455 49.8888 20.0731 49.8888 19.0966C49.8888 18.1265 50.1134 17.2606 50.5626 16.4989C51.0184 15.7307 51.6434 15.1252 52.4376 14.6825C53.2319 14.2398 54.1303 14.0184 55.1329 14.0184C56.1355 14.0184 57.0275 14.2398 57.8087 14.6825C58.5965 15.1252 59.215 15.7307 59.6642 16.4989C60.1199 17.2606 60.3478 18.1265 60.3478 19.0966C60.3478 20.0731 60.1199 20.9455 59.6642 21.7137C59.215 22.4755 58.5965 23.0777 57.8087 23.5204C57.0275 23.9566 56.1355 24.1747 55.1329 24.1747ZM55.1232 21.6649C55.5724 21.6649 55.9728 21.5542 56.3243 21.3329C56.6824 21.105 56.9624 20.799 57.1642 20.4149C57.3725 20.0243 57.4767 19.5783 57.4767 19.077C57.4767 18.5953 57.3725 18.1623 57.1642 17.7782C56.9624 17.3941 56.6824 17.0913 56.3243 16.87C55.9728 16.6421 55.5724 16.5282 55.1232 16.5282C54.6674 16.5282 54.2605 16.6421 53.9025 16.87C53.5509 17.0913 53.271 17.3941 53.0626 17.7782C52.8608 18.1623 52.7599 18.5953 52.7599 19.077C52.7599 19.5783 52.8608 20.0243 53.0626 20.4149C53.271 20.799 53.5509 21.105 53.9025 21.3329C54.2605 21.5542 54.6674 21.6649 55.1232 21.6649ZM71.881 23.9794H69.0782V18.9794C69.0782 18.2177 68.896 17.6219 68.5314 17.1923C68.1733 16.7561 67.6818 16.538 67.0568 16.538C66.4057 16.538 65.8881 16.7658 65.504 17.2216C65.1264 17.6708 64.9376 18.2828 64.9376 19.0575V23.9794H62.1349V14.2137H64.4005L64.4982 15.7762C64.8497 15.2033 65.3087 14.7671 65.8751 14.4677C66.4415 14.1682 67.1088 14.0184 67.8771 14.0184C69.1206 14.0184 70.0971 14.4025 70.8068 15.1708C71.5229 15.9325 71.881 16.9872 71.881 18.3348V23.9794ZM75.4845 11.8798C75.1134 11.8798 74.8009 11.7496 74.547 11.4891C74.2931 11.2287 74.1661 10.913 74.1661 10.5419C74.1661 10.1708 74.2931 9.85828 74.547 9.60437C74.8009 9.34395 75.1134 9.21375 75.4845 9.21375C75.8621 9.21375 76.1779 9.34395 76.4318 9.60437C76.6922 9.85828 76.8224 10.1708 76.8224 10.5419C76.8224 10.913 76.6922 11.2287 76.4318 11.4891C76.1779 11.7496 75.8621 11.8798 75.4845 11.8798ZM76.8907 23.9794H74.088V14.2137H76.8907V23.9794Z"
                    fill="#2B2122" />
                <defs>
                    <clipPath id="clip0_5_247">
                        <rect width="31.2057" height="30.9367" fill="white" transform="translate(0 0.90332)" />
                    </clipPath>
                </defs>
            </svg>

        </a>
        <span class="navigation-toggle-btn nav_inner_button" style="display:none;">
            <i class="fa fa-bars" aria-hidden="true" style="display: none;"></i>
            <i class="close-icon " aria-hidden="true">
                <svg width="20" height="20" viewBox="0 0 10 10" fill="none"
                    xmlns="http://www.w3.org/2000/svg">
                    <path d="M0.828125 9.10372L8.35463 1.57721" stroke="#292D32" stroke-width="1.5053"
                        stroke-linecap="round" stroke-linejoin="round" />
                    <path d="M8.35463 9.10372L0.828125 1.57721" stroke="#292D32" stroke-width="1.5053"
                        stroke-linecap="round" stroke-linejoin="round" />
                </svg>
            </i>
        </span>
    </div>
    <div class="navbar navigation-column">
        <ul class="top-nav">

            <li class="">
                <a href="{{ url('/dashboard') }}" class="">
                    <span class="dashboard_icon">
                        <svg width="22" height="23" viewBox="0 0 22 23" fill="none"
                            xmlns="http://www.w3.org/2000/svg">
                            <path
                                d="M6.63671 2.02734H4.89504C2.88754 2.02734 1.83337 3.08151 1.83337 5.07984V6.82151C1.83337 8.81984 2.88754 9.87401 4.88587 9.87401H6.62754C8.62587 9.87401 9.68004 8.81984 9.68004 6.82151V5.07984C9.68921 3.08151 8.63504 2.02734 6.63671 2.02734Z"
                                fill="#50555C" />
                            <path
                                d="M17.1141 2.02734H15.3724C13.3741 2.02734 12.3199 3.08151 12.3199 5.07984V6.82151C12.3199 8.81984 13.3741 9.87401 15.3724 9.87401H17.1141C19.1124 9.87401 20.1666 8.81984 20.1666 6.82151V5.07984C20.1666 3.08151 19.1124 2.02734 17.1141 2.02734Z"
                                fill="#50555C" />
                            <path
                                d="M17.1141 12.5049H15.3724C13.3741 12.5049 12.3199 13.559 12.3199 15.5574V17.299C12.3199 19.2974 13.3741 20.3515 15.3724 20.3515H17.1141C19.1124 20.3515 20.1666 19.2974 20.1666 17.299V15.5574C20.1666 13.559 19.1124 12.5049 17.1141 12.5049Z"
                                fill="#50555C" />
                            <path
                                d="M6.63671 12.5049H4.89504C2.88754 12.5049 1.83337 13.559 1.83337 15.5574V17.299C1.83337 19.3065 2.88754 20.3607 4.88587 20.3607H6.62754C8.62587 20.3607 9.68004 19.3065 9.68004 17.3082V15.5665C9.68921 13.559 8.63504 12.5049 6.63671 12.5049Z"
                                fill="#50555C" />
                        </svg>

                    </span>
                    <span class="text">Dashboard</span>
                </a>
            </li>

            <li class="">
                <a href="{{ route('userManagement') }}" class="">
                    <span class="dashboard_icon">

                        <svg width="20" height="19" viewBox="0 0 20 19" fill="none"
                            xmlns="http://www.w3.org/2000/svg">
                            <path
                                d="M12.5009 1.66699H7.50094C3.33427 1.66699 1.6676 3.33366 1.6676 7.50033V12.5003C1.6676 15.6503 2.6176 17.3753 4.88427 18.017C5.0676 15.8503 7.2926 14.142 10.0009 14.142C12.7093 14.142 14.9343 15.8503 15.1176 18.017C17.3843 17.3753 18.3343 15.6503 18.3343 12.5003V7.50033C18.3343 3.33366 16.6676 1.66699 12.5009 1.66699ZM10.0009 11.8086C8.35094 11.8086 7.0176 10.467 7.0176 8.81701C7.0176 7.16701 8.35094 5.83366 10.0009 5.83366C11.6509 5.83366 12.9843 7.16701 12.9843 8.81701C12.9843 10.467 11.6509 11.8086 10.0009 11.8086Z"
                                stroke="#50555C" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                        </svg>
                    </span>
                    <span class="text">Users</span>
                </a>
            </li>

            <li class="">
                <a href="{{ route('statesList') }}" class="">
                    <span class="dashboard_icon">
                        <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <svg width="18" height="18" viewBox="0 0 18 18" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M14.8653 15.3903C14.7253 15.3903 14.5923 15.3343 14.4943 15.2363L10.0288 10.7708C9.82584 10.5678 9.82584 10.2319 10.0288 10.0289L15.4393 4.6185C15.5723 4.48552 15.7752 4.43652 15.9571 4.48552C16.1391 4.54151 16.2791 4.68849 16.3211 4.87047C16.4541 5.46541 16.5241 6.13033 16.5241 6.90024V11.0998C16.5241 13.0385 16.1181 14.3474 15.2362 15.2363C15.1382 15.3343 14.9913 15.3553 14.8653 15.3903ZM11.1417 10.3998L14.8233 14.0814C15.2642 13.4025 15.4742 12.4366 15.4742 11.0998V6.90024C15.4742 6.61327 15.4672 6.3473 15.4462 6.09533L11.1417 10.3998Z" fill="#574244"/>
                            <path d="M11.0997 16.5241H6.90018C6.13027 16.5241 5.47235 16.4611 4.87042 16.3211C4.68144 16.2791 4.53444 16.1391 4.48545 15.9571C4.42945 15.7752 4.48545 15.5792 4.61844 15.4392L10.0288 10.0288C10.2318 9.82584 10.5678 9.82584 10.7708 10.0288L15.2362 14.4943C15.3342 14.5923 15.3902 14.7253 15.3902 14.8653C15.3902 15.0053 15.3342 15.1382 15.2362 15.2362C14.3473 16.1181 13.0385 16.5241 11.0997 16.5241ZM6.09528 15.4462C6.34725 15.4672 6.61322 15.4742 6.90018 15.4742H11.0997C12.4436 15.4742 13.4025 15.2642 14.0814 14.8233L10.3998 11.1417L6.09528 15.4462Z" fill="#574244"/>
                            <path fill-rule="evenodd" clip-rule="evenodd" d="M4.88619 16.3238L4.88618 16.3238L4.87045 16.3211C2.55371 15.7892 1.47583 14.1304 1.47583 11.0997V6.90021C1.47583 3.09965 3.09965 1.47583 6.90021 1.47583H11.0997C14.1304 1.47583 15.7892 2.55371 16.3211 4.87045C16.3631 5.04543 16.3071 5.2344 16.1812 5.36039L5.36038 16.1812C5.26239 16.2791 5.12941 16.3351 4.98942 16.3351C4.95278 16.3351 4.92147 16.3298 4.88619 16.3238ZM6.98421 9.91685C6.54326 9.91685 6.1023 9.75587 5.75234 9.42691C4.63947 8.37003 4.19154 7.2082 4.45751 6.07433C4.72348 4.91246 5.73835 4.12854 6.98421 4.12854C8.23004 4.12854 9.24499 4.91246 9.51096 6.07433C9.76993 7.2152 9.32191 8.37003 8.20904 9.42691C7.86608 9.74887 7.42516 9.91685 6.98421 9.91685ZM5.47939 6.30529C5.25541 7.25718 5.89933 8.11113 6.48027 8.66407C6.76723 8.93704 7.20818 8.93704 7.48815 8.66407C8.06206 8.11813 8.70599 7.26418 8.48901 6.30529C8.30003 5.47239 7.55814 5.17142 6.98421 5.17142C6.41027 5.17142 5.67536 5.47239 5.47939 6.30529ZM6.30524 6.5433C6.30524 6.92826 6.6202 7.24322 7.00516 7.24322C7.39011 7.24322 7.71208 6.92826 7.71208 6.5433C7.71208 6.15835 7.39712 5.84338 7.01216 5.84338H7.00516C6.6132 5.84338 6.30524 6.15835 6.30524 6.5433Z" fill="#574244"/>
                            </svg>

                    </span>
                    <span class="text">States</span>
                </a>
            </li>

            <li class="">
                <a href="{{ route('districtList') }}" class="">
                    <span class="dashboard_icon">
                        <svg width="20" height="19" viewBox="0 0 20 18" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M16.5 15.9375H1.5C1.1925 15.9375 0.9375 16.1925 0.9375 16.5C0.9375 16.8075 1.1925 17.0625 1.5 17.0625H16.5C16.8075 17.0625 17.0625 16.8075 17.0625 16.5C17.0625 16.1925 16.8075 15.9375 16.5 15.9375Z" fill="#574244"/>
                            <path d="M12.75 1.5H5.25C3 1.5 2.25 2.8425 2.25 4.5V16.5H15.75V4.5C15.75 2.8425 15 1.5 12.75 1.5ZM7.5 12.9375H5.25C4.9425 12.9375 4.6875 12.6825 4.6875 12.375C4.6875 12.0675 4.9425 11.8125 5.25 11.8125H7.5C7.8075 11.8125 8.0625 12.0675 8.0625 12.375C8.0625 12.6825 7.8075 12.9375 7.5 12.9375ZM7.5 9.5625H5.25C4.9425 9.5625 4.6875 9.3075 4.6875 9C4.6875 8.6925 4.9425 8.4375 5.25 8.4375H7.5C7.8075 8.4375 8.0625 8.6925 8.0625 9C8.0625 9.3075 7.8075 9.5625 7.5 9.5625ZM7.5 6.1875H5.25C4.9425 6.1875 4.6875 5.9325 4.6875 5.625C4.6875 5.3175 4.9425 5.0625 5.25 5.0625H7.5C7.8075 5.0625 8.0625 5.3175 8.0625 5.625C8.0625 5.9325 7.8075 6.1875 7.5 6.1875ZM12.75 12.9375H10.5C10.1925 12.9375 9.9375 12.6825 9.9375 12.375C9.9375 12.0675 10.1925 11.8125 10.5 11.8125H12.75C13.0575 11.8125 13.3125 12.0675 13.3125 12.375C13.3125 12.6825 13.0575 12.9375 12.75 12.9375ZM12.75 9.5625H10.5C10.1925 9.5625 9.9375 9.3075 9.9375 9C9.9375 8.6925 10.1925 8.4375 10.5 8.4375H12.75C13.0575 8.4375 13.3125 8.6925 13.3125 9C13.3125 9.3075 13.0575 9.5625 12.75 9.5625ZM12.75 6.1875H10.5C10.1925 6.1875 9.9375 5.9325 9.9375 5.625C9.9375 5.3175 10.1925 5.0625 10.5 5.0625H12.75C13.0575 5.0625 13.3125 5.3175 13.3125 5.625C13.3125 5.9325 13.0575 6.1875 12.75 6.1875Z" fill="#574244"/>
                        </svg>

                    </span>
                    <span class="text">Districts</span>
                </a>
            </li>

            <li class="">
                <a href="{{ route('talukaList') }}" class="">
                    <span class="dashboard_icon">

                       <svg width="18" height="18" viewBox="0 0 18 18" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <mask id="mask0_1492_5350" style="mask-type:luminance" maskUnits="userSpaceOnUse" x="1" y="1" width="16" height="16">
                            <path d="M16.9631 1.03687H1.03693V16.963H16.9631V1.03687Z" fill="white"/>
                            </mask>
                            <g mask="url(#mask0_1492_5350)">
                            <path fill-rule="evenodd" clip-rule="evenodd" d="M8.67749 14.3561C8.77059 14.4352 8.88552 14.4745 8.99999 14.4745C9.12043 14.4745 9.24088 14.4307 9.3359 14.3441C9.52556 14.1704 13.9769 10.066 13.9769 7.12362C13.9769 4.31614 11.7443 2.03223 8.99999 2.03223C6.25572 2.03223 4.02307 4.31614 4.02307 7.12362C4.02307 7.14922 4.02501 7.17437 4.02874 7.19893C4.07178 7.72489 4.223 8.74552 4.53669 9.16117C5.7202 11.8178 8.55705 14.2536 8.67749 14.3561ZM7.00922 7.0092C7.00922 8.10711 7.90208 8.99997 8.99998 8.99997C10.0979 8.99997 10.9908 8.10711 10.9908 7.0092C10.9908 5.91129 10.0979 5.01843 8.99998 5.01843C7.90208 5.01843 7.00922 5.91129 7.00922 7.0092Z" fill="#574244"/>
                            <path d="M15.47 15.9676H2.52998C2.3528 15.9676 2.18857 15.8735 2.09948 15.7198C2.01039 15.566 2.00989 15.3774 2.09799 15.2231L4.08875 11.7392C4.22512 11.5008 4.52921 11.4172 4.76761 11.5541C5.006 11.6905 5.08911 11.9945 4.95275 12.233L3.38751 14.9723H14.612L13.0467 12.233C12.9104 11.9945 12.9935 11.6905 13.2319 11.5541C13.4703 11.4167 13.7744 11.5008 13.9107 11.7392L15.9015 15.2231C15.9896 15.3774 15.9891 15.566 15.9 15.7198C15.8114 15.8735 15.6472 15.9676 15.47 15.9676Z" fill="#574244"/>
                            </g>
                        </svg>

                    </span>
                    <span class="text">Talukas</span>
                </a>
            </li>
            
            <li class="">
                <div class="logout-btn">
                    <span class="dashboard_icon">
                        <svg width="20" height="19" viewBox="0 0 20 19" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M12.6 1.74609H10.65C8.25 1.74609 6.75 3.24609 6.75 5.64609V8.68359H11.4375C11.745 8.68359 12 8.93859 12 9.24609C12 9.55359 11.745 9.80859 11.4375 9.80859H6.75V12.8461C6.75 15.2461 8.25 16.7461 10.65 16.7461H12.5925C14.9925 16.7461 16.4925 15.2461 16.4925 12.8461V5.64609C16.5 3.24609 15 1.74609 12.6 1.74609Z" fill="#574244"/>
                            <path d="M3.41996 8.68348L4.97246 7.13098C5.08496 7.01848 5.13746 6.87598 5.13746 6.73348C5.13746 6.59098 5.08496 6.44098 4.97246 6.33598C4.75496 6.11848 4.39496 6.11848 4.17746 6.33598L1.66496 8.84848C1.44746 9.06598 1.44746 9.42598 1.66496 9.64348L4.17746 12.156C4.39496 12.3735 4.75496 12.3735 4.97246 12.156C5.18996 11.9385 5.18996 11.5785 4.97246 11.361L3.41996 9.80848H6.74996V8.68348H3.41996Z" fill="#574244"/>
                            </svg>

                    </span>
                    <span class="text"><button type="button" class="" data-toggle="modal"
                            data-target="#exampleModal"> Log Out</button></span>
                </div>
            </li>
        </ul>
    </div>
</div>

<!-- Modal -->
<div class="logout-modal">
    <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
        aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">

                <div class="modal-body">
                    <h3>Are you sure to log out?</h3>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn lg-btn" data-dismiss="modal">Cancel</button>
                    {{-- <button type="button" class="btn lg-btn lg-yellow">Sign out</button> --}}
                    <a href="{{ route('logout') }}" class="btn lg-btn lg-yellow">Log out</a>
                </div>
            </div>
        </div>
    </div>
</div>


<script>
    $(document).ready(function() {
        var url = window.location.href; // get the current URL
        var urlParts = url.split('/'); // split the URL by '/'
        var mainName = urlParts[3]; // get the last part of the URL

        $(".navigation-column ul li a").each(function() {
            var link = $(this).attr('href'); // get the link's URL
            var linkParts = link.split('/'); // split the link's URL by '/'
            var linkMainName = linkParts[3];
            if (mainName === linkMainName) {
                $(this).parent('li').addClass("active");
                $(this).parent().parent().show();
                $(this).parent().parent().prev().toggleClass('child-open');
            }
        });
    });
</script>

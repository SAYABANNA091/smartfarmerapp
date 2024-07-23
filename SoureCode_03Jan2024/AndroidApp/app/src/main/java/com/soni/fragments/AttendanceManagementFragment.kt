package com.soni.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.archit.calendardaterangepicker.customviews.CalendarListener
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.soni.R
import com.soni.SoniApp
import com.soni.activity.AddEditEmployeeActivity
import com.soni.activity.BaseActivity
import com.soni.activity.HomeActivity
import com.soni.adapter.*
import com.soni.databinding.FragmentAttendanceManagementBinding
import com.soni.services.web.models.*
import com.soni.utils.Const
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class AttendanceManagementFragment : BaseFragment() {

    lateinit var binding: FragmentAttendanceManagementBinding
    lateinit var daysAdapter: AttendanceDateAdapter
    lateinit var daysLinearLayoutManager: LinearLayoutManager
    lateinit var employeeAdapter: AttendanceEmployeeAdapter
    lateinit var employeeAdapterShimmer: AttendanceEmployeeAdapterShimmer
    lateinit var employeeAdapterShimmer2: AttendanceEmployeeAdapterShimmer
    lateinit var employeeLinearLayoutManager: LinearLayoutManager
    lateinit var attendanceViewTabAdapter: AttendanceViewTabAdapter
    lateinit var attendanceViewLinearLayoutManager: LinearLayoutManager
    lateinit var addAttendanceTab: TabLayout.Tab
    lateinit var viewAttendanceTab: TabLayout.Tab
    lateinit var bottomSheetDialog: BottomSheetDialog

    var limit = 20

    var addTabPage = 1
    var addTabTotalPage = 1
    var addTabEmployeeList: ArrayList<EmployeeData> = arrayListOf()
    var addTabSearchText = ""
    var addTabDateText = "2023-04-10"
    var addTabMonthText = "2023-04-10"
    val sdf = SimpleDateFormat("MMMM yyyy")
    val formatterDay = SimpleDateFormat("dd")
    val formatterDate = SimpleDateFormat("EEE")

    var viewTabPage = 1
    var viewTabTotalPage = 1
    var viewTabEmployeeList: ArrayList<EmployeeListData> = arrayListOf()

    var viewTabListPage = 1
    var viewTabListTotalPage = 1
    var viewTabListEmployee: ArrayList<EmployeeAtt> = arrayListOf()

    var currentTime: Date = Calendar.getInstance().time

    var error = ""
    var isLoading=false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // return inflater.inflate(R.layout.fragment_attendance_management, container, false)
        binding = FragmentAttendanceManagementBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addTabDateText = currentTime.yyyyMMDD()
        addTabMonthText = currentTime.MonthYear()
        binding.tvMonth.text=currentTime.MonthYear()
        init()

    }
    private fun init() {
        ivBack = requireView().findViewById(R.id.iv_back)
        ivRight = requireView().findViewById(R.id.iv_right)
        tvTitle = requireView().findViewById(R.id.tv_title)

        tvTitle.setText(getString(R.string.attendance_management))
        ivRight.visibility = View.INVISIBLE

        ivBack.setOnClickListener {
            (requireActivity() as HomeActivity).onBackPressed()
        }

        binding.root.setOnClickListener {
            requireActivity().hideKeyboard(binding.root)
            binding.etSearch.clearFocus();
        }

        binding.ivAddEmployee.setOnClickListener {
            if (addAttendanceTab.isSelected) {
                val mIntent = Intent(requireActivity(), AddEditEmployeeActivity::class.java)
                mIntent.putExtra(Const.IntentKey.isEdit, false)
                requireActivity().startActivity(mIntent)
            } else {
                showBottomSheet()
            }
        }

        daysLinearLayoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        daysAdapter = AttendanceDateAdapter(requireActivity()) { it ->
            addTabDateText = it.yyyyMMDD()
            callGetAttendanceFilterApi(false)
        }

        binding.ivNextMonth.setOnClickListener {
            val d1 =  binding.tvMonth.text.toString()
            val d2 = Calendar.getInstance().time.MonthYear()

            val firstDate: Date = sdf.parse(d1)
            val secondDate: Date = sdf.parse(d2)

            if(secondDate.after(firstDate)){
                var newm=Calendar.getInstance()
                newm.time=firstDate
                newm.add(Calendar.MONTH,1)
                binding.tvMonth.text=newm.time.MonthYear()
                val difference: Long = Math.abs((Calendar.getInstance().time.time - newm.time.time))
                val differenceDates = (difference / (24 * 60 * 60 * 1000)).toInt()
                binding.mainSingleRowCalendar.scrollToPosition(18250-differenceDates)
            }

        }

        binding.ivPreviousMonth.setOnClickListener {
            val d1 =  binding.tvMonth.text.toString()

            val firstDate: Date = sdf.parse(d1)

                var newm=Calendar.getInstance()
                newm.time=firstDate
                newm.add(Calendar.MONTH,-1)
                binding.tvMonth.text=newm.time.MonthYear()
            val difference: Long = Math.abs((Calendar.getInstance().time.time - newm.time.time))
            val differenceDates = (difference / (24 * 60 * 60 * 1000)).toInt()
            binding.mainSingleRowCalendar.scrollToPosition(18250-differenceDates)
//                daysAdapter.onMonthChange(newm,false)

        }
        daysLinearLayoutManager.setReverseLayout(true);
        daysLinearLayoutManager.setStackFromEnd(true);
        binding.rvDate.layoutManager = daysLinearLayoutManager
        binding.rvDate.adapter = daysAdapter
        binding.rvDate.scrollToPosition(0);

        employeeAdapter = AttendanceEmployeeAdapter(requireActivity(), addTabEmployeeList){it->
            binding.ckSelectAllAttendance.isChecked=it
        }
        employeeLinearLayoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvEmployees.layoutManager = employeeLinearLayoutManager
        binding.rvEmployees.adapter = employeeAdapter

        employeeAdapterShimmer = AttendanceEmployeeAdapterShimmer(requireActivity())
        var mEmployeeLinearLayoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvEmployeesShimmer.layoutManager = mEmployeeLinearLayoutManager
        binding.rvEmployeesShimmer.adapter = employeeAdapterShimmer
        employeeAdapterShimmer2 = AttendanceEmployeeAdapterShimmer(requireActivity())
        var mEmployeeLinearLayoutManager2 = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvEmployeesAttendanceShimmer.layoutManager = mEmployeeLinearLayoutManager2
        binding.rvEmployeesAttendanceShimmer.adapter = employeeAdapterShimmer2

        binding.rvEmployees.addOnScrollListener(object : RecyclerView.OnScrollListener()
        {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
            {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading)
                {

                    if (employeeLinearLayoutManager.findLastCompletelyVisibleItemPosition() >= employeeAdapter.itemCount - 10)
                    {
                        // call data
                        loadmore()
                        isLoading=true

                    }
                }
            }
        })
        attendanceViewTabAdapter = AttendanceViewTabAdapter(requireActivity(),viewTabListEmployee)
        attendanceViewLinearLayoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvEmployeesAttendance.layoutManager = attendanceViewLinearLayoutManager
        binding.rvEmployeesAttendance.adapter = attendanceViewTabAdapter


        addAttendanceTab = binding.tabLayoutAttendance.newTab()
        addAttendanceTab.id = 0
        addAttendanceTab.text = requireActivity().resources.getString(R.string.add_attendance)
        viewAttendanceTab = binding.tabLayoutAttendance.newTab()
        viewAttendanceTab.id = 1
        viewAttendanceTab.text = requireActivity().resources.getString(R.string.view_attendance)

        binding.tabLayoutAttendance.addTab(addAttendanceTab)
        binding.tabLayoutAttendance.addTab(viewAttendanceTab)


        binding.ivClearDate.setOnClickListener {
            binding.llViewAttendance.visibility=View.GONE
            binding.llEmptySearch.visibility=View.VISIBLE

        }

        binding.tabLayoutAttendance.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab!!.id) {
                        0 -> {
                            binding.llAddAttendance.visibility = View.VISIBLE
                            binding.llEmptySearch.visibility = View.GONE
                            binding.llViewAttendance.visibility = View.GONE
                            binding.ivAddEmployee.setImageDrawable(
                                requireActivity().resources.getDrawable(
                                    R.drawable.add_icon
                                )
                            )
                        }
                        1 -> {
                            binding.llEmptySearch.visibility = View.VISIBLE
                            binding.llAddAttendance.visibility = View.GONE
                            binding.llViewAttendance.visibility = View.GONE
                            binding.ivAddEmployee.setImageDrawable(
                                requireActivity().resources.getDrawable(
                                    R.drawable.filter_icon
                                )
                            )

                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            }
        )

        binding.ckSelectAllAttendance.setOnClickListener {
            for (i in 0 until addTabEmployeeList.size) {
                addTabEmployeeList[i].isSelected = binding.ckSelectAllAttendance.isChecked
            }
            employeeAdapter.onUpdate()
        }

        binding.etSearch.setOnFocusChangeListener { view, b ->
            (requireActivity() as HomeActivity).onFocusChange(binding.llSearch, b)
        }
        binding.etSearch.doOnTextChanged { text, start, before, count ->
            addTabSearchText = text.toString()
        }
        binding.etSearch.setOnEditorActionListener { textView, i, keyEvent ->
            addTabSearchText = binding.etSearch.text.toString()
            (requireActivity() as BaseActivity).hideKeyboard(binding.root)
            binding.etSearch.clearFocus();
            callGetAttendanceFilterApi(false)
            true
        }
        binding.ivClearSearch.setOnClickListener {
            addTabSearchText = ""
            binding.etSearch.setText("")
            (requireActivity() as BaseActivity).hideKeyboard(binding.root)
            binding.etSearch.clearFocus();
            callGetAttendanceFilterApi(false)
        }


        binding.llAddAttendance.visibility = View.VISIBLE
        binding.llEmptySearch.visibility = View.GONE
        binding.llViewAttendance.visibility = View.GONE

        binding.txtAddAtt.setOnClickListener {
            if (addTabEmployeeList.isEmpty()){
                (requireActivity() as HomeActivity).showToast(requireActivity().getString(R.string.no_employee_has_been_added_yet))
            }
            else{
                var employees = ""
                addTabEmployeeList.forEach {
                    if (it.isAttend == 0 && it.isSelected!!) {
                        employees += if (employees.isEmpty()) {
                            "${it.id}"
                        } else {
                            ",${it.id}"
                        }
                    }
                }
                var remove = ""
                addTabEmployeeList.forEach {
                    if (!it.isSelected!!) {
                        remove += if (remove.isEmpty()) {
                            "${it.id}"
                        } else {
                            ",${it.id}"
                        }
                    }
                }
                callAddAttendanceFilterApi(employees, remove)
            }
        }
        val myCalendarViewManager = object : CalendarViewManager {
            override fun setCalendarViewResourceId(
                position: Int,
                date: Date,
                isSelected: Boolean
            ): Int {
                // return item layout files, which you have created
                return if (!isSelected) {
                    R.layout.calendar_day_layout_selected
                } else{
                    R.layout.calendar_day_layout
                }
            }

            override fun bindDataToCalendarView(
                holder: SingleRowCalendarAdapter.CalendarViewHolder,
                date: Date,
                position: Int,
                isSelected: Boolean
            ) {
              var tvday=  holder.itemView.findViewById<TextView>(R.id.tv_day_calendar_item)
              var tvdate=  holder.itemView.findViewById<TextView>(R.id.tv_date_calendar_item)
                tvday.text = formatterDay.format(date)
                tvdate.text = formatterDate.format(date)
            }
        }
        val mySelectionManager = object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                // return true if item can be selected
                return true
            }
        }

        val myCalendarChangesObserver = object : CalendarChangesObserver {
            override fun whenWeekMonthYearChanged(
                weekNumber: String,
                monthNumber: String,
                monthName: String,
                year: String,
                date: Date
            ) {
                super.whenWeekMonthYearChanged(weekNumber, monthNumber, monthName, year, date)
                binding.tvMonth.text="${monthName} ${year}"
            }

            override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
                super.whenSelectionChanged(isSelected, position, date)
                binding.mainSingleRowCalendar.scrollToPosition(position)
                if (isSelected) {
                    addTabDateText = date.yyyyMMDD()

                    callGetAttendanceFilterApi(false)
                }

            }

            override fun whenCalendarScrolled(dx: Int, dy: Int) {
                super.whenCalendarScrolled(dx, dy)
            }

            override fun whenSelectionRestored() {
                super.whenSelectionRestored()
            }

            override fun whenSelectionRefreshed() {
                super.whenSelectionRefreshed()
            }
        }

            binding.mainSingleRowCalendar.apply {
                calendarViewManager = myCalendarViewManager
                calendarChangesObserver = myCalendarChangesObserver
                calendarSelectionManager = mySelectionManager
                initialPositionIndex=18250
                futureDaysCount=0
                pastDaysCount = 18250
                includeCurrentDate = true
                init()
            }
            binding.mainSingleRowCalendar.select(18250)
            callGetAttendanceFilterApi(false)
            callGetViewAttendanceFilterApi(false)
    }

    private fun loadmore(){
        if(addTabPage< addTabTotalPage!!){
            addTabPage++
            callGetAttendanceFilterApi(true)

        }
    }
    override fun onResume() {
        super.onResume()

        callGetAttendanceFilterApi(false)
    }
    private fun showBottomSheet() {

        val layout: View = LayoutInflater.from(requireActivity())
            .inflate(R.layout.layout_bottom_sheet_filter, null)


        bottomSheetDialog = BottomSheetDialog(requireActivity())
        bottomSheetDialog.setContentView(layout)


        bottomSheetDialog.setOnShowListener(DialogInterface.OnShowListener { dialog ->
            val bottom_dialog = dialog as BottomSheetDialog
            val bottomSheet =
                (bottom_dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?)!!
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED)

            val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
            behavior.setBottomSheetCallback(object : BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
            bottomSheet.setBackgroundResource(R.drawable.transparent_5sdp_radius)
            val ivEmployeeDropDown = layout.findViewById<ImageView>(R.id.iv_employee_dropdown)
            val ivDateDropDown = layout.findViewById<ImageView>(R.id.iv_date_dropdown)
            val ll_employee_filter = layout.findViewById<LinearLayout>(R.id.ll_employee_filter)
            val ll_date_filter = layout.findViewById<LinearLayout>(R.id.ll_date_filter)
            val ll_select_emp = layout.findViewById<LinearLayout>(R.id.ll_select_emp)
            val ll_select_date = layout.findViewById<LinearLayout>(R.id.ll_select_date)
            val tv_apply_filter = layout.findViewById<TextView>(R.id.tv_apply_filter)
            val tv_clear_all = layout.findViewById<TextView>(R.id.tv_clear_all)
            val ck_is_all = layout.findViewById<CheckBox>(R.id.ck_is_all)
            val rv_filter_employee_list =
                layout.findViewById<RecyclerView>(R.id.rv_filter_employee_list)
            val cl_cal =
                layout.findViewById<DateRangeCalendarView>(R.id.calendar)
            var startDate:Date?=null
            var endDate:Date?=null

            cl_cal.setCalendarListener(object : CalendarListener {
                override fun onFirstDateSelected(sDate: Calendar) {
                    startDate=sDate.time
                    endDate=null
                }

                override fun onDateRangeSelected(sDate: Calendar, eDate: Calendar) {

                   startDate=sDate.time
                   endDate=eDate.time
                }
            })

            var startDateSelectable=Calendar.getInstance()
            startDateSelectable.add(Calendar.YEAR,-2)
            var endDateSelectable=Calendar.getInstance()

            cl_cal.setSelectableDateRange(startDateSelectable, endDateSelectable);
            var isList = true
            var isCalander = false
            ivDateDropDown.rotation = 180f



            ll_select_emp.setOnClickListener {
                if (!isList) {
                    ll_employee_filter.visibility = View.VISIBLE
                    ll_date_filter.visibility = View.GONE
                    ivEmployeeDropDown.rotation = 0f
                    ivDateDropDown.rotation = 180f
                    isList = true
                } else {
                    isList = false
                    ll_employee_filter.visibility = View.GONE
                    ivEmployeeDropDown.rotation = 180f
                }

            }
            ll_select_date.setOnClickListener {
                if (!isCalander) {
                    isCalander = true
                    ll_employee_filter.visibility = View.GONE
                    ll_date_filter.visibility = View.VISIBLE
                    ivDateDropDown.rotation = 0f
                    ivEmployeeDropDown.rotation = 180f
                } else {
                    isCalander = false
                    ll_date_filter.visibility = View.GONE
                    ivEmployeeDropDown.rotation = 0f
                }
            }
            tv_apply_filter.setOnClickListener {
                var isEmployeeSelected=false
                viewTabEmployeeList.forEach {
                    if(it.isSelected){
                        isEmployeeSelected=true

                    }
                }
                if(!isEmployeeSelected){
                    (requireActivity() as BaseActivity).showToast(getString(R.string.view_att_select_employee))
                }else if (startDate == null ){
                    (requireActivity() as BaseActivity).showToast(getString(R.string.view_att_select_startdate))
                }else  if (endDate == null ){
                    (requireActivity() as BaseActivity).showToast(getString(R.string.view_att_select_enddate))
                }else {
                    binding.tvDateRange.text = startDate!!.fullDate() + "-" + endDate!!.fullDate()
                    var employees = ""
                    viewTabEmployeeList.forEach {
                        if (it.isSelected) {
                            if (employees.isEmpty()) {
                                employees += "${it.id}"
                            } else {
                                employees += ",${it.id}"

                            }
                        }
                    }
                    callGetAttendanceListApi(
                        employees,
                        startDate!!.fullDateApi(),
                        endDate!!.fullDateApi(),
                        false
                    )
                    bottomSheetDialog.dismiss()
                    binding.llEmptySearch.visibility = View.GONE
                    binding.llAddAttendance.visibility = View.GONE
                    binding.llViewAttendance.visibility = View.VISIBLE
                    binding.ivAddEmployee.setImageDrawable(requireActivity().resources.getDrawable(R.drawable.filter_icon))
                }
            }


            val mEmployeeAdapter = FilterEmployeeAdapter(requireActivity(),viewTabEmployeeList){
                ck_is_all.isChecked=it
            }
            val mEmployeeLinearLayoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.VERTICAL,
                false
            )
            rv_filter_employee_list.layoutManager = mEmployeeLinearLayoutManager
            rv_filter_employee_list.adapter = mEmployeeAdapter
            ck_is_all.setOnClickListener {
                for (i in 0 until viewTabEmployeeList.size){
                    viewTabEmployeeList[i].isSelected=ck_is_all.isChecked

                }
                mEmployeeAdapter.notifyDataSetChanged()
            }
            tv_clear_all.setOnClickListener {
                for (i in 0 until viewTabEmployeeList.size){
                    viewTabEmployeeList[i].isSelected=false

                }
                ck_is_all.isChecked=false
                mEmployeeAdapter.notifyDataSetChanged()
                cl_cal.resetAllSelectedViews()
            }

        })
        for (i in 0 until viewTabEmployeeList.size){
            viewTabEmployeeList[i].isSelected=false

        }
        bottomSheetDialog.show()
    }

    private fun callGetAttendanceFilterApi(isPage: Boolean) {

        if (!isPage) {
            addTabPage = 1
            binding.rvEmployees.visibility = View.GONE
            binding.svEmployeesShimmer.visibility = View.VISIBLE

        }
        binding.tvNoEmployeesAddedYet.visibility = View.GONE
        binding.tvNoEmployees.visibility = View.GONE

        if ((requireActivity() as HomeActivity).isInternetAvailable(requireActivity() as HomeActivity)) {

            var param: HashMap<String, String> = HashMap()
            param["employee_name"] = addTabSearchText
            param["attend_date"] = addTabDateText
            param["Page"] = addTabPage.toString()
            param["Limit"] = limit.toString()

            SoniApp.mApiCall?.postGetAttendanceFilter(param)
                ?.enqueue(object : Callback<AddAttendanceFilterResponse> {
                    override fun onFailure(call: Call<AddAttendanceFilterResponse>, t: Throwable) {
                        (requireActivity() as HomeActivity).showToast(getString(R.string.api_error))
                        binding.rvEmployees.visibility = View.VISIBLE
                        binding.svEmployeesShimmer.visibility = View.GONE

                    }

                    override fun onResponse(
                        call: Call<AddAttendanceFilterResponse>,
                        response: Response<AddAttendanceFilterResponse>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!) {
                                    if (responseLogin.status!!) {
                                        if (!isPage) {
                                            addTabEmployeeList.clear()
                                        }
                                        var isAll=true
                                        for (i in 0 until responseLogin.data.size){
                                            if (responseLogin.data[i].isAttend==0){
                                                isAll=false
                                            }
                                            responseLogin.data[i].isSelected=responseLogin.data[i].isAttend==1
                                        }
                                        binding.ckSelectAllAttendance.isChecked=isAll

                                        addTabEmployeeList.addAll(responseLogin.data)
                                        addTabTotalPage = responseLogin.totalpage!!
                                        employeeAdapter.onUpdate()
//                                        userAccountListAdapter.onDelete(id)
                                        binding.rvEmployees.visibility = View.VISIBLE
                                        binding.svEmployeesShimmer.visibility = View.GONE
                                        binding.ckSelectAllAttendance.visibility=View.VISIBLE
                                        if (addTabPage==1 &&addTabEmployeeList.isEmpty() ){
                                            binding.tvNoEmployeesAddedYet.visibility = View.VISIBLE
                                            binding.rvEmployees.visibility = View.GONE
                                            binding.ckSelectAllAttendance.visibility=View.INVISIBLE
                                        }
                                       else if (addTabEmployeeList.isEmpty()){
                                            binding.tvNoEmployees.visibility = View.VISIBLE
                                            binding.rvEmployees.visibility = View.GONE
                                            binding.ckSelectAllAttendance.visibility=View.INVISIBLE
                                        }


                                    }
                                } else {

                                }

                            }
                        }
                    }


                })

        } else {
            (requireActivity() as HomeActivity).showToast(getString(R.string.internet_error))
            binding.rvEmployees.visibility = View.VISIBLE
            binding.svEmployeesShimmer.visibility = View.GONE

        }
    }

      private fun callGetAttendanceListApi(employees:String, startDate:String,endDate:String,isPage: Boolean) {
        if (!isPage) {
            addTabPage = 1
            binding.rvEmployeesAttendance.visibility = View.GONE
            binding.svViewEmployeesShimmer.visibility = View.VISIBLE
        }

        if ((requireActivity() as HomeActivity).isInternetAvailable(requireActivity() as HomeActivity)) {

            var param: HashMap<String, String> = HashMap()
            param["employee_ids"] = employees
            param["from_date"] = startDate
            param["to_date"] = endDate
            param["Page"] = viewTabListPage.toString()
            param["Limit"] = limit.toString()

            SoniApp.mApiCall?.postListEmployeeAtt(param)
                ?.enqueue(object : Callback<EmployeeAttListResponse> {
                    override fun onFailure(call: Call<EmployeeAttListResponse>, t: Throwable) {
                        (requireActivity() as HomeActivity).showToast(getString(R.string.api_error))
                        binding.rvEmployeesAttendance.visibility = View.VISIBLE
                        binding.svViewEmployeesShimmer.visibility = View.GONE

                    }

                    override fun onResponse(
                        call: Call<EmployeeAttListResponse>,
                        response: Response<EmployeeAttListResponse>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!) {
                                    if (responseLogin.status!!) {
                                        if (!isPage) {
                                            viewTabListEmployee.clear()
                                        }


                                        viewTabListEmployee.addAll(responseLogin.data)
                                        viewTabListTotalPage = responseLogin.totalpage!!
                                        attendanceViewTabAdapter.notifyDataSetChanged()
//                                        userAccountListAdapter.onDelete(id)
                                        binding.rvEmployeesAttendance.visibility = View.VISIBLE
                                        binding.svViewEmployeesShimmer.visibility = View.GONE
//                                        if (addTabPage==1 &&addTabEmployeeList.isEmpty() ){
//                                            binding.tvNoEmployeesAddedYet.visibility = View.VISIBLE
//                                            binding.rvEmployees.visibility = View.GONE
//                                            binding.ckSelectAllAttendance.visibility=View.INVISIBLE
//                                        }
//                                       else if (addTabEmployeeList.isEmpty()){
//                                            binding.tvNoEmployees.visibility = View.VISIBLE
//                                            binding.rvEmployees.visibility = View.GONE
//                                            binding.ckSelectAllAttendance.visibility=View.INVISIBLE
//                                        }


                                    }
                                } else {

                                }

                            }
                        }
                    }


                })

        } else {
            (requireActivity() as HomeActivity).showToast(getString(R.string.internet_error))
            binding.rvEmployeesAttendance.visibility = View.VISIBLE
            binding.svViewEmployeesShimmer.visibility = View.GONE

        }
    }

    private fun callGetViewAttendanceFilterApi(isPage: Boolean) {
        if (!isPage) {
            addTabPage = 1
        }
        if ((requireActivity() as HomeActivity).isInternetAvailable(requireActivity() as HomeActivity)) {

            var param: HashMap<String, String> = HashMap()
            param["Page"] = viewTabPage.toString()
            param["Limit"] = "20"

            SoniApp.mApiCall?.postListEmployee(param)
                ?.enqueue(object : Callback<EmployeeListResponse> {
                    override fun onFailure(call: Call<EmployeeListResponse>, t: Throwable) {
                        (requireActivity() as HomeActivity).showToast(getString(R.string.api_error))
                        binding.rvEmployees.visibility = View.VISIBLE
                        binding.svEmployeesShimmer.visibility = View.GONE

                    }

                    override fun onResponse(
                        call: Call<EmployeeListResponse>,
                        response: Response<EmployeeListResponse>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!) {
                                    if (responseLogin.status!!) {
                                        if (!isPage) {
                                            addTabEmployeeList.clear()
                                        }

                                        viewTabEmployeeList.addAll(responseLogin.data)
                                        viewTabTotalPage = responseLogin.totalpage!!

                                    }
                                } else {

                                }

                            }
                        }
                    }


                })

        } else {
            (requireActivity() as HomeActivity).showToast(getString(R.string.internet_error))


        }
    }

    private fun callAddAttendanceFilterApi(employees:String,remove:String) {
        binding.txtAddAtt.visibility = View.GONE
        binding.txtAddAttShimmer.visibility = View.VISIBLE
        if ((requireActivity() as HomeActivity).isInternetAvailable(requireActivity() as HomeActivity)) {


            var param: HashMap<String, String> = HashMap()
            param["attend_date"] = addTabDateText
            param["employees_added"] = employees
            param["employees_removed"] = remove

            SoniApp.mApiCall?.postAddAttendance(param)
                ?.enqueue(object : Callback<BaseModel> {
                    override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                        (requireActivity() as HomeActivity).showToast(getString(R.string.api_error))
                        binding.txtAddAtt.visibility = View.VISIBLE
                        binding.txtAddAttShimmer.visibility = View.GONE

                    }

                    override fun onResponse(
                        call: Call<BaseModel>,
                        response: Response<BaseModel>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!) {
                                    if (responseLogin.status!!) {
                                        (requireActivity() as HomeActivity).showToast(responseLogin.message!!)
                                        binding.txtAddAtt.visibility = View.VISIBLE
                                        binding.txtAddAttShimmer.visibility = View.GONE

                                    }
                                } else {

                                }

                            }
                        }
                    }


                })

        } else {
            (requireActivity() as HomeActivity).showToast(getString(R.string.internet_error))
            binding.txtAddAtt.visibility = View.VISIBLE
            binding.txtAddAttShimmer.visibility = View.GONE

        }
    }

    fun Date.MonthYear(): String {
        val format = SimpleDateFormat("MMMM yyyy")
        return format.format(this)
    }
    fun Date.day(): String {
        val format = SimpleDateFormat("dd")
        return format.format(this)
    }
    fun Date.date(): String {
        val format = SimpleDateFormat("EE")
        return format.format(this)
    }
    fun Date.fullDate(): String {
        val format = SimpleDateFormat("dd MMMM yyyy")
        return format.format(this)
    }
    fun Date.fullDateApi(): String {
        val format = SimpleDateFormat("yyy-MM-dd")
        return format.format(this)
    }
//    postGetAttendanceFilter
}
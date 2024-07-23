package com.soni.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract.CalendarAlerts
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.soni.Preference.getCurrentLanguageID
import com.soni.R
import com.soni.SoniApp
import com.soni.databinding.ActivityEmployeeDetailsBinding
import com.soni.services.web.models.Attendance
import com.soni.services.web.models.AttendanceResponse
import com.soni.services.web.models.BaseModel
import com.soni.services.web.models.EmployeeData
import com.soni.utils.Const
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class EmployeeDetailsActivity : BaseActivity() {

       lateinit var binder:ActivityEmployeeDetailsBinding
       var employee: EmployeeData?=null
       var listAttendance:ArrayList<Attendance> = arrayListOf()
       var seletedDays:MutableList<Calendar> = ArrayList()
    var maxDate= Calendar.getInstance()

    val sdf = SimpleDateFormat("yyyy-MM-dd")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoniApp.changeAppLanguage(this, getCurrentLanguageID())
        binder= ActivityEmployeeDetailsBinding.inflate(layoutInflater)
        setContentView(binder.root)
        employee=intent.getSerializableExtra(Const.IntentKey.Employee) as EmployeeData
        inti()
       }
    private fun inti(){
        ivBack=findViewById(R.id.iv_back)
        ivRight=findViewById(R.id.iv_right)
        title=findViewById(R.id.tv_title)



        title.setText(getString(R.string.employee_details))
        ivRight.setImageDrawable(resources.getDrawable(R.drawable.trash))
        var paddinf=getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._7sdp).toInt()
        ivRight.setPadding(paddinf,paddinf,paddinf,paddinf) // =
        ivRight.setOnClickListener {
            showLanguageDialog()
        }

        ivBack.setOnClickListener { finish() }
        binder.ivEdit.setOnClickListener {
            var mIntent=Intent(this, AddEditEmployeeActivity::class.java)
            mIntent.putExtra(Const.IntentKey.isEdit,true)
            mIntent.putExtra(Const.IntentKey.Employee,employee)
            startActivity(mIntent)
        }
        if (employee!=null) {
            setData()
        }

        binder.calendarView.setMaximumDate(maxDate)

        binder.ivPreviousMonth.setOnClickListener {
            var now=binder.calendarView.currentPageDate
            now.add(Calendar.MONTH,-1)
            binder.calendarView.setDate(now)
            callAddAttendanceFilterApi((now.get(Calendar.MONTH)+1).toString(),now.get(Calendar.YEAR).toString())
        }
        binder.ivNextMonth.setOnClickListener {
            var now=binder.calendarView.currentPageDate
            now.add(Calendar.MONTH,1)
            if (now.time.before(maxDate.time)) {
                binder.calendarView.setDate(now)
                callAddAttendanceFilterApi((now.get(Calendar.MONTH)+1).toString(),now.get(Calendar.YEAR).toString())
            }
        }
        var now= Calendar.getInstance()
        callAddAttendanceFilterApi((now.get(Calendar.MONTH)+1).toString(),now.get(Calendar.YEAR).toString())
//        calendarView.setEvents(events)
    }

    override fun onResume() {
        var now= Calendar.getInstance()
        callAddAttendanceFilterApi((now.get(Calendar.MONTH)+1).toString(),now.get(Calendar.YEAR).toString())

        super.onResume()
    }
    fun setData(){
        binder.employeeName.text=employee!!.employeeName.toString()
        if(employee!!.employeeEmail.toString()=="null")
        {
            binder.employeeEmail.text=""
        }
        else{
            binder.employeeEmail.text = employee!!.employeeEmail.toString()
        }
        if(employee!!.employeeId.toString()=="null")
        {
            binder.employeeId.text=""
        }
        else{
            binder.employeeId.text=employee!!.employeeId.toString()
        }
        if(employee!!.mobileNo.toString()=="null")
        {
            binder.employeeMobile.text=""
        }
        else{
            binder.employeeMobile.text=employee!!.mobileNo.toString()
        }
        if(employee!!.department.toString()=="null")
        {
            binder.employeeDept.text=""
        }
        else{
            binder.employeeDept.text = employee!!.department.toString()
        }




        Glide.with(this)
            .load(employee!!.profilePicture)
            .placeholder(R.drawable.logo)
            .error(R.drawable.logo)
            .into(binder.ivEmployeePic)



    }

    private fun callAddAttendanceFilterApi(month:String,year:String) {
        binder.svShimmer.visibility = View.VISIBLE
        if (isInternetAvailable(this)) {


            var param: HashMap<String, String> = HashMap()
            param["employee_id"] = employee!!.id.toString()
            param["month"] = month
            param["year"] = year

            SoniApp.mApiCall?.postEmployeeAttendance(param)
                ?.enqueue(object : Callback<AttendanceResponse> {
                    override fun onFailure(call: Call<AttendanceResponse>, t: Throwable) {
                        showToast(getString(R.string.api_error))
                        binder.svShimmer.visibility = View.GONE

                    }

                    override fun onResponse(
                        call: Call<AttendanceResponse>,
                        response: Response<AttendanceResponse>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!) {
                                    if (responseLogin.status!!) {
                                        listAttendance= responseLogin.data!!.employeeDetails!!.attendance
                                        listAttendance.forEach {
                                            val calendar = Calendar.getInstance()
                                           var d= sdf.parse(it.attendDate)
                                            calendar.time=d
                                            seletedDays.add(calendar)


                                        }
                                        var dates:List<Calendar> = seletedDays.toList()
                                        binder.calendarView.selectedDates=dates
                                        binder.tvAttendanceCount.text=listAttendance.size.toString()
                                        binder.calendarView.visibility = View.VISIBLE
                                        binder.svShimmer.visibility = View.GONE
                                        employee=responseLogin.data!!.employeeDetails!!
                                        setData()
//                                        showToast(responseLogin.message!!)
//                                        binding.txtAddAtt.visibility = View.VISIBLE
//                                        binding.txtAddAttShimmer.visibility = View.GONE

                                    }
                                } else {
                                    binder.calendarView.visibility = View.VISIBLE
                                    binder.svShimmer.visibility = View.GONE
                                }

                            }
                        }

                        binder.svShimmer.visibility = View.GONE
                    }


                })

        } else {
            showToast(getString(R.string.internet_error))
            binder.svShimmer.visibility = View.GONE

        }
    }
    private fun callDeleteApi() {

        if (isInternetAvailable(this)) {

            var param: HashMap<String, String> = HashMap()
            param["employee_id"] = employee!!.id!!.toString()

            SoniApp.mApiCall?.postDeleteEmployee(param)
                ?.enqueue(object : Callback<BaseModel> {
                    override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                        showToast(getString(R.string.api_error))


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
                                        showToast(responseLogin.message!!)

                                        finish()
                                    }
                                } else {
                                    showToast(responseLogin.message!!)

                                }

                            }
                        }
                    }


                })

        } else {
            showToast(getString(R.string.internet_error))
        }
    }

    private fun showLanguageDialog(){

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.layout_delete_property_popup);
//        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.window?.setBackgroundDrawable(getDrawable(R.drawable.dialog_back))
        dialog.setCancelable(false);


        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height =getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._150sdp).toInt()

        dialog.window?.setLayout(width, height)

        val txtyes = dialog.findViewById<TextView>(R.id.tv_yes)
        val txtno = dialog.findViewById<TextView>(R.id.tv_no)
        txtyes.setOnClickListener {
            dialog.dismiss()

            callDeleteApi()
        }
        txtno.setOnClickListener {
            dialog.dismiss()

        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.show()
    }
}
package com.soni.activity

import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.soni.Preference.getCurrentLanguageID
import com.soni.Preference.getUserData
import com.soni.R
import com.soni.SoniApp
import com.soni.databinding.ActivityHomeBinding
import com.soni.fragments.*
import com.soni.services.web.models.PropertyModel
import com.soni.services.web.models.State
import com.soni.services.web.models.StateListResponse
import com.soni.utils.Const
import com.soni.utils.LocationCallbackListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class HomeActivity : BaseActivity() {


    var mHomeStacks = HashMap<String, Stack<Fragment>>()
    var mRentStacks = HashMap<String, Stack<Fragment>>()
    var mProfileStacks = HashMap<String, Stack<Fragment>>()
    var mBuySellStacks = HashMap<String, Stack<Fragment>>()


    protected var homeFragmentManager: FragmentManager = supportFragmentManager
    protected var rentFragmentManager: FragmentManager = supportFragmentManager
    protected var profileFragmentManager: FragmentManager = supportFragmentManager
    protected var buySellFragmentManager: FragmentManager = supportFragmentManager


    var mHomeCurrentTab: String? = null
    var mRentCurrentTab: String? = null
    var mProfileCurrentTab: String? = null
    var mBuySellCurrentTab: String? = null

    internal var homeFragment: Fragment? = null
    internal var rentFragment: Fragment? = null
    internal var profileFragment: Fragment? = null
    internal var buySellFragment: Fragment? = null



    var activeTab = 0
    var isFromNotification = false

    lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoniApp.changeAppLanguage(this, getCurrentLanguageID())
        var delay:Long=0
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isFromNotification=intent.getBooleanExtra(Const.IntentKey.isFromNotification,false)

        if(isFromNotification && SoniApp.user==null){
            SoniApp.onCreate(this)
            delay=1000
            SoniApp.user= getUserData()

        }
        locationCalllistener={

            locationCall.forEach { item ->
                item.onUpdate(it)
            }
            Log.d("Location","${it.latitude} ${it.longitude}")
        }
        Handler(Looper.getMainLooper()).postDelayed(
            {
                val notificationId=intent.getIntExtra(Const.IntentKey.notificationId,-1)
                if(notificationId!=-1) {
                    val notificationManager = getSystemService(
                        Context.NOTIFICATION_SERVICE
                    ) as NotificationManager
                    notificationManager.cancel(notificationId)
                }


                mHomeStacks[Const.TAB.home] = Stack<Fragment>()
                mRentStacks[Const.TAB.rent] = Stack<Fragment>()
                mProfileStacks[Const.TAB.profile] = Stack<Fragment>()
                mBuySellStacks[Const.TAB.buySell] = Stack<Fragment>()

                init()

            },delay)



    }

    companion object {
        var cr: ContentResolver? = null
        var activity:HomeActivity? =null

        fun openEditProperty(propertyModel: PropertyModel?){
            Handler(Looper.getMainLooper()).postDelayed({
                val bundle = Bundle()
                bundle.putInt("screenType", 1)
                bundle.putInt(Const.IntentKey.PropertyType, (propertyModel!!.propertyTypeId!!))
                bundle.putSerializable(Const.IntentKey.Property,propertyModel!!)
                activity!!.seEditPropertiesFragment(bundle, Const.TAB.home)
            }, 100)

        }
        var isInit=true;
         var locationCall: ArrayList<LocationCallbackListener> = arrayListOf()
    }

    private fun init() {
        activity=this


        cr = contentResolver
        binding.llHome.setOnClickListener {
            activeTab = 0
            setUserHomeFragment(Const.TAB.home,isFromNotification)
            binding.homeFrameContainer.visibility = View.VISIBLE
            binding.rentFrameContainer.visibility = View.GONE
            binding.profileFrameContainer.visibility = View.GONE
            binding.profileFrameContainer.visibility = View.GONE
            binding.buySellFrameContainer.visibility = View.GONE

            binding.ivHome.setColorFilter(getColor(R.color.Primary100))
            binding.ivRoundHome.visibility = View.VISIBLE
            binding.txtHome.setTextColor(getColor(R.color.Primary100))

            binding.ivRent.setColorFilter(getColor(R.color.Grey60))
            binding.ivRoundRent.visibility = View.INVISIBLE
            binding.txtRent.setTextColor(getColor(R.color.Grey60))

            binding.ivBuySell.setColorFilter(getColor(R.color.Grey60))
            binding.ivRoundBuySell.visibility = View.INVISIBLE
            binding.txtBuySell.setTextColor(getColor(R.color.Grey60))


            binding.ivProfile.setColorFilter(getColor(R.color.Grey60))
            binding.ivRoundProfile.visibility = View.INVISIBLE
            binding.txtProfile.setTextColor(getColor(R.color.Grey60))

        }

        binding.llRent.setOnClickListener {
            setRentFargment()
        }

        binding.llBuySell.setOnClickListener {

            setBuySellFragment()

        }

        binding.llProfile.setOnClickListener {
            activeTab = 3
            binding.homeFrameContainer.visibility = View.GONE
            binding.rentFrameContainer.visibility = View.GONE
            binding.profileFrameContainer.visibility = View.VISIBLE
            binding.buySellFrameContainer.visibility = View.GONE


            binding.ivHome.setColorFilter(getColor(R.color.Grey60))
            binding.ivRoundHome.visibility = View.INVISIBLE
            binding.txtHome.setTextColor(getColor(R.color.Grey60))

            binding.ivRent.setColorFilter(getColor(R.color.Grey60))
            binding.ivRoundRent.visibility = View.INVISIBLE
            binding.txtRent.setTextColor(getColor(R.color.Grey60))


            binding.ivProfile.setColorFilter(getColor(R.color.Primary100))
            binding.ivRoundProfile.visibility = View.VISIBLE
            binding.txtProfile.setTextColor(getColor(R.color.Primary100))

            binding.ivBuySell.setColorFilter(getColor(R.color.Grey60))
            binding.ivRoundBuySell.visibility = View.INVISIBLE
            binding.txtBuySell.setTextColor(getColor(R.color.Grey60))


            setUserProfileFragment(Const.TAB.profile)



        }

        binding.llHome.callOnClick()

        if(SoniApp.stateList.isEmpty()) {
            callStateListApi()
        }


        askNotificationPermission()

    }

    open fun setRentFargment() {
        activeTab = 1
        binding.homeFrameContainer.visibility = View.GONE
        binding.rentFrameContainer.visibility = View.VISIBLE
        binding.profileFrameContainer.visibility = View.GONE
        binding.buySellFrameContainer.visibility = View.GONE

        binding.ivHome.setColorFilter(getColor(R.color.Grey60))
        binding.ivRoundHome.visibility = View.INVISIBLE
        binding.txtHome.setTextColor(getColor(R.color.Grey60))

        binding.ivRent.setColorFilter(getColor(R.color.Primary100))
        binding.ivRoundRent.visibility = View.VISIBLE
        binding.txtRent.setTextColor(getColor(R.color.Primary100))

        binding.ivBuySell.setColorFilter(getColor(R.color.Grey60))
        binding.ivRoundBuySell.visibility = View.INVISIBLE
        binding.txtBuySell.setTextColor(getColor(R.color.Grey60))

        binding.ivProfile.setColorFilter(getColor(R.color.Grey60))
        binding.ivRoundProfile.visibility = View.INVISIBLE
        binding.txtProfile.setTextColor(getColor(R.color.Grey60))

        setUserRentFragment(Const.TAB.rent)
    }

    open fun setBuySellFragment(){
        activeTab = 2
        binding.homeFrameContainer.visibility = View.GONE
        binding.rentFrameContainer.visibility = View.GONE
        binding.profileFrameContainer.visibility = View.GONE
        binding.buySellFrameContainer.visibility = View.VISIBLE


        binding.ivHome.setColorFilter(getColor(R.color.Grey60))
        binding.ivRoundHome.visibility = View.INVISIBLE
        binding.txtHome.setTextColor(getColor(R.color.Grey60))

        binding.ivRent.setColorFilter(getColor(R.color.Grey60))
        binding.ivRoundRent.visibility = View.INVISIBLE
        binding.txtRent.setTextColor(getColor(R.color.Grey60))


        binding.ivBuySell.setColorFilter(getColor(R.color.Primary100))
        binding.ivRoundBuySell.visibility = View.VISIBLE
        binding.txtBuySell.setTextColor(getColor(R.color.Primary100))

        binding.ivProfile.setColorFilter(getColor(R.color.Grey60))
        binding.ivRoundProfile.visibility = View.INVISIBLE
        binding.txtProfile.setTextColor(getColor(R.color.Grey60))

        setUserBuySellFragment(Const.TAB.buySell)
    }

    open fun homePushFragments(
        tag: String?,
        fragment: Fragment?,
        shouldAnimate: Boolean,
        shouldAdd: Boolean,
    ) {
        if (shouldAdd) mHomeStacks[tag]!!.push(fragment)
        homeFragmentManager = supportFragmentManager
        val ft: FragmentTransaction = homeFragmentManager.beginTransaction()
        ft.replace(R.id.home_frame_container, fragment!!)
        ft.commit()
    }

    open fun rentPushFragments(
        tag: String?,
        fragment: Fragment?,
        shouldAnimate: Boolean,
        shouldAdd: Boolean
    ) {
        if (shouldAdd) mRentStacks[tag]!!.push(fragment)
        rentFragmentManager = supportFragmentManager
        val ft = rentFragmentManager.beginTransaction()
        ft.replace(R.id.rent_frame_container, fragment!!)
        ft.commit()
    }

    open fun profilePushFragments(
        tag: String?,
        fragment: Fragment?,
        shouldAnimate: Boolean,
        shouldAdd: Boolean
    ) {
        if (shouldAdd) mProfileStacks[tag]!!.push(fragment)
        profileFragmentManager = supportFragmentManager
        val ft = profileFragmentManager.beginTransaction()
        ft.replace(R.id.profile_frame_container, fragment!!)
        ft.commit()
    }

    open fun buySellPushFragments(
        tag: String?,
        fragment: Fragment?,
        shouldAnimate: Boolean,
        shouldAdd: Boolean
    ) {
        if (shouldAdd) mBuySellStacks[tag]!!.push(fragment)
        buySellFragmentManager = supportFragmentManager
        val ft = buySellFragmentManager.beginTransaction()
        ft.replace(R.id.buy_sell_frame_container, fragment!!)
        ft.commit()
    }


    private fun homeCurrentFragment() {
        val fragment =
            mHomeStacks[mHomeCurrentTab]!!.elementAt(mHomeStacks[mHomeCurrentTab]!!.size - 1)

//        /*pop current fragment from stack.. */mStacks[mCurrentTab]!!.pop()

        /* We have the target fragment in hand.. Just show it.. Show a standard navigation animation*/
        homeFragmentManager = supportFragmentManager
        val ft: FragmentTransaction = homeFragmentManager.beginTransaction()
        ft.replace(R.id.home_frame_container, fragment)
        ft.commit()
    }

    private fun rentCurrentFragment() {
        val fragment =
            mRentStacks[mRentCurrentTab]!!.elementAt(mRentStacks[mRentCurrentTab]!!.size - 1)

//        /*pop current fragment from stack.. */mStacks[mCurrentTab]!!.pop()

        /* We have the target fragment in hand.. Just show it.. Show a standard navigation animation*/
        rentFragmentManager = supportFragmentManager
        val ft = rentFragmentManager.beginTransaction()
        ft.replace(R.id.rent_frame_container, fragment)
        ft.commit()
    }

    private fun profileCurrentFragment() {
        val fragment =
            mProfileStacks[mProfileCurrentTab]!!.elementAt(mProfileStacks[mProfileCurrentTab]!!.size - 1)

//        /*pop current fragment from stack.. */mStacks[mCurrentTab]!!.pop()

        /* We have the target fragment in hand.. Just show it.. Show a standard navigation animation*/
        profileFragmentManager = supportFragmentManager
        val ft: FragmentTransaction = profileFragmentManager.beginTransaction()
        ft.replace(R.id.profile_frame_container, fragment)
        ft.commit()
    }

    private fun buySellCurrentFragment() {
        val fragment =
            mBuySellStacks[mBuySellCurrentTab]!!.elementAt(mBuySellStacks[mBuySellCurrentTab]!!.size - 1)

//        /*pop current fragment from stack.. */mStacks[mCurrentTab]!!.pop()

        /* We have the target fragment in hand.. Just show it.. Show a standard navigation animation*/
        buySellFragmentManager = supportFragmentManager
        val ft: FragmentTransaction = buySellFragmentManager.beginTransaction()
        ft.replace(R.id.buy_sell_frame_container, fragment)
        ft.commit()
    }


    private fun homePopFragments() {

        mHomeStacks[mHomeCurrentTab]!!.pop()
        val fragment =
            mHomeStacks[mHomeCurrentTab]!!.elementAt(mHomeStacks[mHomeCurrentTab]!!.size - 1)
        homeFragmentManager = supportFragmentManager
        val ft: FragmentTransaction = homeFragmentManager.beginTransaction()
        ft.replace(R.id.home_frame_container, fragment)
        ft.commit()
    }

    private fun rentPopFragments() {
        /*
       *    Select the second last fragment in current tab's stack..
       *    which will be shown after the fragment transaction given below
       */
        val fragment =
            mRentStacks[mRentCurrentTab]!!.elementAt(mRentStacks[mRentCurrentTab]!!.size - 2)

        /*pop current fragment from stack.. */mRentStacks[mRentCurrentTab]!!.pop()

        /* We have the target fragment in hand.. Just show it.. Show a standard navigation animation*/
        rentFragmentManager = supportFragmentManager
        val ft = rentFragmentManager.beginTransaction()
        ft.replace(R.id.rent_frame_container, fragment)
        ft.commit()
    }

    private fun buySellPopFragments() {
        /*
       *    Select the second last fragment in current tab's stack..
       *    which will be shown after the fragment transaction given below
       */
        val fragment =
            mBuySellStacks[mBuySellCurrentTab]!!.elementAt(mBuySellStacks[mBuySellCurrentTab]!!.size - 2)

        /*pop current fragment from stack.. */mBuySellStacks[mBuySellCurrentTab]!!.pop()

        /* We have the target fragment in hand.. Just show it.. Show a standard navigation animation*/
        buySellFragmentManager = supportFragmentManager
        val ft: FragmentTransaction = buySellFragmentManager.beginTransaction()
        ft.replace(R.id.buy_sell_frame_container, fragment)
        ft.commit()
    }

    private fun profilePopFragments() {
        /*
       *    Select the second last fragment in current tab's stack..
       *    which will be shown after the fragment transaction given below
       */
        val fragment =
            mProfileStacks[mProfileCurrentTab]!!.elementAt(mProfileStacks[mProfileCurrentTab]!!.size - 2)

        /*pop current fragment from stack.. */mProfileStacks[mProfileCurrentTab]!!.pop()

        /* We have the target fragment in hand.. Just show it.. Show a standard navigation animation*/
        profileFragmentManager = supportFragmentManager
        val ft: FragmentTransaction = profileFragmentManager.beginTransaction()
        ft.replace(R.id.profile_frame_container, fragment)
        ft.commit()
    }


    internal fun setUserHomeFragment(tabId: String, isFromNotification: Boolean) {
        homeFragment = HomeFragment()
        mHomeCurrentTab = tabId
        var b=Bundle()
        b.putBoolean(Const.IntentKey.isFromNotification,isFromNotification)
        (homeFragment as HomeFragment).arguments = b

        try {
            if (mHomeStacks[mHomeCurrentTab]!!.size == 0) {
                homePushFragments(mHomeCurrentTab, homeFragment, false, true)
            } else {
                homeCurrentFragment()
            }
            Log.d("BACKSTACK", mHomeStacks[mHomeCurrentTab]!!.size.toString())

        } catch (e: Exception) {
            Log.d("log1", e.message.toString())
        }

    }


    internal fun setUserRentFragment(tabId: String) {
        rentFragment = RentFragment()
        mRentCurrentTab = tabId
        if (mRentStacks[mRentCurrentTab]!!.size == 0) {
            rentPushFragments(mRentCurrentTab, rentFragment, false, true)
        } else {
            rentCurrentFragment()
        }

        Log.d("BACKSTACK", mRentStacks[mRentCurrentTab]!!.size.toString())

    }

    internal fun setUserProfileFragment(tabId: String) {
        profileFragment = ProfileFragment()
        mProfileCurrentTab = tabId
        if (mProfileStacks[mProfileCurrentTab]!!.size == 0) {
            profilePushFragments(mProfileCurrentTab, profileFragment, false, true)
        } else {
            profileCurrentFragment()
        }
    }


    internal fun setUserBuySellFragment(tabId: String) {
        buySellFragment = BuySellFragment()
        mBuySellCurrentTab = tabId

        try {
            if (mBuySellStacks[mBuySellCurrentTab]!!.size == 0) {
                buySellPushFragments(mBuySellCurrentTab, buySellFragment, false, true)
            } else {
                buySellCurrentFragment()
            }
            Log.d("BACKSTACK", mBuySellStacks[mBuySellCurrentTab]!!.size.toString())

        } catch (e: Exception) {
            Log.d("log1", e.message.toString())
        }

    }

    internal fun setAttendanceManagementFragment(b: Bundle?, tabId: String) {
//        fragment = HotPopularFragment()
        homeFragment = AttendanceManagementFragment()
        (homeFragment as AttendanceManagementFragment).arguments = b
        mHomeCurrentTab = tabId
        homePushFragments(mHomeCurrentTab, homeFragment, false, true)
        Log.d("BACKSTACK", mHomeStacks[mHomeCurrentTab]!!.size.toString())

    }


    internal fun seEditPropertiesFragment(b: Bundle?, tabId: String) {
//        fragment = HotPopularFragment()
        homeFragment = EditPropertiesFragment()
        (homeFragment as EditPropertiesFragment).arguments = b
        mHomeCurrentTab = tabId
        homePushFragments(mHomeCurrentTab, homeFragment, false, true)
        Log.d("BACKSTACK", mHomeStacks[mHomeCurrentTab]!!.size.toString())

    }

    internal fun seEditPropertiesFromRentFragment(b: Bundle?, tabId: String) {
//        fragment = HotPopularFragment()
       val rentFragment = EditPropertiesFragment()
        (rentFragment as EditPropertiesFragment).arguments = b
        mRentCurrentTab = tabId
        rentPushFragments(mRentCurrentTab, rentFragment, false, true)
        Log.d("BACKSTACK", mRentStacks[mRentCurrentTab]!!.size.toString())

    }
    internal fun setMyAccountFragment(b: Bundle?, tabId: String) {
//        fragment = HotPopularFragment()
        homeFragment = MyAccountFragment()
        (homeFragment as MyAccountFragment).arguments = b
        mHomeCurrentTab = tabId
        homePushFragments(mHomeCurrentTab, homeFragment, false, true)
        Log.d("BACKSTACK", mHomeStacks[mHomeCurrentTab]!!.size.toString())

    }

    override fun onBackPressed() {
        when (activeTab) {
            0 -> {
                if (mHomeStacks[mHomeCurrentTab]!!.size == 1) {
                    super.onBackPressed() // or call finish..
                } else {
                    homePopFragments()
                }
            }

            1 -> {
                if (mRentStacks[mRentCurrentTab]!!.size == 1) {
                    super.onBackPressed() // or call finish..
                } else {
                    rentPopFragments()
                }
            }
            2 -> {
                if (mBuySellStacks[mBuySellCurrentTab]!!.size == 1) {
                    super.onBackPressed() // or call finish..
                } else {
                    buySellPopFragments()
                }
            }
            3 -> {
                if (mProfileStacks[mProfileCurrentTab]!!.size == 1) {
                    super.onBackPressed() // or call finish..
                } else {
                    profilePopFragments()
                }
            }

        }

    }


    private fun callStateListApi(
    ) {
        if (isInternetAvailable(this)) {
            var param: HashMap<String, String> = HashMap()

            param["Limit"] ="100"
            param["Page"] ="1"


            SoniApp.mApiCall?.postGetStateList(param)
                ?.enqueue(object : Callback<StateListResponse> {
                    override fun onFailure(call: Call<StateListResponse>, t: Throwable) {
//                        showToast(getString(R.string.api_error))
                   }

                    override fun onResponse(
                        call: Call<StateListResponse>,
                        response: Response<StateListResponse>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!) {
                                    if (responseLogin.status!!) {
                                        SoniApp.stateList.add(State(id = -1, name = getString(R.string.state)))
                                        SoniApp.stateList.addAll(responseLogin.data)
                                    }
                                } else {
                                    Log.d("State ERROR", "DATA Not Found")

                                }
                            }
                        }
                        else {
                            Log.d("State ERROR", "DATA Not Found")

                        }
                    }
                })

        } else {
            Log.d("State ERROR", "DATA Not Found")

        }


    }

    override fun onResume() {
        if(!isInit) {
//            getLocation()
        }
        super.onResume()

    }
}
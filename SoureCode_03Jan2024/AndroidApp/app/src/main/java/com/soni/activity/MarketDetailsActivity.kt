package com.soni.activity

import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils
import com.soni.Preference.getCurrentLanguageID
import com.soni.R
import com.soni.SoniApp
import com.soni.adapter.InquiriesHomeAdapterShimmer
import com.soni.adapter.MarketPastPricesAdapter
import com.soni.adapter.MyMarkerView
import com.soni.databinding.ActivityMarketDetailsBinding
import com.soni.services.web.models.MarketData
import com.soni.services.web.models.MarketDataListResponse
import com.soni.utils.Const
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import kotlin.math.absoluteValue


class MarketDetailsActivity : BaseActivity() {
    lateinit var binding: ActivityMarketDetailsBinding
    lateinit var marketListAdapter: MarketPastPricesAdapter
    lateinit var chart: com.github.mikephil.charting.charts.LineChart
    var marketDataList: ArrayList<MarketData> = arrayListOf()
    var state = ""
    var commodity = ""
    var varity = ""
    var market = ""
    var difference = 0
    val formatin = SimpleDateFormat("dd/MM/yyyy")
    val formatout = SimpleDateFormat("dd MMMM yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoniApp.changeAppLanguage(this, getCurrentLanguageID())
        binding = ActivityMarketDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        state = intent.extras!!.getString(Const.IntentKey.State, "")
        commodity = intent.extras!!.getString(Const.IntentKey.Commodity, "")
        varity = intent.extras!!.getString(Const.IntentKey.Varity, "")
        market = intent.extras!!.getString(Const.IntentKey.Market, "")
        difference = intent.extras!!.getInt(Const.IntentKey.Difference, 0)

        init()
    }

    private fun init() {
        ivBack = findViewById(R.id.iv_back)

        ivBack.setOnClickListener { finish() }


        ivRight = findViewById(R.id.iv_right)
        ivBack = findViewById(R.id.iv_back)
        ivRight.visibility = View.INVISIBLE
        title = findViewById(R.id.tv_title)
        title.text = getString(R.string.market_price)
        ivBack.setOnClickListener { this.onBackPressed() }

        marketListAdapter = MarketPastPricesAdapter(this, marketDataList)
        binding.rvMarket.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvMarket.adapter = marketListAdapter

        binding.rvMarketShimmer.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvMarketShimmer.adapter = InquiriesHomeAdapterShimmer(this)

        callDataListApi()


    }

    private fun setData() {
        // // Chart Style // //
        chart = binding.chart1
        // background color
        chart.setBackgroundColor(Color.WHITE)
        // disable description text
        chart.description.isEnabled = false
        // enable touch gestures
        chart.setTouchEnabled(true)
        // set listeners
        // chart.setOnChartValueSelectedListener(this)
        chart.setDrawGridBackground(false)

        // create marker to display box when values are selected
        val mv = MyMarkerView(this, R.layout.custom_marker_view)

        // Set the marker to the chart
        mv.chartView = chart
        chart.marker = mv

        // enable scaling and dragging
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        // chart.setScaleXEnabled(true);
        // chart.setScaleYEnabled(true);

        // force pinch zoom along both axis
        // chart.setScaleXEnabled(true);
        // chart.setScaleYEnabled(true);

        // force pinch zoom along both axis
        chart.setPinchZoom(true)

        val values = ArrayList<Entry>()
        var marketData = ArrayList<MarketData>()
        marketData.addAll(marketDataList)
        marketData.reverse()


        val xAxis: XAxis
        // // X-Axis Style // //
        xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        // vertical grid lines
        xAxis.enableGridDashedLine(10f, 10f, 0f)
        xAxis.valueFormatter = MyXAxisFormatter(marketData)
        if (marketData.size > 1) {
            xAxis.setLabelCount(marketData.size, true)
        } else {
//            xAxis.setLabelCount(marketData.size, true)
            xAxis.mAxisMaximum = 0f
            xAxis.mAxisMinimum = 0f
        }
//        var c=Calendar.getInstance()
//        var date=formatin.parse(marketDataList[0].arrivalDate)
//        c.time=date
//        c.add(Calendar.DAY_OF_MONTH ,1)
//        xAxis.mAxisMaximum=c.time.time.toFloat()
//        var cmin=Calendar.getInstance()
//        var dateMin=formatin.parse(marketDataList.last().arrivalDate)
//        cmin.time=dateMin
//        cmin.add(Calendar.DAY_OF_MONTH ,-1)
//        xAxis.mAxisMinimum=cmin.time.time.toFloat()
//
//        chart.getRendererXAxis().getPaintAxisLabels().setTextAlign(Paint.Align.RIGHT)


        val yAxis: YAxis
        // // Y-Axis Style // //
        yAxis = chart.axisLeft

        // disable dual axis (only use LEFT axis)
        chart.axisRight.isEnabled = false

        // horizontal grid lines
        yAxis.enableGridDashedLine(10f, 10f, 0f)
        yAxis.valueFormatter = MyYAxisFormatter()
        // axis range
//        yAxis.setAxisMaximum(200f)
//        yAxis.setAxisMinimum(-50f)

        // // Create Limit Lines // //
        var typeface = ResourcesCompat.getFont(this, R.font.kumbh_sans_regular)

        // // Create Limit Lines // //
        val llXAxis = LimitLine(9f, "Index 10")
        llXAxis.lineWidth = 4f
        llXAxis.enableDashedLine(10f, 10f, 0f)
        llXAxis.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
        llXAxis.textSize = 10f
        llXAxis.typeface = typeface
        llXAxis.textColor = getColor(R.color.Grey80)

//        val ll1 = LimitLine(150f, "Upper Limit")
//        ll1.setLineWidth(4f)
//        ll1.enableDashedLine(10f, 10f, 0f)
//        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP)
//        ll1.setTextSize(10f)
//        ll1.setTypeface(typeface)

//        val ll2 = LimitLine(-30f, "Lower Limit")
//        ll2.setLineWidth(4f)
//        ll2.enableDashedLine(10f, 10f, 0f)
//        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM)
//        ll2.setTextSize(10f)
//        ll2.setTypeface(typeface)

        // draw limit lines behind data instead of on top
        yAxis.setDrawLimitLinesBehindData(true)
        xAxis.setDrawLimitLinesBehindData(true)

        // add limit lines
//        yAxis.addLimitLine(ll1)
//        yAxis.addLimitLine(ll2)
        //xAxis.addLimitLine(llXAxis);


        // add data
        //xAxis.addLimitLine(llXAxis);


//        data.forEach {
//            val `val` = it.modalPrice!!.toFloat()
//            var date = formatin.parse(it.arrivalDate!!)
//            values.add(
//                Entry(
//                    date.time.toFloat(),
//                    `val`,
//                    resources.getDrawable(R.drawable.chart_dot)
//                )
//            )
//        }

        for (i in 0 until marketData.size) {

            val `val` = marketData[i].modalPrice!!.toFloat()
            values.add(
                Entry(
                    i.toFloat(),
                    `val`,
                    resources.getDrawable(R.drawable.chart_dot)
                )
            )
        }
        val set1: LineDataSet
        if (chart.data != null &&
            chart.data.dataSetCount > 0
        ) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            set1.notifyDataSetChanged()
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values, "")
            set1.setDrawIcons(false)
            set1.valueTypeface = typeface
            // draw dashed line
//            set1.enableDashedLine(10f, 5f, 0f)

            // black lines and points
            set1.color = getColor(R.color.Primary100)
            set1.setCircleColor(getColor(R.color.Primary100))

            // line thickness and point size
            set1.lineWidth = 2f
            set1.circleRadius = 4f

            // draw points as solid circles
            set1.setDrawCircleHole(false)

            // customize legend entry
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f

            // text size of values
            set1.valueTextSize = 9f

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f)

            // set the filled area
            set1.setDrawFilled(true)
            set1.fillFormatter =
                IFillFormatter { dataSet, dataProvider -> chart.axisLeft.axisMinimum }

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                val drawable = ContextCompat.getDrawable(this, R.drawable.transparent_5sdp_radius)
                set1.fillDrawable = drawable
            } else {
                set1.fillColor = Color.TRANSPARENT
            }
            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the data sets

            // create a data object with the data sets
            val data = LineData(dataSets)

            // set data
            chart.data = data
        }
    }

    private fun callDataListApi(

    ) {
        if (isInternetAvailable(this)) {
            binding.sfShimmer.visibility = View.VISIBLE
            binding.sfBaseShimmer.visibility = View.VISIBLE
            binding.chart1.visibility = View.INVISIBLE
            binding.rvMarket.visibility = View.GONE
            binding.llBaseData.visibility = View.GONE
            binding.sfChatShimmer.visibility = View.VISIBLE
            var param: HashMap<String, String> = HashMap()

            param["stateName"] = state
            param["commodityName"] = commodity
            param["marketName"] = market
            param["varietyName"] = varity



            SoniApp.mApiCall?.postMarketGraphDataList(param)
                ?.enqueue(object : Callback<MarketDataListResponse> {
                    override fun onFailure(call: Call<MarketDataListResponse>, t: Throwable) {
                        showToast(getString(R.string.api_error))
                        binding.sfShimmer.visibility = View.GONE
                        binding.rvMarket.visibility = View.VISIBLE
                        binding.sfBaseShimmer.visibility = View.GONE
                        binding.llBaseData.visibility = View.VISIBLE
                        binding.sfChatShimmer.visibility = View.GONE
                    }


                    override fun onResponse(
                        call: Call<MarketDataListResponse>,
                        response: Response<MarketDataListResponse>?
                    ) {
                        if (response != null) {
                            val responseLogin = response.body()
                            if (responseLogin != null) {
                                if (responseLogin.status!!) {
                                    if (responseLogin.status!!) {

                                        marketDataList.addAll(responseLogin.data)

                                        marketListAdapter.notifyDataSetChanged()
                                        binding.tvCommodityName.text = marketDataList[0].commodity
                                        binding.tvCommodityMarket.text = marketDataList[0].market
                                        var outstr =
                                            formatout.format(formatin.parse(marketDataList[0].arrivalDate!!))
                                        binding.tvLastUpdate.text = outstr
                                        binding.tvCommodityPrice.text =
                                            if (marketDataList[0].modalPrice != null) "₹ ${marketDataList[0].modalPrice}/Q" else ""

                                        if (difference == 0) {
                                            binding.ivTrendIcon.visibility = View.INVISIBLE
                                            binding.ivPriceDifferenceIcon.visibility =
                                                View.GONE
                                            binding.llPriceDifference.background =
                                                getDrawable(R.drawable.yellow_btn_rounded)
                                            binding.tvPriceDifference.text = "-  ₹0"
                                        } else {
                                            if (difference < 0) {
                                                binding.ivTrendIcon.setImageDrawable(
                                                    getDrawable(
                                                        R.drawable.market_down
                                                    )
                                                )
                                                binding.ivPriceDifferenceIcon.setImageDrawable(
                                                    getDrawable(R.drawable.arrow_down_white)
                                                )
                                                binding.llPriceDifference.background =
                                                    (getDrawable(R.drawable.red_btn_rounded))
                                            }
                                            binding.tvPriceDifference.text =
                                                "₹" + difference.absoluteValue.toString()
                                        }



                                        binding.sfShimmer.visibility = View.GONE
                                        binding.rvMarket.visibility = View.VISIBLE
                                        binding.sfBaseShimmer.visibility = View.GONE
                                        binding.llBaseData.visibility = View.VISIBLE
                                        binding.chart1.visibility = View.VISIBLE
                                        binding.sfChatShimmer.visibility = View.GONE

                                        setData()

                                        // draw points over time
                                        chart.animateX(1500)

                                        // get the legend (only possible after setting data)
                                        val l: Legend = chart.legend
                                        l.isEnabled = false
                                        // draw legend entries as lines
                                        l.form = LegendForm.LINE
                                    }
                                } else {
                                    Log.d("State ERROR", "DATA Not Found")
                                    binding.sfShimmer.visibility = View.GONE
                                    binding.rvMarket.visibility = View.VISIBLE
                                    binding.sfBaseShimmer.visibility = View.GONE
                                    binding.llBaseData.visibility = View.VISIBLE
                                    binding.sfChatShimmer.visibility = View.GONE

                                }
                            }
                        } else {
                            Log.d("State ERROR", "DATA Not Found")
                            binding.sfShimmer.visibility = View.GONE
                            binding.rvMarket.visibility = View.VISIBLE
                            binding.sfBaseShimmer.visibility = View.GONE
                            binding.llBaseData.visibility = View.VISIBLE
                            binding.sfChatShimmer.visibility = View.GONE


                        }
                    }
                })

        } else {
            Log.d("State ERROR", "DATA Not Found")
            binding.sfShimmer.visibility = View.GONE
            binding.rvMarket.visibility = View.VISIBLE
            binding.sfChatShimmer.visibility = View.GONE

        }


    }
}

class MyXAxisFormatter(var data: ArrayList<MarketData>) : ValueFormatter() {
    val formatin = SimpleDateFormat("dd/MM/yyyy")
    val formatout = SimpleDateFormat("dd MMM")
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        if (data.size > 1) {
            return formatout.format(formatin.parse(data[value.toInt()].arrivalDate))
                ?: value.toString()
        } else if (data.size == 1 && value == 0f) {
            return formatout.format(formatin.parse(data[value.toInt()].arrivalDate))
                ?: value.toString()
        } else {
            return ""
        }
    }
}

class MyYAxisFormatter : ValueFormatter() {

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return "₹$value"
    }
}
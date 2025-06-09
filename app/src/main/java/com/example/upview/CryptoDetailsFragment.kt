package com.example.upview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.upview.MarketChartResponse
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CryptoDetailsFragment : Fragment() {

    private lateinit var lineChart: LineChart
    private lateinit var apiService: CoinGeckoApi

    private var cryptoId: String? = null
    private var vsCurrency: String = "usd"
    private var selectedDays = "1"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_crypto_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiService = (activity as MainActivity).createApiService()

        cryptoId = arguments?.getString("id")
        val name = arguments?.getString("name")
        val price = arguments?.getDouble("price")
        val rank = arguments?.getInt("rank")
        val symbol = arguments?.getString("symbol")

        lineChart = view.findViewById(R.id.lineChart)

        view.findViewById<TextView>(R.id.textViewName)?.text = name ?: "Unknown"
        view.findViewById<TextView>(R.id.textViewValue)?.text = "Price: $${price ?: "?"}"
        view.findViewById<TextView>(R.id.textViewYear)?.text = "Rank: ${rank ?: "?"}"
        view.findViewById<TextView>(R.id.textViewDescription)?.text = "Symbol: ${symbol ?: "?"}"

        val radioGroupPeriod = view.findViewById<RadioGroup>(R.id.radioGroupPeriod)

        radioGroupPeriod.setOnCheckedChangeListener { _, checkedId ->
            selectedDays = when (checkedId) {
                R.id.radioDay -> "1"
                R.id.radioWeek -> "7"
                R.id.radioMonth -> "30"
                R.id.radioYear -> "365"
                else -> "1"
            }
            cryptoId?.let { loadMarketChart(it, selectedDays) }
        }

        radioGroupPeriod.check(R.id.radioDay)
        cryptoId?.let { loadMarketChart(it, selectedDays) }
    }

    private fun loadMarketChart(cryptoId: String, days: String) {
        Log.d("CryptoDetails", "Загружаем график для id=$cryptoId, days=$days")

        apiService.getMarketChart(cryptoId, "usd", days)
            .enqueue(object : Callback<MarketChartResponse> {
                override fun onResponse(
                    call: Call<MarketChartResponse>,
                    response: Response<MarketChartResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data != null) {
                            showChart(data.prices)
                        }
                    } else {
                        Log.e("CryptoDetails", "Ошибка ответа: ${response.code()} - ${response.errorBody()?.string()}")
                        Toast.makeText(requireContext(), "Ошибка сервера: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MarketChartResponse>, t: Throwable) {
                    Log.e("CryptoDetails", "Ошибка сети: ${t.localizedMessage}", t)
                    Toast.makeText(requireContext(), "Ошибка сети при загрузке графика", Toast.LENGTH_SHORT).show()
                }
            })
    }


    private fun showChart(prices: List<List<Double>>) {
        val entries = prices.map {
            val timestamp = it[0].toLong()
            val price = it[1].toFloat()
            Entry(timestamp.toFloat(), price)
        }

        val dataSet = LineDataSet(entries, "Price")
        dataSet.color = ContextCompat.getColor(requireContext(), android.R.color.holo_blue_light)
        dataSet.valueTextColor = ContextCompat.getColor(requireContext(), android.R.color.white)
        dataSet.setDrawCircles(false)
        dataSet.lineWidth = 2f

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        val xAxis = lineChart.xAxis
        xAxis.valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
            private val sdf = SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())
            override fun getFormattedValue(value: Float): String {
                return sdf.format(Date(value.toLong()))
            }
        }
        xAxis.labelRotationAngle = -45f
        xAxis.textColor = ContextCompat.getColor(requireContext(), android.R.color.white)

        lineChart.axisLeft.textColor = ContextCompat.getColor(requireContext(), android.R.color.white)
        lineChart.axisRight.isEnabled = false
        lineChart.description.isEnabled = false
        lineChart.legend.textColor = ContextCompat.getColor(requireContext(), android.R.color.white)
        lineChart.invalidate()
    }
}

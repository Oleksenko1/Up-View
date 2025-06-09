package com.example.upview

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainActivity : AppCompatActivity() {

    private val popularCryptoIds = listOf("bitcoin", "ethereum", "litecoin")

    private val fragmentStateMap = mutableMapOf<Int, Boolean>()

    private lateinit var cryptoListContainer: LinearLayout

    private var currentCurrency = "usd"

    private val apiService by lazy { createApiService() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupCurrency)
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        cryptoListContainer = findViewById(R.id.cryptoListContainer)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            currentCurrency = if (checkedId == R.id.radioUsd) "usd" else "eur"
            Toast.makeText(this, "Selected currency: ${currentCurrency.uppercase()}", Toast.LENGTH_SHORT).show()
            fetchCryptoData()
        }

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> true
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }

        fetchCryptoData()
    }

    private fun fetchCryptoData() {
        apiService.getCoinsMarkets(
            vsCurrency = currentCurrency,
            ids = popularCryptoIds.joinToString(",")
        ).enqueue(object : Callback<List<CoinMarket>> {
            override fun onResponse(call: Call<List<CoinMarket>>, response: Response<List<CoinMarket>>) {
                if (response.isSuccessful) {
                    val coins = response.body()
                    if (coins != null) displayCoins(coins)
                    else Toast.makeText(this@MainActivity, "Ошибка получения данных", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Ошибка сервера: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CoinMarket>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Ошибка сети: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayCoins(coins: List<CoinMarket>) {
        val inflater = LayoutInflater.from(this)
        cryptoListContainer.removeAllViews()
        fragmentStateMap.clear()

        coins.forEach { coin ->
            val itemView = inflater.inflate(R.layout.item_crypto_card, cryptoListContainer, false)
            val cardView = itemView.findViewById<CardView>(R.id.cardView)
            val nameText = itemView.findViewById<TextView>(R.id.cryptoName)
            val priceText = itemView.findViewById<TextView>(R.id.price)
            val descText = itemView.findViewById<TextView>(R.id.description)
            val fragmentContainer = itemView.findViewById<FrameLayout>(R.id.fragmentContainer)

            nameText.text = coin.name
            priceText.text = "Price: $${coin.current_price}"
            descText.text = "Market cap rank: ${coin.market_cap_rank}"

            val containerId = View.generateViewId()
            fragmentContainer.id = containerId
            fragmentStateMap[containerId] = false

            cardView.setOnClickListener {
                val fragmentManager = supportFragmentManager
                val tag = "crypto_fragment_$containerId"
                val isOpen = fragmentStateMap[containerId] == true

                if (isOpen) {
                    val fragment = fragmentManager.findFragmentByTag(tag)
                    if (fragment != null) {
                        fragmentManager.beginTransaction()
                            .remove(fragment)
                            .commit()
                    }
                    fragmentStateMap[containerId] = false
                } else {
                    val fragment = CryptoDetailsFragment().apply {
                        arguments = bundleOf(
                            "id" to coin.id,
                            "name" to coin.name,
                            "price" to coin.current_price,
                            "rank" to coin.market_cap_rank,
                            "symbol" to coin.symbol
                        )
                    }

                    fragmentManager.beginTransaction()
                        .replace(containerId, fragment, tag)
                        .addToBackStack(null)
                        .commit()

                    fragmentStateMap[containerId] = true
                }
            }

            cryptoListContainer.addView(itemView)
        }
    }

    public fun createApiService(): CoinGeckoApi {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.coingecko.com/api/v3/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(CoinGeckoApi::class.java)
    }
}
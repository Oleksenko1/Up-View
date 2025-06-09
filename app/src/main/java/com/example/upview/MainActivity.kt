package com.example.upview

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.widget.FrameLayout

class MainActivity : AppCompatActivity() {

    private val cryptoList = listOf(
        CryptoCurrency("Bitcoin", "$27,000", "The first and most well-known cryptocurrency.", 2009),
        CryptoCurrency("Ethereum", "$1,800", "Platform for decentralized apps and smart contracts.", 2015),
        CryptoCurrency("Litecoin", "$95", "A peer-to-peer cryptocurrency for instant payments.", 2011)
    )

    // Храним, открыт ли фрагмент под каждой карточкой
    private val fragmentStateMap = mutableMapOf<Int, Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupCurrency)
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val cryptoListContainer = findViewById<LinearLayout>(R.id.cryptoListContainer)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val currency = if (checkedId == R.id.radioUsd) "USD" else "EUR"
            Toast.makeText(this, "Selected currency: $currency", Toast.LENGTH_SHORT).show()
        }

        val inflater = LayoutInflater.from(this)

        cryptoList.forEachIndexed { index, crypto ->
            val itemView = inflater.inflate(R.layout.item_crypto_card, cryptoListContainer, false)
            val cardView = itemView.findViewById<CardView>(R.id.cardView)
            val nameText = itemView.findViewById<TextView>(R.id.cryptoName)
            val priceText = itemView.findViewById<TextView>(R.id.price)
            val descText = itemView.findViewById<TextView>(R.id.description)
            val fragmentContainer = itemView.findViewById<FrameLayout>(R.id.fragmentContainer)

            nameText.text = crypto.name
            priceText.text = "Price: ${crypto.currentValue}"
            descText.text = crypto.description

            val containerId = View.generateViewId()
            fragmentContainer.id = containerId
            fragmentStateMap[containerId] = false

            cardView.setOnClickListener {
                val fragmentManager = supportFragmentManager
                val tag = "crypto_fragment_$containerId"
                val isOpen = fragmentStateMap[containerId] == true

                if (isOpen) {
                    // Закрыть фрагмент
                    val fragment = fragmentManager.findFragmentByTag(tag)
                    if (fragment != null) {
                        fragmentManager.beginTransaction()
                            .remove(fragment)
                            .commit()
                    }
                    fragmentStateMap[containerId] = false
                } else {
                    // Открыть фрагмент
                    val fragment = CryptoDetailsFragment().apply {
                        arguments = bundleOf("crypto" to crypto)
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
    }
}

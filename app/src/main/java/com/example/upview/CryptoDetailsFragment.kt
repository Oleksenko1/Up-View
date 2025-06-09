package com.example.upview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class CryptoDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_crypto_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val crypto = arguments?.getParcelable<CryptoCurrency>("crypto")
        crypto?.let {
            view.findViewById<TextView>(R.id.textViewName)?.text = it.name
            view.findViewById<TextView>(R.id.textViewValue)?.text = "Price: ${it.currentValue}"
            view.findViewById<TextView>(R.id.textViewYear)?.text = "Created: ${it.yearCreated}"
            view.findViewById<TextView>(R.id.textViewDescription)?.text = "Description: ${it.description}"
        }
    }
}

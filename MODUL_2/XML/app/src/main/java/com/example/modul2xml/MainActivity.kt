package com.example.modul2xml

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {
    private lateinit var etBillAmount: TextInputEditText
    private lateinit var spinnerTip: AutoCompleteTextView
    private lateinit var switchRoundUp: MaterialSwitch
    private lateinit var tvTipAmount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etBillAmount = findViewById(R.id.etBillAmount)
        spinnerTip = findViewById(R.id.spinnerTipPercentage)
        switchRoundUp = findViewById(R.id.switchRoundUp)
        tvTipAmount = findViewById(R.id.tvTipAmount)

        val options = arrayOf("15%", "18%", "20%")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, options)
        spinnerTip.setAdapter(adapter)

        spinnerTip.setText("15%", false)

        etBillAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                hitungTip()
            }
        })

        etBillAmount.setOnEditorActionListener { v, actionId, event ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_NEXT) {
                val imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                spinnerTip.requestFocus()
                true
            } else {
                false
            }
        }

        spinnerTip.setOnItemClickListener { parent, view, position, id ->
            hitungTip()
        }

        switchRoundUp.setOnCheckedChangeListener { buttonView, isChecked ->
            hitungTip()
        }
    }

    private fun hitungTip() {
        val textTagihan = etBillAmount.text.toString()
        var jumlahTagihan = 0.0

        try {
            jumlahTagihan = textTagihan.toDouble()
        } catch (e: Exception) {
            jumlahTagihan = 0.0
        }

        val textPersen = spinnerTip.text.toString()
        var persenTip = 15.0

        if (textPersen == "15%") {
            persenTip = 15.0
        } else if (textPersen == "18%") {
            persenTip = 18.0
        } else if (textPersen == "20%") {
            persenTip = 20.0
        }

        var hasilTip = (persenTip / 100) * jumlahTagihan

        if (switchRoundUp.isChecked == true) {
            hasilTip = ceil(hasilTip)
        }

        val formatUang = NumberFormat.getCurrencyInstance().format(hasilTip)
        tvTipAmount.text = "Tip Amount: " + formatUang
    }
}
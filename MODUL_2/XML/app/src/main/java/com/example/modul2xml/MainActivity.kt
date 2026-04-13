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
                calculateTip()
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
            calculateTip()
        }

        switchRoundUp.setOnCheckedChangeListener { buttonView, isChecked ->
            calculateTip()
        }
    }

    private fun calculateTip() {
        val amountInput = etBillAmount.text.toString()
        var amount = 0.0

        try {
            amount = amountInput.toDouble()
        } catch (e: Exception) {
            amount = 0.0
        }

        val tipInput = spinnerTip.text.toString()
        var tipPercent = 15.0

        if (tipInput == "15%") {
            tipPercent = 15.0
        } else if (tipInput == "18%") {
            tipPercent = 18.0
        } else if (tipInput == "20%") {
            tipPercent = 20.0
        }

        var tip = (tipPercent / 100) * amount

        if (switchRoundUp.isChecked == true) {
            tip = ceil(tip)
        }

        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        tvTipAmount.text = "Tip Amount: " + formattedTip
    }
}
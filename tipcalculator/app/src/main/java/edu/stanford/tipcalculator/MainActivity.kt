package edu.stanford.tipcalculator

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"
private const val INITIAL_VALUE = 15

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val countries = arrayOf(
            "United States",
            "United Kingdom",
            "Italy",
            "France",
            "Canada",
            "Mexico",
            "China",
            "Japan"
        )
        //resources.getStringArray(R.array())

        seekBarTip.progress = INITIAL_VALUE
        tvTipPctView.text = "$INITIAL_VALUE%"

        // access the spinner
        val spinner = findViewById<Spinner>(R.id.currencySpinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, countries)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                    // Display the selected item text on text view
                    val country = parent.getItemAtPosition(position).toString()
                    Log.i(TAG, "Spinner selected : ${country}")
                    computeTipAndTotal()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
                }
            }



            seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "OnProgressChanged $progress")
                tvTipPctView.text = "$progress%"
                computeTipAndTotal()
                changeTipEmoji()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        etBase.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndTotal()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })

        etSplitNumber.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "splitNumberChanged $s")
                computeTipAndTotal()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })
    }

    private fun getCurrencySymbol(): String {
        var currencySymbol = "$"
        when (currencySpinner.selectedItem) {
            "United States", "Canada", "Mexico" -> currencySymbol = "$"
            "United Kingdom" -> currencySymbol = "£"
            "China", "Japan" -> currencySymbol = "¥"
            "Italy", "France" -> currencySymbol = "€"
        }
        return currencySymbol
    }

    private fun changeTipEmoji() {
        val tipEmoji: String
        when (seekBarTip.progress) {
            in 0..9 -> tipEmoji = "\uD83D\uDE41"
            in 10..14 -> tipEmoji = "\uD83D\uDE10"
            in 15..19 -> tipEmoji = "\uD83D\uDE0A"
            in 20..24 -> tipEmoji = "\uD83D\uDE03"
            else -> tipEmoji = "\uD83E\uDD29"
        }
        tvTipEmoji.text = tipEmoji
    }

    private fun computeTipAndTotal() {
        if (etBase.text.toString().isEmpty()) {
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            tvPerPersonAmount.text = ""
            return
        }
        val pct = seekBarTip.progress
        val baseAmt = etBase.text.toString().toDouble()
        val tip = pct * baseAmt / 100
        val currencySymbol = getCurrencySymbol()
        val total = tip + baseAmt
        var numSplit: Int
        if  (etSplitNumber.text.toString().isEmpty()) {
            numSplit = 1
        }
//        var numSplit = etSplitNumber.text.toString()
        else {
            numSplit = etSplitNumber.text.toString().toInt()
        }
        tvTipAmount.text = currencySymbol + "%.2f".format(tip)
        tvTotalAmount.text = currencySymbol + "%.2f".format(total)
        tvPerPersonAmount.text = currencySymbol + "%.2f".format(total / numSplit.toDouble())
        Log.i(TAG, "numSplit $numSplit ")
    }



}

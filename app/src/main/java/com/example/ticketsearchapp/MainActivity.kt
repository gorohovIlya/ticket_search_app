package com.example.ticketsearchapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class MainActivity : AppCompatActivity() {

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spinnerDeparture = findViewById<Spinner>(R.id.spinnerDeparture)
        val spinnerArrival = findViewById<Spinner>(R.id.spinnerArrival)
        setupCitySpinner(spinnerDeparture, R.string.select_departure)
        setupCitySpinner(spinnerArrival, R.string.select_arrival)

        val editDateOut = findViewById<EditText>(R.id.editDateOut)
        val editDateIn = findViewById<EditText>(R.id.editDateIn)
        editDateOut.setOnClickListener { showDatePicker(editDateOut) }
        editDateIn.setOnClickListener { showDatePicker(editDateIn) }

        val editAdults = findViewById<EditText>(R.id.editAdults)
        val editChildren = findViewById<EditText>(R.id.editChildren)
        val editInfants = findViewById<EditText>(R.id.editInfants)

        findViewById<Button>(R.id.buttonSearch).setOnClickListener {
            val message = getString(
                R.string.search_result,
                spinnerDeparture.selectedItem?.toString().orEmpty(),
                spinnerArrival.selectedItem?.toString().orEmpty(),
                editDateOut.text.toString(),
                editDateIn.text.toString(),
                editAdults.text.toString().toIntOrNull() ?: 0,
                editChildren.text.toString().toIntOrNull() ?: 0,
                editInfants.text.toString().toIntOrNull() ?: 0
            )
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun setupCitySpinner(spinner: Spinner, promptRes: Int) {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.cities,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.prompt = getString(promptRes)
    }

    private fun showDatePicker(target: EditText) {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.date_picker_title)
            .build()
        picker.addOnPositiveButtonClickListener { selection ->
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = selection
            target.setText(dateFormat.format(calendar.time))
        }
        picker.show(supportFragmentManager, "date_picker")
    }
}

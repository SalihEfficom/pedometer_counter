package com.example.pedometer_counter

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*




class MainActivity : AppCompatActivity(), SensorEventListener {

    var running = false
    var sensorManager:SensorManager? = null

    var SHARED_PREFS = "sharedPrefs"
    var TEXT = "text"

    var text:String = ""

    var steps: Float = 0F




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        loadData()
        ///configureBottomView()
    }

    override fun onResume() {
        super.onResume()
        running = true
        var stepsSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepsSensor == null) {
            Toast.makeText(this, "No Step Counter Sensor !", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepsSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        running = false
        sensorManager?.unregisterListener(this)
        saveData(steps.toString())
        loadData()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        steps = event.values[0]
        Log.d("pas start",event.values[0].toString())
        if (running) {
            stepsValue.text = steps.toString()
            Log.d("pas",event.values[0].toString())
        }
    }


    fun saveData(stepCounter : String){

        val sdf = SimpleDateFormat("yyyy/mm/dd")
        val french = Locale("fr","FR","FR")
        val calendar:Calendar  = Calendar.getInstance(french)
        val currentDate:String = DateFormat.getDateInstance(DateFormat.FULL,french).format(calendar.time)

        val editor = getSharedPreferences(SHARED_PREFS , Context.MODE_PRIVATE).edit()
        editor.putString(currentDate, stepCounter)
        editor.commit()
    }

    fun loadData(){
        val sharedPrefs = getSharedPreferences(SHARED_PREFS , Context.MODE_PRIVATE)

        val french = Locale("fr","FR","FR")
        val calendar:Calendar  = Calendar.getInstance(french)
        val currentDate:String = DateFormat.getDateInstance(DateFormat.FULL,french).format(calendar.time)

        text = sharedPrefs.getString(currentDate,"")

        Log.d("pas counter",text)

    }

//    private fun configureBottomView() {
//        activity_main_bottom_navigation.setOnNavigationItemSelectedListener { item -> updateMainFragment(item.getItemId()) }
//
//    }

//    private fun updateMainFragment(integer: Int): Boolean {
//        when (integer) {
//            R.id.action_android -> this.mainFragment.updateDesignWhenUserClickedBottomView(MainFragment.REQUEST_ANDROID)
//            R.id.action_logo -> this.mainFragment.updateDesignWhenUserClickedBottomView(MainFragment.REQUEST_LOGO)
//            R.id.action_landscape -> this.mainFragment.updateDesignWhenUserClickedBottomView(MainFragment.REQUEST_LANDSCAPE)
//        }
//        return true
//    }

}
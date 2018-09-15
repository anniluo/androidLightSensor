package com.example.anni.lightsensortest

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var sLight: Sensor? = null
    private val studyLux = IntRange(500, 1000)
    private val fullDaylightLux = IntRange(10000,10752)

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.BrightTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        var maxValue = sLight?.maximumRange?.toInt()
        max_light_textview.text = getString(R.string.max_value, maxValue.toString())
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0?.sensor == sLight) {
            val currentLight = p0?.values?.get(0)?.toInt()
            Log.d("DEBUG", "Light sensor values: ${p0?.values?.get(0) ?: -1f}" )
            current_light_textview.text = getString(R.string.current_value, currentLight.toString())
            when {
                currentLight!! < studyLux.first -> {
                    image_view.setImageResource(R.drawable.ic_lamp)
                    light_status_text.text = getString(R.string.too_dark)
                }
                currentLight >= fullDaylightLux.first -> {
                    image_view.setImageResource(R.drawable.ic_sunglasses)
                    light_status_text.text = getString(R.string.too_bright)
                }
                else -> {
                    image_view.setImageResource(R.drawable.ic_happy_emoji)
                    light_status_text.text = getString(R.string.good_light)
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onResume() {
        super.onResume()
        sLight?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL )
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    private fun changeColours() {
    }
}

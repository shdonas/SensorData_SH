/**
 * SensorData class
 *
 * @author          Shakhawat Hossain
 * @version         1.0
 * @since           11/23/2019
 */



package com.example.sensordata;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity
        implements SensorEventListener {
    private static final String TAG = "Sensor Data";

    // System sensor manager instance.
    private SensorManager mSensorManager;

    // Proximity and light sensors, as retrieved from the sensor manager.
    private Sensor mSensorLight;
    private Sensor mSensorProximity;
    private Sensor mSensorTemp;
    private Sensor mSensorHumid;
    private Sensor mSensorPressure;

    // TextViews to display current sensor values.
    private TextView mTextSensorLight;
    private TextView mTextSensorProximity;
    private TextView mTextSensorTemp;
    private TextView mTextSensorHumid;
    private TextView mTextSensorPressure;

    // sensor properties
    private float ambient_light;
    private float humidity;
    private float pressure;
    private float temperature;

    /**
     * Getting sensor data and display it
     *
     * Sensor used:
     *  Ambient Light
     *  Humidity
     *  Pressure
     *  Temperature
     *
     * @author          Shakhawat Hossain
     * @version         1.0
     * @since           11/23/2019
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize all view variables.
        mTextSensorLight = (TextView) findViewById(R.id.label_light);
        mTextSensorProximity = (TextView) findViewById(R.id.label_proximity);
        mTextSensorTemp = (TextView) findViewById(R.id.label_temp);
        mTextSensorHumid = (TextView) findViewById(R.id.label_humidity);
        mTextSensorPressure = (TextView) findViewById(R.id.label_pressure);

        // Get an instance of the sensor manager.
        mSensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);

        // Get light and proximity sensors from the sensor manager.
        // The getDefaultSensor() method returns null if the sensor
        // is not available on the device.
        mSensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorTemp = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mSensorHumid = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        mSensorPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        // Get the error message from string resources.
        String sensor_error = getResources().getString(R.string.error_no_sensor);

        // If either mSensorLight or mSensorProximity are null, those sensors
        // are not available in the device.  Set the text to the error message
        if (mSensorLight == null) { mTextSensorLight.setText(sensor_error); }
        if (mSensorProximity == null) { mTextSensorProximity.setText(sensor_error); }
        if (mSensorTemp == null) { mTextSensorTemp.setText(sensor_error); }
        if (mSensorHumid == null) { mTextSensorHumid.setText(sensor_error); }
        if (mSensorPressure == null) { mTextSensorPressure.setText(sensor_error); }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Listeners for the sensors are registered in this callback and
        // can be unregistered in onPause().
        //
        // Check to ensure sensors are available before registering listeners.
        // Both listeners are registered with a "normal" amount of delay
        // (SENSOR_DELAY_NORMAL)

        if (mSensorLight != null) {
            mSensorManager.registerListener(this, mSensorLight,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (mSensorProximity != null) {
            mSensorManager.registerListener(this, mSensorProximity,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (mTextSensorTemp != null) {
            mSensorManager.registerListener(this, mSensorTemp,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (mTextSensorHumid != null){
            mSensorManager.registerListener(this, mSensorHumid,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (mTextSensorPressure != null){
            mSensorManager.registerListener(this, mSensorPressure,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Unregister all sensor listeners in this callback so they don't
        // continue to use resources when the app is paused.
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        // The sensor type (as defined in the Sensor class).
        int sensorType = sensorEvent.sensor.getType();

        // The new data value of the sensor.  Both the light and proximity
        // sensors report one value at a time, which is always the first
        // element in the values array.
        float currentValue = sensorEvent.values[0];

        switch (sensorType) {
            // Event came from the light sensor.
            case Sensor.TYPE_LIGHT:
                // Set the light sensor text view to the light sensor string
                // from the resources, with the placeholder filled in.
                mTextSensorLight.setText(getResources().getString(
                        R.string.label_light, currentValue));
                setAmbient_light(currentValue);
                Log.d("Light", Double.toString(getAmbient_light()));
                break;

            case Sensor.TYPE_PROXIMITY:
                mTextSensorProximity.setText(getResources().getString(
                        R.string.label_proximity, currentValue));
                Log.d("Proximity", Double.toString(currentValue));
                break;

            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                mTextSensorTemp.setText(getResources().getString(
                        R.string.label_temp, currentValue));
                setTemperature(currentValue);
                Log.d("Temperature", Double.toString(getTemperature()));
                break;

            case Sensor.TYPE_RELATIVE_HUMIDITY:
                mTextSensorHumid.setText(getResources().getString(
                        R.string.label_humidity, currentValue));
                setHumidity(currentValue);
                Log.d("Humidity", Double.toString(getHumidity()));
                break;

            case Sensor.TYPE_PRESSURE:
                mTextSensorPressure.setText(getResources().getString(
                        R.string.label_pressure, currentValue));
                setPressure(currentValue);
                Log.d("Pressure", Double.toString(getPressure()));

            default:
                // do nothing
        }
    }

    /**
     * Abstract method in SensorEventListener.  It must be implemented, but is
     * unused in this app.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    /**
     * POJO- setters
     * Getting sensor data and display it
     *
     * Sensor used:
     *  Ambient Light
     *  Humidity
     *  Pressure
     *  Temperature
     *
     * @param humidity
     *
     * @author          Shakhawat Hossain
     * @version         1.0
     * @since           11/23/2019
     */
    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    /**
     *@param ambient_light
     *
     * @author          Shakhawat Hossain
     * @version         1.0
     * @since           11/23/2019
     */
    public void setAmbient_light(float ambient_light) {
        this.ambient_light = ambient_light;
    }

    /**
     *@param pressure
     *
     * @author          Shakhawat Hossain
     * @version         1.0
     * @since           11/23/2019
     */
    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    /**
     *@param temperature
     *
     * @author          Shakhawat Hossain
     * @version         1.0
     * @since           11/23/2019
     */
    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    /**
     * POJO- getters
     * Getting sensor data and display it
     *
     * Sensor used:
     *  Ambient Light
     *  Humidity
     *  Pressure
     *  Temperature
     *
     * @author          Shakhawat Hossain
     * @version         1.0
     * @since           11/23/2019
     */
    public float getAmbient_light() {
        return ambient_light;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public float getTemperature() {
        return temperature;
    }
}
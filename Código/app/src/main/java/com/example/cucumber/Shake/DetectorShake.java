package com.example.cucumber.Shake;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class DetectorShake implements SensorEventListener {

    private static final float LIMITE_GRAVEDAD = 2.7F;
    private static final int TIEMPO_SHAKE_MS = 500;
    private static final int TIEMPO_RESET_SHAKE_MS = 3000;

    private OnShakeListener mListener;
    private long mShakeTimestamp;
    private int mShakeCount;

    public void setOnShakeListener(OnShakeListener listener) {
        this.mListener = listener;
    }

    public interface OnShakeListener {
        public void onShake(int count);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mListener != null) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;

            //Cuando no haya movimiento, gForce será de un valor cercano a 1
            float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

            if (gForce > LIMITE_GRAVEDAD) {
                final long now = System.currentTimeMillis();
                //Esto es para que se ignoren los Shakes que ocurran muy cerca
                if (mShakeTimestamp + TIEMPO_SHAKE_MS > now) {
                    return;
                }

                //Se resetea la cuenta de Shakes pasados 3 segundos
                if (mShakeTimestamp + TIEMPO_RESET_SHAKE_MS < now) {
                    mShakeCount = 0;
                }

                mShakeTimestamp = now;
                mShakeCount++;
                mListener.onShake(mShakeCount);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

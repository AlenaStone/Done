package com.done.app.ui.home

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import kotlin.math.sqrt

@Composable
internal fun ShakeRefreshEffect(
    enabled: Boolean,
    onShake: () -> Unit
) {
    val context = LocalContext.current
    val currentOnShake by rememberUpdatedState(onShake)

    DisposableEffect(context, enabled) {
        if (!enabled) {
            return@DisposableEffect onDispose {}
        }

        val sensorManager =
            context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer =
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (accelerometer == null) {
            return@DisposableEffect onDispose {}
        }

        var lastShakeAt = 0L
        val listener =
            object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) {
                        return
                    }

                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]
                    val force =
                        sqrt((x * x + y * y + z * z).toDouble()) /
                                SensorManager.GRAVITY_EARTH
                    val now = SystemClock.elapsedRealtime()

                    if (force > SHAKE_FORCE_THRESHOLD && now - lastShakeAt > SHAKE_COOLDOWN_MS) {
                        lastShakeAt = now
                        currentOnShake()
                    }
                }

                override fun onAccuracyChanged(
                    sensor: Sensor?,
                    accuracy: Int
                ) = Unit
            }

        sensorManager.registerListener(
            listener,
            accelerometer,
            SensorManager.SENSOR_DELAY_UI
        )

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }
}

private const val SHAKE_FORCE_THRESHOLD = 2.7
private const val SHAKE_COOLDOWN_MS = 1_200L

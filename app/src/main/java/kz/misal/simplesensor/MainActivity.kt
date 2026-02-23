package kz.misal.simplesensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kz.misal.simplesensor.ui.theme.SimpleSensorTheme



class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelSensor: Sensor? = null

    // Состояния для Compose
    private var xValue by mutableStateOf(0f)
    private var yValue by mutableStateOf(0f)
    private var zValue by mutableStateOf(0f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Инициализируем менеджер датчиков
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        setContent {
            SimpleSensorScreen(xValue, yValue, zValue)
        }
    }

    // 2. Регистрируем слушатель при открытии приложения
    override fun onResume() {
        super.onResume()
        accelSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    // 3. Отключаем, чтобы не тратить батарею
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            xValue = event.values[0]
            yValue = event.values[1]
            zValue = event.values[2]
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

@Composable
fun SimpleSensorScreen(x: Float, y: Float, z: Float) {
    // Рассчитываем цвет фона исходя из наклона
//    val backgroundColor = Color(
//        red = ((x + 10) / 20f).coerceIn(0f, 1f),
//        green = ((y + 10) / 20f).coerceIn(0f, 1f),
//        blue = ((z + 10) / 20f).coerceIn(0f, 1f)
//    )

    Column(
        modifier = Modifier
            .fillMaxSize(),
//            .background(backgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(text = "Акселерометр", style = MaterialTheme.typography.headlineMedium)
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text(text = "Ось X (влево-вправо): ${String.format("%.2f", x)}")
                Text(text = "Ось Y (вверх-вниз): ${String.format("%.2f", y)}")
                Text(text = "Ось Z (от себя-к себе): ${String.format("%.2f", z)}")
            }
        }

        Text(
            text = "Наклоняй телефон!",
            color = if (y > 0) Color.Black else Color.White,
            modifier = Modifier.padding(top = 20.dp)
        )
    }
}
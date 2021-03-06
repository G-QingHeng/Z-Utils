package qh.z.utils.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import qh.z.utils.R
import qh.z.utils.Temperature
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cpuTemperature = Temperature()
        var temperature: String
        // 获取CPU温度
        launch(UI) {
            while (true) {
                temperature = String.format("%.2f", cpuTemperature.getCpuTemperature())
                println("[${Thread.currentThread().name}] CPU温度: ${temperature}℃")
                tv1.text = "CPU温度: ${temperature}℃"
                delay(1, TimeUnit.SECONDS)
            }
        }
    }

    val batteryReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val temperature = intent?.getIntExtra("temperature", 0)!! / 10.0
            val temperatureStr: String = String.format("%.2f", temperature)
            tv2.text = "电池温度: ${temperatureStr}℃"
        }
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(batteryReceiver, filter)
    }

    override fun onPause() {
        unregisterReceiver(batteryReceiver)
        super.onPause()
    }
}

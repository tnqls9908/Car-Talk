package com.cheayoung.car_talk_app

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import android.Manifest


class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Tag name for Log message
        val TAG = "Central"
        // used to identify adding bluetooth names
        val REQUEST_ENABLE_BT = 1
        val REQUEST_FINE_LOCATION = 2
        // scan period in milliseconds
        val SCAN_PERIOD = 5000
        // ble adapter
        var ble_adapter_: BluetoothAdapter
        // flag for scanning
        val is_scanning_ = false
        // flag for connection
        val connected_ = false
        // scan results
        var scan_results_: Map<String?, BluetoothDevice?>
        // scan callback
        var scan_cb_: ScanCallback
        // ble scanner
        var ble_scanner_: BluetoothLeScanner
        // scan handler
        var scan_handler_: Handler

        // ble manager, BLE 사용하기 위한 기본 설정
        val ble_manager: BluetoothManager
        ble_manager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        // set ble adapter
        ble_adapter_ = ble_manager.adapter


        fun startScan(v: View) {
            tv_status_.setText("Scanning...")
            // check ble adapter and ble enabled
            if (ble_adapter_ == null || !ble_adapter_.isEnabled()) {
                val ble_enable_intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(ble_enable_intent, REQUEST_ENABLE_BT)
                tv_status_.setText("Scanning Failed: ble not enabled")
                return
            }


            // used to request fine location permission


            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED){
                // Request Fine Location permission
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_FINE_LOCATION)
                tv_status_.setText("Scanning Failed: no fine location permission")
                return
            }
        }

        mode_start_button.setOnClickListener{
            start_scan()

        }

    }

    // BLE 지원확인하는 함수수
    override fun onResume() {
        super.onResume()
        // finish app if the BLE is not supported
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            finish()
        }
    }

    private fun start_scan() {

    }
}
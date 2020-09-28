package com.cheayoung.car_talk_app

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    // Tag name for Log message
    val TAG = "Central"

    // used to identify adding bluetooth names
    val REQUEST_ENABLE_BT = 1
    val REQUEST_FINE_LOCATION = 2

    // scan period in milliseconds
    val SCAN_PERIOD = 5000

    // ble adapter
    lateinit var ble_adapter: BluetoothAdapter

    // flag for scanning
    var is_scanning_ = false

    // flag for connection
    var connected_ = false

    // scan results
    lateinit var scan_results_: Map<String?, BluetoothDevice?>

    // scan callback
    lateinit var scan_cb_: ScanCallback

    // ble scanner
    lateinit var ble_scanner_: BluetoothLeScanner

    // scan handler
    lateinit var scan_handler_: Handler

    //scan 필터 이와같은 정보를 가진 것들만 스캔한다.
    val SERVICE_STRING = "0000aab0-f845-40fa-995d-658a43feea4c"
    val UUID_TDCS_SERVICE: UUID = UUID.fromString(SERVICE_STRING)
    val CHARACTERISTIC_COMMAND_STRING = "0000AAB1-F845-40FA-995D-658A43FEEA4C"
    val UUID_CTRL_COMMAND: UUID = UUID.fromString(CHARACTERISTIC_COMMAND_STRING)
    val CHARACTERISTIC_RESPONSE_STRING = "0000AAB2-F845-40FA-995D-658A43FEEA4C"
    val UUID_CTRL_RESPONSE: UUID = UUID.fromString(CHARACTERISTIC_RESPONSE_STRING)
    val MAC_ADDR = "78:A5:04:58:A7:92"

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //// set click event handler
        // ble manager

        // ble manager
        val ble_manager: BluetoothManager
        ble_manager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        // set ble adapter
        // set ble adapter
        ble_adapter = ble_manager.adapter

    }


    // ble manager, BLE 사용하기 위한 기본 설정


    // ble 스캔 시작하는 함수
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun startScan() {
        tv_status_.setText("Scanning...")
        // check ble adapter and ble enabled
        if (ble_adapter == null || !ble_adapter.isEnabled()) {
            // request BLE enable
            val ble_enable_intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(ble_enable_intent, REQUEST_ENABLE_BT)
            tv_status_.setText("Scanning Failed: ble not enabled")
            return
        }
        // check if location permission
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED) {
            // Request Fine Location permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FINE_LOCATION
            )
            tv_status_.setText("Scanning Failed: no fine location permission")
            return
        }
        //// set scan filters
        // create scan filter list
        val filters: MutableList<ScanFilter> = ArrayList()
        // create a scan filter with device mac address
        // create a scan filter with device mac address
        val scan_filter = ScanFilter.Builder()
            .setDeviceAddress(MAC_ADDR)
            .build()
        // add the filter to the list
        filters.add(scan_filter)

        //// scan settings
        // set low power scan mode
        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
            .build()

        scan_results_ = HashMap()
        scan_cb_ = BLEScanCallback(scan_results_)



        //// now ready to scan
        // start scan
        ble_scanner_.startScan(filters, settings, scan_cb_);
        // set scanning flag
        is_scanning_= true;

    }
    class BLEScanCallback internal constructor(_scan_results: Map<String?, BluetoothDevice?>) : ScanCallback()
    {
        private val cb_scan_results_: Map<String, BluetoothDevice>
        fun onScanResult(_callback_type: Int, _result: ScanResult) {
            Log.d(TAG, "onScanResult")
            addScanResult(_result)
        }

        fun onBatchScanResults(_results: List<ScanResult>) {
            for (result in _results) {
                addScanResult(result)
            }
        }

        fun onScanFailed(_error: Int) {
            Log.e(TAG, "BLE scan failed with code $_error")
        }

        /*
        Add scan result
         */
        private fun addScanResult(_result: ScanResult) {
            // get scanned device
            val device: BluetoothDevice = _result.getDevice()
            // get scanned device MAC address
            val device_address: String = device.getAddress()
            // add the device to the result list
            cb_scan_results_.put(device_address, device)
            // log
            Log.d(TAG, "scan results device: $device")
            tv_status_.setText("add scanned device: $device_address")
        }

        init {
            cb_scan_results_ = _scan_results
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
}
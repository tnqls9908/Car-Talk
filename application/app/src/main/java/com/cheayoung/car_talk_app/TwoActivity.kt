package com.cheayoung.car_talk_app

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class TwoActivity : AppCompatActivity() {
    var mBluetoothAdapter: BluetoothAdapter? = null
    lateinit var mBluetoothLeScanner: BluetoothLeScanner
    var mBluetoothLeAdvertiser: BluetoothLeAdvertiser? = null
    var beacon: Vector<Beacon>? = null
    var beaconAdapter: BeaconAdapter? = null
    var beaconListView: ListView? = null
    var mScanSettings: ScanSettings.Builder? = null
    var scanFilters: MutableList<ScanFilter>? = null
    var simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.KOREAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two)
        ActivityCompat.requestPermissions(
            this, arrayOf<String>(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), PERMISSIONS
        )
        beaconListView = findViewById(R.id.beaconListView) as ListView?
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        mBluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner()
        mBluetoothLeAdvertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser()
        beacon = Vector<Beacon>()
        mScanSettings = ScanSettings.Builder()
        mScanSettings!!.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        // 얘는 스캔 주기를 2초로 줄여주는 Setting입니다.
        // 공식문서에는 위 설정을 사용할 때는 다른 설정을 하지 말고
        // 위 설정만 단독으로 사용하라고 되어 있네요 ^^
        // 위 설정이 없으면 테스트해 본 결과 약 10초 주기로 스캔을 합니다.

        // 얘는 스캔 주기를 2초로 줄여주는 Setting입니다.
        // 공식문서에는 위 설정을 사용할 때는 다른 설정을 하지 말고
        // 위 설정만 단독으로 사용하라고 되어 있네요 ^^
        // 위 설정이 없으면 테스트해 본 결과 약 10초 주기로 스캔을 합니다.
        val scanSettings = mScanSettings!!.build()
        scanFilters = Vector()
        val scanFilter: ScanFilter.Builder = ScanFilter.Builder()
        //scanFilter.setDeviceAddress("특정 기기의 MAC 주소") //ex) 00:00:00:00:00:00
        scanFilter.setDeviceAddress("00:00:00:00:00:00") //ex) 00:00:00:00:00:00
        val scan: ScanFilter = scanFilter.build()
        (scanFilters as Vector<ScanFilter>).add(scan)
        //mBluetoothLeScanner.startScan(scanFilters, scanSettings, mScanCallback)
        mBluetoothLeScanner.startScan(mScanCallback)
        /* filter와 settings 기능을 사용하지 않을 때는
        mBluetoothLeScanner.startScan(mScanCallback)
        var 사용하시면: 처럼
        돼요.*/
    }

    var mScanCallback: ScanCallback = object : ScanCallback() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            try {
                val scanRecord: ScanRecord? = result.getScanRecord()
                Log.d("getTxPowerLevel()", scanRecord?.getTxPowerLevel().toString() + "")
                Log.d(
                    "onScanResult()",
                    result.getDevice().getAddress()
                        .toString() + "\n" + result.getRssi() + "\n" + result.getDevice().getName()
                            + "\n" + result.getDevice().getBondState() + "\n" + result.getDevice()
                        .getType()
                )
                val scanResult: ScanResult = result
                Thread {
                    runOnUiThread {
                        beacon!!.add(
                            0,
                            Beacon(

                                scanResult.getDevice().getAddress(),
                                scanResult.getRssi(),
                                simpleDateFormat.format(Date())
                            )
                        )
                        beaconAdapter = BeaconAdapter(beacon, layoutInflater)
                        beaconListView?.setAdapter(beaconAdapter)
                        beaconAdapter?.notifyDataSetChanged()
                    }
                }.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onBatchScanResults(results: List<ScanResult?>) {
            super.onBatchScanResults(results)
            Log.d("onBatchScanResults", results.size.toString() + "")
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.d("onScanFailed()", errorCode.toString() + "")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBluetoothLeScanner?.stopScan(mScanCallback)
    }

    companion object {
        private val PERMISSIONS = 100
    }
}
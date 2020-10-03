package com.cheayoung.car_talk_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_scan.*
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        go_scan_page.setOnClickListener {
            val intent = Intent(this, ScanActivity::class.java)// 다음 화면으로 이동
            startActivity(intent)
            finish()
        }
    }
}
package com.cheayoung.car_talk_app

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.util.*


class BeaconAdapter(beacons: Vector<Beacon>?, layoutInflater: LayoutInflater) :
    BaseAdapter() {
    private val beacons: Vector<Beacon>
    private val layoutInflater: LayoutInflater

    init {
        this.beacons = beacons!!
        this.layoutInflater = layoutInflater
    }

    override fun getCount(): Int {
        return beacons.size
    }

    override fun getItem(position: Int): Any {
        return beacons.get(position)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView: View? = convertView
        val beaconHolder: BeaconHolder
        if (convertView == null) {
            beaconHolder = BeaconHolder()
            convertView = layoutInflater.inflate(R.layout.item_beacon, parent, false)
            beaconHolder.address = convertView.findViewById(R.id.address)
            beaconHolder.rssi = convertView.findViewById(R.id.rssi)
            beaconHolder.time = convertView.findViewById(R.id.time)
            beaconHolder.uuid = convertView.findViewById(R.id.uuid)
            convertView.setTag(beaconHolder)
        } else {
            beaconHolder = convertView.getTag() as BeaconHolder
        }
        beaconHolder.time?.setText("시간 :" + beacons.get(position).now)
        beaconHolder.address?.setText("MAC Addr :" + beacons.get(position).address)
        beaconHolder.rssi?.setText("RSSI :" + beacons.get(position).rssi.toString() + "dBm")
        val change = beacons.get(position).uuid.toString()
        var ran1 = IntRange(change.indexOf("mManufacturerSpecificData")+27, change.indexOf("mServiceData")-4)
        var uuid_data = change.slice(ran1)
        var data_list = uuid_data.split(",")

        var ran2 = IntRange(0, data_list[0].indexOf("=")-1)
        var start_data = uuid_data.slice(ran2)
        var ran3 = IntRange(0, 3)
        var end_data : String
        try {
            end_data = data_list[data_list.size - 1].slice(ran3)
        }
        catch(e: Exception){
            end_data = "-1"
        }
        beaconHolder.uuid?.setText("UUID :" + uuid_data + "\n start : " +start_data+ "\n end : "+ end_data + "\n size : " + data_list.size )
        return convertView
    }

    private inner class BeaconHolder {
        var address: TextView? = null
        var rssi: TextView? = null
        var time: TextView? = null
        var uuid: TextView? = null
    }


}
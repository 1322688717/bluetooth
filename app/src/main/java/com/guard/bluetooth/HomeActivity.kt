package com.guard.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.permissionx.guolindev.PermissionX

class HomeActivity : AppCompatActivity() {
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btAdapt = BluetoothAdapter.getDefaultAdapter()



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PermissionX.init(this)
                .permissions(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                )
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        Toast.makeText(
                            this,
                            "All permissions are granted$grantedList",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        if (!btAdapt.isDiscovering) {
                            btAdapt.startDiscovery()
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "These permissions are denied:$deniedList",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        btAdapt.startDiscovery()
        val intent = IntentFilter()
        intent.apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            priority = IntentFilter.SYSTEM_HIGH_PRIORITY
        }
        registerReceiver(searchDevices, intent)
    }

    private val searchDevices: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                  //  LogUtil.LOGE("ACTION_STATE_CHANGED")
                    Log.e("TAG","ACTION_STATE_CHANGED")
                }
                BluetoothDevice.ACTION_FOUND -> { //found device
                    val device =
                        intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

                    if (!device?.name.isNullOrEmpty()) {
                        // 得到设备对象
                        Log.e("TAG","device===$device")
                        //mData.add(device)
                        //adapter.notifyDataSetChanged()
                    }
                }
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                   // ToastUtil.show("正在扫描")
                    Log.e("TAG","正在扫描")
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    //ToastUtil.show("扫描完成，点击列表中的设备来尝试连接")
                    Log.e("TAG","扫描完成，点击列表中的设备来尝试连接")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(searchDevices)
    }

}
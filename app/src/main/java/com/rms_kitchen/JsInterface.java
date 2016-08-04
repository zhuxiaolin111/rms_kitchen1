package com.rms_kitchen;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class JsInterface {
    private Context context;
    private UsbAdmin mUsbAdmin;
    Boolean isBluetoothConnect=false;
    private List<String> mpairedDeviceList = new ArrayList<String>();
    private ArrayAdapter<String> mArrayAdapter;
    boolean UsbConnect=false;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothDevice mBluetoothDevice = null;
    private BluetoothSocket mBluetoothSocket = null;
    OutputStream mOutputStream = null;
    /*Hint: If you are connecting to a Bluetooth serial board then try using
    * the well-known SPP UUID 00001101-0000-1000-8000-00805F9B34FB. However
    * if you are connecting to an Android peer then please generate your own unique UUID.*/
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private AlertDialog.Builder dialog = null;

    Set<BluetoothDevice> pairedDevices = null;

    public JsInterface(Context context, UsbAdmin mUsbAdmin) {
        this.context = context;
        this.mUsbAdmin = mUsbAdmin;
        mUsbAdmin.Openusb();
    }

    private void connectPrinter() {

        if (!mUsbAdmin.GetUsbStatus()) {
            mUsbAdmin.Openusb();
            UsbConnect=true;
        }
        UsbConnect=true;
    }


    private boolean sendCommand(byte[] data) {

        if (!mUsbAdmin.sendCommand(data)) {
            return false;
        } else {
            return true;
        }
    }

    @JavascriptInterface
    public void printData(String sendedData) {
        connectBluetooth();
        if(isBluetoothConnect){
            try {
                mOutputStream = mBluetoothSocket.getOutputStream();
                mOutputStream.write((sendedData).getBytes("GB2312"));
                mOutputStream.flush();
                Log.d("bluetooth", "Print success");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else {
            connectPrinter();
            try {
                sendCommand(sendedData.getBytes("GB2312"));
                Log.d("usb", "Print success");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    @JavascriptInterface
    public void startRing() {
    /*MediaPlayer mediaPlayer=MediaPlayer.create(context,R.raw.aaa);
        try {
            mediaPlayer.prepare();
             mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        NotificationManager mgr = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification nt = new Notification();
        nt.defaults = Notification.DEFAULT_SOUND;
        int soundId = 1;
        mgr.notify(soundId, nt);
    }

    public void connectBluetooth() {
      /* mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String getName = mBluetoothAdapter.getName();
        pairedDevices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            // Add the name and address to an array adapter to show in a ListView
            getName = device.getName() + "#" + device.getAddress();
            mpairedDeviceList.add(getName);
        }
        String temString =getName;
        temString = temString.substring(temString.length() - 17);
        try {
            mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(temString);
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(SPP_UUID);
            mBluetoothSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        dialog = new AlertDialog.Builder(context);
        dialog.setTitle("GouMarket:");
        dialog.setMessage("蓝牙没有打开，或没有配对设备是否在设定界面中打开？");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
               // mBluetoothAdapter.startDiscovery();
               context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                // finish();
            }
        });
        dialog.setNeutralButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                isBluetoothConnect=false;
                // TODO Auto-generated method stub
                //finish();
            }
        });
        try {
            if (mBluetoothAdapter == null) {
                Toast.makeText(context, "设备没有蓝牙适配器！", Toast.LENGTH_SHORT).show();
            } else if (mBluetoothAdapter.isEnabled()) {
                String getName = mBluetoothAdapter.getName();
                pairedDevices = mBluetoothAdapter.getBondedDevices();
               /* while (mpairedDeviceList.size() > 1) {
                    mpairedDeviceList.remove(1);
                }*/
                if (pairedDevices.size() == 0) {
                    dialog.create().show();
                }
                for (BluetoothDevice device : pairedDevices) {
                    // Add the name and address to an array adapter to show in a ListView
                    getName = device.getName() + "#" + device.getAddress();
                    mpairedDeviceList.add(getName);
                }
                String temString = getName;
                temString = temString.substring(temString.length() - 17);
                try {
                    mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(temString);
                    mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(SPP_UUID);
                    mBluetoothSocket.connect();
                    isBluetoothConnect=true;
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("bluetooth","Password error");
                }
            }
            else {
                Log.d("Bluetooth", "BluetoothAdapter not open...");
                    Toast.makeText(context,"请用USB或Bluetooth连接打印机",Toast.LENGTH_SHORT).show();
               // dialog.create().show();
             //  boolean result = mBluetoothAdapter.enable();
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.d(null,"就是不好使了，自己看着办吧！");
        }
    }
}

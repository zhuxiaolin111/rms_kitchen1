package com.rms_kitchen;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class JsInterface {
    private Context context;
    private  UsbAdmin mUsbAdmin;

    private List<String> mpairedDeviceList=new ArrayList<String>();
    private ArrayAdapter<String> mArrayAdapter;

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothDevice mBluetoothDevice=null;
    private BluetoothSocket mBluetoothSocket=null;
    OutputStream mOutputStream=null;
    /*Hint: If you are connecting to a Bluetooth serial board then try using
    * the well-known SPP UUID 00001101-0000-1000-8000-00805F9B34FB. However
    * if you are connecting to an Android peer then please generate your own unique UUID.*/
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private AlertDialog.Builder dialog=null;

    Set<BluetoothDevice> pairedDevices=null;
    public JsInterface(Context context,UsbAdmin mUsbAdmin) {
        this.context = context;
        this.mUsbAdmin = mUsbAdmin;
        mUsbAdmin.Openusb();

    }
    private void connectPrinter() {
        connectBluetooth();
        if (!mUsbAdmin.GetUsbStatus()) {
            mUsbAdmin.Openusb();
        }

    }
    private boolean sendCommand(byte[] data){

        if (!mUsbAdmin.sendCommand(data)) {
            return false;
        } else {
            return true;
        }
    }
    @JavascriptInterface
    public void printData(String sendedData) {
        connectPrinter();
        try {
            sendCommand(sendedData.getBytes("GB2312"));
            Log.d("cash", "Print success");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            mOutputStream = mBluetoothSocket.getOutputStream();
            mOutputStream.write((sendedData).getBytes("GBK"));
            mOutputStream.flush();
            ;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
       // print();

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
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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
        }
    }
}

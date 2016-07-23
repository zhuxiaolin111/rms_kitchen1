package com.rms_kitchen;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import java.util.Random;

public class JsInterface {
    private Context context;
    private SQLiteDatabase db;
    private PrinterUtils pt;

    public JsInterface(Context context, SQLiteDatabase db, PrinterUtils pt) {
        this.context = context;
        this.db = db;
        this.pt = pt;
    }

    @JavascriptInterface
    public void printData(String sendedData) {
        if (pt.connectPrinter()) {
            pt.printData(sendedData);
        } else {
            Toast.makeText(context,
                    "The printer is not connected", Toast.LENGTH_SHORT).show();
        }
    }

    @JavascriptInterface
    public void printOpen() {
        pt.printOpen();
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
        int soundId = new Random(System.currentTimeMillis())
                .nextInt(Integer.MAX_VALUE);
        mgr.notify(soundId, nt);
    }
}

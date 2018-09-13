package com.example.sadic.phonedetailsapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    TextView tvIEM, tvPhoneNo, tvSoftwareVersion;
    Button btSend;
    TelephonyManager tm;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvIEM = findViewById(R.id.tvIEM);
        tvPhoneNo = findViewById(R.id.tvPhoneNo);
        tvSoftwareVersion = findViewById(R.id.tvSerial);
        btSend = findViewById(R.id.btSend);

        tts = new TextToSpeech(MainActivity.this, this);

        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        PhoneStateListener psl = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    Toast.makeText(MainActivity.this, "Incoming Call " + incomingNumber, Toast.LENGTH_SHORT).show();
                }
                if(state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    Toast.makeText(MainActivity.this, "Call Disconnected", Toast.LENGTH_SHORT).show();
                }

                if(state == TelephonyManager.CALL_STATE_IDLE) {
                    Toast.makeText(MainActivity.this, "Phone is Idle", Toast.LENGTH_SHORT).show();
                }
            }
        };

        tm.listen(psl, PhoneStateListener.LISTEN_CALL_STATE);

        btSend.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                String imei = tm.getImei();
                tvIEM.setText(imei);
                String phoneNo = tm.getLine1Number();
                tvPhoneNo.setText(phoneNo);
                String serial = tm.getDeviceSoftwareVersion();
                tvSoftwareVersion.setText(serial);
                String phoneInfo =
                        "IMEI number is " + imei + " phone number is " + phoneNo + " Software version is " + serial;
                tts.speak(phoneInfo, TextToSpeech.QUEUE_FLUSH, null);

            }
        });


    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.ENGLISH);
            if(result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
                Toast.makeText(this, "Language is not Supported or Missing", Toast.LENGTH_SHORT).show();
            }

        }
    }


}

package ru.yandex.dunaev.mick.mecellularnetwork;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISION_CODE = 6688;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //привязать вьюшки
        ButterKnife.bind(this);
        //Проверка и запрос разрешений
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED)
        {
            telephoneInfo();
        } else {
            //запросить разрешение
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_WIFI_STATE},
                    REQUEST_PERMISION_CODE);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_PERMISION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Разрешение таки получено
                    telephoneInfo();
                }
                break;
        }
    }

    private void telephoneInfo() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        int cid;
        int lac;
        int countryCode = -1;
        int operatorId = -1;

        GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
        cid = cellLocation.getCid();
        lac = cellLocation.getLac();
        String mccAndMnc = telephonyManager.getNetworkOperator();
        if (mccAndMnc != null && mccAndMnc.length() > 3) {
            countryCode = Integer.parseInt(mccAndMnc.substring(0, 3));
            operatorId = Integer.parseInt(mccAndMnc.substring(3));
        }

        GsmCell cell = new GsmCell(countryCode,operatorId,cid,lac);
        List<WifiInfo> wifiInfoList = new ArrayList<>();

        if (wifi != null && wifi.isWifiEnabled()) {
            List<ScanResult> wifiNetworks = wifi.getScanResults();

            if (wifiNetworks != null && wifiNetworks.size() > 0) {
                for (ScanResult net : wifiNetworks) {
                    WifiInfo info = new WifiInfo(net.BSSID.toUpperCase().replace(":",""));
                    wifiInfoList.add(info);
                }
            }
        }

    }
}

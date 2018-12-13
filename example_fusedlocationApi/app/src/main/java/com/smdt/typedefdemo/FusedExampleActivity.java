package com.smdt.typedefdemo;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class FusedExampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fused_example);
        FusedLocationTracker.getTrackerinstance().buildTracker(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        FusedLocationTracker.getTrackerinstance().connectGoogleapiClient();

    }
    @Override
    protected void onResume() {
        super.onResume();
        FusedLocationTracker.getTrackerinstance().checkServices();
    }

    @Override
    protected void onPause() {
        super.onPause();
        FusedLocationTracker.getTrackerinstance().disconnectService();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case FusedLocationTracker.ALL_PERMISSION_RESULT:
              final ArrayList<String> permissionsToReject = FusedLocationTracker.getTrackerinstance().getrejectedPermissions();
                if (permissionsToReject.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsToReject.get(0))) {
                            new AlertDialog.Builder(FusedExampleActivity.this)
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsToReject.toArray(new String[permissionsToReject.size()]), FusedLocationTracker.ALL_PERMISSION_RESULT);
                                            }
                                        }
                                    }).setNegativeButton("cancel", null).create().show();
                            return;
                        }
                    }
                } else {
                    FusedLocationTracker.getTrackerinstance().connectGoogleapiClient();
                }
                break;
        }
    }
}

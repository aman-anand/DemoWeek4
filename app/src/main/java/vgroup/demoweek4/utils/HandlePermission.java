package vgroup.demoweek4.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by DELL on 10/4/2017.
 */

public class HandlePermission implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int REQUEST_PERMISSION_CODE = 1;
    public static boolean permissionCamera = true;
    public static boolean permissionStorage = true;
    private static HandlePermission handlePermissionInstance;
    private static Activity activity;
    private final String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA

            //Manifest.permission.BATTERY_STATS
    };

    private HandlePermission() {

    }

    public static HandlePermission getInstance(Activity context) {
        if (handlePermissionInstance == null) {
            handlePermissionInstance = new HandlePermission();
        }
        activity = context;
        return handlePermissionInstance;
    }

    public void requestAllPermission() {
//        AlertBuilder.getInstance().getDialog(activity,
//                "Please allow all the permissions or else some or all the features may not work"
//                , 5, 3);
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_PERMISSION_CODE);
        checkCameraPerms();
        checkStoragePerms();
    }

    private void checkCameraPerms() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            permissionCamera = true;
        }
    }

    private void checkStoragePerms() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            permissionStorage = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;


        }
    }


}

package vgroup.demoweek4.camera.ui;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import vgroup.demoweek4.R;
import vgroup.demoweek4.camera.Photo;
import vgroup.demoweek4.camera.RequestView;
import vgroup.demoweek4.camera.handler.PhotoHandler;

public class CustomCamera extends AppCompatActivity implements RequestView {
    private final static String DEBUG_TAG = "MakePhotoActivity";
    Realm realm;
    //    PhotoHandler photoHandler;
    private Camera camera;
    private Preview mPreview;
    private int cameraId = 0;
    private RelativeLayout cameraContainer;
    private ImageView imagePreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_camera_layout);
        cameraContainer = (RelativeLayout) findViewById(R.id.camera_space);
        init();
//        this.requestFeature();
//        requestWindowFeature(Window.FEATURE_NO_TITLE);


        if (!getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
                    .show();
            finish();
        } else {
            cameraId = findFrontFacingCamera();
            if (cameraId < 0) {
                Toast.makeText(this, "No front facing camera found.",
                        Toast.LENGTH_LONG).show();
            } else {
                camera = Camera.open(cameraId);
            }
        }
        mPreview = new Preview(this, camera);
        cameraContainer.addView(mPreview);
        //setContentView(mPreview);
    }

    private void init() {
        realm = Realm.getDefaultInstance();
//        photoHandler = new PhotoHandler(CustomCamera.this, (RequestView) CustomCamera.this);
        imagePreview = (ImageView) findViewById(R.id.imagePreview);

//        RealmResults<Photo> results=realm.where(Photo.class).findAllAsync();
//
//        results.addChangeListener(new RealmChangeListener<RealmResults<Photo>>() {
//            @Override
//            public void onChange(RealmResults<Photo> photos) {
//                if (photos.size()>0) {
//                    imagePreview.setImageURI(Uri.parse(photos.get(photos.size()-1).getImageUri()));
//                    Toast.makeText(CustomCamera.this, "Image Preview set sucessfully", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                //Toast.makeText(CustomCamera.this, "Realm Changed : " +realm.toString(), Toast.LENGTH_SHORT).show();

                RealmResults<Photo> results = realm.where(Photo.class).findAllAsync();
                results.addChangeListener(new RealmChangeListener<RealmResults<Photo>>() {
                    @Override
                    public void onChange(RealmResults<Photo> photos) {
                        if (photos.size() > 0) {
                            //Image image=getImageResourceID(Uri.parse(photos.get(photos.size()-1).getImageUri()));
                            imagePreview.setImageURI(Uri.parse(photos.get(photos.size() - 1).getImageUri()));
                            imagePreview.setRotation(270);
                            //Toast.makeText(CustomCamera.this, "Image Preview set sucessfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }


    public void onClick(View view) {
//        camera.startPreview();
        camera.takePicture(null, null,
                new PhotoHandler(getApplicationContext()));

    }


    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                Log.d(DEBUG_TAG, "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    protected void onPause() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
        super.onPause();
    }


    private void changeOrientation() {
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = CustomCamera.this.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    @Override
    public void getImageResourceID(Uri uri) {
        imagePreview.setImageURI(uri);
    }

    private class Preview extends SurfaceView implements SurfaceHolder.Callback {
        SurfaceHolder mHolder;
        Camera mCamera;

        Preview(Context context, Camera camera) {
            super(context);
            mCamera = camera;
            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            // The Surface has been created, acquire the camera and tell it where
            // to draw.
            //mCamera = Camera.open();
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            // Now that the size is known, set up the camera parameters and begin
            // the preview.
            Camera.Parameters parameters = mCamera.getParameters();
            List<Camera.Size> allSizes = parameters.getSupportedPictureSizes();
            Camera.Size size = allSizes.get(0); // get top size
            for (int i = 0; i < allSizes.size(); i++) {
                if (allSizes.get(i).width > size.width)
                    size = allSizes.get(i);
            }
//set max Picture Size
            parameters.setPictureSize(size.width, size.height);
            parameters.setPreviewSize(size.width, size.height);
            //mCamera.setDisplayOrientation(Camer);
            requestLayout();
            changeOrientation();
            //mCamera.setParameters(parameters);
            mCamera.startPreview();
        }


    }
}
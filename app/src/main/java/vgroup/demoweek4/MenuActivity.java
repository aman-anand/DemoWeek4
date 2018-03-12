package vgroup.demoweek4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import io.realm.Realm;
import vgroup.demoweek4.JsonVolley.requests.JsonRequestActivity;
import vgroup.demoweek4.alerts.AlertBuilder;
import vgroup.demoweek4.camera.ui.CustomCamera;
import vgroup.demoweek4.camera.ui.DefaultCameraApp;
import vgroup.demoweek4.googleMaps.ui.MapsActivity;
import vgroup.demoweek4.utils.HandlePermission;
import vgroup.demoweek4.utils.Utils;

public class MenuActivity extends AppCompatActivity {
    private Button json, map, multimedia, inbuiltCamera, customCamera, gallery;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
//        startActivity(new Intent(this,FullscreenActivity.class));
        init();
        listeners();
        Realm.init(this);

        HandlePermission.getInstance(this).requestAllPermission();
//        pattern();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
//    private void pattern() {
//        int i,j,k;
//        int space;
//        for(i=0;i<4;i++)
//        {
//            for(j=0;j<=i;j++) //for left star pattern
//            {
//                System.out.print("*");
//
//            }
//            for(j=5;(j-2*i)>0;j--)
//            {
//                System.out.print(" ");
//            }
//            for(j=0;j<=i;j++){
//                System.out.print("*");
//            }
//            System.out.print("\n");
//        }
//    }

    private void listeners() {
        json.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, JsonRequestActivity.class));
            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.checkInternet(MenuActivity.this)) {
                    startActivity(new Intent(MenuActivity.this, MapsActivity.class));

                } else {
                    AlertBuilder.getInstance().getDialog(MenuActivity.this, getString(R.string.error_internet), 1);
                }
            }
        });
        multimedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MenuActivity.this, DefaultCameraApp.class));

                if (linearLayout.getVisibility() == View.VISIBLE) {
                    linearLayout.setVisibility(View.GONE);
                    multimedia.setBackgroundColor(getResources().getColor(R.color.LightSkyBlue));

                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                    multimedia.setBackgroundColor(getResources().getColor(R.color.BlueViolet));
                }
            }
        });
        inbuiltCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, DefaultCameraApp.class));
            }
        });
        customCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (HandlePermission.permissionCamera && HandlePermission.permissionStorage) {
                    startActivity(new Intent(MenuActivity.this, CustomCamera.class));
                } else {
                    AlertBuilder.getInstance().getPermissionDialog(MenuActivity.this);
                }
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // FIXME: 10/4/2017 class reference
                startActivity(new Intent(MenuActivity.this, DefaultCameraApp.class));
            }
        });

    }

    private void init() {
        json = (Button) findViewById(R.id.jsonBtn);
        map = (Button) findViewById(R.id.mapBtn);
        inbuiltCamera = (Button) findViewById(R.id.inbuilt_camera);
        customCamera = (Button) findViewById(R.id.custom_camera);
        gallery = (Button) findViewById(R.id.camera_gallery);
        multimedia = (Button) findViewById(R.id.multiMediaBtn);
        linearLayout = (LinearLayout) findViewById(R.id.hidden_menu);
        linearLayout.setVisibility(View.GONE);

    }
}

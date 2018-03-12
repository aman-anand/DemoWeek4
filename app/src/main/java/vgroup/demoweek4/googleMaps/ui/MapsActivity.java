package vgroup.demoweek4.googleMaps.ui;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vgroup.demoweek4.R;
import vgroup.demoweek4.googleMaps.ui.beans.MapBean;
import vgroup.demoweek4.googleMaps.ui.parser.DirectionsJSONParser;
import vgroup.demoweek4.utils.Utils;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private ProgressDialog progressDialog;
    private GoogleMap mMap;
    private LatLng manali = new LatLng(32.2396, 77.1887);
    private LatLng Gulmarg = new LatLng(34.0484, 74.3805);
    private LatLng Munnar = new LatLng(10.0889, 77.0595);
    private LatLng Pachmarhi = new LatLng(22.4674, 78.4346);
    private LatLng MountAbu = new LatLng(24.5926, 72.7156);
    private LatLng Gangtok = new LatLng(27.3389, 88.6065);
    private LatLng Kodaikanal = new LatLng(10.2381, 77.4892);
    private LatLng myLoc = new LatLng(23.198454, 77.427467);
    private ArrayList<MapBean> touristPlaces;
    private Polyline polyline = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        init();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void init() {
        progressDialog = new ProgressDialog(this);
        touristPlaces = new ArrayList<>();
        touristPlaces.add(new MapBean(manali, "Manali", null));
        touristPlaces.add(new MapBean(Gulmarg, "Gulmarg", null));
        touristPlaces.add(new MapBean(Munnar, "Munnar", null));
        touristPlaces.add(new MapBean(Pachmarhi, "Pachmarhi", null));
        touristPlaces.add(new MapBean(MountAbu, "MountAbu", null));
        touristPlaces.add(new MapBean(Gangtok, "Gangtok", null));
        touristPlaces.add(new MapBean(Kodaikanal, "Kodaikanal", null));
        touristPlaces.add(new MapBean(myLoc, "my Location", null));
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        addMarkers();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 5));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getPosition() != myLoc) {
                    if (polyline != null) {
                        polyline.remove();
                    }

                    String url = getDirectionsUrl(myLoc, marker.getPosition());
                    DownloadTask downloadTask = new DownloadTask();
                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);
                }
                return false;
            }
        });

    }

    private void addMarkers() {
        mMap.clear();
        MarkerOptions m;
        for (MapBean place : touristPlaces) {
            if (place.getName().equals("my Location")) {
                m = new MarkerOptions().position(place.getLatLng()).title(place.getName());
                mMap.addMarker(m.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            } else {
                m = new MarkerOptions().position(place.getLatLng()).title(place.getName());
                mMap.addMarker(m);
            }
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer stringBuffer = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                stringBuffer.append(line);
            }

            data = stringBuffer.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Retriving and baking Information..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String s = result.substring(0, 20);
            if (result.contains("error_message")) {
                Toast.makeText(MapsActivity.this, "API query OVER LIMIT", Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
            } else {
                ParserTask parserTask = new ParserTask();


                parserTask.execute(result);
            }
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList points = null;
            PolylineOptions lineOptions = null;


            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.BLUE);
                lineOptions.geodesic(true);
                lineOptions.clickable(true);

            }

// Drawing polyline in the Google Map for the i-th route
            polyline = mMap.addPolyline(lineOptions);
            progressDialog.cancel();
            Toast.makeText(MapsActivity.this, "Distance: " + Utils.distance, Toast.LENGTH_LONG).show();
        }
    }
}



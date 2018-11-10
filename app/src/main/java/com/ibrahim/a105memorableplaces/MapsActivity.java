package com.ibrahim.a105memorableplaces;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener{
    private static final String TAG = "MapsActivity";

    LocationManager locationManager ;

    LocationListener locationListener ;

   static LatLng latLng;

    public void storedata(){
        SharedPreferences  mPrefs = this.getSharedPreferences("com.ibrahim.a105memorableplaces", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        String json = gson.toJson(MainActivity.locations);
        prefsEditor.putString("MyObject", json);
        prefsEditor.commit();
    }

    public static void movecamera(LatLng latLng){

        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,5));
        Log.i("42map","move camera");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    static GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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

        //remember intent start from her

        mMap = googleMap;

        if (latLng!=null) {
            movecamera(latLng);
        }

         locationManager =(LocationManager) getSystemService(LOCATION_SERVICE);

         locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("===",location.toString());

               movecamera( new LatLng(location.getLatitude(), location.getLongitude()));


                locationManager.removeUpdates(locationListener);
//                mMap.clear();
//                mMap.addMarker(new MarkerOptions().position(currentlocation).title("Marker in Sydney"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentlocation,15));

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };


        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

//            Location lastknownlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if (lastknownlocation != null) {
//                movecamera(new LatLng(lastknownlocation.getLatitude(), lastknownlocation.getLongitude()));
//            }
        }

        mMap.setOnMapLongClickListener(this);

    }


    @Override
    public void onMapLongClick(LatLng latLng) {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String adress = "";

        try {

            List<Address> addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);

            Log.d(TAG, "onMapLongClick: "+addressList);

            if (addressList !=null && addressList.size()>0){

                Log.i("adress",addressList.get(0).toString());

                if (addressList.get(0).getAdminArea()!=null){

                    adress+=addressList.get(0).getAdminArea()+", ";
                }

                if (addressList.get(0).getSubAdminArea()!=null){
                    adress+=addressList.get(0).getSubAdminArea()+", ";

                }

                if (addressList.get(0).getCountryName()!=null){

                    adress+=addressList.get(0).getCountryName();

                }

                Toast.makeText(this, adress, Toast.LENGTH_SHORT).show();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        SavedPlaces sp=new SavedPlaces(latLng,adress);
        MainActivity.locations.add(sp);
//        MainActivity.locationsname.add(adress);

        Log.i(MainActivity.locations.get(0).toString(),"9999999");

        MainActivity.listView.setAdapter(MainActivity.arrayAdapter);

        storedata();


    }
}

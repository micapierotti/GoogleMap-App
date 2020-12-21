package com.example.parcialapps;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
//import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
/*import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;*/

public class MapaActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ArrayList<String> lugares;

    List<LatLng> latLngList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();
    Polyline polyline = null;
    Polygon polygon = null;

    Location currentLocation;
    FusedLocationProviderClient flpc;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);


        Intent in = getIntent();
        Bundle b = in.getExtras();
        if (b != null) {
            lugares = (ArrayList<String>) b.get("lugares");
        }

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //flpc = LocationServices.getFusedLocationProviderClient(this);
        //fetchLastLocation();

    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = flpc.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude()
                    +""+currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(MapaActivity.this);
                }
            }
        });
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        actualizarMapa();

    }
    private void actualizarMapa() {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    9999);
            return;
        }
        MarkerOptions lugar = new MarkerOptions()
                .position(new LatLng(-31.620816, -60.747582))
                .title("Tu lugar elegido "+  lugares.get(0))
                .snippet(lugares.get(0));
        //mMap.addMarker(lugar);
        Marker marker = mMap.addMarker(lugar);
        markerList.add(marker);
        latLngList.add(marker.getPosition());

        /*LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .title("I am here");
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
        mMap.addMarker(markerOptions);*/

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                MarkerOptions marker = new MarkerOptions()
                        .position(latLng)
                        .title("ubic random");
                //mMap.addMarker(marker);
                Marker mark = mMap.addMarker(marker);
                markerList.add(mark);
                latLngList.add(latLng);
                //dibujarPolyline();
                dibujarPolygon();
            }
        });
    }

    public void dibujarPolyline(){
        if (polyline != null){
            polyline.remove();
        }
        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(latLngList).clickable(true);
        polyline = mMap.addPolyline(polylineOptions);
    }

    public void dibujarPolygon(){
        if(polygon != null){
            polygon.remove();
        }
        PolygonOptions polygonOptions = new PolygonOptions()
                .addAll(latLngList).clickable(true);
        polygon = mMap.addPolygon(polygonOptions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String
            permissions[], int[] grantResults) {
        switch (requestCode) {
            case 9999: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    actualizarMapa();
                }
            }
            /*case REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLastLocation();
                }
                break;*/
        }
    }


}
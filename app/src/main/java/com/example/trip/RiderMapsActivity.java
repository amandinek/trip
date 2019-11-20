package com.example.trip;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RiderMapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap riderMap;
    Location mLastLocation,nextLocation;
    LocationRequest mLocationRequest;

    private FusedLocationProviderClient mFusedLocationClient;


    private LatLng pickupLocation,dropLocation;

    private Boolean requestBol = false;

    private Marker pickupMarker,dropMarker;

    private SupportMapFragment mapFragment;

    private String destination, requestService;

    private LatLng destinationLatLng;

    private LinearLayout inputs;
    @BindView(R.id.depart)EditText departure;
    @BindView(R.id.arrival)EditText drop;
    @BindView(R.id.submit) Button request;
    @BindView(R.id.add)Button view;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        destinationLatLng=new LatLng(0.0,0.0);
        ButterKnife.bind(this);
        departure.setOnClickListener(this);
        drop.setOnClickListener(this);
        request.setOnClickListener(this);

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
        riderMap = googleMap;

        // Add a marker in Sydney and move the camera
        float zoomLevel=15.0f;
        LatLng kigali = new LatLng(-1.9501, 30.0588);
        riderMap.addMarker(new MarkerOptions().position(kigali).title("Marker in kigali"));
        riderMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kigali,zoomLevel));
    }

    @Override
    public void onClick(View v) {
        if(v==view){
            pickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            pickupMarker = riderMap.addMarker(new MarkerOptions().position(pickupLocation).title("Pickup Here")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.common_full_open_on_phone)));

            dropLocation=new LatLng(nextLocation.getLatitude(),nextLocation.getLongitude());
            dropMarker= riderMap.addMarker(new MarkerOptions().position(dropLocation).title("Next Location")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.common_full_open_on_phone)));

        }
        if(v==request){
            Intent pay =new Intent(RiderMapsActivity.this,PaymentActivity.class);
            startActivity(pay);
        }

    }
}

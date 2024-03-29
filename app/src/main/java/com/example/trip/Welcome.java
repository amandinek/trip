package com.example.trip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trip.Common.Common;
import com.example.trip.Remote.IGoogleAPI;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Welcome extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;

    private final int MY_PERMISSION_REQUEST_CODE=7000;
    private static final int PLAY_SERVICE_RES_REQUEST = 7001;

    private LocationRequest mlocationRequest;
    private GoogleApiClient mgoogleApiClient;
    private Location mLastlocation;



    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;

    DatabaseReference driver;
    GeoFire geoFire;
    Marker mCurrent;
    SupportMapFragment mapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;
//    private  SwitchMaterial switchMaterial;
    ////////Bike anim////////

    private List<LatLng> polyLineList;
    private Marker bikeMarker;
    private float v;
    private double lat,lng;
    private android.os.Handler handler;
    private LatLng startPosition,endPosition,currentPosition;
    private int index,next;
    private Button btnGo;
    private EditText mplace;
    private String destination;
    private PolylineOptions polylineOptions,blackPolyLineOptions;
    private Polyline blackPolyline,greyPolyline;
    private IGoogleAPI mService;

    Runnable drawPathRunnable = new Runnable() {
        @Override
        public void run() {
            if (index<polyLineList.size()-1){
                index++;
                next = index+1;
            }
            if (index<polyLineList.size()-1){
                startPosition =polyLineList.get(index);
                endPosition = polyLineList.get(next);
            }

            ValueAnimator valueAnimator =ValueAnimator.ofFloat(0,1);
            valueAnimator.setDuration(3000);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    v=valueAnimator.getAnimatedFraction();
                    lng =v*endPosition.longitude+(1-v)*startPosition.longitude;
                    lng =v*endPosition.latitude+(1-v)*startPosition.latitude;
                    LatLng newPos = new LatLng(lat,lng);
                    bikeMarker.setPosition(newPos);
                    bikeMarker.setAnchor(0.5f,0.5f);
                    bikeMarker.setRotation(getBearing(startPosition,newPos));
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                            .target(newPos)
                            .zoom(15.5f)
                            .build()
                    ));
                }
            });
            valueAnimator.start();
            handler.postDelayed(this,5000);
        }
    };

    private float getBearing(LatLng startPosition, LatLng endPosition) {
        double lat = Math.abs(startPosition.latitude-endPosition.latitude);
        double lng = Math.abs(startPosition.longitude-endPosition.longitude);
        if(startPosition.latitude < endPosition.latitude && startPosition.longitude < endPosition.longitude)
            return (float)(Math.toDegrees(Math.atan(lng/lat)));
        else if(startPosition.latitude >= endPosition.latitude && startPosition.longitude < endPosition.longitude)
            return (float)((90-Math.toDegrees(Math.atan(lng/lat)))+90);
        else if(startPosition.latitude >= endPosition.latitude && startPosition.longitude >= endPosition.longitude)
            return (float)(Math.toDegrees(Math.atan(lng/lat))+180);
        else if(startPosition.latitude < endPosition.latitude && startPosition.longitude >= endPosition.longitude)
            return (float)((90-Math.toDegrees(Math.atan(lng/lat)))+270);

        return -1;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        polyLineList = new ArrayList<>();
        btnGo = (Button) findViewById(R.id.btnGo);
        mplace =(EditText) findViewById(R.id.editPlace);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destination = mplace.getText().toString();
                destination = destination.replace("","+");
                Log.d("karambizi",destination);

                getDirections();
            }
        });

        driver=FirebaseDatabase.getInstance().getReference("drivers");
        geoFire =new GeoFire(driver);
        mService = Common.getGoogleAPI();


    }

    private void getDirections() {
        currentPosition = new LatLng(mLastlocation.getLatitude(),mLastlocation.getLongitude());

        String requestAPI = null;
        try {
            requestAPI ="https://maps.googleapis.com/maps/api/directions/json?"+"mode=driving&"+
                    "transit_routing_preference=less_driving&"+"origin="+ currentPosition.latitude+","+currentPosition.longitude +
                    "&"+"destination="+destination+"&"+"key="+getResources().getString(R.string.google_map_api);
            Log.d("karambizi",requestAPI);
            mService.getPath(requestAPI)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            try {
                                JSONObject jsonObject= new JSONObject(response.body().toString());
                                JSONArray jsonArray = jsonObject.getJSONArray("routes");
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject routes = jsonArray.getJSONObject(0);
                                    JSONObject poly = routes.getJSONObject("overview_polyline");
                                    String polyline = poly.getString("points");
                                    polyLineList = decodePoly(polyline);
                                }
                                LatLngBounds.Builder builder= new LatLngBounds.Builder();
                                for(LatLng latLng:polyLineList)
                                    builder.include(latLng);
                                LatLngBounds bounds= builder.build();
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,2);
                                mMap.animateCamera(cameraUpdate);

                                polylineOptions = new PolylineOptions();
                                polylineOptions.color(Color.BLACK);
                                polylineOptions.width(5);
                                polylineOptions.startCap(new SquareCap());
                                polylineOptions.endCap(new SquareCap());
                                polylineOptions.jointType(JointType.ROUND);
                                polylineOptions.addAll(polyLineList);
                                greyPolyline = mMap.addPolyline(polylineOptions);

                                blackPolyLineOptions = new PolylineOptions();
                                blackPolyLineOptions.color(Color.GRAY);
                                blackPolyLineOptions.width(5);
                                blackPolyLineOptions.startCap(new SquareCap());
                                blackPolyLineOptions.endCap(new SquareCap());
                                blackPolyLineOptions.addAll(polyLineList);
                                blackPolyline = mMap.addPolyline(blackPolyLineOptions);

                                mMap.addMarker(new MarkerOptions()
                                     .position(polyLineList.get(polyLineList.size()-1))
                                      .title("pickup location"));
                                /////animations////
                                ValueAnimator polylineAnimator = ValueAnimator.ofInt(0,100);
                                polylineAnimator.setDuration(2000);
                                polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        List<LatLng> points = greyPolyline.getPoints();
                                        int parcentValue =(int) animation.getAnimatedValue();
                                        int size = points.size();
                                        int newPoints =(int) (size*(parcentValue/100.0f));
                                        List<LatLng> p =points.subList(0,newPoints);
                                        blackPolyline.setPoints(p);

                                    }
                                });
                                polylineAnimator.start();

                                bikeMarker = mMap.addMarker(new MarkerOptions().position(currentPosition)
                                 .flat(true)
                                  .icon(BitmapDescriptorFactory.fromResource(R.drawable.bike)));
                                handler = new Handler();
                                index= 1;
                                next = 1;
                                handler.postDelayed(drawPathRunnable,3000);






                            } catch (Exception e){
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private List<LatLng> decodePoly(String polyline) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = polyline.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = polyline.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = polyline.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_CODE);
            mMap.setMyLocationEnabled(true);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mLastlocation = location;
                    Toast.makeText(getApplicationContext(), mLastlocation .getLatitude() + "" + mLastlocation .getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(Welcome.this);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPlayServices()) {
                        buildGoogleApiClient();
                        createLocationRequest();
                        displayLocation();


                } else {
                    Toast.makeText(getApplicationContext(), "Please provide the permission", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

//    private void setUpLocation(){
//    if (checkPlayServices()) {
//        buildGoogleApiClient();
//        createLocationRequest();
//        displayLocation();
    }


    private void createLocationRequest() {
        mlocationRequest = LocationRequest.create();
        mlocationRequest.setInterval(UPDATE_INTERVAL);
        mlocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mlocationRequest.setSmallestDisplacement(DISPLACEMENT);
        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildGoogleApiClient() {
        mgoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mgoogleApiClient.connect();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICE_RES_REQUEST).show();
            } else {
                Toast.makeText(this, "This device is not supported by Google Play Services", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }







    @Override
    public void onConnected(@Nullable Bundle bundle) {

        displayLocation();
        startLocationUpdates();

    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           mMap.setMyLocationEnabled(true);
            return;
        }

        mLastlocation= LocationServices.FusedLocationApi.getLastLocation(mgoogleApiClient);
        if(mLastlocation!=null){
            final double latitude = mLastlocation.getLatitude();
            final double longitude =  mLastlocation.getLongitude();
            geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {

                    if(mCurrent!=null){
                        mCurrent.remove();
                        mMap.clear();
                        handler.removeCallbacks(drawPathRunnable);
                        LatLng latLng = new LatLng(mLastlocation.getLatitude(), mLastlocation.getLongitude());
                        mCurrent =mMap.addMarker(new MarkerOptions()
                                     .icon(BitmapDescriptorFactory.fromResource(R.drawable.bike))
                                      .position(new LatLng(latitude,longitude))
                                      .title("you"));
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),15.0f));

                    }
                }
            });
        }


    }

    private void startLocationUpdates(){

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
          mMap.setMyLocationEnabled(true);
            return;
        } else {
            mapFragment.getMapAsync(this);
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mgoogleApiClient, mlocationRequest, this);
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onConnectionSuspended(int i) {
        mgoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastlocation = location;
        displayLocation();

    }
}

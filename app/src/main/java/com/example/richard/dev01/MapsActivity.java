package com.example.richard.dev01;


import android.animation.Animator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.nfc.Tag;
import android.widget.Toast;
import android.view.View;
import android.animation.AnimatorListenerAdapter;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.content.Context;
import android.app.ProgressDialog;




import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;


import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;

import java.util.Calendar;


import android.location.Location;

import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;

import android.Manifest;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;







public class MapsActivity extends FragmentActivity
        implements
        OnMyLocationButtonClickListener,
        OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;

    public static int id_batiment;


    //Nom des batiments//
    private GroundOverlay mI1nom;
    private GroundOverlay mI2nom;
    private GroundOverlay mI3nom;
    private GroundOverlay mI4nom;
    private GroundOverlay mI5nom;
    private GroundOverlay mI6nom;
    private GroundOverlay mI7nom;
    private GroundOverlay mI8nom;
    private GroundOverlay mI9nom;
    private GroundOverlay mI10nom;
    private GroundOverlay mI11nom;
    private GroundOverlay mI12nom;

    private static final int default_zoom = 17;
    private final LatLng mDefaultLocation = new LatLng(48.359375, -4.570071);


    //Polygones des batiments//
    private Polygon polygonei3;
    private Polygon polygoneB03;


    //For the date and style
    private Calendar calendar = Calendar.getInstance();


    private CameraPosition cameraPosition;

    private boolean mPermissionDenied = false;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";


    private static final String TAG = MapsActivity.class.getSimpleName();

    private View mContentView;
    private View mLoadingView;
    private int mShortAnimationDuration;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    private boolean mLocationPermissionGranted;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);



         ///TRANSITION LOADING SCREEN -> MAP
         mContentView = findViewById(R.id.map);
         mLoadingView = findViewById(R.id.frame_map);

         // Retrieve and cache the system's default "medium" animation time.
         mShortAnimationDuration = getResources().getInteger(
                 android.R.integer.config_mediumAnimTime);



    }




    CameraPosition newcameraposition(LatLng position, int val_zoom) { //Fonction : centrer sur une coordonnée avec une valeur de zoom
        cameraPosition = new CameraPosition.Builder()
                .target(position)      // Sets the center of the map to imt
                .zoom(val_zoom)                   // Sets the zoom
                .bearing(-24)                // Sets the orientation of the camera to imt north
                .build();                   // Creates a CameraPosition from the builder

        return (cameraPosition);
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {


        /////
        /////INITIALISATION DES COORDONNES/////
        /////

        mMap = googleMap;

        UiSettings UiSet = mMap.getUiSettings();
        UiSet.setCompassEnabled(true);
        UiSet.setMapToolbarEnabled(false);
        UiSet.setZoomControlsEnabled(true);

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();


        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

        TypedValue typedValue = new TypedValue();


        //CENTRE I1
        getResources().getValue(R.dimen.posi1x, typedValue, true);
        float posi1x = typedValue.getFloat();

        getResources().getValue(R.dimen.posi1y, typedValue, true);
        float posi1y = typedValue.getFloat();

        final LatLng posi1 = new LatLng(posi1y,posi1x);


        //CENTRE I3
        getResources().getValue(R.dimen.posi3x, typedValue, true);
        float posi3x = typedValue.getFloat();

        getResources().getValue(R.dimen.posi3y, typedValue, true);
        float posi3y = typedValue.getFloat();

        final LatLng posi3 = new LatLng(posi3y,posi3x);


        //CENTRE B03
        getResources().getValue(R.dimen.posB03x, typedValue, true);
        float posB03x = typedValue.getFloat();

        getResources().getValue(R.dimen.posB03y, typedValue, true);
        float posB03y = typedValue.getFloat();

        final LatLng posB03 = new LatLng(posB03y,posB03x);


        ///RESTRICTIONS///

        //RESTRICTION CONTOUR
        LatLngBounds RESTRICIMT = new LatLngBounds(
                new LatLng(48.353, -4.5739) , new LatLng(48.363, -4.565));
        // Constrain the camera target to the IMT Atlantique bounds (BAS-GAUCHE,HAUT-DROITE)
        // TODO : ROTATION DES LATLNGBOUNDS.
        mMap.setLatLngBoundsForCameraTarget(RESTRICIMT);

        //RESTRICTION ZOOM
        mMap.setMaxZoomPreference(19f); //Zoom maximal
        mMap.setMinZoomPreference(15f); //Zoom Minimal
        //////////////////////////////////////////////










        ///STYLE DE LA MAP/// (En fonction de l'heure)

        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        System.out.println("HOUR IS : " + currentHour);



        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.

            //Met le style "NIGHT"
            if ((currentHour > 18) || (currentHour < 7)) {
                boolean success = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                this, R.raw.style_json));

                if (!success) {
                    Log.e(TAG, "Style parsing failed.");
                }
            }

            //Met le style "DAY"
            else {
                boolean success = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                this, R.raw.style_json_day));

                if (!success) {
                    Log.e(TAG, "Style parsing failed.");
                }
            }


        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        /////////////////////////////////////////////////////////







        // Add a marker in IMT Atlantique and move the camera
        LatLng imt = new LatLng(48.359375,  -4.570071);


        mMap.addMarker(new MarkerOptions()
                .position(mDefaultLocation)
                .title("IMT Atlantique")
                .snippet("L'école d'ingénieur la plus proche de New York")
                .infoWindowAnchor(0.5f, 0.5f));
        ///////////////////////////////////////////////////////////////////






        //Animation au démarrage de la carte

        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation,10));

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(newcameraposition(mDefaultLocation,default_zoom)));







        //////NOM DES BATIMENTS//////


        //I1

        mI1nom = mMap.addGroundOverlay(new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.mipmap.nomi1))
                .bearing(-24)
                .zIndex(1) //Position z
                .position(posi1, 15f, 15f));

        mI1nom.setTag("Bâtiment I1");


        //I3 48.357958, -4.571160

        mI3nom = mMap.addGroundOverlay(new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.mipmap.nomi3))
                .bearing(-24)
                .zIndex(1) //Position z
                .position(posi3, 15f, 15f));

        mI3nom.setTag("Bâtiment I3");


        //TODO faire pareil en changeant les noms de batiments et en mettant les bonnes coordonnées



        //////
        //////FORME DES BATIMENTS//////
                                 //////


        //polygonI3////////////////////////////////////////////////////////

        PolygonOptions rectOptionsI3 = new PolygonOptions()
                .add(new LatLng(48.358136, -4.571402))
                .add(new LatLng(48.358184, -4.571225))
                .add(new LatLng(48.358000, -4.571101))
                .add(new LatLng(48.357957, -4.571068))
                .add(new LatLng(48.357991, -4.570939))
                .add(new LatLng(48.357893, -4.570872))
                .add(new LatLng(48.357808, -4.571171))
                .add(new LatLng(48.357947, -4.571288))
                .add(new LatLng(48.358136, -4.571402))  // Closes the polyline.
                .clickable(true)
                .zIndex(0) //Position z
                .fillColor(Color.GRAY)
                .strokeWidth(2f);


        // Ajout du polygone I3 sur la carte
        polygonei3 = mMap.addPolygon(rectOptionsI3);
        polygonei3.setTag("I3"); //permet de différencier les polygones


        //polygonB03///////////////////////////////////////////////////////

        PolygonOptions rectOptionsB03 = new PolygonOptions()
                .add(new LatLng(48.358468, -4.570853))
                .add(new LatLng(48.358533, -4.570646))
                .add(new LatLng(48.358510, -4.570631))
                .add(new LatLng(48.358555, -4.570483))
                .add(new LatLng(48.358571, -4.570391))
                .add(new LatLng(48.358412, -4.570277))
                .add(new LatLng(48.358336, -4.570540))
                .add(new LatLng(48.358363, -4.570560))
                .add(new LatLng(48.358311, -4.570742))  // Closes the polyline.
                .clickable(true)
                .zIndex(0) //Position z
                .fillColor(Color.GRAY)
                .strokeWidth(2f);

        // Ajout du polygone sur la carte
        polygoneB03 = mMap.addPolygon(rectOptionsB03);
        polygoneB03.setTag("B03"); //permet de différencier les polygones






        //Action du click sur n'importe quel polygone
        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {

                if (polygon.getTag() == "B03"){ //click sur le bat B03
                    //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(newcameraposition(posB03, 20)));
                    id_batiment = 15;
                    startActivity(new Intent(MapsActivity.this, MapsInteriorActivity.class));
                }

//                if (polygon.getTag() == "I3"){ //click sur le bat I3
//                    //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(newcameraposition(posi3,18)));
//                    id_batiment = 3;
//                    startActivity(new Intent(MapsActivity.this, MapsInteriorActivity.class));
//                }

            }
        });




        // When map renders entirely, and display MapFragment
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback()
        {
            @Override
            public void onMapLoaded()
            {

                System.out.println("MAP READY");
                ActivityLoadingScreen.mapready = true;

                FrameLayout layout = (FrameLayout)findViewById(R.id.frame_map);
                layout.setVisibility(View.GONE);

                 mContentView.setAlpha(0f);
                 mContentView.setVisibility(View.VISIBLE);

                 // Animate the content view to 100% opacity, and clear any animation
                 // listener set on the view.
                 mContentView.animate()
                         .alpha(1f)
                         .setDuration(mShortAnimationDuration)
                         .setListener(null);

                 // Animate the loading view to 0% opacity. After the animation ends,
                 // set its visibility to GONE as an optimization step (it won't
                 // participate in layout passes, etc.)
                 mLoadingView.animate()
                         .alpha(0f)
                         .setDuration(mShortAnimationDuration)
                         .setListener(new AnimatorListenerAdapter() {
                             @Override
                             public void onAnimationEnd(Animator animation) {
                                 mLoadingView.setVisibility(View.GONE);
                             }
                         });

            }
        });
















    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }


    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }




    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }



    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


}

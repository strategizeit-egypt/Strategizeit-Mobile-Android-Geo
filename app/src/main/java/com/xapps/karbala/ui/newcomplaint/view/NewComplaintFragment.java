package com.xapps.karbala.ui.newcomplaint.view;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.xapps.karbala.R;
import com.xapps.karbala.ui.area.view.AreaActivity;
import com.xapps.karbala.utils.KarbalaUtils;
import com.xapps.karbala.utils.Constants;
import com.xapps.karbala.utils.TLocationManager;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


public class NewComplaintFragment extends Fragment implements OnMapReadyCallback {

    @BindView(R.id.location_marker_iv)
    ImageView mLocationMarkerIV;


    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Location mLocation;
    public static LatLng mLatLng;
    private final static List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
            Place.Field.LAT_LNG, Place.Field.ADDRESS);


    private boolean mapIsReady = false;


    public NewComplaintFragment() {
        // Required empty public constructor
    }


    public static NewComplaintFragment newInstance() {
        NewComplaintFragment fragment = new NewComplaintFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_complaint, container, false);
        ButterKnife.bind(this, view);

        initUI();

        return view;
    }

    private void initUI() {

        if (!Places.isInitialized()) {
            Places.initialize(getContext(), getString(R.string.google_maps_key));
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mapIsReady = true;
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        mLatLng = new LatLng(32.61603, 44.02488);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng,12));

        if (isLocationAllowed()) {
            getCurrentLocation();
            new Handler().postDelayed(() -> mLocationMarkerIV.setVisibility(View.VISIBLE), 1500);
        } else {
            requestLocationPermission();
        }


    }

    @OnClick(R.id.layout_search_for_place)
    public void onSearchForPlacesClickListener(View view) {
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .build(getContext());
        startActivityForResult(intent, Constants.AUTOCOMPLETE_REQUEST_CODE);
    }

    private void getCurrentLocation() {
        TLocationManager.getInstance().start(getContext());

        try {
            mLocation = TLocationManager.getInstance().getBestLocation(getContext(), location -> {
            });
            // should check on provider itself because if we get mLocation and disabled gps still mLocation != null until close app and clear chche
            if (!TLocationManager.getInstance().checkGPSEnabled()) {
                displayLocationSettingsRequest(getContext());
            } else {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), 19));
            }
            new Handler().postDelayed(() -> mLocationMarkerIV.setVisibility(View.VISIBLE), 1500);

            mMap.setOnCameraIdleListener(() -> {
                mLatLng = mMap.getCameraPosition().target;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private boolean isLocationAllowed() {
        if (
                ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void requestLocationPermission() {
        if (shouldShowRequestPermissionRationale(
                Manifest.permission.ACCESS_COARSE_LOCATION) && shouldShowRequestPermissionRationale(
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(getContext())
                    .setTitle(getResources().getString(R.string.permission_denied))
                    .setMessage(getResources().getString(R.string.location_permission_denied_message))
                    .setPositiveButton(getResources().getString(R.string.dialog_ok),
                            (dialog, which) -> requestPermissions(
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    Constants.REQUEST_LOCATION_PERMISSION_CODE))
                    .setNegativeButton(getResources().getString(R.string.dialog_cancel), null)
                    .create()
                    .show();
        } else {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constants.REQUEST_LOCATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.REQUEST_LOCATION_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(getContext(),
                                    Manifest.permission.ACCESS_FINE_LOCATION)
                                    == PackageManager.PERMISSION_GRANTED) {
                        getCurrentLocation();
                    } else {
                        KarbalaUtils.showToast(getContext(), getResources().getString(R.string.error), Constants.FANCYERROR);

                    }

                } else {
                    KarbalaUtils.showToast(getContext(), getResources().getString(R.string.error), Constants.FANCYERROR);
                }
            }
        }
    }

    @OnClick(R.id.current_location_icon)
    public void onCurrentLocationIconClickListener(View view) {
        if (mapIsReady) {
            if (isLocationAllowed()) {
                getCurrentLocation();
                new Handler().postDelayed(() -> mLocationMarkerIV.setVisibility(View.VISIBLE), 1500);
            } else {
                requestLocationPermission();
            }
        } else {
//            Toast.makeText(getContext(), "Map not ready yet", Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.add_complaint_details)
    public void onAddComplaintClickListener(View view) {
        if (mLatLng != null) {
            Intent intent = new Intent(getContext(), AreaActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), getString(R.string.error_choose_location_first), Toast.LENGTH_SHORT).show();
        }
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    getCurrentLocation();

                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    Log.i("SettingsDTO Not sattisfied", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                    try {
                        // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().
                        status.startResolutionForResult(getActivity(), 1000);
                    } catch (IntentSender.SendIntentException e) {
                        Log.i("", "PendingIntent unable to execute request.");
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    Log.i("", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                    break;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                getCurrentLocation();
            } else {
                KarbalaUtils.showToast(getContext(), getString(R.string.enable_gps), Constants.FANCYERROR);

            }
        } else if (requestCode == Constants.AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.e("Selected Place", "Place: " + place.getName() + ", " + place.getId() + ", " + place.getLatLng());
                if (place != null)
                //location_tv.setText(userLocation.toString());
                {
                    LatLng placeLatLng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(placeLatLng, 19));
                    mLatLng = mMap.getCameraPosition().target;
                }
            } else {
                Log.e("Place", "error to find place");
            }

        }
    }
}

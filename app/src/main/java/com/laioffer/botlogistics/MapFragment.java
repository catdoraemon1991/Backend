package com.laioffer.botlogistics;


import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final String ORDER = "order_key";
    private MapView mapView;
    private View view;
    private GoogleMap googleMap;
    private LocationTracker locationTracker;
    LatLng latLng;
    private FloatingActionButton fabOrderDetail;
    private FloatingActionButton fabFocus;
    private OrderDetailDialog dialog;
    private Order order;


    public static MapFragment newInstance(Order order) {

        Bundle args = new Bundle();
        args.putSerializable(ORDER, order);
        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container,
                false);
        final String json = getActivity().getIntent().getStringExtra("EXTRA_ORDER");
        order = new Gson().fromJson(json, Order.class);
        fabOrderDetail = (FloatingActionButton)view.findViewById(R.id.fab_order_detail);
        fabOrderDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show Detail
                showDialog(null, null);
                Log.println(Log.DEBUG, "Debug:",json);
            }
        });

        fabFocus = (FloatingActionButton) view.findViewById(R.id.fab_focus);

        fabFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)      // Sets the center of the map to Mountain View
                        .zoom(16)// Sets the zoom
                        .bearing(0)           // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder

                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();// needed to get the map to display immediately
            mapView.getMapAsync(this);
        }
        if (getArguments() != null && getArguments().containsKey(ORDER)) {
            order = (Order) getArguments().get(ORDER);
        }
        // get current location
        locationTracker = new LocationTracker(getActivity());
        locationTracker.getLocation();
        latLng = new LatLng(locationTracker.getLatitude(), locationTracker.getLongitude());

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) this.view.findViewById(R.id.event_map_view);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();// needed to get the map to display immediately
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        this.googleMap = googleMap;
        this.googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        getActivity(), R.raw.style_json));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to Mountain View
                .zoom(16)// Sets the zoom
                .bearing(0)           // Sets the orientation of the camera to north
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        MarkerOptions marker = new MarkerOptions().position(latLng).
                title("You");

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.boy));

        // adding marker
        googleMap.addMarker(marker);
    }

    private void showDialog(String label, String prefillText) {
        dialog = new OrderDetailDialog(getContext());
        dialog.show();
    }

}


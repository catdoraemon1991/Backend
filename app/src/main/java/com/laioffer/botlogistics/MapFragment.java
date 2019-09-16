package com.laioffer.botlogistics;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.laioffer.entity.Location;
import com.laioffer.entity.Order;

import org.json.JSONException;
import org.json.JSONObject;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final String ORDER = "order_key";
    private MapView mapView;
    private View view;
    private GoogleMap googleMap;
    private LocationTracker locationTracker;
    LatLng focusLatLng;
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
        order = (Order)getArguments().get(ORDER);

        // Instantiate the RequestQueue.
        RequestQueue queue = HttpHelper.getInstance(getContext()).getRequestQueue();
        String url = Config.url_prefix + "tracking";

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("orderId", order.getOrderId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest postRequest = new JsonObjectRequest( Request.Method.POST, url,
                parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        generateTrack(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Handle Error
                        Log.d("Error", error.toString());
                    }
                }) {
        };
        queue.add(postRequest);

        fabOrderDetail = (FloatingActionButton)view.findViewById(R.id.fab_order_detail);
        fabOrderDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show Detail
                showDialog(null, null);
            }
        });

        fabFocus = (FloatingActionButton) view.findViewById(R.id.fab_focus);

        fabFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(focusLatLng)      // Sets the center of the map to Mountain View
                        .zoom(12)// Sets the zoom
                        .bearing(0)           // Sets the orientation of the camera to east
                        .tilt(0)                   // Sets the tilt of the camera to 30 degrees
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
//        // get current location
//        locationTracker = new LocationTracker(getActivity());
//        locationTracker.getLocation();
//        latLng = new LatLng(locationTracker.getLatitude(), locationTracker.getLongitude());

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
        this.googleMap.clear();
    }

    private void showDialog(String label, String prefillText) {
        dialog = OrderDetailDialog.newInstance(getContext(), (Order)getArguments().get(ORDER));
        dialog.show();
    }

    private void generateTrack(JSONObject response) {
        Location current = new Location();
        Location station = new Location();
        Location shippingAddress = new Location();
        Location destination = new Location();
        String status = new String();
        try {
            JSONObject currentJson = (JSONObject) response.get("currentLocation");
            current.setLat((Double) currentJson.get("latitude"))
                    .setLog((Double) currentJson.get("longitude"));

            JSONObject stationJson = (JSONObject) response.get("station");
            station.setLat((Double) stationJson.get("latitude"))
                    .setLog((Double) stationJson.get("longitude"));

            JSONObject shippingAddressJson = (JSONObject) response.get("shippingAddress");
            shippingAddress.setLat((Double) shippingAddressJson.get("latitude"))
                    .setLog((Double) shippingAddressJson.get("longitude"));

            JSONObject destinationJson = (JSONObject) response.get("destination");
            destination.setLat((Double) destinationJson.get("latitude"))
                    .setLog((Double) destinationJson.get("longitude"));

            status = (String) response.get("status");

            Log.d("current", current.toString());
            Log.d("station", station.toString());
            Log.d("shippingAddress", shippingAddress.toString());
            Log.d("destination", destination.toString());
            Log.d("status", status.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // set the default camera position
        focusLatLng = new LatLng(destination.getLat(), destination.getLog());

        Polyline line1 = googleMap.addPolyline(new PolylineOptions()
                .add(new LatLng(station.getLat(), station.getLog()), new LatLng(shippingAddress.getLat(), shippingAddress.getLog()))
                .width(20)
                .color(R.color.colorOrange));

        Polyline line2 = googleMap.addPolyline(new PolylineOptions()
                .add(new LatLng(shippingAddress.getLat(), shippingAddress.getLog()), new LatLng(destination.getLat(), destination.getLog()))
                .width(20)
                .color(R.color.colorOrange));

        // station
        addMark(station, "station", R.drawable.station);
        Log.d("isEqual", Boolean.toString("delivered".equals(Utils.DELIVER_MESG)));

        if(status.equals(Utils.DEPART_MESG) || status.equals(Utils.BEFORE_SHIP_MESG)){
            // package haven't been picked up
            addMark(shippingAddress, "Your package is here", R.drawable.box);
        }else{
            // package haven been picked up
            addMark(shippingAddress, "Your package has been picked up", R.drawable.checkmark);
        }

        if(status.equals(Utils.DEPART_MESG) || status.equals(Utils.PICKUP_MESG)){
            if(order.getShippingMethod().equals("robot")){
                // task is assigned to a robot
                addMark(current, "Robot on the way!", R.drawable.robot);
            }else {
                // task is assigned to a drone
                addMark(current, "Drone on the way!", R.drawable.drone);
            }
        }

        if(status.equals(Utils.DELIVER_MESG)){
            // package have delivered
            addMark(destination, "delivered!", R.drawable.destination);
        }else{
            // package haven't delivered yet
            addMark(destination, "destination", R.drawable.location);
        }
    }

    private void addMark(Location loc, String text, int id){
        LatLng latLng = new LatLng(loc.getLat(), loc.getLog());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to Mountain View
                .zoom(12)// Sets the zoom
                .bearing(0)           // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        MarkerOptions marker = new MarkerOptions().position(latLng).
                title(text);

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory.fromResource(id));

        // adding marker
        googleMap.addMarker(marker);

    }

}


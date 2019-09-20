package com.laioffer.botlogistics;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.IdRes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.amalbit.trail.Route;
import com.amalbit.trail.RouteOverlayView;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.laioffer.entity.Location;
import com.laioffer.entity.Order;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final String ORDER = "order_key";
    private MapView mapView;
    private View view;
    private GoogleMap googleMap;
    private RouteOverlayView mRouteOverlayView;
    private List<LatLng> track;

    private LatLng focusLatLng;

    private FloatingActionButton fabOrderDetail;
    private FloatingActionButton fabFocus;
    private OrderDetailDialog dialog;
    private Order order;

    private Location current = new Location();
    private Location station = new Location();
    private Location shippingAddress = new Location();
    private Location destination = new Location();
    private String status = new String();

    private Marker stationMarker;
    private Marker boxMarker;
    private Marker machineMarker;
    private Marker destinationMarker;

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
        configOrderDetail();

        track = new ArrayList<LatLng>();
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
                Log.d("fabOrderDetail", "Clicked!");
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

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) this.view.findViewById(R.id.event_map_view);
        mRouteOverlayView = (RouteOverlayView) this.view.findViewById(R.id.mapOverlayView);
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
        try {
            JSONObject currentJson = (JSONObject) response.get("currentLocation");
            current.setLat((Double) currentJson.get("latitude"))
                    .setLog((Double) currentJson.get("longitude"));

            JSONObject stationJson = (JSONObject) response.get("station");
            station.setLat((Double) stationJson.get("latitude"))
                    .setLog((Double) stationJson.get("longitude"));

            JSONObject shippingAddressJson = (JSONObject) response.get("shippingAddress");
            Log.d("shippingAddressJson", shippingAddressJson.toString());
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

        LatLng stationLatLng = new LatLng(station.getLat(), station.getLog());
        LatLng currentLatLng = new LatLng(current.getLat(), current.getLog());
        LatLng shippingLatLng = new LatLng(shippingAddress.getLat(), shippingAddress.getLog());
        LatLng destinationLatLng = new LatLng(destination.getLat(), destination.getLog());

        // set the default camera position
        focusLatLng = currentLatLng;

        track.add(stationLatLng);
        track.add(shippingLatLng);
        track.add(destinationLatLng);

        // draw two static lines
//        Polyline line1 = googleMap.addPolyline(new PolylineOptions()
//                .add(stationLatLng, shippingLatLng)
//                .width(20)
//                .color(R.color.colorOrange));
//
//        Polyline line2 = googleMap.addPolyline(new PolylineOptions()
//                .add(shippingLatLng, destinationLatLng)
//                .width(20)
//                .color(R.color.colorOrange));

        // draw a dynamic line
        googleMap.setOnMapLoadedCallback(() -> {
                Route normalRoute = new Route.Builder(mRouteOverlayView)
                .setRouteType(RouteOverlayView.RouteType.PATH)
                .setCameraPosition(googleMap.getCameraPosition())
                .setProjection(googleMap.getProjection())
                .setLatLngs(track)
                .setBottomLayerColor(Color.YELLOW)
                .setTopLayerColor(Color.GREEN)
                .create();
        });

        googleMap.setOnCameraMoveListener(() -> {
                    mRouteOverlayView.onCameraMove(googleMap.getProjection(), googleMap.getCameraPosition());
                }
        );

        // station
        if(status.equals(Utils.BEFORE_SHIP_MESG)){
            stationMarker = addMark(station, "Preparing your Machine...", R.drawable.station);
        }else{
            stationMarker = addMark(station, "Station", R.drawable.station);
        }

        if(status.equals(Utils.DEPART_MESG) || status.equals(Utils.BEFORE_SHIP_MESG)){
            // package haven't been picked up
            boxMarker = addMark(shippingAddress, "Your package is here", R.drawable.box);
        }else{
            // package haven been picked up
            boxMarker = addMark(shippingAddress, "Your package has been picked up", R.drawable.ic_picked_up_box);
        }

        if(status.equals(Utils.DELIVER_MESG)){
            // package have delivered
            destinationMarker = addMark(destination, "Delivered!", R.drawable.destination);
        }else{
            // package haven't delivered yet
            destinationMarker = addMark(destination, "Destination", R.drawable.location);
        }

       // set the machine to the original location and get the marker
        if(order.getShippingMethod().equals("robot")){
            // task is assigned to a robot
            machineMarker = addMark(station, "Robot on the way!", R.drawable.robot);
        }else {
            // task is assigned to a drone
            machineMarker = addMark(station, "Drone on the way!", R.drawable.drone);
        }

        // move camera to destination
        moveCamera(destination, machineMarker);
    }


    private void moveCamera(Location loc, Marker marker){
        LatLng latLng = new LatLng(loc.getLat(), loc.getLog());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to Mountain View
                .zoom(12)// Sets the zoom
                .bearing(0)           // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),3000, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                dropOtherMarker();
                startAnimateMarker(marker);
            }

            @Override
            public void onCancel() {

            }
        });
        //googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void startAnimateMarker(Marker marker){
        Queue<Location> locations = new LinkedList<>();
        // move the marker based on status
        if(status.equals(Utils.DEPART_MESG)){
            locations.offer(station);
            locations.offer(current);
            animateMarker(marker, locations, false);
        }else if(status.equals(Utils.PICKUP_MESG)){
            locations.offer(station);
            locations.offer(shippingAddress);
            locations.offer(current);
            animateMarker(marker, locations, false);
        }else if(status.equals(Utils.DELIVER_MESG)){
            locations.offer(station);
            locations.offer(shippingAddress);
            locations.offer(destination);
            animateMarker(marker, locations, true);
        }
    }

    private Marker addMark(Location loc, String text, int id){
        LatLng latLng = new LatLng(loc.getLat(), loc.getLog());

        MarkerOptions marker = new MarkerOptions().position(latLng).title(text);

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory.fromResource(id));

        // adding marker
        return googleMap.addMarker(marker);
    }

    //This methos is used to move the marker of each car smoothly when there are any updates of their position
    public void animateMarker(Marker marker, Queue<Location> locations,
                              final boolean hideMarker) {
        if(locations.size() < 2){
            return;
        }
        final LatLng startPosition = new LatLng(locations.peek().getLat(), locations.peek().getLog());
        locations.poll();

        // make it next start
        final LatLng toPosition = new LatLng(locations.peek().getLat(), locations.peek().getLog());

        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();

        final long duration = 1000;
        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startPosition.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startPosition.latitude;

                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else if(locations.size() > 1){
                    animateMarker(marker, locations, hideMarker);
                }else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    private void dropPinEffect(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1000;

        final Interpolator interpolator = new BounceInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                marker.setAnchor(0.5f, 1.0f + 14 * t);

                if (t > 0.0) {
                    // Post again 15ms later.
                    handler.postDelayed(this, 15);
                } else {
                    marker.showInfoWindow();

                }
            }
        });
    }

    private void dropOtherMarker(){
        dropPinEffect(stationMarker);
        dropPinEffect(boxMarker);
        dropPinEffect(destinationMarker);
    }

    private void configOrderDetail() {
        // show order detail data
        TextView orderId = (TextView) view.findViewById(R.id.order_detail_order_id);
        orderId.setText(order.getOrderId());
        TextView orderDeliveryTime = (TextView) view.findViewById(R.id.order_detail_order_delivery_time);
        orderDeliveryTime.setText(Utils.convertTime(order.getDeliveryTime()));
        TextView orderDepartTime = (TextView) view.findViewById(R.id.order_detail_order_depart_time);
        orderDepartTime.setText(Utils.convertTime(order.getDepartTime()));
        TextView orderDestination = (TextView) view.findViewById(R.id.order_detail_order_destination);
        orderDestination.setText(order.getDestination());
        TextView orderMachineId = (TextView) view.findViewById(R.id.order_detail_order_machine_id);
        orderMachineId.setText(order.getMachineId());
        TextView orderPickupTime = (TextView) view.findViewById(R.id.order_detail_order_pick_up_time);
        orderPickupTime.setText(Utils.convertTime(order.getPickupTime()));
        TextView orderShippingAddress = (TextView) view.findViewById(R.id.order_detail_order_shipping_address);
        orderShippingAddress.setText(order.getShippingAddress());
        TextView orderShippingMethod = (TextView) view.findViewById(R.id.order_detail_shipping_method);
        orderShippingMethod.setText(order.getShippingMethod());
        TextView orderShippingTime = (TextView) view.findViewById(R.id.order_detail_shipping_time);
        orderShippingTime.setText(Utils.convertTime(order.getShippingTime()));
        TextView orderUserId = (TextView) view.findViewById(R.id.order_detail_user_id);
        orderUserId.setText(order.getUserId());
    }

}


package com.example.android.taskcommander.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.android.taskcommander.R;
import com.example.android.taskcommander.model.Task;
import com.example.android.taskcommander.util.HttpUtils;
import com.example.android.taskcommander.util.JsonToClassMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private Mapbox mapbox;
    public MapboxMap mapboxMap;
    public Task task;

    MapboxMap.OnMapLongClickListener listener = new MapboxMap.OnMapLongClickListener() {
        @Override
        public void onMapLongClick(@NonNull LatLng point) {
            String string = String.format(Locale.US, "User clicked at: %s", point.toString());
            Toast.makeText(MapActivity.this, string, Toast.LENGTH_LONG).show();

//            ImageView dropPinView = new ImageView(getApplicationContext());
//            dropPinView.setImageResource(R.drawable.placeholder);
//
//            // Statically Set drop pin in center of screen
////            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
////            float density = getResources().getDisplayMetrics().density;
////            params.bottomMargin = (int) (12 * density);
////            dropPinView.setLayoutParams(params);
////            mapView.addView(dropPinView);

            IconFactory iconFactory = IconFactory.getInstance(getApplicationContext());

            Icon icon = iconFactory.defaultMarker();

            // Add the custom icon marker to the map
            try {
                mapboxMap.clear();
                mapboxMap.addMarker(new MarkerOptions()
                        .position(point)
                        .icon(icon));
                task.setLatitude(point.getLatitude());
                task.setLongitude(point.getLongitude());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        this.task = (Task)intent.getSerializableExtra("task");

        mapbox = Mapbox.getInstance(getApplicationContext(), "pk.eyJ1IjoicHJvZ3JhbW1lcnRvYmUiLCJhIjoiY2poZ2tyd2FnMDA4dTNjbWVmbXRzeTZveSJ9.2XY4dT2HlZiangoN0owdMQ");

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                MapActivity.this.mapboxMap = mapboxMap;
                mapboxMap.setOnMapLongClickListener(listener);
            }


        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        mapView.onStart();
    }

    public void locationDoneButtonClicked(final View view) {
        final Context context = view.getContext();
        final Intent intent = new Intent(context, TaskDetailsActivity.class);
        Handler handler = new Handler();
        AndroidNetworking.initialize(this);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonInString = mapper.writeValueAsString(task);
            JSONObject obj = new JSONObject(jsonInString);

            AndroidNetworking.put(HttpUtils.WEB_SERVICE_BASE+"/task/setLocation")
                    .addJSONObjectBody(obj)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            intent.putExtra("task", task);

                            context.startActivity(intent);
                            finish();
                        }
                        @Override
                        public void onError(ANError error) {
                            // handle error
                            System.out.print("Greska");
                        }
                    });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

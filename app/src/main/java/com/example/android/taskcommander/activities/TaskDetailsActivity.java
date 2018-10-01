package com.example.android.taskcommander.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.android.taskcommander.R;
import com.example.android.taskcommander.adapters.GroupsAdapter;
import com.example.android.taskcommander.adapters.TasksAdapter;
import com.example.android.taskcommander.model.Task;
import com.example.android.taskcommander.util.HttpUtils;
import com.example.android.taskcommander.util.JsonToClassMapper;
import com.example.android.taskcommander.util.SessionHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class TaskDetailsActivity extends AppCompatActivity {

    TextView mCaptionTextView;
    TextView mDesctriptionTextView;
    Button mCompleteButton;
    Button mLocationButton;
    Toast mToastyText;
    ProgressBar mSpinningLoader;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        Intent intent = getIntent();
        task = (Task)intent.getSerializableExtra("task");

        mCaptionTextView = (TextView) findViewById(R.id.task_details_caption_tv);
        mDesctriptionTextView = (TextView) findViewById(R.id.task_details_description_tv);
        mCompleteButton = (Button) findViewById(R.id.complete_btn);
        mLocationButton = (Button) findViewById(R.id.location_btn);

        if(!SessionHandler.loggedEmail().equals(task.getAssigneeMail())){
        //if(!task.getAssigneeMail().equals("dad@mail.com")){
            mCompleteButton.setVisibility(View.INVISIBLE);
            mLocationButton.setVisibility(View.INVISIBLE);
        }

        if(System.currentTimeMillis()>task.getDeadline().getTime()){
            mCaptionTextView.setTextColor(Color.RED);
            mLocationButton.setVisibility(View.INVISIBLE);
        }

        mSpinningLoader = (ProgressBar) findViewById(R.id.complete_progress_spinning_loader);
        mCaptionTextView.setText(task.getCaption());

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        String location ;
        try {

            if(task.getLatitude()!=-1000 && task.getLongitude()!=-1000) {
                addresses = geocoder.getFromLocation(task.getLatitude(), task.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                location = address;
            }else {
                location = "Reminder location has not been set";
            }
        } catch (IOException e) {
            location = "Reminder location has not been set";
        }

        if(System.currentTimeMillis()>task.getDeadline().getTime()){
            mCaptionTextView.setTextColor(Color.RED);
            mLocationButton.setVisibility(View.INVISIBLE);
            mDesctriptionTextView.setText(task.getDescription()+"\n\n"+ task.getDeadline()+
                    "\n\n"+ task.getAssigneeMail()+ "\n\n"+ location+ "\n\nDEADLINE HAS PASSED");
        }else {
            mDesctriptionTextView.setText(task.getDescription() + "\n\n" + task.getDeadline() + "\n\n" + task.getAssigneeMail() + "\n\n" + location);
        }
    }

    public void completeButtonClicked(final View view) {
        mSpinningLoader.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        AndroidNetworking.put(HttpUtils.WEB_SERVICE_BASE+"/task/complete/"+task.getUid()+"/"+SessionHandler.loggedEmail())
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println(response);
                        JsonToClassMapper jsonToClassMapper = new JsonToClassMapper();

                        mSpinningLoader.setVisibility(View.INVISIBLE);
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.completion_message),
                                Toast.LENGTH_SHORT).show();

                        Context context = view.getContext();
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("task", task);
                        context.startActivity(intent);

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    public void locationButtonClicked(final View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra("task", task);
        context.startActivity(intent);
        finish();
    }

}

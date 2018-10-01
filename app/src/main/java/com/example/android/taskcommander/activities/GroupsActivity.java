package com.example.android.taskcommander.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.android.taskcommander.R;
import com.example.android.taskcommander.adapters.GroupsAdapter;
import com.example.android.taskcommander.adapters.TasksAdapter;
import com.example.android.taskcommander.model.Group;
import com.example.android.taskcommander.model.Task;
import com.example.android.taskcommander.util.HttpUtils;
import com.example.android.taskcommander.util.JsonToClassMapper;
import com.example.android.taskcommander.util.SessionHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class GroupsActivity extends AppCompatActivity {

    private ArrayList<Group> groups = new ArrayList<Group>();
    private RecyclerView recyclerView;
    private GroupsAdapter gAdapter;

    ProgressBar mSpinningLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

  //      recyclerView = (RecyclerView) findViewById(R.id.groups_recycler_view);

//        gAdapter = new GroupsAdapter(this, groups);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
//        recyclerView.setAdapter(gAdapter);

        prepareGroupsData(this);

        Intent intent = getIntent();
        if(intent.hasExtra("newGroup")){
            Group group =(Group)intent.getSerializableExtra("newGroup");
            groups.add(group);
        }

    }

    private void prepareGroupsData(final Context context){
        AndroidNetworking.initialize(context);
        //AndroidNetworking.get(HttpUtils.WEB_SERVICE_BASE+"/user/initialRequest/"+ SessionHandler.loggedEmail())
        AndroidNetworking.get(HttpUtils.WEB_SERVICE_BASE+"/user/initialRequest/"+ SessionHandler.loggedEmail()+"/"+SessionHandler.loggedUid())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        JsonToClassMapper jsonToClassMapper = new JsonToClassMapper();
                        groups =  jsonToClassMapper.groupsMapping(response, context);

                        recyclerView = (RecyclerView) findViewById(R.id.groups_recycler_view);
                        gAdapter = new GroupsAdapter(context, groups);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
                        recyclerView.setAdapter(gAdapter);
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        System.out.println(error.getErrorCode() + error.getErrorDetail());
                    }
                });

//        Group group1 = new Group("Grupa 1");
//        groups.add(group1);
//
//        Group group2 = new Group("Grupa 2");
//        groups.add(group2);
    }

    public View getSpinningLoader(){
        return mSpinningLoader;
    }

    public void newGroupButtonClick(final View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, NewGroupActivity.class);
        context.startActivity(intent);
        finish();
    }
}

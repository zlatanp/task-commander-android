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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.android.taskcommander.R;
import com.example.android.taskcommander.adapters.TasksAdapter;
import com.example.android.taskcommander.model.Task;
import com.example.android.taskcommander.util.HttpUtils;
import com.example.android.taskcommander.util.JsonToClassMapper;
import com.example.android.taskcommander.util.SessionHandler;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {
    private ArrayList<Task> tasks = new ArrayList<Task>();
    private RecyclerView recyclerView;
    private TasksAdapter tAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        int index = -1;
        if(intent.hasExtra("task")){
            Task task =(Task)intent.getSerializableExtra("task");
            prepareTasksData(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.prepareTasksData(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_groups:
                Intent intent = new Intent(this, GroupsActivity.class);
                this.startActivity(intent);
                break;
            case R.id.menu_item2:
                // another startActivity, this is for item with id "menu_item2"
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void prepareTasksData(final Context context){
        //AndroidNetworking.get(HttpUtils.WEB_SERVICE_BASE+"/task/find/assignee/"+ SessionHandler.loggedEmail())
        AndroidNetworking.get(HttpUtils.WEB_SERVICE_BASE+"/task/find/assignee/"+ SessionHandler.loggedEmail())
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println(response);
                        JsonToClassMapper jsonToClassMapper = new JsonToClassMapper();
                        tasks =  jsonToClassMapper.tasksMapping(response, context);

                        recyclerView = (RecyclerView) findViewById(R.id.tasks_recycler_view);

                        tAdapter = new TasksAdapter(context, tasks);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
                        recyclerView.setAdapter(tAdapter);

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }
}

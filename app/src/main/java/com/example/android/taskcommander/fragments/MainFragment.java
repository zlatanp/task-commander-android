package com.example.android.taskcommander.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.ArrayList;
import java.util.Date;

public class MainFragment extends Fragment {

    private ArrayList<Task> tasks = new ArrayList<Task>();
    private RecyclerView recyclerView;
    private TasksAdapter tAdapter;
    private LayoutInflater inflater;
    private ViewGroup container;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        prepareTasksData(getContext(), inflater, container, view);
        this.inflater = inflater;

        int index = -1;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Task task = (Task) bundle.getSerializable("task");

            for (Task t : tasks) {
                if(t.getCaption().equals(task.getCaption())){
                    index = tasks.indexOf(t);
                    break;
                }
            }
            if(index!=-1) {
                tasks.remove(index);
            }
        }

        return view;
    }

    private void prepareTasksData(final Context context, final LayoutInflater inflater, final ViewGroup container, final View view){

        AndroidNetworking.initialize(context);

        AndroidNetworking.get(HttpUtils.WEB_SERVICE_BASE+"/user/initialRequest/"+SessionHandler.loggedEmail()+"/"+SessionHandler.loggedUid())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AndroidNetworking.get(HttpUtils.WEB_SERVICE_BASE+"/task/find/assignee/"+ SessionHandler.loggedEmail())
                                .build()
                                .getAsJSONArray(new JSONArrayRequestListener() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        System.out.println(response);
                                        JsonToClassMapper jsonToClassMapper = new JsonToClassMapper();
                                        tasks =  jsonToClassMapper.tasksMapping(response, context);

                                        recyclerView = view.findViewById(R.id.tasks_recycler_view);

                                        tAdapter = new TasksAdapter(getContext(), tasks);
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                        recyclerView.setLayoutManager(mLayoutManager);
                                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                                        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                                        recyclerView.setAdapter(tAdapter);

                                    }
                                    @Override
                                    public void onError(ANError error) {
                                        // handle error

                                    }
                                });

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });

        //AndroidNetworking.get(HttpUtils.WEB_SERVICE_BASE+"/task/find/assignee/"+ SessionHandler.loggedEmail())


    }


}

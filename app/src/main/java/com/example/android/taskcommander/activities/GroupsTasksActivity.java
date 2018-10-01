package com.example.android.taskcommander.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.android.taskcommander.R;
import com.example.android.taskcommander.adapters.GroupsAdapter;
import com.example.android.taskcommander.adapters.TasksAdapter;
import com.example.android.taskcommander.model.Group;
import com.example.android.taskcommander.model.Task;
import com.example.android.taskcommander.util.HttpUtils;
import com.example.android.taskcommander.util.JsonToClassMapper;
import com.example.android.taskcommander.util.SessionHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class GroupsTasksActivity extends AppCompatActivity {

    private ArrayList<Task> tasks = new ArrayList<Task>();
    private Group group;
    private RecyclerView recyclerView;
    private TasksAdapter tAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_tasks);

        Intent intent = getIntent();
        this.group = (Group)intent.getSerializableExtra("group");
        prepareGroupsTasksData(this, group.getUid());

        if(intent.hasExtra("parentGroup")) {
            this.group = (Group) intent.getSerializableExtra("parentGroup");
        }

    }

    private void prepareGroupsTasksData(final Context context, Long group_id){
        AndroidNetworking.initialize(context);
        //AndroidNetworking.get(HttpUtils.WEB_SERVICE_BASE+"/user/initialRequest/"+ SessionHandler.loggedEmail())
        AndroidNetworking.get(HttpUtils.WEB_SERVICE_BASE+"/task/find/group_id/"+group_id)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println(response);
                        JsonToClassMapper jsonToClassMapper = new JsonToClassMapper();
                        tasks =  jsonToClassMapper.tasksMapping(response, context);

                        recyclerView = (RecyclerView) findViewById(R.id.groups_tasks_recycler_view);

                        tAdapter = new TasksAdapter(context, tasks);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
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

    public void onAddNewTaskButtonClicked(final View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, NewTaskActivity.class);

        intent.putExtra("parentGroup", group);
        context.startActivity(intent);

    }

    public void onChatButtonClicked(final View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("parentGroup", group);
        context.startActivity(intent);

    }

    public void onAddNewMemberButtonClicked(final View view) {
        final Context context = view.getContext();
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.add_new_member_dialog);

        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("Enter Member Email!");

        Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText email = (EditText) dialog.findViewById(R.id.email);
                final String emailString = email.getText().toString();

                AndroidNetworking.initialize(context);

                AndroidNetworking.put(HttpUtils.WEB_SERVICE_BASE+"/task_group/addMember/"+group.getUid()+"/"+ emailString)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                group.getGroupMembers().add(emailString);
                                Toast.makeText(context, "Member " + emailString +" successfully added to " + group.getName() + " !",
                                        Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onError(ANError error) {
                                // handle error
                            }
                        });
                dialog.dismiss();
            }
        });

        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
        // if button is clicked, close the custom dialog
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.prepareGroupsTasksData(this, group.getUid());
    }
}

package com.example.android.taskcommander.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.android.taskcommander.R;
import com.example.android.taskcommander.adapters.TasksAdapter;
import com.example.android.taskcommander.model.Group;
import com.example.android.taskcommander.model.Task;
import com.example.android.taskcommander.model.dto.TaskDto;
import com.example.android.taskcommander.util.HttpUtils;
import com.example.android.taskcommander.util.JsonToClassMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewTaskActivity extends AppCompatActivity {
    private Group group;
    private Spinner members;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_new_task);
        }catch (Exception e){
            System.out.println(e);
        }

        Intent intent = getIntent();
        this.group = (Group)intent.getSerializableExtra("parentGroup");

        members = (Spinner) findViewById(R.id.members);
        List<String> list = new ArrayList<String>();

        for (String member : group.getGroupMembers()) {
            list.add(member);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        members.setAdapter(dataAdapter);

    }

    public void onCreateTaskButtonClicked(final View view) {
        final Context context = view.getContext();
        final Intent intent = new Intent(context, GroupsTasksActivity.class);

        EditText captionField = (EditText) findViewById(R.id.task_details_caption_et);
        String caption = captionField.getText().toString();

        EditText descriptionField = (EditText) findViewById(R.id.task_details_description_et);
        String description = descriptionField.getText().toString();

        DatePicker date = (DatePicker)findViewById(R.id.date);
        TimePicker time = (TimePicker) findViewById(R.id.time);

        int year = date.getYear();
        int month = date.getMonth();
        int day = date.getDayOfMonth();

        int hour = time.getCurrentHour();
        int minute = time.getCurrentMinute();

        String assignee = String.valueOf(members.getSelectedItem());

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, date.getDayOfMonth());
        cal.set(Calendar.MONTH, date.getMonth());
        cal.set(Calendar.YEAR, date.getYear());
        cal.set(Calendar.HOUR_OF_DAY, time.getCurrentHour());
        cal.set(Calendar.MINUTE, time.getCurrentMinute());
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        long final_time = cal.getTimeInMillis();

        Timestamp ts = new Timestamp(final_time);
//        Timestamp ts = new Timestamp(year, month, day, hour, minute, 0, 0 );
//        ts.setYear(year);
        final Task newTask = new Task(caption, description, ts, this.group.getUid(), assignee);

        try {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(newTask);

       JSONObject obj = new JSONObject(jsonInString);

        AndroidNetworking.initialize(context);
        //AndroidNetworking.get(HttpUtils.WEB_SERVICE_BASE+"/user/initialRequest/"+ SessionHandler.loggedEmail())
        AndroidNetworking.post(HttpUtils.WEB_SERVICE_BASE+"/task/create")
                .addJSONObjectBody(obj)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        intent.putExtra("newTask", newTask);
                        intent.putExtra("parentGroup", group);
                        intent.putExtra("group", group);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }


    }
}

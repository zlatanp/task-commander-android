package com.example.android.taskcommander.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.android.taskcommander.R;
import com.example.android.taskcommander.activities.GroupsActivity;
import com.example.android.taskcommander.activities.GroupsTasksActivity;
import com.example.android.taskcommander.activities.TaskDetailsActivity;
import com.example.android.taskcommander.model.Group;
import com.example.android.taskcommander.model.Task;
import com.example.android.taskcommander.util.HttpUtils;
import com.example.android.taskcommander.util.JsonToClassMapper;
import com.example.android.taskcommander.util.SessionHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Tea on 4/13/2018.
 */

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.MyViewHolder> {
    private Context context;
    private List<Group> groupsList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mGroupName;
        public Button mLeaveButon;
        public Group group;

        public MyViewHolder(View view) {
            super(view);
            mGroupName = (TextView) view.findViewById(R.id.group_name_tv);
            mLeaveButon = view.findViewById(R.id.leave_button);

            mLeaveButon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    AndroidNetworking.initialize(context);

                    AndroidNetworking.put(HttpUtils.WEB_SERVICE_BASE+"/task_group/leave/"+group.getUid()+"/"+ SessionHandler.loggedEmail())
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    Toast.makeText(context, "You left the group "+group.getName(),
                                            Toast.LENGTH_SHORT).show();
                                    removeAt(getAdapterPosition());
                                }
                                @Override
                                public void onError(ANError error) {
                                    // handle error
                                }
                            });

//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(context, group.getName(),
//                                    Toast.LENGTH_SHORT).show();
//                            removeAt(getAdapterPosition());
//
//                        }
//                    }, 2000);
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, GroupsTasksActivity.class);
                    intent.putExtra("group", group);
                    context.startActivity(intent);
                }
            });

        }

    }

    public GroupsAdapter(Context context, List<Group> groupsList) {
        this.context = context;
        this.groupsList = groupsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_row, parent, false);

        return new GroupsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Group group = groupsList.get(position);
        holder.mGroupName.setText(group.getName());
        holder.group = group;
    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }


    public void removeAt(int position) {
        groupsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, groupsList.size());
    }
}

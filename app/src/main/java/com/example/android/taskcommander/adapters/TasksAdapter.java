package com.example.android.taskcommander.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.taskcommander.R;
import com.example.android.taskcommander.activities.TaskDetailsActivity;
import com.example.android.taskcommander.model.Task;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Tea on 3/31/2018.
 */

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.MyViewHolder> {
    private Context context;
    private List<Task> tasksList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView caption;
        public TextView deadline;
        public Task task;
        public MyViewHolder(View view) {
            super(view);
            caption = (TextView) view.findViewById(R.id.task_caption_tv);
            deadline = (TextView) view.findViewById(R.id.task_deadline_tv);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, TaskDetailsActivity.class);
                    intent.putExtra("task", task);
                    context.startActivity(intent);
                }
            });
        }
    }


    public TasksAdapter(Context context, List<Task> tasksList) {
        this.context = context;
        this.tasksList = tasksList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Task task = tasksList.get(position);

        if(System.currentTimeMillis()>task.getDeadline().getTime()){
            holder.caption.setTextColor(Color.RED);
        }
        holder.caption.setText(task.getCaption()+"\n Assignee "+ task.getAssigneeMail());
        holder.task = task;
        SimpleDateFormat format = new SimpleDateFormat(context.getResources().getString(R.string.date_format));
        holder.deadline.setText(format.format(task.getDeadline()).toString());
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }
}

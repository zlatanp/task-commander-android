package com.example.android.taskcommander.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.taskcommander.R;
import com.example.android.taskcommander.activities.GroupsTasksActivity;
import com.example.android.taskcommander.model.Group;

import java.util.List;

/**
 * Created by Tea on 5/9/2018.
 */

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MyViewHolder> {
    private Context context;
    private List<String> membersList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mMember;
        public String member;
        public Button mRemoveButon;

        public MyViewHolder(View view) {
            super(view);
            mMember = (TextView) view.findViewById(R.id.member_row);

            mRemoveButon = view.findViewById(R.id.remove_member_button);

            mRemoveButon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    int i = getAdapterPosition();
                    removeAt(i);

                }
            });
        }
    }

    public MembersAdapter(Context context, List<String> membersList) {
        this.context = context;
        this.membersList = membersList;
    }

    @Override
    public MembersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        try {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.member_row, parent, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new MembersAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MembersAdapter.MyViewHolder holder, int position) {
        String member = membersList.get(position);
        holder.mMember.setText(member);
        holder.member =member;
    }

    @Override
    public int getItemCount() {
        return membersList.size();
    }


    public void removeAt(int position) {
        membersList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, membersList.size());
    }
}

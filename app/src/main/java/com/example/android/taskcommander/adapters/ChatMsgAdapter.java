package com.example.android.taskcommander.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.taskcommander.R;
import com.example.android.taskcommander.model.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatMsgAdapter extends RecyclerView.Adapter<ChatMsgAdapter.ChatMsgViewHolder> {

    public class ChatMsgViewHolder extends RecyclerView.ViewHolder {

        LinearLayout leftMsgLayout;

        LinearLayout rightMsgLayout;

        TextView leftMsgTextView;

        TextView rightMsgTextView;

        public ChatMsgViewHolder(View itemView) {
            super(itemView);

            if (itemView != null) {
                leftMsgLayout = (LinearLayout) itemView.findViewById(R.id.chat_left_msg_layout);
                rightMsgLayout = (LinearLayout) itemView.findViewById(R.id.chat_right_msg_layout);
                leftMsgTextView = (TextView) itemView.findViewById(R.id.chat_left_msg_text_view);
                rightMsgTextView = (TextView) itemView.findViewById(R.id.chat_right_msg_text_view);
            }
        }
    }

    private List<Message> msgList = null;

    public ChatMsgAdapter(List<Message> msgList) {
        this.msgList = msgList;
    }

    @Override
    public void onBindViewHolder(ChatMsgViewHolder holder, int position) {
        Message msgDto = this.msgList.get(position);
        // If the message is a received message.
        if(msgDto.MSG_TYPE_RECEIVED.equals(msgDto.getMsgType()))
        {
            // Show received message in left linearlayout.
            holder.leftMsgLayout.setVisibility(LinearLayout.VISIBLE);
            holder.leftMsgTextView.setText(msgDto.getContent());
            // Remove right linearlayout.The value should be GONE, can not be INVISIBLE
            // Otherwise each iteview's distance is too big.
            holder.rightMsgLayout.setVisibility(LinearLayout.GONE);
        }
        // If the message is a sent message.
        else if(msgDto.MSG_TYPE_SENT.equals(msgDto.getMsgType()))
        {
            // Show sent message in right linearlayout.
            holder.rightMsgLayout.setVisibility(LinearLayout.VISIBLE);
            holder.rightMsgTextView.setText(msgDto.getContent());
            // Remove left linearlayout.The value should be GONE, can not be INVISIBLE
            // Otherwise each iteview's distance is too big.
            holder.leftMsgLayout.setVisibility(LinearLayout.GONE);
        }
    }

    @Override
    public ChatMsgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_chat_app_item_view, parent, false);
        return new ChatMsgViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (msgList == null) {
            msgList = new ArrayList<Message>();
        }
        return msgList.size();
    }
}
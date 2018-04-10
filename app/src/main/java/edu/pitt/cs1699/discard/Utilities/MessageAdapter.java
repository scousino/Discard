package edu.pitt.cs1699.discard.Utilities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.android.databinding.library.baseAdapters.BR;

import edu.pitt.cs1699.discard.Database.Message;
import edu.pitt.cs1699.discard.R;
import edu.pitt.cs1699.discard.databinding.MessageListItemBinding;

import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.SimpleViewHolder> {

    private List<Message> mMessages;
    private Context mContext;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {

        private MessageListItemBinding listItemBinding;

        public SimpleViewHolder(View v) {
            super(v);
            listItemBinding = DataBindingUtil.bind(v);
        }

        public MessageListItemBinding getBinding() {
            return listItemBinding;
        }
    }

    public MessageAdapter(List<Message> messages, Context context) {
        mMessages = messages;
        mContext = context;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_list_item, parent, false);
        SimpleViewHolder holder = new SimpleViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        final Message message = mMessages.get(position);

        final View.OnClickListener keepClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatroomActivity.keepMessage(mContext);
            }
        };

        final View.OnClickListener discardClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatroomActivity.discardMessage(message, mContext);

            }
        };
        holder.getBinding().setVariable(BR.message, message);
        holder.getBinding().setVariable(BR.keepClick, keepClick);
        holder.getBinding().setVariable(BR.discardClick, discardClick);

        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public void addMessage(Message msg) {
        this.mMessages.add(msg);
        super.notifyDataSetChanged();
    }
}

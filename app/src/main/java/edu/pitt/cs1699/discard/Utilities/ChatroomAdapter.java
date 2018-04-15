package edu.pitt.cs1699.discard.Utilities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;

import java.util.List;

import edu.pitt.cs1699.discard.Activities.ChatroomActivity;
import edu.pitt.cs1699.discard.Activities.MainActivity;
import edu.pitt.cs1699.discard.Database.Chatroom;
import edu.pitt.cs1699.discard.R;
import edu.pitt.cs1699.discard.databinding.ChatroomListItemBinding;

import static edu.pitt.cs1699.discard.Enums.CHATROOM_ID;

public class ChatroomAdapter extends RecyclerView.Adapter<ChatroomAdapter.SimpleViewHolder> {

    private List<Chatroom> mChatrooms;
    private Context mContext;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {

        private ChatroomListItemBinding listItemBinding;

        public SimpleViewHolder(View v) {
            super(v);
            listItemBinding = DataBindingUtil.bind(v);
        }

        public ChatroomListItemBinding getBinding() {
            return listItemBinding;
        }
    }

    public ChatroomAdapter(List<Chatroom> chatrooms, Context context) {
        mChatrooms = chatrooms;
        mContext = context;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chatroom_list_item, parent, false);
        SimpleViewHolder holder = new SimpleViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        final Chatroom chatroom = mChatrooms.get(position);
        final View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatroomActivity.class);
                String roomName = chatroom.name;
                String roomID = chatroom.cr_id;
                intent.putExtra(CHATROOM_ID, roomID);
                mContext.startActivity(intent);
            }
        };
        final View.OnClickListener keepClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.keepChatroom(chatroom, mContext);
            }
        };

        final View.OnClickListener discardClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.discardChatroom(chatroom, mContext);

            }
        };
        holder.getBinding().setVariable(BR.chatroom, chatroom);
        holder.getBinding().setVariable(BR.onClick, click);
        holder.getBinding().setVariable(BR.keepClick, keepClick);
        holder.getBinding().setVariable(BR.discardClick, discardClick);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mChatrooms.size();
    }
}

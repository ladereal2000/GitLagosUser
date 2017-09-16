package com.practice.gitlagosuser.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.practice.gitlagosuser.R;
import com.practice.gitlagosuser.adapter.IAdapterListener.IUserAdapterListener;
import com.practice.gitlagosuser.models.GitUsers;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by David on 8/11/2017.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private Context mContext;
    private List<GitUsers> mUsersList;
    private IUserAdapterListener listener;

    public UsersAdapter(Context mContext, List<GitUsers> mUsersList, IUserAdapterListener listener) {
        this.mContext = mContext;
        this.mUsersList = mUsersList;
        this.listener = listener;
    }

    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_card, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        GitUsers user = mUsersList.get(position);

        holder.username.setText(user.getUsername());
        holder.scores.setText("Score : " + user.getScore());

        // loading album cover using Glide library
//        Glide.with(mContext).load(user.getThumbnail()).into(holder.thumbnail);

        Picasso.with(mContext).load(user.getAvatarUrl())
                .placeholder(R.drawable.avatarplaceholder)
                .into(holder.thumbnail);

        // apply click events
        applyClickEvents(holder, position);

    }

    @Override
    public long getItemId(int position) {
        return mUsersList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return mUsersList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username, scores;
        public CircleImageView thumbnail;
        public LinearLayout user_content;
        public CardView card_view;

        public ViewHolder(View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.username);
            scores = (TextView) itemView.findViewById(R.id.score);
            thumbnail = (CircleImageView) itemView.findViewById(R.id.thumbnail);
            user_content = (LinearLayout) itemView.findViewById(R.id.rl_usercontent);
            /*card_view = (CardView) itemView.findViewById(R.id.card_view);*/
        }
    }

    private void applyClickEvents(ViewHolder viewHolder, final int position){
        viewHolder.user_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUserClickedClicked(position);
            }
        });

        viewHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUserClickedClicked(position);
            }
        });
    }

}

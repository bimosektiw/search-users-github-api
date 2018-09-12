package com.example.bimosektiw.bimotakehome.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bimosektiw.bimotakehome.R;
import com.example.bimosektiw.bimotakehome.model.User;

import java.util.List;

public class UserAdapter2 extends RecyclerView.Adapter<UserAdapter2.MyViewHolder>{
    private List<User> userList;

    public UserAdapter2(List<User> userList){
        this.userList = userList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final User user = userList.get(position);

        holder.name.setText(user.getLogin());

        Context context = holder.image.getContext();
        Glide.with(context).load(user.getAvatarUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name;
        public MyViewHolder(View view){
            super(view);
            image = (ImageView) view.findViewById(R.id.profile_image);
            name = (TextView) view.findViewById(R.id.user_name);


        }

    }
}

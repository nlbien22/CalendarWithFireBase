package com.example.firebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.UserViewHolder> {

    private Context context;
    private List<User> users;

    public RecycleViewAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        if(users != null){
            User user = new User();
            user = users.get(position);
            holder.id.setText(user.getId() + "");
            holder.name.setText(user.getName());
            holder.sex.setText(user.isSex()?"True":"False");
            holder.age.setText(user.getAge() + "");
        }
    }

    @Override
    public int getItemCount() {
        return (users != null) ? users.size() : 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        private TextView id, name, sex, age;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.name);
            sex = itemView.findViewById(R.id.sex);
            age = itemView.findViewById(R.id.age);
        }
    }
}

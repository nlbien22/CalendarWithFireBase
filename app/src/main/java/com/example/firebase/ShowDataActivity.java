package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.usage.UsageEvents;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowDataActivity extends AppCompatActivity {

    public static RecycleViewAdapter adapter;
    public static List<User> list;
    RecyclerView recyclerView;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data_activty);
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycleview);
        SetUpAdapter();
    }

    private void SetUpAdapter() {
        getData(new FirebaseCallBack() {
            @Override
            public void onCallBack(List<User> users) {
                list = users;
                adapter = new RecycleViewAdapter(list);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowDataActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    public void getData(FirebaseCallBack callBack){
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<User> users = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = new User();
                        user.setId((Long) document.getData().get("id"));
                        user.setName((String) document.getData().get("name"));
                        user.setSex((Boolean) document.getData().get("sex"));
                        user.setAge(12);
                        users.add(user);
                    }
                    callBack.onCallBack(users);

                }
            }
        });
    }

    interface FirebaseCallBack {
        void onCallBack(List<User> users);
    }


}
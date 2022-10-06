package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText name, sex, age;
    Button confirm, show;
    FirebaseFirestore db;

    interface FirebaseCallBack {
        void onCallBack(long id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitView();
        db = FirebaseFirestore.getInstance();
        addData();
        Show();

    }

    private void Show() {
        show.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ShowDataActivity.class));
        });
    }

    private void addData() {
        confirm.setOnClickListener(view -> {
            User user = new User();
            user.setId(0);
            user.setAge(Integer.parseInt(age.getText().toString()));
            user.setName(name.getText().toString());
            user.setSex(Boolean.parseBoolean(sex.getText().toString()));
            getCurrentID(new FirebaseCallBack() {
                @Override
                public void onCallBack(long id) {
                    Map<String, Object> mUser = new HashMap<>();
                    mUser.put("id", id);
                    mUser.put("name", user.getName());
                    mUser.put("sex", user.isSex());
                    mUser.put("age", user.getAge());

                    db.collection("users").document(String.valueOf(id))
                            .set(mUser)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.e("TAG", "Success");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Error adding document", e);
                                }
                            });
                }
            });
        });
//        getData();
    }

    private void getCurrentID(FirebaseCallBack callBack) {
        final long[] next_id = {0};
        db.collection("users").orderBy("id", Query.Direction.DESCENDING).limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task == null)
                                next_id[0] = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                next_id[0] = (long) document.getData().get("id") + 1;
                            }
                            callBack.onCallBack(next_id[0]);
                        }
                    }
                });

    }

    public void getData(){
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.e("TAG", document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.e("TAG", "Error getting documents.", task.getException());
                }
            }
        });
    }

    private void InitView() {
        name = findViewById(R.id.name);
        sex = findViewById(R.id.sex);
        sex.setText("true");
        age = findViewById(R.id.age);
        confirm = findViewById(R.id.confirm);
        show = findViewById(R.id.show);
    }
}
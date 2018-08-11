package com.example.thuan.thuctap.Activity.Shipper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.thuan.thuctap.Adapter.Shipper.RatingShipperAdapter;
import com.example.thuan.thuctap.Model.DetailOrder;
import com.example.thuan.thuctap.Model.User;
import com.example.thuan.thuctap.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RatingShipperActivity extends AppCompatActivity {
    private RatingShipperAdapter adapter;
    private ArrayList<User> arrayList;
    private ListView lstRatingShipper;

    private FirebaseDatabase mDatabase;
    private DatabaseReference myRefUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_shipper);

        getId();
        getData();
    }

    private void getId() {
        lstRatingShipper = findViewById(R.id.lstRating_ratingShipper);
    }

    private void getData() {
        arrayList = new ArrayList<>();
        adapter = new RatingShipperAdapter(RatingShipperActivity.this, R.layout.layout_ratingshipper, arrayList);
        lstRatingShipper.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance();
        myRefUser = mDatabase.getReference("user");
        myRefUser.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getStatus().equals("Shipper")) {
                    arrayList.add(user);

                    Collections.sort(arrayList, new Comparator<User>(){
                        public int compare(User s1, User s2) {
                            return s1.getPoint().compareTo(s2.getPoint());
                        }
                    });
                    Collections.reverse(arrayList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

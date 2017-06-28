package com.braintri.chodznaobiad;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DatabaseReference database;
    private DatabaseReference today;

    private RecyclerView recyclerView;

    private Handler handler = new Handler(Looper.getMainLooper());
    private View add;

    ValueEventListener placesListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try {
                GenericTypeIndicator<HashMap<String, Place>> typeIndicator = new GenericTypeIndicator<HashMap<String, Place>>() {
                };
                final List<Place> places = new ArrayList<>(dataSnapshot.getValue(typeIndicator).values());
                handler.post(() -> setPlaces(places));
            } catch (Exception e) {
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        add = findViewById(R.id.add);
        add.setOnClickListener(view -> addPlace());

        database = FirebaseDatabase.getInstance().getReference();
        Calendar calendar = Calendar.getInstance();
        today = database.child("places").child("" + calendar.get(Calendar.DAY_OF_MONTH) + calendar.get(Calendar.MONTH) + calendar.get(Calendar.YEAR));

        today.addValueEventListener(placesListener);
    }

    private void addPlace() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add, null);
        final EditText name = dialogView.findViewById(R.id.name);
        new AlertDialog.Builder(this).setTitle("Add a place").setView(dialogView).setPositiveButton("add", (dialogInterface, i) -> {
            addPlace(name.getText().toString());
            dialogInterface.dismiss();
        }).show();
    }

    private void setPlaces(List<Place> places) {
        recyclerView.setAdapter(new PlacesAdapter(places, position -> {
            Place place = places.get(position);
            if (place.votes.contains(Build.SERIAL)) {
                place.votes.remove(Build.SERIAL);
            } else {
                place.votes.add(Build.SERIAL);
            }
            today.child(place.name).setValue(place);
        }));
    }

    private void addPlace(String name) {
        today.child(name).setValue(new Place(new ArrayList<>(), name));
    }
}

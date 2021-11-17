package com.example.locationfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button add_btn, update_btn, delete_btn, viewAll_btn, search_btn;
    private EditText location, latitude, longitude;
    private ListView listView;
    ArrayAdapter locationArrayAdapter;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        location = findViewById(R.id.location_view);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);

        //Add new record events
        add_btn = findViewById(R.id.add);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationModel locationModel;
                try{
                    locationModel = new LocationModel(+1, location.getText().toString(), latitude.getText().toString(), longitude.getText().toString());
                    Toast.makeText(MainActivity.this, locationModel.toString(), Toast.LENGTH_SHORT).show();
                }catch(Exception e){
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    locationModel = new LocationModel(-1, "error", "error", "error");
                }

                dbHandler = new DBHandler(MainActivity.this);
                boolean success = dbHandler.addOne(locationModel);
                Toast.makeText(MainActivity.this, "Success = " + success, Toast.LENGTH_SHORT).show();
                locationArrayAdapter = new ArrayAdapter<LocationModel>(MainActivity.this, android.R.layout.simple_list_item_1, dbHandler.getEveryone());
                listView.setAdapter(locationArrayAdapter);
            }
        });

        //update record events
        update_btn = findViewById(R.id.update);
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String in_address = location.getText().toString();
                String in_latitude = latitude.getText().toString();
                String in_longitude = longitude.getText().toString();

                boolean checkinsertdata = dbHandler.updateOne(in_address, in_latitude, in_longitude);
                locationArrayAdapter = new ArrayAdapter<LocationModel>(MainActivity.this, android.R.layout.simple_list_item_1, dbHandler.getEveryone());
                listView.setAdapter(locationArrayAdapter);
                if(checkinsertdata == true){
                    Toast.makeText(MainActivity.this, "update success", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "update error", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //delete record events
        delete_btn = findViewById(R.id.delete);
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = location.getText().toString();
                Boolean checkdeletedata = dbHandler.deleteOne(address);
                locationArrayAdapter = new ArrayAdapter<LocationModel>(MainActivity.this, android.R.layout.simple_list_item_1, dbHandler.getEveryone());
                listView.setAdapter(locationArrayAdapter);
                if(checkdeletedata == true){
                    Toast.makeText(MainActivity.this, "delete success", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "delete error", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //view all records in the listview
        listView = findViewById(R.id.location_list);
        viewAll_btn = findViewById(R.id.view_all);
        viewAll_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHandler = new DBHandler(MainActivity.this);
                locationArrayAdapter = new ArrayAdapter<LocationModel>(MainActivity.this, android.R.layout.simple_list_item_1, dbHandler.getEveryone());
                listView.setAdapter(locationArrayAdapter);
            }
        });


        //click listview items to geocoder
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positon, long id) {
                LocationModel locationModel = (LocationModel) adapterView.getItemAtPosition(positon);
                String latitude = locationModel.getLatitude();
                String longitude = locationModel.getLongitude();
                Geocoder geocoder = new Geocoder(MainActivity.this);
                try {
                    List<Address> address = geocoder.getFromLocation(Double.parseDouble(latitude),Double.parseDouble(longitude),1);
                    if (address.size() > 0) {
                        System.out.println(address.get(0).getLocality());
                        String location = address.get(0).getLocality();
                        Toast.makeText(getApplicationContext(), "Location: " + location, Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "unable to get location", Toast.LENGTH_SHORT).show();
                }




            }
        });

        //search record
        search_btn = findViewById(R.id.search);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHandler = new DBHandler(MainActivity.this);
                String address = location.getText().toString();

                locationArrayAdapter = new ArrayAdapter<LocationModel>(MainActivity.this, android.R.layout.simple_list_item_1, dbHandler.searchOne(address));
                listView.setAdapter(locationArrayAdapter);

//                Toast.makeText(MainActivity.this, "search", Toast.LENGTH_SHORT).show();
            }
        });
    }





}
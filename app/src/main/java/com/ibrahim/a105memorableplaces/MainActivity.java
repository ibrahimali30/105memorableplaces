package com.ibrahim.a105memorableplaces;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static ArrayList<SavedPlaces> locations;
    static ArrayList<String> locationsname;
    static ArrayAdapter arrayAdapter;
    static ListView listView;


    public void storedata(){
        SharedPreferences  mPrefs = this.getSharedPreferences("com.ibrahim.a105memorableplaces",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        String json = gson.toJson(locations);
        prefsEditor.putString("MyObject", json);
        prefsEditor.commit();
    }


    public void gotomap(View view){

        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        locations = new ArrayList<>();
        locationsname= new ArrayList<>();




        SharedPreferences  mPrefs = this.getSharedPreferences("com.ibrahim.a105memorableplaces",Context.MODE_PRIVATE);
        Gson gson = new Gson();


        String newjson = mPrefs.getString("MyObject", "");
        Type type = new TypeToken<List<SavedPlaces>>(){}.getType();
        ArrayList<SavedPlaces> newlocations = gson.fromJson(newjson, type);

        if (newlocations !=null) {
            locations = newlocations;

            for (int i=0 ; i<locations.size();i++) {
                locationsname.add(locations.get(i).getName());
            }

        }else {
            Log.i("5555555555555555","55555555555555555555555555555");
        }


        listView = (ListView) findViewById(R.id.placesListView) ;

        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,locationsname);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //update the latlong of location wewant to jumb
                MapsActivity.latLng = locations.get(i).getLatLng();

                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);

                startActivity(intent);

                Log.i(locations.get(i).getLatLng().toString(),"ttttttttttttttt");

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                locations.remove(i);
                locationsname.remove(i);


                arrayAdapter.remove(view);

                arrayAdapter.notifyDataSetChanged();
                listView.setAdapter(arrayAdapter);
                storedata();

                return false;
            }
        });

    }
}

//
//    ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add("1");arrayList.add("1");arrayList.add("ghfhfdh");arrayList.add("1");arrayList.add("1");
//
//                try {
//                sharedPreferences.edit().putString("friends",ObjectSerializer.serialize(SavedPlaces)).apply();
//                } catch (IOException e) {
//                e.printStackTrace();
//                }
//
//                SavedPlaces newsavedplaces = new SavedPlaces() ;
//                ArrayList<String> newfriends = new ArrayList<>();
//
//        try {
//        newsavedplaces = (com.ibrahim.a105memorableplaces.SavedPlaces) ObjectSerializer.deserialize(sharedPreferences.getString("friends",ObjectSerializer.serialize(new SavedPlaces())));
//        } catch (IOException e) {
//        e.printStackTrace();
//        }
//
//        Log.i("88888888888",newsavedplaces.toString());

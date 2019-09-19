package org.foodmanoeuvre;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.foodmanoeuvre.Adapter.FoodItemAdapter;
import org.foodmanoeuvre.Adapter.RestorentItemAdapter;
import org.foodmanoeuvre.modal.FoodItem;
import org.foodmanoeuvre.modal.Restorent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Ngo extends AppCompatActivity {

    CircleImageView profileimg;
    TextView textname;

    SessionManagement  session;
    HashMap<String,String> map;

    RecyclerView recycleritem;
    List<Restorent> restorentList;
    RestorentItemAdapter restorentItemAdapter;
    ImageView noti,logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo);
        profileimg=findViewById(R.id.profile_image);
        textname=findViewById(R.id.name);
        noti=findViewById(R.id.notification);
        logout=findViewById(R.id.logout);
        noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),NgoHistory.class);
                startActivity(i);
            }
        });

        session=new SessionManagement(Ngo.this);
        map=session.getUserDetails();
        Picasso.with(Ngo.this)
                .load(API.IMG_URL+map.get("image"))
                .error(R.drawable.ic_user)
                .into(profileimg);
        textname.setText(map.get("Name"));
        restorentList=new ArrayList<>();
        recycleritem=findViewById(R.id.recitem);
        recycleritem.setLayoutManager(new LinearLayoutManager(this));
        getTodayRes();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
            }
        });

    }

    private void getTodayRes() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<List<Restorent>> call=api.getRestaurants(map.get("latt"),map.get("lang"));
        call.enqueue(new Callback<List<Restorent>>() {
            @Override
            public void onResponse(Call<List<Restorent>> call, Response<List<Restorent>> response) {
                if(response.isSuccessful())
                {
                    restorentList=response.body();
                    restorentItemAdapter=new RestorentItemAdapter(restorentList);
                    recycleritem.setAdapter(restorentItemAdapter);
                }
                else
                {
                    Log.e("Error",""+response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Restorent>> call, Throwable t) {
                Log.e("Error",""+t.getMessage());
            }
        });
    }
}

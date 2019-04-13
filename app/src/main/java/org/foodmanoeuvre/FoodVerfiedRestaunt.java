package org.foodmanoeuvre;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.foodmanoeuvre.Adapter.FoodItemAdapter;
import org.foodmanoeuvre.Adapter.FoodStatusAdapter;
import org.foodmanoeuvre.modal.FoodHistory;
import org.foodmanoeuvre.modal.FoodItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodVerfiedRestaunt extends AppCompatActivity {

    RecyclerView recyclerView;
    SessionManagement  session;
    HashMap<String,String> map;
    FoodStatusAdapter  foodStatusAdapter;
    List<FoodHistory> foodItemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_verfiedrestaunt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView=findViewById(R.id.rcvhistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        session=new SessionManagement(FoodVerfiedRestaunt.this);
        map=session.getUserDetails();
        getTodayHis();
        foodItemList=new ArrayList<>();
    }

    private void getTodayHis() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<List<FoodHistory>> call=api.restoHistory(map.get("uid"));
        call.enqueue(new Callback<List<FoodHistory>>() {
            @Override
            public void onResponse(Call<List<FoodHistory>> call, Response<List<FoodHistory>> response) {
                if(response.isSuccessful())
                {
                    foodItemList=response.body();
                    foodStatusAdapter=new FoodStatusAdapter(foodItemList);
                    recyclerView.setAdapter(foodStatusAdapter);
                    Log.e("Size",""+foodItemList.size());
                }
                else
                {
                    Toast.makeText(FoodVerfiedRestaunt.this, "Something Is Wrong ", Toast.LENGTH_SHORT).show();
                    Log.e("Error",""+response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<FoodHistory>> call, Throwable t) {
                Log.e("Error",""+t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();  // optional depending on your needs
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


package org.foodmanoeuvre;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.foodmanoeuvre.Adapter.FoodItemAdapter;
import org.foodmanoeuvre.Adapter.FoodItemNgoAdapter;
import org.foodmanoeuvre.Adapter.RestorentItemAdapter;
import org.foodmanoeuvre.modal.FoodItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ItemView extends AppCompatActivity {

    SessionManagement session;
    HashMap<String,String> map;
    int uid;
    List<FoodItem> foodItemList;
    RecyclerView recycleritem;
    FoodItemNgoAdapter foodItemNgoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        session=new SessionManagement(ItemView.this);
        map=session.getUserDetails();
        foodItemList=new ArrayList<>();
        uid=getIntent().getExtras().getInt("RID");
        recycleritem=findViewById(R.id.recitem);
        recycleritem.setLayoutManager(new LinearLayoutManager(this));
        getItemlList();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void getItemlList()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<List<FoodItem>> call=api.todayItemDetail(String.valueOf(uid));
        call.enqueue(new Callback<List<FoodItem>>() {
            @Override
            public void onResponse(Call<List<FoodItem>> call, Response<List<FoodItem>> response) {
                if(response.isSuccessful())
                {
                    foodItemList=response.body();
                    foodItemNgoAdapter=new FoodItemNgoAdapter(foodItemList);
                    recycleritem.setAdapter(foodItemNgoAdapter);
                }
                else
                {
                    Toast.makeText(ItemView.this, "Something Is Wrong ", Toast.LENGTH_SHORT).show();
                    Log.e("Error",""+response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<FoodItem>> call, Throwable t) {
                Log.e("Error",""+t.getMessage());
            }
        });
    }

    public void corder(View view) {
        List<String> id=new ArrayList<String>();
        List<String> name=new ArrayList<String>();
        List<String> qtyls=new ArrayList<String>();
        if(foodItemList.size() > 0)
        {
            final ProgressDialog dialog = new ProgressDialog(ItemView.this);
            dialog.setMessage("Sending to lobby ...");
            dialog.show();
            boolean check=false;
            for( FoodItem t:foodItemList)
            {
                if(t.getQty()!=0)
                {
                    check=true;
                    id.add(String.valueOf(t.getId()));
                    name.add(t.getName());
                    qtyls.add(String.valueOf(t.getQty()));
                }
            }
            if(check)
            {
                Retrofit retrofit=new Retrofit.Builder()
                        .baseUrl(API.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .build();
                API api=retrofit.create(API.class);

                Call<String> call=api.confirmorder(String.valueOf(uid),map.get("uid"),qtyls.toArray(new String[qtyls.size()]),name.toArray(new String[name.size()]),id.toArray(new String[id.size()]));
                call.enqueue(new Callback<String>() {

                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        dialog.dismiss();
                        if(response.isSuccessful())
                        {
                            onBackPressed();
                        }
                        else
                        {
                            Toast.makeText(ItemView.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Message",""+response.errorBody().string());
                            } catch (Exception e) {

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        dialog.dismiss();
                        Log.e("Error",""+t.getMessage());
                  }
                });
            }
            else
            {
                dialog.dismiss();
            }

        }
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

package org.foodmanoeuvre;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.foodmanoeuvre.Adapter.FoodItemAdapter;
import org.foodmanoeuvre.modal.FoodItem;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FoodArea extends AppCompatActivity {
    CircleImageView profileimg;
    TextView textname;
    EditText txtitem,txtqty,txttime;
    ImageView itemig,imgnoti,imglogout;
    Button btnadd;

    Bitmap itemage=null;

    SessionManagement  session;
    HashMap<String,String> map;

    RecyclerView recycleritem;
    List<FoodItem> foodItemList;
    FoodItemAdapter foodItemAdapter;

    TimePickerDialog picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodarea);
        profileimg=findViewById(R.id.profile_image);
        textname=findViewById(R.id.name);
        txtitem=findViewById(R.id.txtname);
        txtqty=findViewById(R.id.txtqty);
        txttime=findViewById(R.id.txttime);
        itemig=findViewById(R.id.itemimg);
        btnadd=findViewById(R.id.itemadd);
        recycleritem=findViewById(R.id.recitem);
        imgnoti=findViewById(R.id.notification);
        imglogout=findViewById(R.id.logout);

        recycleritem.setLayoutManager(new LinearLayoutManager(this));
        session=new SessionManagement(FoodArea.this);
        map=session.getUserDetails();
        foodItemList=new ArrayList<>();

        Picasso.with(FoodArea.this)
                .load(API.IMG_URL+map.get("image"))
                .error(R.drawable.ic_user)
                .into(profileimg);

        textname.setText(map.get("Name"));
        txttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);

                picker = new TimePickerDialog(FoodArea.this, R.style.TimePicker,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        txttime.setText(hourOfDay + ":" + minute);

                    }
                }, hour, minutes, true);
                picker.show();
            }
        });
        itemig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");


                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(Intent.createChooser(chooserIntent, "Select Picture"), 200);
            }
        });

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation())
                {
                    addItem();
                }
            }
        });

        imgnoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),FoodVerfiedRestaunt.class);
                startActivity(i);
            }
        });
        imglogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
            }
        });
        getTodayFood();
    }

    private void getTodayFood() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<List<FoodItem>> call=api.getTodayFood(map.get("uid").toString());
        call.enqueue(new Callback<List<FoodItem>>() {
            @Override
            public void onResponse(Call<List<FoodItem>> call, Response<List<FoodItem>> response) {
                if(response.isSuccessful())
                {
                    foodItemList=response.body();
                    foodItemAdapter=new FoodItemAdapter(foodItemList);
                    recycleritem.setAdapter(foodItemAdapter);
                    Log.e("Size",""+foodItemList.size());
                }
                else
                {
                    Toast.makeText(FoodArea.this, "Something Is Wrong ", Toast.LENGTH_SHORT).show();
                    Log.e("Error",""+response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<FoodItem>> call, Throwable t) {
                Log.e("Error",""+t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            try {
                final Bundle extras = data.getExtras();
                if (extras != null) {
                    itemage = extras.getParcelable("data");
                    itemig.setImageBitmap(itemage);
                }
                Uri u=data.getData();
                itemage= MediaStore.Images.Media.getBitmap(this.getContentResolver(), u);
                itemig.setImageBitmap(itemage);
            } catch (Exception e) {
                Log.e("error",""+e.getMessage());
            }
        }
    }

    public boolean validation()
    {
        boolean check=true;
        if(txtitem.getText().toString().trim().equals(""))
        {
            txtitem.setError("Please Enter Item Name");
            check=false;
        }
        if(txttime.getText().toString().trim().equals(""))
        {
            txttime.setError("Please Select Time");
            check=false;
        }
        if(txtqty.getText().toString().trim().equals(""))
        {
            txtqty.setError("Please Enter Quantity");
            check=false;
        }
        if(itemage==null)
        {
            Toast.makeText(this, "Please select Image", Toast.LENGTH_SHORT).show();
        }
        return check;
    }

    private void clear()
    {
        txtitem.setText("");
        txtqty.setText("");
        txttime.setText("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            itemig.setImageDrawable(getDrawable(R.drawable.ic_menu));
        }
    }
    private void addItem() {
        Map<String, RequestBody> mapfill=new HashMap<>();
        mapfill.put("Name", RequestBody.create(MediaType.parse("multipart/form-data"),txtitem.getText().toString()));
        mapfill.put("Time",RequestBody.create(MediaType.parse("multipart/form-data"),txttime.getText().toString()));
        mapfill.put("Qty",RequestBody.create(MediaType.parse("multipart/form-data"),txtqty.getText().toString()));
        mapfill.put("R_id",RequestBody.create(MediaType.parse("multipart/form-data"),map.get("uid").toString()));

        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File file = wrapper.getDir("Images",MODE_PRIVATE);
        file = new File(file, UUID.randomUUID().toString()+".jpg");
        try{
            OutputStream stream = null;
            stream = new FileOutputStream(file);
            itemage.compress(Bitmap.CompressFormat.JPEG,50,stream);
            stream.flush();
            stream.close();
        }catch(Exception ee){

        }
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"),file );
        MultipartBody.Part body = MultipartBody.Part.createFormData("Image", file.getName(), reqFile);

        final ProgressDialog progressDialog = new ProgressDialog(FoodArea.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Processing ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        API api=retrofit.create(API.class);

        Call<String> call=api.addFood(body,mapfill);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialog.dismiss();
                if(response.isSuccessful())
                {
                    try {
                        JSONObject json=new JSONObject(response.body());
                        FoodItem foodItem=new FoodItem();
                        foodItem.setImage(json.getString("Image"));
                        foodItem.setQty(Integer.parseInt(txtqty.getText().toString()));
                        foodItem.setName(txtitem.getText().toString());
                        foodItem.setId(json.getInt("id"));
                        foodItem.setTime(txttime.getText().toString());
                        foodItemList.add(foodItem);
                        foodItemAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    clear();
                }
                else {
                    Log.e("Ok",""+response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }
}

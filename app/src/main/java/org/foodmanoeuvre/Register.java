package org.foodmanoeuvre;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Register extends AppCompatActivity {

    private static final int REQUEST_WRITE_PERMISSION = 2;
    final static int REQUEST_LOCATION = 199;

    double longitude=0.0,latitude=0.0;
    String type;
    EditText txtname,txtmno,txtemail,txtoname,txtomno,txtweburl,txtpass,txtadd;
    ImageView profile;
    Button register;

    Bitmap newProfilePic=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle(null);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView t1=findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle b=getIntent().getExtras();
        initcontroll();
        requestPermission();
        if(b.getString("type","res").equals("res"))
        {
            type="res";
            t1.setText("FoodArea Registration");
        }
        else
        {
            txtomno.setVisibility(View.GONE);
            txtoname.setVisibility(View.GONE);
            type="ngo";
            t1.setText("ngo Registration");
        }
        int perwrite= ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(perwrite==PackageManager.PERMISSION_GRANTED)
        {
            onGps();
        }
        profile.setOnClickListener(new View.OnClickListener() {
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
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation())
                {
                    singnup();
                }
            }
        });
    }

    public void  initcontroll()
    {
        txtname=findViewById(R.id.txtname);
        txtmno=findViewById(R.id.txtmno);
        txtemail=findViewById(R.id.txtemail);
        txtoname=findViewById(R.id.txtoname);
        txtomno=findViewById(R.id.txtomno);
        txtweburl=findViewById(R.id.txturl);
        txtpass=findViewById(R.id.txtpass);
        txtadd=findViewById(R.id.txtadd);
        profile=findViewById(R.id.profile);
        register=findViewById(R.id.btnreg);
    }

    public boolean validation()
    {
        boolean check=true;
        if(txtname.getText().toString().trim().equals(""))
        {
            txtname.setError("Please Enter Name");
            check=false;
        }
        if(txtmno.getText().toString().trim().equals(""))
        {
            txtmno.setError("Please Enter Mobile Number");
            check=false;
        }
        if(txtemail.getText().toString().trim().equals(""))
        {
            txtemail.setError("Please Enter Email");
            check=false;
        }

        if(txtpass.getText().toString().trim().equals(""))
        {
            txtpass.setError("Please Enter Password");
            check=false;
        }
        if(profile==null)
        {
            Toast.makeText(this, "Please Select Image", Toast.LENGTH_SHORT).show();
            check=false;
        }
        if(latitude==0.0 || longitude==0.0)
        {
            onGps();
            Toast.makeText(this, "GPS NOT Working ", Toast.LENGTH_SHORT).show();
        }
        if(type.equals("res"))
        {
            if(txtomno.getText().toString().trim().equals(""))
            {
                txtoname.setError("Please Enter Owner Mobile Number");
                check=false;
            }
            if(txtoname.getText().toString().trim().equals(""))
            {
                txtoname.setError("Please Enter Owner Mobile Number");
                check=false;
            }
        }
        return check;
    }

    private void singnup() {
        Map<String, RequestBody> map=new HashMap<>();
        map.put("name", RequestBody.create(MediaType.parse("multipart/form-data"),txtname.getText().toString()));
        map.put("type",RequestBody.create(MediaType.parse("multipart/form-data"),type));
        map.put("latt",RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(latitude)));
        map.put("lang",RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(longitude)));
        map.put("mno",RequestBody.create(MediaType.parse("multipart/form-data"),txtmno.getText().toString()));
        map.put("email",RequestBody.create(MediaType.parse("multipart/form-data"),txtemail.getText().toString()));
        map.put("address",RequestBody.create(MediaType.parse("multipart/form-data"),txtadd.getText().toString()));
        map.put("url",RequestBody.create(MediaType.parse("multipart/form-data"),txtweburl.getText().toString()));
        map.put("oname",RequestBody.create(MediaType.parse("multipart/form-data"),txtoname.getText().toString()));
        map.put("omno",RequestBody.create(MediaType.parse("multipart/form-data"),txtomno.getText().toString()));
        map.put("pass",RequestBody.create(MediaType.parse("multipart/form-data"),txtpass.getText().toString()));
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File file = wrapper.getDir("Images",MODE_PRIVATE);
        file = new File(file, UUID.randomUUID().toString()+".jpg");
        try{
            OutputStream stream = null;
            stream = new FileOutputStream(file);
            newProfilePic.compress(Bitmap.CompressFormat.JPEG,50,stream);
            stream.flush();
            stream.close();
        }catch(Exception ee){

        }
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"),file );
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);

        final ProgressDialog progressDialog = new ProgressDialog(Register.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        API api=retrofit.create(API.class);

        Call<String> call=api.Register(body,map);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialog.dismiss();
                if(response.isSuccessful())
                {
                    try {
                        JSONObject json=new JSONObject(response.body());
                        if(json.has("status"))
                        {
                                Intent i=new Intent(getApplicationContext(),Login.class);
                                startActivity(i);
                        }
                        if(json.has("email"))
                        {
                            Toast.makeText(Register.this, ""+json.getString("email"), Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(Register.this, ""+response.toString(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Log.e("Error",e.getMessage());
                    }
                }
                else {
                    Log.e("Ok",""+response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("Error",""+t.getMessage());
            }
        });
    }

    public void onGps()
    {
        final LocationManager manager = (LocationManager) Register.this.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
            builder.setMessage("Please Enable GPS")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent,100);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else
        {
            int perwrite= ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if(perwrite==PackageManager.PERMISSION_GRANTED) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, new Listener());
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, new Listener());
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location == null){
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }
        }
    }

    private void requestPermission() {
        int perfine= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(perfine!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE_PERMISSION);
        }
    }

    private void permissionLocation()
    {
        int perwrite= ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(perwrite!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_PERMISSION ) {
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED)
            {
                requestPermission();
            }
            else
            {
                permissionLocation();
            }
        }
        else if(requestCode ==REQUEST_LOCATION)
        {
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED)
            {
                permissionLocation();
            }
            else
            {
                onGps();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            switch (requestCode) {
                case 1:
                    onGps();
                    break;
            }
        }
        else if(requestCode == 200)
        {

            try {
                final Bundle extras = data.getExtras();
                if (extras != null) {
                    newProfilePic = extras.getParcelable("data");
                    profile.setImageBitmap(newProfilePic);
                }
                Uri u=data.getData();
                newProfilePic= MediaStore.Images.Media.getBitmap(this.getContentResolver(), u);
                profile.setImageBitmap(newProfilePic);
            } catch (Exception e) {
                Log.e("error",""+e.getMessage());
            }
        }
    }

    private class Listener implements LocationListener {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
        public void onProviderDisabled(String provider){}
        public void onProviderEnabled(String provider){}
        public void onStatusChanged(String provider, int status, Bundle extras){}
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
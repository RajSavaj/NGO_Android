package org.foodmanoeuvre;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Login extends AppCompatActivity {

    Button login;
    EditText txtpass,txtemail;
    TextView txtregres,txtregngo;
    SessionManagement session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        login=findViewById(R.id.btnlogin);
        txtemail=findViewById(R.id.txtemail);
        txtpass=findViewById(R.id.txtpass);
        txtregngo=findViewById(R.id.ngoreg);
        txtregres=findViewById(R.id.foodarea);
        session=new SessionManagement(Login.this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation())
                {
                    login();
                }
            }
        });


        txtregres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Register.class);
                i.putExtra("type","res");
                startActivity(i);
            }
        });

        txtregngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Register.class);
                i.putExtra("type","ngo");
                startActivity(i);
            }
        });
        txtregres.setText(Html.fromHtml("<u>FoodArea Registration</u>"));
        txtregngo.setText(Html.fromHtml("<u>Ngo Registration</u>"));


    }

    public boolean validation()
    {
        boolean check=true;
        if(txtemail.getText().toString().trim().equals(""))
        {
            check=false;
            txtemail.setError("Please Enter Email");
        }
        if(txtpass.getText().toString().trim().equals(""))
        {
            check=false;
            txtpass.setError("Please Enter Password");
        }
        return check;
    }

    public void login() {
        final ProgressDialog progressDialog = new ProgressDialog(Login.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = txtemail.getText().toString();
        String password = txtpass.getText().toString();

        HashMap<String,String> map=new HashMap<>( );
        map.put("email",email);
        map.put("pass",password);

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        API api=retrofit.create(API.class);

        Call<String> call=api.Login(map);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialog.cancel();
                if(response.isSuccessful()){
                    try {
                        JSONObject json=new JSONObject(response.body());
                        if(json.has("status"))
                        {
                            Toast.makeText(Login.this, "Invalid Username And Password", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            HashMap<String,String> map=new HashMap<>();
                            map.put("uid",String.valueOf(json.getInt("uid")));
                            map.put("Name",json.getString("Name"));
                            map.put("type",json.getString("type"));
                            map.put("latt",json.getString("latt"));
                            map.put("lang",json.getString("lang"));
                            map.put("mno",json.getString("mno"));
                            map.put("email",json.getString("email"));
                            map.put("pass",json.getString("pass"));
                            map.put("oname",json.getString("oname"));
                            map.put("omno",json.getString("omno"));
                            map.put("address",json.getString("address"));
                            map.put("image",json.getString("image"));
                            map.put("url",json.getString("url"));
                            session.createLoginSession(map);
                            if(map.get("type").equals("res"))
                            {
                                Intent i=new Intent(getApplicationContext(), FoodArea.class);
                                startActivity(i);
                                finish();
                            }
                            else
                            {
                                Intent i=new Intent(getApplicationContext(), Ngo.class);
                                startActivity(i);
                                finish();
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.cancel();
                Toast.makeText(Login.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

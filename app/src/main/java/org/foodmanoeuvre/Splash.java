package org.foodmanoeuvre;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.HashMap;

public class Splash extends AppCompatActivity {

    SessionManagement  session;
    HashMap<String,String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        session=new SessionManagement(Splash.this);
        map=session.getUserDetails();
        ImageView imageView=findViewById(R.id.img);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (session.isLoggedIn()) {
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
                } else {
                    Intent intent=new Intent(getApplicationContext(),Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 5000);
    }
}

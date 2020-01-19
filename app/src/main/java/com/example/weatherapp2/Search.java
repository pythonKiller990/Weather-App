package com.example.weatherapp2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Search extends AppCompatActivity {
    FloatingActionButton search;
    EditText city;
    private String cityvalue;
    private SimpleGestureFilter detector;
    private Animation animation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        setContentView(R.layout.activity_search);

        city=findViewById(R.id.city);
        animation= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.left);
        search=findViewById(R.id.search);
        search.startAnimation(animation);
        setTitle("Search City");



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityvalue=city.getText().toString();
                if (cityvalue.length() == 0)
                {
                    Toast.makeText(getApplicationContext(),"City Required",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //send this cityvalue to another activity using intent
                    Intent i = new Intent(getApplicationContext(),city_temp.class);
                    i.putExtra("city",cityvalue);
                    startActivity(i);
                }


            }
        });


    }


}

package com.example.weatherapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;

public class MainActivity extends AppCompatActivity {
    TextView temprature,desc;
    ImageView imageView;
    private FloatingActionButton fab;
    private static final int PERMISSION_REQUEST_CODE = 200;
    boolean doubleBackToExitPressedOnce = false;
    ProgressBar progressBar;
    ConstraintLayout main;

    public static final String SHARED_PREF="shared pref";
    public static final String TEXT="text";
    public static final String SWITCH="switch";

    private String text;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        temprature=findViewById(R.id.temprature);
        fab=findViewById(R.id.floatingActionButton);
        progressBar=findViewById(R.id.progress);
        setTitle("WeatherApp");
        main=findViewById(R.id.main);
        desc=findViewById(R.id.desc);
        imageView=findViewById(R.id.img);





        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Search.class);
                startActivity(intent);
            }
        });
        checkConnection();
        loaddata();
        updateData();
    }


    public void loaddata()
    {
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
        text=sharedPreferences.getString(TEXT,"Bangalore");
    }

    public void updateData()
    {
        getWeather(text);
    }



    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }


    private void checkConnection(){
        if(!isOnline()){
            temprature.setText("Internet Connection Not Available!");
            temprature.setTextSize(12);
            fab.hide();

        }
        else
        {
            fab.show();
            getWeather(text);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.bar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent i = new Intent(getApplicationContext(),Settings.class);
                startActivity(i);
                return true;
            case R.id.about:
                checkConnection();
                loaddata();
                updateData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press Back Again to Exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }










    public void getWeather(final String city) {
        final String cityname=city;
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+cityname+"&APPID=16020d8d307b65395a579b8cdb9b33dd&units=imperial";
        JsonObjectRequest jsonreq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response!=null)
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    setTitle(cityname);
                    JSONObject mainobj = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    String tempString = String.valueOf(mainobj.getDouble("temp"));
                    String description=String.valueOf(object.getString("description"));
                    String iconimage=String.valueOf(object.getString("icon"));

                    double tempint = Double.parseDouble(tempString);
                    double celsius = (tempint - 32) / 1.8000;
                    celsius = Math.round(celsius);
                    int i = (int) celsius;
                    String json = null;



                    temprature.setText(String.valueOf(i) + (char) 0x00B0 + "C");
                    temprature.setTextSize(85);
                    desc.setText(description);
                    JSONObject jo = new JSONObject();
                    String jsonStr = "{\"01d\": \"one\",\"01n\": \"one\",\"02d\": \"two\", \"02n\": \"two\",\"03d\": \"three\",\"03n\": \"three\",\"04d\": \"four\",\"04n\": \"four\",\"04d\": \"fourth\",\"10n\": \"fifth\",\"10n\": \"fifth\",\"10d\": \"fifth\",\"11d\": \"sixth\",\"13n\": \"seventh\",\"50d\": \"eight\",\"50n\": \"nine\"}";
                    // create parser object
                    JsonParser parser = new JsonParser();
                    // get json element object
                    JsonElement jsonElement = parser.parse(jsonStr);
                    // get json object
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    Resources res = getResources();
                    int resID = res.getIdentifier(jsonObject.get(iconimage).getAsString() , "drawable", getPackageName());
                   Drawable drawable = res.getDrawable(resID );
                   imageView.setImageDrawable(drawable );





                } catch (JSONException e) {
                    e.printStackTrace();
                    temprature.setText("There is an Error!1");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //temprature.setText("Internet Problem!");
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonreq);
    }
}

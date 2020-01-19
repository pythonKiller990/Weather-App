package com.example.weatherapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class city_temp extends AppCompatActivity {
    TextView city_temp,desc;
    ProgressBar progressBar;
    ImageView icon;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        setContentView(R.layout.activity_city_temp);
        city_temp=findViewById(R.id.city_temp);
        progressBar=findViewById(R.id.pb2);
        desc=findViewById(R.id.desc2);
        icon=findViewById(R.id.imv);
        Intent i = getIntent();
        setTitle(i.getStringExtra("city"));

        getWeather();

    }

    public void getWeather() {
        Intent intent = getIntent();
        String city= intent.getStringExtra("city");
        String url="http://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID=16020d8d307b65395a579b8cdb9b33dd&units=imperial";
        JsonObjectRequest jsonreq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject mainobj=response.getJSONObject("main");
                    JSONArray array= response.getJSONArray("weather");
                    JSONObject object=array.getJSONObject(0);
                    String tempString=String.valueOf(mainobj.getDouble("temp"));
                    String description=String.valueOf(object.getString("description"));
                    String iconimage=String.valueOf(object.getString("icon"));

                    progressBar.setVisibility(View.INVISIBLE);


                    double tempint=Double.parseDouble(tempString);
                    double celsisus=(tempint-32)/1.8000;
                    celsisus=Math.round(celsisus);
                    int i =(int)celsisus;

                    city_temp.setText(String.valueOf(i)+(char) 0x00B0+"C");

                    desc.setText(description);




                    JSONObject jo = new JSONObject();
                    String jsonStr = "{\"01d\": \"one\",\"01n\": \"one\", \"02n\": \"two\",\"03d\": \"three\",\"04d\": \"four\",\"04n\": \"fourth\",\"10n\": \"fifth\",\"11d\": \"sixth\",\"13n\": \"seventh\",\"50d\": \"eight\",\"50n\": \"nine\"}";
                    // create parser object
                    JsonParser parser = new JsonParser();
                    // get json element object
                    JsonElement jsonElement = parser.parse(jsonStr);
                    // get json object
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    Resources res = getResources();
                    int resID = res.getIdentifier(jsonObject.get(iconimage).getAsString() , "drawable", getPackageName());
                    Drawable drawable = res.getDrawable(resID );
                    icon.setImageDrawable(drawable );


                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    Toast toast = Toast.makeText(getApplicationContext(), "There is an error!", Toast.LENGTH_SHORT);
                    progressBar.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String code=String.valueOf(networkResponse.statusCode);
                Log.e("Status code", String.valueOf(networkResponse.statusCode));
                String status_code=String.valueOf(networkResponse.statusCode);
                switch(status_code)
                {
                    case "404":
                        city_temp.setText("city not found!");
                        city_temp.setTextSize(12);
                        setTitle("city not found!");
                     break;

                     default:
                         city_temp.setText("Internal Api Error");
                         city_temp.setTextSize(12);
                         setTitle("Internal Api Error");


                }




            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonreq);
}
}

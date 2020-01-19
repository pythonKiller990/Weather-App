package com.example.weatherapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class Settings extends AppCompatActivity {

    FloatingActionButton save;
    public static final String SHARED_PREF="shared pref";
    public static final String TEXT="text";
    EditText city;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        save=findViewById(R.id.save);
        setTitle("Default City");
        city=findViewById(R.id.defaultcity);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(TEXT,city.getText().toString());
                editor.apply();
                Toast.makeText(getApplicationContext(),"Setting Saved Restart the Application",Toast.LENGTH_SHORT).show();
                finish();

            }
        });
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
        city.setText(text);
    }
}

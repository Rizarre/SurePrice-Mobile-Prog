package com.guevarra.finalprojectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        // Action bar
        ActionBar ab = getSupportActionBar();
        ab.setTitle("");
        ab.setDisplayHomeAsUpEnabled(true);

        TextView tvDevs = (TextView)findViewById(R.id.tvDevs);
        tvDevs.setText("Developers:\n" +
                "\tGuevarra, Alejandro Kyle\n" +
                "\tMallare, Kendrick Oliver\n" +
                "\tReyes, Sean Rizarre\n\n\n" +
                "This programs aims to aid users in efficiently\n" +
                "choosing any item they desire on buying in\n" +
                "different stores. Upon comparing the products\n" +
                "and their prices on different stores, the user\n" +
                "can view the information about their prices\n" +
                "and availability in each store.");
    }
    // Back button for Action Bar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
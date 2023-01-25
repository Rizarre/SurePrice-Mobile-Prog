package com.guevarra.finalprojectapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Apply Color Preferences
        setContentView(R.layout.activity_main);

        // Action bar
        ActionBar ab = getSupportActionBar();
        ab.setTitle("");
    }

    public void btnCompare(View view)
    {
        // Goes to CompareActivity that alters the currentlist that can be added to favorites
        Intent intent = new Intent(MainActivity.this, CompareActivity.class);
        intent.putExtra("TableName", "currentlist");
        startActivity(intent);
    }

    public void btnFavorites(View view)
    {
        // Goes to CompareActivity that alters the currentlist that can be added to favorites
        Intent intent = new Intent(MainActivity.this, Favorites.class);
        startActivity(intent);
    }

    public void btnAboutUs(View view)
    {
        // Goes to CompareActivity that alters the currentlist that can be added to favorites
        Intent intent = new Intent(MainActivity.this, AboutUs.class);
        startActivity(intent);
    }
}
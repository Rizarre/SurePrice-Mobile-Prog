package com.guevarra.finalprojectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Favorites extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        CreateTable();

        // Action bar
        ActionBar ab = getSupportActionBar();
        ab.setTitle("");
        ab.setDisplayHomeAsUpEnabled(true);
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

    public void CreateTable()
    {
        // Creates the favorite list dynamically
        DBHelper db = new DBHelper(this);
        Cursor data = db.SelectTable("favorite");
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        while(data.moveToNext())
        {
            Button fav = new Button(this);
            fav.setText(data.getString(0));
            fav.setGravity(Gravity.CENTER);
            fav.setTextSize(20);
            TableRow.LayoutParams rowparams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            rowparams.setMargins(0,20, 0,0);
            fav.setLayoutParams(rowparams);
            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button b = (Button) view;
                    Intent intent = new Intent(Favorites.this, FavoriteList.class);
                    intent.putExtra("TableName", b.getText().toString());
                    startActivity(intent);
                }
            });
            linearLayout.addView(fav);
        }
        db.close();
    }
}
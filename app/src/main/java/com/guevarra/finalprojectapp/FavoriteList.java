package com.guevarra.finalprojectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

public class FavoriteList extends AppCompatActivity {
    String table;
    float Total1 = 0, Total2 = 0, Total3 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        // Action bar
        ActionBar ab = getSupportActionBar();
        ab.setTitle("");
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        table = intent.getStringExtra("TableName");
        TextView lblTableName = findViewById(R.id.lblTableName);
        lblTableName.setText(table);

        // Create Table
        CreateTable();
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

    public void CreateTable() {
        // Creates the entire Table dynamically
        TableLayout tblFavList = findViewById(R.id.tblFavList);
        DBHelper db = new DBHelper(this);
        Cursor data = db.SelectTable(table);
        while (data.moveToNext()) {
            TableRow row = new TableRow(this);
            row.setGravity(Gravity.CENTER);
            row.setBackgroundColor(Color.rgb(253, 253, 102));
            TableRow.LayoutParams rowparams = new TableRow.LayoutParams();
            rowparams.setMargins(5, 5, 5, 5);
            row.setLayoutParams(rowparams);
            for (int i = 0; i < 6; i++) {
                TextView c = new TextView(this);
                c.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                c.setLayoutParams(new TableRow.LayoutParams(-2, -2, 1f));
                String value = data.getString(i);
                c.setText(value.substring(0, Math.min(value.length(), 10)));
                row.addView(c);
            }
            tblFavList.addView(row);
            Total1 += Float.valueOf(data.getString(1)).floatValue();
            Total2 += Float.valueOf(data.getString(3)).floatValue();
            Total3 += Float.valueOf(data.getString(5)).floatValue();
        }
        TableRow finalrow = new TableRow(this);
        finalrow.setBackgroundColor(Color.rgb(253, 253, 102));
        finalrow.setGravity(Gravity.CENTER);
        TableRow.LayoutParams rowparams = new TableRow.LayoutParams();
        rowparams.setMargins(5, 5, 5, 5);
        finalrow.setLayoutParams(rowparams);
        for (int i = 0; i < 6; i++) {
            TextView c = new TextView(this);
            c.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            c.setLayoutParams(new TableRow.LayoutParams(-2, -2, 1f));
            String text;
            switch (i) {
                case 0:
                case 2:
                case 4:
                    text = "Total = ";
                    break;
                case 1:
                    text = String.valueOf(Total1);
                    break;
                case 3:
                    text = String.valueOf(Total2);
                    break;
                case 5:
                    text = String.valueOf(Total3);
                    break;
                default:
                    text = " ";
                    break;
            }
            c.setText(text);
            finalrow.addView(c);
        }
        tblFavList.addView(finalrow);
        db.close();
    }
    public void btnRemove(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(FavoriteList.this);
        builder.setCancelable(true);
        builder.setTitle("Delete this list?");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                DBHelper db = new DBHelper(FavoriteList.this);
                db.DropFavoriteTable(table);
                db.close();
                Intent intent = new Intent(FavoriteList.this, Favorites.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    public void btnEdit(View view)
    {
        Intent intent = new Intent(FavoriteList.this, CompareActivity.class);
        intent.putExtra("TableName", table);
        startActivity(intent);
    }
}
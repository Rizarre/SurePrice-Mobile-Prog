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
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ViewList extends AppCompatActivity {
    Boolean TableIsEmpty = false;
    String table;
    float Total1 = 0, Total2 = 0, Total3 = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);
        // Gets the table to display
        Intent intent = getIntent();
        table = intent.getStringExtra("TableName");
        // Calls method to create table
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
                if(TableIsEmpty == true)
                {
                    Intent intent = new Intent(ViewList.this, CompareActivity.class);
                    intent.putExtra("TableName", table);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    this.finish();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void CreateTable()
    {
        // Creates the entire Table dynamically
        TableLayout tblData = findViewById(R.id.tblData);
        DBHelper db = new DBHelper(this);
        Cursor data = db.SelectTable(table);
        if(data.getCount() == 0)
        {
            TableIsEmpty = true;
        }
        while(data.moveToNext())
        {
            TableRow row = new TableRow(this);
            row.setGravity(Gravity.CENTER);
            row.setBackgroundColor(Color.rgb(253,253,102));
            TableRow.LayoutParams rowparams = new TableRow.LayoutParams();
            rowparams.setMargins(5,5,5,5);
            row.setLayoutParams(rowparams);
            for(int i = 0; i < 6; i++)
            {
                TextView c = new TextView(this);
                c.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                c.setLayoutParams(new TableRow.LayoutParams(-2, -2, 1f));
                String value = data.getString(i);
                c.setText(value.substring(0, Math.min(value.length(), 10)));
                row.addView(c);
            }
            tblData.addView(row);
            Total1 += Float.valueOf(data.getString(1)).floatValue();
            Total2 += Float.valueOf(data.getString(3)).floatValue();
            Total3 += Float.valueOf(data.getString(5)).floatValue();
        }
        TableRow finalrow = new TableRow(this);
        finalrow.setBackgroundColor(Color.rgb(253,253,102));
        finalrow.setGravity(Gravity.CENTER);
        TableRow.LayoutParams rowparams = new TableRow.LayoutParams();
        rowparams.setMargins(5,5,5,5);
        finalrow.setLayoutParams(rowparams);
        for(int i = 0; i < 6; i++)
        {
            TextView c = new TextView(this);
            c.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            c.setLayoutParams(new TableRow.LayoutParams(-2, -2, 1f));
            String text;
            switch (i)
            {
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
        tblData.addView(finalrow);
        db.close();
    }
    // No code to add to Favorites
    public void btnAdd(View view)
    {
        DBHelper db = new DBHelper(this);
        EditText edtFaveName = (EditText)findViewById(R.id.edtFaveName);
        if(edtFaveName.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Must enter a name for the list", Toast.LENGTH_SHORT).show();
            return;
        }
        Boolean create = db.AddtoFavorite(edtFaveName.getText().toString(), table);
        if(create == false)
        {
            Toast.makeText(this, "Name of table is already in use", Toast.LENGTH_SHORT).show();
            return;
        }
        db.close();
        Intent intent = new Intent(ViewList.this, Favorites.class);
        finish();
        startActivity(intent);
    }
    public void btnFavorite(View view)
    {
        if(TableIsEmpty == true)
        {
            Toast.makeText(this, "Table is empty, must add products first", Toast.LENGTH_SHORT).show();
            return;
        }
        EditText edtFaveName = (EditText)findViewById(R.id.edtFaveName);
        Button btnAdd = (Button)findViewById(R.id.btnAdd);
        edtFaveName.setVisibility(View.VISIBLE);
        btnAdd.setVisibility(View.VISIBLE);
    }

    public void btnEdit(View view)
    {
        if(TableIsEmpty == true)
        {
            Toast.makeText(this, "Table is empty, must add products first", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(ViewList.this, EditPage.class);
        intent.putExtra("TableName", table);
        startActivity(intent);
    }
}
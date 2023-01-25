package com.guevarra.finalprojectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Set;

public class EditPage extends AppCompatActivity {
    String table;
    int currentrow = 0;
    Cursor datatable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page);

        // Gets the table to display
        Intent intent = getIntent();
        table = intent.getStringExtra("TableName");

        // Action bar
        ActionBar ab = getSupportActionBar();
        ab.setTitle("");
        ab.setDisplayHomeAsUpEnabled(true);

        // Generate Table
        DBHelper db = new DBHelper(this);
        datatable = db.SelectTable(table);
        SetItems();
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

    public void RemoveItems(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditPage.this);
        builder.setCancelable(true);
        builder.setTitle("Remove from List?");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                DBHelper db = new DBHelper(EditPage.this);
                TextView lblPureGoldName = findViewById(R.id.lblPureGoldName);
                TextView lblPureGoldPrice = findViewById(R.id.lblPureGoldPrice);
                ImageView imgStore1 = findViewById(R.id.imgStore1);
                TextView lblSMName = findViewById(R.id.lblSMName);
                TextView lblSMPrice = findViewById(R.id.lblSMPrice);
                ImageView imgStore2 = findViewById(R.id.imgStore2);
                TextView txtCustomName = findViewById(R.id.txtCustomName);
                TextView txtCustomPrice = findViewById(R.id.txtCustomPrice);

                String a = lblPureGoldName.getText().toString();
                String b = lblPureGoldPrice.getText().toString();
                String c = lblSMName.getText().toString();
                String d = lblSMPrice.getText().toString();
                String e = txtCustomName.getText().toString();
                String f= txtCustomPrice.getText().toString();
                String g = imgStore1.getContentDescription().toString();
                String h = imgStore2.getContentDescription().toString();

                db.DropRowFromTable(table, a, b, c, d, e, f, g, h);
                SetItems();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
    public void NextItem(View view)
    {
        SetItems();
    }
    public void SetItems()
    {
        if(currentrow == datatable.getCount())
        {
            DBHelper db = new DBHelper(this);
            datatable = db.SelectTable(table);
            Toast.makeText(this, "You've reached the end of the list", Toast.LENGTH_SHORT).show();
            currentrow = 0;
        }
        if(datatable.getCount() == 0 || datatable == null)
        {
            Toast.makeText(this, "The list is now empty", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditPage.this, ViewList.class);
            intent.putExtra("TableName", table);
            startActivity(intent);
            finish();
            return;
        }
        TextView lblPureGoldName = findViewById(R.id.lblPureGoldName);
        TextView lblPureGoldPrice = findViewById(R.id.lblPureGoldPrice);
        ImageView imgStore1 = findViewById(R.id.imgStore1);
        TextView lblSMName = findViewById(R.id.lblSMName);
        TextView lblSMPrice = findViewById(R.id.lblSMPrice);
        ImageView imgStore2 = findViewById(R.id.imgStore2);
        TextView txtCustomName = findViewById(R.id.txtCustomName);
        TextView txtCustomPrice = findViewById(R.id.txtCustomPrice);

        datatable.moveToNext();
        lblPureGoldName.setText(datatable.getString(0));
        lblPureGoldPrice.setText(datatable.getString(1));
        Glide.with(this).load(datatable.getString(6)).into(imgStore1);
        imgStore1.setContentDescription(datatable.getString(6));
        lblSMName.setText(datatable.getString(2));
        lblSMPrice.setText(datatable.getString(3));
        Glide.with(this).load(datatable.getString(7)).into(imgStore2);
        imgStore2.setContentDescription(datatable.getString(7));
        txtCustomName.setText(datatable.getString(4));
        txtCustomPrice.setText(datatable.getString(5));

        currentrow++;
    }
}
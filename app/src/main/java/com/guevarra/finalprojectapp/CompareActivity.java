package com.guevarra.finalprojectapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.io.InputStream;
import java.net.URL;

public class CompareActivity extends AppCompatActivity {
    String[][] Store1Items = new String[5][2];
    String[][] Store2Items = new String[5][2];
    String CustomProduct = "N/A", CustomPrice = "0", table;
    int Store1Count = 0, Store2Count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        // Action bar
        ActionBar ab = getSupportActionBar();
        ab.setTitle("");
        ab.setDisplayHomeAsUpEnabled(true);

        // Grabs Intent and gets the Table Name
        // Allows reusability for adding data
        Intent intent = getIntent();
        table = intent.getStringExtra("TableName");
        // Start Python Code
        if(!Python.isStarted()){
            Python.start(new AndroidPlatform(CompareActivity.this));
        }
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

    // Goes to the Puregold and SM websites and scrapes data depending on what the user inputted
    // Has a delay
    public void SearchProducts(View view)
    {
        EditText txtInput = findViewById(R.id.txtInput);
        if(txtInput.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Must enter a keyword to search", Toast.LENGTH_SHORT).show();
            return;
        }
        Button btnStore1 = findViewById(R.id.btnStore1);
        Button btnStore2 = findViewById(R.id.btnStore2);
        Python py = Python.getInstance();
        final PyObject pyobj1 = py.getModule("puregold");
        final PyObject pyobj2 = py.getModule("sm");
        Store1Items = pyobj1.callAttr("main", txtInput.getText().toString()).toJava(String[][].class);
        Store2Items = pyobj2.callAttr("main", txtInput.getText().toString()).toJava(String[][].class);
        // Calls the two Next Buttons and enables them once a search is made
        btnStore1.setVisibility(View.VISIBLE);
        btnStore2.setVisibility(View.VISIBLE);
        NextStore1(findViewById(android.R.id.content).getRootView());
        NextStore2(findViewById(android.R.id.content).getRootView());
    }

    // Scrolls through the top 5 products found in the Puregold Store
    public void NextStore1(View view)
    {
        TextView lblPureGoldName = findViewById(R.id.lblPureGoldName);
        TextView lblPureGoldPrice = findViewById(R.id.lblPureGoldPrice);
        ImageView imgStore1 = findViewById(R.id.imgStore1);
        if(Store1Count == 5)
        {
            // At the end, it will set things to default
            lblPureGoldName.setText("N/A");
            lblPureGoldPrice.setText("0");
            Glide.with(this).clear(imgStore1);
            imgStore1.setContentDescription("Nothing");
        }
        else
        {
            lblPureGoldName.setText(Store1Items[Store1Count][0]);
            lblPureGoldPrice.setText(Store1Items[Store1Count][1]);
            Glide.with(this).load(Store1Items[Store1Count][2]).into(imgStore1);
            imgStore1.setContentDescription(Store1Items[Store1Count][2]);
        }
        Store1Count++;
        if(Store1Count == 6)
        {
            // When at the end, it resets the search
            Toast.makeText(this, "Reached the last item that was searched", Toast.LENGTH_SHORT).show();
            Store1Count = 0;
        }
    }

    // Scrolls through the top 5 products found in the SM Store
    public void NextStore2(View view)
    {
        TextView lblSMName = findViewById(R.id.lblSMName);
        TextView lblSMPrice = findViewById(R.id.lblSMPrice);
        ImageView imgStore2 = findViewById(R.id.imgStore2);
        if(Store2Count == 5)
        {
            // At the end, it will set things to default
            lblSMName.setText("N/A");
            lblSMPrice.setText("0");
            Glide.with(this).clear(imgStore2);
            imgStore2.setContentDescription("Nothing");
        }
        else
        {
            lblSMName.setText(Store2Items[Store2Count][0]);
            lblSMPrice.setText(Store2Items[Store2Count][1]);
            Glide.with(this).load(Store2Items[Store2Count][2]).into(imgStore2);
            imgStore2.setContentDescription(Store2Items[Store2Count][2]);
        }
        Store2Count++;
        if(Store2Count == 6)
        {
            // When at the end, it resets the search
            Toast.makeText(this, "Reached the last item that was searched", Toast.LENGTH_SHORT).show();
            Store2Count = 0;
        }
    }
    // Adds the Custom Product and Price
    // Once successful, disables the inputs, if pressed again, will reset the custom product and price to default
    public void CustomAdd(View view)
    {
        Button btnCustom = findViewById(R.id.btnCustom);
        EditText txtCustomName = findViewById(R.id.txtCustomName);
        EditText txtCustomPrice = findViewById(R.id.txtCustomPrice);
        if(btnCustom.getText().toString().matches("ADD"))
        {
            if(txtCustomName.getText().toString().matches("") || txtCustomPrice.getText().toString().matches(""))
            {
                Toast.makeText(this, "You did not enter a Product Name or Price properly", Toast.LENGTH_SHORT).show();
            }
            else
            {
                btnCustom.setText("REMOVE");
                txtCustomName.setInputType(InputType.TYPE_NULL);
                txtCustomPrice.setInputType(InputType.TYPE_NULL);
                CustomProduct =  txtCustomName.getText().toString();
                CustomPrice = txtCustomPrice.getText().toString();
            }
        }
        else
        {
            btnCustom.setText("ADD");
            txtCustomName.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            txtCustomPrice.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
            CustomProduct = "N/A";
            CustomPrice = "0";
        }
    }
    // Inserts current data set up to the table
    // Checks if theres a product thats set to be added, if not then it creates an message
    public void NextButton(View view)
    {
        DBHelper db = new DBHelper(this);
        TextView lblPureGoldName = findViewById(R.id.lblPureGoldName);
        TextView lblPureGoldPrice = findViewById(R.id.lblPureGoldPrice);
        ImageView imgStore1 = findViewById(R.id.imgStore1);
        TextView lblSMName = findViewById(R.id.lblSMName);
        TextView lblSMPrice = findViewById(R.id.lblSMPrice);
        ImageView imgStore2 = findViewById(R.id.imgStore2);
        TextView txtCustomName = findViewById(R.id.txtCustomName);
        TextView txtCustomPrice = findViewById(R.id.txtCustomPrice);
        Button btnStore1 = findViewById(R.id.btnStore1);
        Button btnStore2 = findViewById(R.id.btnStore2);
        Button btnCustom = findViewById(R.id.btnCustom);
        String store1link = imgStore1.getContentDescription().toString();
        String store2link = imgStore2.getContentDescription().toString();
        if(lblPureGoldName.getText().toString().matches("N/A") && lblSMName.getText().toString().matches("N/A") && CustomProduct.matches("N/A"))
        {
            Toast.makeText(this, "Must have a Product to add in the list",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Boolean insertdata = db.InsertList(table, lblPureGoldName.getText().toString(), lblPureGoldPrice.getText().toString(), lblSMName.getText().toString(), lblSMPrice.getText().toString(), CustomProduct, CustomPrice, store1link, store2link);
            if(insertdata == true)
            {
                Toast.makeText(this, "Items are added", Toast.LENGTH_SHORT).show();
                lblPureGoldName.setText("N/A");
                lblPureGoldPrice.setText("0");
                Glide.with(this).load("").into(imgStore1);
                imgStore1.setContentDescription("Nothing");
                Store1Items = new String[5][2];
                lblSMName.setText("N/A");
                lblSMPrice.setText("0");
                Glide.with(this).load("").into(imgStore2);
                imgStore2.setContentDescription("Nothing");
                Store2Items = new String[5][2];
                txtCustomName.setText("");
                txtCustomPrice.setText("");
                CustomAdd(findViewById(android.R.id.content).getRootView());
                CustomProduct = "N/A";
                CustomPrice = "0";
                btnStore1.setVisibility(View.GONE);
                btnStore2.setVisibility(View.GONE);
            }
            else
            {
                Toast.makeText(this, "Items was NOT entered successfully", Toast.LENGTH_SHORT).show();
            }
        }
        db.close();
    }
    // Views the current list made
    public void CheckList(View view)
    {
        // Goes to ViewList Activity with current table being used
        Intent intent = new Intent(CompareActivity.this, ViewList.class);
        intent.putExtra("TableName", table);
        startActivity(intent);
    }
    // Resets the entire current list
    public void ResetList(View view)
    {
        // Creates a pop up that will confirm resetting the current table being used
        AlertDialog.Builder builder = new AlertDialog.Builder(CompareActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Restart List");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                DBHelper db = new DBHelper(CompareActivity.this);
                db.ResetList(table);
                db.close();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
}
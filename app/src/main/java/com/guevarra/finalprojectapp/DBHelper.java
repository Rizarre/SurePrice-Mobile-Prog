package com.guevarra.finalprojectapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "surefire.db ", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creates a Table on start up of app
        // This table is mandatory
        db.execSQL("CREATE TABLE 'currentlist'(Store1ProductName TEXT, Store1ProductPrice TEXT, Store2ProductName TEXT, Store2ProductPrice TEXT, CustomProductName TEXT, CustomProductPrice TEXT, Store1Link TEXT, Store2Link TEXT)");
        // Add here a table if you want to add a static table for entire app duration
        // Note: it makes a permanent table, not temporary or something that can be added again and dropped

        //Table for saving products into favorite
        db.execSQL("CREATE TABLE 'favorite'(TableName TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Updates a Table when theres update required I think, unsure if required to
        db.execSQL("DROP TABLE IF EXISTS 'currentlist'");
        db.execSQL("DROP TABLE IF EXISTS 'favorite'");
    }

    public boolean InsertList(String table,String S1PN, String S1PP, String S2PN, String S2PP, String CSN, String CSP, String S1L, String S2L)
    {
        // Insert data into the Comparing list that is specified in the parameters
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Store1ProductName", S1PN);
        cv.put("Store1ProductPrice", S1PP.replaceAll("[₱]",""));
        cv.put("Store2ProductName", S2PN);
        cv.put("Store2ProductPrice", S2PP.replaceAll("[₱]",""));
        cv.put("CustomProductName", CSN);
        cv.put("CustomProductPrice", CSP.replaceAll("[₱]",""));
        cv.put("Store1Link", S1L);
        cv.put("Store2Link", S2L);
        long result = db.insert(table, null, cv);
        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    // Clears the list that is called
    public void ResetList(String table)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM '" + table + "'");
    }
    // Grabs data for Comparing Tables and returns a CURSOR Object
    public Cursor SelectTable(String table)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM '" + table + "'", null);
        return data;
    }

    // Adds the list into the new table
    public boolean AddtoFavorite(String NewTableName, String OriginalTableName)
    {
        // If false is returned, then the table name is used, if returned true, then its successful
        SQLiteDatabase db = this.getWritableDatabase();
        // Check if table name is used
        Cursor tablenames = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        while(tablenames.moveToNext())
        {
            if(NewTableName.matches(tablenames.getString(0)))
            {
                return false;
            }
        }
        // Insert the table name into favorite table
        db.execSQL("INSERT INTO 'favorite' VALUES ('" + NewTableName + "')");
        // Creates a table using the input given
        db.execSQL("CREATE TABLE '" + NewTableName + "'(Store1ProductName TEXT, Store1ProductPrice TEXT, Store2ProductName TEXT, Store2ProductPrice TEXT, CustomProductName TEXT, CustomProductPrice TEXT, Store1Link TEXT, Store2Link TEXT)");
        // Copies all data from currentlist into the new table
        db.execSQL("INSERT INTO '" + NewTableName + "' SELECT * FROM '" + OriginalTableName + "'");
        // Deletes all current record for currentlist
        // Check if currentlist or not, if not then it drops table as well
        if(OriginalTableName.matches("currentlist"))
        {
            db.execSQL("DELETE FROM '" + OriginalTableName + "'");
        }
        else
        {
            DropFavoriteTable(OriginalTableName);
        }
        return true;
    }
    // Remove
    public void DropFavoriteTable(String Table)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM 'favorite' WHERE TableName = '" + Table + "'");
        db.execSQL("DROP TABLE '" + Table + "'");
    }

    public void DropRowFromTable(String table, String item1, String item1price, String item2, String item2price, String custom, String customprice, String item1link, String item2link)
    {
        // Store1ProductName, Store1ProductPrice, Store2ProductName, Store2ProductPrice,
        // CustomProductName, CustomProductPrice, Store1Link, Store2Link
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "DELETE FROM '" + table + "' WHERE " +
                "Store1ProductName = '" + item1 + "' AND " +
                "Store1ProductPrice = '" + item1price + "' AND " +
                "Store2ProductName = '" + item2 + "' AND " +
                "Store2ProductPrice = '" + item2price + "' AND " +
                "CustomProductName = '" + custom + "' AND " +
                "CustomProductPrice = '" + customprice + "' AND " +
                "Store1Link = '" + item1link + "' AND " +
                "Store2Link = '" + item2link + "'";
        db.execSQL(sql);
    }
}

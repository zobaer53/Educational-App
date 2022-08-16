package com.educational_app.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.educational_app.model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteAssetHelper {
    private static  final String DB_NAME="mydb.db";

    private static final int DB_VER=1;
    public DataBase(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @SuppressLint("Range")
    public List<Order> getCarts(){
        final List<Order> result;
        SQLiteDatabase db = getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ProductId", "ImageUrl", "ProductName", "InstructorName","Quantity", "Price"};
        String sqlTable = "OrderDetail";

        queryBuilder.setTables(sqlTable);
        Cursor cursor = queryBuilder.query(db, sqlSelect, null, null, null, null, null,null);
        result = new ArrayList<>();

        if (cursor.moveToFirst()) {

            do {
                result.add(new Order(cursor.getString(cursor.getColumnIndex("ProductId")),
                        cursor.getString(cursor.getColumnIndex("ImageUrl")),
                        cursor.getString(cursor.getColumnIndex("ProductName")),
                        cursor.getString(cursor.getColumnIndex("InstructorName")),
                        cursor.getString(cursor.getColumnIndex("Quantity")),
                        cursor.getString(cursor.getColumnIndex("Price"))
                ));
            } while (cursor.moveToNext());


        }


        return result;
    }

    public void addToCart(Order order){
        try {
               SQLiteDatabase db = getReadableDatabase();
            String query = String.format("INSERT INTO OrderDetail (ProductId,ImageUrl,ProductName,InstructorName,Quantity,Price) VALUES('%s','%s','%s','%s','%s','%s');",
                    order.getProductId(),
                    order.getImageUrl(),
                    order.getProductName(),
                    order.getInstructorName(),
                    order.getQuantity(),
                    order.getPrice());

            db.execSQL(query);


        }catch (Exception e){

            e.printStackTrace();
        }

    }

    public void clearCart(){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);
    }

}

package com.tejab.museumapp;


import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class InfoActivity extends AppCompatActivity {
    Cursor c=null;
    DatabaseHelper myDbHelper;
    TextView title,info;
    ImageView image;
    Button b1,b2,b3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        String word= getIntent().getStringExtra("key");
        image=(ImageView)findViewById(R.id.image);
        title=(TextView)findViewById(R.id.title);
        info=(TextView)findViewById(R.id.info);
        myDbHelper=new DatabaseHelper(InfoActivity.this);
        try{
            myDbHelper.createDataBase();
        }catch (IOException ioe){
            throw new Error("Unable to create database");
        }
        try
        {
            myDbHelper.openDataBase();
        } catch(SQLException sqle)
        {
            throw sqle;
        }
        c=myDbHelper.getInfo(word);
        if(c.moveToFirst()){
            byte[] img= c.getBlob(2);
            Bitmap bitmap= BitmapFactory.decodeByteArray(img,0,img.length);
            title.setText(word);
            image.setImageBitmap(bitmap);
            info.setText(c.getString(3));
        }

        b1=(Button)findViewById(R.id.button3);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word=title.getText().toString();
                c=myDbHelper.nextEntry(word);
                if(c.moveToFirst()) {
                   Toast.makeText(InfoActivity.this,""+c.getString(1),Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(InfoActivity.this,InfoActivity.class);
                    i.putExtra("key", c.getString(1));
                    finish();
                    startActivity(i);
                }
            }
        });
        b2=(Button)findViewById(R.id.button4);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word=title.getText().toString();
                c=myDbHelper.preEntry(word);
                if(c.moveToFirst()) {
                    Toast.makeText(InfoActivity.this,""+c.getString(1),Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(InfoActivity.this,InfoActivity.class);
                    i.putExtra("key", c.getString(1));
                    finish();
                    startActivity(i);
                }
            }
        });
        b3=(Button)findViewById(R.id.button5);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent i = new Intent(InfoActivity.this,MainActivity.class);
                    finish();
                    startActivity(i);

            }
        });
        c.close();
    }

}

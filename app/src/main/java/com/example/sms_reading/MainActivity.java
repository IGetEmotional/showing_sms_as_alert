package com.example.sms_reading;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView myTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myTextView = findViewById(R.id.textView);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
    }


    public String Read_SMS(View view) {
            Cursor cursor = getContentResolver().query(Uri.parse("content://sms"), null, null, null, null);
            cursor.moveToFirst();
            return (cursor.getString(12));
            //view.invalidate();
    }
    public void showDialog (View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.myDialogThemePrior_0);
        builder.setTitle("Message")
                .setMessage(Read_SMS(view))
                .setNeutralButton("Подробнее", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Прочитано", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
AlertDialog dialog = builder.create();
dialog.getWindow().setGravity(Gravity.TOP);
dialog.show();
  view.invalidate();
    }
}
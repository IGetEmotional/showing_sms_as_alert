package com.example.sms_reading;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.service.controls.Control;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private TextView myTextView;
    private Button button;
    private int[] s = {R.style.myDialogThemePrior_0, R.style.myDialogThemePrior_1, R.style.myDialogThemePrior_2, R.style.myDialogThemePrior_3};  //contains styles for dialogs depending on priority
    private Thread thread;     //don't know what is it for yet

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // myTextView = findViewById(R.id.textView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView2);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                    if (Read_SMS().indexOf("no ") != -1)
                        showSpecDialog(1);
                    else
                        showSpecDialog(2);
                }

        });
    }

    protected void onResume() {
        super.onResume();
      //   button.performClick();
            thread.run();
         //   SystemClock.sleep(2000);

    }

    public String Read_SMS() {          //returns the last SMS on the phone
        String s;
            Cursor cursor = getContentResolver().query(Uri.parse("content://sms"), null, null, null, null);
            cursor.moveToFirst();
            s = cursor.getString(12);
            cursor.close();
            return (s);
            //view.invalidate();
    }



    public void showSpecDialog (int t){           //shows AlertDialog with chosen style and message
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, s[t]);
        builder.setTitle("Message")
           //     .setCancelable(false)
                .setMessage(Read_SMS())
                .setPositiveButton("Прочитано", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setNeutralButton("Подробнее", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
        .setNegativeButton("Напомнить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               Toast toast =  Toast.makeText(getApplicationContext(), "Напомним через 5 минут", Toast.LENGTH_SHORT);
               toast.show();
            }
        });
        AlertDialog dialog = builder.create();//choice of positioning
        switch(t){
            case 2:
            case 3:
                dialog.getWindow().setGravity(Gravity.TOP);
                break;
            default:
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                break;
        }

    //    dialog.setMessage(Read_SMS());
        dialog.show();

        {
            (dialog.getButton(AlertDialog.BUTTON_POSITIVE)).setTextColor(getResources().getColor(R.color.black));  //default color of ButtonText is not good to see on every background
            (dialog.getButton(AlertDialog.BUTTON_NEUTRAL)).setTextColor(getResources().getColor(R.color.black));
            (dialog.getButton(AlertDialog.BUTTON_NEGATIVE)).setTextColor(getResources().getColor(R.color.black));
        }
        // view.invalidate();
    }
}
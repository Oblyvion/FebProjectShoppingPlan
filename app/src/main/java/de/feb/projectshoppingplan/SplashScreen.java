package de.feb.projectshoppingplan;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //get shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);

        //get splash screen timeOut
        int splashTimeOut = sharedPreferences.getInt("splashTimeOut", 500);

        //start next activity after splash timeOut.
        new Handler().postDelayed(new Runnable() {
            //Das hier wird NACH dem Time_Out ausgeführt
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);

                startActivity(i);

                finish();

            }
        }, splashTimeOut);
    }
}

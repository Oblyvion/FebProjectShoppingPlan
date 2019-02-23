package de.feb.projectshoppingplan;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {

    private int splashTimeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);

        splashTimeOut = sharedPreferences.getInt("splashTimeOut", 500);

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

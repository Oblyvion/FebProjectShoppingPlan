package de.feb.projectshoppingplan;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SplashScreen extends Activity {

    private String TAG = "splashScreenActivity";

    //shows splash screen on startup
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //get shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs" + getString(R.string.list0_key), MODE_PRIVATE);

        //get splash screen timeOut
        int splashTimeOut = sharedPreferences.getInt("splashTimeOut", 1500);

        Log.d(TAG, "onCreate: SPLASH TIME OUT = " + splashTimeOut);
        //start next activity after splash timeOut.
        new Handler().postDelayed(new Runnable() {
            //executes after timeout
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);

                startActivity(intent);

                finish();

            }
        }, splashTimeOut);
    }
}

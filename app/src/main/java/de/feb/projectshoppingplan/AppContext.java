package de.feb.projectshoppingplan;

import android.app.Application;
import android.content.Context;

public class AppContext extends Application {

    private static Context mContext;

    /**
     * Initializes the Context field mContext with the current applications context
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
    }

    /**
     * @return the context of the application
     */
    public static Context getContext() {
        return mContext;
    }
}

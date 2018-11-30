package de.feb.projectshoppingplan;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;

import static de.feb.projectshoppingplan.R.drawable.ic_list_black_24dp;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Category  implements InterfaceListElement {

    String name;
    Drawable drawable;
    Activity activity;

    Category(Activity activity) {
        this.activity = activity;
        this.drawable = activity.getDrawable(R.drawable.ic_list_black_24dp);
    }

    @Override
    public int getListElementType() {
        return InterfaceListElement.typeCat;
    }

    @Override
    public Drawable getDrawable() {
        return this.drawable;
    }

    public void setDrawable(Drawable x) {
        this.drawable = x;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

package de.feb.projectshoppingplan;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Category  implements InterfaceListElement {

    String name;
    Drawable drawable;
    Activity activity;
    boolean itemsvisible = true;

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

    @Override
    public String getCategory() {
        return this.name;
    }

    @Override
    public boolean getVisibility() {
        return this.itemsvisible;
    }

    @Override
    public void setVisibility(boolean bool) {
        this.itemsvisible = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

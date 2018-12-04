package de.feb.projectshoppingplan;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public interface InterfaceListElement {
    int typeCat = 0;
    int typeShopItem = 1;

    int getListElementType();

    Drawable getDrawable();

    void setDrawable(Drawable x);

    String getCategory();

    boolean getVisibility();

    void setVisibility(boolean bool);

    String getName();

    Bitmap getIcon();

}

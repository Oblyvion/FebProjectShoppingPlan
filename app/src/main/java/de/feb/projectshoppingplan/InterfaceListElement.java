package de.feb.projectshoppingplan;

import android.graphics.drawable.Drawable;

public interface InterfaceListElement {
    int typeCat = 0;
    int typeShopItem = 1;

    int getListElementType();

    Drawable getDrawable();

    void setDrawable(Drawable x);

}

package de.feb.projectshoppingplan;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class ShopItem implements InterfaceListElement {

    String name;
    String description;
    Bitmap icon;
    transient Activity activity;

    ShopItem() {
        this.name = "standard";
//      this.description = "25";
    }

    public ShopItem(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public int getListElementType() {
        return InterfaceListElement.typeShopItem;
    }

    @Override
    public Drawable getDrawable() {
        return null;
    }

    @Override
    public void setDrawable(Drawable x) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

//    public void setDescription(String description) {
//        this.description = description;
//    }

    void setIcon() {
        String firstchars = name.substring(0,1);
        LetterIconProvider letterIconProvider = new LetterIconProvider(activity);
        int tilesize = 64;
        this.icon = letterIconProvider.getLetterIcon(name, firstchars, tilesize, tilesize, true);
    }

    void setActivity(Activity temp) {
        this.activity = temp;
    }

    // https://medium.com/@ruut_j/a-recyclerview-with-multiple-item-types-bce7fbd1d30e

}

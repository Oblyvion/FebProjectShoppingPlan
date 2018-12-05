package de.feb.projectshoppingplan;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class ShopItem implements InterfaceListElement {

    String name;
    String description;
    String category;
    Bitmap icon;
    transient Activity activity;
    boolean visible = true;

//    ShopItem() {
//        this.name = "standard";
//        this.category = "standard_cat";
//        this.description = "1";
//    }

    public ShopItem(String name, String description, String category) {
        this.name = name;
        this.description = description;
        this.category = category;
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

    @Override
    public String getCategory() {
        return this.category;
    }

    @Override
    public boolean getVisibility() {
        return this.visible;
    }

    @Override
    public void setVisibility(boolean bool) {
        this.visible = bool;
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

    public Bitmap getIcon() {
        return this.icon;
    }

    void setActivity(Activity temp) {
        this.activity = temp;
    }

    // https://medium.com/@ruut_j/a-recyclerview-with-multiple-item-types-bce7fbd1d30e

}

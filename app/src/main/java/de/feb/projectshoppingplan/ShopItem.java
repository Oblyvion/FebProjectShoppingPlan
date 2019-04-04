package de.feb.projectshoppingplan;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

public class ShopItem implements Parcelable {

//    private final String TAG = "ShopItem.class";

    public final String name;
    Bitmap icon;
    Boolean checked = false;
    private transient Activity activity;
    transient ImageView checkmark;

    /**
     * Contains all functions to build a item view for adding new shop item.
     * @param name String for a new shopItem.
     */
    ShopItem(String name) {
        this.name = name;
    }

    private ShopItem(Parcel in) {
        name = in.readString();
    }

    public static final Creator<ShopItem> CREATOR = new Creator<ShopItem>() {
        @Override
        public ShopItem createFromParcel(Parcel in) {
            return new ShopItem(in);
        }

        @Override
        public ShopItem[] newArray(int size) {
            return new ShopItem[size];
        }
    };

    /**
     * Sets icon of a shopItem with its first letter.
     */
    void setIcon() {
        String firstchars = name.substring(0,1);
        LetterIconProvider letterIconProvider = new LetterIconProvider(activity);
        int tilesize = 64;
        this.icon = letterIconProvider.getLetterIcon(name, firstchars, tilesize, tilesize, true);
    }

    Bitmap getIcon() {
        return this.icon;
    }

    void setActivity(Activity temp) {
        this.activity = temp;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
    }

    int getCheckmarkId() {
        if (this.checked) {

           return this.activity.getResources().getIdentifier("de.feb.projectshoppingplan:drawable/ic_check_box_black_24dp", null, null);
        }
        else return this.activity.getResources().getIdentifier("de.feb.projectshoppingplan:drawable/ic_check_box_outline_blank_black_24dp", null, null);
    }

    void setChecked(boolean check) { this.checked = check; }

//    boolean isEmpty() {
//        Log.d(TAG, "isEmpty: name boolean = " + (this.name != null));
//        return name != null;
//    }

//    public int getIndexPosition() {
//        return
//    }
}

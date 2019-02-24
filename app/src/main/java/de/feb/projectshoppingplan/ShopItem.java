package de.feb.projectshoppingplan;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;

public class ShopItem implements Parcelable {

    public final String name;
    Bitmap icon;
    Boolean checked = false;
    private transient Activity activity;
    transient Drawable checkmark;


    ShopItem(String name) {
        this.name = name;
    }

    protected ShopItem(Parcel in) {
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

    void setCheckmark() {
        if (this.checked) {
            this.checkmark = ContextCompat.getDrawable(AppContext.getContext(), R.drawable.ic_check_box_black_24dp);
        }
        else this.checkmark = ContextCompat.getDrawable(AppContext.getContext(),R.drawable.ic_check_box_outline_blank_black_24dp);
    }

    void setChecked(boolean check) { this.checked = check; }
}

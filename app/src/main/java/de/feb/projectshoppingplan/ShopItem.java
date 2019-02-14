package de.feb.projectshoppingplan;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.widget.ImageView;

public class ShopItem implements Parcelable {

    public final String name;
    String description;
    String category;
    Bitmap icon;
    Boolean checked = false;
    transient Activity activity;
    transient Drawable checkmark;


    public ShopItem(String name) {
        this.name = name;
//        this.description = description;
//        this.category = category;
    }

//    public ShopItem(String name) {
//
//        this.name = name;
//    }

    protected ShopItem(Parcel in) {
        name = in.readString();
//        description = in.readString();
//        category = in.readString();
//        icon = in.readParcelable(Bitmap.class.getClassLoader());
//        visible = in.readByte() != 0;
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

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDescription() {
//        return description;
//    }

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setCheckmark() {
        if (this.checked) {
            this.checkmark = activity.getDrawable(R.drawable.ic_check_box_black_24dp);
        }
        else this.checkmark = activity.getDrawable(R.drawable.leer);
    }

    public void setChecked(boolean check) { this.checked = check; }
}

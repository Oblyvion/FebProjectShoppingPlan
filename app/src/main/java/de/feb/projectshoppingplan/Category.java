package de.feb.projectshoppingplan;

import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup;

import java.util.List;

public class Category extends CheckedExpandableGroup {
    public Category(String title, List items) {

        super(title, items);
    }

    @Override
    public void onChildClicked(int childIndex, boolean checked) {

    }

//    String name;
//    Drawable drawable;
//    Activity activity;
//    boolean itemsvisible;
//    List<ShopItem> listofCat;
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    Category(Activity activity) {
//        this.activity = activity;
//        this.drawable = activity.getDrawable(R.drawable.ic_list_black_24dp);
//        this.itemsvisible = true;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    @Override
//    public void onChildClicked(int childIndex, boolean checked) {
//
//    }

}

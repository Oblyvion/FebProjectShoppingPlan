package de.feb.projectshoppingplan;

import android.widget.Toast;

import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup;

import java.util.ArrayList;
import java.util.List;

public class Category extends CheckedExpandableGroup {
    Category(String title, ArrayList<ShopItem> items) {
        super(title, items);
    }

    @Override
    public void onChildClicked(int childIndex, boolean checked) {
        
    }

    void setItems(ArrayList<ShopItem> list) {
        this.getItems().clear();
        this.getItems().addAll(list);
    }
}

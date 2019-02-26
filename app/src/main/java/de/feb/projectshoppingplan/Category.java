package de.feb.projectshoppingplan;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;

public class Category extends ExpandableGroup {
    /**
     * creates a category with a title and a list of ShopItems
     * @param title - String, title of the category
     * @param items - ArrayList<ShopItem>, ShopItem list of the category
     */
    Category(String title, ArrayList<ShopItem> items) {
        super(title, items);
    }

}

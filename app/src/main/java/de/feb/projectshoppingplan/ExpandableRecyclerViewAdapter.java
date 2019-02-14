package de.feb.projectshoppingplan;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtbot.expandablerecyclerview.ExpandCollapseController;
import com.thoughtbot.expandablerecyclerview.ExpandableListUtils;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableList;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ExpandableRecyclerViewAdapter extends com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter<ViewHolderCat, ViewHolderShopI> {

    ExpandableRecyclerViewAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public ViewHolderCat onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category, parent, false);
        return new ViewHolderCat(v);
    }

    @Override
    public ViewHolderShopI onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopitem, parent, false);
        return new ViewHolderShopI(v);
    }

    @Override
    public void onBindChildViewHolder(ViewHolderShopI holder, int flatPosition, ExpandableGroup group, int childIndex) {
        ShopItem shopItem;
        try {
            shopItem = (ShopItem) group.getItems().get(childIndex);
        } catch (Error error) {
            Log.d("MyActivity", "ERROR: "+error);
            shopItem = ShopItemFromJson(group.getItems().get(childIndex).toString());
        }
        holder.bind(shopItem);
    }

    //aus Json string wird wieder eine Arraylist<ShopItem>
    public ShopItem ShopItemFromJson(String json){
        Gson gson = new Gson();
        Type type = new TypeToken<ShopItem>() {}.getType();
        return gson.fromJson(json, type);
    }

    @Override
    public void onBindGroupViewHolder(ViewHolderCat holder, int flatPosition, ExpandableGroup group) {
        final Category category = (Category) group;
        holder.bind(category);
    }

    public void addNewGroup() {
        ExpandableListUtils.notifyGroupDataChanged(this);
        notifyDataSetChanged();
    }

    public void remove() {
        getGroups().clear();
    }
}

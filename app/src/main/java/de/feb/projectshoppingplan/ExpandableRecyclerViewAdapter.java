package de.feb.projectshoppingplan;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.thoughtbot.expandablerecyclerview.ExpandableListUtils;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableListPosition;

import java.util.List;

public class ExpandableRecyclerViewAdapter extends com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter<ViewHolderCat, ViewHolderShopI> {

    private static final String TAG = "RecyclerViewAdapter";
    private View view;

    ExpandableRecyclerViewAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public ViewHolderCat onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category, parent, false);
        this.view = v;
        return new ViewHolderCat(v);
    }

    @Override
    public ViewHolderShopI onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopitem, parent, false);
        return new ViewHolderShopI(v);
    }

    @Override
    public void onBindGroupViewHolder(ViewHolderCat holder, int flatPosition, ExpandableGroup group) {
        final Category category = (Category) group;
        holder.bind(category);
    }

    @Override
    public void onBindChildViewHolder(final ViewHolderShopI holder, int flatPosition, ExpandableGroup group, int childIndex) {
        ShopItem shopItem = (ShopItem) group.getItems().get(childIndex);
        holder.bind(shopItem);
    }

    public void moveItem(int flatPosFrom, int flatPostTo) {
        Log.d(TAG, "moveItem: HELLOOOOOOOOO");
        ExpandableListUtils.notifyItemMoved(this, flatPosFrom, flatPostTo);
        notifyItemMoved(flatPosFrom, flatPostTo);
    }

    public void swipeItemLeft(int flatPos) {
        Log.d(TAG, "swipeItem: SWIPE NOW!");
        ExpandableListUtils.notifyItemRemoved(this, flatPos);
    }


    @Override
    public boolean onGroupClick(int flatPos) {
        Log.d(TAG, "onGroupClick: GROUP CLICKED!!!");
        if (ExpandableListUtils.notifyGroupNotClickable(this, flatPos)) {
            return super.onGroupClick(flatPos);
        }
        this.notifyItemChanged(flatPos);
        return false;
    }

//    @Override
//    public boolean toggleGroup(int flatPos) {
//        ExpandableListPosition listPos = expandableList.getUnflattenedPosition(flatPos);
//        boolean expanded = expandableList.expandedGroupIndexes[listPos.groupPos];
//        if (expanded) {
//            collapseGroup(listPos);
//        } else {
//            expandGroup(listPos);
//        }
//        return expanded;
//    }

    public void addNewGroup() {

        ExpandableListUtils.notifyGroupDataChanged(this, view);
        notifyDataSetChanged();
    }

    public Category getCategory(int position) {
        return (Category) getGroups().get(position);
    }

    public void remove() {
        getGroups().clear();
    }
}

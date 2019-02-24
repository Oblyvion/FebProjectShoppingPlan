package de.feb.projectshoppingplan;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableListUtils;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

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

    /**
     * Moves the item from position A to position B.
     * @param flatPosFrom int: Flat position of layout.
     * @param flatPostTo int: Flat position of layout.
     */
    void moveItem(int flatPosFrom, int flatPostTo) {
        ExpandableListUtils.notifyItemMoved(this, flatPosFrom, flatPostTo);
        notifyItemMoved(flatPosFrom, flatPostTo);
    }

    /**
     * Swipes one item to the left and right side as well.
     * @param flatPos int: Flat position of layout.
     */
    void swipeItem(int flatPos) {
        Log.d(TAG, "swipeItem: SWIPE NOW!");
        ExpandableListUtils.notifyItemRemoved(this, flatPos, view);
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

    /**
     * Adds new group.
     */
    void addNewGroup() {

        ExpandableListUtils.notifyGroupDataChanged(this, view);
        notifyDataSetChanged();
    }
}

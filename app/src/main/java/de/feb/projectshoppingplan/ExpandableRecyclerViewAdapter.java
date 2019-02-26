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

    /**
     * Builds ExpandableRecyclerViewAdapter
     * @param groups - all categories (expandable groups)
     */
    ExpandableRecyclerViewAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    /**
     * creates the ViewHolderCat
     * @param parent - the viewgroup where the view will be inserted
     * @param viewType - viewType (Category or ShopItem)
     * @return
     */
    @Override
    public ViewHolderCat onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category, parent, false);
        this.view = v;
        return new ViewHolderCat(v);
    }

    /**
     * creates the ViewHolderShopI
     * @param parent - the viewgroup where the view will be inserted
     * @param viewType - viewType (Category or ShopItem)
     * @return
     */
    @Override
    public ViewHolderShopI onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopitem, parent, false);
        return new ViewHolderShopI(v);
    }

    /**
     * binds Category to the View
     * @param holder - which ViewHolder will be used to bind data to
     * @param flatPosition - the flat position (index in the list)
     * @param group - which group will be binded
     */
    @Override
    public void onBindGroupViewHolder(ViewHolderCat holder, int flatPosition, ExpandableGroup group) {
        final Category category = (Category) group;
        holder.bind(category);
    }

    /**
     * binds ShopItem to the View
     * @param holder - which ViewHolder will be used to bind data to
     * @param flatPosition - the raw index in the list at which to bind the child
     * @param group - to call getItems() and get all shop items
     * @param childIndex - the real index of the child in its own ShopItem list of its category
     *
     */
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

    void onSortCategories() {
        ExpandableListUtils.notifyCollapseAllGroups(this);
    }

    /**
     * Adds new group.
     */
    void addNewGroup() {

        ExpandableListUtils.notifyGroupDataChanged(this, view);
        notifyDataSetChanged();
    }
}

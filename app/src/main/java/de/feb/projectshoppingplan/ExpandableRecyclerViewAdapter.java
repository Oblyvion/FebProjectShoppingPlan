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

    public void notifySwapItem() {
        Log.d(TAG, "notifySwapItem: HUHUUU ");

    }

    public void addNewGroup() {
        ExpandableListUtils.notifyGroupDataChanged(this);
        notifyDataSetChanged();
    }

    public void remove() {
        getGroups().clear();
    }
}

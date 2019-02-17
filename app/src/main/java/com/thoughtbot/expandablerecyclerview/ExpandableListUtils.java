package com.thoughtbot.expandablerecyclerview;

import android.util.Log;

import com.thoughtbot.expandablerecyclerview.models.ExpandableListPosition;

import java.util.Collections;

import de.feb.projectshoppingplan.Category;

public class ExpandableListUtils {

    private static final String TAG = "LISTUTILS";

        public static void notifyGroupDataChanged(ExpandableRecyclerViewAdapter adapter) {
            adapter.expandableList.expandedGroupIndexes = new boolean[adapter.getGroups().size()];
            for (int i = 0; i < adapter.getGroups().size(); i++) {
                adapter.expandableList.expandedGroupIndexes[i] = false;
            }
        }

        public static void notifyItemMoved(ExpandableRecyclerViewAdapter adapter, int flatPosFrom, int flatPosTo) {
            ExpandableListPosition itemPositionFrom = adapter.expandableList.getUnflattenedPosition(flatPosFrom);
            ExpandableListPosition itemPositionTo = adapter.expandableList.getUnflattenedPosition(flatPosTo);

            Log.d(TAG, "notifyItemMoved: itemPositionFrom =   " + itemPositionFrom);
            Log.d(TAG, "notifyItemMoved: itemPositionTo =   " + itemPositionTo);

            int groupIndexFrom = itemPositionFrom.groupPos;
            int groupIndexTo = itemPositionTo.groupPos;
            int shopItemIndexFrom = itemPositionFrom.childPos;
            int shopItemIndexTo = itemPositionTo.childPos;
            int typeFrom = itemPositionFrom.type;   //type: shopItem = 1; category = 2
            int typeTo = itemPositionTo.type;

            Log.d(TAG, "notifyItemMoved: groupIndex = " + groupIndexFrom);
            Log.d(TAG, "notifyItemMoved: groupIndex = " + groupIndexTo);
            Log.d(TAG, "notifyItemMoved: shopItemIndexFrom = " + shopItemIndexFrom);
            Log.d(TAG, "notifyItemMoved: shopItemIndexTo = " + shopItemIndexTo);
            Log.d(TAG, "notifyItemMoved: typeFrom = " + typeFrom);
            Log.d(TAG, "notifyItemMoved: typeTo = " + typeTo);

            Category currentCategory = (Category) adapter.getGroups().get(groupIndexFrom);
//            Category currentCategory = (Category) adapter.getGroups().get(groupIndexTo);

            Log.d(TAG, "notifyItemMoved: currentCategory = " + currentCategory);
            if (typeFrom == 1) {
                Log.d(TAG, "notifyItemMoved: get Group Title = " + currentCategory.getTitle());
                Log.d(TAG, "notifyItemMoved: get Group Items = " + currentCategory.getItems());
                Log.d(TAG, "notifyItemMoved: get Group Item MOVED NOW = " + currentCategory.getItems().get(shopItemIndexFrom));
                Collections.swap(currentCategory.getItems(), shopItemIndexFrom, shopItemIndexTo);
                Log.d(TAG, "notifyItemMoved: get Group Items SWAPED! = " + currentCategory.getItems());
            } else {
                Log.d(TAG, "notifyItemMoved: Category MOVE NOW...");
                try {
                    Collections.swap(adapter.getGroups(), groupIndexFrom, groupIndexTo);
                }catch (Exception e) {
                    Log.d(TAG, "notifyItemMoved: ERROR THROWED = " + e.getMessage());
                    throw new Error(e.getMessage());
                }
            }
        }
    }

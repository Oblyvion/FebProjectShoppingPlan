package com.thoughtbot.expandablerecyclerview;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.thoughtbot.expandablerecyclerview.models.ExpandableListPosition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import de.feb.projectshoppingplan.ArrayListUtils;
import de.feb.projectshoppingplan.Category;
import de.feb.projectshoppingplan.ShopItem;
import de.feb.projectshoppingplan.ViewHolderShopI;

public class ExpandableListUtils {

    private static final String TAG = "LISTUTILS";

    /**
     * Adapter notifies that group added.
     *
     * @param adapter ExpandableRecyclerViewAdapter
     * @param view    View
     */
    public static void notifyGroupDataChanged(ExpandableRecyclerViewAdapter adapter, View view) {
        ArrayListUtils arrayListUtils = new ArrayListUtils();
        Log.d(TAG, "Das ist Arraylist vor save: " + (ArrayList<Category>) adapter.getGroups());
        arrayListUtils.saveArrayList((ArrayList<Category>) adapter.getGroups(), "categories_arraylist");
        adapter.expandableList.expandedGroupIndexes = new boolean[adapter.getGroups().size()];
        for (int i = 0; i < adapter.getGroups().size(); i++) {
            adapter.expandableList.expandedGroupIndexes[i] = false;
        }
    }

    /**
     * Adapter notifies that item moved.
     *
     * @param adapter     ExpandableRecyclerViewAdapter
     * @param flatPosFrom int: Flat position of layout.
     * @param flatPosTo   int: Flat position of layout.
     */
    public static void notifyItemMoved(ExpandableRecyclerViewAdapter adapter, int flatPosFrom, int flatPosTo) {
        ExpandableListPosition itemPositionFrom = adapter.expandableList.getUnflattenedPosition(flatPosFrom);
        ExpandableListPosition itemPositionTo = adapter.expandableList.getUnflattenedPosition(flatPosTo);

//        Log.d(TAG, "notifyItemMoved: itemPositionFrom =   " + itemPositionFrom);
//        Log.d(TAG, "notifyItemMoved: itemPositionTo =   " + itemPositionTo);

        int groupIndexFrom = itemPositionFrom.groupPos;
        int groupIndexTo = itemPositionTo.groupPos;
        int shopItemIndexFrom = itemPositionFrom.childPos;
        int shopItemIndexTo = itemPositionTo.childPos;
        int typeFrom = itemPositionFrom.type;   //type: shopItem = 1; category = 2
        int typeTo = itemPositionTo.type;

//        Log.d(TAG, "notifyItemMoved: groupIndexFrom = " + groupIndexFrom);
//        Log.d(TAG, "notifyItemMoved: groupIndexTo = " + groupIndexTo);
//        Log.d(TAG, "notifyItemMoved: shopItemIndexFrom = " + shopItemIndexFrom);
//        Log.d(TAG, "notifyItemMoved: shopItemIndexTo = " + shopItemIndexTo);
//        Log.d(TAG, "notifyItemMoved: typeFrom = " + typeFrom);
//        Log.d(TAG, "notifyItemMoved: typeTo = " + typeTo);

        Category currentGroupFrom = (Category) adapter.getGroups().get(groupIndexFrom);

        //move category
        if (typeFrom == 2 && typeTo == 2) {
//            Log.d(TAG, "notifyItemMoved: CATEGORY MOVED");

            //move category expanded
            if (adapter.isGroupExpanded(flatPosFrom)) {
                Log.d(TAG, "notifyItemMoved: EXPANDED group MOVE");
                Collections.swap(adapter.getGroups(), groupIndexFrom, groupIndexTo);

                //notifiy group moved
                adapter.notifyItemMoved(flatPosFrom, --flatPosTo);

                //notifiy shopItems of group moved
                for (flatPosFrom += 1; flatPosFrom <= ((Category) adapter.getGroups().get(groupIndexFrom)).getItemCount(); flatPosFrom++) {
                    adapter.notifyItemMoved(flatPosFrom, flatPosTo);
                }

                //group should be expanded after drag & drop
                adapter.expandableList.expandedGroupIndexes[groupIndexFrom] = false;
                adapter.expandableList.expandedGroupIndexes[groupIndexTo] = true;

                adapter.notifyDataSetChanged();
                return;
            }

            //move group collapsed
            Log.d(TAG, "notifyItemMoved: COLLAPSED group MOVE");
            Collections.swap(adapter.getGroups(), groupIndexFrom, groupIndexTo);

            //groupIndexTo is expanded
            if (adapter.isGroupExpanded(flatPosTo)) {
//                Log.d(TAG, "notifyItemMoved: groupTo is expanded = " + flatPosTo);
                //expand the group after swap
                adapter.expandableList.expandedGroupIndexes[groupIndexFrom] = true;
                adapter.expandableList.expandedGroupIndexes[groupIndexTo] = false;
            }
            adapter.notifyDataSetChanged();
        } else
            //move shopItem inside current group
            if (typeFrom == 1 && typeTo == 1 && groupIndexFrom == groupIndexTo) {
                Log.d(TAG, "notifyItemMoved: SHOPITEM MOVE");
                Collections.swap(currentGroupFrom.getItems(), shopItemIndexFrom, shopItemIndexTo);
            }
    }

    /**
     * Adapter notifies that item removed.
     *
     * @param adapter ExpandableRecyclerViewAdapter
     * @param flatPos int
     * @param view    View
     */
    public static void notifyItemRemoved(ExpandableRecyclerViewAdapter adapter, int flatPos, View view) {
        ExpandableListPosition itemPosition = adapter.expandableList.getUnflattenedPosition(flatPos);

        int groupPos = itemPosition.groupPos;
        int shopItemPos = itemPosition.childPos;

        int size = ((Category) adapter.getGroups().get(groupPos)).getItems().size();

        //swiped GROUP
        if (shopItemPos == -1 && adapter.isGroupExpanded(flatPos)) {
//            Log.d(TAG, "notifyItemRemoved: group swiped and group is expanded... " + adapter.isGroupExpanded(flatPos));

            //delete all children
            for (int i = size - 1; i >= 0; i--) {
//                Log.d(TAG, "notifyItemRemoved: shopitem removed " + i);
                ((Category) adapter.getGroups().get(groupPos)).getItems().remove(i);
                adapter.notifyItemRemoved(flatPos + i + 1);
            }
            //notify adapter
            adapter.getGroups().remove(groupPos);
            adapter.notifyItemRemoved(flatPos);
        } else

            //delete collapsed group
            if (shopItemPos == -1 && !adapter.isGroupExpanded(flatPos)) {
                Log.d(TAG, "notifyItemRemoved: swiped collapsed group...");
                adapter.getGroups().remove(groupPos);
                adapter.notifyItemRemoved(flatPos);
            } else {
                Log.d(TAG, "notifyItemRemoved: shopItem swiped and shopItem removed!");
                //swiped SHOPITEM
                ViewHolderShopI.delete((ShopItem)((Category) adapter.getGroups().get(groupPos)).getItems().get(shopItemPos));
                ((Category) adapter.getGroups().get(groupPos)).getItems().remove(shopItemPos);
                adapter.notifyItemRemoved(flatPos);

                //group is empty
                if (((Category) adapter.getGroups().get(groupPos)).getItems().size() < 1) {
                    adapter.notifyDataSetChanged();
                }
            }

        ArrayListUtils arrayListUtils = new ArrayListUtils();
        arrayListUtils.saveArrayList((ArrayList<Category>) adapter.getGroups(), "categories_arraylist");
        Log.d(TAG, "notifyItemRemoved: SAVE THIS =" + (ArrayList<Category>) adapter.getGroups());

    }

    /**
     * Adapter notifies that group is not clickable.
     * @param adapter ExpandableRecyclerViewAdapter
     * @param flatPosGroup int
     * @return true: Group has children, thus group is clickable.
     * false: If group content size < 1 => group is empty and not clickable.
     */
    public static boolean notifyGroupNotClickable(ExpandableRecyclerViewAdapter adapter, int flatPosGroup) {
        ExpandableListPosition groupPos = adapter.expandableList.getUnflattenedPosition(flatPosGroup);
        int groupPosition = groupPos.groupPos;
//        Log.d(TAG, "notifyGroupNotClickable: groupPositio = " + groupPosition);

        //if group size < 1, than group is empty
        if (((Category) adapter.getGroups().get(groupPosition)).getItems().size() < 1) {
            Log.d(TAG, "notifyGroupNotClickable: group < 1");
            return false;
        }
        //group has child elements and is clickable
        return true;
    }

    public static void notifyCollapseAllGroups(ExpandableRecyclerViewAdapter adapter) {
        for (int i = 0; i < adapter.getGroups().size(); i++) {
            adapter.expandableList.expandedGroupIndexes[i] = false;
        }
    }
}

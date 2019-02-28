package com.thoughtbot.expandablerecyclerview;

import android.util.Log;
import android.view.View;

import com.thoughtbot.expandablerecyclerview.models.ExpandableListPosition;

import java.util.ArrayList;
import java.util.Collections;

import de.feb.projectshoppingplan.ArrayListUtils;
import de.feb.projectshoppingplan.Category;
import de.feb.projectshoppingplan.ShopItem;
import de.feb.projectshoppingplan.ViewHolderShopI;

public class ExpandableListUtils {

    private static final String TAG = "LISTUTILS";

    private static ArrayListUtils arrayListUtils = new ArrayListUtils();


    /**
     * Adapter notifies that group added.
     *
     * @param adapter ExpandableRecyclerViewAdapter in which the data should be changed.
     * @param view    View in which should the adapter notifies its data.
     */
    public static void notifyGroupDataChanged(ExpandableRecyclerViewAdapter adapter, View view) {
        ArrayListUtils arrayListUtils = new ArrayListUtils();

        //        Log.d(TAG, "Das ist Arraylist vor save: " + (ArrayList<Category>) adapter.getGroups());
        arrayListUtils.saveArrayList((ArrayList<Category>) adapter.getGroups(), "categories_arraylist");

        //add new expandable group and collapse all existing groups
        adapter.expandableList.expandedGroupIndexes = new boolean[adapter.getGroups().size()];
        for (int i = 0; i < adapter.getGroups().size(); i++) {
            adapter.expandableList.expandedGroupIndexes[i] = false;
        }
    }

    /**
     * Adapter notifies that item moved.
     *
     * @param adapter     ExpandableRecyclerViewAdapter in which the data should be changed.
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
            Log.d(TAG, "notifyItemMoved: CATEGORY MOVED");

            //move category expanded
            if (adapter.isGroupExpanded(flatPosFrom)) {
                Log.d(TAG, "notifyItemMoved: EXPANDED group MOVE");
                Collections.swap(adapter.getGroups(), groupIndexFrom, groupIndexTo);

                //notifiy group moved
                adapter.notifyItemMoved(flatPosFrom, flatPosTo);

                //notifiy shopItems of group moved
                for (int fPosF = flatPosFrom + 1;
                     fPosF <= ((Category) adapter.getGroups().get(groupIndexFrom)).getItemCount();
                     fPosF++) {
                    adapter.notifyItemMoved(fPosF, flatPosTo);
                }

                //groupIndexTO is NOT expanded
                if (!adapter.expandableList.expandedGroupIndexes[groupIndexTo]) {
                    //group should be expanded after drag & drop
                    adapter.expandableList.expandedGroupIndexes[groupIndexFrom] = false;
                    adapter.expandableList.expandedGroupIndexes[groupIndexTo] = true;
                }
                //notify adapter
                adapter.notifyDataSetChanged();
            } else {
                Log.d(TAG, "notifyItemMoved: COLLAPSED group MOVE");

                //swap category collapsed
                Collections.swap(adapter.getGroups(), groupIndexFrom, groupIndexTo);

                //groupIndexTO is expanded
                if (adapter.isGroupExpanded(flatPosTo)) {
                    //expand the groups after swap
                    adapter.expandableList.expandedGroupIndexes[groupIndexFrom] = true;
                    adapter.expandableList.expandedGroupIndexes[groupIndexTo] = false;
                    adapter.notifyDataSetChanged();
                }
            }
        } else
            //move shopItem inside current group
            if (typeFrom == 1 && typeTo == 1 && groupIndexFrom == groupIndexTo) {
//                Log.d(TAG, "notifyItemMoved: SHOPITEM MOVE");
                Collections.swap(currentGroupFrom.getItems(), shopItemIndexFrom, shopItemIndexTo);
            } else {
                //shopItem moves into another group
                adapter.notifyDataSetChanged();
            }
    }

    /**
     * Adapter notifies that item removed.
     *
     * @param adapter ExpandableRecyclerViewAdapter in which the data should be changed.
     * @param flatPos int Flat position of layout.
     */
    public static void notifyItemRemoved(ExpandableRecyclerViewAdapter adapter, int flatPos) {
        ExpandableListPosition itemPosition = adapter.expandableList.getUnflattenedPosition(flatPos);

        int groupPos = itemPosition.groupPos;
        int shopItemPos = itemPosition.childPos;

        //number of categories
        int size = ((Category) adapter.getGroups().get(groupPos)).getItems().size();

        //swiped expanded GROUP
        if (shopItemPos == -1) {
            Log.d(TAG, "notifyItemRemoved: group swiped and group is expanded... " + adapter.isGroupExpanded(flatPos));

            if (adapter.isGroupExpanded(flatPos)) {
                //delete all children from last to first
                for (int i = size - 1; i >= 0; i--) {
//                Log.d(TAG, "notifyItemRemoved: shopitem removed " + i);
                    ((Category) adapter.getGroups().get(groupPos)).getItems().remove(i);
                    adapter.notifyItemRemoved(flatPos + i + 1);
                }
                //notify adapter
                adapter.getGroups().remove(groupPos);
                adapter.notifyItemRemoved(flatPos);
            } else {
                //swiped collapsed group
                adapter.getGroups().remove(groupPos);
                adapter.notifyItemRemoved(flatPos);
            }
        } else {
//            Log.d(TAG, "notifyItemRemoved: shopItem swiped and shopItem removed!");
            //delete swiped SHOPITEM
            ((Category) adapter.getGroups().get(groupPos)).getItems().remove(shopItemPos);
            adapter.notifyItemRemoved(flatPos);
        }

        //saves the new arrayList
        arrayListUtils.saveArrayList((ArrayList<Category>) adapter.getGroups(), "categories_arraylist");
        Log.d(TAG, "notifyItemRemoved: SAVE THIS =" + (ArrayList<Category>) adapter.getGroups());
    }

    /**
     * Adapter notifies that group is not clickable.
     *
     * @param adapter      ExpandableRecyclerViewAdapter in which the data should be changed.
     * @param flatPosGroup int Flat position of layout.
     * @return true: Group has children, thus group is clickable.
     * false: If group content size < 1 => group is empty and not clickable.
     */
    public static boolean notifyGroupNotClickable(ExpandableRecyclerViewAdapter adapter,
                                                  int flatPosGroup) {
        ExpandableListPosition groupPos = adapter.expandableList.getUnflattenedPosition(flatPosGroup);
        int groupPosition = groupPos.groupPos;
//        Log.d(TAG, "notifyGroupNotClickable: groupPositio = " + groupPosition);

        //if group size < 1, than group is empty
        return ((Category) adapter.getGroups().get(groupPosition)).getItems().size() >= 1;
    }

    /**
     * Adapter notifies that all groups are collapsed.
     *
     * @param adapter ExpandableRecyclerViewAdapter in which the data should be changed.
     */
    public static void notifyCollapseAllGroups(ExpandableRecyclerViewAdapter adapter) {
        for (int i = 0; i < adapter.getGroups().size(); i++) {
            adapter.expandableList.expandedGroupIndexes[i] = false;
        }
    }
}

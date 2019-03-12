package com.thoughtbot.expandablerecyclerview;

import android.util.Log;
import android.widget.Toast;

import com.thoughtbot.expandablerecyclerview.models.ExpandableListPosition;

import java.util.ArrayList;
import java.util.Collections;

import de.feb.projectshoppingplan.AppContext;
import de.feb.projectshoppingplan.ArrayListUtils;
import de.feb.projectshoppingplan.Category;

public class ExpandableListUtils {

    private static final String TAG = "LISTUTILS";

    private static ArrayListUtils arrayListUtils = new ArrayListUtils();


    /**
     * Adapter notifies that group added.
     *
     * @param adapter ExpandableRecyclerViewAdapter in which the data should be changed.
     */
    @SuppressWarnings("unchecked")
    public static void notifyGroupDataChanged(ExpandableRecyclerViewAdapter adapter) {
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

        Log.d(TAG, "notifyItemMoved: itemPositionFrom =   " + itemPositionFrom);
        Log.d(TAG, "notifyItemMoved: itemPositionTo =   " + itemPositionTo);

        int groupIndexFrom = itemPositionFrom.groupPos;
        int groupIndexTo = itemPositionTo.groupPos;
        int shopItemIndexFrom = itemPositionFrom.childPos;
        int shopItemIndexTo = itemPositionTo.childPos;
        int typeFrom = itemPositionFrom.type;   //type: shopItem = 1; category = 2
        int typeTo = itemPositionTo.type;

        Log.d(TAG, "notifyItemMoved: groupIndexFrom = " + groupIndexFrom);
        Log.d(TAG, "notifyItemMoved: groupIndexTo = " + groupIndexTo);
        Log.d(TAG, "notifyItemMoved: shopItemIndexFrom = " + shopItemIndexFrom);
        Log.d(TAG, "notifyItemMoved: shopItemIndexTo = " + shopItemIndexTo);
        Log.d(TAG, "notifyItemMoved: typeFrom = " + typeFrom);
        Log.d(TAG, "notifyItemMoved: typeTo = " + typeTo);

        Category currentGroupFrom = (Category) adapter.getGroups().get(groupIndexFrom);

        //move category
        if (typeFrom == 2 && typeTo == 2) {
            Log.d(TAG, "notifyItemMoved: CATEGORY MOVED");

            //move category expanded
            if (adapter.isGroupExpanded(flatPosFrom)) {
                Log.d(TAG, "notifyItemMoved: EXPANDED group MOVE");

                Log.d(TAG, "notifyItemMoved: flatPosFrom = " + flatPosFrom);
                Log.d(TAG, "notifyItemMoved: flatPosTo = " + flatPosTo);
                Log.d(TAG, "notifyItemMoved: groupIndexFrom = " + groupIndexFrom);
                Log.d(TAG, "notifyItemMoved: groupIndexTo = " + groupIndexTo);

                Collections.swap(adapter.getGroups(), groupIndexFrom, groupIndexTo);

                //TODO SWAP EXPANDED GROUPS WITHOUT SETDATACHANGED
                //TODO move collapse group and expand group again

                //notify shopItems of group moved
//                for (int shopItemFlatPositionFrom = flatPosFrom + 1;
//                     shopItemFlatPositionFrom < groupIndexTo + ((Category) adapter.getGroups().get(groupIndexFrom)).getItemCount();
//                     shopItemFlatPositionFrom++) {
//                    Log.d(TAG, "notifyItemMoved: flatposFROM = " + flatPosFrom);
//                    Log.d(TAG, "notifyItemMoved: shopItemFlatPositionFrom = " + shopItemFlatPositionFrom);
//                    Log.d(TAG, "notifyItemMoved: flatPosTO BEFORE INCREMENT = " + shopItemFlatPositionTo);
//                    adapter.notifyItemMoved(shopItemFlatPositionFrom, shopItemFlatPositionTo++);
//                    Log.d(TAG, "notifyItemMoved: flatPosTO AFTER INCREMENT = " + shopItemFlatPositionTo);
//                }
                Log.d(TAG, "notifyItemMoved: FLATPOSTO AFTER FOR_LOOP = " + flatPosTo);

//                adapter.notifyDataSetChanged();
                //MOVE CATEGORY DOWN
                if (flatPosFrom < flatPosTo) {
                    Log.d(TAG, "notifyItemMoved: DOWN DOWN DOWN");
                    Log.d(TAG, "notifyItemMoved: flat pos from = " + flatPosFrom);
                    Log.d(TAG, "notifyItemMoved: flat pos to = " + flatPosTo);

                    Log.d(TAG, "notifyItemMoved: ITEMCOUNT GROUP FROM = " + ((Category) adapter.getGroups().get(groupIndexTo)).getItemCount());
                    adapter.notifyItemMoved(flatPosFrom, flatPosTo);
                }
                //MOVE CATEGORY UP
                else {
                    Log.d(TAG, "notifyItemMoved: UP UP UP");
                    Log.d(TAG, "notifyItemMoved: from = " + flatPosFrom);
                    Log.d(TAG, "notifyItemMoved: to = " + flatPosTo);
                    adapter.notifyItemMoved(flatPosFrom, flatPosTo);

                }

                //groupIndexTO is NOT expanded
                if (!adapter.expandableList.expandedGroupIndexes[groupIndexTo]) {
                    //group should be expanded after drag & drop
                    adapter.expandableList.expandedGroupIndexes[groupIndexFrom] = false;
                    adapter.expandableList.expandedGroupIndexes[groupIndexTo] = true;
                    adapter.notifyDataSetChanged();

                    Log.d(TAG, "notifyItemMoved: ITEMCOUNT = " + ((Category) adapter.getGroups().get(groupIndexTo)).getItemCount());
                } else
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
                adapter.notifyItemMoved(flatPosFrom, flatPosTo);
            }
        } else {
            //move shopItem inside current group
            if (typeFrom == 1 && typeTo == 1 && groupIndexFrom == groupIndexTo) {
//                Log.d(TAG, "notifyItemMoved: SHOPITEM MOVE");
                Collections.swap(currentGroupFrom.getItems(), shopItemIndexFrom, shopItemIndexTo);
            } else {
                //prevent that shopItem moves into another group
                adapter.notifyDataSetChanged();
                Toast.makeText(AppContext.getContext(), "Do not move groceries into other categories!", Toast.LENGTH_LONG).show();
            }
            adapter.notifyItemMoved(flatPosFrom, flatPosTo);
        }
        Log.d(TAG, "notifyItemMoved: SAVE THIS =" + adapter.getGroups());
        for (int i = 0; i < adapter.getGroups().size(); i++) {
            Log.d(TAG, "notifyItemMoved: EXPANDABLE GROUP IS EXPANDED @ POSITION " + i + " = " + adapter.expandableList.expandedGroupIndexes[i]);
        }
    }

    /**
     * Adapter notifies that item removed.
     *
     * @param adapter ExpandableRecyclerViewAdapter in which the data should be changed.
     * @param flatPos int Flat position of layout.
     */
    @SuppressWarnings("unchecked")
    public static void notifyItemRemoved(ExpandableRecyclerViewAdapter adapter, int flatPos) {
        Log.d(TAG, "notifyItemRemoved: BEFORE SWIPE ACTIONS SAVE THIS =" + (ArrayList<Category>) adapter.getGroups());

        ExpandableListPosition itemPosition = adapter.expandableList.getUnflattenedPosition(flatPos);

        int groupPos = itemPosition.groupPos;
        int shopItemPos = itemPosition.childPos;

        //number of categories
        int size = ((Category) adapter.getGroups().get(groupPos)).getItems().size();

        //swiped expanded GROUP
        if (shopItemPos == -1) {
            Log.d(TAG, "notifyItemRemoved: group swiped and group is expanded??? " + adapter.isGroupExpanded(flatPos));

            if (adapter.isGroupExpanded(flatPos)) {
                //delete all children from last to first
                for (int i = size - 1; i >= 0; i--) {
                    Log.d(TAG, "notifyItemRemoved: shopItem removed " + i);
                    ((Category) adapter.getGroups().get(groupPos)).getItems().remove(i);
                    adapter.notifyItemRemoved(flatPos + i + 1);
                }
                //notify adapter
                adapter.getGroups().remove(groupPos);
            } else {
                //swiped collapsed group
                Log.d(TAG, "notifyItemRemoved: GROUP IS COOOOLLLLAAAPPPSED!!!!!!");
                adapter.getGroups().remove(groupPos);
            }
        } else {
            Log.d(TAG, "notifyItemRemoved: shopItem swiped and shopItem removed!");
            //delete swiped SHOPITEM
            ((Category) adapter.getGroups().get(groupPos)).getItems().remove(shopItemPos);

            //if last item removed, do not show expandable group arrow
            if (((Category) adapter.getGroups().get(groupPos)).getItems().size() < 1) {
                adapter.expandableList.expandedGroupIndexes[groupPos] = false;
                adapter.notifyItemChanged(--flatPos);
            }
        }

        //saves the new arrayList
//        arrayListUtils.saveArrayList((ArrayList<Category>) adapter.getGroups(), "categories_arraylist");
        Log.d(TAG, "notifyItemRemoved: AFTER SWIPE ACTIONS SAVE THIS =" + (ArrayList<Category>) adapter.getGroups());
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
//        if (((Category) adapter.getGroups().get(groupPosition)).getItems().size() < 1) {
//            adapter.expandableList.expandedGroupIndexes[groupPosition] = false;
//            adapter.notifyItemChanged(flatPosGroup);
//            return true;
//        }
        return ((Category) adapter.getGroups().get(groupPosition)).getItems().size() < 1;
//        return false;
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

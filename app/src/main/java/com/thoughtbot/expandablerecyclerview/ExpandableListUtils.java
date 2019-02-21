package com.thoughtbot.expandablerecyclerview;

import android.util.Log;

import com.thoughtbot.expandablerecyclerview.models.ExpandableListPosition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.feb.projectshoppingplan.AddShopItemToCategory;
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

        Log.d(TAG, "notifyItemMoved: groupIndexFrom = " + groupIndexFrom);
        Log.d(TAG, "notifyItemMoved: groupIndexTo = " + groupIndexTo);
        Log.d(TAG, "notifyItemMoved: shopItemIndexFrom = " + shopItemIndexFrom);
        Log.d(TAG, "notifyItemMoved: shopItemIndexTo = " + shopItemIndexTo);
        Log.d(TAG, "notifyItemMoved: typeFrom = " + typeFrom);
        Log.d(TAG, "notifyItemMoved: typeTo = " + typeTo);

        Category currentGroupFrom = (Category) adapter.getGroups().get(groupIndexFrom);
        Category currentCategoryTo = (Category) adapter.getGroups().get(groupIndexTo);

        Log.d(TAG, "notifyItemMoved: currentGroupFrom = " + currentGroupFrom);
//       if(typeFrom != typeTo) {
//           Log.d(TAG, "notifyItemMoved: MACH am besten NIX!");
//       } else
        //move category collapsed
        if (typeFrom == 2 && typeTo == 2) {
            Log.d(TAG, "notifyItemMoved: CATEGORY MOVED");
            //move category expanded
            if (adapter.isGroupExpanded(flatPosFrom)) {
                Log.d(TAG, "notifyItemMoved: EXPANDED group MOVE");
                Collections.swap(adapter.getGroups(), groupIndexFrom, groupIndexTo);

                //
                Log.d(TAG, "notifyItemMoved: expandedGroupIndexes = " + Arrays.toString(adapter.expandableList.expandedGroupIndexes));
                Log.d(TAG, "notifyItemMoved: expandedGroupIndexes length = " + adapter.expandableList.expandedGroupIndexes.length);


                adapter.notifyItemMoved(flatPosFrom, --flatPosTo);
                for (flatPosFrom += 1; flatPosFrom <= ((Category) adapter.getGroups().get(groupIndexFrom)).getItemCount(); flatPosFrom++) {
                    Log.d(TAG, "notifyItemMoved: SWAP all shopItems after group");
                    Log.d(TAG, "notifyItemMoved: groupViewFrom = " + flatPosFrom);
                    Log.d(TAG, "notifyItemMoved: groupViewTo = " + flatPosFrom);
                    // baue liste neu auf
                    adapter.notifyItemMoved(flatPosFrom, flatPosTo);
                }
                adapter.expandableList.expandedGroupIndexes[groupIndexFrom] = false;
                adapter.expandableList.expandedGroupIndexes[groupIndexTo] = true;
                Log.d(TAG, "notifyItemMoved: expandedGroupIndexes = " + Arrays.toString(adapter.expandableList.expandedGroupIndexes));

                adapter.notifyDataSetChanged();
                return;
            }
            Log.d(TAG, "notifyItemMoved: groupIndexFrom = " + groupIndexFrom);
            Log.d(TAG, "notifyItemMoved: groupIndexTo = " + groupIndexTo);
            Collections.swap(adapter.getGroups(), groupIndexFrom, groupIndexTo);
            adapter.notifyDataSetChanged(); //
        } else
            //move shopItem inside current group
            Log.d(TAG, "notifyItemMoved: groupIndexFrom = " + groupIndexFrom);
        Log.d(TAG, "notifyItemMoved: groupIndexTo = " + groupIndexTo);
        if (typeFrom == 1 && typeTo == 1
                && groupIndexFrom == groupIndexTo) {
            Log.d(TAG, "notifyItemMoved: SHOPITEM MOVE");
//            Log.d(TAG, "notifyItemMoved: typeFrom = " + typeFrom);
//            Log.d(TAG, "notifyItemMoved: typeTo = " + typeTo);
//
//            Log.d(TAG, "notifyItemMoved: get Group Title = " + currentGroupFrom.getTitle());
//            Log.d(TAG, "notifyItemMoved: get Group Items = " + currentGroupFrom.getItems());
//            Log.d(TAG, "notifyItemMoved: get Group Item MOVED NOW = " + currentGroupFrom.getItems().get(shopItemIndexFrom));
            Collections.swap(currentGroupFrom.getItems(), shopItemIndexFrom, shopItemIndexTo);
//            Log.d(TAG, "notifyItemMoved: get Group Items SWAPED! = " + currentGroupFrom.getItems());
        } else if (typeFrom == 1 && typeTo == 1) {
            Log.d(TAG, "notifyItemMoved: jlfda");
            //try to add shopItem to another group
//            List items = ((Category) adapter.getGroups().get(groupIndexTo)).getItems();
//            items.add(, adapter.getGroups().get(0));
//            ((Category) adapter.getGroups().get(groupIndexFrom)).getItems().remove(shopItemIndexFrom);
//            adapter.notifyItemRemoved(flatPosFrom);
//            adapter.notifyItemInserted(flatPosTo);

            //dont swap shopitems into other other groups
            adapter.notifyItemMoved(flatPosTo, flatPosFrom);
            adapter.notifyDataSetChanged();
        }
//        else
//            //if group is EXPANDED, collapse and move it
//            if (typeFrom == 2 && typeTo == 2 && adapter.isGroupExpanded(groupIndexFrom)) {
//                Log.d(TAG, "notifyItemMoved: CATEGORY MOVE NOW...");
//                Log.d(TAG, "notifyItemMoved: if EXPANDED = " + adapter.isGroupExpanded(groupIndexFrom));
//                Log.d(TAG, "notifyItemMoved: if EXPANDED POSITION = " + groupIndexFrom);
//                Log.d(TAG, "notifyItemMoved: group getItemCount = " + ((Category) adapter.getGroups().get(groupIndexFrom)).getItemCount());
//
//                //collapse group
////                adapter.onGroupCollapsed(groupIndexFrom, ((Category) adapter.getGroups().get(groupIndexFrom)).getItemCount());
//                //swap groups
//                Collections.swap(adapter.getGroups(), groupIndexFrom, groupIndexTo);
////                adapter.notifyItemMoved(groupIndexFrom, groupIndexTo); //reloads complete recyclerView
//                adapter.onGroupExpanded(groupIndexFrom, ((Category) adapter.getGroups().get(groupIndexFrom)).getItemCount());
//                adapter.notifyDataSetChanged();
//            }
//            //if group is COLLAPSED and category draged, then swap categories
//            else if (typeFrom == 2 && typeTo == 2 && !adapter.isGroupExpanded(groupIndexFrom)) {
//                Log.d(TAG, "notifyItemMoved: CATEGORY COLLAPSED MOVE NOW...");
//                try {
////                adapter.onGroupCollapsed(groupIndexFrom, ((Category) adapter.getGroups().get(groupIndexFrom)).getItems().size());
//                    Collections.swap(adapter.getGroups(), groupIndexFrom, groupIndexTo);
//                } catch (Exception e) {
//                    Log.d(TAG, "notifyItemMoved: ERROR THROWED = " + e.getMessage());
//                    throw new Error(e.getMessage());
//                }
//            }

    }

    public static boolean  notifyGroupNotClickable(ExpandableRecyclerViewAdapter adapter, int flatPosGroup) {
        Log.d(TAG, "notifyGroupNotClickable: JOJJOJOJOJ");
        ExpandableListPosition groupPos = adapter.expandableList.getUnflattenedPosition(flatPosGroup);
        int groupPosition = groupPos.groupPos;
        Log.d(TAG, "notifyGroupNotClickable: groupPositio = " + groupPosition);
        if (((Category) adapter.getGroups().get(groupPosition)).getItems().size() < 1) {
            Log.d(TAG, "notifyGroupNotClickable: group < 1");
            return false;
        }
        return true;
    }

}

package de.feb.projectshoppingplan;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ListElementAdapter extends RecyclerView.Adapter<ViewHolder>
        implements ItemTouchHelperAdapter {
    private final static String TAG = "MyActivity";

    private final Context context;
    private final List<InterfaceListElement> shoppingList;
    private CategoryElementOnClick listener;

    ListElementAdapter(Context context, List<InterfaceListElement> shoppingList, CategoryElementOnClick listener) {
        this.context = context;
        this.shoppingList = shoppingList;
        this.listener = listener;
    }

    public int getItemViewType(int position) {
        return shoppingList.get(position).getListElementType();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int type) {
        View view;
        switch (type) {
            case InterfaceListElement.typeCat:
                view = LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.category, viewGroup, false);
                final ViewHolderCat viewHolderCat = new ViewHolderCat(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onItemClick(view, viewHolderCat.getPosition());
                    }
                });
                viewHolderCat.imageViewCatGrap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onImageViewCatClick(view, viewHolderCat.getPosition());
                    }
                });
                return viewHolderCat;
            case InterfaceListElement.typeShopItem:
                view = LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.shopitem, viewGroup, false);
                return new ViewHolderShopI(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        InterfaceListElement item = shoppingList.get(position);
        viewHolder.bindType(item);
        Log.d(TAG, "onBindViewHolder called!");
    }

    @Override
    public int getItemCount() {
        return shoppingList.size();
    }

    //TODO funktioniert nicht ganz!
    //TODO auch die einzelnen shopItems sollen mit der category hier mitgemoved werden
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        int typeFrom = shoppingList.get(fromPosition).getListElementType();
        int typeTo = shoppingList.get(toPosition).getListElementType();
        String catFrom = shoppingList.get(fromPosition).getCategory();
        String catTo = shoppingList.get(toPosition).getCategory();

        //nur unter dem genau gleichen Type darf getauscht werden
        if (typeFrom == typeTo) {
            //sobald der Type eine Category ist, müssen alle ShopItems mit der gleichen Category
            //mitgemoved werden
            if (typeFrom == InterfaceListElement.typeCat && toPosition > fromPosition) {
                for (int i = fromPosition; i < shoppingList.size(); i++) {
                    if (catFrom.equals(shoppingList.get(i).getCategory())) {
                        Log.d(TAG, "i = fromPosition = " + i + " |  toPosition = " + toPosition);
                        Collections.swap(shoppingList, i, toPosition);
                    }
                }
                // dachte, man könnte die shopItems unterhalb der category mitswapen
//            } else if (typeFrom == InterfaceListElement.typeCat && toPosition < fromPosition) {
////                Collections.swap(shoppingList, fromPosition, toPosition);
//                int j = 1;  //füge alle shopitems der category genau hinter die neue toPosition ein
//                for (int i = 0; i < shoppingList.size(); i++) {
//                    if (catFrom.equals(shoppingList.get(i).getCategory())) {
//                        Log.d(TAG, "i = fromPosition = " + i + " |  toPosition = " + toPosition);
//                        Collections.swap(shoppingList, i, toPosition + j);
//                        j++;
//                    }
//                }
            }

            notifyItemMoved(fromPosition, toPosition);
        }

    }

    @Override
    public void onItemDismiss(int position) {
        shoppingList.remove(position);
        notifyItemRemoved(position);
    }
}

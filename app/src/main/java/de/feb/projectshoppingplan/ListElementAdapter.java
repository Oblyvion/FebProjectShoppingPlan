package de.feb.projectshoppingplan;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ListElementAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final static String TAG = "MyActivity";

    private final Context context;
    private final List<InterfaceListElement> shoppingList;

    public ListElementAdapter(Context context, List<InterfaceListElement> shoppingList) {
        this.context = context;
        this.shoppingList = shoppingList;
    }

    public int getItemViewType(int position) {
        return shoppingList.get(position).getListElementType();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        View view = null;
        switch (type) {
            case InterfaceListElement.typeCat:
                view = LayoutInflater
                        .from(viewGroup.getContext())
                        .inflate(R.layout.category, viewGroup, false);
                return new ViewHolderCat(view);
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
        Log.d(TAG, "onBincViewHolder called!");
    }


    @Override
    public int getItemCount() {
        return shoppingList.size();
    }
}

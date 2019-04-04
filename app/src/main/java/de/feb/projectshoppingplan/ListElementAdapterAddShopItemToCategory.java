package de.feb.projectshoppingplan;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListElementAdapterAddShopItemToCategory extends RecyclerView.Adapter<ListElementAdapterAddShopItemToCategory.MyViewHolder> {

    private List<ShopItem> itemList;

    /**
     * Contains all functions to build a item view for adding new shop item.
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView imageViewIcon, imageViewCheckMark;

        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.textViewProductName);
            imageViewCheckMark = view.findViewById(R.id.imageViewProductCheck);
            imageViewIcon = view.findViewById(R.id.imageViewProduct);
        }
    }

    ListElementAdapterAddShopItemToCategory(List<ShopItem> itemList) {
        this.itemList = itemList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_shop_item_to_category_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ShopItem shopItem = itemList.get(position);
        holder.name.setText(shopItem.name);
        holder.imageViewIcon.setImageBitmap(shopItem.getIcon());
        holder.imageViewCheckMark.setImageResource(shopItem.getCheckmarkId());
//                setImageDrawable(shopItem.checkmark);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}


package de.feb.projectshoppingplan;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

class ViewHolderCat extends ViewHolder {
    private ImageView imageViewCatGrap;
    private TextView textViewCat;
    private ImageView imageViewCatAdd;

    public ViewHolderCat(View view) {
        super(view);

        imageViewCatGrap = view.findViewById(R.id.imageViewShopItem);
        textViewCat = view.findViewById(R.id.textViewShopItem);
        imageViewCatAdd = view.findViewById(R.id.editTextShopItem);
    }

    @Override
    public void bindType(InterfaceListElement item) {
        imageViewCatGrap.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_list_black_24dp));
        textViewCat.setText(((Category)item).getName());
        imageViewCatAdd.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_add_circle_24dp));
    }
}
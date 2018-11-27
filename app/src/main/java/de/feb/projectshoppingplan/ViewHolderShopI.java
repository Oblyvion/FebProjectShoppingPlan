package de.feb.projectshoppingplan;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

class ViewHolderShopI extends ViewHolder {

    private ImageView imageViewShopI;
    private TextView textViewShopI;
    private EditText editTextViewShopI;

    public ViewHolderShopI(View view) {
        super(view);

        imageViewShopI = view.findViewById(R.id.imageViewShopItem);
        textViewShopI = view.findViewById(R.id.textViewShopItem);
        editTextViewShopI = view.findViewById(R.id.editTextShopItem);
    }

    @Override
    public void bindType(InterfaceListElement item) {
        imageViewShopI.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_launcher_background));
        textViewShopI.setText(((ShopItem) item).getName());
        editTextViewShopI.setText(((ShopItem) item).getDescription());
//        itemView.getResources().getDrawable(R.drawable.ic_launcher_background);
//        imageViewShopI.setImageBitmap((ShopItem) item.getIcon());
    }
}

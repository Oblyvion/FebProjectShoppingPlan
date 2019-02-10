package de.feb.projectshoppingplan;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;


class ViewHolderShopI extends ChildViewHolder {

    private static final String TAG = "MyActivity";

    private ImageView imageViewShopI;
    private TextView textViewShopI;
    private Spinner spinnerShopI;
    // Dropdown-Listen Inhalt mit dazugehörigem Adapter
    String[] numberShopI;
    ArrayAdapter<String> adapter;

    ViewHolderShopI(View view) {
        super(view);

        imageViewShopI = view.findViewById(R.id.imageViewShopItem);
        textViewShopI = view.findViewById(R.id.textViewShopItem);
        spinnerShopI = view.findViewById(R.id.spinnerShopItem);
        numberShopI = new String[100];
        for (int i = 0; i < 100; i++) {
            numberShopI[i] = "" + i;
        }
        adapter = new ArrayAdapter<>(view.getContext(), R.layout.spinner_item, numberShopI);
    }

    public void bind(ShopItem shopItem) {
        textViewShopI.setText(shopItem.name);
        imageViewShopI.setImageBitmap(shopItem.icon);
        spinnerShopI.setAdapter(adapter);
    }

//    @Override
//    public void bindType(InterfaceListElement item) {
//        Log.d(TAG, "Hallo hier bindtype");
//        imageViewShopI.setImageBitmap(((ShopItem) item).icon);
//        textViewShopI.setText(((ShopItem) item).getName());
//        spinnerShopI.setAdapter(adapter);
//        if (!((ShopItem) item).visible) {
//            itemView.setVisibility(View.INVISIBLE);
//            itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
//        } else {
//            itemView.setVisibility(View.VISIBLE);
//            itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT));
//        }
//    }

//    @Override
//    public void onItemSelected() {
//        itemView.setBackgroundColor(itemView.getResources().getColor(R.color.colorAccent));
//    }
//
//    @Override
//    public void onItemClear() {
//        itemView.setBackgroundColor(0);
//    }

}

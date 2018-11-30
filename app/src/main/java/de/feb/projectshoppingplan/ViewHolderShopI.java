package de.feb.projectshoppingplan;

import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class ViewHolderShopI extends ViewHolder {

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
        adapter = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_item, numberShopI);
    }

    @Override
    public void bindType(InterfaceListElement item) {
        Log.d(TAG, "Hallo hier bindtype");
        imageViewShopI.setImageBitmap(((ShopItem) item).icon);
        textViewShopI.setText(((ShopItem) item).getName());
        spinnerShopI.setAdapter(adapter);
        if (!((ShopItem) item).visible) {
            itemView.setVisibility(View.GONE);
            itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
    }
}

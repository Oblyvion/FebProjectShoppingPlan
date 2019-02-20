package de.feb.projectshoppingplan;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import java.nio.ByteOrder;


class ViewHolderShopI extends ChildViewHolder {

    //private static final String TAG = "MyActivity";

    private ImageView imageViewShopI;
    private TextView textViewShopI;
    private Spinner spinnerShopI;
    private ArrayAdapter<String> adapter;

    ViewHolderShopI(final View view) {
        super(view);

        imageViewShopI = view.findViewById(R.id.imageViewShopItem);
        textViewShopI = view.findViewById(R.id.textViewShopItem);
        spinnerShopI = view.findViewById(R.id.spinnerShopItem);

        view.setOnClickListener(new View.OnClickListener() {
            boolean clicked = false;

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                if (!clicked) {
                    textViewShopI.setPaintFlags(textViewShopI.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    v.setBackground(view.getContext().getDrawable(R.drawable.border));
                    v.setBackgroundColor(Color.LTGRAY);
                    clicked = true;
                } else {
                    textViewShopI.setPaintFlags(textViewShopI.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    v.setBackgroundColor(Color.WHITE);
                    v.setBackground(view.getContext().getDrawable(R.drawable.border));
                    clicked = false;
                }

            }



        });

        // Dropdown-Listen Inhalt mit dazugehörigem Adapter
        String[] numberShopI = new String[100];
        for (int i = 0; i < 100; i++) {
            numberShopI[i] = "" + i;
        }
        adapter = new ArrayAdapter<>(view.getContext(), R.layout.spinner_item, numberShopI);
    }

    void bind(ShopItem shopItem) {
        textViewShopI.setText(shopItem.name);
        imageViewShopI.setImageBitmap(shopItem.icon);
        spinnerShopI.setAdapter(adapter);
    }
}

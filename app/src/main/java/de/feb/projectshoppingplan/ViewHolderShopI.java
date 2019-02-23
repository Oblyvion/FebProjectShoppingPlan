package de.feb.projectshoppingplan;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import static android.content.Context.MODE_PRIVATE;


class ViewHolderShopI extends ChildViewHolder {

    private ImageView imageViewShopI;
    private TextView textViewShopI;
    private Spinner spinnerShopI;
    private ArrayAdapter<String> adapter;

    /**
     * Creates shopItem viewHolder.
     * @param view View
     */
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

        //TODO REQUIRED ?
        //delete();

        //spinner dropDown list content array
        final String[] numberShopI = new String[100];

        //adds 0 to 99 ints to spinner array
        for (int i = 0; i < 100; i++) {
            numberShopI[i] = "" + i;
        }

        //initialise ArrayAdapter
        adapter = new ArrayAdapter<>(view.getContext(), R.layout.spinner_item, numberShopI);
    }

    /**
     * Builds shopItem itemView.
     * @param shopItem ShopItem
     */
    void bind(ShopItem shopItem) {
        textViewShopI.setText(shopItem.name);
        imageViewShopI.setImageBitmap(shopItem.icon);
        spinnerShopI.setAdapter(adapter);

        //sets spinner value
        if (spinnerShopI.getSelectedItem() == null) {
            spinnerShopI.setSelection(0, true);
        } else getSpinnerValue();
        spinnerShopI.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //saves spinner value
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                saveSpinnerValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}

        });
    }

    /**
     * Saves spinner value.
     */
    private void saveSpinnerValue() {
        String userChoice = spinnerShopI.getSelectedItem().toString();
        Log.d("hallo", "save: "+userChoice);
        SharedPreferences sharedPref = spinnerShopI.getContext().getSharedPreferences(textViewShopI.getText().toString(),MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putString("userChoiceSpinner",userChoice);
        prefEditor.apply();
    }

    /**
     * Gets spinner value.
     */
    private void getSpinnerValue() {
        SharedPreferences sharedPref = spinnerShopI.getContext().getSharedPreferences(textViewShopI.getText().toString(),MODE_PRIVATE);
        String spinnerValue = sharedPref.getString("userChoiceSpinner",null);
        if(spinnerValue != null) {
            // set the selected value of the spinner
            Log.d("hallo", "hallo hier spinnerValue: "+spinnerValue);
            spinnerShopI.setSelection(Integer.parseInt(spinnerValue),true);
        }
    }
}

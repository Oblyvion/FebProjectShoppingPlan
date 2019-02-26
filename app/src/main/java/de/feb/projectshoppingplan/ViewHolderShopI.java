package de.feb.projectshoppingplan;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import java.util.Random;

import static android.content.Context.MODE_PRIVATE;


class ViewHolderShopI extends ChildViewHolder {

    private ImageView imageViewShopI;
    private TextView textViewShopI;
    private Spinner spinnerShopI;
    private ArrayAdapter<String> adapter;
    private View view;
    private boolean clicked = false;
    //private int random;
    /**
     * Creates shopItem viewHolder.
     * @param view View
     */
    ViewHolderShopI(final View view) {
        super(view);
        this.view = view;

        imageViewShopI = view.findViewById(R.id.imageViewShopItem);
        textViewShopI = view.findViewById(R.id.textViewShopItem);
        spinnerShopI = view.findViewById(R.id.spinnerShopItem);

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!clicked) {
                    textViewShopI.setPaintFlags(textViewShopI.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    v.setBackground(ContextCompat.getDrawable(AppContext.getContext(), R.drawable.border));
                    v.setBackgroundColor(Color.LTGRAY);
                    clicked = true;
                    saveClicked("Yes");
                } else {
                    textViewShopI.setPaintFlags(textViewShopI.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    v.setBackgroundColor(Color.WHITE);
                    v.setBackground(ContextCompat.getDrawable(AppContext.getContext(), R.drawable.border));
                    clicked = false;
                    saveClicked("No");
                }
            }
        });

        //TODO REQUIRED? // Löscht eben die SharedPreferences zum Speichern von dem Spinner und ob es durchgestrichen ist oder nicht
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
        String temp = this.getClicked();
        //random = new Random().nextInt(100) + 20;

        if (temp == null) {
            temp = "No";
        }
        if (temp.equals("Yes")) {
            textViewShopI.setPaintFlags(textViewShopI.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            view.setBackground(ContextCompat.getDrawable(AppContext.getContext(), R.drawable.border));
            view.setBackgroundColor(Color.LTGRAY);
            clicked = true;
        } else {
            textViewShopI.setPaintFlags(textViewShopI.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            view.setBackgroundColor(Color.WHITE);
            view.setBackground(ContextCompat.getDrawable(AppContext.getContext(), R.drawable.border));
            clicked = false;
        }



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


    private void saveClicked(String clickedYesNo) {
        SharedPreferences sharedPref = spinnerShopI.getContext().getSharedPreferences(textViewShopI.getText().toString()+"clicked",MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putString("ClickedYesNo", clickedYesNo);
        prefEditor.apply();
    }

    private String getClicked() {
        SharedPreferences sharedPref = spinnerShopI.getContext().getSharedPreferences(textViewShopI.getText().toString()+"clicked",MODE_PRIVATE);
        return sharedPref.getString("ClickedYesNo",null);
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

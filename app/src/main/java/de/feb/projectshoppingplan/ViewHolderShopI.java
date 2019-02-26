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

import static android.content.Context.MODE_PRIVATE;


public class ViewHolderShopI extends ChildViewHolder {

    private ImageView imageViewShopI;
    private TextView textViewShopI;
    private Spinner spinnerShopI;
    private ArrayAdapter<String> adapter;
    private View view;
    private boolean clicked = false;
    /**
     * Creates shopItem viewHolder.
     * @param view View
     */
    ViewHolderShopI(final View view) {
        super(view);

        imageViewShopI = view.findViewById(R.id.imageViewShopItem);
        textViewShopI = view.findViewById(R.id.textViewShopItem);
        spinnerShopI = view.findViewById(R.id.spinnerShopItem);

        this.view = view;

        saveClicked("No");
        saveSpinnerValue(true);

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

        if(temp == null) {
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
                saveSpinnerValue(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}

        });
    }

    public static void delete(ShopItem item) {
        AppContext.getContext().getSharedPreferences(item.name+"clicked", 0).edit().clear().apply();
        AppContext.getContext().getSharedPreferences(item.name+"spinnervalue", 0).edit().clear().apply();
    }

    private void saveClicked(String clickedYesNo) {
        SharedPreferences sharedPref = AppContext.getContext().getSharedPreferences(textViewShopI.getText().toString()+"clicked",MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putString("ClickedYesNo", clickedYesNo);
        prefEditor.apply();
    }

    private String getClicked() {
        SharedPreferences sharedPref = AppContext.getContext().getSharedPreferences(textViewShopI.getText().toString()+"clicked",MODE_PRIVATE);
        return sharedPref.getString("ClickedYesNo",null);
    }


    /**
     * Saves spinner value.
     */
    private void saveSpinnerValue(boolean newitem) {
        String userChoice;
        if(newitem) {
            userChoice = "0";
        }
        else {
            userChoice = spinnerShopI.getSelectedItem().toString();
        }
        Log.d("hallo", "save: "+userChoice);
        SharedPreferences sharedPref = AppContext.getContext().getSharedPreferences(textViewShopI.getText().toString()+"spinnervalue",MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putString("userChoiceSpinner",userChoice);
        prefEditor.apply();
    }

    /**
     * Gets spinner value.
     */
    private void getSpinnerValue() {
        SharedPreferences sharedPref = AppContext.getContext().getSharedPreferences(textViewShopI.getText().toString()+"spinnervalue",MODE_PRIVATE);
        String spinnerValue = sharedPref.getString("userChoiceSpinner",null);
        if(spinnerValue != null) {
            // set the selected value of the spinner
            Log.d("hallo", "hallo hier spinnerValue: "+spinnerValue);
            spinnerShopI.setSelection(Integer.parseInt(spinnerValue),true);
        }
    }
}

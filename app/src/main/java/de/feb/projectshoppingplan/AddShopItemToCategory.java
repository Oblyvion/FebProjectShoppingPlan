package de.feb.projectshoppingplan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddShopItemToCategory extends AppCompatActivity {

    private final static String TAG = "MyActivity";
    private final int REQ_CODE_SPEECH_INPUT = 100;

    String temp_user_input;
    EditText editText;
    RecyclerView recyclerView;
    List<InterfaceListElement> itemList_text;
    List<InterfaceListElement> itemList_voice;
    ListElementAdapterAddShopItemToCategory adapter;
    Activity addShopItemToCategory = this;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop_item_to_category);

        //get the elements from the activity
        editText = findViewById(R.id.editText_newShopItem);
        EditText editor = new EditText(this);
        editor.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        recyclerView = findViewById(R.id.recyclerView);

        itemList_text = new ArrayList<>();
        itemList_voice = new ArrayList<>();
        adapter = new ListElementAdapterAddShopItemToCategory(itemList_text);
        recyclerView.setAdapter(adapter);
        // use a linear layout manager
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(LayoutManager);
        //make editText respond directly when the activity starts
        editText.requestFocus();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() > 1) {
                    addtoListandgiveIcon(s.toString());
                }
                else {
                    if (!itemList_text.isEmpty()) {
                        itemList_text.remove(0);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
//                    if(event.getRawX() >= (editText.getLeft() - editText.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
//                        // your action here
//                        Log.d(TAG, "Hallo hier click on back button!");
//                        return true;
//                    }

                    if(event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        Log.d(TAG, "Hallo hier click on voice button!");
                        promptSpeechInput();
                        return true;
                    }
                }
                return false;
            }

        });
    }

    /**
     * Initializing speech input
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    temp_user_input = result.get(0);
                    Log.d(TAG, "Das ist das ergebnis der sprachsuche: "+temp_user_input);
                    seperateSpokenWords(temp_user_input);
                }
                break;
            }
        }
    }

    private void seperateSpokenWords(String input) {
        String[] separated = input.split("( and)");
        String added = "";
        String categoryName = "";

        for (int i = 0; i < separated.length; i++) {
            Intent intent = getIntent();
            categoryName = intent.getStringExtra("ShoppingCategory");
            Log.d(TAG, "seperateSpokenWords[i]: " + separated[i]);
            ShopItem item = new ShopItem(separated[i], "", categoryName);
            Log.d(TAG, "Item: "+item);
            item.setActivity(this);
            this.itemList_voice.add(item);
            if (i == 0) {
                added = added.concat(separated[i]);
            }
            else if (i == separated.length-1) {
                added = added.concat(" and " + separated[i]);
            }
            else added = added.concat(", " + separated[i]);
        }

        Toast.makeText(this, "You added: " + added + " to the category " + categoryName + "!", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Das ist die liste: "+this.itemList_voice);

    }

    public void addtoListandgiveIcon(String text) {
        temp_user_input = text;
        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("ShoppingCategory");

        ShopItem item = new ShopItem(temp_user_input, "", categoryName);
        item.setActivity(this);
        item.setIcon();

//        if (!findDuplicates(item)) {
//            item.setChecked(true);
//            item.setCheckmark();
//        }

        if (itemList_text.isEmpty()) {
            itemList_text.add(item);
        }
        else { itemList_text.set(0, item); }

        adapter.notifyDataSetChanged();

        //itemList_temp.set(2, new item_shopping(letterTile, temp_user_input, "testcategory"));
        //itemList_temp.add(shoppingitem);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // recycleView.setHasFixedSize(true);
    }

//    public boolean findDuplicates(ShopItem item) {
//        boolean element_not_found = true;
//        for (int i = 0; i < itemList_forMain.size(); i++) {
//            if (itemList_forMain.get(i).getName().equals(item.getName())) {
//                element_not_found = false;
//            }
//        }
//        return  element_not_found;
//    }
}

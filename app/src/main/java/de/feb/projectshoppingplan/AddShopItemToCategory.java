package de.feb.projectshoppingplan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.media.MediaPlayer;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddShopItemToCategory extends AppCompatActivity {

    private final static String TAG = "MyActivity";
    private final int REQ_CODE_SPEECH_INPUT = 100;

    String temp_user_input;
    EditText editText;
    RecyclerView recyclerView;
    List<ShopItem> itemList_text;
    List<ShopItem> itemList_voice;
    ListElementAdapterAddShopItemToCategory adapter;
    String categoryName;
    ArrayList<Category> categories = new ArrayList<>();
    ArrayList<ShopItem> itemList_forMain = new ArrayList<>();
    Category category;
    Activity AddShopItemToCategoryActivity = this;
    private final ArrayListUtils arrayListHelper = new ArrayListUtils(this);

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop_item_to_category);
        Toolbar toolbar = findViewById(R.id.toolbar_add_activity);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //create music player with free sound "service_bell" from source: http://soundbible.com/2218-Service-Bell-Help.html
        final MediaPlayer mediaPlayer = MediaPlayer.create(AddShopItemToCategory.this, R.raw.service_bell_daniel_simion);

        Intent intent = getIntent();
        categoryName = intent.getStringExtra("category_name");

        categories = arrayListHelper.loadArrayList("categories_arraylist");

        Log.d(TAG, "Categories hier!!: " + categories);

        getShoppingListFromCat();

        //get the elements from the activity
        editText = findViewById(R.id.editText_newShopItem);
        EditText editor = new EditText(this);
        editor.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        final SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        recyclerView = findViewById(R.id.recyclerView);

        itemList_text = new ArrayList<>();
        itemList_voice = new ArrayList<>();

        adapter = new ListElementAdapterAddShopItemToCategory(itemList_text);

        recyclerView.setAdapter(adapter);
        // use a linear layout manager
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(LayoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Toast.makeText(getApplicationContext(), "Hallo hier Item Click", Toast.LENGTH_LONG).show();
                        itemList_text.get(position).setChecked(!itemList_text.get(position).checked);
                        Log.d(TAG, "onItemClick: findDuplicates = " + findDuplicates(itemList_text.get(position)));
                        //add shopItem
                        if (!findDuplicates(itemList_text.get(position)) && itemList_text.get(position).checked) {
                            Toast.makeText(AddShopItemToCategoryActivity, itemList_text.get(position).name + " added", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onItemClick: NO DUPLICATES WERE FOUND....");
                            itemList_text.get(position).setCheckmark();
                            itemList_forMain.add(itemList_text.get(position));

                            for (int i = 0; i < categories.size(); i++) {
                                if(categories.get(i).getTitle().equals(categoryName)) {
                                    categories.get(i).getItems().clear();
                                    categories.get(i).getItems().addAll(itemList_forMain);
                                }
                            }
                            arrayListHelper.saveArrayList(categories, "categories_arraylist");

                            mediaPlayer.start();
                        }
                        else {
                            itemList_text.get(position).setCheckmark();
                            Toast.makeText(AddShopItemToCategoryActivity, "deleted!", Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < itemList_forMain.size(); i++) {
                                if(itemList_forMain.get(i).name.equals(itemList_text.get(position).name)) {
                                    itemList_forMain.remove(i);
                                }
                            }
                            for (int i = 0; i < categories.size(); i++) {
                                if(categories.get(i).getTitle().equals(categoryName)) {
                                    categories.get(i).getItems().clear();
                                    categories.get(i).getItems().addAll(itemList_forMain);
                                }
                            }
                            arrayListHelper.saveArrayList(categories, "categories_arraylist");
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        finish();
                    }
                })
        );

        //make editText respond directly when the activity starts
        editText.requestFocus();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.d(TAG, "onCreate: CharSequence TextWatcher = " + s);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences.Editor sharedPrefEditor;
                String sharedPrefsKey = "Grocery";

                if (s.toString().length() > 1) {
                    temp_user_input = s.toString();
                    Log.d(TAG, "TEMP USER INPUT 1: " + temp_user_input);
                    addtoRecyclerViewandgiveIcon(temp_user_input);
//                    if (s.charAt(s.length() - 1) == '\n') {
                    sharedPrefEditor = sharedPreferences.edit();
                    sharedPrefEditor.putString(sharedPrefsKey + temp_user_input, temp_user_input);
                    sharedPrefEditor.apply();
//                    Log.d(TAG, "afterTextChanged: if sharedPrefs = " + sharedPreferences.getString(sharedPrefsKey + "Hgzuuh", "no gro"));
//                    }
                } else {
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
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editText.getRight() - 30 - 2*editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Log.d(TAG, "Hallo hier click on voice button!");
                        promptSpeechInput();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void getShoppingListFromCat() {
        // search for category and get item list
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getTitle().equals(categoryName)) {
                category = categories.get(i);
                Log.d(TAG, "Category name: " + categoryName);
                Log.d(TAG, "list of Category: " + category.getItems());
                Gson gson = new Gson();
                String json = gson.toJson(category.getItems());
                itemList_forMain = arrayListHelper.getListFromJson(json);
                Log.d(TAG, "Hallo hier itemListForMain      " + itemList_forMain.toString());
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //initializing speech input
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
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    temp_user_input = result.get(0);
                    Log.d(TAG, "Das ist das Ergebnis der Sprachsuche: " + temp_user_input);
                    seperateSpokenWords(temp_user_input);
                }
                break;
            }
        }
    }

    private void seperateSpokenWords(String input) {
        String[] separated = input.split("( "+ getString(R.string.speech_input_separator) +" )");
        String added = "";

        for (String aSeparated : separated) {
            Log.d(TAG, "seperateSpokenWords[i]: " + aSeparated);
            ShopItem item = new ShopItem(aSeparated);
            Log.d(TAG, "Item: " + item);
            added = addVoiceItemsToList(item);
        }

        itemList_forMain.addAll(itemList_voice);

        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getTitle().equals(categoryName)) {
                categories.get(i).getItems().clear();
                //categories.get(i).setItems(itemList_forMain);
                //TODO Lösung für Warnung
                categories.get(i).getItems().addAll(itemList_forMain);
                Log.d(TAG, "Das ist die liste nachdem was geadded wurde: " + categories);
                arrayListHelper.saveArrayList(categories, "categories_arraylist");
            }
        }

        Toast.makeText(this, getString(R.string.speech_string_part1) + added + getString(R.string.speech_string_part2) + categoryName + getString(R.string.speech_string_part3), Toast.LENGTH_LONG).show();
        Log.d(TAG, "Das ist die liste: " + this.itemList_voice);
    }

    private String addVoiceItemsToList(ShopItem item) {
        String result = "";

        if (!findDuplicates(item)) {
            if(!itemList_voice.contains(item)) {
                this.itemList_voice.add(item);
            }
        }

        for (int i = 0; i < itemList_voice.size(); i++) {
            if (i == 0) {
                result = result.concat(itemList_voice.get(i).name);
            } else if (i == itemList_voice.size() - 1) {
                result = result.concat(" " + getString(R.string.speech_input_separator) + " " + itemList_voice.get(i).name);
            } else result = result.concat(", " + itemList_voice.get(i).name);
        }
        return result;
    }

    public void addtoRecyclerViewandgiveIcon(String text) {
        temp_user_input = text;
        Log.d(TAG, "TEMP USER INPUT 2: " + temp_user_input);

        ShopItem item = new ShopItem(temp_user_input);
        item.setActivity(this);
        item.setIcon();
        item.setCheckmark();

        if (findDuplicates(item)) {
            item.setChecked(true);
            item.setCheckmark();
            showItemList(item);
            adapter.notifyDataSetChanged();
            return;
        }
        showItemList(item);

        //itemList_forMain.addAll(itemList_text);

        adapter.notifyDataSetChanged();

    }

    /**
     * Displays itemlist after text input.
     *
     * @param item ShopItem
     */
    private void showItemList(ShopItem item) {
        if (itemList_text.isEmpty()) {
            itemList_text.add(item);
        } else {
            itemList_text.set(0, item);
        }
    }

    //true heißt duplikat gefunden
    //false nicht gefunden
    public boolean findDuplicates(ShopItem item) {
        Log.d(TAG, "findDuplicates: itemlist_forMain = " + itemList_forMain);
        Log.d(TAG, "findDuplicates: categories = " + category.getItems());
        for (int i = 0; i < category.getItems().size(); i++) {
//            Log.d(TAG, "findDuplicates: itemlist_forMain ITEM = " + itemList_forMain.get(i).name);
            if (itemList_forMain.get(i).name.equals(item.name))
                return true;
            }
        return false;
    }
}

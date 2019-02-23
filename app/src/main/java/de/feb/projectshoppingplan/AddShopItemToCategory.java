package de.feb.projectshoppingplan;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.media.MediaPlayer;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.support.annotation.RequiresApi;
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
import android.widget.ImageView;
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
    ArrayList<ShopItem> shopItems = new ArrayList<>();
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
            close();
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

        //get the elements from the activity
        editText = findViewById(R.id.editText_newShopItem);
        EditText editor = new EditText(this);
        editor.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        final SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        recyclerView = findViewById(R.id.recyclerView);

        itemList_text = new ArrayList<>();
        itemList_voice = new ArrayList<>();

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
                for (int j = 0; j < itemList_forMain.size(); j++) {
                    Log.d("MyActivity", "Hier einzelnes item *_*: " + itemList_forMain.get(j));
                }
            }
        }
        adapter = new ListElementAdapterAddShopItemToCategory(itemList_text);

        recyclerView.setAdapter(adapter);
        // use a linear layout manager
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(LayoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    //Wegen dem drawable muss mindestens Lollipop auf dem Smartphone sein => todo schauen ob es eine andere lösung gibt
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onItemClick(View view, int position) {
                        //Toast.makeText(getApplicationContext(), "Hallo hier Item Click", Toast.LENGTH_LONG).show();
                        itemList_text.get(position).setChecked(!itemList_text.get(position).checked);
                        if (!findDuplicates(itemList_text.get(position)) && itemList_text.get(position).checked) {
                            itemList_text.get(position).setCheckmark();
                            Toast.makeText(AddShopItemToCategoryActivity,itemList_text.get(position).name+" added", Toast.LENGTH_SHORT).show();

                            mediaPlayer.start();
                        } else {
                            itemList_text.get(position).setCheckmark();
                            Toast.makeText(AddShopItemToCategoryActivity,"deleted!", Toast.LENGTH_SHORT).show();
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        close();
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

            //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                    if (event.getRawX() >= (editText.getRight() - 80 - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Log.d(TAG, "Hallo hier click on voice button!");
                        promptSpeechInput();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    void close() {
        addToMainList();
        finish();
//        Intent intent = new Intent(this, MainActivity.class);
//        this.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        close();
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
                    Log.d(TAG, "Das ist das ergebnis der sprachsuche: " + temp_user_input);
                    seperateSpokenWords(temp_user_input);
                }
                break;
            }
        }
    }

    private void seperateSpokenWords(String input) {
        String[] separated = input.split("( and )");
        String added = "";

        for (int i = 0; i < separated.length; i++) {
            Log.d(TAG, "seperateSpokenWords[i]: " + separated[i]);
            ShopItem item = new ShopItem(separated[i]);
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

        Toast.makeText(this, "You added: " + added + " to the category " + categoryName + "!", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Das ist die liste: " + this.itemList_voice);
    }

    private String addVoiceItemsToList(ShopItem item) {
        String result = "";

        if (!findDuplicates(item)) {
            this.itemList_voice.add(item);
        }

        for (int i = 0; i < itemList_voice.size(); i++) {
            if (i == 0) {
                result = result.concat(itemList_voice.get(i).name);
            } else if (i == itemList_voice.size() - 1) {
                result = result.concat(" and " + itemList_voice.get(i).name);
            } else result = result.concat(", " + itemList_voice.get(i).name);
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        }

        if (itemList_text.isEmpty()) {
            itemList_text.add(item);
        } else {
            itemList_text.set(0, item);
        }

        shopItems.addAll(itemList_text);

        adapter.notifyDataSetChanged();

    }

    public void addToMainList() {
        for (int i = 0; i < shopItems.size(); i++) {
            if(shopItems.get(i).checked) {
                ShopItem shopItem = new ShopItem(shopItems.get(i).name);
                shopItem.setActivity(AddShopItemToCategoryActivity);
                shopItem.setIcon();
                itemList_forMain.add(shopItem);
            }
        }

        Log.d(TAG, "Hallo hier itemList Main: " + itemList_forMain);

        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getTitle().equals(categoryName)) {
                categories.get(i).setItems(itemList_forMain);
                Log.d(TAG, "Das ist die liste nachdem was geadded wurde: " + categories);
            }
        }
        arrayListHelper.saveArrayList(categories, "categories_arraylist");
    }

    //true heißt duplikat gefunden
    //false nicht gefunden
    public boolean findDuplicates(ShopItem item) {
        for (int i = 0; i < itemList_forMain.size(); i++) {

            if (itemList_forMain.get(i).name.equals(item.name)) {
                return true;
            }
        }
        return false;
    }
}

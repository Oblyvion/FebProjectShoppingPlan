package de.feb.projectshoppingplan;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop_item_to_category);

        Intent intent = getIntent();
        categoryName = intent.getStringExtra("category_name");

        categories = getCategoryList("categories_arraylist");

        Log.d(TAG, "Categories hier!!: "+categories);

        //get the elements from the activity
        editText = findViewById(R.id.editText_newShopItem);
        EditText editor = new EditText(this);
        editor.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        final SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        recyclerView = findViewById(R.id.recyclerView);

        itemList_text = new ArrayList<>();
        itemList_voice = new ArrayList<>();

        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getTitle().equals(categoryName)) {
                category = categories.get(i);
                Log.d(TAG, "Category name: "+categoryName);
                Log.d(TAG, "list of Category: "+category.getItems());
                Gson gson = new Gson();
                String json = gson.toJson(category.getItems());
                itemList_forMain = getListFromJson(json);
                Log.d(TAG, "Hallo hier itemListForMain      "+ itemList_forMain.toString());
                for (int j = 0; j < itemList_forMain.size(); j++) {
                    Log.d("MyActivity", "Hier einzelnes item *_*: "+itemList_forMain.get(j));
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
                        if(!findDuplicates(itemList_text.get(position))) {
                            itemList_text.get(position).setChecked(true);
                            itemList_text.get(position).setCheckmark();
                            itemList_forMain.add(new ShopItem(itemList_text.get(position).name));
                            for (int i = 0; i < itemList_forMain.size(); i++) {
                                itemList_forMain.get(i).setActivity(AddShopItemToCategoryActivity);
                                itemList_forMain.get(i).setIcon();
                            }
                            Log.d(TAG, "Hallo hier itemList Main: "+itemList_forMain);
                            for (int i = 0; i < categories.size(); i++) {
                                if (categories.get(i).getTitle().equals(categoryName)) {
                                    categories.get(i).getItems().clear();
                                    //categories.get(i).setItems(itemList_forMain);
                                    //TODO Lösung für Warnung
                                    categories.get(i).getItems().addAll(itemList_forMain);
                                    Log.d(TAG, "Das ist die liste nachdem was geadded wurde: "+categories);
                                    saveArrayList(categories, "categories_arraylist");
                                }
                            }
//                            saveArrayList(categories, "MainList");
                        }
                        else {
                            itemList_text.get(position).setCheckmark();
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

//        editText.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int key, KeyEvent keyEvent) {
//                SharedPreferences.Editor sharedPrefEditor;
//
//                if (!(key == KeyEvent.KEYCODE_ENTER)) {
//                    editText.addTextChangedListener(new TextWatcher() {
//                        @Override
//                        public void onTextChanged(CharSequence s, int start, int before, int count) {
//                            //Log.d(TAG, "onCreate: CharSequence TextWatcher = " + s);
//
//                        }
//
//                        @Override
//                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                        }
//
//                        //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//                        @Override
//                        public void afterTextChanged(Editable s) {
//                            SharedPreferences.Editor sharedPrefEditor;
//                            String sharedPrefsKey = "Grocery";
//
//                            //Log.d(TAG, "afterTextChanged: jojooojojjo charAT = " + s.charAt(s.length() - 1));
//                            if (s.toString().length() > 1) {
//
//                                temp_user_input = "\""+s.toString()+"\"";
//                                Log.d(TAG, "TEMP USER INPUT 1: "+temp_user_input);
//
//                                addtoListandgiveIcon("\""+s.toString()+"\"");
//                                //Log.d(TAG, "afterTextChanged: HLALLJSADFOJODSA");
//                                //Log.d(TAG, "afterTextChanged: temp_user_input = " + temp_user_input);
////                    if (s.charAt(s.length() - 1) == '\n') {
//                                sharedPrefEditor = sharedPreferences.edit();
//                                sharedPrefEditor.putString(sharedPrefsKey + temp_user_input.toString(), temp_user_input);
//                                sharedPrefEditor.apply();
////                                Log.d(TAG, "afterTextChanged: if sharedPrefs = " + sharedPreferences.getString(sharedPrefsKey + "Hgzuuh", "no gro"));
////                    }
//                            } else {
//                                if (!itemList_text.isEmpty()) {
//                                    itemList_text.remove(0);
//                                }
//                                adapter.notifyDataSetChanged();
//                            }
//                        }
//                    });
//
//                }
//
////                Log.d(TAG, "onKey: sharedPrefs Juhuuu");
//                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
//                    if (key == KeyEvent.KEYCODE_ENTER) {
//                        sharedPrefEditor = sharedPreferences.edit();
//                        sharedPrefEditor.putString(getString(R.string.groceries), temp_user_input);
//                        sharedPrefEditor.apply();
////                        Log.d(TAG, "onKey: sharedPrefs = " + getString(R.string.groceries));
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });

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

//                Log.d(TAG, "afterTextChanged: jojooojojjo charAT = " + s.charAt(s.length() - 1));
                if (s.toString().length() > 1) {
                    temp_user_input = s.toString();
                    Log.d(TAG, "TEMP USER INPUT 1: "+temp_user_input);
                    addtoListandgiveIcon(s.toString());
//                    if (s.charAt(s.length() - 1) == '\n') {
                    sharedPrefEditor = sharedPreferences.edit();
                    sharedPrefEditor.putString(sharedPrefsKey + temp_user_input.toString(), temp_user_input);
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

//       editText.setOnKeyListener(new View.OnKeyListener() {
//            //
//            @Override
//            public boolean onKey(View view, int key, KeyEvent keyEvent) {
//                SharedPreferences.Editor sharedPrefEditor;
//
//                Log.d(TAG, "onKey: sharedPrefs Juhuuu");
//                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
//                    if (key == KeyEvent.KEYCODE_ENTER) {
//                        sharedPrefEditor = sharedPreferences.edit();
//                        sharedPrefEditor.putString(getString(R.string.groceries), temp_user_input);
//                        sharedPrefEditor.apply();
//                        Log.d(TAG, "onKey: sharedPrefs = " + getString(R.string.groceries));
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
//                performClick();

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (editText.getLeft() - editText.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                        // your action here
                        Log.d(TAG, "Hallo hier click on back button!");
                        close();
                        return true;
                    }

                    if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        Log.d(TAG, "Hallo hier click on voice button!");
                        promptSpeechInput();
                        return true;
                    }
                }
                return false;
            }

//            @Override
//            public void performClick() {
//
//            }


        });
    }

    void close() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
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
        String[] separated = input.split("( and)");
        String added = "";
        categoryName = "";

        for (int i = 0; i < separated.length; i++) {
            Log.d(TAG, "seperateSpokenWords[i]: " + separated[i]);
            ShopItem item = new ShopItem(separated[i]);
            Log.d(TAG, "Item: " + item);
//            item.setActivity(this);
            this.itemList_voice.add(item);
            if (i == 0) {
                added = added.concat(separated[i]);
            } else if (i == separated.length - 1) {
                added = added.concat(" and " + separated[i]);
            } else added = added.concat(", " + separated[i]);
        }

        Toast.makeText(this, "You added: " + added + " to the category " + categoryName + "!", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Das ist die liste: " + this.itemList_voice);

    }

    //Die Komplette Liste mit Categories aus der Main Activity
    public ArrayList<Category> getCategoryList(String key){
        SharedPreferences prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        Gson gson = gsonBuilder.create();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<Category>>() {}.getType();
        return gson.fromJson(json, type);
    }

    //Aus einzelner Unterliste im JSON format wird wieder eine Arraylist<ShopItem>
    public ArrayList<ShopItem> getListFromJson(String json){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        Gson gson = gsonBuilder.create();
        Type type = new TypeToken<ArrayList<ShopItem>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void saveArrayList(ArrayList<Category> list, String key){
        SharedPreferences prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
//        Log.d(TAG, "Json: "+json);
        editor.putString(key, json);
        editor.apply();     //Wichtige Zeile
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void addtoListandgiveIcon(String text) {
        temp_user_input = text;
        Log.d(TAG, "TEMP USER INPUT 2: "+temp_user_input);

//        Intent intent = getIntent();
//        String categoryName = intent.getStringExtra("ShoppingCategory");

        ShopItem item = new ShopItem(temp_user_input);
        item.setActivity(this);
        item.setIcon();

        if (findDuplicates(item)) {
            item.setChecked(true);
            item.setCheckmark();
        }

        if (itemList_text.isEmpty()) {
            itemList_text.add(item);
        } else {
            itemList_text.set(0, item);
        }

        adapter.notifyDataSetChanged();

    }

    //true heißt duplikat gefunden
    //false nicht gefunden
    public boolean findDuplicates(ShopItem item) {
        for (int i = 0; i < itemList_forMain.size(); i++) {

            if (itemList_forMain.get(i).name.equals(item.name)) {
                return true;
            }
        }
        return  false;
    }
}

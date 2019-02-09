package de.feb.projectshoppingplan;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.annotation.LongDef;
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

import com.google.gson.Gson;
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
    Activity addShopItemToCategory = this;
    ArrayList<Category> categories;
    List<ShopItem> itemList_forMain;
    Category category;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop_item_to_category);

        Intent intent = getIntent();
        categoryName = intent.getStringExtra("category_name");

        categories = getArrayList("categories_arraylist");

        Log.d(TAG, "Categories hier!!: "+categories);

        //get the elements from the activity
        editText = findViewById(R.id.editText_newShopItem);
        EditText editor = new EditText(this);
        editor.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        Log.d(TAG, "onCreate: editor = " + editor.getText());

        recyclerView = findViewById(R.id.recyclerView);

        itemList_text = new ArrayList<>();
        itemList_voice = new ArrayList<>();
        itemList_forMain = new ArrayList<ShopItem>();
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getTitle().equals(categoryName)) {
                category = categories.get(i);
                category.getItems().addAll(itemList_forMain);
                //TODO alle items die in der liste der category sind der itemList_forMain hinzufüge
                //TODO dann kann man überprüfen ob das jeweils eingegebene produkt schon in der liste ist
//                for (int j = 0; j < categories.get(i).getItems().size(); j++) {
//                    itemList_forMain.add((ShopItem) category.getItems().get(j));
//                }
            }
        }
        Log.d(TAG, "Category name: "+categoryName);
        Log.d(TAG, "Category: "+category);
        Log.d(TAG, "Hallo hier itemListForMain      "+ itemList_forMain);
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
                            itemList_text.get(position).setCheckmark();
                            itemList_forMain.add(new ShopItem(itemList_text.get(position).name, "", itemList_text.get(position).category));
                            saveArrayList((ArrayList<ShopItem>)itemList_forMain, "MainList");
                        }
                        else {
                            itemList_text.get(position).setCheckmark();
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
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                    if(event.getRawX() >= (editText.getLeft() - editText.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                        // your action here
                        Log.d(TAG, "Hallo hier click on back button!");
                        finish();
                        return true;
                    }

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

    //receiving speech input
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
        categoryName = "";

        for (int i = 0; i < separated.length; i++) {
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

    public ArrayList<Category> getArrayList(String key){
        SharedPreferences prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<Category>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void saveArrayList(ArrayList<ShopItem> list, String key){
        SharedPreferences prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        Log.d(TAG, "Json: "+json);
        editor.putString(key, json);
        editor.apply();     //Wichtige Zeile
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void addtoListandgiveIcon(String text) {
        temp_user_input = text;
        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("ShoppingCategory");

        ShopItem item = new ShopItem(temp_user_input, "", categoryName);
        item.setActivity(this);
        item.setIcon();

        if (findDuplicates(item)) {
            item.setChecked(true);
            item.setCheckmark();
        }

        if (itemList_text.isEmpty()) {
            itemList_text.add(item);
        }
        else { itemList_text.set(0, item); }

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

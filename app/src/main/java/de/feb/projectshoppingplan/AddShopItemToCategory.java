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
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddShopItemToCategory extends AppCompatActivity {

    private final static String TAG = "AddShopItem";
    private final int REQ_CODE_SPEECH_INPUT = 100;

    String temp_user_input;
    ArrayList<String> temp_user_input_rest = new ArrayList<>();
    EditText editText;
    ImageButton imageButton;
    RecyclerView recyclerView;
    ArrayList<ShopItem> itemList_text;
    ArrayList<ShopItem> itemList_voice;
    ListElementAdapterAddShopItemToCategory adapter;
    String categoryName;
    ArrayList<Category> categories = new ArrayList<>();
    ArrayList<ShopItem> itemList_forMain = new ArrayList<>();
    Category category;
    Activity AddShopItemToCategoryActivity = this;
    private final ArrayListUtils arrayListHelper = new ArrayListUtils();

    private String key;

    //regex: matches only words or sentences without "," and spaces before first word
    // and spaces behind last word as well
    private final String REGEX = "[^\\s,]+[0-9a-zA-Z]?\\s?[^,]{2,}[^,\\s]";


    /**
     * Back button functionality gets defined
     *
     * @param item - Menu item which was selected
     * @return constructor from upper class
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ClickableViewAccessibility")
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop_item_to_category);

        key = MainActivity.key;

//        SharedPreferences prefs = getApplicationContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
//        final String key = getApplicationContext().getString(R.string.list0_key);
//        Log.d(TAG, "onCreate: --------->>>> KEY ----------<<<<<< = " + key);

        //create music player with free sound "service_bell" from source: http://soundbible.com/2218-Service-Bell-Help.html
        final MediaPlayer mediaPlayer = MediaPlayer.create(AddShopItemToCategory.this, R.raw.service_bell_daniel_simion);

        Intent intent = getIntent();
        categoryName = intent.getStringExtra("category_name");
        Log.d(TAG, "onCreate: category = " + categoryName);

        Toolbar toolbar = findViewById(R.id.toolbar_add_activity);
        toolbar.setTitle(categoryName);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        categories = arrayListHelper.loadArrayList(key);

        Log.d(TAG, "Categories hier!!: " + categories);

        getShoppingListFromCat();

        //get the elements of the activity
        editText = findViewById(R.id.editText_newShopItem);
        EditText editor = new EditText(this);
        editor.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        imageButton = findViewById(R.id.bttn_editText_microphone);
        int idVoiceSymbol = getResources().getIdentifier("de.feb.projectshoppingplan:drawable/ic_keyboard_voice_black_24dp", null, null);
        imageButton.setImageResource(idVoiceSymbol);

        recyclerView = findViewById(R.id.recyclerView);

        itemList_text = new ArrayList<>();
        itemList_voice = new ArrayList<>();

        adapter = new ListElementAdapterAddShopItemToCategory(itemList_text);

        recyclerView.setAdapter(adapter);
        // use a linear layout manager
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(LayoutManager);

        //Recognizes a click on an item
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    /**
                     * * Triggers: short click on an item, if item is not in list of category yet it will be added otherwise it is going to be deleted
                     *
                     */
//                    @SuppressWarnings("SuspiciousListRemoveInLoop")
                    @Override
                    public void onItemClick(View view, int position) {
                        mediaPlayer.start();
                        itemList_text.get(position).setChecked(!itemList_text.get(position).checked);
//                        Log.d(TAG, "onItemClick: findDuplicates = " + findDuplicates(itemList_text, itemList_text.get(position)));

                        //TODO GET ALL APPROPRIATE STRINGS FROM text
                        if (temp_user_input.contains(",")) {

                            Pattern pattern = Pattern.compile(REGEX);
                            Matcher matcher = pattern.matcher(temp_user_input);

//            String[] wordsSeparated = new String[5];


                            Log.d(TAG, "afterTextChanged: TEMP USER INPUT ==== " + temp_user_input);

                            int i = 0;
                            while (matcher.find()) {
                                Log.d(TAG, "afterTextChanged: INSIDE WHILE");

                                Log.d(TAG, "afterTextChanged: MATCHER GROUP COUNT = " + matcher.groupCount());
                                Log.d(TAG, "afterTextChanged: MATCHER IN WHILE FIND GROUP 0 = " + matcher.group(0));


                                //get string until comma index
//                            wordsSeparated[i] = matcher.group(0);
//                i++;

                                Log.d(TAG, "afterTextChanged: TEMP USER INPUT REST IN WHILE before replacement ==== " + temp_user_input_rest);
                                Log.d(TAG, "afterTextChanged: TEMP USER INPUT IN WHILE before replacement ==== " + temp_user_input);
                                Log.d(TAG, "afterTextChanged: TEMP USER INPUT 2 IN WHILE before replacement ==== " + temp_user_input);

                                //saving all separated words in array temp_user_input_rest []
                                itemList_text.clear();
                                //TODO while no duplicates were found, add to itemList_text
                                if (!temp_user_input_rest.contains(matcher.group(0))) {
                                    temp_user_input_rest.add(matcher.group(0));

//                                    item.setActivity();
//                                    item.setChecked(true);
//                                    item.getCheckmarkId();
                                    //TODO save it into shopItem Array itemList_text
                                }

                                temp_user_input = temp_user_input.replaceFirst(REGEX, "");
                                i++;

                                Log.d(TAG, "afterTextChanged: TEMP USER INPUT REST IN WHILE after replacement ==== " + temp_user_input_rest);
                                Log.d(TAG, "afterTextChanged: TEMP USER INPUT 2 IN WHILE before replacement ==== " + temp_user_input);
                                Log.d(TAG, "onItemClick: ITEM LIST ============== " + itemList_text.size());
                            }

                            for (String str : temp_user_input_rest) {
                                Log.d(TAG, "onItemClick: ITEM LIST ============== " + itemList_text.size());
                                ShopItem item = new ShopItem(str);
                                item.setChecked(true);
                                item.setActivity(AddShopItemToCategoryActivity);
                                item.setIcon();
                                itemList_text.add(item);
                            }
                            Log.d(TAG, "onItemClick: ITEM LIST LAST POST ============== " + itemList_text.size());

                        }

                        Log.d(TAG, "onItemClick: LIST FOR MAIN = " + itemList_forMain.size());

                        while (position < itemList_text.size()) {
                            //add shopItem
                            if (!findDuplicates(itemList_forMain, itemList_text.get(position)) && itemList_text.get(position).checked) {
                                Toast.makeText(AddShopItemToCategoryActivity, itemList_text.get(position).name + " added", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onItemClick: NO DUPLICATES WERE FOUND....");
//                            itemList_text.get(position).getCheckmarkId();
                                itemList_forMain.add(itemList_text.get(position));

                                for (int i = 0; i < categories.size(); i++) {
                                    if (categories.get(i).getTitle().equals(categoryName)) {
                                        categories.get(i).getItems().clear();
                                        categories.get(i).getItems().addAll(itemList_forMain);
                                    }
                                }
                                arrayListHelper.saveArrayList(categories, key);
                            } else {
//                                itemList_text.get(position).getCheckmarkId();
                                Log.d(TAG, "onItemClick: FOUND DUPLICATES ---> " + itemList_text.get(position).name);

                                //TODO maybe temp_user_input_rest do not work for one item => add one item also to temp_user_input_rest
//                                for (ShopItem shopItemListMain : itemList_forMain) {
                                    for (ShopItem shopItemListText : itemList_text) {
                                        if (shopItemListText.name != null
                                                && shopItemListText.name.equals(itemList_forMain.get(position).name)) {
                                            Toast.makeText(AddShopItemToCategoryActivity, shopItemListText.name + " exists already!", Toast.LENGTH_SHORT).show();
                                            ViewHolderShopI.delete(shopItemListText);
                                            itemList_forMain.remove(shopItemListText);
                                        }
                                    }
//                                }

//                            for (int i = 0; i < itemList_forMain.size(); i++) {
//                                if (itemList_forMain.get(i).name.equals(itemList_text.get(position).name)) {
//                                    ViewHolderShopI.delete(itemList_forMain.get(i));
//                                    itemList_forMain.remove(i);
//                                }
//                            }
                                for (int i = 0; i < categories.size(); i++) {
                                    if (categories.get(i).getTitle().equals(categoryName)) {
                                        categories.get(i).getItems().clear();
                                        categories.get(i).getItems().addAll(itemList_forMain);
                                    }
                                }
                                arrayListHelper.saveArrayList(categories, key);
                            }

                            //get next shopItem from itemList_text => position++
                            position++;
                        }
                        adapter.notifyDataSetChanged();
                    }

                    /**
                     * * Recognizes a long click on an item and finishes the activity
                     */
                    @Override
                    public void onLongItemClick(View view, int position) {
                        finish();
                    }
                })
        );

        //makes editText respond directly when the activity starts
        editText.requestFocus();


        // editText addTextChangedListener - Notifies if text changed
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 1) {
                    temp_user_input = s.toString();

                    Log.d(TAG, "afterTextChanged: AFTERT");

                    //TODO SEPARATE WORDS BY COMMA
//                    Log.d(TAG, "afterTextChanged: AFTERT INSIDE IF");

//                    if (temp_user_input.contains(",")) {
//
//
//                        int i = 0;
//                        String[] wordsSeparated = new String[5];
//                        Pattern pattern = Pattern.compile("[^,\\s]{2,}[^,]*");
//                        Matcher matcher = pattern.matcher(temp_user_input);
//
//                        Log.d(TAG, "afterTextChanged: MATCHER FIND = " + matcher);
//                        Log.d(TAG, "afterTextChanged: MATCHER FIND STRING = " + matcher.toString());
//                        Log.d(TAG, "afterTextChanged: MATCHER FIND ? = " + matcher.find());
//                        Log.d(TAG, "afterTextChanged: MATCHER GROUP COUNT = " + matcher.groupCount());
////                        Log.d(TAG, "afterTextChanged: MATCHER GROUP  = " + matcher.group());
////                        Log.d(TAG, "afterTextChanged: MATER FIND GROUP = " + matcher.group());
//                        Log.d(TAG, "afterTextChanged: MATCHER FIND GROUP 0 = " + matcher.group(0));
//
//                        Log.d(TAG, "afterTextChanged: TEMP USER INPUT ==== " + temp_user_input);
//
////                        if (matcher.find()) {
////                            Log.d(TAG, "afterTextChanged: INSIDE WHILE");
////                            Log.d(TAG, "afterTextChanged: MATCHER IN WHILE FIND GROUP 0 = " + matcher.group(0));
////
//////                            TODO save it into shopItem Array itemList_text
//////                            itemList_text[i] =
////
////                            //TODO while no duplicates were found, add to itemList_text
////
////                            //get string until comma index
//////                            wordsSeparated[i] = matcher.group(0);
//////                            i++;
////                            Log.d(TAG, "afterTextChanged: TEMP USER INPUT IN WHILE before replacement ==== " + temp_user_input);
////
////                            addtoRecyclerViewandgiveIcon(matcher.group(0));
////                            temp_user_input_rest = temp_user_input.replaceFirst(REGEX, "");
////
////                            Log.d(TAG, "afterTextChanged: TEMP USER INPUT REST IN WHILE after replacement ==== " + temp_user_input_rest);
////                        }
//                    } else {
                    addtoRecyclerViewandgiveIcon(temp_user_input);
//                    }


                } else {
                    if (!itemList_text.isEmpty()) {
                        itemList_text.remove(0);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int DRAWABLE_RIGHT = 2;

//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (event.getRawX() >= (editText.getRight() - 30 - 2 * editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                Log.d(TAG, "Hallo hier click on voice button!");
                promptSpeechInput();
//                    }
//                }
//
            }
        });

        //imageButton onTouchListener - triggers if voice button is clicked
//        imageButton.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final int DRAWABLE_RIGHT = 2;
//
////                if (event.getAction() == MotionEvent.ACTION_UP) {
////                    if (event.getRawX() >= (editText.getRight() - 30 - 2 * editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
//                        Log.d(TAG, "Hallo hier click on voice button!");
//                        promptSpeechInput();
//                        return true;
////                    }
////                }
////                return false;
//            }
//        });
    }

    /**
     * Get the shop item list of the chosen category
     */
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

    /**
     * Overrides onBackPressed(), so that the back button is working
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * Opens speech input activity
     */
    //initializing speech input
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            Log.d(TAG, "promptSpeechInput: SPEECH INPUT STARTED!");
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

    /**
     * Separates the string which is passed to the function and adds it to the categories list
     *
     * @param input - Voice Input of the user
     */
    @SuppressWarnings("unchecked")
    private void seperateSpokenWords(String input) {
        String[] separated = input.split("( " + getString(R.string.speech_input_separator) + " )");
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
                categories.get(i).getItems().addAll(itemList_forMain);
                Log.d(TAG, "Das ist die liste nachdem was geadded wurde: " + categories);
                arrayListHelper.saveArrayList(categories, key);
            }
        }

        Toast.makeText(this, getString(R.string.speech_string_part1) + added + getString(R.string.speech_string_part2) + categoryName + getString(R.string.speech_string_part3), Toast.LENGTH_LONG).show();
        Log.d(TAG, "Das ist die liste: " + this.itemList_voice);
    }

    /**
     * @param item - item to check if duplicates where found
     * @return separated String which is now concatenated for the Toast message
     */
    private String addVoiceItemsToList(ShopItem item) {
        String result = "";

        if (!findDuplicates(itemList_forMain, item)) {
            if (!findDuplicates(itemList_voice, item)) {
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

    /**
     * Creates new item out of user input,
     *
     * @param text - user text input
     */
    public void addtoRecyclerViewandgiveIcon(String text) {
        String temp_user_input2 = text;
        Log.d(TAG, "TEMP USER INPUT 2: " + temp_user_input2);

        //only one word or sentence without comma separation
//        else {
        Log.d(TAG, "addtoRecyclerViewandgiveIcon: ONE WORD OR SENTENCE WORKS GREAT");
        ShopItem item = new ShopItem(temp_user_input2);
        item.setActivity(this);
        item.setIcon();

        if (findDuplicates(itemList_forMain, item)) {
            item.setChecked(true);
            showItemList(item);
            adapter.notifyDataSetChanged();
            return;
        }
        showItemList(item);

//        }


//        ShopItem item = new ShopItem(temp_user_input2);
//        item.setActivity(this);
//        item.setIcon();
//        item.getCheckmarkId();


    }

    private void showItemList(ShopItem item) {
        if (itemList_text.isEmpty()) {
            itemList_text.add(item);
        } else {
            itemList_text.set(0, item);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Adds items to itemList_text and Displays itemlist after text input.
     *
     * @param items ShopItem
     */
    private void showItemList(ShopItem[] items) {
        if (itemList_text.isEmpty()) {
            itemList_text.addAll(Arrays.asList(items));
        } else {
            for (int i = 0; i < items.length; i++) {

                itemList_text.set(i, items[i]);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * @param list - list in which duplicates will be searched
     * @param item - item which is searched in list
     * @return - returns if duplicates were found (true) or not (false)
     */
    public boolean findDuplicates(ArrayList<ShopItem> list, ShopItem item) {
        getShoppingListFromCat();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).name.equals(item.name))
                return true;
        }
        return false;
    }
}

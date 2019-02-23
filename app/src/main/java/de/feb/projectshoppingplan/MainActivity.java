package de.feb.projectshoppingplan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    //TAG logcat
    final static String TAG = "MyActivity";
    //Recyclerview declaration
    RecyclerView recyclerView;

    private final ArrayListUtils arrayListHelper = new ArrayListUtils(this);


    private boolean isMultiSelect = false;
    //private ArrayList<Integer> selectedIds = new ArrayList<>();

    //Categories
    final String[] STANDARD_CATEGORIES = {AppContext.getContext().getString(R.string.standardCat0), AppContext.getContext().getString(R.string.standardCat1),
            AppContext.getContext().getString(R.string.standardCat2), AppContext.getContext().getString(R.string.standardCat3), AppContext.getContext().getString(R.string.standardCat4), AppContext.getContext().getString(R.string.standardCat5)};

    ExpandableRecyclerViewAdapter adapter;

    //Main Liste der Categories (enthält alle Categories und die dazu gehörigen ShopItem Listen)
    public ArrayList<Category> categories = new ArrayList<>();

    /**
     * Loads the categories list when app returned to current activity.
     */
    @Override
    protected void onResume() {
        Log.d(TAG, "MainActivity: On Resume");
        super.onResume();
        loadSharedPreferences();
        datachanged();
    }


    /**
     * @param menu Options
     * @return true: Inflate the menu. This adds items to the action bar if it is present.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.deleteCats);
        SpannableString spannableString = new SpannableString(item.getTitle().toString());
        spannableString.setSpan(new ForegroundColorSpan(Color.rgb(179, 0, 0)), 0, spannableString.length(), 0);

        item.setTitle(spannableString);
        return true;
    }

    /**
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d(TAG, "MainActivity: On Create");

        //finden recycler view
        recyclerView = findViewById(R.id.recyclerViewMain);

        //developer is able to reset all
//        delete();

        loadSharedPreferences();

        if (categories.isEmpty()) {
            addStandardCats();
        }

        //Adapter wird deklariert und initialisiert
        //Kann erst hier gemacht werden, da in categories was drin sein muss
        adapter = new ExpandableRecyclerViewAdapter(categories);

        //Soll man machen wenn man weiß das sich die recyclerview elemente nicht ändern (also bezogen auf größe immer gleich bleibend)
        recyclerView.setHasFixedSize(true);

        //Linearlayout dem recycleview mitgeben soll man so machen damits besser angezeigt wird
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        // drag and drop
        ItemTouchHelper.Callback itemTouchHelperCallback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
//                Log.d(TAG, "getMovementFlags: GRAP ITEM...");
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
            }

            /**
             *
             * @param recyclerView The current recycler view.
             * @param fromViewHolder ViewHolder which is dragged.
             * @param toViewHolder ViewHolder where dropped to.
             * @return true: If items have equal types, than swap them.
             */
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder fromViewHolder, @NonNull RecyclerView.ViewHolder toViewHolder) {

//                Log.d(TAG, "onMove: itemViewFROM TYPE = " + fromViewHolder.getItemViewType());
//                Log.d(TAG, "onMove: itemViewTO TYPE = " + toViewHolder.getItemViewType());

                //do not swap when item types are unequal
                if (fromViewHolder.getItemViewType() != toViewHolder.getItemViewType()) {
                    return false;
                }

//                Log.d(TAG, "onMove: adapter POSITION FROM = " + fromViewHolder.getAdapterPosition());
//                Log.d(TAG, "onMove: adapter POSITION TO = " + toViewHolder.getAdapterPosition());

                //notifies adapter that items are moving
                adapter.moveItem(fromViewHolder.getAdapterPosition(), toViewHolder.getAdapterPosition());

                //saves changes after drag & drop
                categories = (ArrayList<Category>) adapter.getGroups();
                arrayListHelper.saveArrayList(categories, "categories_arraylist");

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            }
        };

        SwipeController swipeController = new SwipeController();
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        datachanged();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (!isMultiSelect) {
                    //selectedIds = new ArrayList<>();
                    isMultiSelect = true;
                }

                if (menuItem.toString().equals("Clear Categories!")) {
                    Log.d(TAG, "das ist das menu item: " + menuItem);

                    AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Remove all items from each category?");
                    LayoutInflater.from(MainActivity.this).inflate(R.layout.alert_dialog, (ViewGroup) findViewById(android.R.id.content), false);

                    // button setup
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            //lässt Kategorie-Erweiterungspfeile verschwinden
//                        showCategoryNotExpandable();

                            clearAllCategories();

                            //Save der Liste nachdem alle categories gecleared wurden
                            arrayListHelper.saveArrayList(categories, "categories_arraylist");


                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
                if (menuItem.toString().equals("Sort Categories")) {
                    Log.d("MENUItem", "Menu Item sort");
                    sortList();
                    arrayListHelper.saveArrayList(categories, "categories_arraylist");
                    datachanged();
                }

                if (menuItem.toString().equals("Intro Screen ON/OFF")) {

                    SharedPreferences prefs = recyclerView.getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

                    Log.d(TAG, "onMenuItemClick: sharedPrefs = " + prefs);

                    AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                    //intro screen is off
                    if (prefs.getInt("splashTimeOut", 500) != 500) {

                        builder.setTitle("Do you want to set intro screen ON?");
                    } else builder.setTitle("Do you want to set intro screen OFF?");

                    LayoutInflater.from(MainActivity.this).inflate(R.layout.alert_dialog, (ViewGroup) findViewById(android.R.id.content), false);

                    // button setup
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            SharedPreferences prefs = recyclerView.getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();

                            if (prefs.getInt("splashTimeOut", 500) != 500)
                                editor.putInt("splashTimeOut", 500).apply();
                            else editor.putInt("splashTimeOut", 0).apply();
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }

                return false;
            }
        });


        // recyclerView wird itemTouchHelper hinzugefügt
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //saves arrayList categories
        arrayListHelper.saveArrayList(categories, "categories_arraylist");

        //set adapter to recyclerView
        recyclerView.setAdapter(adapter);

        Log.d(TAG, "Das ist die Größe der Category Liste: " + categories.size());

        //Floating button und Alert Dialog für Category Adding
        FloatingActionButton floatingBttn_add = findViewById(R.id.floatingBttn_add);
        floatingBttn_add.setSize(50);
        floatingBttn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "Das ist die Liste bei click auf floating button: " + categories.toString());

                AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add a new category");
                View viewInflated = LayoutInflater.from(MainActivity.this).inflate(R.layout.alert_dialog, (ViewGroup) findViewById(android.R.id.content), false);
                // input setup
                final EditText input = viewInflated.findViewById(R.id.input);
                builder.setView(viewInflated);

                Log.d(TAG, "onClick: FOCUS KEYBOARD");
                //TODO das keyboard sollte nach add group button angezeigt werden => geht aber so nicht!
                input.requestFocus();

                // button setup
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //item liste für neue cat erstellen
                        ArrayList<ShopItem> list = new ArrayList<>();

                        String temp = input.getText().toString();
                        int help = 0;
                        for (int i = 0; i < categories.size(); i++) {
                            if (categories.get(i).getTitle().equals(temp)) {
                                while (categories.get(i).getTitle().equals(temp)) {
                                    help++;
                                    temp = input.getText().toString() + "(" + help + ")";
                                }
                            }
                        }

                        //newCat erstellen mit Name und Liste
                        Category newCat = new Category(temp, list);

                        //Zur Liste der categories hinzufügen
                        categories.add(newCat);

                        //dafür sorgen das der adapter die neue category auch anzeigt
                        adapter.addNewGroup();

                        datachanged();

                        //saves arrayList categories after added new category group
                        arrayListHelper.saveArrayList(categories, "categories_arraylist");

                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    /**
     * Delete all categories.
     */
    private void clearAllCategories() {
        for (int i = 0; i < categories.size(); i++) {
            categories.get(i).getItems().clear();
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Adds standard categories to arrayList.
     */
    private void addStandardCats() {
        for (String STANDARD_CATEGORY : STANDARD_CATEGORIES) {
            ArrayList<ShopItem> list = new ArrayList<>();
            Category cat = new Category(STANDARD_CATEGORY, list);
            categories.add(cat);
//            Log.d(TAG, "addStandardCats: cats = " + categories);
        }

        ShopItem shopItem = new ShopItem("EXAMPLE GROCERY");
        shopItem.setActivity(this);
        shopItem.setIcon();

        categories.get(0).getItems().add(shopItem);

    }

    /**
     * Sorts the arrayList alphabetically.
     */
    private void sortList() {
//        Log.d(TAG, "sortList: VORRRRHEEEEEEEEEEEEERRRRRR arrayList = " + categories);

        Collections.sort(categories, new Comparator<Category>() {
            @Override
            public int compare(Category catLeft, Category catRight) {
                return catLeft.getTitle().compareTo(catRight.getTitle());
            }
        });

//        Log.d(TAG, "sortList: NACHHHEEEEEERRRRRRRR arrayList AFTER SORT = " + categories);
    }

    /**
     * Deletes all shared preferences with name myPrefs.
     */
    public void delete() {
        this.getSharedPreferences("myPrefs", 0).edit().clear().apply();
    }

    /**
     * Adapter notifies data changes and show them.
     */
    public void datachanged() {
        recyclerView.getRecycledViewPool().clear();
        Log.d(TAG, "datachanged: adapter = " + adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * Load shared preferences with name myPrefs.
     */
    private void loadSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        Spinner spinner = findViewById(R.id.spinnerShopItem);
        if (prefs.getString("categories_arraylist", null) != null) {
            categories.clear();
            categories.addAll(arrayListHelper.loadArrayList("categories_arraylist"));
            Log.d(TAG, "SharedPreferences JSON categories: " + categories, null);
            for (int i = 0; i < categories.size(); i++) {
                ArrayList<ShopItem> shopItems;
                //Log.d(TAG, "LOAD shop item lists: "+categories.get(i).getItems());
                //shopItems = (ArrayList<ShopItem>) categories.get(i).getItems();
                Gson gson = new Gson();
                String json = gson.toJson(categories.get(i).getItems());
                shopItems = arrayListHelper.getListFromJson(json);
                for (int j = 0; j < shopItems.size(); j++) {
                    //Log.d(TAG, "SharedPreferences einzelne items nach load shop item list: "+shopItems.get(j).name);
                    shopItems.get(j).setActivity(this);
                    shopItems.get(j).setIcon();
                }
                categories.get(i).getItems().clear();
                categories.get(i).getItems().addAll(shopItems);
                Log.d(TAG, "LOADSHAREDPREFERENCES: shopis: " + shopItems);
            }
        }
    }
}

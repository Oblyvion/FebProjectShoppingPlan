package de.feb.projectshoppingplan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Spannable;
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
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    //TAG logcat
    final static String TAG = "MyActivity";
    //Deklaration Recyclerview
    RecyclerView recyclerView;

    private final ArrayListUtils arrayListHelper = new ArrayListUtils(this);


    private boolean isMultiSelect = false;
    //private ArrayList<Integer> selectedIds = new ArrayList<>();


    // TODO  dummy_items anlegen: ZUGRIFF = categories.addAll(Arrays.asList(context.getResources().getStringArray(R.array.dummy_items)));
    //Categories
    final static String[] STANDARD_CATEGORIES = {"Vegetables", "Sausage & dairy products",
            "Wheat products", "Meat and fish", "Hygiene", "Convenience"};

    ExpandableRecyclerViewAdapter adapter;

    //Main Liste der Categories (enthält alle Categories und die dazu gehörigen ShopItem Listen)
    public ArrayList<Category> categories = new ArrayList<>();

    @Override
    protected void onResume() {
        Log.d(TAG, "MainActivity: On Resume");
        super.onResume();
        loadSharedPreferences();
        datachanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.deleteCats);
        SpannableString spannableString = new SpannableString(item.getTitle().toString());
        spannableString.setSpan(new ForegroundColorSpan(Color.rgb(179, 0, 0)), 0, spannableString.length(), 0);

        item.setTitle(spannableString);
        return true;
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
//        onRestoreInstanceState(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d(TAG, "MainActivity: On Create");

        //recycler view finden
        recyclerView = findViewById(R.id.recyclerViewMain);

//        delete();
        loadSharedPreferences();

        if (categories.isEmpty()) {
            addStandardCats();
        }

        //Adapter wird deklariert und initialisiert
        //Kann erst hier gemacht werden, da in categories was drin sein muss
        adapter = new ExpandableRecyclerViewAdapter(categories);

//        if (adapter != null) {
//            adapter.onRestoreInstanceState(savedInstanceState);
//        } else adapter = new ExpandableRecyclerViewAdapter(categories);
//        adapter = new ExpandableRecyclerViewAdapter(categories);

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
//                int dragAction = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END;
//                Log.d(TAG, "getMovementFlags: dragAction = " + dragAction);
//                int swipeAction = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
//                Log.d(TAG, "getMovementFlags: swipeAction = " + swipeAction);
//                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, ItemTouchHelper.ACTION_STATE_SWIPE);
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder fromViewHolder, @NonNull RecyclerView.ViewHolder toViewHolder) {

                Log.d(TAG, "onMove: itemViewFROM TYPE = " + fromViewHolder.getItemViewType());
                Log.d(TAG, "onMove: itemViewTO TYPE = " + toViewHolder.getItemViewType());

                //do not swap when item types are unequal
                if (fromViewHolder.getItemViewType() != toViewHolder.getItemViewType()) {
                    return false;
                }

                Log.d(TAG, "onMove: adapter POSITION FROM = " + fromViewHolder.getAdapterPosition());
                Log.d(TAG, "onMove: adapter POSITION TO = " + toViewHolder.getAdapterPosition());
                adapter.moveItem(fromViewHolder.getAdapterPosition(), toViewHolder.getAdapterPosition());

                categories = (ArrayList<Category>) adapter.getGroups();
                arrayListHelper.saveArrayList(categories, "categories_arraylist");

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
//                Log.d(TAG, "HALLO HIER SWIPEY SWUPP SWIPE");
//                adapter.notifyItemRemoved(i);
            }
        };

        SwipeController swipeController = new SwipeController();
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        datachanged();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Log.d(TAG, "das ist das menu item: " + menuItem);

                if (!isMultiSelect) {
                    //selectedIds = new ArrayList<>();
                    isMultiSelect = true;
                }

                if (menuItem.toString().equals("Clear Categories")) {

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
                    sortList();
                    arrayListHelper.saveArrayList(categories, "categories_arraylist");
                    datachanged();
                }
                return false;
            }});


            // recyclerView wird itemTouchHelper hinzugefügt
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

            //erster wichtiger save der liste
        arrayListHelper.saveArrayList(categories,"categories_arraylist");

            //Adapter auf den recyclerview setzen
        recyclerView.setAdapter(adapter);

//        datachanged();
//        adapter.notifyDataSetChanged();

        Log.d(TAG,"Das ist die Größe der Category Liste: "+categories.size());

            //Floating button und Alert Dialog für Category Adding
            FloatingActionButton floatingBttn_add = findViewById(R.id.floatingBttn_add);
        floatingBttn_add.setSize(50);
        floatingBttn_add.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View view){

                Log.d(TAG, "Das ist die Liste bei click auf floating button: " + categories.toString());

                AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add a new category");
                View viewInflated = LayoutInflater.from(MainActivity.this).inflate(R.layout.alert_dialog, (ViewGroup) findViewById(android.R.id.content), false);
                // input setup
                final EditText input = viewInflated.findViewById(R.id.input);
                builder.setView(viewInflated);

                // button setup
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //item liste für neue cat erstellen
                        ArrayList<ShopItem> list = new ArrayList<>();

                        //newCat erstellen mit Name und Liste
                        //Category newCat = new Category(input.getText().toString() , list);

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

                        //lässt Kategorie-Erweiterungspfeile verschwinden
//                        showCategoryNotExpandable();

                        //Save der Liste nachdem eine neue Cat hinzugefügt wurde
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

        private void clearAllCategories () {
            for (int i = 0; i < categories.size(); i++) {
                categories.get(i).getItems().clear();
            }
            adapter.notifyDataSetChanged();
        }

        private void addStandardCats () {
            ArrayList<ShopItem> veggie_list = new ArrayList<>();
            Category veggie = new Category(STANDARD_CATEGORIES[0], veggie_list);

            veggie_list.add(new ShopItem("banana"));
            veggie_list.add(new ShopItem("apple"));
            veggie_list.add(new ShopItem("cucumber"));
            veggie_list.add(new ShopItem("apricots"));
            veggie_list.add(new ShopItem("salad"));

            for (int i = 0; i < veggie_list.size(); i++) {
                veggie_list.get(i).setActivity(this);
                veggie_list.get(i).setIcon();
            }

            categories.add(veggie);


            ArrayList<ShopItem> hygienics_list = new ArrayList<>();
            Category hygienics = new Category(STANDARD_CATEGORIES[4], hygienics_list);

            hygienics_list.add(new ShopItem("deonummer1"));
            hygienics_list.add(new ShopItem("toothbrush"));
            hygienics_list.add(new ShopItem("shampoo"));
            hygienics_list.add(new ShopItem("perfume"));
            hygienics_list.add(new ShopItem("soap"));

            for (int i = 0; i < hygienics_list.size(); i++) {
                hygienics_list.get(i).setActivity(this);
                hygienics_list.get(i).setIcon();
            }

            categories.add(hygienics);
        }

        private void sortList () {
            Log.d(TAG, "sortList: arrayList = " + categories);
            Collections.sort(categories, new Comparator<Category>() {
                @Override
                public int compare(Category catLeft, Category catRight) {
                    return catLeft.getTitle().compareTo(catRight.getTitle());
                }
            });
            Log.d(TAG, "sortList: arrayList AFTER SORT = " + categories);


//        ArrayList<Category> arrayList = new ArrayList<>();
//        for (int i = 0; i < categories.size(); i++) {
//            if (categories.get(i).getTitle().toString() > ) {
//
//            }
//        }
//        arrayListHelper.loadArrayList("")
        }

        //TODO TODO
//    private void multiSelect(int position) {
//        ShopItem item = categories.get(position);
//        if (item != null){
//            if (actionMode != null) {
//                if (selectedIds.contains(item.))
//                    selectedIds.remove(Integer.valueOf(item.getId()));
//                else
//                    selectedIds.add(item.getId());
//
//                if (selectedIds.size() > 0)
//                    actionMode.setTitle(String.valueOf(selectedIds.size())); //show selected item count on action mode.
//                else{
//                    actionMode.setTitle(""); //remove item count from action mode.
//                    actionMode.finish(); //hide action mode.
//                }
//                adapter.setSelectedIds(selectedIds);
//
//            }
//        }
//    }

        public void delete () {
            this.getSharedPreferences("myPrefs", 0).edit().clear().apply();
        }

        public void datachanged () {
            recyclerView.getRecycledViewPool().clear();
            Log.d(TAG, "datachanged: adapter = " + adapter);
            adapter.notifyDataSetChanged();
        }

        //wenn category Grösse < 1, dann blende Kategorie-Erweiterungspfeile aus
//    private void showCategoryNotExpandable() {
//        for (Category cat : categories) {
//            ImageView imageViewCatArrow = findViewById(R.id.imageViewCategory);
//            if (cat.getItems().size() < 1) {
//                imageViewCatArrow.setVisibility(View.INVISIBLE);
//            }
//        }
//    }

        private void loadSharedPreferences () {
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

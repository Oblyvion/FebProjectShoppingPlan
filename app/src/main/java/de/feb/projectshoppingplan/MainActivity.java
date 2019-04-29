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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    //TAG logcat
    final static String TAG = "MyActivity";


    private Toolbar toolbar;

    private LinearLayout linLayoutAllCreatedLists;
    private ImageButton imgBttnCreateNewList;

    //Recyclerview declaration
    private RecyclerView recyclerView;

    private Button listBttn0;
    private Button listBttn1;

    private final ArrayListUtils arrayListHelper = new ArrayListUtils();

    //main list (obtain all categories with their shopItems)
    public ArrayList<Category> categories = new ArrayList<>();

    //shared preferences saved lists
    private String LIST_0 = null;
    private String LIST_1 = null;

    //shared preferences list names
    private String listName0 = null;
    private String listName1 = null;

    //shared preferences current list
    public static String key;

    final String[] STANDARD_CATEGORIES = {AppContext.getContext().getString(R.string.standardCat0), AppContext.getContext().getString(R.string.standardCat1),
            AppContext.getContext().getString(R.string.standardCat2), AppContext.getContext().getString(R.string.standardCat3), AppContext.getContext().getString(R.string.standardCat4), AppContext.getContext().getString(R.string.standardCat5)};

    ExpandableRecyclerViewAdapter adapter;


    private InputMethodManager imm;

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
     * @param menu Menu
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
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: HEYYYYYYYYYYYJOJOJO");

                //create dialog
                AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                View viewInflated = LayoutInflater.from(MainActivity.this).inflate(R.layout.alert_dialog,
                        (ViewGroup) findViewById(android.R.id.content), false);

                // input setup
                final EditText input = viewInflated.findViewById(R.id.input);
                input.setHint(getString(R.string.newListNameHint));
                builder.setView(viewInflated);
                builder.setTitle(R.string.newListNameTitle);

                //show keyboard
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                builder.setCancelable(false);

                // button setup
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        SharedPreferences prefsListNames = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefsListNames.edit();

                        if (key.equals(LIST_0)) {
                            listName0 = input.getText().toString();
                            editor.putString("listName0", listName0).apply();

                            //change toolbar title
                            toolbar.setTitle(listName0);
                        } else {
                            listName1 = input.getText().toString();
                            editor.putString("listName1", listName1).apply();

                            //change toolbar title
                            toolbar.setTitle(listName1);
                        }
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: 3: NEGATIVE BUTTON");
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });


        Log.d(TAG, "MainActivity: On Create");

        //linear layout for all created lists of the user
        linLayoutAllCreatedLists = findViewById(R.id.linLayoutHorizontalList);

        //insert listBttn0 into horizontalList
        listBttn0 = new Button(this);
        linLayoutAllCreatedLists.addView(listBttn0);
//        listBttn0.setText();
        listBttn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "This is LIST 1 of 2: " + listName0, Toast.LENGTH_SHORT).show();

                key = LIST_0;

//                Log.d(TAG, "onClick: key = " + key);

                // load standard list 0
                arrayListHelper.loadArrayList(key);
                loadSharedPreferences();
                datachanged();
                adapter.onSwitchLists(key);
                toolbar.setTitle(listName0);
            }
        });

        //insert listBttn1 into horizontalList and set invisible
        listBttn1 = new Button(this);
        linLayoutAllCreatedLists.addView(listBttn1);
        listBttn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "This is LIST 2 of 2: " + listName1, Toast.LENGTH_SHORT).show();

                key = LIST_1;
                arrayListHelper.loadArrayList(key);
                loadSharedPreferences();
                datachanged();
                adapter.onSwitchLists(key);
                toolbar.setTitle(listName1);
            }
        });
        listBttn1.setVisibility(View.INVISIBLE);

        //set standard list
        LIST_0 = getString(R.string.list0_key);

        //save list name 0
        SharedPreferences prefsListNames = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        listName0 = prefsListNames.getString("listName0", null);

        if (listName0 == null) {

            SharedPreferences.Editor editor = prefsListNames.edit();

            editor.putString("listName0", LIST_0).apply();

            //set list name 0
            listName0 = prefsListNames.getString("listName0", null);
        }

        //set second list
        LIST_1 = getString(R.string.list1_key);

        //set current list
        key = LIST_0;

        //set second list name
        if (listName1 == null) {
            SharedPreferences prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
            Log.d(TAG, "onCreate: GET SHARED PREFS FOR LIST_1 = " + prefs.getString("listName1", null));
            listName1 = prefs.getString("listName1", null);
            if (listName1 != null) {
                Log.d(TAG, "onCreate: FUUUUUUUUCK");

                listBttn1.setVisibility(View.VISIBLE);
            }
        }


        Log.d(TAG, "onCreate: KEY = " + key);

        //image button to create a new list
        imgBttnCreateNewList = findViewById(R.id.imageBttnAddNewList);

//        listBttn1 = new Button(this);
//        linLayoutAllCreatedLists.addView(listBttn1);
//        listBttn1.setVisibility(View.INVISIBLE);


        //finden recycler view
        recyclerView = findViewById(R.id.recyclerViewMain);

        //developer is able to RESET ALL
//        delete();

//        toolbar.setTitle("LIST 0");
        setTitle(listName0);

        int idImgAddCategory = getResources().getIdentifier("de.feb.projectshoppingplan:drawable/ic_add_circle_outline_black_40dp", null, null);
        imgBttnCreateNewList.setBackgroundResource(idImgAddCategory);

        //ask for list name and adds the new list to linLayoutAllCreatedLists
        imgBttnCreateNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listName1 == null) {


                    //create dialog
                    AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                    View viewInflated = LayoutInflater.from(MainActivity.this).inflate(R.layout.alert_dialog,
                            (ViewGroup) findViewById(android.R.id.content), false);

                    // input setup
                    final EditText input = viewInflated.findViewById(R.id.input);
                    input.setHint(R.string.hintEditTextNewList);
                    builder.setView(viewInflated);
                    builder.setTitle(R.string.DialogTitleNewList);

                    //show keyboard
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                    builder.setCancelable(false);

                    // button setup
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            Log.d(TAG, "onClick: KEYYYY =      " + key);
                            //TODO SAVE CURRENT MAIN LIST IN SHARED PREFERENCES
                            arrayListHelper.saveArrayList(categories, key);

                            //TODO CLEAR CURRENT MAIN LIST
                            categories.clear();


                            //TODO REPLACE KEY
                            key = LIST_1;

                            //set list name 2
                            listName1 = input.getText().toString();


                            //shared preferences for list names
                            SharedPreferences prefsListName = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editorListName = prefsListName.edit();

                            //TODO SAVE LIST NAME IN SHARED PREFERENCES
                            editorListName.putString("listName1", listName1).apply();


                            //shared preferences for saving lists
                            SharedPreferences prefs = getSharedPreferences("myPrefs" + LIST_0, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();

                            Log.d(TAG, "onClick: KEY = " + key);
                            //save list 2 in shared preferences
                            editor.putString(key, null).apply();

                            //TODO SAVE NEW MAIN LIST IN SHARED PREFERENCES
                            arrayListHelper.saveArrayList(categories, key);

                            //TODO CHANGE TOOLBAR HEADER TITLE TO NEW MAIN LIST NAME
                            //TODO SAVE INPUT TEXT INTO SHARED PREFERENCES
                            toolbar.setTitle(listName1);


                            datachanged();

//                        listBttn1 = new Button(getApplicationContext());
//                        linLayoutAllCreatedLists.addView(listBttn1);

                            listBttn1.setVisibility(View.VISIBLE);
//                        listBttn1.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                key = LIST_1;
//                                Toast.makeText(getApplicationContext(), "This is the current list 2 of 2: " + listName1, Toast.LENGTH_SHORT).show();
//                                arrayListHelper.loadArrayList(key);
//                                loadSharedPreferences();
//                                datachanged();
//                                adapter.onSwitchLists(key);
//                                toolbar.setTitle(listName1);
//                            }
//                        });

                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "onClick: 3: NEGATIVE BUTTON");
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else
                    Toast.makeText(getApplicationContext(), "Maximal list count reached!" + listName1, Toast.LENGTH_LONG).show();

            }
        });


        if (categories.isEmpty()) {
            addStandardCats();
        }

        //Adapter wird deklariert und initialisiert
        //Kann erst hier gemacht werden, da in categories was drin sein muss
        adapter = new ExpandableRecyclerViewAdapter(categories);

        loadSharedPreferences();

        //Soll man machen wenn man weiß das sich die recyclerview elemente nicht ändern (also bezogen auf größe immer gleich bleibend)
//        recyclerView.setHasFixedSize(true);

        //Linearlayout dem recycleview mitgeben soll man so machen damits besser angezeigt wird
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        // drag & drop and swipe
        ItemTouchHelper.Callback itemTouchHelperCallback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            }

            /**
             * @param recyclerView The current recycler view.
             * @param fromViewHolder ViewHolder which is dragged.
             * @param toViewHolder ViewHolder where dropped to.
             * @return true: If items have equal types, than swap them.
             */
            @SuppressWarnings("unchecked")
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder fromViewHolder, @NonNull RecyclerView.ViewHolder toViewHolder) {
//                Log.d(TAG, "onMove: itemViewFROM TYPE = " + fromViewHolder.getItemViewType());
//                Log.d(TAG, "onMove: itemViewTO TYPE = " + toViewHolder.getItemViewType());

                //do not swap when item types are unequal
                if (fromViewHolder.getItemViewType() != toViewHolder.getItemViewType()) {
                    return false;
                }

                Log.d(TAG, "onMove: adapter POSITION FROM = " + fromViewHolder.getAdapterPosition());
                Log.d(TAG, "onMove: adapter POSITION TO = " + toViewHolder.getAdapterPosition());

                //notifies adapter that items are moving
                adapter.moveItem(fromViewHolder.getAdapterPosition(), toViewHolder.getAdapterPosition());

                //saves changes after drag & drop
                categories = (ArrayList<Category>) adapter.getGroups();
                arrayListHelper.saveArrayList(categories, key);

                return true;
            }

            @Override
            public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
                Log.d(TAG, "onMoved: ON MOOVEEEEDDDDDDDDDDDDDDDDDDDDDDD");
//
//                //TODO try: optical feedback after items swapped. Do recyclerView have any highlighting
//                //TODO when item is selected???
//                //highlight on
//                viewHolder.itemView.setSelected(true);
//                new Handler().postDelayed(new Runnable() {
//                    //executes after timeout
//                    @Override
//                    public void run() {
//                    }
//                }, 500);
//                //highlight off
//                viewHolder.itemView.setSelected(false);
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                Log.d(TAG, "onMove: adapter POSITION FROM = " + viewHolder.getAdapterPosition());

                AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.removeItem);
                LayoutInflater.from(MainActivity.this).inflate(R.layout.alert_dialog, (ViewGroup) findViewById(android.R.id.content), false);

                // button setup
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        adapter.swipeItem(viewHolder.getAdapterPosition());

                        //Save der Liste nachdem alle categories gecleared wurden
                        arrayListHelper.saveArrayList(categories, key);


                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        adapter.restoreItem(viewHolder.getAdapterPosition());

                        dialog.cancel();
                    }
                });

                builder.show();
            }
        };

        datachanged();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (menuItem.toString().equals(getResources().getString(R.string.menuitem_clear))) {
                    Log.d(TAG, "das ist das menu item: " + menuItem);

                    AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(R.string.removeAllItems);
                    LayoutInflater.from(MainActivity.this).inflate(R.layout.alert_dialog, (ViewGroup) findViewById(android.R.id.content), false);

                    // button setup
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            clearAllCategories();

                            adapter.onCollapseCategories();

                            //Save der Liste nachdem alle categories gecleared wurden
                            arrayListHelper.saveArrayList(categories, key);


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
                if (menuItem.toString().equals(getResources().getString(R.string.menuitem_sort))) {
                    Log.d("MENUItem", "Menu Item sort");
                    AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(getString(R.string.categoriesSort));
                    LayoutInflater.from(MainActivity.this).inflate(R.layout.alert_dialog, (ViewGroup) findViewById(android.R.id.content), false);

                    // button setup
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            sortList();

                            adapter.onCollapseCategories();
                            datachanged();

                            //Save der Liste nachdem alle categories sortiert wurden
                            arrayListHelper.saveArrayList(categories, key);


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

                if (menuItem.toString().equals(getString(R.string.introOnOff))) {

                    SharedPreferences prefs = getSharedPreferences("myPrefs" + LIST_0, Context.MODE_PRIVATE);

                    AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                    //intro screen is off
                    if (prefs.getInt("splashTimeOut", 1500) != 1500) {

                        Log.d(TAG, "onMenuItemClick: SPLASH TIME OUT OOOOON!");
                        builder.setTitle(R.string.BuilderTitleOn);
                    } else {
                        Log.d(TAG, "onMenuItemClick: SPLASH TIME OUT OOOOOFFFFF!");

                        builder.setTitle(R.string.BuilderTitleOff);
                    }

                    LayoutInflater.from(MainActivity.this).inflate(R.layout.alert_dialog, (ViewGroup) findViewById(android.R.id.content), false);

                    // button setup
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            SharedPreferences prefs = getSharedPreferences("myPrefs" + LIST_0, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();

                            if (prefs.getInt("splashTimeOut", 1500) != 1500)
                                editor.putInt("splashTimeOut", 1500).apply();
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
        arrayListHelper.saveArrayList(categories, key);

        //set adapter to recyclerView
        recyclerView.setAdapter(adapter);

        Log.d(TAG, "Das ist die Größe der Category Liste: " + categories.size());

        //Floating button und Alert Dialog für Category Adding
        FloatingActionButton floatingBttn_add = findViewById(R.id.floatingBttn_add);
        floatingBttn_add.setSize(50);
        floatingBttn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //create dialog
                AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                View viewInflated = LayoutInflater.from(MainActivity.this).inflate(R.layout.alert_dialog, (ViewGroup) findViewById(android.R.id.content), false);

                // input setup
                final EditText input = viewInflated.findViewById(R.id.input);
                input.setHint(R.string.hintEditTextNewCat);
                builder.setView(viewInflated);
                builder.setTitle(R.string.DialogTitleNewCat);

                //show keyboard
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                builder.setCancelable(false);

                // button setup
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        //item liste für neue cat erstellen
                        ArrayList<ShopItem> list = new ArrayList<>();

                        String temp = input.getText().toString();
                        int help = 0;


                        //TODO

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
                        adapter.addNewGroup(key);

//                        datachanged();

                        Log.d(TAG, "onClick FLOATINGBUTTON: KEYYYYYY = " + key);

                        Log.d(TAG, "onClick: ARRAY LIST BEFORE SAVING IN FLOATINGBUTTON = " + categories);

                        //saves arrayList categories after added new category group
                        arrayListHelper.saveArrayList(categories, key);
                        datachanged();

                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: 3: NEGATIVE BUTTON");
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    /**
     * delete all ShopItems from each category
     */
    private void clearAllCategories() {
        for (int i = 0; i < categories.size(); i++) {
            for (int j = 0; j < categories.get(i).getItems().size(); j++) {
                ViewHolderShopI.delete((ShopItem) categories.get(i).getItems().get(j));
            }
            categories.get(i).getItems().clear();
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Adds standard categories to arrayList.
     */
    @SuppressWarnings("unchecked")
    private void addStandardCats() {
        for (String STANDARD_CATEGORY : STANDARD_CATEGORIES) {
            ArrayList<ShopItem> list = new ArrayList<>();
            Category cat = new Category(STANDARD_CATEGORY, list);
            categories.add(cat);
//            Log.d(TAG, "addStandardCats: cats = " + categories);
        }

        ShopItem shopItem = new ShopItem(getString(R.string.exampleGrocery));
        shopItem.setActivity(this);
        shopItem.setIcon();

        categories.get(0).getItems().add(shopItem);

    }

    /**
     * Sorts the arrayList alphabetically.
     */
    private void sortList() {
        Collections.sort(categories, new Comparator<Category>() {
            @Override
            public int compare(Category catLeft, Category catRight) {
                return catLeft.getTitle().compareTo(catRight.getTitle());
            }
        });
        adapter.onCollapseCategories();
    }

    /**
     * Deletes all shared preferences with name myPrefs.
     * these shared preferences are containing the whole shopping list with categories and shop items
     */
    public void delete() {
        this.getSharedPreferences("myPrefs" + LIST_0, 0).edit().clear().apply();
        this.getSharedPreferences("myPrefs", 0).edit().clear().apply();
    }

    /**
     * Adapter notifies data changes and show them.
     */
    public void datachanged() {
        recyclerView.getRecycledViewPool().clear();
        Log.d(TAG, "datachanged: adapter = " + adapter);
        adapter.notifyDataSetChanged();
        adapter.onSwitchLists(key);
    }

    /**
     * Load shared preferences with name myPrefs.
     */
    @SuppressWarnings("unchecked")

    //TODO HAVE TO BE IN A SEPARATED THREAD
    private void loadSharedPreferences() {

        Log.d(TAG, "loadSharedPreferences: KEY = " + key);
        //TODO should be working
//        if (key == null)
//            key = LIST_0;
        Log.d(TAG, "loadSharedPreferences: KEY = " + key);

        SharedPreferences prefs = getSharedPreferences("myPrefs" + key, Context.MODE_PRIVATE);
        if (prefs.getString(key, null) != null) {
            categories.clear();


            Log.d(TAG, "loadSharedPreferences: KEYYYYYYYYY =============  " + key);

            Log.d(TAG, "loadSharedPreferences: WHAT DO YOU ADD = " + arrayListHelper.loadArrayList(key));

            categories.addAll(arrayListHelper.loadArrayList(key));

            Log.d(TAG, "loadSharedPreferences: SIIIIIIIZE = " + categories.size());
            Log.d(TAG, "loadSharedPreferences: ADAPTER SIIIIIIZE = " + adapter.getGroups().size());

            Log.d(TAG, "SharedPreferences JSON categories: " + categories);
            for (int i = 0; i < categories.size(); i++) {

                Log.d(TAG, "loadSharedPreferences: HALLOOOOOOO");

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
            }
        } else {
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putString("key", getString(R.string.list0_key)).apply();
//

            Log.d(TAG, "loadSharedPreferences: HALLLLOOO EEEEEELSSSSSSEEEEEE");

//            key = prefs.getString("key", getString(R.string.list0_key));
//
//            Log.d(TAG, "loadSharedPreferences: KEY ============= " + key);

//            loadSharedPreferences();
        }
    }
}

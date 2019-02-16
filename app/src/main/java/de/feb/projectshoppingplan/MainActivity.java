package de.feb.projectshoppingplan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    //TAG logcat
    final static String TAG = "MyActivity";
    //Deklaration Recyclerview
    RecyclerView recyclerView;
    ItemTouchHelper.Callback itemTouchHelperCallback;
    ItemTouchHelper itemTouchHelper;
    //Categories
    final static String[] STANDARD_CATEGORIES = {"Obst & Gemüse", "Wurst & Milchprodukte",
            "Getreideprodukte", "Fleisch & Fisch", "Hygiene", "Fertiggerichte"};

    ExpandableRecyclerViewAdapter Adapter;

    public ArrayList<Category> categories = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity: On Create");


//        delete();
        SharedPreferences prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        if (prefs.getString("categories_arraylist", null) != null) {
            loadArrayList("categories_arraylist");
            Log.d(TAG, "SharedPreferences JSON categories: "+prefs.getString("categories_arraylist", null));
            for (int i = 0; i < categories.size(); i++) {
                    ArrayList<ShopItem> shopItems;
                    //Log.d(TAG, "LOAD shop item lists: "+categories.get(i).getItems());
                    //shopItems = (ArrayList<ShopItem>) categories.get(i).getItems();
                    Gson gson = new Gson();
                    String json = gson.toJson(categories.get(i).getItems());
                    shopItems = getListFromJson(json);
                    categories.get(i).getItems().clear();
                    categories.get(i).getItems().addAll(shopItems);
                    for (int j = 0; j < shopItems.size(); j++) {
                        //Log.d(TAG, "SharedPreferences einzelne items nach load shop item list: "+shopItems.get(j).name);
                        shopItems.get(j).setActivity(this);
                        shopItems.get(j).setIcon();
                    }
            }
        }

        if(categories.isEmpty()) {
            addStandardCats();
        }

        //Adapter wird deklariert und initialisiert
        //Kann erst hier gemacht werden, da in categories was drin sein muss
        Adapter = new ExpandableRecyclerViewAdapter(categories);


        //recycler view finden
        recyclerView = findViewById(R.id.recyclerViewMain);

        //Soll man machen wenn man weiß das sich die recyclerview elemente nicht ändern (also bezogen auf größe immer gleich bleibend)
        recyclerView.setHasFixedSize(true);

        //Linearlayout dem recycleview mitgeben soll man so machen damits besser angezeigt wird
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        //erster wichtiger save der liste
        saveArrayList(categories, "categories_arraylist");

//        itemTouchHelperCallback = new ItemTouchHelperCallback(adapter);
//        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);

        //Adapter auf den recyclerview setzen
        recyclerView.setAdapter(Adapter);

        datachanged();


        final ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
                int position_source = source.getAdapterPosition();
                int position_target = target.getAdapterPosition();

                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });

        helper.attachToRecyclerView(recyclerView);

        Log.d(TAG, "Das ist die Größe der Category Liste: " + categories.size());

        //Floating button und Alert Dialog für Category Adding
        FloatingActionButton floatingBttn_add  = findViewById(R.id.floatingBttn_add);
        floatingBttn_add.setSize(50);
        floatingBttn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "Das ist die Liste bei click auf floating button: "+categories.toString());


                AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add new category");
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
                        Category newCat = new Category(input.getText().toString(), list);
                        //Zur Liste der categories hinzufügen
                        categories.add(newCat);
                        //dafür sorgen das der adapter die neue category auch anzeigt
                        Adapter.addNewGroup();

                        //Save der Liste nachdem eine neue Cat hinzugefügt wurde
                        saveArrayList(categories, "categories_arraylist");


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

    private void addStandardCats() {
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

    //aus Json string wird wieder eine Arraylist<ShopItem>
    public ArrayList<ShopItem> getListFromJson(String json){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        Gson gson = gsonBuilder.create();
        //TODO MalFormedJsonException lösen wegen leerzeichen
//        JsonReader reader = new JsonReader(new StringReader(json));
//        reader.setLenient(true);
        Type type = new TypeToken<ArrayList<ShopItem>>() {}.getType();
        Log.d(TAG, "getListFromJson: JSON HIER: "+json);

        ArrayList<ShopItem> shopis = gson.fromJson(json, type);

        //Log.d(TAG, "HALLO HIER DIE SHOPITEM LIST IN GETLISTFROMJSON: "+gson.fromJson(json, type));
        //ArrayList<ShopItem> shopis = gson.fromJson(json, type);
        for (int i = 0; i < shopis.size(); i++) {
            Log.d(TAG, "die einzelnen namen der shop items: "+shopis.get(i).name);
        }
        return shopis;
    }

    public void saveArrayList(ArrayList<Category> list, String key){
        SharedPreferences prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public void loadArrayList(String key) {
        //Log.d(TAG, "Categories vor .clear(): "+categories+"\n");
        categories.clear();
        SharedPreferences prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        Gson gson = gsonBuilder.create();

        //Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<Category>>() {}.getType();
        categories = gson.fromJson(json, type);
        //Log.d(TAG, "HALLO HIER DIE CATEGORY LIST IN LOAD: "+categories);
    }

    public void delete() {
        this.getSharedPreferences("myPrefs", 0).edit().clear().apply();
    }

    public void datachanged() {
        recyclerView.getRecycledViewPool().clear();
        Adapter.notifyDataSetChanged();
    }
}

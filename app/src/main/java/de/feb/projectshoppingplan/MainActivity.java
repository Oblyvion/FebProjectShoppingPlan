package de.feb.projectshoppingplan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity: On Create");

        //initialisiere categories mit arraylist
        //Deklaration Array List categories
        ArrayList<Category> categories = new ArrayList<>();

        //recycler view finden
        recyclerView = findViewById(R.id.recyclerViewMain);

        //Soll man machen wenn man weiß das sich die recyclerview elemente nicht ändern
        recyclerView.setHasFixedSize(true);

        //Linearlayout dem recycleview mitgeben soll man so machen damits besser angezeigt wird
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        ArrayList<ShopItem> veggie_list = new ArrayList<>();
        Category veggie = new Category(STANDARD_CATEGORIES[0], veggie_list);

        veggie_list.add(new ShopItem("banana", "", veggie.getTitle()));
        veggie_list.add(new ShopItem("apple", "", veggie.getTitle()));
        veggie_list.add(new ShopItem("cucumber", "", veggie.getTitle()));
        veggie_list.add(new ShopItem("apricots", "", veggie.getTitle()));
        veggie_list.add(new ShopItem("salad", "", veggie.getTitle()));

        for (int i = 0; i < veggie_list.size(); i++) {
            veggie_list.get(i).setActivity(this);
            veggie_list.get(i).setIcon();
        }

        categories.add(veggie);


        ArrayList<ShopItem> hygienics_list = new ArrayList<>();
        Category hygienics = new Category(STANDARD_CATEGORIES[4], hygienics_list);

        hygienics_list.add(new ShopItem("deo", "", hygienics.getTitle()));
        hygienics_list.add(new ShopItem("toothbrush", "", hygienics.getTitle()));
        hygienics_list.add(new ShopItem("shampoo", "", hygienics.getTitle()));
        hygienics_list.add(new ShopItem("perfume", "", hygienics.getTitle()));
        hygienics_list.add(new ShopItem("soap", "", hygienics.getTitle()));

        for (int i = 0; i < hygienics_list.size(); i++) {
            hygienics_list.get(i).setActivity(this);
            hygienics_list.get(i).setIcon();
        }

        categories.add(hygienics);

//        itemTouchHelperCallback = new ItemTouchHelperCallback(adapter);
//        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);

        //Adapter wird deklariert und initialisiert
        //Kann erst hier gemacht werden, da in categories was drin sein muss
        ExpandableRecyclerViewAdapter Adapter = new ExpandableRecyclerViewAdapter(categories);

        //Adapter auf den recyclerview setzen
        recyclerView.setAdapter(Adapter);


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


//        ItemTouchHelperAdapter itemTouchHelperAdapter = new ItemTouchHelperAdapter() {
//            @Override
//            public void onItemMove(int fromPosition, int toPosition) {
//                if (shoppingList.get(fromPosition).getListElementType() == InterfaceListElement.typeCat
//                        && shoppingList.get(toPosition).getListElementType() == InterfaceListElement.typeCat) {
//                    InterfaceListElement temp;
//                    temp = shoppingList.get(fromPosition);
//                    shoppingList.set(fromPosition, shoppingList.get(toPosition));
//                    shoppingList.set(toPosition, temp);
//                }
//            }
//
//            @Override
//            public void onItemDismiss(int position) {
//
//            }
//        };
//
//        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelperCallback(itemTouchHelperAdapter) {
//
//            @Override
//            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
//                return 0;
//            }
//
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder source, @NonNull RecyclerView.ViewHolder target) {
//                int position_source = source.getAdapterPosition();
//                int position_target = target.getAdapterPosition();
//
//                if (shoppingList.get(position_source).getListElementType() == InterfaceListElement.typeCat
//                        && shoppingList.get(position_target).getListElementType() == InterfaceListElement.typeCat) {
//                    Collections.swap(shoppingList, position_source, position_target);
//                }
//
//                adapter.notifyItemMoved(position_source, position_target);
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//
//            }
//        });
//
//        helper.attachToRecyclerView(recyclerView);


        Log.d(TAG, "Das ist die Größe der Category Liste: " + categories.size());

    }

    public void datachanged() {
        //notifyDataSetChanged();
    }
}

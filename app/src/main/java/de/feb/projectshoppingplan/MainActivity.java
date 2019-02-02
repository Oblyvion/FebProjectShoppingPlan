package de.feb.projectshoppingplan;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MyActivity";
    List<InterfaceListElement> shoppingList;
    ListIterator<InterfaceListElement> shoppingListIterator;
    RecyclerView recyclerView;
    ListElementAdapter adapter;
    ItemTouchHelper.Callback itemTouchHelperCallback;
    ItemTouchHelper itemTouchHelper;
    //Categories
    final static String[] STANDARD_CATEGORIES = {"Obst & Gemüse", "Wurst & Milchprodukte",
            "Getreideprokute", "Fleisch & Fisch", "Convenience & Hygiene", "Fertiggerichte"};

    Category cat0, cat1, cat2, cat3, cat4, cat5;

    //möglich innerhalb von 3 Sekunden
    int delay = 3000; //millisekunden


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity: On Create");

        cat0 = new Category(this);
        cat1 = new Category(this);
        cat2 = new Category(this);
        cat3 = new Category(this);
        cat0.setName(STANDARD_CATEGORIES[0]);
        cat1.setName(STANDARD_CATEGORIES[1]);
        cat2.setName(STANDARD_CATEGORIES[2]);
        cat3.setName(STANDARD_CATEGORIES[3]);

        shoppingList = new ArrayList<>();
        shoppingListIterator = shoppingList.listIterator(0);
        //final ImageView imageViewCatGrap;

        recyclerView = findViewById(R.id.recyclerViewMain);
        //Testobjekte erstellen
        //ArrayList<ShopItem> arrayListShopI = new ArrayList<>();
        ShopItem cucumber = new ShopItem("Gurke", "", STANDARD_CATEGORIES[0]);
        ShopItem pork = new ShopItem("Schweinefleisch", "", STANDARD_CATEGORIES[3]);
        ShopItem milk = new ShopItem("Milch", "", STANDARD_CATEGORIES[1]);
        ShopItem flour = new ShopItem("Mehl", "", STANDARD_CATEGORIES[2]);
        ShopItem lemon = new ShopItem("Zitrone", "", STANDARD_CATEGORIES[0]);
        ShopItem bread = new ShopItem("Brot", "", STANDARD_CATEGORIES[2]);

        //Testobjekt testItem braucht Activity
        cucumber.setActivity(this);
        pork.setActivity(this);
        milk.setActivity(this);
        flour.setActivity(this);
        lemon.setActivity(this);
        bread.setActivity(this);

        //Testobjekt tesItem bekommt Icon
        cucumber.setIcon();
        pork.setIcon();
        milk.setIcon();
        flour.setIcon();
        lemon.setIcon();
        bread.setIcon();

        //Objekte zur liste hinzufügen
        shoppingList.add(cat0);
        shoppingList.add(cucumber);
        shoppingList.add(lemon);
        shoppingList.add(cat1);
        shoppingList.add(milk);
        shoppingList.add(cat2);
        shoppingList.add(flour);
        shoppingList.add(bread);
        shoppingList.add(cat3);
        shoppingList.add(pork);

        Log.d(TAG, "...new ListElementAdapter...");
        //Neuen List Adapter erstellen
        adapter = new ListElementAdapter(this, shoppingList, new CategoryElementOnClick() {
            @Override
            public void onItemClick(View v, final int position) {
                Log.d(TAG, "...invoke onItemClick...");
                Drawable.ConstantState constantState = shoppingList.get(position).getDrawable().getConstantState();
                int counter = 0;

                if (constantState != null) {
                    if (shoppingList.get(position).getListElementType() == InterfaceListElement.typeCat
                            && constantState.equals(Objects.requireNonNull(getDrawable(R.drawable.ic_list_black_24dp)).getConstantState())
                            && shoppingList.get(position).getVisibility()) {
                        shoppingList.get(position).setDrawable(getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
                        counter++;
                    } else if (shoppingList.get(position).getListElementType() == InterfaceListElement.typeCat
                            && constantState.equals(Objects.requireNonNull(getDrawable(R.drawable.ic_arrow_drop_up_black_24dp)).getConstantState())) {
                        shoppingList.get(position).setDrawable(getDrawable(R.drawable.ic_list_black_24dp));
                    } else if (shoppingList.get(position).getListElementType() == InterfaceListElement.typeCat
                            && constantState.equals(Objects.requireNonNull(getDrawable(R.drawable.ic_list_black_24dp)).getConstantState())
                            && !shoppingList.get(position).getVisibility()) {
                        shoppingList.get(position).setDrawable(getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
                        counter++;
                    } else if (shoppingList.get(position).getListElementType() == InterfaceListElement.typeCat
                            && constantState.equals(Objects.requireNonNull(getDrawable(R.drawable.ic_arrow_drop_down_black_24dp)).getConstantState())) {
                        shoppingList.get(position).setDrawable(getDrawable(R.drawable.ic_list_black_24dp));
                    }
                }

                if (counter != 0) {
                    timer();
                }

                datachanged();
            }

            @Override
            public void onImageViewCatClick(View v, int position) {
                if (v.getId() == R.id.imageViewCategory) {
                    Drawable.ConstantState constantState = shoppingList.get(position).getDrawable().getConstantState();

                    if (constantState != null && !(constantState.equals(Objects.requireNonNull(getDrawable(R.drawable.ic_list_black_24dp)).getConstantState()))) {
                        Toast.makeText(getApplicationContext(), "ImageView CLICK!", Toast.LENGTH_LONG).show();

                        shoppingList.get(position).setDrawable(getDrawable(R.drawable.ic_list_black_24dp));

                        for (int i = 0; i < shoppingList.size(); i++) {
                            if (shoppingList.get(i).getVisibility()) {
                                //element der liste muss typ shopping item haben und die gleiche kategorie wie der name der category
                                if (shoppingList.get(i).getListElementType() == InterfaceListElement.typeShopItem && shoppingList.get(i).getCategory().equals(shoppingList.get(position).getCategory())) {
                                    shoppingList.get(i).setVisibility(false);
                                }
                            } else {
                                if (shoppingList.get(i).getListElementType() == InterfaceListElement.typeShopItem && shoppingList.get(i).getCategory().equals(shoppingList.get(position).getCategory())) {
                                    shoppingList.get(i).setVisibility(true);
                                }
                            }
                        }
                        shoppingList.get(position).setVisibility(!shoppingList.get(position).getVisibility());
                        datachanged();
                    }
                }
            }

            @Override
            public void onImageViewCatAddClick(View v, int position) {
                if (v.getId() == R.id.imageViewAddCategory) {
                    //Toast.makeText(getApplicationContext(), "Hallo hier plus klick!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), AddShopItemToCategory.class);
                    //position funktioniert hier noch nicht, weil auch die shopItems in der liste eine position haben
                    //brauche hier aber den namen der Category um ihn in die neue activity mitzugeben
                    Log.d(TAG, "MainActivity, onImageViewCatAddClick position: "+ position);
                    intent.putExtra("ShoppingCategory", STANDARD_CATEGORIES[position]);
                    startActivity(intent);
                }
            }
        });

        //Adapter auf den recyclerview setzen
        recyclerView.setAdapter(adapter);

        //Soll man machen wenn man weiß das sich die recyclerview elemente nicht ändern
        recyclerView.setHasFixedSize(true);

        //Linearlayout dem recycleview mitgeben soll man so machen damits besser angezeigt wird
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


//        itemTouchHelperCallback = new ItemTouchHelperCallback(adapter);
//        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);


        final ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
                int position_source = source.getAdapterPosition();
                int position_target = target.getAdapterPosition();

                if (shoppingList.get(position_source).getListElementType() == InterfaceListElement.typeCat
                        && shoppingList.get(position_target).getListElementType() == InterfaceListElement.typeCat) {

                    Collections.swap(shoppingList, position_source, position_target );

                    int product_count_category_from = count_shopitems_category(position_source);
                    int product_count_category_to = count_shopitems_category(position_target);

                    Log.d(TAG, "das hier ist product_count_category_to: " + product_count_category_to);
                    Log.d(TAG, "das hier ist product_count_category_from: " + product_count_category_from);

                    List<InterfaceListElement> tempList = shoppingList;

                    Log.d(TAG, "Das hier ist die Größe von shoppingList: "+shoppingList.size());
                    for (int i = 0; i < shoppingList.size(); i++) {
                        if (shoppingList.get(i).getListElementType() == InterfaceListElement.typeCat) {
                            tempList.add(shoppingList.get(i));
                            for (int j = 0; j < shoppingList.size(); j++) {
                                if (shoppingList.get(j).getListElementType() == InterfaceListElement.typeShopItem
                                        && shoppingList.get(j).getCategory().equals(shoppingList.get(i).getCategory())) {
                                    tempList.add(shoppingList.get(j));
                                }
                            }
                        }
                    }
                    Log.d(TAG, "Das ist die size von Temp LIST: "+tempList.size());
                    shoppingList.clear();
                    shoppingList.addAll(tempList);
                    adapter.notifyItemMoved(position_source, position_target);
                }

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


        Log.d(TAG, "Das ist die Größe der Liste: " + shoppingList.size());

    }

    public void datachanged() {
        adapter.notifyDataSetChanged();
    }

    public void timer() {
        new CountDownTimer(delay, 1000) {
            @Override
            public void onTick(long l) {

            }
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onFinish() {
                for (int i = 0; i < shoppingList.size(); i++) {
                    if (shoppingList.get(i).getListElementType() == InterfaceListElement.typeCat) {
                        shoppingList.get(i).setDrawable(getDrawable(R.drawable.ic_list_black_24dp));
                        datachanged();
                    }
                }
            }
        }.start();
    }

    public int count_shopitems_category(int position) {
        String name_Category = shoppingList.get(position).getName();
        int product_count_category = 0;

        for (int i = 0; i<shoppingList.size(); i++) {
            if (shoppingList.get(i).getListElementType() == InterfaceListElement.typeShopItem
                    && name_Category.equals(shoppingList.get(i).getCategory())) {
                product_count_category++;
            }
        }
        return product_count_category;
    }

}

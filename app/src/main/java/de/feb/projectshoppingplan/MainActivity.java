package de.feb.projectshoppingplan;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MyActivity";
    List<InterfaceListElement> shoppingList;
    RecyclerView recyclerView;
    ListElementAdapter adapter;

    // Categories
    final static String[] STANDARD_CATEGORIES = {"Obst & Gemüse", "Wurst & Milchprodukte",
            "Getreideprokute", "Fleisch & Fisch", "Convenience & Hygiene", "Fertiggerichte"};

    Category cat0, cat1, cat2, cat3, cat4, cat5;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity: On Create");

        cat0 = new Category(this);
        cat1 = new Category(this);
        cat0.setName(STANDARD_CATEGORIES[0]);
        cat1.setName(STANDARD_CATEGORIES[1]);

        shoppingList = new ArrayList<>();
        //final ImageView imageViewCatGrap;

        recyclerView = findViewById(R.id.recyclerViewMain);
        //imageViewCatGrap = findViewById(R.id.imageViewCategory);

        //Testobjekte erstellen
        ShopItem testItem = new ShopItem();

        //Testobjekt testItem braucht Activity
        testItem.setActivity(this);

        //Testobjekt tesItem bekommt Icon
        testItem.setIcon();

        //Objekte zur liste hinzufügen
        shoppingList.add(cat0);
        shoppingList.add(testItem);
        shoppingList.add(testItem);
        shoppingList.add(cat1);

        //Neuen List Adapter erstellen
       adapter  = new ListElementAdapter(this, shoppingList, new CategoryElementOnClick() {
            @Override
            public void onItemClick(View v, int position) {
                //Toast.makeText(getApplicationContext(), "hallo hier category click!", Toast.LENGTH_LONG).show();
//                if(shoppingList.get(position).getDrawable() == getDrawable(R.drawable.ic_list_black_24dp))
//                    shoppingList.get(position).setDrawable(getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
//                else
//                    shoppingList.get(position).setDrawable(getDrawable(R.drawable.ic_list_black_24dp));

                //Macht nicht die Kategorie unsichtbar, sondern ändert den Wert von itemsvisible in der Category class
                //soll also bedeuten: sind die itemsvisible? antwort: nach item press nein damit eingeklappt werden kann wenn auf den
                //image View geklickt wird
                //shoppingList.get(position).setVisibility(false);

                shoppingList.get(position).setDrawable(getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
                datachanged();
            }

           @Override
           public void onImageViewCatClick(View v, int position) {
                if (v.getId() == R.id.imageViewCategory) {
                    Toast.makeText(getApplicationContext(), "ImageView CLICK!", Toast.LENGTH_LONG).show();
                    for (int i = 0; i < shoppingList.size(); i++) {
                        //element der liste muss typ shopping item haben und die gleiche kategorie wie der name der category
                        if (shoppingList.get(i).getListElementType() == InterfaceListElement.typeShopItem && shoppingList.get(i).getCategory().equals(shoppingList.get(position).getCategory())) {
                            shoppingList.get(i).setVisibility(false);
                            Log.d(TAG, "FICKEN DICKEN MICKEN!");
                        }
                    }
                    datachanged();
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


        Log.d(TAG, "Das hier ist der Name der cat0: "+cat0.getName());
        Log.d(TAG, "Das hier ist der Name des TestItems: "+testItem.getName());
        Log.d(TAG, "Das ist die Größe der Liste: "+shoppingList.size());

    }

    public void datachanged() {
        adapter.notifyDataSetChanged();
    }
}

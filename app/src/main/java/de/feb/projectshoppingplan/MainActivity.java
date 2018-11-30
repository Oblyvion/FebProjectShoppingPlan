package de.feb.projectshoppingplan;

import android.os.Build;
import android.os.Handler;
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
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MyActivity";
    List<InterfaceListElement> shoppingList;
    ListIterator<InterfaceListElement> shoppingListIterator;
    RecyclerView recyclerView;
    ListElementAdapter adapter;

    // Categories
    final static String[] STANDARD_CATEGORIES = {"Obst & Gemüse", "Wurst & Milchprodukte",
            "Getreideprokute", "Fleisch & Fisch", "Convenience & Hygiene", "Fertiggerichte"};

    Category cat0, cat1, cat2, cat3, cat4, cat5;

    boolean categoryPressed = false;

    //Handler für Auf- und Zuklappfunktion der Kategorien
    Handler handler;
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
        cat0.setName(STANDARD_CATEGORIES[0]);
        cat1.setName(STANDARD_CATEGORIES[1]);

        shoppingList = new ArrayList<>();
        shoppingListIterator = shoppingList.listIterator(0);
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
        shoppingList.add(cat1);
        shoppingList.add(testItem);

        // TEST: meine Kommentare sich NICHT sichtbar
        Log.d(TAG, "HALLO LOOOOGCAAAATT\nWOOOOO BIST DUUUU????");

        //Neuen List Adapter erstellen
        adapter = new ListElementAdapter(this, shoppingList, new CategoryElementOnClick() {
            @Override
            public void onItemClick(View v, final int position) {
                //Toast.makeText(getApplicationContext(), "hallo hier category click!", Toast.LENGTH_LONG).show();
//                if(shoppingList.get(position).getDrawable() == getDrawable(R.drawable.ic_list_black_24dp))
//                    shoppingList.get(position).setDrawable(getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
//                else
//                    shoppingList.get(position).setDrawable(getDrawable(R.drawable.ic_list_black_24dp));

                // TODO:
                //wenn auf eine Kategorie gedrückt wurde, und die entsprechende Kategorie das
                //Pfeil-nach-oben Symbol hat, dann sollen alle Elemente dieser Kategorie auf
                //enable=false gesetzt werden
                if (!categoryPressed) {
                    // zeige Pfeil nach oben
                    shoppingList.get(position).setDrawable(getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
                    categoryPressed = true;
                    // CRASH: die nächsten auskommentierten Zeilen mit dem handler
                    // schließen die App nach drücken auf eine Kategorie
//                    handler.postDelayed(new Runnable(){
//                        public void run(){
//                            //do something
//                            shoppingList.get(position).setDrawable(getDrawable(R.drawable.ic_list_black_24dp));
//                            handler.postDelayed(this, delay);
//                        }
//                    }, delay);
                } else {
                    // PROBLEM: die nächsten auskommentierten Zeilen funktionieren garnicht
//                    do {
//                        Log.d(TAG, "shopItem = " + shoppingListIterator.next());
//                    } while (shoppingListIterator.hasNext());
                    shoppingList.get(position).setDrawable(getDrawable(R.drawable.ic_list_black_24dp));
                    categoryPressed = false;
                }
                datachanged();
            }
        });

        //Adapter auf den recyclerview setzen
        recyclerView.setAdapter(adapter);

        //Soll man machen wenn man weiß das sich die recyclerview elemente nicht ändern
        recyclerView.setHasFixedSize(true);

        //Linearlayout dem recycleview mitgeben soll man so machen damits besser angezeigt wird
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        Log.d(TAG, "Das hier ist der Name der cat0: " + cat0.getName());
        Log.d(TAG, "Das hier ist der Name des TestItems: " + testItem.getName());
        Log.d(TAG, "Das ist die Größe der Liste: " + shoppingList.size());

    }

    public void datachanged() {
        adapter.notifyDataSetChanged();
    }

}

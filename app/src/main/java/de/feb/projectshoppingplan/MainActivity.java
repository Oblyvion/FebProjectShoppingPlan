package de.feb.projectshoppingplan;

import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
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
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MyActivity";
    List<InterfaceListElement> shoppingList;
    ListIterator<InterfaceListElement> shoppingListIterator;
    RecyclerView recyclerView;
    ListElementAdapter adapter;
    ItemTouchHelper.Callback itemTouchHelperCallback;
    ItemTouchHelper itemTouchHelper;
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

        Log.d(TAG, "...new ListElementAdapter...");
        //Neuen List Adapter erstellen
        adapter = new ListElementAdapter(this, shoppingList, new CategoryElementOnClick() {
            @Override
            public void onItemClick(View v, final int position) {
                Log.d(TAG, "...invoke onItemClick...");

                //Toast.makeText(getApplicationContext(), "hallo hier category click!", Toast.LENGTH_LONG).show();
//                if(shoppingList.get(position).getDrawable() == getDrawable(R.drawable.ic_list_black_24dp))
//                    shoppingList.get(position).setDrawable(getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
//                else
//                    shoppingList.get(position).setDrawable(getDrawable(R.drawable.ic_list_black_24dp));


                //Das hier macht das was du praktisch unten im TODO
                //beschrieben hast. Zumindest haben die ShopItems ihre eigene visible variable die dann false gesetzt wird wenn ihre Kategorie
                // gedrückt wurde. Jedoch hab ich es leider noch nicht hinbekommen, dass
                //das ImageView clickable ist und somit erst dann die einträge visible=false gesetzt werden
                //Kannst dir ja mal unten die onImageViewCatClick anschauen funktioniert aber leider nicht
                //wird trotz klick auf das imageview nicht ausgeführt
                //aber ich bau auch gerne mit dir an deinem handlerteil weiter wollte nur mal pushen weil ich soviel in den classen geändert hab
                //und in den viewholdern und wo auch immer

                for (int i = 0; i < shoppingList.size(); i++) {
                    //element der liste muss typ shopping item haben und die gleiche kategorie wie der name der category
                    Log.d(TAG, "...FOR_i = " + i);
                    Log.d(TAG, "...OUT_IF_shoppingListVisible = " + shoppingList.get(i).getVisibility());
                    if (shoppingList.get(i).getListElementType() == InterfaceListElement.typeShopItem &&
                            shoppingList.get(i).getCategory().equals(shoppingList.get(position).getCategory())) {
                        Log.d(TAG, "...IF0_shoppingListVisible = " + shoppingList.get(i).getVisibility());
                        if (shoppingList.get(i).getVisibility()) {
                            shoppingList.get(i).setVisibility(false);
                            shoppingList.get(position).setDrawable(getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
                            Log.d(TAG, "...IF1_shoppingListVisible = " + shoppingList.get(i).getVisibility());
                        } else if (!shoppingList.get(i).getVisibility()){
                            shoppingList.get(i).setVisibility(true);
                            shoppingList.get(position).setDrawable(getDrawable(R.drawable.ic_list_black_24dp));
                            Log.d(TAG, "...IF2_shoppingListVisible = " + shoppingList.get(i).getVisibility());
                        }
                        break;
                    }
                }
                datachanged();

//=======
//                // TODO:
//                //wenn auf eine Kategorie gedrückt wurde, und die entsprechende Kategorie das
//                //Pfeil-nach-oben Symbol hat, dann sollen alle Elemente dieser Kategorie auf
//                //visibility=false gesetzt werden

                // würde das ob sie enabled sind oder nicht beim objekt lassen damit wir uns die globale variable gespart haben
                // aber das mit dem handler sieht cool aus das können wir aufjeden versuchen einzubauen weiß jedoch auch noch nicht wie das klappt
                // und nach dem einklappen und dem delay soll dann wieder das list symbol kommen das ist auch geil hab ich jedoch oben auch noch nicht drinn


//              if (!categoryPressed) {
//                    // zeige Pfeil nach oben
//                    shoppingList.get(position).setDrawable(getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
//                    categoryPressed = true;
//                    // CRASH: die nächsten auskommentierten Zeilen mit dem handler
//                    // schließen die App nach drücken auf eine Kategorie
////                    handler.postDelayed(new Runnable(){
////                        public void run(){
////                            //do something
////                            shoppingList.get(position).setDrawable(getDrawable(R.drawable.ic_list_black_24dp));
////                            handler.postDelayed(this, delay);
////                        }
////                    }, delay);
//                } else {
//                    // PROBLEM: die nächsten auskommentierten Zeilen funktionieren garnicht
////                    do {
////                        Log.d(TAG, "shopItem = " + shoppingListIterator.next());
////                    } while (shoppingListIterator.hasNext());
//                    shoppingList.get(position).setDrawable(getDrawable(R.drawable.ic_list_black_24dp));
//                    categoryPressed = false;
//                }
//>>>>>>> aee158b6258f713f87c357caa1f4a776c09c2bad
            }

            @Override
            public void onImageViewCatClick(View v, int position) {
                if (v.getId() == R.id.imageViewCategory) {
                    Toast.makeText(getApplicationContext(), "ImageView CLICK!", Toast.LENGTH_LONG).show();
                    for (int i = 0; i < shoppingList.size(); i++) {
                        //element der liste muss typ shopping item haben und die gleiche kategorie wie der name der category
                        if (shoppingList.get(i).getListElementType() == InterfaceListElement.typeShopItem && shoppingList.get(i).getCategory().equals(shoppingList.get(position).getCategory())) {
                            shoppingList.get(i).setVisibility(false);
                        }
                    }
                    datachanged();
                }
            }
        });

        //Adapter auf den recyclerview setzen
        recyclerView.setAdapter(adapter);
        itemTouchHelperCallback = new ItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

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

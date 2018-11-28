package de.feb.projectshoppingplan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MyActivity";
    List<InterfaceListElement> shoppingList;
    RecyclerView recyclerView;

    // Categories
    final static String[] STANDARD_CATEGORIES = {"Obst & Gemüse", "Wurst & Milchprodukte",
            "Getreideprokute", "Fleisch & Fisch", "Convenience & Hygiene", "Fertiggerichte"};

    Category cat0, cat1, cat2, cat3, cat4, cat5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity: On Create");

        cat0 = new Category();
        cat1 = new Category();
        cat0.setName(STANDARD_CATEGORIES[0]);
        cat1.setName(STANDARD_CATEGORIES[1]);

        shoppingList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerViewMain);

        //Testobjekte erstellen
        final Category testCategory = new Category();
        ShopItem testItem = new ShopItem();

        //Testobjekt testItem braucht Activity
        testItem.setActivity(this);

        //Testobjekt tesItem bekommt Icon
        testItem.setIcon();

        //Objekte zur liste hinzufügen
        shoppingList.add(testCategory);
        shoppingList.add(testItem);

        //Neuen List Adapter erstellen
        ListElementAdapter adapter = new ListElementAdapter(this, shoppingList, new CategoryElementOnClick() {
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(getApplicationContext(), "hallo hier category click!", Toast.LENGTH_LONG).show();
                // Hier müssen wir irgendwie das bild ändern wie weiß ich noch nicht genau
                // Also zu drawable ressource pfeil nach unten oder dieses Aufklappding eben du weißt ja was ich mein :D
            }
        });

        //Adapter auf den recyclerview setzen
        recyclerView.setAdapter(adapter);

        recyclerView.setHasFixedSize(true);

        //Linearlayout dem recycleview mitgeben soll man so machen damits besser angezeigt wird
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        Log.d(TAG, "Das hier ist der Name der TestCategory: "+testCategory.getName());
        Log.d(TAG, "Das hier ist der Name des TestItems: "+testItem.getName());
        Log.d(TAG, "Das ist die Größe der Liste: "+shoppingList.size());
    }
}

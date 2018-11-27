package de.feb.projectshoppingplan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    // Test
    TextView textViewTEST;


    final static String TAG = "MyActivity";

    // Categories
    final static String[] STANDARD_CATEGORIES = {"Obst & Gemüse", "Wurst & Milchprodukte",
            "Getreideprokute", "Fleisch & Fisch", "Convenience & Hygiene", "Fertiggerichte"};

    Category cat0, cat1, cat2, cat3, cat4, cat5;
    List<InterfaceListElement> listInterfaceElements;

    RelativeLayout relativeLayout;
    ViewHolder viewHolderCat0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity: On Create");

        cat0 = new Category();
        cat1 = new Category();
        cat0.setName(STANDARD_CATEGORIES[0]);
        cat1.setName(STANDARD_CATEGORIES[1]);
        listInterfaceElements = new ArrayList<>();
        listInterfaceElements.add(cat0);
        listInterfaceElements.add(cat1);
        ListElementAdapter listElementAdapter = new ListElementAdapter(this, listInterfaceElements);

        /* TODO zeige den listElementAdapter an
        *  Warum wird nichts auf dem Display ausgegeben?
        *  auskommentierte Zeile lässt die App crashen!!!
        */
        relativeLayout = findViewById(R.id.relativeLayoutMain);
        viewHolderCat0 = listElementAdapter.onCreateViewHolder(relativeLayout, listElementAdapter.getItemViewType(0));
//        listElementAdapter.onBindViewHolder(viewHolderCat0, 0);

        // Mein Test-Output, weil kein Logcat :/
        textViewTEST = findViewById(R.id.textViewTEST);
        textViewTEST.setText("blaaasjlkg = " + listElementAdapter.getItemCount());
    }
}

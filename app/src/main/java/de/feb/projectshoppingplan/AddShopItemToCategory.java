package de.feb.projectshoppingplan;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class AddShopItemToCategory extends AppCompatActivity {

    private final static String TAG = "MyActivity";

    String temp_user_input;
    EditText editText;
    RecyclerView recyclerView;
    List<InterfaceListElement> itemList_temp;
    ListElementAdapterAddShopItemToCategory adapter;
    ShopItem item;
    Activity searchActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop_item_to_category);

        editText = findViewById(R.id.editText_newShopItem);
        EditText editor = new EditText(this);
        editor.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        recyclerView = findViewById(R.id.recyclerView);
        itemList_temp = new ArrayList<>();
        adapter = new ListElementAdapterAddShopItemToCategory(itemList_temp);
        recyclerView.setAdapter(adapter);
        // use a linear layout manager
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(LayoutManager);
        //make editText respond directly when the activity starts
        editText.requestFocus();

    }
}

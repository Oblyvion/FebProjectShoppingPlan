package de.feb.projectshoppingplan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

class ViewHolderCat extends GroupViewHolder implements View.OnClickListener {
    private final static String TAG = "MyActivity";

    ImageView imageViewCatGrap;
    TextView textViewCat;
    ImageView imageViewCatAdd;
    Context context;

    ViewHolderCat(View view) {
        super(view);

        imageViewCatGrap = view.findViewById(R.id.imageViewCategory);
        textViewCat = view.findViewById(R.id.textViewCategory);
        imageViewCatAdd = view.findViewById(R.id.imageViewAddCategory);
        this.context = view.getContext();

    }

    public void bind(Category category) {
        textViewCat.setText(category.getTitle());
        imageViewCatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "ViewHolderCat Click Add Button");
                Intent intent = new Intent(context,AddShopItemToCategory.class);
                Log.d(TAG, "ViewHolderCat CategoryName: " + textViewCat.getText());
                intent.putExtra("category_name", textViewCat.getText());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        imageViewCatGrap.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        imageViewCatGrap.setAnimation(rotate);
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public void bindType(final InterfaceListElement item) {
//        imageViewCatGrap.setImageDrawable(item.getDrawable());
//        textViewCat.setText(((Category)item).getName());
//        imageViewCatAdd.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_add_circle_24dp));
//    }
//
//    @Override
//    public void onItemSelected() {
//        itemView.setBackgroundColor(itemView.getResources().getColor(R.color.colorPrimaryDark));
//    }
//
//    @Override
//    public void onItemClear() {
//        itemView.setBackgroundColor(0);
//    }
}

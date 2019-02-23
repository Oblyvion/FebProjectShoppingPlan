package de.feb.projectshoppingplan;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

class ViewHolderCat extends GroupViewHolder implements View.OnClickListener {
    private final static String TAG = "MyActivity";

    private ImageView imageViewCatArrow;
    private TextView textViewCat;
    private ImageView imageViewCatAdd;
    private Context context;

    ViewHolderCat(View view) {
        super(view);

        imageViewCatArrow = view.findViewById(R.id.imageViewCategory);
        textViewCat = view.findViewById(R.id.textViewCategory);
        imageViewCatAdd = view.findViewById(R.id.imageViewAddCategory);
        this.context = view.getContext();

    }

    /**
     * build category item
     *
     * @param category
     */
    void bind(final Category category) {
        textViewCat.setText(category.getTitle());

        Log.d("VIEWHOLDERCAT", "BIND: size: " + category.getItems().size());
        if (category.getItems().size() < 1) {
            imageViewCatArrow.setImageDrawable(ContextCompat.getDrawable(imageViewCatArrow.getContext(),R.drawable.leer));
        }
        else imageViewCatArrow.setImageDrawable(ContextCompat.getDrawable(imageViewCatArrow.getContext(), R.drawable.ic_keyboard_arrow_down_black_24dp));

        imageViewCatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "ViewHolderCat Click Add Button");
                Intent intent = new Intent(context, AddShopItemToCategory.class);
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
        imageViewCatArrow.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        imageViewCatArrow.setAnimation(rotate);
    }

//    @Override
//    public void onItemSelected() {
//        itemView.setBackgroundColor(Color.LTGRAY);
//    }
//
//    @Override
//    public void onItemClear() {
//        itemView.setBackgroundColor(0);
//    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public void bindType(final InterfaceListElement item) {
//        imageViewCatArrow.setImageDrawable(item.getDrawable());
//        textViewCat.setText(((Category)item).getName());
//        imageViewCatAdd.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_add_circle_24dp));
//    }
}

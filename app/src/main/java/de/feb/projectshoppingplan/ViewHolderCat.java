package de.feb.projectshoppingplan;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

class ViewHolderCat extends ViewHolder {
    private final static String TAG = "MyActivity";

    ImageView imageViewCatGrap;
    private TextView textViewCat;
    private ImageView imageViewCatAdd;

    ViewHolderCat(View view) {
        super(view);

        imageViewCatGrap = view.findViewById(R.id.imageViewCategory);
        textViewCat = view.findViewById(R.id.textViewCategory);
        imageViewCatAdd = view.findViewById(R.id.imageViewAddCategory);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void bindType(final InterfaceListElement item) {
        imageViewCatGrap.setImageDrawable(item.getDrawable());
        textViewCat.setText(((Category)item).getName());
        imageViewCatAdd.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_add_circle_24dp));
    }
}

package de.feb.projectshoppingplan;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class ViewHolder extends RecyclerView.ViewHolder
        implements ItemTouchHelperViewHolder {

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void bindType(InterfaceListElement item);

}

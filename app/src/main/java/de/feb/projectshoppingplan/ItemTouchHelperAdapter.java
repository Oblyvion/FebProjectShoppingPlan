package de.feb.projectshoppingplan;

public interface ItemTouchHelperAdapter {

    void onItemMove(int formPosition, int toPosition);

    void onItemDismiss(int position);
}

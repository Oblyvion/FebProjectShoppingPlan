package de.feb.projectshoppingplan;

public class shopItem implements InterfaceListElement {

    String name;
    String description;

    @Override
    public int getListElementType() {
        return InterfaceListElement.typeShopItem;
    }

    // https://medium.com/@ruut_j/a-recyclerview-with-multiple-item-types-bce7fbd1d30e

}

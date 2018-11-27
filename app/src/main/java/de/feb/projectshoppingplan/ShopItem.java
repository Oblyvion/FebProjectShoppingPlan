package de.feb.projectshoppingplan;

public class ShopItem implements InterfaceListElement {

    String name;
    String description;

    @Override
    public int getListElementType() {
        return InterfaceListElement.typeShopItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // https://medium.com/@ruut_j/a-recyclerview-with-multiple-item-types-bce7fbd1d30e

}

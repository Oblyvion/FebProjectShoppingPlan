package de.feb.projectshoppingplan;

public class Category  implements InterfaceListElement {

    String name;

    @Override
    public int getListElementType() {
        return InterfaceListElement.typeCat;
    }
}

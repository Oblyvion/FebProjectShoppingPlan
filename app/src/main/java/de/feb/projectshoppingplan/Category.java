package de.feb.projectshoppingplan;

public class Category  implements InterfaceListElement {

    String name;

    Category() {
        this.name = "standard";
    }

    @Override
    public int getListElementType() {
        return InterfaceListElement.typeCat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

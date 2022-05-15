package DataTypes;


public class PieceProduct extends Product {

    PieceProduct() {
        regex = super.regex + " шт";
    }

    public PieceProduct(String name, String description, String maker, double price, int count) {
        super(name, description, maker, price, count);
        regex = super.regex + " шт";
    }

    @Override
    public String toString() {
        return super.toString() + " шт";
    }

    @Override
    PieceProduct parseString(String s) {
        Product father = super.parseString(s);
        if (father == null) return null;
        return new PieceProduct(father.name, father.description, father.maker, father.price, father.count);
    }

    @Override
    public String getEnding() {
        return " шт";
    }
}

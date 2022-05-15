package DataTypes;

public class LiquidProduct extends DoubleProduct {

    LiquidProduct() {
        regex = super.regex + " л";
    }

    public LiquidProduct(String name, String description, String maker, double price, double count) {
        super(name, description, maker, price, count);
        regex = super.regex + " л";
    }

    @Override
    public String toString() {
        return super.toString() + " л";
    }

    @Override
    LiquidProduct parseString(String s) {
        DoubleProduct father = super.parseString(s);
        if (father == null) return null;
        return new LiquidProduct(father.name, father.description, father.maker, father.price, father.count);
    }

    @Override
    public String getEnding() {
        return " л";
    }
}

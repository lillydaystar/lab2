package DataTypes;

public class WeightProduct extends DoubleProduct {

    WeightProduct() {
        regex = super.regex + " кг";
    }

    public WeightProduct(String name, String description, String maker, double price, double count) {
        super(name, description, maker, price, count);
        regex = super.regex + " кг";
    }

    @Override
    public String toString() {
        return super.toString() + " кг";
    }

    @Override
    WeightProduct parseString(String s) {
        DoubleProduct father = super.parseString(s);
        if (father == null) return null;
        return new WeightProduct(father.name, father.description, father.maker, father.price, father.count);
    }

    @Override
    public String getEnding() {
        return " кг";
    }
}

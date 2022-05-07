package DataTypes;

public class WeightProduct extends Product {

    WeightProduct() {
        regex = super.regex + " кг";
    }

    public WeightProduct(String name, String description, String maker, double price, int count) {
        super(name, description, maker, price, count);
        regex = super.regex + " кг";
    }

    @Override
    public String toString() {
        return super.toString() + " кг";
    }

    @Override
    WeightProduct parseString(String s) {
        Product father = super.parseString(s);
        if (father == null) return null;
        return new WeightProduct(father.name, father.description, father.maker, father.price, father.count);
    }

    @Override
    public String getEnding() {
        return " кг";
    }
}

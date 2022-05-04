public class Product {
    String name;
    String description;
    double price;
    String maker;
    String group;
    int count;
    String regex = "([A-Za-zА-ЯІЇЄа-іїє ,]+ / ){3}[1-9][0-9]* грн / [1-9][0-9]*";

    Product(String name, String description, String maker, double price, int count){
        this.name = name;
        this.description = description;
        this.price = price;
        this.maker = maker;
        this.count = count;
    }

    public String toString(){
        return name + " / " + description + " / " + maker + " / " + price + " грн / " + count;
    }

    Product parseString(String s){
        Product pr = null;
        try{
            if(!s.matches(regex))
                throw new Exception("Неправильний формат введення даних!!!");
            String[] temp = s.split(" / ");
            double price = Double.parseDouble(temp[3].substring(0,temp[3].length()-4));
            int count = Integer.parseInt(temp[4].substring(0,temp[4].length()-3));
            pr = new Product(temp[0], temp[1], temp[2], price, count);
        }
        catch (Exception e){
            System.out.println("Неправильний формат введення даних!!!");
        }
        return pr;
    }

}

class LiquidProduct extends Product{

    LiquidProduct(String name, String description, String maker, double price, int count) {
        super(name, description, maker, price, count);
        this.regex = super.regex + " л";
    }

    @Override
    public String toString() {
        return super.toString() + " л";
    }

    @Override
    LiquidProduct parseString(String s) {
        Product father = super.parseString(s);
        return new LiquidProduct(father.name,father.description,father.maker,father.price,father.count);
    }
}

class WeightProduct extends Product{

    WeightProduct(String name, String description, String maker, double price, int count) {
        super(name, description, maker, price, count);
        this.regex = super.regex + " кг";
    }

    @Override
    public String toString() {
        return super.toString() + " кг";
    }

    @Override
    WeightProduct parseString(String s) {
        Product father = super.parseString(s);
        return new WeightProduct(father.name,father.description,father.maker,father.price,father.count);
    }
}

class PieceProduct extends Product{

    PieceProduct(String name, String description, String maker, double price, int count) {
        super(name, description, maker, price, count);
        this.regex = super.regex + " шт";
    }

    @Override
    public String toString() {
        return super.toString() + " шт";
    }

    @Override
    PieceProduct parseString(String s){
        Product father = super.parseString(s);
        return new PieceProduct(father.name,father.description,father.maker,father.price,father.count);
    }
}
public abstract class Product {
    String name;
    String description;
    double price;
    String maker;
    String group;
    int count;

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

    abstract Product parseString(String s);

}

class LiquidProduct extends Product{

    LiquidProduct(String name, String description, String maker, double price, int count) {
        super(name, description, maker, price, count);
    }

    @Override
    public String toString() {
        return name + " / " + description + " / " + maker + " / " + price + " грн / " + count + " л";
    }

    @Override
    Product parseString(String s) {
        Product pr = null;
        if(s.matches("([А-ЯІЇЄа-іїє]+ / ){3}[1-9][0-9]* грн / [1-9][0-9]* л")){
            String[] temp = s.split(" / ");
            double price = Double.parseDouble(temp[3].substring(0,temp[3].length()-4));
            int count = Integer.parseInt(temp[4].substring(0,temp[4].length()-2));
            pr = new LiquidProduct(temp[0], temp[1], temp[2], price, count);
        }
        return pr;
    }
}

class WeightProduct extends Product{

    WeightProduct(String name, String description, String maker, double price, int count) {
        super(name, description, maker, price, count);
    }

    @Override
    public String toString() {
        return name + " / " + description + " / " + maker + " / " + price + " грн / " + count + " кг";
    }

    @Override
    Product parseString(String s) {
        Product pr = null;
        if(s.matches("([А-ЯІЇЄа-іїє]+ / ){3}[1-9][0-9]* грн / [1-9][0-9]* кг")){
            String[] temp = s.split(" / ");
            int count = Integer.parseInt(temp[4].substring(0,temp[4].length()-3));
            double price = Double.parseDouble(temp[3].substring(0,temp[3].length()-4));
            pr = new WeightProduct(temp[0], temp[1], temp[2], price, count);
        }
        return  pr;
    }
}

class PieceProduct extends Product{

    PieceProduct(String name, String description, String maker, double price, int count) {
        super(name, description, maker, price, count);
    }

    @Override
    public String toString() {
        return name + " / " + description + " / " + maker + " / " + price + " грн / " + count + " шт";
    }

    @Override
    Product parseString(String s){
        Product pr = null;
        if(s.matches("([A-Za-zА-ЯІЇЄа-іїє]+ / ){3}[1-9][0-9]*(\\.[0-9]+)? грн / [1-9][0-9]* шт")){
            String[] temp = s.split(" / ");
            double price = Double.parseDouble(temp[3].substring(0,temp[3].length()-4));
            int count = Integer.parseInt(temp[4].substring(0,temp[4].length()-3));
            pr = new PieceProduct(temp[0], temp[1], temp[2], price, count);
        }
        return pr;
    }
}
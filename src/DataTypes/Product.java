package DataTypes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Product {
    String name;
    String description;
    double price;
    String maker;
    String group;
    int count;
    String regex = "([A-Za-zА-ЯІЇЄа-іїє ]+) / ([A-Za-zА-ЯІЇЄа-іїє ,]+) / ([A-Za-zА-ЯІЇЄа-іїє ]+) / (\\d+(.[0-9]+)?) грн / (\\d+)";

    Product(){}

    public Product(String name, String description, String maker, double price, int count){
        this.name = name;
        this.description = description;
        this.price = price;
        this.maker = maker;
        this.count = count;
    }

    public String toString(){
        String newPrice = String.valueOf(price);
        if(newPrice.endsWith(".0")) newPrice = newPrice.substring(0,newPrice.length()-2);
        return name + " / " + description + " / " + maker + " / " + newPrice + " грн / " + count;
    }

    Product parseString(String s){
        Product pr;
        try{
            Pattern pat = Pattern.compile(regex);
            Matcher matcher = pat.matcher(s);
            if(!matcher.matches()) throw new Exception("Неправильний формат введення даних!!!");
            double price = Double.parseDouble(matcher.group(4));
            int count = Integer.parseInt(matcher.group(6));
            pr = new Product(matcher.group(1), matcher.group(2), matcher.group(3), price, count);
        }
        catch (Exception e){
            System.out.println("Неправильний формат введення даних!!!");
            return null;
        }
        return pr;
    }

    static Product parseProduct(String s){
        Product pr = null;
        if(s.endsWith("шт")) pr = new PieceProduct();
        if(s.endsWith("л")) pr = new LiquidProduct();
        if(s.endsWith("кг")) pr = new WeightProduct();
        return pr;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getMaker() {
        return maker;
    }

    public int getCount() {
        return count;
    }

    public String getEnding(){return "";}

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public void setCount(int count) {
        this.count = count;
    }
}


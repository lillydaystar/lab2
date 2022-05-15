package DataTypes;

import Graphical.MainWindow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Product {

    String name;
    String description;
    double price;
    String maker;
    int count;
    String regex = "([\"'A-Za-zА-ЯІЇЄа-іїє ]+) / ([\"'A-Za-zА-ЯІЇЄа-іїє.%\\d ,]+) / ([\"'A-Za-zА-ЯІЇЄа-іїє ]+) / (\\d+(.[0-9]+)?) грн / (\\d+(.[0-9]+)?)";

    Product() {}

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
            if(!matcher.matches()) throw new IllegalFileFormatException("Неправильний формат введення даних!!!");
            if(MainWindow.store.ProductExist(matcher.group(1))) throw new IllegalFileFormatException("Такий товар ("+matcher.group(1)+") уже існує на складі.");
            double price = Double.parseDouble(matcher.group(4));
            double count = Double.parseDouble(matcher.group(6));
            pr = new Product(matcher.group(1), matcher.group(2), matcher.group(3), price, (int) count);
        }
        catch (IllegalFileFormatException e){
            e.printStackTrace();
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

    public double getDCount() {
        return count;
    }

    public void setCount(double count){
        setCount((int) count);
    }

}

class DoubleProduct extends Product{
    double count;

    DoubleProduct(String name, String description, String maker, double price, double count){
        super(name, description, maker, price, (int) count);
        this.count = count;
    }

    DoubleProduct(){}

    @Override
    public void setCount(double count){
        this.count = count;
    }

    @Override
    public double getDCount(){
        return this.count;
    }

    @Override
    DoubleProduct parseString(String s){
        Product father = super.parseString(s);
        Pattern pat = Pattern.compile(regex);
        Matcher matcher = pat.matcher(s);
        if(!matcher.matches()) return null;
        double count = Double.parseDouble(matcher.group(6));
        if (father == null) return null;
        return new DoubleProduct(father.name, father.description, father.maker, father.price, count);
    }

    @Override
    public String toString() {
        String newPrice = String.valueOf(price);
        if(newPrice.endsWith(".0")) newPrice = newPrice.substring(0,newPrice.length()-2);
        String newCount = String.valueOf(count);
        if(newCount.endsWith(".0")) newCount = newCount.substring(0,newCount.length()-2);
        return name + " / " + description + " / " + maker + " / " + newPrice + " грн / " + newCount;
    }
}
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

    Product(String name, String description, String maker, double price, int count){
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
}

class LiquidProduct extends Product{

    LiquidProduct(){
        regex = super.regex + " л";}

    LiquidProduct(String name, String description, String maker, double price, int count) {
        super(name, description, maker, price, count);
        regex = super.regex + " л";
    }

    @Override
    public String toString() {
        return super.toString() + " л";
    }

    @Override
    LiquidProduct parseString(String s) {
        Product father = super.parseString(s);
        if(father == null) return null;
        return new LiquidProduct(father.name,father.description,father.maker,father.price,father.count);
    }
}

class WeightProduct extends Product{

    WeightProduct(){
        regex = super.regex + " кг";}

    WeightProduct(String name, String description, String maker, double price, int count) {
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
        if(father == null) return null;
        return new WeightProduct(father.name,father.description,father.maker,father.price,father.count);
    }
}

class PieceProduct extends Product{

    PieceProduct(){
        regex = super.regex + " шт";}

    PieceProduct(String name, String description, String maker, double price, int count) {
        super(name, description, maker, price, count);
        regex = super.regex + " шт";
    }

    @Override
    public String toString() {
        return super.toString() + " шт";
    }

    @Override
    PieceProduct parseString(String s){
        Product father = super.parseString(s);
        if(father == null) return null;
        return new PieceProduct(father.name,father.description,father.maker,father.price,father.count);
    }
}
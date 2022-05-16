package DataTypes;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Group implements Iterable<Product> {

    //File format:
    //
    //Group '<name>' size <size> description '<...>'
    //...products...

    private final List<Product> products;
    private String name;
    private String description;
    private File file;

//    public DataTypes.Group(String name) {
//        this.name = name;
//    }

    /* This constructor takes File object and reads group of products
     * from it.
     */
    public Group(File path) throws IOException, IllegalFileFormatException {
        this.products = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String nextString = reader.readLine();
        if (nextString == null) throw new IllegalFileFormatException();
        else {
            parseHeader(nextString);
            nextString = reader.readLine();
        }
        while (nextString != null) {
            Product product = Product.parseProduct(nextString);
            if(product != null)
                product = product.parseString(nextString);
            if(product != null) {     //якщо у файлі помилка в написанні групи або група вже існує, то програма поверне null
                this.products.add(product);
                product.setGroup(this);
            }
            nextString = reader.readLine();
        }
        reader.close();
        this.file = newGroupFile(path);
        addProductToFile(products);
    }

    private File newGroupFile(File path){
        return new File("Store//" + path.getName());
    }

    private void addProductToFile(List<Product> products) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write("Group '" + this.name + "' size " + this.getNumberOfProducts() + " description '" + this.description + "'\n");
        for (Product product : products) {
            writer.write(product.toString() + "\n");
        }
        writer.close();
    }

    public void add(Product newProduct) {
        this.products.add(newProduct);
    }

    public int getNumberOfProducts() {
        return this.products.size();
    }

    public Product getProduct(int number) {
        if (number < 0 || number >= this.products.size())
            throw new IllegalArgumentException("No such index "+number+" in group "+this.name);
        return this.products.get(number);
    }

    public List<Product> getProducts(){
        return products;
    }

    public void deleteProduct(Product deletedProduct){
        this.products.remove(deletedProduct);
    }

    public File getFile(){
        return this.file;
    }

    public void setFile(File file){this.file = file;}

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void sortByName(boolean fromAtoZ) {
        this.products.sort((fst, snd) -> compare(fst.getName(), snd.getName())*(fromAtoZ ? 1 : -1));
    }

    public void sortByPrice(boolean fromCheapToExpensive) {
        this.products.sort((fst, snd) -> {
            if (fst.price > snd.price)
                return fromCheapToExpensive ? 1 : -1;
            else if (fst.price < snd.price)
                return fromCheapToExpensive ? -1 : 1;
            else return 0;
        });
    }

    private void parseHeader(String header) throws IllegalFileFormatException {
        Pattern headerPattern = Pattern.compile("Group\\s+'([A-Za-zА-ЯІЇЄа-іїє ]*)'\\s+size\\s+(\\d+)\\s+description\\s+'([A-Za-zА-ЯІЇЄа-іїє\\s]*)'");
        Matcher matcher = headerPattern.matcher(header);
        if (!matcher.matches()) throw new IllegalFileFormatException("Incorrect header format");
        this.name = matcher.group(1);
        this.description = matcher.group(3);
    }

    private static int compare(String fst, String snd) {
        for (int i = 0; i < Math.min(fst.length(), snd.length()); i++) {
            if (fst.charAt(i) < snd.charAt(i))
                return -1;
            else if (fst.charAt(i) > snd.charAt(i))
                return 1;
        }
        if (fst.length() < snd.length())
            return -1;
        if (fst.length() > snd.length())
            return 1;
        return 0;
    }

    public boolean contains(Product argument) {
        for (Product product : this)
            if (product.name.equals(argument.name))
                return true;
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Iterator<Product> iterator() {
        return new Iterator<Product>() {
            int position = 0;
            @Override
            public boolean hasNext() {
                return position < products.size();
            }
            @Override
            public Product next() {
                return products.get(position++);
            }
        };
    }

    public double totalPrice() {
        double price = 0;
        for (Product product : this)
            price += product.price*product.count;
        return price;
    }
}
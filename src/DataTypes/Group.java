package DataTypes;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Group {

    //File format:
    //
    //Group '<name>' size <size> description '<...>'
    //...products...

    private List<Product> products;
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
        this.file = path;
        this.products = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String nextString = reader.readLine();
        int numberOfProducts;
        if (nextString == null) throw new IllegalFileFormatException();
        else {
            numberOfProducts = parseHeader(nextString);
            nextString = reader.readLine();
        }
        while (nextString != null) {
            Product product = Product.parseProduct(nextString);
            product = product.parseString(nextString);
            this.products.add(product);
            nextString = reader.readLine();
        }
        reader.close();
        System.out.println(products);
        System.out.println("Group "+this.name+" number "+this.getNumberOfProducts()+" description "+this.description);
        if (numberOfProducts != this.products.size())
            throw new IllegalFileFormatException("Expected "+numberOfProducts+" products, found "+this.products.size());
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

    private int parseHeader(String header) throws IllegalFileFormatException {
        Pattern headerPattern = Pattern.compile("Group\\s+'([A-Za-z ]*)'\\s+size\\s+(\\d+)\\s+description\\s+'([A-Za-z\\s]*)'");
        Matcher matcher = headerPattern.matcher(header);
        if (!matcher.matches()) throw new IllegalFileFormatException("Incorrect header format");
        this.name = matcher.group(1);
        int numberOfProducts = Integer.parseInt(matcher.group(2));
        this.description = matcher.group(3);
        return numberOfProducts;
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
        if (snd.length() > snd.length())
            return 1;
        return 0;
    }

    @Override
    public String toString() {
        return name;
    }
}
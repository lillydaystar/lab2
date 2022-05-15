package DataTypes;

import java.util.LinkedList;
import java.util.List;

public class Searching {

    /*
     * Search through all the store. As argument takes instance of ProductPattern
     * and pointer to the store. Performs search by searching through all groups
     * and makes LinkedList of matching products.
     */
    public static List<Product> search(Store store, ProductPattern pattern)
            throws IncorrectPriceException, IncorrectCountException {
        LinkedList<Product> list = new LinkedList<>();
        for (Group group : store)
            list.addAll(search(group, pattern));
        return list;
    }

    /*
     * Search through one group only. Takes pointer to this group and instance of
     * ProductPattern. Performs search by just scanning this group and looking
     * for matches between Product and ProductPattern. Returns LinkedList of
     * matching products.
     */
    public static List<Product> search(Group group, ProductPattern pattern)
            throws IncorrectCountException, IncorrectPriceException {
        LinkedList<Product> list = new LinkedList<>();
        for (Product product : group)
            if (satisfies(product, pattern))
                list.add(product);
        return list;
    }

    /*
     * Checks whether Product satisfies ProductPattern by just calling
     * "satisfies" methods described below and checking if all of them
     * are "true".
     */
    private static boolean satisfies(Product product, ProductPattern pattern)
            throws IncorrectPriceException, IncorrectCountException {
        return satisfiesName(product, pattern) && satisfiesQuantity(product, pattern) &&
                satisfiesPrice(product, pattern) && satisfiesMaker(product, pattern) &&
                satisfiesDescription(product, pattern) && satisfiesType(product, pattern);
    }

    private static boolean satisfiesName(Product product, ProductPattern pattern) {
        return pattern.satisfiesName(product.name);
    }

    private static boolean satisfiesPrice(Product product, ProductPattern pattern)
            throws IncorrectPriceException {
        return pattern.satisfiesPrice(product.price);
    }

    private static boolean satisfiesQuantity(Product product, ProductPattern pattern)
            throws IncorrectCountException {
        return pattern.satisfiesCount(product.count);
    }

    private static boolean satisfiesMaker(Product product, ProductPattern pattern) {
        return pattern.satisfiesMaker(product.maker);
    }

    private static boolean satisfiesDescription(Product product, ProductPattern pattern) {
        return pattern.satisfiesDescription(product.description);
    }

    /*
     * If type filter was not set (it is null in ProductPattern class), then
     * this method will return true for all products. If it is set, then it
     * will not be null and this method will filter products by type.
     */
    private static boolean satisfiesType(Product product, ProductPattern pattern) {
        return pattern.satisfiesTypeFilter(product);
    }
}
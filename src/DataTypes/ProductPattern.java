package DataTypes;

public class ProductPattern {

    private String name;
    private String description;
    private String maker;
    private String price;
    private String count;
    private boolean regex;
    private TypeFilter filter;

    public ProductPattern(String name, String description, String maker, String price, String count, boolean regex) {
        this.name = name;
        this.description = description;
        this.maker = maker;
        this.price = price;
        this.count = count;
        this.regex = regex;
    }

    public void addTypeFilter(boolean pieceAllowed, boolean liquidAllowed, boolean weightAllowed) {
        this.filter = new TypeFilter(pieceAllowed, liquidAllowed, weightAllowed);
    }

    boolean satisfiesPrice(double price) throws IncorrectPriceException {
        if (this.price.isEmpty()) return true;
        try {
            double patternPrice = Double.parseDouble(this.price);
            return patternPrice == price;
        } catch (NumberFormatException e) {
            throw new IncorrectPriceException(e.getMessage());
        }
    }

    boolean satisfiesCount(double count) throws IncorrectCountException {
        if (this.count.isEmpty()) return true;
        try {
            double patternCount = Double.parseDouble(this.count);
            return patternCount == count;
        } catch (NumberFormatException e) {
            throw new IncorrectCountException(e.getMessage());
        }
    }

    boolean satisfiesName(String name) {
        if (this.description != null) return true;
        if (this.regex) return this.name.isEmpty() || StringMatch.matches(name, this.name);
        else return name.contains(this.name);
    }

    boolean satisfiesMaker(String maker) {
        return this.regex ?
                (this.maker.isEmpty() || StringMatch.matches(maker, this.maker)) : maker.contains(this.maker);
    }

    boolean satisfiesDescription(String description) {
        if (this.description == null) return true;
        return description.contains(this.description);
    }

    boolean satisfiesTypeFilter(Product product) {
        if (this.filter == null) return true;
        return filter.matches(product);
    }

    private class TypeFilter {
        private boolean pieceAllowed;
        private boolean liquidAllowed;
        private boolean weightAllowed;

        TypeFilter(boolean piece, boolean liquid, boolean weight) {
            this.pieceAllowed = piece;
            this.liquidAllowed = liquid;
            this.weightAllowed = weight;
        }

        boolean matches(Product product) {
            if (product instanceof LiquidProduct)
                return this.liquidAllowed;
            else if (product instanceof WeightProduct)
                return this.weightAllowed;
            else if (product instanceof PieceProduct)
                return this.pieceAllowed;
            return true;
        }

        public String toString() {
            return "piece "+this.pieceAllowed+" liquid "+this.liquidAllowed+" weight "+this.weightAllowed;
        }
    }

    public String toString() {
        return "name "+this.name+" maker "+this.maker+
                " price "+this.price+" count "+this.count+" regex "+this.regex;
    }
}
/**
 * Лабораторна робота №2
 * @author Первушин Кирило і Паращак Лілія
 *
 */
public class Tester {

    public static void main(String[] args) {
        Product pr = new PieceProduct("asd","asd","asd", 45,12);
        System.out.println(pr);
        pr.parseString("Привіт / це / тестувальник / 45 грн / 34 шт");
    }
}

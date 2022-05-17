package DataTypes;

import java.util.Scanner;

public class StringMatch {

    private char[] word;
    private char[] pattern;

    static boolean matches(String word, String pattern) {
        StringMatch matcher = new StringMatch(word, pattern);
        return matcher.match(0, 0);
    }

    private StringMatch(String word, String pattern) {
        this.word = new char[word.length()+1];
        this.pattern = new char[pattern.length()+1];

        for (int i = 0; i < word.length(); i++)
            this.word[i] = word.charAt(i);
        this.word[this.word.length - 1] = 0;

        for (int i = 0; i < pattern.length(); i++)
            this.pattern[i] = pattern.charAt(i);
        this.pattern[this.pattern.length - 1] = 0;
    }

    private boolean match(int wordPosition, int patternPosition) {
        for (;; wordPosition++, patternPosition++) {
            switch (pattern[patternPosition]) {
                case 0: {
                    return word[wordPosition] == 0;
                }
                case '?': {
                    if (word[wordPosition] == 0)
                        return false;
                    break;
                }
                case '*': {
                    for (int i = wordPosition; ; i++) {
                        if (match(i, patternPosition + 1))
                            return true;
                        if (word[i] == 0)
                            return false;
                    }
                }
                default: {
                    if (word[wordPosition] != pattern[patternPosition])
                        return false;
                }
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Scanner scanner = new Scanner(System.in);
            String word = scanner.nextLine();
            String pattern = scanner.nextLine();
            System.out.println(matches(word, pattern));
        }
    }
}
package dot_package;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Main
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        System.out.println(Pattern.matches(".", " "));
        System.out.println(Pattern.matches(".", "1"));
        System.out.println(Pattern.matches(".", "."));
        System.out.println(Pattern.matches(".", "\\"));
        System.out.println(Pattern.matches(".", "`"));
        System.out.println(Pattern.matches(".a", "na"));
        System.out.println(Pattern.matches(".a", "a"));
        System.out.println(Pattern.matches("a.", "ace."));
        System.out.println(Pattern.matches("a...", "acid"));
        System.out.println(Pattern.matches("a..", "accept"));
    }
}


import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        String[] data_list = {"int","byte","double","String"};
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the  Line");
        String[] data = input.nextLine().split(" ");
        check_declaration(data_list,data);
    }
    static void check_declaration(String[] data_list,String data[])
    {
        if(check_type(data_list,data[0]))
        {
            System.out.println("datatype is : "+data[0]);
            System.out.println("Variable name is "+data[1]);
            String last = data[data.length-1];
            int last_index = data[data.length-1].length()-1;
            System. out.println(last_index);
            if(last.charAt(last_index)==';')
                System.out.println("has semi colon");
            else
                System.out.println("does not have semi colon");
        }
        else
        {
            System.out.println("Type is not defined");
        }
    }

    private static boolean check_type(String[] data_list, String word)
    {
        for (String w : data_list)
            if(w.equals(word))
                return true;
        return false;
    }
}

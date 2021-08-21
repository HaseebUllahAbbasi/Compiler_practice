package syntex;


import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.HashMap;

public class Check {

    public static void main(String[] args) {

        //String operators[]={"=","+","-","*","/"};

        Scanner input = new Scanner(System.in);
        String data = input.nextLine();

        StringTokenizer st = new StringTokenizer(data," ");
        HashMap<String, String> symtab = new HashMap<String, String>();

        while (st.hasMoreTokens()) {

            System.out.println(st.nextToken());
        }
        String[] array_1 =data.split(" ");
        int i=1;
        for(String take : array_1)
        {
            int ascii = (int)take.charAt(0);
            if((ascii>=65 && ascii <=90) ||(ascii>=97 && ascii <=122))
                symtab.put(take,"<id,"+(i++)+">");
            else
                symtab.put(take,"<"+take+">");
        }

       // System.out.println("Statement: position = initial + rate * 60 ");
        System.out.println("Lexeme: "+symtab);

        System.out.println("token: "+symtab.get("position"));
        String lele = input.next();
        System.out.println (symtab.get(lele).toString());


    }

}

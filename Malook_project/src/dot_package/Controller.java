package dot_package;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Controller
{
    @FXML
    public TextArea output_screen;
    @FXML
    public TextArea input_screen;
    public void parser(ActionEvent actionEvent) throws Exception {
        try
        {
            Main(input_screen.getText());
        }
        catch(Exception exp)
        {
           // alert = new Alert(Alert.AlertType.ERROR,"Parsing Error", ButtonType.OK);
            //alert.show();
        }
    }
    public  void Main(String line) throws Exception
    {
        FileWriter writer = new FileWriter("input.txt");
        writer.write(line);
        writer.close();

        //Take code input from file
        Scanner reader = new Scanner(new File("input.txt"));

        //ArrayList for lexemes and HashTable for the Symbol Table
        ArrayList<String> lines = new ArrayList<String>();
        Map<String, List<String>> symbolTable = new HashMap<String, List<String>>();

        File file = new File("input.txt");
        String separators[] = {" ",",",";","\n","=","(",")","}","{","."};
        List<String> seperators_array = Arrays.asList(separators.clone());
        // System.out.println(seperators_array);
        //System.out.println(
        // seperators_array);
        try (FileReader fr = new FileReader(file))
        {
            int lookahaed;
            String content="";

            while ((lookahaed = fr.read()) != -1) {
                //System.out.println(String.valueOf((char)lookahaed));
                if((char)lookahaed == '\"')
                {
                    content+=String.valueOf((char)lookahaed);
                    while ((lookahaed = fr.read()) != -1)
                    {
                        content+=String.valueOf((char)lookahaed);
                        if(((char)lookahaed) == '\"' || seperators_array.contains(String.valueOf((char)lookahaed)))
                            break;
                    }

                }
                else {
                    if (seperators_array.contains(String.valueOf((char) lookahaed))) {
                        if ((char) lookahaed == '\n') {
                            content = "";
                            continue;
                        }
                        lines.add(content);
                        if ((char) lookahaed != ' ') {
                            lines.add(String.valueOf((char) lookahaed));
                        }
                        content = "";

                    }
                    else {
                        content += (char) lookahaed;
                    }
                }

            }
        } catch (IOException e)
        {
            String data = output_screen.getText() + "\n";
            output_screen.setText(e.getMessage());
           // e.printStackTrace();
        }

        String Keywords_array[] = {"int","float","String","Boolean","double","char","if","else","void","public","static","return","main",
                "System","out","print","println"};
        //Add values from ArrayList to HashMap Key keywords
        List<String> keywords = new ArrayList<String>();
        for (int i = 0; i < Keywords_array.length; i++) {
            if(lines.contains(Keywords_array[i]))
            {
                int index = 0;
                index = lines.indexOf(Keywords_array[i]);
                keywords.add(lines.get(index));
            }
        }

        //Put the ArrayLists in HashMap for particular Keys
        symbolTable.put("Keywords", keywords);

        String Operators_array[] = {"+","-","*","/","=","=="};
        //Add values from ArrayList to HashMap Key keywords
        List<String> operators = new ArrayList<String>();
        for (int i = 0; i < Operators_array.length; i++) {
            if(lines.contains(Operators_array[i]))
            {
                int index = 0;
                index = lines.indexOf(Operators_array[i]);
                operators.add(lines.get(index));
            }
        }

        //Put the ArrayLists in HashMap for particular Keys
        symbolTable.put("Math Operators", operators);

        String logical_operators[] = {">","<",">=","<=","&","&&","!=","!"};
        //Add values from ArrayList to HashMap Key keywords
        List<String> logical = new ArrayList<String>();
        for (int i = 0; i < logical_operators.length; i++) {
            if(lines.contains(logical_operators[i]))
            {
                int index = 0;
                index = lines.indexOf(logical_operators[i]);
                logical.add(lines.get(index));
            }
        }


        //Put the ArrayLists in HashMap for particular Keys
        symbolTable.put("Logical|relational Operators ", logical);

        //Convert the array list into an array
        String [] linesArray = lines.toArray(new String [0]);

        //Add values from ArrayList to HashMap Key keywords
        List<String> digits = new ArrayList<String>();
        for (int count = 0;  count < linesArray.length; count++) {
            if (linesArray[count].matches("\\d+|\\d+\\.\\d+")) {  //Use regex here for numbers
                digits.add(linesArray[count]);
            }
        }

        //Put the ArrayLists in HashMap for particular Keys
        symbolTable.put("Numerical Values", digits);

        List<String> keywords_arr = Arrays.asList(Keywords_array.clone());
        //Add values from ArrayList to HashMap Key keywords
        List<String> identifiers = new ArrayList<String>();
        for (int count = 0;  count < linesArray.length; count++) {
            if (linesArray[count].matches("\\w+") && !linesArray[count].matches("\\d+") && !keywords_arr.contains(linesArray[count])) {  //Use regex here for variables
                if (!identifiers.contains(linesArray[count])) {
                    identifiers.add(linesArray[count]);
                }
            }
        }

        //Put the ArrayLists in HashMap for particular Keys
        symbolTable.put("Identifiers", identifiers);

        //Add values from ArrayList to HashMap Key keywords

        List<String> others = new ArrayList<String>();

        for (int count = 0;  count < linesArray.length; count++) {

            if (linesArray[count].matches("\\(|\\)|\\{|\\}|\\,|\\;")) {  //Use regex here for variables
                if (!identifiers.contains(linesArray[count])) {
                    others.add(linesArray[count]);
                }

            }
            if(linesArray[count].matches("((?<![\\\\])['\"])((?:.(?!(?<![\\\\])\\1))*.?)\\1"))
            {
                others.add((linesArray[count]));
            }
        }

        //Put the ArrayLists in HashMap for particular Keys

        symbolTable.put("Others", others);

        Iterator itr = lines.iterator();
        while (itr.hasNext())
        {
            String x = (String) itr.next();
            if (x  == "")
                itr.remove();
        }
        // lines.remove(" ");
        //Prints the ArrayList
       // System.out.print("The Lexemes: ");
        //System.out.println(lines);

        //System.out.println();

        String data = output_screen.getText() + "\n"+
        ("The Lexemes: ");
        data+=(lines) +" \n";

        output_screen.setText(data);

        //Prints the Symbol Table
        output_screen.setText("");
        String builder = "";
        for (Map.Entry<String, List<String>> entry : symbolTable.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            builder+=key+" : ";

            System.out.print(key + ": ");
            System.out.println(values);
            builder+=values+"\n";

        }
        output_screen.setText(builder);
        lines.remove(" ");
         System.out.println(lines);
        checkSyntax(lines);
    }

    public  void checkSyntax(ArrayList<String> lexemes) throws Exception {

        String[] accessModifiers = {"public","private","protected"};
        List<String> access_Modifiers = Arrays.asList(accessModifiers.clone());

        if(access_Modifiers.contains(lexemes.get(0)))
        {

            if(lexemes.get(1).equals("static"))
            {

                if(lexemes.get(2).equals("void") || lexemes.get(2).equals("int") || lexemes.get(2).equals("float") || lexemes.get(2).equals("double") || lexemes.get(2).equals("boolean") || lexemes.get(2).equals("char"))
                {

                    if(lexemes.get(3).matches("^[a-zA-Z_$][a-zA-Z_$0-9]*$")) {
                        if (lexemes.get(4).equals("(")) {
                            int count = 5;
                            while (!lexemes.get(count).equals(")")) {
                                System.out.println(lexemes.get(count));
                                ArrayList<String> exp = new ArrayList<>();
                                //count++;
                                while (!(lexemes.get(count).equals(",")) && !lexemes.get(count).equals(")")) {

                                    System.out.println(lexemes.get(count) + " " + count);
                                    exp.add(lexemes.get(count));
                                    count++;
                                }
                                exp.add(lexemes.get(count));
                                boolean flag = checkExpression(exp,true);
                                if (!flag) {
                                    break;
                                }
                                System.out.println(lexemes.get(count));

                            }
                            count++;

                            checkBody(lexemes,count);
                        }

                    }
                    else {
                        alert = new Alert(Alert.AlertType.ERROR,"Invalid Method Name", ButtonType.OK);
                        alert.show();throw new Exception();
                        ////System.exit(0);
                    }
                }
                else
                {
                    alert = new Alert(Alert.AlertType.ERROR,"Invalid return Type", ButtonType.OK);
                    alert.show();throw new Exception();                }

            }
            else
            {
                if(lexemes.get(1).equals("void") || lexemes.get(1).equals("int") || lexemes.get(1).equals("float") || lexemes.get(1).equals("double") || lexemes.get(1).equals("boolean") || lexemes.get(1).equals("char"))
                {

                    if(lexemes.get(2).matches("^[a-zA-Z_$][a-zA-Z_$0-9]*$")) {
                        if (lexemes.get(3).equals("(")) {
                            int count = 4;
                            while (!lexemes.get(count).equals(")")) {

                                ArrayList<String> exp = new ArrayList<>();
                                //count++;
                                while (!(lexemes.get(count).equals(",")) && !lexemes.get(count).equals(")")) {

                                    exp.add(lexemes.get(count));
                                    count++;
                                }
                                exp.add(lexemes.get(count));
                                boolean flag = checkExpression(exp,true);
                                if (!flag) {
//                                    //System.exit(0);
                                }

                            }
                            count++;

                            checkBody(lexemes,count);
                        }

                    }
                    else
                    {

                        alert = new Alert(Alert.AlertType.ERROR,"Invalid Method Name", ButtonType.OK);
                        alert.show();throw new Exception();//                        //System.exit(0);
                    }
                }
                else {
                    alert = new Alert(Alert.AlertType.ERROR,"Invalid return Type", ButtonType.OK);
                    alert.show();throw new Exception();
//
//                    //System.exit(0);
                }
            }
        }
        else
        {
            if(lexemes.get(0).equals("static"))
            {

                if(lexemes.get(1).equals("void") || lexemes.get(1).equals("int") || lexemes.get(1).equals("float") || lexemes.get(1).equals("double") || lexemes.get(1).equals("boolean") || lexemes.get(1).equals("char"))
                {

                    if(lexemes.get(2).matches("^[a-zA-Z_$][a-zA-Z_$0-9]*$")) {
                        if (lexemes.get(3).equals("(")) {
                            int count = 4;
                            while (!lexemes.get(count).equals(")")) {

                                ArrayList<String> exp = new ArrayList<>();
                                //count++;
                                while (!(lexemes.get(count).equals(",")) && !lexemes.get(count).equals(")")) {

                                    exp.add(lexemes.get(count));
                                    count++;
                                }
                                exp.add(lexemes.get(count));
                                boolean flag = checkExpression(exp,true);
                                if (!flag) {
//                                    //System.exit(0);
                                }

                            }
                            count++;
                            checkBody(lexemes,count);
                        }

                    }
                    else {
                        alert = new Alert(Alert.AlertType.ERROR,"Invalid Method Name", ButtonType.OK);
                        alert.show();throw new Exception();
                    }
                }
                else
                { alert = new Alert(Alert.AlertType.ERROR,"Invalid Return Type", ButtonType.OK);
                    alert.show();throw new Exception();
                }
            }
            else
            {
                if(lexemes.get(0).equals("void") || lexemes.get(0).equals("int") || lexemes.get(0).equals("float") || lexemes.get(0).equals("double") || lexemes.get(0).equals("boolean") || lexemes.get(0).equals("char"))
                {

                    if(lexemes.get(1).matches("^[a-zA-Z_$][a-zA-Z_$0-9]*$")) {
                        if (lexemes.get(2).equals("(")) {
                            int count = 3;
                            while (!lexemes.get(count).equals(")")) {

                                ArrayList<String> exp = new ArrayList<>();
                                //count++;
                                while (!(lexemes.get(count).equals(",")) && !lexemes.get(count).equals(")")) {

                                    exp.add(lexemes.get(count));
                                    count++;
                                }
                                exp.add(lexemes.get(count));
                                boolean flag = checkExpression(exp,true);
                                if (!flag) {
                                  break;
                                }

                            }
                           // System.out.println(lexemes);
                            count++;
                            checkBody(lexemes,count);
                        }
                        else
                        {

                            alert = new Alert(Alert.AlertType.ERROR,"Invalid Method Declaration", ButtonType.OK);
                            alert.show();throw new Exception();                        }

                    }
                    else {
                        alert = new Alert(Alert.AlertType.ERROR,"Invalid Method Name", ButtonType.OK);
                        alert.show();throw new Exception();                        //System.exit(0);
                    }
                }
                else {
                    alert = new Alert(Alert.AlertType.ERROR,"Invalid Return Type", ButtonType.OK);
                    alert.show();throw new Exception();
                }
            }
        }

    }
    public static boolean checkExpression(ArrayList<String> lexemes,boolean arg) throws Exception {

        System.out.println(lexemes);
        String dataTypes[] = {"int", "float", "String", "boolean", "char", "double"};
        boolean flag = false;
        int count = 0;


        for (int i = 0; i < dataTypes.length; i++) {
            if (lexemes.get(count).equals(dataTypes[i])) {
                flag = true;
            }
        }
        if (flag) {
            count++;

            if (arg) {
                if (lexemes.get(count).matches("^[a-zA-Z_$][a-zA-Z_$0-9]*$")) {
                    count++;
                    if (lexemes.get(count).equals(",") || lexemes.get(count).equals(")")) {
                        return true;
                    } else {
                        alert = new Alert(Alert.AlertType.ERROR,"Comma Missing", ButtonType.OK);
                        alert.show();throw new Exception();                        //System.exit(0);
                    }
                }
                else {

                    alert = new Alert(Alert.AlertType.ERROR,"Invalid variable declaration", ButtonType.OK);
                    alert.show();throw new Exception();

                    //System.exit(0);
                }
            } else {
                if (lexemes.contains(";")) {
                    while (!lexemes.get(count).equals(";")) {
                        if (lexemes.get(count).matches("^[a-zA-Z_$][a-zA-Z_$0-9]*$")) {
                            count++;

                            if (lexemes.get(count).equals(",") || lexemes.get(count).equals(";")) {
                                if(lexemes.get(count).equals(","))
                                    count++;
                            } else {
                                alert = new Alert(Alert.AlertType.ERROR,"Comma Missing", ButtonType.OK);
                                alert.show();
                                return false;
                            }
                        } else {

                            alert = new Alert(Alert.AlertType.ERROR,"Invalid variable declaration " + lexemes.get(count), ButtonType.OK);
                            alert.show();throw new Exception();//                            System.out.println("Invalid variable declaration " + lexemes.get(count));
                            //System.exit(0);
                        }

                    }
                    return true;
                } else {
                    alert = new Alert(Alert.AlertType.ERROR,"Error!: SemiColon Missing", ButtonType.OK);
                    alert.show();throw new Exception();
                }
            }

        }
        else {
            alert = new Alert(Alert.AlertType.ERROR,"Error! Data Type missing", ButtonType.OK);
            alert.show();
        }
        return false;

    }

    public static void checkBody(ArrayList<String> lexemes,int count) throws Exception {
System.out.println(lexemes.get(count));
        if (lexemes.get(count).equals("{")) {
            count++;
            while (!lexemes.get(count).equals("}")) {

                if (lexemes.get(count).equals("System") || lexemes.get(count).equals("system")) {
                    if (lexemes.get(count).equals("system")) {
                        System.out.println("Error: system is not defined!.");
                        alert = new Alert(Alert.AlertType.ERROR,"Error: system is not defined!.", ButtonType.OK);
                        alert.show();throw new Exception();                    } else {
                        //code for checking syntax of System.out.println()
                        if (lexemes.get(++count).equals(".")) {
                            if (lexemes.get(++count).equals("out")) {
                                if (lexemes.get(++count).equals(".")) {
                                    count++;
                                    if (lexemes.get(count).equals("print") || lexemes.get(count).equals("println")) {
                                        if (lexemes.get(++count).equals("(")) {
                                            boolean flag=checkArgument(lexemes.get(++count), lexemes);
                                            if(flag)
                                            {
                                                count++;
                                                if(lexemes.get(count).equals(")"))
                                                {
                                                    count++;
                                                    if(lexemes.get(count).equals(";"))
                                                    {

                                                    }
                                                    else {

                                                        alert = new Alert(Alert.AlertType.ERROR,"Missing Semicolon !", ButtonType.OK);
                                                        alert.show();throw new Exception();                                                        //System.exit(0);
                                                    }
                                                }
                                                else {
                                                    //System.exit(0);

                                                    alert = new Alert(Alert.AlertType.ERROR,"Missing closing bracket", ButtonType.OK);
                                                    alert.show();throw new Exception();                                                }
                                            }
                                        } else {
//                                            System.out.println("Error! at " + lexemes.get(count));
                                            //System.exit(0);
                                            alert = new Alert(Alert.AlertType.ERROR,("Error! at " + lexemes.get(count)), ButtonType.OK);
                                            alert.show();throw new Exception();                                        }
                                    } else {
                                        alert = new Alert(Alert.AlertType.ERROR,("Error! at " + lexemes.get(count)), ButtonType.OK);
                                        alert.show();throw new Exception();
                                    }
                                } else {
                                    alert = new Alert(Alert.AlertType.ERROR,("Error! at " + lexemes.get(count)), ButtonType.OK);
                                    alert.show();throw new Exception();                                }
                            } else {
                                alert = new Alert(Alert.AlertType.ERROR,("Error! at " + lexemes.get(count)), ButtonType.OK);
                                alert.show();throw new Exception();                            }
                        } else {
                            alert = new Alert(Alert.AlertType.ERROR,("Error! at " + lexemes.get(count)), ButtonType.OK);
                            alert.show();throw new Exception();                        }
                    }

                } else {
        System.out.println("hello");
                    ArrayList<String> exp = new ArrayList<>();
                    while (!(lexemes.get(count).equals(";")) && !lexemes.get(count).equals("}")) {

                        exp.add(lexemes.get(count));
                        count++;
                    }
                    exp.add(lexemes.get(count));
                    boolean flag = checkExpression(exp,false);
                    if (!flag) {
                        //System.exit(0);
                    }

                }
                count++;
            }
            System.out.println("hi");
            if(lexemes.get(count).equals("}"))
            {
                System.out.println("Compiled successfully!");
                alert = new Alert(Alert.AlertType.CONFIRMATION,"Compiled Successfully", ButtonType.OK);
                alert.show();throw new Exception();            }

        }


    }
    static Alert  alert;
    public static boolean checkArgument(String lexeme,ArrayList<String> lexemes)
    {
        String dataTypes[] = {"int","float","String","boolean","char","double"};
        List<String> dataTypesList = Arrays.asList(dataTypes.clone());
        if(lexeme.startsWith("\""))
        {
            if(lexeme.endsWith("\""))
            {

                return true;

            }
            else
            {
                alert = new Alert(Alert.AlertType.ERROR,"Error!: Enclosing quotation mark missing", ButtonType.OK);
                alert.show();
                return false;
            }
        }
        else
        {
            int index = lexemes.indexOf(lexeme);
            if(dataTypesList.contains(lexemes.get(index-1)))
            {
                return true;
            }
            else
            {
                alert = new Alert(Alert.AlertType.ERROR,(lexeme + " Identifier not defined"), ButtonType.OK);
                alert.show();
                //System.exit(0);
                return false;
            }
        }
    }
}

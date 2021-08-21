package dot_compiler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;

public class Controller
{
    @FXML
    public Label file_name;
    @FXML
    public TextArea output_screen;

    private Stage primaryStage;

    public void load_json(ActionEvent actionEvent)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.json")
        );
        selectedFile = fileChooser.showOpenDialog(primaryStage);
        if(selectedFile!=null)
        {
            System.out.println("selected file is assigned value");
            path+= (selectedFile.getAbsolutePath());
            file_name.setText(selectedFile.getName());
        }
    }
    public void setStage(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
    }

    String path = "";
    File selectedFile;
    public void load_xml(ActionEvent actionEvent)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.xml")
        );
        selectedFile = fileChooser.showOpenDialog(primaryStage);
        if(selectedFile!=null)
        {
            System.out.println("selected file is assigned value");
            path+= (selectedFile.getAbsolutePath());
            file_name.setText(selectedFile.getName());
        }
    }

    public void show_data(ActionEvent actionEvent) throws FileNotFoundException
    {
        Scanner scanner = new Scanner(selectedFile);
        StringBuilder data = new StringBuilder();
        output_screen.setText("");
        while (scanner.hasNext())
        {
            data.append(scanner.nextLine()+"\n");
        }
        output_screen.setText(data.toString());
    }


    /*
      Code for Parser
     */

    public Hashtable rootTable;
    public Vector rootVector;

    public Stack objectStack = new Stack();
    public Stack keyStack = new Stack();
    boolean afterColon=false;

    boolean quoteOn=false;
    boolean escapeNext=false;
    StringBuffer buff = new StringBuffer();
    StringBuffer hexBuff = new StringBuffer();

    int openSquareBracketCount=0;
    int openCurlyBracketCount=0;
    int fourDigitHexCodeCount=-1;

    public void addToBuff(char c) {
        buff.append(c);
    }

    public void handleQuoteOn(char c) {

        if (fourDigitHexCodeCount > -1) {
            hexBuff.append(c);
            fourDigitHexCodeCount++;

            if (fourDigitHexCodeCount == 4) {
                addToBuff((char)Integer.parseInt(hexBuff.toString(), 16));
                fourDigitHexCodeCount = -1;
            }
            return;
        }

        switch (c) {
            case '"':
                if (escapeNext) {
                    escapeNext = false;
                    addToBuff(c);
                }
                else {
                    quoteOn = false;
                }
                break;
            case '\\':
                if (escapeNext) {
                    escapeNext = false;
                    addToBuff(c);
                }
                else
                    escapeNext = true;
                break;
            case 'u':
                if (escapeNext) {
                    fourDigitHexCodeCount = 0;
                    hexBuff.setLength(0);
                    escapeNext = false;
                } else
                    addToBuff(c);
                break;
            case 'n':
                if (escapeNext) {
                    addToBuff('\n');
                    escapeNext = false;
                } else
                    addToBuff(c);
                break;
            default:
                addToBuff(c);
                escapeNext = false;
        }
    }

    public void handleQuoteOff(char c) {
        switch (c) {
            case '"':
                quoteOn = true;
                break;
            case '{':
                Hashtable table = new Hashtable();
                if (rootVector == null && rootTable == null)
                    rootTable = table;
                objectStack.push(table);
                afterColon=false;
                break;
            case '}':
                table = (Hashtable)objectStack.pop();

                if (afterColon)
                    table.put(keyStack.pop(), buff.toString());
                buff.setLength(0);

                if (!objectStack.empty()) {
                    Object object = objectStack.peek();
                    if (object instanceof Hashtable)
                        ((Hashtable)object).put(keyStack.pop(), table);
                    else
                        ((Vector)object).addElement(table);
                }
                afterColon=false;
                break;
            case '[':
                Vector vector = new Vector();
                if (rootVector == null && rootTable == null)
                    rootVector = vector;
                objectStack.push(vector);
                afterColon = true;
                break;
            case ']':
                vector = (Vector)objectStack.pop();

                if (buff.length() > 0)
                    vector.addElement(buff.toString());
                buff.setLength(0);

                if (!objectStack.empty()) {
                    Object object = objectStack.peek();
                    if (object instanceof Hashtable)
                        ((Hashtable)object).put(keyStack.pop(), vector);
                    else
                        ((Vector)object).addElement(vector);
                }
                afterColon = false;
                break;
            case ':':
                keyStack.push(buff.toString());
                buff.setLength(0);
                afterColon = true;
                break;
            case ',':
                if (!afterColon)
                    return;

                Object object = objectStack.peek();
                if (object instanceof Hashtable)
                    ((Hashtable)object).put(keyStack.pop(), buff.toString());
                else
                    ((Vector)object).addElement(buff.toString());

                buff.setLength(0);
                break;
            case ' ':
                break;
            case '\t':
                break;
            case '\n':
                break;
            case '\r':
                break;
            default:
                addToBuff(c);
        }
    }

    public void handleStream(InputStream is) throws Exception {
        try {
            int ch;
            while ((ch = is.read()) != -1) {
                char c = (char)ch;
                if (quoteOn)
                    handleQuoteOn(c);
                else
                    handleQuoteOff(c);
            }
        }
        finally {
            is.close();
        }
    }

    public void start(File file) throws Exception
    {
        Controller jp = new Controller();
        jp.handleStream(new FileInputStream(file));
      //  System.out.println(jp.rootVector+"");
        System.out.println(jp.rootTable+"");
//        output_screen.setText(jp.rootTable.toString());
    }

    Alert alert;
    public void parse_data(ActionEvent actionEvent)
    {
        try
        {
            start(selectedFile);

            alert = new Alert(Alert.AlertType.INFORMATION," Parsing Successful", ButtonType.OK);
            alert.showAndWait();
            show_data(actionEvent);

        }
        catch (Exception exp)
        {
            alert = new Alert(Alert.AlertType.ERROR," Parsing Error ", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void about_screen(ActionEvent actionEvent) throws IOException {

        ((Node)actionEvent.getSource()).getScene().getWindow().hide();

        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("about.fxml").openStream());

        primaryStage.setTitle("About");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}

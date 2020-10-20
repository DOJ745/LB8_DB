package by.bstu.faa.lb8db;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static int MAX_TASKS = 20;
    public static int MAX_CATEGORIES = 5;
    public static String LOG_FILE = "log_file";
    public static String LOG_TASK = "log_task";

    public static String FILENAME = "LB8.xml";
    public static String PICKED_DATE;

    public static ArrayList<Task> TASKS = new ArrayList<>();
    ArrayList<String> Categories = new ArrayList<>();
    TextView pickedDate;
    EditText editNote;
    CalendarView calendarView;

    Button addButton;
    Button changeButton;
    Button deleteButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File xmlFile = null;
        try {
            xmlFile = checkFile();
        } catch (TransformerException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        XMLOperations.Operations.readXMLRaw(xmlFile);
        try {
            XMLOperations.Operations.addTask(xmlFile,
                    new Task("randomInfo", "15-9-2020", "Home", "TODO"));
        } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
            e.printStackTrace();
        }

        try {
            XMLOperations.Operations.changeTask(xmlFile, "newrandInfo",
                    "12-9-2020","Class", "TODO");
        } catch (ParserConfigurationException | TransformerException | SAXException | IOException e) {
            e.printStackTrace();
        }

        try {
            XMLOperations.Operations.deleteTask(xmlFile, "1");
        } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private boolean existFile(String fileName){
        boolean flag;
        File checkFile = new File(super.getFilesDir(), fileName);
        if(flag = checkFile.exists()){
            Log.d("log_file_ex", "File " + fileName + " exist");
        }
        else{
            Log.d("log_file_ex", "File " + fileName + " not found");
        }
        return flag;
    }

    public File checkFile() throws TransformerException, ParserConfigurationException {
        boolean isExist = existFile(FILENAME);
        File mainFile = new File(super.getFilesDir(), FILENAME);
        if (isExist) {
            Log.d(LOG_FILE, "File " + FILENAME + " is already exist!");
        } else {
            AlertDialog.Builder warning = new AlertDialog.Builder(this);
            warning.setTitle("Creating file " + FILENAME).setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.d(LOG_FILE, "Creating file " + FILENAME);
                        }
                    });
            AlertDialog alertDialog = warning.create();
            alertDialog.show();
            mainFile = XMLOperations.Operations.createXMLFile(super.getFilesDir(), FILENAME);
            //Categories.add()
            return mainFile;
        }
        return mainFile;
    }
}
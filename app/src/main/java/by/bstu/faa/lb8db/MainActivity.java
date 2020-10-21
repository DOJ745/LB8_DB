package by.bstu.faa.lb8db;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import TaskThings.Task;
import TaskThings.XMLOperations;

public class MainActivity extends AppCompatActivity {
    public static int MAX_TASKS = 20;
    public static int MAX_CATEGORIES = 5;
    public static String LOG_FILE = "log_file";
    public static String LOG_TASK = "log_task";

    public static String FILENAME = "LB8.xml";
    public static String PICKED_DATE;

    public static ArrayList<Task> TASKS = new ArrayList<>();
    ArrayList<String> CATEGORIES = new ArrayList<>();
    TextView pickedDate;
    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File xmlFile = null;
        try {
            xmlFile = checkFile();
        } catch (TransformerException | ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

        pickedDate = findViewById(R.id.pickedDate);
        calendarView = findViewById(R.id.calendarView);

        final File finalXmlFile = xmlFile;
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i,
                                            int i1, int i2) {
                pickedDate.setText(i2 + "-" + i1 + "-" + i);
                PICKED_DATE = pickedDate.getText().toString();
                try {
                    TASKS = XMLOperations.Operations.getDateTasks(finalXmlFile, PICKED_DATE);
                } catch (ParserConfigurationException | IOException | SAXException e) {
                    e.printStackTrace();
                }
            }
        });

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

    public File checkFile()
            throws TransformerException, ParserConfigurationException, IOException, SAXException {
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
            CATEGORIES = XMLOperations.Operations.getCategories(mainFile);
            return mainFile;
        }
        return mainFile;
    }

    public void showTasks(View view){
        Intent intent = new Intent(this, TaskListActivity.class);
        intent.putExtra("TaskList", TASKS);
        startActivity(intent);
    }

    public void showCategories(View view){
        Intent intent = new Intent(this, TaskCategoriesActivity.class);
        intent.putExtra("TaskCategories", CATEGORIES);
        startActivity(intent);
    }
}
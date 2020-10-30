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

import TaskThings.JSONOperations;
import TaskThings.Task;
import TaskThings.XMLOperations;

public class MainActivity extends AppCompatActivity {
    public static String LOG_FILE = "log_file";
    public static String LOG_TASK = "log_task";

    public static String FILENAME = "LB8.xml";
    public static String FILE_CATEGORIES = "categories.json";
    public static File XMLFILE;
    public static String PICKED_DATE;

    public static ArrayList<Task> TASKS = new ArrayList<>();

    TextView pickedDate;
    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File xmlFile = null;
        try {
            xmlFile = checkFile();
            createFileCategories(FILE_CATEGORIES);
            XMLFILE = xmlFile;
        } catch (TransformerException | ParserConfigurationException | IOException e) {
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
            throws TransformerException, ParserConfigurationException {
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
            return mainFile;
        }
        return mainFile;
    }

    public void createFileCategories(String filename) throws IOException {
        boolean isExist = existFile(FILE_CATEGORIES);
        if (isExist) {
            Log.d(LOG_FILE, "File " + FILE_CATEGORIES + " is already exist!");
        } else {
            AlertDialog.Builder warning = new AlertDialog.Builder(this);
            warning.setTitle("Creating file " + FILE_CATEGORIES).setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.d(LOG_FILE, "Creating file " + FILE_CATEGORIES);
                        }
                    });
            AlertDialog alertDialog = warning.create();
            alertDialog.show();
            JSONOperations.Operations.createCategory(FILE_CATEGORIES, super.getFilesDir());
        }
    }

    public void showTasks(View view){
        Intent intent = new Intent(this, TaskListActivity.class);
        intent.putExtra("TaskList", TASKS);
        startActivity(intent);
    }

    public void showCategories(View view) {
        Intent intent = new Intent(this, TaskCategoriesActivity.class);
        startActivity(intent);
    }
}
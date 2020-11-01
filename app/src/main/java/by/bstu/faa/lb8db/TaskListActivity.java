package by.bstu.faa.lb8db;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import TaskThings.JSONOperations;
import TaskThings.Task;
import TaskThings.XMLOperations;

public class TaskListActivity extends AppCompatActivity {
    public static int MAX_TASKS = 5;
    public static String CATEGORIES = "categories.json";
    public static String CHOOSED_CATEGORY;
    public static int CHOOSED_TASK;
    public static File XMLFILE;
    public static String CURRENT_DATE;

    ArrayList<Task> CurrentTasks = new ArrayList<>();
    ArrayList<String> stingsTasks = new ArrayList<>();
    ArrayList<String> selectedTasks = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> categoriesAdapter;

    EditText editName;
    EditText editInfo;
    Spinner editCategory;

    ListView listView;
    Button addButton;
    Button updateButton;
    Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        initTasks();
        initStringTasks(stingsTasks);
        XMLFILE = new File(super.getFilesDir(), "LB8.xml");

        listView = findViewById(R.id.taskList);
        editName = findViewById(R.id.editName);
        editInfo = findViewById(R.id.editInfo);
        editCategory = findViewById(R.id.spinner);

        addButton = findViewById(R.id.addButton);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);

        categoriesAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                JSONOperations.Operations.readString(new File(super.getFilesDir(), CATEGORIES)));
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CHOOSED_CATEGORY = (String)parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        editCategory.setOnItemSelectedListener(itemSelectedListener);
        editCategory.setAdapter(categoriesAdapter);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, stingsTasks);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                String selectedTask = adapter.getItem(position);
                if(listView.isItemChecked(position)){
                    selectedTasks.add(selectedTask);
                    CHOOSED_TASK = position;

                    addButton.setEnabled(false);
                    deleteButton.setEnabled(true);
                    updateButton.setEnabled(true);
                }
                else{
                    selectedTasks.remove(selectedTask);
                    addButton.setEnabled(true);
                    deleteButton.setEnabled(false);
                    updateButton.setEnabled(false);
                }
            }
        });
    }

    private void initTasks(){
        Bundle arguments = getIntent().getExtras();
        try{
            CurrentTasks = (ArrayList<Task>) arguments.get("TaskList");
            CURRENT_DATE = arguments.get("DATE").toString();
        }
        catch (NullPointerException e){
            Log.e("log_intent", "Categories not initialized!");
        }
    }

    private void initStringTasks(ArrayList<String> tasks){
        for(int i = 0; i < CurrentTasks.size(); i++){
           tasks.add(CurrentTasks.get(i).toString());
        }
    }

    public void addTask(View view)
            throws ParserConfigurationException, TransformerException, SAXException, IOException {
        Task newTask = new Task();
        if(editInfo.getText().toString().length() > 0){
            newTask.setInfo(editInfo.getText().toString());
        }

        if(editName.getText().toString().length() > 0){
            newTask.setName(editName.getText().toString());
        }
        if(CHOOSED_CATEGORY.length() > 0){
            newTask.setCategory(CHOOSED_CATEGORY);
        }
        newTask.setDate(CURRENT_DATE);

        if(stingsTasks.size() < MAX_TASKS){
            stingsTasks.add(newTask.toString());
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, stingsTasks);
        listView.setAdapter(adapter);
        XMLOperations.Operations.addTask(XMLFILE, newTask);

        Toast toast = Toast.makeText(getApplicationContext(), "Задача успешно добавлена!", Toast.LENGTH_LONG);
        toast.show();
    }

    public void updateTask(View view)
            throws ParserConfigurationException, TransformerException, SAXException, IOException {
        String oldId = CurrentTasks.get(CHOOSED_TASK).getId();

        if(editInfo.getText().toString().length() > 0 &&
                editName.getText().toString().length() > 0){

            Task newTask = new Task();
            newTask.setName(editName.getText().toString());
            newTask.setInfo(editInfo.getText().toString());
            newTask.setDate(CurrentTasks.get(0).getDate());
            newTask.setCategory(CHOOSED_CATEGORY);

            stingsTasks.remove(CHOOSED_TASK);
            CurrentTasks.remove(CHOOSED_TASK);

            XMLOperations.Operations.changeTask(XMLFILE, oldId,
                    editInfo.getText().toString(), CHOOSED_CATEGORY, editName.getText().toString());
            stingsTasks.add(newTask.toString());
            CurrentTasks.add(newTask);
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, stingsTasks);
            listView.setAdapter(adapter);
            Toast toast = Toast.makeText(getApplicationContext(), "Задача успешно изменена!", Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), "Данные не введены!", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void deleteTask(View view)
            throws ParserConfigurationException, TransformerException, SAXException, IOException {
        String oldId = CurrentTasks.get(CHOOSED_TASK).getId();
        stingsTasks.remove(CHOOSED_TASK);
        CurrentTasks.remove(CHOOSED_TASK);
        XMLOperations.Operations.deleteTask(XMLFILE, oldId);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, stingsTasks);
        listView.setAdapter(adapter);
        Toast toast = Toast.makeText(getApplicationContext(), "Задача успешно удалена!", Toast.LENGTH_LONG);
        toast.show();
    }
}
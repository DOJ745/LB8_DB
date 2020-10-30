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
    public static String CATEGORIES = "categories.json";
    public static String CHOOSED_CATEGORY;
    public static File XMLFILE;

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

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, stingsTasks);
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
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                String selectedTask = adapter.getItem(position);
                if(listView.isItemChecked(position)){
                    selectedTasks.add(selectedTask);

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
        newTask.setDate(CurrentTasks.get(0).getDate());
        XMLOperations.Operations.addTask(XMLFILE, newTask);
    }

    public void updateTask(View view){

    }

    public void deleteTask(View view){

    }
}
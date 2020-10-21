package by.bstu.faa.lb8db;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import TaskThings.Task;

public class TaskListActivity extends AppCompatActivity {

    ArrayList<Task> CurrentTasks = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayList<String> stingsTasks = new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        initTasks();
        initStringTasks(stingsTasks);
        listView = findViewById(R.id.taskList);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, stingsTasks);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                /*Task selectedTask = adapter.getItem(position);
                if(listView.isItemChecked(position)){
                    selectedCategories.add(selectedCategory);
                }
                else{
                    selectedCategories.remove(selectedCategory);
                }*/
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
}
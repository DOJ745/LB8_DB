package by.bstu.faa.lb8db;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import TaskThings.JSONOperations;
import TaskThings.Task;

public class TaskCategoriesActivity extends AppCompatActivity {

    ListView listView;
    EditText editCategory;
    Button addButton;
    Button updateButton;
    Button deleteButton;

    ArrayList<String> Categories = new ArrayList<>();
    ArrayList<String> selectedCategories = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_categories);

        listView = findViewById(R.id.categoryList);
        editCategory = findViewById(R.id.editCategory);

        addButton = findViewById(R.id.addButton);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);
        Categories = initCategories();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, Categories);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                String selectedCategory = adapter.getItem(position);
                if(listView.isItemChecked(position)){
                    selectedCategories.add(selectedCategory);
                    addButton.setEnabled(false);
                }
                else{
                    selectedCategories.remove(selectedCategory);
                    addButton.setEnabled(true);
                }
            }
        });
    }

    private ArrayList<String> initCategories(){
        ArrayList<String> readCategories = JSONOperations.Operations.readString(
                new File(super.getFilesDir(), "categories.json"));
        return  readCategories;
    }

    public void addCategory(View view){

    }

    public void updateCategory(View view){

    }

    public void deleteCategory(View view){

    }
}
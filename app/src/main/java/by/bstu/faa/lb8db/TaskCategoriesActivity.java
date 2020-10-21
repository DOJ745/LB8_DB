package by.bstu.faa.lb8db;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import TaskThings.Task;

public class TaskCategoriesActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> CategoriesOperations = new ArrayList<>();
    ArrayList<String> selectedCategories = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_categories);

        listView = findViewById(R.id.categoryList);
        initCategories();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, CategoriesOperations);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                String selectedCategory = adapter.getItem(position);
                if(listView.isItemChecked(position)){
                    selectedCategories.add(selectedCategory);
                }
                else{
                    selectedCategories.remove(selectedCategory);
                }
            }
        });
    }

    private void initCategories(){
        Bundle arguments = getIntent().getExtras();
        String[] categories;
        try{
            categories = (String[]) arguments.get("TaskCategories");
            for(int i = 0; i < categories.length; i++){
                if(categories[i] != null){
                    CategoriesOperations.add(categories[i]);
                }
            }
        }
        catch (NullPointerException e){
            Log.e("log_intent", "Categories not initialized!");
        }
    }
}
package by.bstu.faa.lb8db;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import TaskThings.JSONOperations;
import TaskThings.XMLOperations;

public class TaskCategoriesActivity extends AppCompatActivity {

    private static final String LOG_FILE = "log_file";
    public static int MAX_CATEGORIES = 5;
    public static String FILENAME = "categories.json";
    public static File XMLFILE;
    public static int CHOOSED_POS = 0;

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

        XMLFILE = new File(super.getFilesDir(), "LB8.xml");

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
                CHOOSED_POS = position;

                if(listView.isItemChecked(position)){
                    selectedCategories.add(selectedCategory);

                    editCategory.setText(selectedCategory);

                    addButton.setEnabled(false);
                    deleteButton.setEnabled(true);
                    updateButton.setEnabled(true);
                }
                else{
                    selectedCategories.remove(selectedCategory);

                    editCategory.setText("");

                    addButton.setEnabled(true);
                    deleteButton.setEnabled(false);
                    updateButton.setEnabled(false);
                }
            }
        });
    }

    private ArrayList<String> initCategories(){
        ArrayList<String> readCategories = JSONOperations.Operations.readString(
                new File(super.getFilesDir(), FILENAME));
        return  readCategories;
    }

    public void addCategory(View view) throws IOException {
        if(editCategory.getText().toString().length() > 0 && Categories.size() < MAX_CATEGORIES){
            Categories.add(editCategory.getText().toString());
            JSONOperations.Operations.saveString(Categories, FILENAME, super.getFilesDir());

            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, Categories);
            listView.setAdapter(adapter);
            Toast toast = Toast.makeText(getApplicationContext(), "Категория добавлена!", Toast.LENGTH_SHORT);
            toast.show();
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Неверные данные или превышен лимит(5 категорий)",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void updateCategory(View view)
            throws IOException, ParserConfigurationException, SAXException, TransformerException {
        if(editCategory.getText().toString().length() > 0 && Categories.size() < MAX_CATEGORIES){

            XMLOperations.Operations.updateCategory(
                    XMLFILE, adapter.getItem(CHOOSED_POS), editCategory.getText().toString());

            Categories.remove(adapter.getItem(CHOOSED_POS));
            Categories.add(editCategory.getText().toString());
            JSONOperations.Operations.saveString(Categories, FILENAME, super.getFilesDir());

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, Categories);
        listView.setAdapter(adapter);
        Toast toast = Toast.makeText(getApplicationContext(), "Категория была изменена!", Toast.LENGTH_SHORT);
        toast.show();
        }
        else{
                Toast toast = Toast.makeText(getApplicationContext(),
                "Неверные данные или превышен лимит(5 категорий)",
                Toast.LENGTH_SHORT);
        toast.show();
        }
    }

    public void deleteCategory(View view)
            throws IOException, ParserConfigurationException, SAXException, TransformerException {
        XMLOperations.Operations.deleteCategory(XMLFILE, adapter.getItem(CHOOSED_POS));
        Categories.remove(adapter.getItem(CHOOSED_POS));
        JSONOperations.Operations.saveString(Categories, FILENAME, super.getFilesDir());

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, Categories);
        listView.setAdapter(adapter);
        Toast toast = Toast.makeText(getApplicationContext(), "Категория была удалена!", Toast.LENGTH_SHORT);
        toast.show();
    }
}
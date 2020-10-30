package by.bstu.faa.lb8db;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import TaskThings.Task;
import TaskThings.XMLOperations;

public class XPathActivity extends AppCompatActivity {
    public static File XMLFILE;

    EditText editCategory;
    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_x_path);

        XMLFILE = new File(super.getFilesDir(), "LB8.xml");

        editCategory = findViewById(R.id.editCategory);
        listView = findViewById(R.id.taskList);
    }

    public void findTasks(View view)
            throws ParserConfigurationException, SAXException,
            XPathExpressionException, IOException {
        ArrayList<Task> findedTasks;
        ArrayList<String> stringTasks = new ArrayList<>();
        if(editCategory.getText().toString().length() > 0){
            findedTasks = XMLOperations.Operations.findByXPath(XMLFILE, editCategory.getText().toString());
            for(int i = 0; i < findedTasks.size(); i++){
                stringTasks.add(findedTasks.get(i).toString());
            }
            if(stringTasks.size() <= 0){
                ArrayList empty = new ArrayList<String>();
                empty.add("None");
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, empty);
                listView.setAdapter(adapter);
                Toast toast = Toast.makeText(getApplicationContext(), "Ничего не найдено!", Toast.LENGTH_SHORT);
                toast.show();
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, stringTasks);
            listView.setAdapter(adapter);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), "Вы ничего не ввели!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
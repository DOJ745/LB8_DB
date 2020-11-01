package by.bstu.faa.lb8db;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import TaskThings.XMLOperations;

public class XSLTActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_x_s_l_t);

        try {
            XMLOperations.Operations.resultXSLT("LB8.xml", "form.xslt", super.getFilesDir());
        } catch (IOException | TransformerException e) {
            e.printStackTrace();
        }
    }
}
package com.example.savetest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    Button buttonSave;
    Button buttonLoad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView txtv = findViewById(R.id.textView);
        final EditText edittxt = findViewById(R.id.editText);

        final EditText nametext = findViewById(R.id.editText2);

        //button save
        buttonSave = findViewById(R.id.savebtn);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView output = findViewById(R.id.textView);
                String filename = nametext.getText().toString() + ".txt";
                FileOutputStream outstream;
                try {
                    outstream = openFileOutput(filename, Context.MODE_PRIVATE);
                    outstream.write(edittxt.getText().toString().getBytes());
                    outstream.close();
                }
                catch(Exception e) {
                    Log.d("debug", "file not found; creating new file");
                    File newfile = new File(getFilesDir(), filename);
                    try{
                        outstream = new FileOutputStream(newfile);
                        outstream.write(edittxt.getText().toString().getBytes());
                        outstream.close();
                    }
                    catch (Exception ex){
                        Log.d("error", "Couldn't create file");
                        output.setText("Error: Couldn't create file");
                    }
                }
            }
        });
        buttonLoad = findViewById(R.id.loadbtn);
        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = nametext.getText().toString() + ".txt";
                TextView output = findViewById(R.id.textView);

                File file = new File(getFilesDir(), filename);
                int length = (int) file.length();

                byte[] bytes = new byte[length];

                try {
                    FileInputStream in = new FileInputStream(file);
                    in.read(bytes);
                    in.close();
                    String contents = new String(bytes);
                    output.setText(contents);
                }
                catch (Exception e){
                    Log.d("error", "Couldn't read file");
                    output.setText("Error: Couldn't read file (file does not exist?)");
                }

            }
        });


    }


}

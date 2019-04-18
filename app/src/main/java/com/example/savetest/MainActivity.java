package com.example.savetest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button buttonSave;
    Button buttonLoad;

    ArrayList<String> filenames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView txtv = findViewById(R.id.textView);
        final EditText edittxt = findViewById(R.id.editText);
        final EditText nametext = findViewById(R.id.editText2);

        loadfilenames();

        //button save
        buttonSave = findViewById(R.id.savebtn);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = nametext.getText().toString() + ".txt";
                FileOutputStream outstream;
                try {
                    outstream = openFileOutput(filename, Context.MODE_PRIVATE);
                    outstream.write(edittxt.getText().toString().getBytes());
                    outstream.close();
                    if(!filenames.contains(filename)){
                        filenames.add(filename);
                        try {
                            FileOutputStream namestream = openFileOutput("metadata", Context.MODE_APPEND);
                            namestream.write((filename + "\n").getBytes());
                            namestream.close();
                        }
                        catch (Exception e){
                            Log.d("update", e.getMessage());
                        }
                        updatenamelist();
                    }
                }
                catch(Exception e) {
                    Log.d("debug", "file not found; creating new file");
                    /*File newfile = new File(getFilesDir(), filename);
                    try{
                        outstream = new FileOutputStream(newfile);
                        outstream.write(edittxt.getText().toString().getBytes());
                        outstream.close();

                    }
                    catch (Exception ex){
                        Log.d("error", "Couldn't create file");
                        txtv.setText("Error: Couldn't create file");
                    }*/
                }
            }
        });
        buttonLoad = findViewById(R.id.loadbtn);
        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = nametext.getText().toString() + ".txt";

                File file = new File(getFilesDir(), filename);
                int length = (int) file.length();

                byte[] bytes = new byte[length];

                try {
                    FileInputStream in = new FileInputStream(file);
                    in.read(bytes);
                    in.close();
                    String contents = new String(bytes);
                    txtv.setText(contents);
                }
                catch (Exception e){
                    Log.d("error", "Couldn't read file");
                    txtv.setText("Error: Couldn't read file (file does not exist?)");
                }

            }
        });


    }

    private void loadfilenames() {
        TextView filelist = findViewById(R.id.filelist);

        try {
            //InputStream fin = getApplicationContext().getResources().getAssets().open("metadata");
            FileInputStream fin = openFileInput("metadata");
            BufferedReader in = new BufferedReader(new InputStreamReader(fin));
            String line;
            while ((line = in.readLine()) != null) {
                filenames.add(line);
            }

            updatenamelist();
        }
        catch (Exception e){
            Log.d("error1", e.getMessage());
            e.printStackTrace();
            //File file = new File(getFilesDir(), "metadata");
            try {
                //FileOutputStream outstream = new FileOutputStream(file);
                FileOutputStream outstream = openFileOutput("metadata", Context.MODE_PRIVATE);
                outstream.write("".getBytes());
                outstream.close();
            }
            catch (Exception e2){
                Log.d("error2", e2.getMessage());
            }
            filelist.setText("!!! No files !!!");
        }
    }

    private void updatenamelist() {
        String out = "";
        TextView filelist = findViewById(R.id.filelist);

        for (int i = 0; i < filenames.size(); i++){
            out += filenames.get(i) + "\n";
        }
        filelist.setText(out);
    }


}

package com.example.encryptor;

import java.io.*;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.content.*;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button encrypt, decrypt, fileInput, fileOutput, switchToSecondActivity;
    private EditText message, cipher, key;
    private TextView screen_output;
    private static final String alphabetString = "abcdefghijklmnopqrstuvwxyz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        encrypt = findViewById(R.id.btnencrypt);
        decrypt = findViewById(R.id.btndecrypt);
        screen_output = findViewById(R.id.tV1);
        message = findViewById(R.id.inputMessage);
        cipher = findViewById(R.id.ciphEdt);
        key = findViewById(R.id.key_dt);
        fileInput = findViewById(R.id.file_input);
        fileOutput = findViewById(R.id.file_output);
        switchToSecondActivity = findViewById(R.id.main_button_switch);

        encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                encrypt12(message.getText().toString(), Integer.parseInt(key.getText().toString()));
            }
        });

        decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrypt12(cipher.getText().toString(), Integer.parseInt(key.getText().toString()));
            }
        });

        fileOutput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = "caesar.txt";
                String outpt = String.join(",", cipher.getText().toString(), String.valueOf(key.getText()));
                FileOutputStream outputStream;
                try {
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(outpt.getBytes());
                    outputStream.close();
                    Toast.makeText(getBaseContext(),
                            "Done writing to 'caesar.txt'",
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        fileInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileInputStream fis;
                final StringBuffer storedString = new StringBuffer();

                try {
                    fis = openFileInput("caesar.txt");
                    DataInputStream dataIO = new DataInputStream(fis);
                    String strLine = null;

                    if ((strLine = dataIO.readLine()) != null) {
                        storedString.append(strLine);
                    }
                        String fetch = String.valueOf(storedString);
                        String[] sep = fetch.split(",");
                        cipher.setText(sep[0]);
                        key.setText(sep[1]);

                    dataIO.close();
                    fis.close();
                    Toast.makeText(getBaseContext(),
                            "Successfully read data from 'caesar.txt'",
                            Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        switchToSecondActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivities();
            }
        });

    }

    public void switchActivities() {
        Intent switchActivityIntent = new Intent(this, ExtraActivity.class);
        startActivity(switchActivityIntent);
    }

    public void decrypt12(String cipher, int key) {
        screen_output.setText((utility.decrypt1(cipher, key).toLowerCase()));
    }

    public String encrypt12(String message, int shiftkey) {
        message = message.toLowerCase();
        String cipherText = "";
        for (int i = 0; i < message.length(); i++) {
            int charPosition = alphabetString.indexOf(message.charAt(i));
            int keyval = (shiftkey + charPosition) % 26;
            char replaceVAL = alphabetString.charAt(keyval);
            cipherText += replaceVAL;
            screen_output.setText(cipherText);
            cipher.setText(cipherText);
        }

        return cipherText;
    }
}

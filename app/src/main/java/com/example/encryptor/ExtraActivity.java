package com.example.encryptor;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.*;

import android.content.Context;
import android.util.Log;
import android.util.Base64;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ExtraActivity extends AppCompatActivity {

    private EditText etString, etKey;
    private Button btnEncrypt, btnDecrypt, fileInput, fileOutput, switchToFirstActivity;
    private TextView tvResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);

        etString = findViewById(R.id.et_string);
        etKey = findViewById(R.id.et_key);
        btnEncrypt = findViewById(R.id.btn_encrypt);
        btnDecrypt = findViewById(R.id.btn_decrypt);
        tvResults = findViewById(R.id.tv_results);
        fileInput = findViewById(R.id.file_input);
        fileOutput = findViewById(R.id.file_output);
        switchToFirstActivity = findViewById(R.id.extra_button_switch);

        btnEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SecretKey secretKey=generateKey(etKey.getText().toString());
                    String strResult=encryptMsg(etString.getText().toString(),secretKey);
                    tvResults.setText(strResult);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        btnDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SecretKey secretKey=generateKey(etKey.getText().toString());
                    String strResult=decryptMsg(etString.getText().toString(),secretKey);
                    tvResults.setText(strResult);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        fileOutput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = "aes.txt";
                String outpt = String.join(",", tvResults.getText().toString(), String.valueOf(etKey.getText()));
                FileOutputStream outputStream;
                try {
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(outpt.getBytes());
                    outputStream.close();
                    Toast.makeText(getBaseContext(),
                            "Done writing to 'aes.txt'",
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
                    fis = openFileInput("aes.txt");
                    DataInputStream dataIO = new DataInputStream(fis);
                    String strLine = null;

                    if ((strLine = dataIO.readLine()) != null) {
                        storedString.append(strLine);
                    }
                    String fetch = String.valueOf(storedString);
                    String[] sep = fetch.split(",");
                    etString.setText(sep[0]);
                    etKey.setText(sep[1]);

                    dataIO.close();
                    fis.close();
                    Toast.makeText(getBaseContext(),
                            "Successfully read data from 'aes.txt'",
                            Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        switchToFirstActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivities();
            }
        });
    }

    public void switchActivities() {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }

    public String encryptMsg(String message, SecretKey secret)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
        return Base64.encodeToString(cipherText, Base64.NO_WRAP);
    }
    public String decryptMsg(String cipherText, SecretKey secret)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        byte[] decode = Base64.decode(cipherText, Base64.NO_WRAP);
        String decryptString = new String(cipher.doFinal(decode), "UTF-8");
        return decryptString;
    }
    public static SecretKey generateKey(String key)
            throws NoSuchAlgorithmException
    {
        SecretKeySpec secret;
        secret = new SecretKeySpec(key.getBytes(), "AES");
        return secret;
    }
}

package com.example.parcialapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ToggleButton toggle;
    EditText lugarFavorito;
    Button boton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ToggleButton toggle = (ToggleButton) findViewById(R.id.togglebutton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(MainActivity.this,"Que suerte!",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this,"Ya lo visitarás!",Toast.LENGTH_LONG).show();
                }
            }
        });
        toggle = (ToggleButton) findViewById(R.id.togglebutton);
        boton = findViewById(R.id.button);
        lugarFavorito = findViewById(R.id.editText);

        List<String> listaLugares;
        listaLugares = new ArrayList<String>();

        String lugar = lugarFavorito.getText().toString();
        listaLugares.add(lugar);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MapaActivity.class);
                i.putStringArrayListExtra("lugares", (ArrayList<String>) listaLugares);
                startActivity(i);
            }
        });



    }
}
package com.cdi.practica.jefaturapoliciaagente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Sancion extends AppCompatActivity {

    private Spinner tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sancion);

        tipo = (Spinner) findViewById(R.id.t2);
        String[] hechos = { "Exceso velocidad", "Aparcamiento", "Altercado público",
                "Posesión de drogas", "Robo"};
        tipo.setAdapter(new ArrayAdapter<String>(this, R.layout.my_spinner_item, hechos));


        findViewById(R.id.t8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Sancion.this, MainActivity.class));
                finish();
                Toast.makeText(getApplicationContext(), "Sanción enviada", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

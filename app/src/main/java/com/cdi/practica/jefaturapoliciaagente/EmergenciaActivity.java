package com.cdi.practica.jefaturapoliciaagente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmergenciaActivity extends AppCompatActivity {

    private Button botonCompletada;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference refEmgAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencia);

        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        refEmgAct = database.getReference("emergencias").child("activas");
        botonCompletada = (Button) findViewById(R.id.comletada);

        botonCompletada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refEmgAct.child(user.getUid()).removeValue();
                startActivity(new Intent(EmergenciaActivity.this,MainActivity.class));
                finish();
            }
        });
    }
}

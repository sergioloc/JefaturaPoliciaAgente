package com.cdi.practica.jefaturapoliciaagente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cdi.practica.jefaturapoliciaagente.Objetos.Denuncia;
import com.cdi.practica.jefaturapoliciaagente.Objetos.Predenuncia;
import com.cdi.practica.jefaturapoliciaagente.Objetos.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PredenunciaActivity extends AppCompatActivity {

    TextView nombre, apellidos, dni, sexo, telefono, email, cumple, nacio, domicilio,
            tipo, direccion, hora, descripcion;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference refPreActiva, refUsu, refDen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predenuncia);

        init();
        setInfoP();
        findViewById(R.id.botonPre).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDatos();
            }
        });

    }

    private void init(){
        nombre = (TextView) findViewById(R.id.nombre);
        apellidos = (TextView) findViewById(R.id.apellidos);
        dni = (TextView) findViewById(R.id.dni);
        sexo = (TextView) findViewById(R.id.sexo);
        telefono = (TextView) findViewById(R.id.telefono);
        email = (TextView) findViewById(R.id.email);
        cumple = (TextView) findViewById(R.id.cumple);
        nacio = (TextView) findViewById(R.id.nacio);
        domicilio = (TextView) findViewById(R.id.domicilio);
        tipo = (TextView) findViewById(R.id.tipo);
        direccion = (TextView) findViewById(R.id.direccion);
        hora = (TextView) findViewById(R.id.hora);
        descripcion = (TextView) findViewById(R.id.descripcion);

        // Firebase
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        refPreActiva = database.getReference("predenuncias").child("activas");
        refUsu = database.getReference("usuarios");
        refDen = database.getReference("denuncias");

    }


    private void setInfoP(){
        refPreActiva.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tipo.setText(dataSnapshot.child("tipo").getValue(String.class));
                dni.setText(dataSnapshot.child("dni").getValue(String.class));
                direccion.setText(dataSnapshot.child("ubicacion").getValue(String.class));
                hora.setText(dataSnapshot.child("hora").getValue(String.class));

                refUsu.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Usuario usuario = snapshot.child("info").getValue(Usuario.class);
                            if(usuario.getDni().equals(dni.getText().toString())){
                                nombre.setText(usuario.getNombre());
                                apellidos.setText(usuario.getApellidos());
                                sexo.setText(usuario.getSexo());
                                telefono.setText(usuario.getTelefono());
                                email.setText(usuario.getEmail());
                                cumple.setText(usuario.getNacimiento());
                                nacio.setText(usuario.getNacionalidad());
                                domicilio.setText(usuario.getDomicilio());
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void guardarDatos(){
        Denuncia d = new Denuncia(nombre.getText().toString(),apellidos.getText().toString(),
                dni.getText().toString(),sexo.getText().toString(),telefono.getText().toString(),
                email.getText().toString(),cumple.getText().toString(),nacio.getText().toString(),
                domicilio.getText().toString(),tipo.getText().toString(),direccion.getText().toString(),
                hora.getText().toString(),descripcion.getText().toString());

        refDen.push().setValue(d);
        refPreActiva.child(user.getUid()).removeValue();
        toast("Denuncia completada");
        startActivity(new Intent(PredenunciaActivity.this,MainActivity.class));
    }


    private void toast(String texto){
        Toast.makeText(getApplicationContext(),texto,Toast.LENGTH_SHORT).show();
    }
}

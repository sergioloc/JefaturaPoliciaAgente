package com.cdi.practica.jefaturapoliciaagente;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
    private int cont=0;
    private ImageView iv1,iv2,iv3,iv11,iv22,iv33;

    private static final int CAMERA_PIC_REQUEST = 1337;

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

        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        iv3 = (ImageView) findViewById(R.id.iv3);

        iv11 = (ImageView) findViewById(R.id.iv11);
        iv22 = (ImageView) findViewById(R.id.iv21);
        iv33 = (ImageView) findViewById(R.id.iv31);

        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cont=1;
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cont=2;
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cont=3;
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });

        iv11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog1 = new Dialog(PredenunciaActivity.this);
                dialog1.setContentView(R.layout.abrir_imagen);
                dialog1.findViewById(R.id.imagen).setBackground(iv1.getDrawable());
                dialog1.show();
            }
        });

        iv22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog1 = new Dialog(PredenunciaActivity.this);
                dialog1.setContentView(R.layout.abrir_imagen);
                dialog1.findViewById(R.id.imagen).setBackground(iv2.getDrawable());
                dialog1.show();
            }
        });

        iv33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog1 = new Dialog(PredenunciaActivity.this);
                dialog1.setContentView(R.layout.abrir_imagen);
                dialog1.findViewById(R.id.imagen).setBackground(iv3.getDrawable());
                dialog1.show();
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            if(cont==1){
                iv1.setImageBitmap(image);
                iv1.setPadding(0,0,0,0);
                iv11.setVisibility(View.VISIBLE);
            }else if(cont==2){
                iv2.setImageBitmap(image);
                iv2.setPadding(0,0,0,0);
                iv22.setVisibility(View.VISIBLE);
            }else{
                iv3.setImageBitmap(image);
                iv3.setPadding(0,0,0,0);
                iv33.setVisibility(View.VISIBLE);
            }
        }
    }
}

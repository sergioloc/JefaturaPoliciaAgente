package com.cdi.practica.jefaturapoliciaagente;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.cdi.practica.jefaturapoliciaagente.Objetos.Agente;
import com.cdi.practica.jefaturapoliciaagente.Objetos.Emergencia;
import com.cdi.practica.jefaturapoliciaagente.Objetos.Evento;
import com.cdi.practica.jefaturapoliciaagente.Objetos.Predenuncia;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AlertDialog alert;
    private Boolean preAct;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference refAgentes, refEmgEspera, refEmgActiva, refPreEspera, refPreActiva, refEve;
    private ArrayList emgEsperaList, preEsperaList, eveList;
    private TextView numEmg, numPre, nombreAgente, idAgente, key, desPre, desEve;
    private View headerView;
    private NavigationView navigationView;
    private Button botonSOS, botonPre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();
        if(user.getUid().equals("dmA60cSLAGOr7dT7FN7U5L32i4w2")){
            cargarEventos();
        }else{
            cargarEmergencias();
            cargarPredenuncias();
        }
        buttons();
        key.setVisibility(View.INVISIBLE);

    }


    /**Inits**/

    private void init(){
        preAct=false;
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        nombreAgente = (TextView) headerView.findViewById(R.id.nombreAgente);
        idAgente = (TextView) headerView.findViewById(R.id.idAgente);
        // Buttons
        botonSOS = (Button) findViewById(R.id.botonSOS);
        botonPre = (Button) findViewById(R.id.botonPre);
        //TextView
        numEmg = (TextView) findViewById(R.id.numSOS);
        numPre = (TextView) findViewById(R.id.numPre);
        key = (TextView) findViewById(R.id.key);
        desPre = (TextView) findViewById(R.id.desPre);
        desEve = (TextView) findViewById(R.id.desEve);
        // Firebase
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        refAgentes = database.getReference("agentes");
        refEmgEspera = database.getReference("emergencias").child("espera").child(user.getUid());
        refEmgActiva = database.getReference("emergencias").child("activas");
        refPreEspera = database.getReference("predenuncias").child("espera").child(user.getUid());
        refPreActiva = database.getReference("predenuncias").child("activas");
        refEve = database.getReference("eventos");
        getDatosAgente();
        // ArrayList
        emgEsperaList = new ArrayList();
        preEsperaList = new ArrayList();
        eveList = new ArrayList();
    }

    private void buttons(){
        botonPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(preEsperaList.size()!=0){
                    mostrarDialogPre();
                }else{
                    toast("No hay ninguna predenuncia pendiente");
                }
            }
        });
        botonSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(emgEsperaList.size()!=0){
                    mostrarDialogEmg();
                }else{
                    toast("No hay ninguna emergencia pendiente");
                }
            }
        });
    }

    private void getDatosAgente(){
        refAgentes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Agente agente = snapshot.getValue(Agente.class);
                    if(snapshot.getKey().equals(user.getUid())){
                        nombreAgente.setText(agente.getApellidos()+", "+agente.getNombre());
                        idAgente.setText(agente.getId());
                        key.setText(agente.getId());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /**BBDD**/

    private void cargarEmergencias(){
        refEmgEspera.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Emergencia emergencia = snapshot.getValue(Emergencia.class);
                    emgEsperaList.add(emergencia);
                }
                numEmg.setText(String.valueOf(emgEsperaList.size()));
                if(emgEsperaList.size()>0)
                    Toast.makeText(getApplication().getApplicationContext(),"EMERGENCIA RECIBIDA", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void cargarPredenuncias(){
        refPreEspera.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Predenuncia pre = snapshot.getValue(Predenuncia.class);
                    preEsperaList.add(pre);
                }
                numPre.setText(String.valueOf(preEsperaList.size()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void cargarEventos(){
        refEve.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.getKey().equals("evento1")){
                        Evento eve = snapshot.getValue(Evento.class);
                        eveList.add(eve);
                        escribirDescripcion(eve);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void activarEmergencia(){
        toast("Emergencia activa");
        refEmgActiva.child(user.getUid()).setValue(emgEsperaList.get(0));
        refEmgEspera.removeValue();
        startActivity(new Intent(MainActivity.this, EmergenciaActivity.class));

    }

    private void activarPredenuncia(){
        desPre.setText("Dirección: c/ Pepinillos 12\nUsuario: Sara Perez");
        toast("Predenuncia activa");
        refPreActiva.child(user.getUid()).setValue(preEsperaList.get(0));
        refPreEspera.removeValue();
        preAct=true;
    }

    /*private Boolean estaActiva(){
        boolean activa = false;
        refPreActiva.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                key.setText(dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(key.getText().toString().equals(user.getUid()))
            activa = true;
        return activa;
    }*/

    private void escribirDescripcion(Evento e) {
        desEve.setText("Nombre: "+e.getNombre()+
                        "\nUbicación: "+e.getUbicacion()+
                        "\nHora: "+e.getHora()+
                        "\nNº de agentes: "+e.getNumAgentes());
    }

    /**Dialog**/

    private void mostrarDialogEmg(){
        // Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("")
                .setTitle("¿Aceptar emergencia?")
                .setCancelable(true)
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            activarEmergencia();
                            }
                        });
        alert = builder.create();
        alert.show();
    }

    private void mostrarDialogPre(){
        // Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("")
                .setTitle("¿Aceptar predenuncia?")
                .setCancelable(true)
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                activarPredenuncia();
                                preAct=true;
                            }
                        });
        alert = builder.create();
        alert.show();
    }

    private void toast(String texto){
        Toast.makeText(getApplicationContext(),texto,Toast.LENGTH_LONG).show();
    }



    /**Navigation**/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_predenuncia) {
            if(preAct)
                startActivity(new Intent(MainActivity.this,PredenunciaActivity.class));
            else
                Toast.makeText(getApplicationContext(), "Debes aceptar una predenuncia",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_atestado) {
            startActivity(new Intent(MainActivity.this,Atestado.class));
        } else if (id == R.id.nav_sancion) {
            startActivity(new Intent(MainActivity.this,Sancion.class));
        } else if (id == R.id.nav_opciones) {
            Toast.makeText(getApplicationContext(), "App desarollada por:\n- Sergio López\n- Carlos Tarancón\n- Joaquín Capel\n- Javier Sangil",Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_cerrar) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this,Login.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cdi.practica.jefaturapoliciaagente.Objetos.Agente;
import com.cdi.practica.jefaturapoliciaagente.Objetos.Emergencia;
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
    private ImageView button_pre, button_emg;
    private Boolean preAct;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference refAgentes, refEmgEspera, refEmgActiva, refPreEspera, refPreActiva;
    private ArrayList emgEsperaList, preEsperaList, preActivaList;
    private TextView numEmg, numPre, nombreAgente, idAgente, key;
    private View headerView;
    private NavigationView navigationView;
    private String keyS;


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
        cargarEmergencias();
        cargarPredenuncias();
        buttons();

        //numPre.setText(String.valueOf(preEsperaList.size()));
        //numEmg.setText(String.valueOf(emgEsperaList.size()));

    }



    /**Inits**/

    private void init(){
        preAct=false;
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        nombreAgente = (TextView) headerView.findViewById(R.id.nombreAgente);
        idAgente = (TextView) headerView.findViewById(R.id.idAgente);
        // Buttons
        button_pre = (ImageView) findViewById(R.id.framePre);
        button_emg = (ImageView) findViewById(R.id.frameSOS);
        //TextView
        numEmg = (TextView) findViewById(R.id.numSOS);
        numPre = (TextView) findViewById(R.id.numPre);
        key = (TextView) findViewById(R.id.key);
        // Firebase
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        refAgentes = database.getReference("agentes");
        refEmgEspera = database.getReference("emergencias").child("espera").child(user.getUid());
        refEmgActiva = database.getReference("emergencias").child("activas");
        refPreEspera = database.getReference("predenuncias").child("espera").child(user.getUid());
        refPreActiva = database.getReference("predenuncias").child("activas");
        getDatosAgente();
        // ArrayList
        emgEsperaList = new ArrayList();
        preEsperaList = new ArrayList();
        preActivaList = new ArrayList();
    }

    private void buttons(){
        button_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(preEsperaList.size()!=0){
                    Toast.makeText(getApplicationContext(),String.valueOf(preEsperaList.size()),Toast.LENGTH_SHORT).show();
                    mostrarDialogPre();
                }
                else{
                    Toast.makeText(getApplicationContext(), "No hay ninguna predenuncia pendiente", Toast.LENGTH_SHORT).show();
                }
            }
        });
        button_emg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emgEsperaList.size()!=0){
                    Toast.makeText(getApplicationContext(),String.valueOf(emgEsperaList.size()),Toast.LENGTH_SHORT).show();
                    mostrarDialogEmg();
                }
                else{
                    Toast.makeText(getApplicationContext(), "No hay ninguna emergencia pendiente", Toast.LENGTH_SHORT).show();
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        numEmg.setText(String.valueOf(emgEsperaList.size()));
    }

    private void cargarPredenuncias(){
        refPreEspera.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Predenuncia pre = snapshot.getValue(Predenuncia.class);
                    preEsperaList.add(pre);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void activarEmergencia(){
        Toast.makeText(getApplicationContext(),"Emergencia activa",Toast.LENGTH_SHORT).show();
        refEmgActiva.child(user.getUid()).setValue(emgEsperaList.get(0));
        refEmgEspera.removeValue();
        startActivity(new Intent(MainActivity.this, EmergenciaActivity.class));

    }

    private void activarPredenuncia(){
        Toast.makeText(getApplicationContext(),"Predenuncia activa",Toast.LENGTH_SHORT).show();
        refPreActiva.child(user.getUid()).setValue(preEsperaList.get(0));
        refPreEspera.removeValue();
        preAct=true;
    }




    /**Dialog**/

    private void mostrarDialogEmg(){
        String ubicacion = "";
        // Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Ubicación "+ubicacion)
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
        String tipo = "";
        String ubicacion = "";
        // Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Tipo: "+tipo+"\nUbicación: "+ubicacion)
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
            Toast.makeText(getApplicationContext(), "Sancion",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_opciones) {
            Toast.makeText(getApplicationContext(), "Opciones",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_cerrar) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this,Login.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**Menu**/
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/

    /*
     private Boolean estaActiva(){
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
    }
      */
}

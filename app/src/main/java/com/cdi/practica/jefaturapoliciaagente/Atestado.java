package com.cdi.practica.jefaturapoliciaagente;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.cdi.practica.jefaturapoliciaagente.Adaptadores.RVAdapter;
import com.cdi.practica.jefaturapoliciaagente.Objetos.Item;

import java.util.ArrayList;

public class Atestado extends AppCompatActivity {

    private Button botonAlc, botonDro, botonAceptar;
    private RecyclerView rvMat, rvPer, rvTes;
    private ArrayList matriculas, personas, testigos;
    private  RVAdapter adapterMat, adapterPer, adapterTes;
    private AlertDialog m, p, t;
    private FloatingActionButton botonMat, botonPer, botonTes;
    private ImageView iv1,iv2,iv3,iv11,iv22,iv33;
    private int cont=0;

    private static final int CAMERA_PIC_REQUEST = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atestado);

        init();
        initAlertDialogMatricula();
        initAlertDialogPersona();
        initAlertDialogTestigo();
        botones();

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
                Dialog dialog1 = new Dialog(Atestado.this);
                dialog1.setContentView(R.layout.abrir_imagen);
                dialog1.findViewById(R.id.imagen).setBackground(iv1.getDrawable());
                dialog1.show();
            }
        });

        iv22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog1 = new Dialog(Atestado.this);
                dialog1.setContentView(R.layout.abrir_imagen);
                dialog1.findViewById(R.id.imagen).setBackground(iv2.getDrawable());
                dialog1.show();
            }
        });

        iv33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog1 = new Dialog(Atestado.this);
                dialog1.setContentView(R.layout.abrir_imagen);
                dialog1.findViewById(R.id.imagen).setBackground(iv3.getDrawable());
                dialog1.show();
            }
        });
    }

    private void init(){
        botonMat = (FloatingActionButton) findViewById(R.id.mat_boton);
        botonPer = (FloatingActionButton) findViewById(R.id.per_boton);
        botonTes = (FloatingActionButton) findViewById(R.id.tes_boton);
        botonAlc = (Button) findViewById(R.id.alc_boton);
        botonDro = (Button) findViewById(R.id.dro_boton);
        botonAceptar = (Button) findViewById(R.id.aceptar);

        rvMat = (RecyclerView)findViewById(R.id.rvMat);
        rvPer = (RecyclerView)findViewById(R.id.rvPer);
        rvTes = (RecyclerView)findViewById(R.id.rvTes);

        rvMat.setHasFixedSize(true);
        rvPer.setHasFixedSize(true);
        rvTes.setHasFixedSize(true);

        LinearLayoutManager llmMat = new LinearLayoutManager(getApplicationContext());
        LinearLayoutManager llmPer = new LinearLayoutManager(getApplicationContext());
        LinearLayoutManager llmTes = new LinearLayoutManager(getApplicationContext());

        rvMat.setLayoutManager(llmMat);
        rvPer.setLayoutManager(llmPer);
        rvTes.setLayoutManager(llmTes);

        matriculas = new ArrayList<>();
        personas = new ArrayList<>();
        testigos = new ArrayList<>();

        adapterMat = new RVAdapter(matriculas);
        adapterPer = new RVAdapter(personas);
        adapterTes = new RVAdapter(testigos);

        rvMat.setAdapter(adapterMat);
        rvPer.setAdapter(adapterPer);
        rvTes.setAdapter(adapterTes);
    }

    private void botones(){
        botonMat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m.show();
            }
        });
        botonPer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p.show();
            }
        });
        botonTes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t.show();
            }
        });
        botonAlc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(botonAlc.getText().equals("Negativo")){
                    botonAlc.setText("Positivo");
                    botonAlc.setBackgroundColor(getResources().getColor(R.color.red));
                }else{
                    botonAlc.setText("Negativo");
                    botonAlc.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
            }
        });
        botonDro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(botonDro.getText().equals("Negativo")){
                    botonDro.setText("Positivo");
                    botonDro.setBackgroundColor(getResources().getColor(R.color.red));
                }else{
                    botonDro.setText("Negativo");
                    botonDro.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
            }
        });
        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Atestado.this, MainActivity.class));
            }
        });
    }

    private void initAlertDialogMatricula(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_matricula, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        dialogBuilder.setTitle("Número de matricula");
        dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                matriculas.add(new Item(edt.getText().toString(),R.drawable.icon_car));
                adapterMat.notifyDataSetChanged();
                edt.setText("");
            }
        });
        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        m = dialogBuilder.create();
    }

    private void initAlertDialogPersona(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_persona, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.nombre);

        dialogBuilder.setTitle("Datos de la persona afectada");
        dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                personas.add(new Item(edt.getText().toString(),R.drawable.icon_user));
                adapterPer.notifyDataSetChanged();
                edt.setText("");
            }
        });
        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        p = dialogBuilder.create();
    }

    private void initAlertDialogTestigo(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_testigo, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.nombre);

        dialogBuilder.setTitle("Datos del testigo");
        dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                testigos.add(new Item(edt.getText().toString(),R.drawable.icon_eye));
                adapterTes.notifyDataSetChanged();
                edt.setText("");
            }
        });
        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        t = dialogBuilder.create();
    }
}

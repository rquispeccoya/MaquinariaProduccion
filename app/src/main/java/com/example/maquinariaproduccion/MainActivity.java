package com.example.maquinariaproduccion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.gridlayout.widget.GridLayout;

import android.widget.LinearLayout;

import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alclabs.fasttablelayout.FastTableLayout;
import com.example.maquinariaproduccion.databinding.ActivityMainBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    int pos;
    private Spinner[] spinnersSentido = new Spinner[4];
    private Spinner[] spinnersTipoSuelo = new Spinner[4];
    private String[] labelTabla = {"Tramo", "RP%", "RR%", "RT%", "V. max", "v=f.max.50/3", "Distancia", "T.total"};
    private String[] resistenciaRodadura = {"Concreto", "Tierra compactada con Mantenimiento", "Tierra sin mantenimiento"};
    private int[][] resistenciaRodaduraValor = {{18, 23}, {35, 35}, {110, 110}};
    private String[] resistenciaPendiente = {"Horizontal", "Subida", "Bajada"};
    //private String []rrTabla = new String[4];
    private String[] tiempoCicloBase = {"914G-962H", "966H-980H", "988H-990H", "992K-994F"};
    private double[] tiempoCicloBaseValor = {0.5, 0.55, 0.6, 0.7};
    private String[] tiempoDescarga = {"Favorable", "Promedio", "No Favorable"};
    private double[] tiempoDescargaValor = {0.7, 1.3, 2.0};
    private String[] tiempoPosicionamiento = {"Favorable", "Promedio", "No Favorable"};
    private double[] tiempoPosicionamientoValor = {0.2, 0.35, 0.5};
    private double eficiencia;
    private double densidad;

    private int[] checks = new int[16];

    private String[] headersTraslado = {"Tramo", "RP%", "RR%", "RT%", "Distancia"};

    private String[][] dataTraslado = new String[4][5];

    private String[][] dataRetorno = new String[4][5];

    private List<MaterialCheckBox> checkBoxs = new ArrayList<>();
    private MaterialCheckBox check;


    private Spinner spinner1, spinner2, spinnerTiempoCicloBase, spinnerTiempoDescarga, spinnerTiempoPosicionamiento,spinnerPesoMaterial,spinnerPesoMaterialOpcion;

    private String[] opcionesPrincipales = {"Camion", "Articulado"};
    private String[][] opcionesSecundarias = {
            {"789C", "793F Estandar", "797F"},
            {"Subopción A", "Subopción B", "Subopción C"}
    };

    double colmada = 176;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        spinner1 = findViewById(R.id.spinnerMaquinaria);
        spinner2 = findViewById(R.id.spinnerTipo);
        spinnerTiempoCicloBase = findViewById(R.id.spinnerTiempoCicloBase);
        spinnerTiempoDescarga = findViewById(R.id.spinnerTiempoDescarga);
        spinnerTiempoPosicionamiento = findViewById(R.id.spinnerTiempoPosicionamiento);

        spinnerPesoMaterial = findViewById(R.id.spinnerPesoMateriales);
        spinnerPesoMaterialOpcion = findViewById(R.id.spinnerPesoMaterialesOpcion);


        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opcionesPrincipales);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opcionesSecundarias[0]);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        ArrayAdapter<String> adapterCicloBase = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiempoCicloBase);
        adapterCicloBase.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTiempoCicloBase.setAdapter(adapterCicloBase);

        ArrayAdapter<String> adapterTiempoDescarga = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiempoDescarga);
        adapterTiempoDescarga.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTiempoDescarga.setAdapter(adapterTiempoDescarga);

        ArrayAdapter<String> adapterTiempoPosicionamiento = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiempoPosicionamiento);
        adapterTiempoPosicionamiento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTiempoPosicionamiento.setAdapter(adapterTiempoPosicionamiento);

        ArrayAdapter<String> adapterPesoMaterial = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,Data.pesoMaterial );
        adapterTiempoPosicionamiento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPesoMaterial.setAdapter(adapterPesoMaterial);

        ArrayAdapter<String> adapterPesoMaterialOpcion = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Data.pesoMaterialOpcion);
        adapterTiempoPosicionamiento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPesoMaterialOpcion.setAdapter(adapterPesoMaterialOpcion);


        binding.editTextTramoAR.setText("0");
        binding.editTextTramoBR.setText("10");
        binding.editTextTramoCR.setText("0");
        binding.editTextTramoDR.setText("5");

        binding.editTextTramoA.setText("1000");
        binding.editTextTramoB.setText("500");
        binding.editTextTramoC.setText("1200");
        binding.editTextTramoD.setText("500");


        selectCamion();


        LinearLayout containerSpinners = findViewById(R.id.container_spinner1);

        // Configurar cada Spinner en el arreglo
        for (int i = 0; i < spinnersSentido.length; i++) {
            spinnersSentido[i] = new Spinner(this);
            spinnersSentido[i].setId(i + 1); // Asignar ID dinámicamente

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, resistenciaPendiente);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnersSentido[i].setAdapter(adapter);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 1.0f);
            params.gravity = Gravity.CENTER_VERTICAL; // Centrar horizontalmente dentro del LinearLayout
            spinnersSentido[i].setLayoutParams(params);
            containerSpinners.addView(spinnersSentido[i]);
        }


        //LinearLayout containerSpinners = findViewById(R.id.spinner_container);
        LinearLayout containerSpinners2 = findViewById(R.id.container_spinner2);

        // Configurar cada Spinner en el arreglo
        for (int i = 0; i < spinnersTipoSuelo.length; i++) {
            spinnersTipoSuelo[i] = new Spinner(this);
            spinnersTipoSuelo[i].setId(i + 1); // Asignar ID dinámicamente

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, resistenciaRodadura);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnersTipoSuelo[i].setAdapter(adapter);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 1.0f);
            params.gravity = Gravity.CENTER_VERTICAL; // Centrar horizontalmente dentro del LinearLayout
            spinnersTipoSuelo[i].setLayoutParams(params);
            containerSpinners2.addView(spinnersTipoSuelo[i]);

            // Manejar el evento de selección para cada Spinner
            final int finalI = i;

            spinnersTipoSuelo[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String seleccionSpinner = (String) parent.getItemAtPosition(position);

                    int calculoResisRodadura = Math.round(resistenciaRodaduraValor[position][0] / 10.0f);
                    dataTraslado[finalI][2] = String.valueOf(calculoResisRodadura);

                    int calculoResisRodaduraRetorno = Math.round(resistenciaRodaduraValor[position][1] / 10.0f);
                    dataRetorno[Math.abs(finalI - 3)][2] = String.valueOf(calculoResisRodaduraRetorno);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }


        binding.buttonCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float sumatoriaFactor = 0;
                for (int i = 0; i < checks.length; i++) {
                    if (checks[i] == 1) {
                        sumatoriaFactor += Data.factorExternoValor[i];
                    }
                }
                //Log.d("ROYER", "Elemento   "+ (11+sumatoriaFactor));
                Toast.makeText(MainActivity.this, "CMD " +spinnerPesoMaterial.getSelectedItemPosition(), Toast.LENGTH_SHORT).show();

                int n = (int) Math.round(colmada / (Double.parseDouble(binding.editTextCapacidadCucharon.getText().toString())* Data.pesoMaterialValor[spinnerPesoMaterial.getSelectedItemPosition()][2] ));
                double cms = tiempoCicloBaseValor[spinnerTiempoCicloBase.getSelectedItemPosition()] + sumatoriaFactor;
                float tiempoCarga = (float) (n * cms);
                double tiempoDescarga = tiempoDescargaValor[spinnerTiempoDescarga.getSelectedItemPosition()];
                double tiempoPosicionamiento = tiempoPosicionamientoValor[spinnerTiempoPosicionamiento.getSelectedItemPosition()];

                //Toast.makeText(MainActivity.this, "CMD " +tiempoPosicionamiento+" \n   "+cms, Toast.LENGTH_SHORT).show();


                //TABLA PRUEBAS

                dataTraslado[0][0] = "a";
                dataTraslado[1][0] = "b";
                dataTraslado[2][0] = "c";
                dataTraslado[3][0] = "d";
                dataTraslado[0][1] = binding.editTextTramoAR.getText().toString();
                dataTraslado[1][1] = binding.editTextTramoBR.getText().toString();
                dataTraslado[2][1] = binding.editTextTramoCR.getText().toString();
                dataTraslado[3][1] = binding.editTextTramoDR.getText().toString();
                dataTraslado[0][4] = binding.editTextTramoA.getText().toString();
                dataTraslado[1][4] = binding.editTextTramoB.getText().toString();
                dataTraslado[2][4] = binding.editTextTramoC.getText().toString();
                dataTraslado[3][4] = binding.editTextTramoD.getText().toString();

                dataRetorno[0][0] = "d";
                dataRetorno[1][0] = "c";
                dataRetorno[2][0] = "b";
                dataRetorno[3][0] = "a";
                dataRetorno[0][1] = binding.editTextTramoDR.getText().toString();
                dataRetorno[1][1] = binding.editTextTramoCR.getText().toString();
                dataRetorno[2][1] = binding.editTextTramoBR.getText().toString();
                dataRetorno[3][1] = binding.editTextTramoAR.getText().toString();
                dataRetorno[0][4] = binding.editTextTramoD.getText().toString();
                dataRetorno[1][4] = binding.editTextTramoC.getText().toString();
                dataRetorno[2][4] = binding.editTextTramoB.getText().toString();
                dataRetorno[3][4] = binding.editTextTramoA.getText().toString();

                for (int i = 0; i < 4; i++) {
                    //Toast.makeText(MainActivity.this, "Spinnerfdfsdf " + spinnersSentido[i].getSelectedItemPosition(), Toast.LENGTH_SHORT).show();
                    if (spinnersSentido[i].getSelectedItemPosition() == 3 || spinnersSentido[i].getSelectedItemPosition() == 1)
                        dataTraslado[i][3] = String.valueOf(Math.abs(Double.parseDouble(dataTraslado[i][2]) + Double.parseDouble(dataTraslado[i][1])));
                    else
                        dataTraslado[i][3] = String.valueOf(Math.abs(Double.parseDouble(dataTraslado[i][2]) - Double.parseDouble(dataTraslado[i][1])));

                    if (spinnersSentido[Math.abs(i - 3)].getSelectedItemPosition() == 3 || spinnersSentido[Math.abs(i - 3)].getSelectedItemPosition() == 2)
                        dataRetorno[i][3] = String.valueOf(Math.abs(Double.parseDouble(dataRetorno[i][2]) + Double.parseDouble(dataRetorno[i][1])));
                    else
                        dataRetorno[i][3] = String.valueOf(Math.abs(Double.parseDouble(dataRetorno[i][2]) - Double.parseDouble(dataRetorno[i][1])));

                }

                binding.editTextEficiencia.setText("0.9");
                eficiencia = Double.parseDouble(binding.editTextEficiencia.getText().toString());
                densidad = Data.pesoMaterialValor[spinnerPesoMaterial.getSelectedItemPosition()][spinnerPesoMaterialOpcion.getSelectedItemPosition()];



                // Crear un Intent para iniciar Activity2
                Intent intent = new Intent(MainActivity.this, ResultadoActivity.class);

                // Crear un Bundle para pasar datos
                Bundle bundle = new Bundle();

                bundle.putSerializable("matriz1", dataTraslado);
                bundle.putSerializable("matriz2", dataRetorno);
                bundle.putDouble("tiempoCarga", tiempoCarga);
                bundle.putDouble("tiempoDescarga", tiempoDescarga);
                bundle.putDouble("tiempoPosicionamiento", tiempoPosicionamiento);
                bundle.putDouble("eficiencia", eficiencia);
                bundle.putDouble("colmada", colmada);
                bundle.putDouble("densidad", densidad);
                intent.putExtras(bundle);
                startActivity(intent);

                //FastTableLayout fastTable = new FastTableLayout(getApplicationContext(), binding.myTableLayout, headersTraslado, dataTraslado);
                //fastTable.build();

                //FastTableLayout fastTableRetorno = new FastTableLayout(getApplicationContext(), binding.myTableLayout, headersTraslado, dataRetorno);
                //fastTableRetorno.build();
            }
        });

        binding.buttonSumatoriaFactor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openModal();
            }
        });
    }

    private void selectCamion() {
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, opcionesSecundarias[position]);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter);
                pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, android.view.View view, int position, long l) {
                String opcionPrincipalSeleccionada = (String) spinner1.getItemAtPosition(spinner1.getSelectedItemPosition());
                String opcionSecundariaSeleccionada = (String) spinner2.getItemAtPosition(position);
                //Toast.makeText(MainActivity.this, "Seleccionaste: " + opcionPrincipalSeleccionada + " - " + opcionSecundariaSeleccionada, Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this, "Seleccionasteeeee: " + pos, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void openModal() {
        // Crear el cuadro de diálogo
        AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_checkbox, null);

        builder.setView(view);
        LinearLayout linearLayoutOtro = view.findViewById(R.id.checkbox_group_linear);

        for (int i = 0; i < Data.factorExternoValor.length; i++) {
            check = new MaterialCheckBox(this);
            check.setId(i + 2);
            check.setText(Data.factorExterno[i]);
            if (checks[i] == 1)
                check.setChecked(true);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            linearLayoutOtro.addView(check, layoutParams);
            checkBoxs.add(check);

        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        MaterialButton buttonSave = view.findViewById(R.id.buttonSave);
        MaterialButton buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "GATOOO", Toast.LENGTH_SHORT).show();


                for (int i = 0; i < Data.factorExternoValor.length; i++) {
                    if (checkBoxs.get(i).isChecked()) {
                        checks[i] = 1;
                        //Toast.makeText(getApplicationContext(),Data.factorExterno[i],Toast.LENGTH_SHORT).show();
                    } else {
                        checks[i] = 0;
                    }
                }
                alertDialog.dismiss();
                checkBoxs.clear();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                checkBoxs.clear();
            }
        });

    }
}
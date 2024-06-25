package com.example.maquinariaproduccion;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
    private Spinner spinnerTipoMaquina, spinnerTamanoCucharon, spinnerTipoCucharon, spinnerRendimiento;
    private ArrayAdapter<String> adapterTipoMaquina, adapterTamanoCucharon, adapterTipoCucharon, adapterRendimiento;

    private String[] labelTabla = {"Tramo", "RP%", "RR%", "RT%", "V. max", "v=f.max.50/3", "Distancia", "T.total"};
    private String[] resistenciaRodadura = {"Concreto", "Asfalto", "Tierra compactada con Mantenimiento", "Tierra con poco mantenimiento", "Tierra sin mantenimiento", "Arena suelta y grava", "Tierra muy lodosa y suave"};
    private int[][] resistenciaRodaduraValor = {{18, 23}, {33, 30}, {35, 35}, {70, 50}, {110, 110}, {145, 130}, {200, 170}};
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

    private String[] headersTraslado = {"Tramo","RP%", "RR%", "RT%", "Distancia","Sentido Terreno"};


    private String[][] dataTraslado = new String[4][6];

    private String[][] dataRetorno = new String[4][6];

    private List<MaterialCheckBox> checkBoxs = new ArrayList<>();
    private MaterialCheckBox check;


    private Spinner spinnerMaquinaria, spinnerModeloMaquinaria, spinnerTiempoCicloBase, spinnerTiempoDescarga, spinnerTiempoPosicionamiento, spinnerPesoMaterial, spinnerPesoMaterialOpcion;

    private String[] tipoMaquinaria = {"Camion"};
    private String[][] modeloMaquinaria = {
            {
                    "770 - Piso plano de acero de impacto medio", "770 - Caja de cantera", "770 - Piso de doble declive de acero de impacto medio",
                    "770G - Piso plano", "770 - Caja de cantera", "770G - Piso de doble declive",
                    "772 - Piso plano de acero", "772 - Caja de cantera", "772 - Doble declive de acero",
                    "772G - Piso plano", "772G - Caja de cantera", "772G - Piso de doble declive",
                    "773E****", "773G Tier 4F - Piso plano revestido de acero",
                    "773G Tier 4f - Piso doble declive revestido de acero", "773G - Piso plano revestido de acero", "773G - Piso doble declive revestido de acero",
                    "775G Tier 4f - Piso plano revestido de acero", "775G Tier 4f - Caja cantera", "775G Tier 4f - Piso doble declive revestido de acero",
                    "775G - Piso plano revestido de acero", "775G - Caja de cantera", "775G - Piso doble declive revestido de acero",
                    "777D", "777G Tier 4f - Piso doble declive", "777G Tier 4f - Cuerpo X",
                    "777G - Piso doble declive", "777G - Cuerpo X",
                    "785C", "785D", "789C",
                    "793D Estandar(MA1)", "793D Retardacion adicional(MA2)", "793F Estandar",
                    "793F XLP", "795F CA", "797F"
            }
    };

    static final double[][] modeloMaquinariaValor = {
            {
                    25.1, 25.1, 25.1,
                    25.1, 25.1, 25.9,
                    31.3, 31.3, 31.2,
                    31.3, 31.3, 32,
                    35.2, 35,
                    35.2, 35, 35.2,
                    41.6, 41.9, 41.7,
                    41.6, 41.9, 41.7,
                    60.2, 60.2, 60.2,
                    60.2, 60.2,
                    78, 78, 105,
                    176, 176, 176,
                    176, 213, 267
            }
    };

    //double colmada = 176;
    double colmada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //MAQUINARIA
        spinnerMaquinaria = findViewById(R.id.spinnerMaquinaria);
        spinnerModeloMaquinaria = findViewById(R.id.spinnerTipo);
        spinnerTiempoCicloBase = findViewById(R.id.spinnerTiempoCicloBase);

        //CAPACIDAD CUCHARON ************************
        // Inicializar spinners
        spinnerTipoMaquina = findViewById(R.id.spinnerTipoMaquina);
        spinnerTamanoCucharon = findViewById(R.id.spinnerTamanoCucharon);
        spinnerTipoCucharon = findViewById(R.id.spinnerTipoCucharon);
        spinnerRendimiento = findViewById(R.id.spinnerRendimiento);

        //TIEMPO DESCARGA Y POSICIONAMIENTO************
        spinnerTiempoDescarga = findViewById(R.id.spinnerTiempoDescarga);
        spinnerTiempoPosicionamiento = findViewById(R.id.spinnerTiempoPosicionamiento);

        //SUMATORIA FACTOR EXTERNO*********************
        //Inicializar spinner modal-check
        spinnerPesoMaterial = findViewById(R.id.spinnerPesoMateriales);
        spinnerPesoMaterialOpcion = findViewById(R.id.spinnerPesoMaterialesOpcion);


        ArrayAdapter<String> adapterMaquinaria = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipoMaquinaria);
        adapterMaquinaria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaquinaria.setAdapter(adapterMaquinaria);

        ArrayAdapter<String> adapterModeloMaquinaria = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, modeloMaquinaria[0]);
        adapterModeloMaquinaria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModeloMaquinaria.setAdapter(adapterModeloMaquinaria);

        ArrayAdapter<String> adapterCicloBase = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiempoCicloBase);
        adapterCicloBase.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTiempoCicloBase.setAdapter(adapterCicloBase);

        ArrayAdapter<String> adapterTiempoDescarga = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiempoDescarga);
        adapterTiempoDescarga.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTiempoDescarga.setAdapter(adapterTiempoDescarga);

        ArrayAdapter<String> adapterTiempoPosicionamiento = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiempoPosicionamiento);
        adapterTiempoPosicionamiento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTiempoPosicionamiento.setAdapter(adapterTiempoPosicionamiento);

        ArrayAdapter<String> adapterPesoMaterial = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Data.pesoMaterial);
        adapterTiempoPosicionamiento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPesoMaterial.setAdapter(adapterPesoMaterial);

        ArrayAdapter<String> adapterPesoMaterialOpcion = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Data.pesoMaterialOpcion);
        adapterTiempoPosicionamiento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPesoMaterialOpcion.setAdapter(adapterPesoMaterialOpcion);

        // Inicializar adapters capacidad cucharon
        adapterTipoMaquina = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Data.tipomaquina);
        adapterTipoMaquina.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoMaquina.setAdapter(adapterTipoMaquina);

        adapterTamanoCucharon = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapterTamanoCucharon.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTamanoCucharon.setAdapter(adapterTamanoCucharon);

        adapterTipoCucharon = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapterTipoCucharon.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoCucharon.setAdapter(adapterTipoCucharon);

        adapterRendimiento = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Data.rendimiento);
        adapterRendimiento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRendimiento.setAdapter(adapterRendimiento);


        binding.editTextTramoAR.setText("0");
        binding.editTextTramoBR.setText("10");
        binding.editTextTramoCR.setText("0");
        binding.editTextTramoDR.setText("5");

        binding.editTextTramoA.setText("1000");
        binding.editTextTramoB.setText("500");
        binding.editTextTramoC.setText("1200");
        binding.editTextTramoD.setText("500");


        selectCamion();
        selectCapacidadCucharon();

        LinearLayout containerSpinners = findViewById(R.id.container_spinner1);
        Drawable styleSpinner = getResources().getDrawable(R.drawable.style_spinner_1);

        // Configurar cada Spinner en el arreglo
        for (int i = 0; i < spinnersSentido.length; i++) {
            spinnersSentido[i] = new Spinner(this);
            spinnersSentido[i].setId(i + 1); // Asignar ID dinámicamente
            spinnersSentido[i].setBackground(styleSpinner);
            spinnersSentido[i].setPadding(0, 0, 50, 0);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, resistenciaPendiente);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnersSentido[i].setAdapter(adapter);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f);
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
            spinnersTipoSuelo[i].setBackground(styleSpinner);
            spinnersTipoSuelo[i].setPadding(0, 0, 50, 0);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, resistenciaRodadura);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnersTipoSuelo[i].setAdapter(adapter);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f);
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

                if (binding.editTextEficiencia.getText().length() > 0) {

                    float sumatoriaFactor = 0;
                    for (int i = 0; i < checks.length; i++) {
                        if (checks[i] == 1) {
                            sumatoriaFactor += Data.factorExternoValor[i];
                        }
                    }
                    //Log.d("ROYER", "Elemento   "+ (11+sumatoriaFactor));
                    //Toast.makeText(MainActivity.this, "CMD " +spinnerPesoMaterial.getSelectedItemPosition(), Toast.LENGTH_SHORT).show();

                    double capacidadCucharon = Data.capacidadNominal[spinnerTipoMaquina.getSelectedItemPosition()][spinnerTamanoCucharon.getSelectedItemPosition()][spinnerTipoCucharon.getSelectedItemPosition()][spinnerRendimiento.getSelectedItemPosition()];
                    double pesomaterial = Data.pesoMaterialValor[spinnerPesoMaterial.getSelectedItemPosition()][2];
                    int n = (int) Math.round(colmada / (capacidadCucharon * pesomaterial));
                    double cms = tiempoCicloBaseValor[spinnerTiempoCicloBase.getSelectedItemPosition()] + sumatoriaFactor;
                    float tiempoCarga = (float) (n * cms);
                    double tiempoDescarga = tiempoDescargaValor[spinnerTiempoDescarga.getSelectedItemPosition()];
                    double tiempoPosicionamiento = tiempoPosicionamientoValor[spinnerTiempoPosicionamiento.getSelectedItemPosition()];

                    //Toast.makeText(MainActivity.this, "CMD " +tiempoPosicionamiento+" \n   "+cms, Toast.LENGTH_SHORT).show();
                    Log.d("ROYER", "cap cuchar " + capacidadCucharon + "\n" +
                            "n " + n + "\n" +
                            "colmada " + colmada + "\n" +
                            "pes mate " + pesomaterial + "\n" +
                            "cms " + cms + "\n" +
                            "tiempo carga " + tiempoCarga);


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

                        dataTraslado[i][5]=spinnersSentido[i].getSelectedItem().toString();
                        dataRetorno[Math.abs(i - 3)][5]=spinnersSentido[i].getSelectedItem().toString();
                    }

                    //binding.editTextEficiencia.setText("0.9");
                    eficiencia = Double.parseDouble(binding.editTextEficiencia.getText().toString());
                    if(eficiencia>=1)
                        eficiencia=eficiencia/100;
                    Toast.makeText(MainActivity.this, "Ingrese "+ eficiencia, Toast.LENGTH_SHORT).show();

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
                } else {
                    Toast.makeText(MainActivity.this, "Ingrese eficiencia ", Toast.LENGTH_SHORT).show();
                }
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
        spinnerMaquinaria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, modeloMaquinaria[position]);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerModeloMaquinaria.setAdapter(adapter);
                //pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerModeloMaquinaria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, android.view.View view, int position, long l) {
                colmada = modeloMaquinariaValor[spinnerMaquinaria.getSelectedItemPosition()][position];
                //Toast.makeText(MainActivity.this, "Seleccionasteeeee: " + colmada, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void selectCapacidadCucharon() {
        //CAPACIDAD CUCHARON *************************************
        // Listener para spinner1
        spinnerTipoMaquina.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, "QUESO : " + position, Toast.LENGTH_SHORT).show();
                updateSpinnerTamanoCucharon(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Listener para spinner2
        spinnerTamanoCucharon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateSpinnerTipoCucharon(spinnerTipoMaquina.getSelectedItemPosition(), position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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

    // Método para actualizar datos de spinner2 basados en spinner1
    private void updateSpinnerTamanoCucharon(int position) {
        adapterTamanoCucharon.clear();
        adapterTamanoCucharon.addAll(Data.tamanoCucharon[position]);
        adapterTamanoCucharon.notifyDataSetChanged();
        spinnerTamanoCucharon.setSelection(0);  // Resetear selección
        updateSpinnerTipoCucharon(position, 0);
    }

    // Método para actualizar datos de spinner3 basados en spinner1 y spinner2
    private void updateSpinnerTipoCucharon(int spinner1Position, int spinner2Position) {
        adapterTipoCucharon.clear();
        adapterTipoCucharon.addAll(Data.tipoCucharon[spinner1Position][spinner2Position]);
        adapterTipoCucharon.notifyDataSetChanged();
        spinnerTipoCucharon.setSelection(0);  // Resetear selección
    }
}
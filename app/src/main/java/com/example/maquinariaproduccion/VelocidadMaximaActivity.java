package com.example.maquinariaproduccion;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alclabs.fasttablelayout.FastTableLayout;
import com.example.maquinariaproduccion.databinding.ActivityVelocidadMaximaBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class VelocidadMaximaActivity extends AppCompatActivity {
    ActivityVelocidadMaximaBinding binding;
    String[] headersTraslado = {"Tramo", "R. Pendiente%", "R. Rodadura%", "R. Total%", "Distancia", "Sentido"};
    double[][] factorVelocidadValor = {{0.5, 0.7}, {0.6, 0.75}, {0.65, 0.80},
            {0.70, 0.80}, {0.75, 0.85}, {0.85, 0.90}};
    double tiempoTrasladoTotal = 0;
    double tiempoRetornoTotal = 0;
    double tiempoCarga, tiempoDescarga, tiempoPosicionamiento, eficiencia, colmada, densidad;
    DecimalFormat formato = new DecimalFormat("#.##");
    List<Integer> sequence = new ArrayList<>();
    private DrawingView drawingView;
    private String[] distanciaGraficoValor = new String[4];

    private LineChart lineChartVelocidadATraslado, lineChartVelocidadBTraslado, lineChartVelocidadCTraslado, lineChartVelocidadDTraslado;
    private LineChart lineChartVelocidadARetorno, lineChartVelocidadBRetorno, lineChartVelocidadCRetorno, lineChartVelocidadDRetorno;
    XAxis xAxis;
    YAxis yAxisRight;
    YAxis yAxisLeft;
    LineDataSet dataSet1, dataSet2, dataSet3;
    LineData lineData;
    ArrayList<Entry> entries1, entries2, entries3;
    int nombreMaquinaria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityVelocidadMaximaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        drawingView = findViewById(R.id.drawing_view);
        lineChartVelocidadATraslado = findViewById(R.id.lineChartVelocidadATraslado);
        lineChartVelocidadBTraslado = findViewById(R.id.lineChartVelocidadBTraslado);
        lineChartVelocidadCTraslado = findViewById(R.id.lineChartVelocidadCTraslado);
        lineChartVelocidadDTraslado = findViewById(R.id.lineChartVelocidadDTraslado);

        lineChartVelocidadARetorno = findViewById(R.id.lineChartVelocidadARetorno);
        lineChartVelocidadBRetorno = findViewById(R.id.lineChartVelocidadBRetorno);
        lineChartVelocidadCRetorno = findViewById(R.id.lineChartVelocidadCRetorno);
        lineChartVelocidadDRetorno = findViewById(R.id.lineChartVelocidadDRetorno);


        String[][] dataTraslado = new String[4][8];
        String[][] dataRetorno = new String[4][8];
        String[][] matriz1Recibida;
        String[][] matriz2Recibida;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        if (bundle != null && bundle.containsKey("matriz1")) {

            tiempoCarga = bundle.getDouble("tiempoCarga");
            tiempoDescarga = bundle.getDouble("tiempoDescarga");
            tiempoPosicionamiento = bundle.getDouble("tiempoPosicionamiento");
            eficiencia = bundle.getDouble("eficiencia");
            colmada = bundle.getDouble("colmada");
            densidad = bundle.getDouble("densidad");
            nombreMaquinaria = bundle.getInt("nombreMaquinaria");

            matriz1Recibida = (String[][]) bundle.getSerializable("matriz1");
            matriz2Recibida = (String[][]) bundle.getSerializable("matriz2");


            if (nombreMaquinaria == 31) {
                LinearLayout linearLayoutTraslado = findViewById(R.id.layoutContenedorGraficaTraslado);
                LinearLayout linearLayoutRetorno = findViewById(R.id.layoutContenedorGraficaRetorno);
                LinearLayout linearLayoutTrasladoBasico = findViewById(R.id.layoutContenedorGraficaBasicaTraslado);
                LinearLayout linearLayoutRetornoBasico = findViewById(R.id.layoutContenedorGraficaBasicaRetorno);
                linearLayoutTraslado.setVisibility(View.VISIBLE);
                linearLayoutRetorno.setVisibility(View.VISIBLE);
                linearLayoutTrasladoBasico.setVisibility(View.GONE);
                linearLayoutRetornoBasico.setVisibility(View.GONE);


                mostrarGraficaTraslado();
                mostrarGraficaRetorno();

                binding.editTextVelocidadATraslado.setText("13");
                binding.editTextVelocidadBTraslado.setText("4");
                binding.editTextVelocidadCTraslado.setText("30");
                binding.editTextVelocidadDTraslado.setText("55");

                binding.editTextVelocidadARetorno.setText("22");
                binding.editTextVelocidadBRetorno.setText("33");
                binding.editTextVelocidadCRetorno.setText("55");
                binding.editTextVelocidadDRetorno.setText("33");
            }
/*
            binding.editTextVelocidadATraslado.setText("14");
            binding.editTextVelocidadBTraslado.setText("11");
            binding.editTextVelocidadCTraslado.setText("14");
            binding.editTextVelocidadDTraslado.setText("60");

            binding.editTextVelocidadARetorno.setText("40");
            binding.editTextVelocidadBRetorno.setText("30");
            binding.editTextVelocidadCRetorno.setText("60");
            binding.editTextVelocidadDRetorno.setText("30");*/

            if (matriz1Recibida != null && matriz2Recibida != null) {

                for (int i = 0; i < matriz1Recibida.length; i++) {
                    for (int j = 0; j < matriz1Recibida[i].length; j++) {
                        dataTraslado[i][j] = matriz1Recibida[i][j];
                        dataRetorno[i][j] = matriz2Recibida[i][j];
                        //Log.d("Matriz", "Elemento [" + i + "][" + j + "] = " + matriz1Recibida[i][j]);
                        if (j == 5) {
                            if (dataTraslado[i][j].equals("Subida"))
                                sequence.add(0); // Subida diagonal
                            else if (dataTraslado[i][j].equals("Bajada"))
                                sequence.add(1); // Bajada diagonal
                            else
                                sequence.add(2); // Horizontal
                        }
                        if (j == 4) {
                            distanciaGraficoValor[i] = dataTraslado[i][j];
                        }
                    }
                }

                FastTableLayout fastTable = new FastTableLayout(getApplicationContext(), binding.myTableLayoutResultadoTraslado, headersTraslado, matriz1Recibida);
                fastTable.setSET_DEAFULT_HEADER_BORDER(true);
                fastTable.setSET_DEFAULT_HEADER_BACKGROUND(true);
                fastTable.setTABLE_TEXT_SIZE(15);
                fastTable.build();

                FastTableLayout fastTableRetorno = new FastTableLayout(getApplicationContext(), binding.myTableLayoutResultadoRetorno, headersTraslado, matriz2Recibida);
                fastTableRetorno.setSET_DEAFULT_HEADER_BORDER(true);
                fastTableRetorno.setSET_DEFAULT_HEADER_BACKGROUND(true);
                fastTableRetorno.setTABLE_TEXT_SIZE(15);
                fastTableRetorno.build();
            }
        } else {
            Log.e("Activity2", "Error: No se recibió la matriz");
        }


        binding.buttonCalcularFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //drawingView.setSequence(sequence,distanciaGraficoValor);

                dataTraslado[0][5] = binding.editTextVelocidadATraslado.getText().toString();
                dataTraslado[1][5] = binding.editTextVelocidadBTraslado.getText().toString();
                dataTraslado[2][5] = binding.editTextVelocidadCTraslado.getText().toString();
                dataTraslado[3][5] = binding.editTextVelocidadDTraslado.getText().toString();

                dataRetorno[0][5] = binding.editTextVelocidadARetorno.getText().toString();
                dataRetorno[1][5] = binding.editTextVelocidadBRetorno.getText().toString();
                dataRetorno[2][5] = binding.editTextVelocidadCRetorno.getText().toString();
                dataRetorno[3][5] = binding.editTextVelocidadDRetorno.getText().toString();

                if (binding.editTextVelocidadATraslado.getText().length() > 0 && binding.editTextVelocidadBTraslado.getText().length() > 0 &&
                        binding.editTextVelocidadCTraslado.getText().length() > 0 && binding.editTextVelocidadDTraslado.getText().length() > 0 &&
                        binding.editTextVelocidadARetorno.getText().length() > 0 && binding.editTextVelocidadBRetorno.getText().length() > 0 &&
                        binding.editTextVelocidadCRetorno.getText().length() > 0 && binding.editTextVelocidadDRetorno.getText().length() > 0) {

                    int distancia;
                    double f = 0;
                    int indicePivote = 0;
                    double tiempo;
                    double velocidad;
                    for (int i = 0; i < 4; i++) {
                        distancia = Integer.parseInt(dataTraslado[i][4]);
                        //Log.d("ROYER", "Elementos "+i+"   "+distancia);
                        if (i != 0)
                            indicePivote = 1;
                        if (0 <= distancia && distancia <= 100) {
                            f = factorVelocidadValor[0][indicePivote];
                        } else if (100 < distancia && distancia <= 250) {
                            f = factorVelocidadValor[1][indicePivote];
                        } else if (250 < distancia && distancia <= 500) {
                            f = factorVelocidadValor[2][indicePivote];
                        } else if (500 < distancia && distancia <= 750) {
                            f = factorVelocidadValor[3][indicePivote];
                        } else if (750 < distancia && distancia <= 1000) {
                            f = factorVelocidadValor[4][indicePivote];
                        } else if (1000 < distancia) {
                            f = factorVelocidadValor[5][indicePivote];
                        }
                        //Log.d("ROYER", "Elemento "+f+"   "+Double.parseDouble(dataTraslado[i][5]));

                        velocidad = f * Double.parseDouble(dataTraslado[i][5]) * 50.0 / 3;
                        tiempo = distancia / velocidad;
                        tiempoTrasladoTotal += tiempo;
                        dataTraslado[i][7] = formato.format(tiempo);
                        dataTraslado[i][6] = formato.format(velocidad);
                        Log.d("ROYER", "Velocidad " + velocidad + "\n" +
                                "Tiempo " + tiempo + " \n" +
                                "Tiempo data traslado" + dataTraslado[i][7]);

                    }
                    Log.d("ROYER", "\n TIEMPO TRASLADO  " + tiempoTrasladoTotal);
                    indicePivote = 0;
                    for (int i = 0; i < 4; i++) {
                        distancia = Integer.parseInt(dataRetorno[i][4]);
                        //Log.d("ROYER", "Elementos "+i+"   "+distancia);
                        if (i != 0)
                            indicePivote = 1;
                        if (0 <= distancia && distancia <= 100) {
                            f = factorVelocidadValor[0][indicePivote];
                        } else if (100 < distancia && distancia <= 250) {
                            f = factorVelocidadValor[1][indicePivote];
                        } else if (250 < distancia && distancia <= 500) {
                            f = factorVelocidadValor[2][indicePivote];
                        } else if (500 < distancia && distancia <= 750) {
                            f = factorVelocidadValor[3][indicePivote];
                        } else if (750 < distancia && distancia <= 1000) {
                            f = factorVelocidadValor[4][indicePivote];
                        } else if (1000 < distancia) {
                            f = factorVelocidadValor[5][indicePivote];
                        }
                        velocidad = f * Double.parseDouble(dataRetorno[i][5]) * 50.0 / 3;
                        tiempo = distancia / velocidad;
                        tiempoRetornoTotal += tiempo;
                        dataRetorno[i][7] = formato.format(tiempo);
                        //Log.d("ROYER", "Elemento "+f+"   "+Double.parseDouble(dataTraslado[i][5]));
                        dataRetorno[i][6] = formato.format(velocidad);
                    }

                    Log.d("ROYER", "Elemento " + tiempoTrasladoTotal + "   " + tiempoRetornoTotal);

                    // Crear un Intent para iniciar Activity2
                    Intent intent = new Intent(VelocidadMaximaActivity.this, ResultadoActivity.class);

                    // Crear un Bundle para pasar datos
                    Bundle bundle = new Bundle();

                    bundle.putSerializable("dataTraslado", dataTraslado);
                    bundle.putSerializable("dataRetorno", dataRetorno);
                    bundle.putDouble("tiempoCarga", tiempoCarga);
                    bundle.putDouble("tiempoDescarga", tiempoDescarga);
                    bundle.putDouble("tiempoPosicionamiento", tiempoPosicionamiento);
                    bundle.putDouble("eficiencia", eficiencia);
                    bundle.putDouble("colmada", colmada);
                    bundle.putDouble("densidad", densidad);
                    bundle.putDouble("tiempoTrasladoTotal", tiempoTrasladoTotal);
                    bundle.putDouble("tiempoRetornoTotal", tiempoRetornoTotal);
                    bundle.putIntegerArrayList("sequence", (ArrayList<Integer>) sequence);
                    bundle.putSerializable("distanciaGraficoValor", distanciaGraficoValor);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Complete los campos vacios de velocidad", Toast.LENGTH_SHORT).show();
                }
                /*
                FastTableLayout fastTable = new FastTableLayout(getApplicationContext(), binding.myTableLayoutResultadoTrasladoFinal, headersTrasladoCompleto, dataTraslado);
                fastTable.setSET_DEAFULT_HEADER_BORDER(true);
                fastTable.setSET_DEFAULT_HEADER_BACKGROUND(true);
                fastTable.setTABLE_TEXT_SIZE(15);
                fastTable.build();

                FastTableLayout fastTableRetorno = new FastTableLayout(getApplicationContext(), binding.myTableLayoutResultadoRetornoFinal, headersTrasladoCompleto, dataRetorno);
                fastTableRetorno.setSET_DEAFULT_HEADER_BORDER(true);
                fastTableRetorno.setSET_DEFAULT_HEADER_BACKGROUND(true);
                fastTableRetorno.setTABLE_TEXT_SIZE(15);
                fastTableRetorno.build();

                double tiempoCicloCamion = tiempoCarga + tiempoDescarga + tiempoPosicionamiento + tiempoTrasladoTotal + tiempoRetornoTotal;
                binding.textViewTiempoTrasladoTotal.setText("Tiempo Traslado Total  Tt: " + formato.format(tiempoTrasladoTotal));
                binding.textViewTiempoRetornoTotal.setText("Tiempo Retorno Total  Tr: " + formato.format(tiempoRetornoTotal));
                binding.textViewTiempoCicloCamion.setText("Tiempo Ciclo Camion  Cm: " + formato.format(tiempoCicloCamion));
                binding.textViewProduccionHoraria.setText("Produccion Horaria  Q: " + formato.format(colmada * eficiencia * 60 * densidad / tiempoCicloCamion));

                LinearLayout linearLayout = findViewById(R.id.resultadoLayout);
                linearLayout.setVisibility(View.VISIBLE);*/
            }
        });

// Establecer la imagen basada en el número aleatorio generado
        // int drawableResourceId = getResources().getIdentifier("image" + randomNumber, "drawable", getPackageName());
        //imageView.setImageResource(drawableResourceId);
    }

    private void mostrarGraficaTraslado() {

        float[] coordenadaATraslado = {0, 0, 40, 3, 0, 2.5f, 40, 2.5f, 30, 0, 30, 2.5f};
        float[] coordenadaBTraslado = {0, 0, 40, 18, 0, 15, 40, 15, 4, 0, 4, 15};
        float[] coordenadaCTraslado = {0, 0, 40, 3, 0, 2.5f, 40, 2.5f, 30, 0, 30, 2.5f};
        float[] coordenadaDTraslado = {0, 0, 80, 1, 0, 1, 80, 1, 55, 0, 55, 1};


        lineChart(lineChartVelocidadATraslado, coordenadaATraslado);
        lineChart(lineChartVelocidadBTraslado, coordenadaBTraslado);
        lineChart(lineChartVelocidadCTraslado, coordenadaCTraslado);
        lineChart(lineChartVelocidadDTraslado, coordenadaDTraslado);
    }

    private void mostrarGraficaRetorno() {

        float[] coordenadaDRetorno = {0, 0, 40, 11, 14, 3.5f, 40, 3.5f, 22, 0, 22, 3.5f};
        float[] coordenadaCRetorno = {0, 0, 40, 3, 14, 1, 40, 1, 33, 0, 33, 1};
        float[] coordenadaBRetorno = {0, 0, 80, 6, 28, 2, 80, 2, 55, 0, 55, 2};
        float[] coordenadaARetorno = {0, 0, 40, 3, 14, 1, 40, 1, 33, 0, 33, 1};


        lineChart(lineChartVelocidadARetorno, coordenadaARetorno);
        lineChart(lineChartVelocidadBRetorno, coordenadaBRetorno);
        lineChart(lineChartVelocidadCRetorno, coordenadaCRetorno);
        lineChart(lineChartVelocidadDRetorno, coordenadaDRetorno);
    }

    private void lineChart(LineChart lineChart, float[] punto) {

        entries1 = new ArrayList<>();
        entries1.add(new Entry(punto[0], punto[1]));
        entries1.add(new Entry(punto[2], punto[3]));

        entries2 = new ArrayList<>();
        entries2.add(new Entry(punto[4], punto[5]));
        entries2.add(new Entry(punto[6], punto[7]));

        entries3 = new ArrayList<>();
        entries3.add(new Entry(punto[8], punto[9]));
        entries3.add(new Entry(punto[10], punto[11]));

        dataSet1 = new LineDataSet(entries1, "Trazo 1");
        dataSet1.setColor(Color.BLUE);
        dataSet1.setValueTextColor(Color.BLACK);

        dataSet2 = new LineDataSet(entries2, "Trazo 2");
        dataSet2.setColor(Color.RED);
        dataSet2.setValueTextColor(Color.BLACK);

        dataSet3 = new LineDataSet(entries3, "Trazo 3");
        dataSet3.setColor(Color.GREEN);
        dataSet3.setValueTextColor(Color.BLACK);

        lineData = new LineData(dataSet1, dataSet2, dataSet3);
        lineChart.setData(lineData);

        // Customize X axis
        xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // Customize Y axis
        yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setTextColor(Color.BLACK);

        lineChart.invalidate(); // refresh
    }

}
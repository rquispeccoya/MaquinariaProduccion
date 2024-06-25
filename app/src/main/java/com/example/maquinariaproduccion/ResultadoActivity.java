package com.example.maquinariaproduccion;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alclabs.fasttablelayout.FastTableLayout;
import com.example.maquinariaproduccion.databinding.ActivityMainBinding;
import com.example.maquinariaproduccion.databinding.ActivityResultadoBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ResultadoActivity extends AppCompatActivity {
    ActivityResultadoBinding binding;
    String[] headersTraslado = {"Tramo", "R. Pendiente%", "R. Rodadura%", "R. Total%", "Distancia", "Sentido"};
    String[] headersTrasladoCompleto = {"Tramo", "R.Pend.%", "R. Rodadura%", "R. Total%", "Dist.", "V.max", "Vel.", "Tiempo"};
    double[][] factorVelocidadValor = {{0.5, 0.7}, {0.6, 0.75}, {0.65, 0.80},
            {0.70, 0.80}, {0.75, 0.85}, {0.85, 0.90}};

    double tiempoTrasladoTotal = 0;
    double tiempoRetornoTotal = 0;
    double tiempoCarga, tiempoDescarga, tiempoPosicionamiento, eficiencia, colmada, densidad;
    DecimalFormat formato = new DecimalFormat("#.##");
    List<Integer> sequence = new ArrayList<>();
    private DrawingView drawingView;
    private String []distanciaGraficoValor= new String[4];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityResultadoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        drawingView = findViewById(R.id.drawing_view);

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
            matriz1Recibida = (String[][]) bundle.getSerializable("matriz1");
            matriz2Recibida = (String[][]) bundle.getSerializable("matriz2");

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
                        if(j==4){
                            distanciaGraficoValor[i]= dataTraslado[i][j];
                        }
                    }
                }

                for(int i=0;i<sequence.size();i++){
                    Toast.makeText(ResultadoActivity.this, "POSCION"+  sequence.get(i) , Toast.LENGTH_SHORT).show();
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

        binding.editTextVelocidadATraslado.setText("14");
        binding.editTextVelocidadBTraslado.setText("11");
        binding.editTextVelocidadCTraslado.setText("14");
        binding.editTextVelocidadDTraslado.setText("60");

        binding.editTextVelocidadARetorno.setText("40");
        binding.editTextVelocidadBRetorno.setText("30");
        binding.editTextVelocidadCRetorno.setText("60");
        binding.editTextVelocidadDRetorno.setText("30");

        binding.buttonCalcularFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawingView.setSequence(sequence,distanciaGraficoValor);

                dataTraslado[0][5] = binding.editTextVelocidadATraslado.getText().toString();
                dataTraslado[1][5] = binding.editTextVelocidadBTraslado.getText().toString();
                dataTraslado[2][5] = binding.editTextVelocidadCTraslado.getText().toString();
                dataTraslado[3][5] = binding.editTextVelocidadDTraslado.getText().toString();

                dataRetorno[0][5] = binding.editTextVelocidadARetorno.getText().toString();
                dataRetorno[1][5] = binding.editTextVelocidadBRetorno.getText().toString();
                dataRetorno[2][5] = binding.editTextVelocidadCRetorno.getText().toString();
                dataRetorno[3][5] = binding.editTextVelocidadDRetorno.getText().toString();

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
                }

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
                linearLayout.setVisibility(View.VISIBLE);
            }
        });

// Establecer la imagen basada en el número aleatorio generado
        // int drawableResourceId = getResources().getIdentifier("image" + randomNumber, "drawable", getPackageName());
        //imageView.setImageResource(drawableResourceId);
    }

}
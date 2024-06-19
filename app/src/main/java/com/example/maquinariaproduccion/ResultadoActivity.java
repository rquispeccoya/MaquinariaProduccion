package com.example.maquinariaproduccion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alclabs.fasttablelayout.FastTableLayout;
import com.example.maquinariaproduccion.databinding.ActivityMainBinding;
import com.example.maquinariaproduccion.databinding.ActivityResultadoBinding;

import java.text.DecimalFormat;

public class ResultadoActivity extends AppCompatActivity {
    ActivityResultadoBinding binding;
    String[] headersTraslado = {"Tramo", "RP%", "RR%", "RT%", "Distancia"};
    String[] headersTrasladoCompleto = {"Tramo", "RP%", "RR%", "RT%", "D","V.max","V.","T"};
    double [][] factorVelocidadValor ={{0.5,0.7},{0.6,0.75},{0.65,0.80},
            {0.70,0.80},{0.75,0.85},{0.85,0.90}};

    double tiempoTrasladoTotal=0;
    double tiempoRetornoTotal=0;
    DecimalFormat formato = new DecimalFormat("#.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityResultadoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String[][] dataTraslado = new String[4][8];
        String[][] dataRetorno = new String[4][8];
        String[][] matriz1Recibida;
        String[][] matriz2Recibida;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null && bundle.containsKey("matriz1")) {
            matriz1Recibida = (String[][]) bundle.getSerializable("matriz1");
            matriz2Recibida = (String[][]) bundle.getSerializable("matriz2");

            if (matriz1Recibida != null && matriz2Recibida != null) {

                for (int i = 0; i < matriz1Recibida.length; i++) {
                    for (int j = 0; j < matriz1Recibida[i].length; j++) {
                        dataTraslado[i][j]= matriz1Recibida[i][j];
                        dataRetorno[i][j]= matriz2Recibida[i][j];
                        //Log.d("Matriz", "Elemento [" + i + "][" + j + "] = " + matriz1Recibida[i][j]);
                    }
                }

                FastTableLayout fastTable = new FastTableLayout(getApplicationContext(), binding.myTableLayoutResultadoTraslado, headersTraslado, matriz1Recibida);
                fastTable.build();

                FastTableLayout fastTableRetorno = new FastTableLayout(getApplicationContext(), binding.myTableLayoutResultadoRetorno, headersTraslado, matriz2Recibida);
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
                dataTraslado[0][5]=binding.editTextVelocidadATraslado.getText().toString();
                dataTraslado[1][5]=binding.editTextVelocidadBTraslado.getText().toString();
                dataTraslado[2][5]=binding.editTextVelocidadCTraslado.getText().toString();
                dataTraslado[3][5]=binding.editTextVelocidadDTraslado.getText().toString();

                dataRetorno[0][5]=binding.editTextVelocidadARetorno.getText().toString();
                dataRetorno[1][5]=binding.editTextVelocidadBRetorno.getText().toString();
                dataRetorno[2][5]=binding.editTextVelocidadCRetorno.getText().toString();
                dataRetorno[3][5]=binding.editTextVelocidadDRetorno.getText().toString();

                int distancia;
                double f=0;
                int indicePivote=0;
                double tiempo;
                double velocidad;
                for (int i=0;i<4;i++){
                    distancia =Integer.parseInt(dataTraslado[i][4]);
                    //Log.d("ROYER", "Elementos "+i+"   "+distancia);
                    if(i!=0)
                        indicePivote=1;
                    if(0<=distancia&&distancia<=100){
                        f=factorVelocidadValor[0][indicePivote];
                    }else if(100<distancia&&distancia<=250){
                        f=factorVelocidadValor[1][indicePivote];
                    }else if(250<distancia&&distancia<=500){
                        f=factorVelocidadValor[2][indicePivote];
                    }else if(500<distancia&&distancia<=750){
                        f=factorVelocidadValor[3][indicePivote];
                    }else if(750<distancia&&distancia<=1000){
                        f=factorVelocidadValor[4][indicePivote];
                    }else if(1000<distancia){
                        f=factorVelocidadValor[5][indicePivote];
                    }
                    //Log.d("ROYER", "Elemento "+f+"   "+Double.parseDouble(dataTraslado[i][5]));

                    velocidad = f*Double.parseDouble(dataTraslado[i][5])*50.0/3;
                    tiempo=distancia/velocidad;
                    tiempoTrasladoTotal+=tiempo;
                    dataTraslado[i][7]=formato.format(tiempo);
                    dataTraslado[i][6]= formato.format(velocidad);
                }

                indicePivote=0;
                for (int i=0;i<4;i++){
                    distancia =Integer.parseInt(dataRetorno[i][4]);
                    //Log.d("ROYER", "Elementos "+i+"   "+distancia);
                    if(i!=0)
                        indicePivote=1;
                    if(0<=distancia&&distancia<=100){
                        f=factorVelocidadValor[0][indicePivote];
                    }else if(100<distancia&&distancia<=250){
                        f=factorVelocidadValor[1][indicePivote];
                    }else if(250<distancia&&distancia<=500){
                        f=factorVelocidadValor[2][indicePivote];
                    }else if(500<distancia&&distancia<=750){
                        f=factorVelocidadValor[3][indicePivote];
                    }else if(750<distancia&&distancia<=1000){
                        f=factorVelocidadValor[4][indicePivote];
                    }else if(1000<distancia){
                        f=factorVelocidadValor[5][indicePivote];
                    }
                    velocidad = f*Double.parseDouble(dataRetorno[i][5])*50.0/3;
                    tiempo =distancia/velocidad;
                    tiempoRetornoTotal+=tiempo;
                    dataRetorno[i][7]=formato.format(tiempo);
                    //Log.d("ROYER", "Elemento "+f+"   "+Double.parseDouble(dataTraslado[i][5]));
                    dataRetorno[i][6]= formato.format(velocidad);
                }
                Log.d("ROYER", "Elemento "+tiempoTrasladoTotal+"   "+tiempoRetornoTotal);

                FastTableLayout fastTable = new FastTableLayout(getApplicationContext(), binding.myTableLayoutResultadoTrasladoFinal, headersTrasladoCompleto, dataTraslado);
                fastTable.build();

                FastTableLayout fastTableRetorno = new FastTableLayout(getApplicationContext(), binding.myTableLayoutResultadoRetornoFinal, headersTrasladoCompleto, dataRetorno);
                fastTableRetorno.build();

            }
        });


    }
}
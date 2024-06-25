package com.example.maquinariaproduccion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alclabs.fasttablelayout.FastTableLayout;
import com.example.maquinariaproduccion.databinding.ActivityResultadoBinding;
import com.github.mikephil.charting.charts.LineChart;

import java.text.DecimalFormat;
import java.util.List;

public class ResultadoActivity extends AppCompatActivity {

    ActivityResultadoBinding binding;
    String[] headersTrasladoCompleto = {"Tramo", "R.Pend.%", "R. Rodadura%", "R. Total%", "Dist.", "V.max", "Vel.", "Tiempo"};

    private DrawingView drawingView;
    DecimalFormat formato = new DecimalFormat("#.##");
    String[][] matriz1Recibida;
    String[][] matriz2Recibida;
    double tiempoCarga, tiempoDescarga, tiempoPosicionamiento, eficiencia, colmada, densidad;
    double tiempoTrasladoTotal,tiempoRetornoTotal;
    private String []distanciaGraficoValor;
    List<Integer> sequence;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultadoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        drawingView = findViewById(R.id.drawing_view);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        tiempoCarga = bundle.getDouble("tiempoCarga");
        tiempoDescarga = bundle.getDouble("tiempoDescarga");
        tiempoPosicionamiento = bundle.getDouble("tiempoPosicionamiento");
        eficiencia = bundle.getDouble("eficiencia");
        colmada = bundle.getDouble("colmada");
        densidad = bundle.getDouble("densidad");
        tiempoTrasladoTotal = bundle.getDouble("tiempoTrasladoTotal");
        tiempoRetornoTotal = bundle.getDouble("tiempoRetornoTotal");
        matriz1Recibida = (String[][]) bundle.getSerializable("dataTraslado");
        matriz2Recibida = (String[][]) bundle.getSerializable("dataRetorno");

        sequence = bundle.getIntegerArrayList("sequence");
        distanciaGraficoValor =(String[]) bundle.getSerializable("distanciaGraficoValor");

        drawingView.setSequence(sequence,distanciaGraficoValor);

        FastTableLayout fastTable = new FastTableLayout(getApplicationContext(), binding.myTableLayoutResultadoTrasladoFinal, headersTrasladoCompleto, matriz1Recibida);
        fastTable.setSET_DEAFULT_HEADER_BORDER(true);
        fastTable.setSET_DEFAULT_HEADER_BACKGROUND(true);
        fastTable.setTABLE_TEXT_SIZE(15);
        fastTable.build();

        FastTableLayout fastTableRetorno = new FastTableLayout(getApplicationContext(), binding.myTableLayoutResultadoRetornoFinal, headersTrasladoCompleto, matriz2Recibida);
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
        //linearLayout.setVisibility(View.VISIBLE);

    }
}
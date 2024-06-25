package com.example.maquinariaproduccion;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DrawingView extends View {

    private List<List<Integer>> linesHistory = new ArrayList<>();
    private List<Integer> currentLine = new ArrayList<>();
    private Paint paint = new Paint();
    private Path path = new Path();
    Context context;
    private String [] distancia;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        //paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLACK); // Color del texto
        paint.setTextSize(50); // Tamaño del texto
    }

    public void setSequence(List<Integer> sequence, String[] distancia) {
        linesHistory.add(new ArrayList<>(sequence));
        this.distancia= distancia.clone();
        invalidate(); // Redraw the view
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (List<Integer> line : linesHistory) {
            drawLine(canvas, line);
        }

        drawLine(canvas, currentLine);
    }

    private void drawLine(Canvas canvas, List<Integer> line) {
        if (line.size() < 2) return;

        path.reset();
        int startX = 0;
        int startY = getHeight()-30 ;
        int sizeLines = getWidth()/line.size();
        path.moveTo(startX, startY);
        Random rnd = new Random();
        //int color= Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        int color;
        int i=0;
        for (Integer option : line) {
            float endX = startX;
            float endY = startY;
            color= Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

            switch (option) {
                case 0: // Subida diagonal
                    path.lineTo(startX + sizeLines, startY - sizeLines);
                    startX += sizeLines;
                    startY -= sizeLines;
                    break;
                case 1: // Bajada diagonal
                    path.lineTo(startX +sizeLines, startY + sizeLines);
                    startX += sizeLines;
                    startY += sizeLines;
                    break;
                case 2: // Horizontal
                    path.lineTo(startX + sizeLines, startY);
                    startX += sizeLines;
                    break;
            }
            float midX = (startX + endX) / 2;
            float midY = (startY + endY) / 2;
            paint.setColor(color);

            // Dibujar texto junto a cada línea
            canvas.drawText(distancia[i], midX, midY, paint);
            i++;
            canvas.drawPath(path, paint);
        }
    }

    public void clearLines() {
        linesHistory.clear();
        invalidate(); // Redraw the view after clearing
    }
}


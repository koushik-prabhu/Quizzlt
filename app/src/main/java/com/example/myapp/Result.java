package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Result extends AppCompatActivity {

    TextView accuracy, mark, heading;
    PieChart chart;
    ArrayList<PieEntry> entry = new ArrayList<>();
    ArrayList<Integer> colors = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        chart = findViewById(R.id.chart);
        accuracy = findViewById(R.id.accuracy);
        mark = findViewById(R.id.marked);
        heading = findViewById(R.id.heading);

        Intent intent = getIntent();
        String main = intent.getStringExtra("main");
        String sub = intent.getStringExtra("sub");
        int answered = intent.getIntExtra("answered", 0);
        int unattempted = intent.getIntExtra("unattempted", 0) * 10;
        int marked = intent.getIntExtra("marked", 0);
        int correct = intent.getIntExtra("correct", 0) * 10;
        int wrong = (100 - correct) - unattempted;

        accuracy.setText("Accuracy : " + correct + "%");
        mark.setText("" + marked);
        heading.setText(main + " -> " + sub);

        entry.add(new PieEntry(correct, ""));
        entry.add(new PieEntry(wrong, ""));
        entry.add(new PieEntry(unattempted, ""));


        colors.add(getResources().getColor(R.color.pie_green));
        colors.add(getResources().getColor(R.color.pie_red));
        colors.add(getResources().getColor(R.color.pie_grey));

        PieDataSet set = new PieDataSet(entry, "");
        PieData data = new PieData(set);
        set.setColors(colors);
        data.setValueTextSize(15f);
        chart.setData(data);
        chart.invalidate();
        chart.setCenterText("Summary");
        chart.setCenterTextSize(20f);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
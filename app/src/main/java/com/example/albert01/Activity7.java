package com.example.albert01;

import android.graphics.Color;
import android.os.Bundle;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.util.ArrayList;

public class Activity7 extends com.example.albert01.MasterActivity {

    private LineChart myChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_7);

        myChart = findViewById(R.id.lineChart);

        showData();
    }

    private void showData() {
        ArrayList<Entry> vals = new ArrayList<>();

        vals.add(new Entry(1, 50f));
        vals.add(new Entry(2, 80f));
        vals.add(new Entry(3, 65f));
        vals.add(new Entry(4, 90f));
        vals.add(new Entry(5, 70f));

        LineDataSet ds = new LineDataSet(vals, "נתונים");

        ds.setColor(Color.rgb(255, 128, 0));
        ds.setCircleColor(Color.rgb(255, 128, 0));
        ds.setLineWidth(2f);
        ds.setCircleRadius(4f);
        ds.setDrawValues(false);

        XAxis x = myChart.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM);

        myChart.getAxisRight().setEnabled(false);
        myChart.getDescription().setEnabled(false);

        LineData d = new LineData(ds);
        myChart.setData(d);
        myChart.invalidate();
    }
}
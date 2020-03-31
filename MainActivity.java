package com.ex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private String[] iteration = {"100", "200", "500", "1000"};
    private String[] speed = {"0.001", "0.01", "0.05", "0.1", "0.2", "0.3"};
    private String[] time = {"0.5", "1", "2", "5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner s = findViewById(R.id.S);
        Spinner t = findViewById(R.id.T);
        Spinner n = findViewById(R.id.N);

        s.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_item, speed));
        t.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_item, time));
        n.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_item, iteration));
    }

    public void onClick(View v) {
        Spinner s = findViewById(R.id.S);
        Spinner t = findViewById(R.id.T);
        Spinner n = findViewById(R.id.N);
        TextView w1 = findViewById(R.id.W1);
        TextView w2 = findViewById(R.id.W2);
        TextView ts = findViewById(R.id.TS);
        TextView ns = findViewById(R.id.NS);
        boolean ok = false;
        int iterationSpend = 0;
        int expectedOutput[] = {1, 1, 0, 0}; // 1 - >threshold, 0 - <threshold
        int threshold = Integer.parseInt(((EditText)findViewById(R.id.P)).getText().toString());
        int inputs[][] = {
                {Integer.parseInt(((EditText)findViewById(R.id.A1)).getText().toString()), Integer.parseInt(((EditText)findViewById(R.id.A2)).getText().toString())},
                {Integer.parseInt(((EditText)findViewById(R.id.B1)).getText().toString()), Integer.parseInt(((EditText)findViewById(R.id.B2)).getText().toString())},
                {Integer.parseInt(((EditText)findViewById(R.id.C1)).getText().toString()), Integer.parseInt(((EditText)findViewById(R.id.C2)).getText().toString())},
                {Integer.parseInt(((EditText)findViewById(R.id.D1)).getText().toString()), Integer.parseInt(((EditText)findViewById(R.id.D2)).getText().toString())}
        };
        long startTime, timeSpend = 0;
        double[] weights = {0, 0};

        startTime = System.nanoTime();
        while (timeSpend < Double.parseDouble(t.getSelectedItem().toString())*Math.pow(10, 9) && iterationSpend < Integer.parseInt(n.getSelectedItem().toString()) && !ok) {
            ok = true;
            for (int i = 0; i < inputs.length; i++) {
                double y = inputs[i][0] * weights[0] + inputs[i][1] * weights[1];

                if ((expectedOutput[i] == 1 && y <= threshold) || (expectedOutput[i] == 0 && y > threshold)) {
                    double delta = threshold - y;

                    ok = false;
                    weights[0] += delta * inputs[i][0] * Double.parseDouble(s.getSelectedItem().toString());
                    weights[1] += delta * inputs[i][1] * Double.parseDouble(s.getSelectedItem().toString());
                }
                iterationSpend++;
            }

            timeSpend = System.nanoTime() - startTime;
        }

        DecimalFormat df = new DecimalFormat("0.000");
        ts.setText(df.format((double)timeSpend/Math.pow(10, 9)));
        ns.setText(Integer.toString(iterationSpend));
        w1.setText(df.format(weights[0]));
        w2.setText(df.format(weights[1]));
    }
}

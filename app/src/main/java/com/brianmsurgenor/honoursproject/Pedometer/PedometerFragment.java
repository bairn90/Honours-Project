package com.brianmsurgenor.honoursproject.Pedometer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.brianmsurgenor.honoursproject.R;

public class PedometerFragment extends Fragment implements View.OnClickListener{

    private TextView steps, miles, stepsPerHour, milesPerHour, calories;
    private Button resetStats, startStop;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pedometer, container, false);

        steps = (TextView) view.findViewById(R.id.stepCount);
        miles = (TextView) view.findViewById(R.id.mileCount);
        stepsPerHour = (TextView) view.findViewById(R.id.stepsPerHour);
        milesPerHour = (TextView) view.findViewById(R.id.milesPerHour);
        calories = (TextView) view.findViewById(R.id.caloriesBurned);

        resetStats = (Button) view.findViewById(R.id.resetStats);
        startStop = (Button) view.findViewById(R.id.startStopPedometer);
        resetStats.setOnClickListener(this);
        startStop.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.resetStats) {
            steps.setText("0");
            stepsPerHour.setText("0");
            miles.setText("0");
            milesPerHour.setText("0");
            calories.setText("0");
        }

        if(v.getId() == R.id.startStopPedometer) {
            if(startStop.getText().equals("Start Pedometer")) {
                startStop.setText("Stop Pedometer");
                Toast.makeText(getActivity(), "Started", Toast.LENGTH_SHORT).show();
            } else {
                startStop.setText("Start Pedometer");
                Toast.makeText(getActivity(), "Stopped", Toast.LENGTH_SHORT).show();
            }
        }

    }
}

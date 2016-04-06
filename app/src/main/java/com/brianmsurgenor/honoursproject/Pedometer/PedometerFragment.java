package com.brianmsurgenor.honoursproject.Pedometer;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.brianmsurgenor.honoursproject.DBContracts.ExerciseContract;
import com.brianmsurgenor.honoursproject.DBContracts.TrophyContract;
import com.brianmsurgenor.honoursproject.DBContracts.UserContract;
import com.brianmsurgenor.honoursproject.IStepService;
import com.brianmsurgenor.honoursproject.IStepServiceCallback;
import com.brianmsurgenor.honoursproject.R;
import com.brianmsurgenor.honoursproject.Trophy.Trophies;

import java.util.Calendar;
import java.util.logging.Logger;

public class PedometerFragment extends Fragment implements View.OnClickListener {

    private static TextView txtSteps;
    private static TextView txtMiles;
    private TextView txtStepsPerHour;
    private TextView milesPerHour;
    private TextView calories;
    private static Button startStop;
    private static int customColour = 0;
    private static ContentResolver mContentResolver;
    private Cursor mCursor;
    private static final double stepInch = 25.00;
    private static final double inchesInMile = 63359.00;
    private static Trophies trophyWins;

    private static PowerManager powerManager = null;
    private static PowerManager.WakeLock wakeLock = null;
    public static IStepService mService = null;
    public static Intent stepServiceIntent = null;
    private static int sensitivity = 250;
    private static int currentSteps = 0;
    private View view;
    private static final Logger logger = Logger.getLogger(PedometerFragment.class.getSimpleName());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentResolver = getActivity().getContentResolver();
        trophyWins = new Trophies(getActivity());

        powerManager = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TESTING");

        if (stepServiceIntent == null) {
            Bundle extras = new Bundle();
            extras.putInt("int", 1);
            stepServiceIntent = new Intent(getActivity(), StepService.class);
            stepServiceIntent.putExtras(extras);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_pedometer, container, false);

        txtSteps = (TextView) view.findViewById(R.id.stepCount);
        txtMiles = (TextView) view.findViewById(R.id.mileCount);
        txtStepsPerHour = (TextView) view.findViewById(R.id.stepsPerHour);
        milesPerHour = (TextView) view.findViewById(R.id.milesPerHour);
        calories = (TextView) view.findViewById(R.id.caloriesBurned);

        startStop = (Button) view.findViewById(R.id.startStopPedometer);
        startStop.setOnClickListener(this);

        mCursor = mContentResolver.query(UserContract.URI_TABLE, null, null, null, null);
        if(mCursor.moveToFirst()) {
            customColour = mCursor.getInt(mCursor.getColumnIndex(UserContract.Columns.CUSTOM_COLOUR));
        }

        if(customColour != 0) {
            colourChange(customColour);
        }

        if (mService != null) {
            startStop.setText("Stop Pedometer");
        }

        currentSteps = getPedometerStats();
        txtSteps.setText(currentSteps + "");

        return view;
    }

    public static void colourChange(int colour) {
        customColour = colour;
        startStop.setBackgroundColor(customColour);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.startStopPedometer) {
            if (startStop.getText().equals("Start Pedometer")) {
                startStop.setText("Stop Pedometer");
                Toast.makeText(getActivity(), "Started", Toast.LENGTH_SHORT).show();
                StepDetector.setSensitivity(sensitivity);
                start();
            } else {

                updatePedometerDB();

                startStop.setText("Start Pedometer");
                Toast.makeText(getActivity(), "Stopped", Toast.LENGTH_SHORT).show();
                stop();
            }
        }

    }

    private int getPedometerStats() {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        month++;
        String date = c.get(Calendar.DAY_OF_MONTH) + "/" + month + "/" + c.get(Calendar.YEAR);
        String[] projection = {ExerciseContract.Columns.EXERCISE};
        String filter = ExerciseContract.Columns.DATE + " = '" + date + "'";

        mCursor = mContentResolver.query(ExerciseContract.URI_TABLE,projection,filter,null,null);

        if(mCursor.moveToFirst()) {
            return mCursor.getInt(mCursor.getColumnIndex(ExerciseContract.Columns.EXERCISE));
        } else {
            return 0;
        }
    }

    private void updatePedometerDB() {

        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        month++;
        String date = c.get(Calendar.DAY_OF_MONTH) + "/" + month + "/" + c.get(Calendar.YEAR);
        String[] projection = {ExerciseContract.Columns.DATE};
        String filter = ExerciseContract.Columns.DATE + " = '" + date + "'";

        mCursor = mContentResolver.query(ExerciseContract.URI_TABLE,projection,filter,null,null);

        ContentValues values = new ContentValues();
        values.put(ExerciseContract.Columns.EXERCISE, txtSteps.getText().toString() + " Steps");

        if(mCursor.moveToFirst()) {
            mContentResolver.update(ExerciseContract.URI_TABLE,values,filter,null);
        } else {
            values.put(ExerciseContract.Columns.DATE, date);
            Uri uri = mContentResolver.insert(ExerciseContract.URI_TABLE, values);
        }

        //Check for trophy win
        String[] trophyProjection = {TrophyContract.Columns.ACHIEVED};
        String where = TrophyContract.Columns.TROPHY_NAME + " = '" + Trophies.TrophyDetails.firstWalk[0] + "'";
        Cursor mCursor = mContentResolver.query(TrophyContract.URI_TABLE, trophyProjection , where, null, null);

        if(mCursor.moveToFirst()) {
            if(mCursor.getInt(mCursor.getColumnIndex(TrophyContract.Columns.ACHIEVED)) == 0) {
                trophyWins.winTrophy(Trophies.TrophyDetails.firstWalk[0], null);
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        if (!wakeLock.isHeld()) wakeLock.acquire();

        // Bind without starting the service
        try {
            getActivity().bindService(stepServiceIntent, mConnection, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (wakeLock.isHeld()) wakeLock.release();

        unbindStepService();
    }

    private void start() {
        logger.info("start");

        startStepService();
        bindStepService();
    }

    private void stop() {
        logger.info("stop");

        unbindStepService();
        stopStepService();
    }

    private void startStepService() {
        try {
            getActivity().startService(stepServiceIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopStepService() {
        try {
            getActivity().stopService(stepServiceIntent);
        } catch (Exception e) {
            logger.info("111");
        }
    }

    private void bindStepService() {
        try {
            getActivity().bindService(stepServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unbindStepService() {
        try {
            getActivity().unbindService(mConnection);
        } catch (Exception e) {
            logger.info("222");
        }
    }

    private static final Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            int current = msg.arg1;
            double inches = current * stepInch;
            logger.info("Inches = " + inches);
            double miles = inches / inchesInMile;
            logger.info("Miles = " + miles);
            txtSteps.setText("" + current);
            txtMiles.setText(String.format("%.2f",miles));

            if(miles > 1.0) {
                //Check for trophy win
                String[] trophyProjection = {TrophyContract.Columns.ACHIEVED};
                String where = TrophyContract.Columns.TROPHY_NAME + " = '" + Trophies.TrophyDetails.longWalk[0] + "'";
                Cursor mCursor = mContentResolver.query(TrophyContract.URI_TABLE, trophyProjection , where, null, null);

                if(mCursor.moveToFirst()) {
                    if(mCursor.getInt(mCursor.getColumnIndex(TrophyContract.Columns.ACHIEVED)) == 0) {
                        trophyWins.winTrophy(Trophies.TrophyDetails.longWalk[0], null);
                    }
                }
            }
        }
    };

    private static final IStepServiceCallback.Stub mCallback = new IStepServiceCallback.Stub() {

        @Override
        public IBinder asBinder() {
            return mCallback;
        }

        @Override
        public void stepsChanged(int value) throws RemoteException {
            logger.info("Steps=" + (currentSteps+value));
            Message msg = handler.obtainMessage();
            msg.arg1 = (currentSteps+value);
            handler.sendMessage(msg);
        }
    };

    private static final ServiceConnection mConnection = new ServiceConnection() {

        /**
         * {@inheritDoc}
         */
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            logger.info("onServiceConnected()");
            mService = IStepService.Stub.asInterface(service);
            try {
                mService.registerCallback(mCallback);
                mService.setSensitivity(sensitivity);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onServiceDisconnected(ComponentName className) {
            logger.info("onServiceDisconnected()");
            mService = null;
        }
    };
}

package com.example.myfirstapp;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class senseService extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    private Geocoder geocoder;
    private List<Address> addresses;

    private TextView tv;
    private TextView hometv;
    private TextView worktv;
    private TextView gymtv;
    private TextView numstepstv;

    private int accIndex = 0;
    private float[] accHistory;
    private float minAccDerivative;
    private float maxAccDerivative;
    private int accHistoryIndex = 0;
    private float accComposite;
    private float accOld;
    private int numSteps = 0;
    private String addr = "";
    private String packet = "";
    private int packetIndex = 0;

    private int homet = 7673;
    private int workt = 9723;
    private int gymt = 1800;
    private int initialnumSteps = 300;
    public HashMap<String, String> loc = new HashMap<String, String>();

    private HashMap<String, Long> startTimes = new HashMap<>();

    private HashMap<String, Long> totalTime = new HashMap<>();


    Context context = this;

    private String prevLocation = "";

    Button btnAcc;
    Button btnStop;
    private long prevTime = 0;
    private int prev = 0;


    GPSDataBase gdb;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    databaseHelper d;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)


    @Override
    public void onCreate() {

        d = new databaseHelper(this);
        gdb = new GPSDataBase(this);
        Log.i("SleepMonitor", "Starting");

        accHistory = new float[10];
        geocoder = new Geocoder(this, Locale.getDefault());


        numstepstv = findViewById(R.id.numsteps_tv);
        gymtv = findViewById(R.id.gym_tv);
        worktv = findViewById(R.id.work_tv);

        numstepstv.setText(("300"));

        hometv.setText("7673");
        worktv.setText("9723");
        gymtv.setText("1800");

        Switch sleepSwitch = (Switch) findViewById(R.id.switchSleep);
        Switch driveSwitch = (Switch) findViewById(R.id.switchDrive);

        Boolean sleepState = sleepSwitch.isChecked();
        Boolean driveState = sleepSwitch.isChecked();
        sleepSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tv.append("Sleep Mode is on\n");
                } else {
                    tv.append("Sleep Mode is off\n");
                }
            }
        });

        driveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tv.append("Drive Mode is on\n");
                } else {
                    tv.append("Drive Mode is off\n");
                }
            }
        });


        //sleepSwitch.setOnCheckedChangeListener();


        tv = findViewById(R.id.textView);
        tv.setText("Starting Program...\n");
        loc.put("Schomburg Commons", "HOME");
        //loc.put("Light Engineering","WORK");
        //loc.put("Civil Engineering","WORK");

        loc.put("Walter J. Hawrys Campus Recreation Center", "GYM");
        //loc.put("700","HOME");
        loc.put("Humanities", "WORK");
        loc.put("The Bench", "RA1");
        loc.put("7-Eleven", "RA2");
        loc.put("The Vape Shop", "RA3");
        //loc.put("Chapin Apartment J","HOME");

        startTimes.put("HOME", Long.valueOf(0));
        startTimes.put("GYM", Long.valueOf(0));
        startTimes.put("WORK", Long.valueOf(0));
        startTimes.put("RA1", Long.valueOf(0));
        startTimes.put("RA2", Long.valueOf(0));
        startTimes.put("RA3", Long.valueOf(0));


        totalTime.put("HOME", Long.valueOf(0));
        totalTime.put("GYM", Long.valueOf(0));
        totalTime.put("WORK", Long.valueOf(0));
        startTimes.put("RA1", Long.valueOf(0));
        startTimes.put("RA2", Long.valueOf(0));
        startTimes.put("RA3", Long.valueOf(0));


        //create empty database
        //boolean ans = d.insertData(System.currentTimeMillis(),accComposite);

        boolean a1 = gdb.insertData("HOME", 0);

        if (a1 == false) {
            Log.i("GPS DATABASE", "Error in database");
        } else {
            Log.i("GPS database", "GPS Database written successfully");
        }
        boolean a2 = gdb.insertData("WORK", 0);
        if (a2 == false) {
            Log.i("GPS DATABASE", "Error in database");
        } else {
            Log.i("GPS database", "GPS Database written successfully");
        }
        boolean a3 = gdb.insertData("GYM", 0);
        if (a3 == false) {
            Log.i("GPS DATABASE", "Error in database");
        } else {
            Log.i("GPS database", "GPS Database written successfully");
        }

        boolean a4 = gdb.insertData("RA1", 0);

        if (a4 == false) {
            Log.i("GPS DATABASE", "Error in database");
        } else {
            Log.i("GPS database", "GPS Database written successfully");
        }
        boolean a5 = gdb.insertData("RA2", 0);

        if (a5 == false) {
            Log.i("GPS DATABASE", "Error in database");
        } else {
            Log.i("GPS database", "GPS Database written successfully");
        }
        boolean a6 = gdb.insertData("RA3", 0);

        if (a6 == false) {
            Log.i("GPS DATABASE", "Error in database");
        } else {
            Log.i("GPS database", "GPS Database written successfully");
        }


        //setup buttons
        btnAcc = (Button) ((MainActivity)context)findViewById(R.id.acc_button);
        btnStop = (Button) findViewById(R.id.stop_button);
        Log.i("SleepMonitor", "Starting");

        accHistory = new float[10];
        geocoder = new Geocoder(this, Locale.getDefault());

        tv = (TextView) findViewById(R.id.textView);
        tv.setText("Starting Program...\n");


        btnAcc.setOnClickListener(new View.OnClickListener() {g
            @Override
            public void onClick(View view) {

                sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 10000);


                //Setup GPS
                LocationManager locationManagerGPS = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
                if (locationManagerGPS.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    locationListenerGPS.onProviderEnabled("GPS");
                } else {
                    locationListenerGPS.onProviderDisabled("GPS");
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        tv.append("Error with permissions\n");
                        tv.append("Fine: " + checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) + "\n");
                        tv.append("Coarse: " + checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) + "\n");
                        return;
                    }
                }
                locationManagerGPS.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGPS);

                LocationManager locationManagerGSM = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
                if (locationManagerGSM.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationListenerGSM.onProviderEnabled("Network");
                } else {
                    locationListenerGSM.onProviderDisabled("Network");
                }
                locationManagerGSM.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerGSM);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorManager.unregisterListener(MainActivity.this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
            }
        });


        /*button change*/



        button_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(this, ReportActivity.class);
                startActivity(intent);

            }
        });
    }







        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
            // Find hypotenuse combined acceleration from X, Y, and Z components
            accComposite = event.values[0] * event.values[0] + event.values[1] * event.values[1] + event.values[2] * event.values[2];
            accComposite = (float) Math.sqrt((double) accComposite);

            // Sleep Tracking:
            //      Sensor gets sampled at 100Hz, only build packets at 10Hz
            //      Send raw composite data over wifi to logger on laptop
            packetIndex++;
            if (packetIndex % 10 == 0) {    // Frequency divider (/10)
                // Packet consists of timestamp and value of accelerometer
                packet += System.currentTimeMillis() + ", " + accComposite + ";";
                boolean ans = d.insertData(System.currentTimeMillis(),accComposite);
                if(ans == false)
                {
                    Log.i("database", "Error in database");
                }
                else
                {
                    Log.i("database","Database written successfully");
                }
                if (packetIndex > 100) {     // Burst send every second
                    //new SendPacket().execute(packet);
                    Log.i("SleepMonitor",packet);
                    packetIndex = 0;
                    packet = "";
                }
            }


            // Step Counter:
            //      Take the derivative of the composite acceleration
            //      Look at 10Hz sample region (10 samples)
            //      Product of max negative and max positive should be greater than 20 (thresholding) to register a step

            accHistory[accHistoryIndex] = accComposite - accOld;    // store the derivative
            accHistoryIndex++;
            if (accHistoryIndex == 10)                  // roll back to beginning of array
            {
                accHistoryIndex = 0;
            }
            minAccDerivative = Integer.MAX_VALUE;       // reset to opposite extremes
            maxAccDerivative = Integer.MIN_VALUE;
            for (int i = 0; i < 10; i++)                // check recent history for min and max derivatives
            {
                if (accHistory[i] > maxAccDerivative)
                    maxAccDerivative = accHistory[i];
                if (accHistory[i] < minAccDerivative)
                    minAccDerivative = accHistory[i];
            }

            if ((maxAccDerivative * minAccDerivative < -20)) {
                // step found
                //new SendPacket().execute("Step");
                //Log.i("Wireless", "Step");
                prev = numSteps;
                numSteps++;
                // reset array
                for (int i = 0; i < 10; i++) {
                    accHistory[i] = 0;
                }
            }
            accOld = accComposite;
            accIndex = accIndex + 1;
            if(numSteps>prev) {

                numstepstv.setText( String.valueOf(initialnumSteps + numSteps));
            }
        }
    }


        // GPS Portion
        LocationListener locationListenerGPS = new LocationListener() {
            public void onLocationChanged(Location location) {
                String lat = String.valueOf(location.getLatitude());
                String lng = String.valueOf(location.getLongitude());
                String acc = String.valueOf(location.getAccuracy());
                //tv.append("GPS: Lat: " + lat + ", Long: " + lng + ", Accuracy: " + acc + "\n");
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                String str = "Status ("+provider+"): ";
                if(status == LocationProvider.TEMPORARILY_UNAVAILABLE){
                    str += "temporarily unavailable : ";
                }else if(status == LocationProvider.OUT_OF_SERVICE) {
                    str += "out of service : ";
                }else if(status == LocationProvider.AVAILABLE) {
                    str += "available : ";
                }else{
                    str += "unknown : ";
                }
                tv.append("GPS" + str + "\n");

            }

            public void onProviderEnabled(String provider)
            {
                tv.append("GPS" + provider+" enabled\n");
            }

            public void onProviderDisabled(String provider) {
                tv.append("GPS" + provider+" disabled\n");
            }
        };


        LocationListener locationListenerGSM = new LocationListener()
        {
            public void onLocationChanged(Location location)
            {
                String lat = String.valueOf(location.getLatitude());
                String lng = String.valueOf(location.getLongitude());
                String acc = String.valueOf(location.getAccuracy());

                tv.append("GSM: Lat: " + lat + ", Long: " + lng + ", Accuracy: " + acc + "\n");

                try
                {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                }
                catch (IOException e)
                {
                    tv.append("Error getting addresses");
                    e.printStackTrace();
                }

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                addr = knownName;
                tv.append("Name of Location is: " + knownName + "\n");

                long time = 0;

                if (prevLocation != loc.get(knownName) && loc.containsKey(knownName)) {
                    prevLocation = loc.get(knownName);
                    tv.append("Name of Location is: " + knownName + "\n");
                    //totalTime.put(knownName, totalTime.get(knownName)+ (System.currentTimeMillis() - startTimes.get(knownName)));
                    startTimes.put(loc.get(knownName), System.currentTimeMillis() / 1000);
                    //time = System.currentTimeMillis();
                    tv.append("the start time for subsequent  = " + " " + startTimes.get(loc.get(knownName)));
                } else if (prevLocation != loc.get(knownName)) {
                    prevLocation = knownName;
                } else {
                    if (loc.containsKey(knownName)) {
                        totalTime.put(loc.get(knownName), totalTime.get(loc.get(knownName)) + ((System.currentTimeMillis()/1000 - startTimes.get(loc.get(knownName)))));
                        tv.append("The start time for subsequent  = " + " " + startTimes.get(loc.get(knownName)) + "\n");
                        tv.append("Time spent at " + " " + loc.get(knownName) + " " + totalTime.get(loc.get(knownName)) + " Seconds " + "\n");

                        if (loc.get(knownName) == "HOME") {
                            hometv.setText(String.valueOf(homet + totalTime.get(loc.get(knownName))));
                            gdb.updateData("HOME", totalTime.get(loc.get(knownName)));
                        } else if (loc.get(knownName) == "WORK") {
                            worktv.setText(String.valueOf(workt + (totalTime.get(loc.get(knownName)))));
                            gdb.updateData("WORK", totalTime.get(loc.get(knownName)));
                        } else {
                            gymtv.setText(String.valueOf(gymt + totalTime.get(loc.get(knownName))));
                            gdb.updateData("GYM", totalTime.get(loc.get(knownName)));
                        }
                    }
                }

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                String str = "Status ("+provider+"): ";
                if(status == LocationProvider.TEMPORARILY_UNAVAILABLE){
                    str += "temporarily unavailable";
                }else if(status == LocationProvider.OUT_OF_SERVICE) {
                    str += "out of service";
                }else if(status == LocationProvider.AVAILABLE) {
                    str += "available";
                }else{
                    str += "unknown";
                }
                tv.append("GSM" + str + "\n");
            }

            public void onProviderEnabled(String provider) {
                tv.append("GSM " + provider+" enabled\n");
            }

            public void onProviderDisabled(String provider) {
                tv.append("GSM"+provider+" disabled\n");
            }
        };





@Override
public IBinder onBind(Intent intent) {
        return null;
        }



@Override
public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
}

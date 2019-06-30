package jellodev.obd2analyzerv10;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.lang.String;

import jellodev.obd2analyzerv10.obd.commands.pressure.IntakeManifoldPressureCommand;
import jellodev.obd2analyzerv10.obd.commands.protocol.*;
import jellodev.obd2analyzerv10.obd.exceptions.*;
import jellodev.obd2analyzerv10.obd.enums.*;
import jellodev.obd2analyzerv10.obd.commands.*;
import jellodev.obd2analyzerv10.obd.commands.temperature.*;
import jellodev.obd2analyzerv10.obd.commands.engine.*;


public class LiveFeed extends Activity {

    private static String deviceAddress;
    private static BluetoothAdapter btAdapter;
    private final BluetoothService mBluetoothService1 = new BluetoothService();
    private final BluetoothService mBluetoothService2 = new BluetoothService();
    private static final String TAG = "LiveFeed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_feed);
        //Θεελουμε η οθονη να μην σβηνει στο live feed
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final ArrayList<String> deviceStrs = new ArrayList<>();

        //Βρισκουμε συσκευες που ειναι ηδη paired
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                Log.d(TAG, "BT: " + device.getName() + " - " + device.getAddress());
                deviceStrs.add(device.getName() + "\n" + device.getAddress());

            }
        }


        CharSequence[] cs = deviceStrs.toArray(new CharSequence[deviceStrs.size()]);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                cs);

        //Μας εμφανιζεται παραθυρο το οποιο περιεχει τις paired συσκευες για να διαλεξουμε
        alertDialog.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                btAdapter.cancelDiscovery();
                int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                deviceAddress = deviceStrs.get(position);
                String address = deviceAddress.substring(deviceAddress.length() - 17);
                BluetoothDevice device = btAdapter.getRemoteDevice(address);
                mBluetoothService1.connect(device);
                Log.d(TAG, "Picked: " + device);
                dialog.dismiss();

            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();

        //Ξεκιναμε μια υπηρεσια στο backround για καταγραφη συντεταγμενων
        //Intent intent = new Intent(this, *****.class);
        //startService(intent);
    }




    public class BluetoothService {
        // Το UUID ειναι ενας μοναδικος κωδικος που χρησιμοποιειται για το ανοιγμα bluetooth sockets
        private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        private ConnectThread mConnectThread;
        private ConnectedThread mConnectedThread;
        private BluetoothAdapter mBluetoothAdapter;


        /*Πατωντας στο προηγουμενο παραθυρο ερχομαστε εδω για να ξεκινησουμε τη συνδεση
         με ορισμα την διευθυνση της συσκευης */
        synchronized void connect(BluetoothDevice device) {
            // Start ConnectThread to connect to given device
            mConnectThread = new ConnectThread(device);
            mConnectThread.start();
        }

        /*Εαν πραγματοποιηθει συνδεση μεταξυ συσκευων καλειτε η παρακατω συναρτηση
        * η οποια ξεκιναει νεο thread και διαχειριζεται τα input/output streams της συνδεσης*/
        synchronized void connected(BluetoothSocket mBluetoothSocket) {
            mConnectedThread = new ConnectedThread(mBluetoothSocket);
            mConnectedThread.start();
        }

        //Εδω ξεκιναει το thread πραγματοποιησης συνδεσης
        private class ConnectThread extends Thread {
            private final BluetoothSocket mBluetoothSocket;
            private final BluetoothDevice mBluetoothDevice;

            private ConnectThread(BluetoothDevice device) {
                mBluetoothDevice = device;
                BluetoothSocket tmp = null;
                // Get a BluetoothSocket to connect with the given BluetoothDevice
                try {
                    // MY_UUID is the app's UUID string
                    tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                } catch (IOException e) {
                    Log.e(TAG, "Something went wrong with the connection");
                }
                mBluetoothSocket = tmp;
            }

            public void run() {
                // Because BluetoothAdapter.discovery() is
                // heavy weight procedure, cancel any on
                // going discovery before attempting to connect
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                mBluetoothAdapter.cancelDiscovery();
                // Connect the device through the socket. This is blocking call so it
                // will return on a successful connection or an exception

                // Make a connection to the BluetoothSocket
                try {
                    // Connect the device through the socket. This is blocking call so it
                    // will return on a successful connection or an exception
                    mBluetoothSocket.connect();
                } catch (IOException e) {
                    try {
                        // Unable to connect, close the socket and get out
                        mBluetoothSocket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                // Reset the ConnectThread because we're done
                synchronized (BluetoothService.this) {
                    mConnectThread = null;
                }
                // Do work to manage the connection (in a separate thread)
                if (mBluetoothSocket.isConnected()) {
                    mBluetoothService2.connected(mBluetoothSocket);
                }
            }

            void cancel() {
                // This method will cancel an in-progress connection and close the socket
                try {
                    mBluetoothSocket.close();
                } catch (IOException e) {
                    Log.e(TAG, "Unable to close socket");
                    e.printStackTrace();
                }
            }
        }
    }

    //Εδω ξεκιναει το thread διαχειρησης συνδεσης
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        Boolean i = false;


        ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;


            runOnUiThread(new Runnable() {
                @Override
                public void run() {

            /*Εδω ρωταμε τον χρηστη αν θελει να ενεργοποιησει την υπηρεσια καταγραφης
            των δεδομενων σε αρχειο*/
                    try {
                AlertDialog.Builder alert = new AlertDialog.Builder(LiveFeed.this);

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                i = true;
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                i = false;
                                break;
                        }
                    }
                };

                alert.setMessage("Would you like to start Logging?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            } catch (Exception e) {
                Log.d(TAG, "getApplicationContext error");
            }
                }
            });


            try {
                //απαραιτητες εντολες για αρχικοποιηση της επικοινωνιας με το αμαξι και το reset της OBD θυρας
                new ObdResetCommand().run(mmSocket.getInputStream(), mmSocket.getOutputStream());
                new EchoOffCommand().run(mmSocket.getInputStream(), mmSocket.getOutputStream());
                new LineFeedOffCommand().run(mmSocket.getInputStream(), mmSocket.getOutputStream());
                new SpacesOffCommand().run(mmSocket.getInputStream(), mmSocket.getOutputStream());
                new HeadersOffCommand().run(mmSocket.getInputStream(), mmSocket.getOutputStream());

                try {
                    new TimeoutCommand(10).run(mmSocket.getInputStream(), mmSocket.getOutputStream());
                } catch (MisunderstoodCommandException e) {
                    Log.d(TAG, "Timeout command not understood, hope that wasn't important..");
                }

                try {
                    //επιλεγεται το πρωτοκολλο επικοινωνιας αναλογα το αυτοκινητο. Συνηθως με AUTO βρισκει μονο του το καταλληλο
                    new SelectProtocolCommand(ObdProtocols.AUTO).run(mmSocket.getInputStream(), mmSocket.getOutputStream());
                } catch (MisunderstoodCommandException e) {
                    Log.d(TAG, "Select protocol command failed");
                }

                //εδω δημιουργειται το αρχειο των δεδομενων του εγκεφαλου
                File sdCard = new File(Environment.getExternalStorageDirectory()+"/data");
                sdCard.mkdirs();
                File file = new File(sdCard, "cardata.txt");
                FileOutputStream osw = new FileOutputStream(file, true);




                //σε αυτο το while block γινεται ολη η επικοινωνια για να παρουμε δεδομενα απο τον εγκεφαλο, οσο ο χρηστης βρισκεται σε σελιδα Livefeed
                while (!Thread.currentThread().isInterrupted()) {

                    //καλουμε τους δημιουργους των αντιστοιχων κλασεων της obd βιβλιοθηκης
                    SpeedCommand speedCommand = new SpeedCommand();
                    RPMCommand engineRpmCommand = new RPMCommand();
                    LoadCommand loadCommand = new LoadCommand();
                    AirIntakeTemperatureCommand airIntakeTemperatureCommand = new AirIntakeTemperatureCommand();
                    IntakeManifoldPressureCommand mapCommand = new IntakeManifoldPressureCommand();
                    EngineCoolantTemperatureCommand carTemperatureCommand = new EngineCoolantTemperatureCommand();

                    //εδω στελνουμε συνεχως request στο αμαξι και λαμβανουμε τις απαντησεις τις οποιες κραταμε
                    engineRpmCommand.run(mmSocket.getInputStream(), mmSocket.getOutputStream());
                    speedCommand.run(mmSocket.getInputStream(), mmSocket.getOutputStream());
                    loadCommand.run(mmSocket.getInputStream(), mmSocket.getOutputStream());
                    airIntakeTemperatureCommand.run(mmSocket.getInputStream(), mmSocket.getOutputStream());
                    mapCommand.run(mmSocket.getInputStream(), mmSocket.getOutputStream());
                    carTemperatureCommand.run(mmSocket.getInputStream(), mmSocket.getOutputStream());

                    //Debugging
                    //Log.d(TAG, "RPM: " + engineRpmCommand.getFormattedResult());
                    //Log.d(TAG, "Speed: " + speedCommand.getFormattedResult());
                    //Log.d(TAG, "Load: " + loadCommand.getFormattedResult());
                    //Log.d(TAG, "Air Temp: " + airIntakeTemperatureCommand.getFormattedResult());
                    //Log.d(TAG, "MAP : " + mapCommand.getFormattedResult());
                    //Log.d(TAG, "Car Temp: " + carTemperatureCommand.getFormattedResult());

                    long time = SystemClock.elapsedRealtime();
                    String timer = Objects.toString(time);
                    final String a = engineRpmCommand.getFormattedResult();
                    final String b = speedCommand.getFormattedResult();
                    final String c = loadCommand.getFormattedResult();
                    final String d = airIntakeTemperatureCommand.getFormattedResult();
                    final String e = mapCommand.getFormattedResult();
                    final String f = carTemperatureCommand.getFormattedResult();
                    final String g = engineRpmCommand.getFormattedResult() + "|" + speedCommand.getFormattedResult() + "|" + loadCommand.getFormattedResult() + "|" + airIntakeTemperatureCommand.getFormattedResult() + "|" + mapCommand.getFormattedResult() + "|" + carTemperatureCommand.getFormattedResult() + "|" + timer + ";";



                    //εδω γινεται ο ελεγχος για το αν ο χρηστης εχει επιλεξει να καταγραψει τα δεδομενα
                    if (i) {
                        //Log.d(TAG, "Trying to write file");
                        try {
                            //εγγραφη στο αρχειο
                            osw.write(g.getBytes());
                            osw.write("\n".getBytes());

                        } catch (IOException e5) {
                            //Log.e(TAG, "Error while writing");
                            e5.printStackTrace();
                        }
                    }


                    /*το παρακατω μπλοκ ειναι υπευθυνο για την παρουσιαση των δεδομενων στην οθονη του κινητου
                    επειδη βρισκομαστε σε backround thread καλειται να εκτελεστει στον βασικο thread της εφαρμογης*/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setContentView(R.layout.activity_live_feed);
                            TextView rpm = (TextView) findViewById(R.id.textView2);
                            rpm.setText(a);
                            TextView speed = (TextView) findViewById(R.id.textView4);
                            speed.setText(b);
                            TextView load = (TextView) findViewById(R.id.textView3);
                            load.setText(c);
                            TextView airIntakeTemp = (TextView) findViewById(R.id.textView6);
                            airIntakeTemp.setText(d);
                            TextView map = (TextView) findViewById(R.id.textView7);
                            map.setText(e);
                            TextView carTemp = (TextView) findViewById(R.id.textView8);
                            carTemp.setText(f);
                        }
                    });

                }
                osw.flush();
                osw.close();
            } catch (MisunderstoodCommandException e) {
                Log.e(TAG, "MisunderstoodCommandException: " + e.toString());

            } catch (IOException | InterruptedException e) {
                Log.e(TAG, "test error");
                e.printStackTrace();
            }
        }
    }



    @Override//κατα την επιστροφη μας στην βασικη σελιδα το παρον καταστρεφεται και σταματαει η καταγραφη
    public void onDestroy() {
        super.onDestroy();
        android.os.Debug.stopMethodTracing();
    }
}


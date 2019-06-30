package jellodev.obd2analyzerv10;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Εδω ζηταμε απο τον χρηστη να μας δωσει την αδεια για χρηση της μνημης και υπηρεσιων διαδικτυου!!!Ειναι απαραιτητες για εκδοσεις Android απο 6 και πανω
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1;
        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        //ΕΛεγχουμε αν ο χρηστης εχει ενεργοποιημενο το bluetooth και αν οχι ζηταμε αδεια ενεργοποιησης
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }


    }

    //οι 2 δραστηριοτητες που εμφανιζονται στην αρχικη οθονη της εφαρμογης


    public void livefeed(View view) {
        Intent intent = new Intent(this, LiveFeed.class);
        startActivity(intent);
    }



    public void upload(View view) {
        Intent intent = new Intent(this, HttpUpload.class);
        startActivity(intent);
    }
}

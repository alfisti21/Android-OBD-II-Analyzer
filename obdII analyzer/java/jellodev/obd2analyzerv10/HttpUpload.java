package jellodev.obd2analyzerv10;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static android.content.ContentValues.TAG;

//Αυτη η δραστηριοτητα ειναι υπευθηνη για την αποστολη του αρχειου απο το κινητο στο server

public class HttpUpload extends Activity implements AdapterView.OnItemClickListener {

    public static final String FILE_UPLOAD_URL = "http://192.168.1.6:80/AndroidFileUpload/fileUpload.php";//το url του server
    int serverResponseCode = 0;
    int INTERNET = 1;
    File sdCard1 = new File(Environment.getExternalStorageDirectory()+"/data");
    File sdCard2 = new File(Environment.getExternalStorageDirectory()+"/gps");
    File f1 = new File(sdCard1, "cardata.txt");
    File f2 = new File(sdCard2, "gpsdata.txt");
    String f11 = f1.toString();
    String f12 = f2.toString();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, INTERNET);


        ListView listview = (ListView) findViewById(R.id.filelist);
        listview.setOnItemClickListener(this);
        String[] files = {"Upload Car Data", ""};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, files);
        listview.setAdapter(adapter);



    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        int pos = l.getPositionForView(v);

        switch (pos) {

            case 0:
                uploadFile(f11, f1);
                break;
            case 1:
                uploadFile(f12, f2);
                break;
        }
    }

    public int uploadFile(String sourceFileUri, File ftodelete) {
        Log.d(TAG, "inside uploadFile function");


        String fileName = sourceFileUri;
        File filetodelete = ftodelete;
        Log.d(TAG, fileName);

        HttpURLConnection conn;
        DataOutputStream dos;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024 * 1024;
        File sourceFile = new File(fileName);

            try {

                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(FILE_UPLOAD_URL);
                String s = url.toString();
                Log.d(TAG, s);

                //ξεκιναει συνδεση με τον server
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("enctype", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);


                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                // δημιουργια buffer
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];


                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Απαντηση απο τον server
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    //Log.d(TAG, "Upload Ok");
                    //εαν το αρχειο εληφθη σωστα το διαγραφουμε απο το τηλεφωνο
                    filetodelete.delete();
                }

                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Upload file to server E", "Exception : " + e.getMessage(), e);
            }
            return serverResponseCode;

        } // End else block
}
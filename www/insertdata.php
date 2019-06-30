<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "obdIIanalysis";
//επιλογη αρχειου αν υπαρχει
$data1 = "c:/wamp64/www/AndroidFileUpload/uploads/cardata.txt";
if (!file_exists($data1)){
echo "There is no file to insert.<br>";
} else {
$fcar = fopen($data1, 'r') or die("Could not open file: " . mysql_error());
}

//συνδεση με τη βαση

$conn = new mysqli($servername, $username, $password);
//εισαγωγη δεδομενων στη βαση κατευθειαν απο το .txt
$sql = "LOAD DATA LOCAL INFILE '$data1' INTO TABLE obdIIanalysis.cardata FIELDS TERMINATED BY '|' LINES TERMINATED BY ';\n' (rpm, speed, engload, airtemp, map, cartemp, stopwatch)";
if (!$conn->query($sql)) {
    die("Could not load ecu data. " . mysql_error());
} else {
	echo "Ecu data inserted succesfully.\n";
}


fclose($fcar);
$conn->close();
?>
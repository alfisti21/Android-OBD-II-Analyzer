<?php
$servername = "localhost";
$username = "root";
$password = "";


// συνδεση με τη βαση
$conn = new mysqli($servername, $username, $password);

// ελεγος συνδεσης
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 
// δημιουργια βασης obdIIanalysis
$sql = "CREATE DATABASE IF NOT EXISTS obdIIanalysis";
if ($conn->query($sql) === TRUE) {
    echo "Database ready for data insertion.\n";
} else {
    echo "Error creating database: " . $conn->error;
}
//δημιουργια πινακα δεδομενων αυτοκινητου καθε φορα απο την αρχη
$table1 = "DROP TABLE IF EXISTS obdIIanalysis.cardata";
if ($conn->query($table1) === TRUE) {
	echo "<br>Old cardata table deleted successfully. \n";
}
$ecudata = "CREATE TABLE obdIIanalysis.cardata (
aa int(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
rpm FLOAT(10,3) UNSIGNED NOT NULL,
speed FLOAT(10,3) UNSIGNED NOT NULL,
engload FLOAT(10,3) UNSIGNED NOT NULL,
airtemp FLOAT(10,3) NOT NULL,
map FLOAT(10,3) UNSIGNED NOT NULL,
cartemp INT(3) NOT NULL,
stopwatch FLOAT(15,3) UNSIGNED NOT NULL,
mpg FLOAT(10,3) NOT NULL,
litres FLOAT(10,3) NOT NULL
)";




//δημιουργια πινακα για την καταναλωση. δεν διαγραφεται ποτε, συνεχως εμπλουτιζεται για να μας δειχνει την συνολικη καταναλωση
$fuel = "CREATE TABLE IF NOT EXISTS obdIIanalysis.fuel (
cc INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
instantfuel FLOAT(10,3) NOT NULL
)";

$accdistance = "CREATE TABLE IF NOT EXISTS obdIIanalysis.accdistance (
ff INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
accdistance FLOAT(10,3) NOT NULL
)";
$conn->query($accdistance);

$conn->query($fuel);

//δημιουργια πινακα μετρησης διαρκειας διαδρομης
$table3 = "DROP TABLE IF EXISTS obdIIanalysis.stopwatch";
if ($conn->query($table3) === TRUE) {
	echo "<br>Old time table deleted successfully. \n";
}

$time = "CREATE TABLE IF NOT EXISTS obdIIanalysis.stopwatch (
dd INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
st FLOAT(15,3) UNSIGNED DEFAULT 0,
et FLOAT(15,3) UNSIGNED DEFAULT 0,
total FLOAT(15,3) UNSIGNED DEFAULT 0,
avgspeed1 FLOAT(15,3) UNSIGNED DEFAULT 0
)";

if ($conn->query($time) ===TRUE) {
	echo "<br>Time table created successfully.";
}

$table4 = "DROP TABLE IF EXISTS obdIIanalysis.distance";
if ($conn->query($table4) === TRUE) {
	echo "<br>Old distance table deleted successfully. \n";
}

$distance = "CREATE TABLE IF NOT EXISTS obdIIanalysis.distance (
ee INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
timetraveled FLOAT(15,3) UNSIGNED DEFAULT 0,
avgspeed2 Float(15,3) UNSIGNED DEFAULT 0,
kmtraveled FLOAT(15,3) UNSIGNED DEFAULT 0
)";
if ($conn->query($distance) ===TRUE) {
	echo "<br>Distance table created successfully.";
}



if ($conn->query($ecudata) === TRUE) {
    echo "<br>Table cardata created successfully.\n";
} else {
    echo "Error creating table cardata: " . $conn->error;
}


if ($conn->query($fuel) === TRUE) {
    echo "<br>Table fuel created successfully.\n";
} else {
    echo "Error creating table fuel: " . $conn->error;
}

$conn->close();
?>
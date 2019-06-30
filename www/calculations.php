<?php
$servername = "localhost";
$username = "root";
$password = "";
$data1 = "c:/wamp64/www/AndroidFileUpload/uploads/cardata.txt";


// συνδεση με τη βαση
$conn = new mysqli($servername, $username, $password);

// ελεγχος συνδεσης
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

if(file_exists($data1)) {
//υπολογιζουμε την στιγμιαια καταναλωση σε μιλια ανα γαλονι. η μονη παρεμβαση που εχει να κανει ο εκαστοτε χρηστης ειναι η τιμη 1.368 βαζοντας την χωρητικοτητα του κινητηρα του σε λιτρα
$mpg = "UPDATE obdIIanalysis.cardata SET mpg=((710.700*speed)/((((((rpm*map)/(2.000*(airtemp+273.000))/60.000)*0.834)*1.368)*28.970)/8.314)/100.000) WHERE speed !=0";
$conn->query($mpg);
//υπολογιζουμε την στιγμιαια καταναλωση με λιτρα ανα 100χλμ
$fuel = "UPDATE obdIIanalysis.cardata SET litres=(100.000/((mpg*1.610)/4.546)) WHERE mpg !=0";
$conn->query($fuel);

//προσθετει συνεχως τις τιμες ολων των στιγμιαιων και ολων των καταγραφων
$fuel2 = "INSERT INTO obdIIanalysis.fuel(instantfuel) SELECT litres FROM obdIIanalysis.cardata";
$conn->query($fuel2);

//επιλεγει απο τον πινακα με τον χρονο εκκινησης και τερματισμου
$time = "INSERT INTO obdIIanalysis.stopwatch(st, et, avgspeed1) SELECT MIN(stopwatch), MAX(stopwatch), AVG(speed) FROM obdIIanalysis.cardata";
$conn->query($time);

//υπολογιζουμε τον χρονο της διαδρομης
$totaltime1 = "UPDATE obdIIanalysis.stopwatch SET total=((et-st)/1000.000)/60.000";
$conn->query($totaltime1);


$avgspeedtransfer = "INSERT INTO obdIIanalysis.distance(timetraveled, avgspeed2) SELECT total, avgspeed1 FROM obdIIanalysis.stopwatch";
$conn->query($avgspeedtransfer);
$kmtraveledcalc = "UPDATE obdIIanalysis.distance SET kmtraveled=avgspeed2*(timetraveled/60)";
$conn->query($kmtraveledcalc);
$accdistance = "INSERT INTO obdIIanalysis.accdistance(accdistance) SELECT kmtraveled FROM obdIIanalysis.distance";
$conn->query($accdistance);




//επιλεγουμε την καταναλωση διαδρομης και την γενικη καταναλωση 
$tripfuel = "SELECT ROUND(AVG(litres),1) FROM obdIIanalysis.cardata";
$accfuel = "SELECT ROUND(AVG(instantfuel),1) FROM obdIIanalysis.fuel";
$avgspeed = "SELECT ROUND(AVG(speed)) FROM obdIIanalysis.cardata";
$distance = "SELECT ROUND(kmtraveled,1) FROM obdIIanalysis.distance";
$distanceacc = "SELECT ROUND(SUM(accdistance),1) FROM obdIIanalysis.accdistance";

//παρακατω επιλεγουμε διαφορα στοιχεια απο τον πινακα cardata συνδυαζοντας τα με συγκεκριμενα κριτηρια
$totaltime = "SELECT ROUND(total) FROM obdIIanalysis.stopwatch";
$rods = "SELECT count(*) FROM obdIIanalysis.cardata WHERE rpm<2000 AND engload>70";
$rpm3000 = "SELECT count(*) FROM obdIIanalysis.cardata WHERE rpm>3000 AND speed!=0";
$rpm5500 = "SELECT count(*) FROM obdIIanalysis.cardata WHERE rpm>5500";
$rpm0speed = "SELECT count(*) FROM obdIIanalysis.cardata WHERE rpm>3000 AND speed=0";
$rpm0speedtemp = "SELECT count(*) FROM obdIIanalysis.cardata WHERE rpm>3000 AND speed=0 AND cartemp<70";
$rpmtemp = "SELECT count(*) FROM obdIIanalysis.cardata WHERE rpm>3000 AND cartemp<70 AND speed!=0";
$speed50 = "SELECT count(*) FROM obdIIanalysis.cardata WHERE speed>50";
$speed130 = "SELECT count(*) FROM obdIIanalysis.cardata WHERE speed>130";
$airtemp = "SELECT count(*) FROM obdIIanalysis.cardata WHERE airtemp>60 AND engload>70";
$cartemp100 = "SELECT count(*) FROM obdIIanalysis.cardata WHERE cartemp>100";

//κανουμε τις αντιστοιχες ερωτησεις στη βαση
$rodsobj = $conn->query($rods);
$rpm3000obj = $conn->query($rpm3000);
$rpm5500obj = $conn->query($rpm5500);
$rpm0speedobj = $conn->query($rpm0speed);
$rpm0speedtempobj = $conn->query($rpm0speedtemp);
$rpmtempobj = $conn->query($rpmtemp);
$speed50obj = $conn->query($speed50);
$speed130obj = $conn->query($speed130);
$airtempobj = $conn->query($airtemp);
$cartemp100obj = $conn->query($cartemp100);
$tripfuelobj = $conn->query($tripfuel);
$accfuelobj = $conn->query($accfuel);
$totaltimeobj = $conn->query($totaltime);
$avgspeedobj = $conn->query($avgspeed);
$distanceobj = $conn->query($distance);
$distanceaccobj = $conn->query($distanceacc);


//παρακατω μετατρεπουμε τις απαντησεις της βασης σε κειμενο
$rodsarray = mysqli_fetch_assoc($rodsobj);
$rpm3000array = mysqli_fetch_assoc($rpm3000obj);
$rpm5500array = mysqli_fetch_assoc($rpm5500obj);
$rpm0speedarray = mysqli_fetch_assoc($rpm0speedobj);
$rpm0speedtemparray = mysqli_fetch_assoc($rpm0speedtempobj);
$rpmtemparray = mysqli_fetch_assoc($rpmtempobj);
$speed50array = mysqli_fetch_assoc($speed50obj);
$speed130array = mysqli_fetch_assoc($speed130obj);
$airtemparray = mysqli_fetch_assoc($airtempobj);
$cartemp100array = mysqli_fetch_assoc($cartemp100obj);
$tripfuelarray = mysqli_fetch_assoc($tripfuelobj);
$accfuelarray = mysqli_fetch_assoc($accfuelobj);
$totaltimearray = mysqli_fetch_assoc($totaltimeobj);
$avgspeedarray = mysqli_fetch_assoc($avgspeedobj);
$distancearray = mysqli_fetch_assoc($distanceobj);
$distanceaccarray = mysqli_fetch_assoc($distanceaccobj);

$rodsf = implode($rodsarray);
$rpm3000f = implode($rpm3000array);
$rpm5500f = implode($rpm5500array);
$rpm0speedf = implode($rpm0speedarray);
$rpm0speedtempf = implode($rpm0speedtemparray);
$rpmtempf = implode($rpmtemparray);
$speed50f = implode($speed50array);
$speed130f = implode($speed130array);
$airtempf = implode($airtemparray);
$cartemp100f = implode($cartemp100array);
$tripfuelf = implode($tripfuelarray);
$accfuelf = implode($accfuelarray);
$totaltimef = implode($totaltimearray);
$avgspeedf = implode($avgspeedarray);
$distancef = implode($distancearray);
$distanceaccf = implode($distanceaccarray);

//παρακατω, αναλογα με τα αποτελεσματα των ερωτηματων τα συγκρινουμε με συγκεκριμενα κριτηρια για να εξαγουμε συμπερασματα

echo "-Η μέση κατανάλωση της διαδρομής που μόλις κάνατε είναι: $tripfuelf lt/100Km.<hr>";
echo "-Η μέση κατανάλωση του αυτοκινήτου σας απο την στιγμή που ξεκινήσατε να χρησιμοποιείτε την εφαρμογή ObdII Analyzer είναι: $accfuelf lt/100Km.<hr>";
echo "-Η μέση ταχύτητα της διαδρομής που μόλις κάνατε είναι: $avgspeedf Km/h.<hr>";
echo "-Οδηγήσατε συνολικά: $totaltimef λεπτά.<hr>";
echo "-Οδηγήσατε συνολικά: $distancef χιλιόμετρα.<hr>";
echo "-Έχετε οδηγήσει συνολικά: $distanceaccf χιλιόμετρα απο την στιγμή που ξεκινήσατε να χρησιμοποιείτε την εφαρμογή ObdII Analyzer.<hr>";

if ($distanceaccf>"10000") {
	echo "-Έχετε διανύσει πανω απο 10000 χιλιομετρα. Πρεπει να αλλάξετε λάδια και φίλτρο λαδιού. Αγνοηστε το παρόν μήνυμα αν έχει γίνει ήδη η αλλαγή. ";
	$table = "DROP TABLE IF EXISTS obdIIanalysis.accdistance";
	if ($conn->query($table4) === TRUE) {
	echo "<br>:¨εχετε όμως υπόψιν οτι ο μετρητής θα αρχίσει να μετράει απο την αρχή.<hr>";
	}
}
if ($totaltimef>"120") {
	echo "-Οδηγήσατε συνεχόμενα $totaltime λεπτά, χρόνος ο οποίος υπερβαίνει τις 2 ώρες που προβλέπει ο ΚΟΚ. Παρακαλείσθε να συμμορφωθείτε για την δική σας ασφάλεια αλλά και των γύρω σας!<hr>";
}
if ($rodsf>"0") {
	echo "-Δώσατε φορτίο κινητήρα μεγαλυτερο του 70% σε χαμηλες RPM $rodsf φορες. Προσοχή καθώς μπορεί να προκληθεί ζημιά στις μπιέλες ή τη μετάδοση!<hr>";
}
if ($rpm3000f>"0") {
	echo "-Ξεπερνάτε τις 3.000 RPM. Αποφύγετέ το προς εξοικονόμηση βενζίνης και λιγότερης μόλυνσης του περιβάλλοντος!<hr>";
}
if ($tripfuelf>"10") {
	echo "-Η μέση κατανάλωση σας ξεπερνά τα 10 λίτρα ανά 100 χιλιόμετρα. Οδηγήστε πιό ήρεμα, αποφύγετε τις ώρες αιχμής ή επιλέξτε διαδρομές με λιγότερη υψομετρική διαφορά!<hr>";
}
if ($rpmtempf>"0") {
	echo "-Ξεπερνάτε τις 3.000 RPM πρωτου η θερμοκρασία του κινητήρα φτάσει σε αποδεκτά επίπεδα. Αυτο μπορει να προκαλέσει φθορά στα μέταλα του κινητήρα λόγω απότομης μεταβολής της θερμοκρασίας!<hr>";
}
if ($rpm5500f>"0") {
	echo "-Οδηγείτε κοντά στα όρια του κόφτη του κινητήρα. Αποφύγετέ το αλλίως θα μειωθεί το προσδόκιμο ζωής του κινητήρα σας!<hr>";
}
if ($rpm0speedf>"0") {
	echo "-Μην ανεβάζεται στροφές στον κινητήρα ενώ βρίσκεστε σε στάση. Λόγω μειωμένης πίεσης λαδιού μπορεί να προκληθεί φθορά!<hr>";
}
if ($rpm0speedtempf>"0") {
	echo "-Μην ανεβάζεται στροφές στον κινητήρα ενώ βρίσκεστε σε στάση και ειδικά αν δεν έχει φτάσει σε αποδεκτή θερμοκρασία λειτουργείας. Εκτός της φθοράς λόγω μειωμένης πίεσης λαδιού, μπορεί να προκλυθεί φθορά στα μέταλα του κινητήρα απο την απότομη άνοδο της θερμοκρασίας αλλά προκαλείτε και ηχορύπανση!<hr>";
}
if ($speed50f>"0") {
	echo "-Εαν η μετακίνηση σας ήταν εντός πόλης τότε ξεπεράσατε το όριο των 50 χλμ/ώρα $speed50f φορές. Τηρείτε τα όρια ταχύτητας!<hr>";
}
if ($speed130f>"0") {
	echo "-Έχετε ξεπεράσει το όριο των 130 χλμ/ώρα (το οποίο αποτελεί και το ανώτερο όριο στις εθνικές οδούς) $speed50f φορές. Τηρείτε τα όρια ταχύτητας!<hr>";
}
if ($airtempf>"0") {
	echo "-Μην δίνεται φορτίο κινητήρα μεγαλύτερο του 70% όταν η θερμοκρασία εισαγωγής του αέρα είναι μεγαλύτερη των 60°C!<hr>";
}
if ($cartemp100f>"300") {
	echo "-Παρατηρείτε πως η θερμοκρασία του κινητήρα σας ήταν πάνω απο 100°C για περισσότερα απο 5 λεπτά. Παρακαλούμε ελέγξτε το κύκλωμα ψύξης και λίπανσης σύντομα!";
}
//unlink($data1);
} else{
	echo "There is no file to perform calculations on. Try uploading one first!";
}

$conn->close();
?>
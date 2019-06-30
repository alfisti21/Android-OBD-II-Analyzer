<!DOCTYPE html>
<html lang="en-US">
<head>
  <meta charset="UTF-8">
</head>

<body style="background-color:powderblue;">

<div>
<h1 style="font-family:verdana; font-size:250%; text-align:center;"><ins>OBDII Server Side Calculations<ins></h1>
<hr>
<form action="dbcreation.php" method="get" target="frame1">
  <input type="submit" value="Step 1: Create Database and/or required tables">
</form>
<form action="insertdata.php" method="get" target="frame1">
  <input type="submit" value="Step 2: Insert data to database">
</form>
<form action="calculations.php" method="get" target="frame1">
  <input type="submit" value="Step 3: Perform calculations">
</form>
<hr>
<iframe width="27%" height="600" style="border:3px solid black;" name="frame1" id="myframe1"></iframe>
</div>
</body>
</html>
<?php
?>

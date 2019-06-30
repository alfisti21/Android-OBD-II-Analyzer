<?php
//σε αυτο το αρχειο της php αναφερεται το κινητο οταν κανει προσπαθεια αποστολης αρχειου
  $file_path = "./uploads/";
  $file_path = $file_path . basename($_FILES["uploaded_file"]["name"]);
    
if (isset($_FILES["uploaded_file"]['name'])) {	
    if(move_uploaded_file($_FILES["uploaded_file"]["tmp_name"], $file_path)) {
        echo "success";
    } else{
        echo "fail";
    }
}
 ?>
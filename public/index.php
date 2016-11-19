<?php

echo "Hey what's up!<br>";
echo "1. When Android client sends transaction data we'll store the data in our db.<br>";
echo "2. When Cisco server queries our db we'll return transaction history.";

function query_connect($server, $user, $pwd, $db){
    $link = mysqli_connect($server, $user, $pwd, $db);
    if (!$link){
        echo "Error: Unable to connect to mySQL".PHP_EQL;
        echo "Debugging errno: " . mysqli_connect_errno().PHP_EQL;
        echo "Debugging error: " . mysqli_connect_error().PHP_EQL;
        return false;
    }
    return $link;
}

function do_query($link, $sql){
    $resultArr = array();
    $result = mysqli_query($link, $sql);
    if (mysqli_num_rows($result) > 0) {
        while ($row = mysqli_fetch_assoc($result)) {
            $resultArr[] = $row;
        }
        return $resultArr;
    }
    return false;
}

//Recieve transaction data from Android
if(isset($_POST['request_type']) && $_POST['request_type'] == "transaction_data"){
    $amount = ""; $phone_number = "";
    if (isset($_POST['amount'])){
        $amount = $_POST['amount'];
    }
    if (isset($_POST['phone_number'])){
        $phone_number = $_POST['phone_number'];
    }

    //default insertion query
    //@TODO : determine id based on phone number (inner join)
    $sql = "insert into fh_records (user_id, amount, payment_date) VALUES(2, ".$amount.", NOW());";

    $link = query_connect("localhost", "root", "", "finhacks");
    if ($link){
        //save record for current user
        $result = do_query($link, $sql);
        echo "Your data has been saved!";
    }
}

//History request from Cisco server
if(isset($_POST['request_type']) && $_POST['request_type'] == "transaction_query"){
    //expected post params
    $phone_number = ""; $message = "";

    if (isset($_POST['phone_number'])){
        $phone_number = $_POST['phone_number'];
    }
    if (isset($_POST['message'])){
        $message = $_POST['message'];
    }
    //hardcorde to test
    $messsage = "What did I buy today?";

    /*
    Keywords:
    -daily, today
    -weekly, week
    -monthly, month
    -price
    -card type

    Concatenations:
    time_range
    order
    where
    */

    //default time range
    $time_range = array("start" => "2015-01-01 12:00:00", "end" => "2020-01-01 12:00:00");

    if (strpos($message, 'month') !== false ||  strpos($message, 'monthly') !== false) {
        //@TODO: generate month dynamically
        $time_range['start'] = "2016-11-01 00:00:00";
        $time_range['end'] = "2016-11-30 23:59:59";
    }
    if (strpos($message, 'week') !== false ||  strpos($message, 'weekly') !== false) {
        //@TODO: generate week dynamically
        $time_range['start'] = "2016-11-14 00:00:00";
        $time_range['end'] = "2016-11-21 23:59:59";
    }
    if (strpos($message, 'today') !== false ||  strpos($message, 'daily') !== false) {
        //@TODO: generate day dynamically
        $time_range['start'] = "2016-11-19 00:00:00";
        $time_range['end'] = "2016-11-19 23:59:59";
    }

    //dynamic query
    $sql = "select * from fh_records where payment_date > '" . $time_range['start'] . "' and payment_date < '" . $time_range['end']."'";
    echo "<br>".$sql;

    //parse request parameters and query db
    $link = query_connect("localhost", "root", "", "finhacks");

    if ($link){
        $result = do_query($link, $sql);
        //var_dump($result);
        echo json_encode($result);
    }
}




?>

<?php

/*echo "Hey what's up!<br>";
echo "1. When Android client sends transaction data we'll store the data in our db.<br>";
echo "2. When Cisco server queries our db we'll return transaction history.";*/

//@TODO: Add card type?

//for time functions, technically should be passed in from client but YOLO lmao
date_default_timezone_set('America/Toronto');

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
    $result = mysqli_query($link, $sql); //var_dump(mysqli_query($link, $sql)); die;
    if($result === true){ //important to use strict comparison here
        return true;
    }
    if (mysqli_num_rows($result) > 0) {
        while ($row = mysqli_fetch_assoc($result)) {
            $resultArr[] = $row;
        }
        return $resultArr;
    }
    return false;
}

function check_phone_number($phone_number){
    $link = query_connect("localhost", "root", "", "finhacks");
    $sql = "select * from fh_users where phone_number = $phone_number;";
    if ($link){
        $result = do_query($link, $sql);
        //number valid
        if($result){
            return true;
        }else{
            echo "Invalid Phone Number! ".$phone_number;
            die;
        }
    }else {
        die;
    }
}

$is_valid_request = false;

//Recieve transaction data from Android
if(isset($_POST['request_type']) && $_POST['request_type'] == "transaction_data"){
    $is_valid_request = true;
    $amount = "0.00"; $phone_number = "6138231111";
    if (isset($_POST['amount'])){
        $amount = $_POST['amount'];
    }
    if (isset($_POST['phone_number'])){
        $phone_number = $_POST['phone_number'];
        check_phone_number($phone_number);
    }

    //default insertion query
    $sql = "insert into fh_records (user_id, amount, payment_date) VALUES((select user_id from fh_users where phone_number = $phone_number), ".$amount.", NOW());";

    $link = query_connect("localhost", "root", "", "finhacks");
    if ($link){
        //save record for current user
        $result = do_query($link, $sql);
        if($result){
            echo "Your data has been saved!";
        }else{
            echo "Error saving data.<br>";
            echo $sql;
        }
    }
}

function parse_sql_timestamp($timestamp, $format = 'd-m-Y')
{
    $date = new DateTime($timestamp);
    return $date->format($format);
}

//History request from Cisco server
if(isset($_POST['request_type']) && $_POST['request_type'] == "transaction_query"){
    $is_valid_request = true;

    //expected post params
    $phone_number = ""; $message = "";

    if (isset($_POST['phone_number'])){
        $phone_number = strtolower($_POST['phone_number']);
        check_phone_number($phone_number);
    }
    if (isset($_POST['message'])){
        $message = strtolower($_POST['message']);
    }
    //test
    $messsage = "What did I buy today?";

    /*
    Keywords:
    -daily, today
    -weekly, week
    -monthly, month
    -price
    -expensive
    -card type

    Concatenations:
    time_range
    order
    where
    */

    //default time range includes all transactions
    $time_range = array("start" => "2015-01-01 12:00:00", "end" => "2020-01-01 12:00:00");
    $lmao = "all recorded purchases";
    if (strpos($message, 'month') !== false ||  strpos($message, 'monthly') !== false) {
        $lmao = " the month";
        $time_range['start'] = date('Y-m-d G:i:s', mktime(date("H"), date("i"), date("s"), date("m")  , date("d")-30, date("Y")));;
        $time_range['end'] = date('Y-m-d G:i:s');
        //var_dump($time_range); die;
    }
    if (strpos($message, 'week') !== false ||  strpos($message, 'weekly') !== false) {
        $lmao = "the week";
        $time_range['start'] = date('Y-m-d G:i:s', mktime(date("H"), date("i"), date("s"), date("m")  , date("d")-7, date("Y")));
        $time_range['end'] = date('Y-m-d G:i:s');
        //var_dump($time_range); die;
    }
    if (strpos($message, 'today') !== false ||  strpos($message, 'daily') !== false) {
        $lmao = "the day";
        $time_range['start'] = date('Y-m-d G:i:s', mktime(date("H"), date("i"), date("s"), date("m")  , date("d")-1, date("Y")));;
        $time_range['end'] = date('Y-m-d G:i:s');
        //var_dump(parse_sql_timestamp($time_range)); die;
    }
    //@TODO : allow custom date ranges

    if (strpos($message, 'expensive') !== false ||  strpos($message, 'pice') !== false) {
        $order = " order by payment_date, amount desc ";
    }else{
        $order = "order by payment_date desc";
    }

    preg_match_all('!\d+!', $message, $matches);
    if(count($matches[0])>0){
        $limit = "limit ".$matches[0][0]; //lmaoooooo
        //var_dump ($matches); die;
    }else{
        $limit = "";
    }

    if (strpos($message, 'expensive') !== false ||  strpos($message, 'price') !== false) {
        $order = " order by payment_date, amount desc ";
    }else{
        $order = " order by payment_date desc";
    }

    //dynamic query
    $sql = "select * from fh_records where payment_date > '" . $time_range['start'] . "' and payment_date < '" . $time_range['end']."' and user_id = (select user_id from fh_users where phone_number = $phone_number)".$order." $limit";

    //var_dump($matches); die;

    //query db and output result
    $link = query_connect("localhost", "root", "", "finhacks");
    if ($link){
        $result = do_query($link, $sql);

        //@TODO: pretty print the output
        //echo json_encode($result);
        $total = 0;
        foreach($result as $key => $row){
            $total += $row['amount'];
        }
        if (strpos($message, 'total') !== false ||  strpos($message, 'sum') !== false) {
            echo "Total for $lmao: $total";
            die;
        }
        foreach($result as $key => $row){
            echo "Date: ".$row['payment_date']." Price: ".$row['amount']." --- ";
        }
    }
}

//invalid request
if($is_valid_request == false){
    echo "HTTP 500: Sorry, you've sent an invalid request!";
    //print_r($_POST);
    //print_r($_GET);
}



?>

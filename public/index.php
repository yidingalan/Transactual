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

//Request from Cisco server
if(1 || isset($_POST['request_type']) && $_POST['request_type'] == "transaction_query"){
    //parse request parameters and query db
    $link = query_connect("localhost", "root", "", "finhacks");
    if ($link){
        $result = do_query($link, "select * from fh_records");
        //var_dump($result);
        echo json_encode($result);
    }
}

//




?>

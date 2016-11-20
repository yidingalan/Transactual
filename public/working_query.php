<?php

$url = 'http://4afaecea.ngrok.io/';

$ch = curl_init();
$post_fields = array("request_type" => "transaction_query");

curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLINFO_HEADER_OUT, true);
curl_setopt($ch, CURLOPT_POST, true);
curl_setopt($ch, CURLOPT_POSTFIELDS, $post_fields);

$response = curl_exec($ch);
//print $response . "\n\n";
$request_header_info = curl_getinfo($ch, CURLINFO_HEADER_OUT);
print $request_header_info;
//curl_close($ch);

call("+16476187566", array(
    "network" => "SMS")
    );
    say("fuuuuu ccccckkkkkkk".$response);

?>

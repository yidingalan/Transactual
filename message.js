call("+16476319895", {
   network:"SMS"});
// //say("Don't forget your meeting at 2 p.m. on Wednesday!yooooooo");
//
// //Testing some responsiveness stuff
// if(currentCall.initialText == "today"){
//    say("You spent a lot today bruh");
// }
// else if(currentCall.initialText == "week"){
//    say("wow you spent a LOT this week");
// }
// //else{
// //   say("wat haha");
// //}

//Load jQuery
// var script = document.createElement('script');
// script.src = 'http://code.jquery.com/jquery-1.11.0.min.js';
// script.type = 'text/javascript';
// document.getElementsByTagName('head')[0].appendChild(script);

// call("+16476319895", {
//    network:"SMS"});


var text;
var http = new XMLHttpRequest();
var response;

//Start of post request
var url = "http://4afaecea.ngrok.io/index.php";
var params = "request_type=transaction_query&phone_number=+16476319895&message=daily";
http.open("POST", url, true);

http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

http.onreadystatechange = function(){
  console.log(http.readyState);
  console.log(http.status);
  //Note that this is a async function
    if(http.readyState == 4 && http.status == 200) {
      //response = http.responseText;
        console.log(http.responseText);
        response = JSON.parse(http.responseText);
        console.log(response);

        var length = response.length;
        for (var i = 0; i < length; i++){
          text += "You spent " + response[i].amount + " on " + response[i].payment_date + "\n";
        }
        console.log(text);
        console.log(typeof(text));
        say(text);
        //console.log(obj);
    }
}
http.send(params);
//console.log(response);

// function getData(done, fail) {
//     $.getJSON(url, function(data) {
//         if (typeof(done) === 'function') done(data);
//         console.log("success");
//     }).fail(function() {
//     if (typeof(fail) === 'function') fail();
//    console.log("fail");
//     });
// }
//
// var success = function (data) {
//     if (!data) return;
//     var length = data.length;
//
//     for (var i = 0; i < length; i++){
//         text += data[i] + " ";
//     }
// };
//
// var fail = function () {
//     return;
// };
//
// var getText = getData(success, fail);
//console.log(typeof(text));
//console.log(text);
//console.log(response);

// say(text);

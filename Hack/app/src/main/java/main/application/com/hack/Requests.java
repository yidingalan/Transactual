package main.application.com.hack;

/**
 * Created by Mark Emery on 11/19/2016.
 */

//import required Java libraries
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import java.util.*;
import okhttp3.*;

//CHANGE URL IN postToServer METHOD BEFORE PRESENTING-----------------------------------------------

public class Requests {

    //class fields, simulating browser
    private final String USER_AGENT = "Mozilla/5.0";

    public double RESULT;
//    //main loop, starting point for JVM
//    public static void main(String[] args) throws Exception {
//
//        //instance of main class
//        Requests http = new Requests();
//        http.customPost("C:\\Users\\Mark Emery\\Downloads\\unnamed.png");
//
//		/*String message = http.parseJsonParams(response);*/
//		/*System.out.println(message);*/
//    }

    public void customPost(String filename, String phoneNumber) throws Exception{
        String url = "http://api.ocr.space/parse/image";
        String apiKey = "37c7b26dcb88957";
        File file = new File(filename);
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse("image/png"), file))
                .addFormDataPart("apikey", apiKey)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        Log.e("Requests.java: ", responseBody);
        Log.e("Requests.java;", purchaseCard(responseBody) + "\nMax: " + purchaseValue(responseBody));
        postToServer(purchaseCard(responseBody), purchaseValue(responseBody), phoneNumber);
    }

    private void postToServer(String card, double payment, String phoneNumber) throws Exception {

        String url = "http://124c8d97.ngrok.io";

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("request_type", "transaction_data")
                .add("amount", Double.toString(payment))
                .add("phone_number", phoneNumber)
//                .add("card", card)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        Log.e("Requests.java:", response.body().string());
    }

    private String purchaseCard(String responseBody) throws Exception{
        JSONParser parser = new JSONParser();
        JSONObject jsonResponse = (JSONObject) parser.parse(responseBody);
        JSONArray jsonArray= (JSONArray) jsonResponse.get("ParsedResults");
        jsonResponse = (JSONObject) jsonArray.get(0);
        String parsedResponse = (String) jsonResponse.get("ParsedText");
        String[] possibleCards = {"VISA", "Visa", "Mastercard", "MASTERCARD"};
        String cardUsed = "DEFAULT_ERROR";
        for (String card : possibleCards) {
            if (!cardUsed.equals("DEFAULT_ERROR") || ( (parsedResponse.indexOf(possibleCards[0])== -1)
                    && (parsedResponse.indexOf(possibleCards[1])== -1)&& (parsedResponse.indexOf(possibleCards[2])== -1)
                    && (parsedResponse.indexOf(possibleCards[3])== -1) )) {
                break;
            } else {
                cardUsed = parsedResponse.substring(parsedResponse.indexOf(card), parsedResponse.indexOf("\n", parsedResponse.indexOf(card)));
            }
        }
        return cardUsed;
    }

    private double purchaseValue(String responseBody){
        String str = "DEFAULT_ERROR";
        Log.d("Vals: ", responseBody.replaceAll("[^$?.?0-9]+", " "));
        if(responseBody.replaceAll("[^$?.?0-9]+", " ").equals("")){
            return -1.0;
        }
        str = responseBody.replaceAll("[^$?.?0-9]+", " ");
        Log.d("Values: ", str);
        str = str.replaceAll("[^.?0-9]+", " ");
        Log.d("Values: ", str);
        List list = Arrays.asList(str.trim().split(" "));
        Log.d("List: ", str);
        double maximum = -1.0;
        for (Object temp : list) {
            if(temp.toString().indexOf('.') != -1 &&
                    !(temp.toString().replaceAll("[^0-9]+", "").equals(""))){

                Log.e("Ohmygod: ", Double.toString(Double.parseDouble(temp.toString())));
                if(Double.parseDouble(temp.toString()) > maximum){
                    maximum = Double.parseDouble(temp.toString());
                    Log.d("Maximum = ", Double.toString(maximum));
                }
            }
        }
        RESULT = (double)((int)(maximum*100))/100;
        return (double)((int)(maximum*100))/100;
    }
}

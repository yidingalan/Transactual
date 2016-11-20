package com.example;

/**
 * Created by Mark Emery on 11/19/2016.
 */

//import required Java libraries
import java.io.File;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import java.util.*;
import okhttp3.*;

//CHANGE URL IN postToServer METHOD BEFORE PRESENTING-----------------------------------------------

public class Requests {

    //class fields, simulating browser
    private final String USER_AGENT = "Mozilla/5.0";

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

    private void customPost(String filename) throws Exception{
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

        System.out.println(purchaseCard(responseBody) + "\nMax: " + purchaseValue(responseBody));
        postToServer(purchaseCard(responseBody), purchaseValue(responseBody));

    }

    private void postToServer(String card, double payment) throws Exception {

        String url = "https://3a57884e.ngrok.io/";

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("request_type", "transaction_data")
                .add("amount", Double.toString(payment))
                .add("phone_number", "????")
//                .add("card", card)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        System.out.print(response.body().string());
    }

    private String purchaseCard(String responseBody) throws Exception{
        JSONParser parser = new JSONParser();
        JSONObject jsonResponse = (JSONObject) parser.parse(responseBody);
        JSONArray jsonArray= (JSONArray) jsonResponse.get("ParsedResults");
        jsonResponse = (JSONObject) jsonArray.get(0);
        String parsedResponse = (String) jsonResponse.get("ParsedText");
        String[] possibleCards = {"VISA", "Visa", "Mastercard", "MASTERCARD"};
        String cardUsed = "";
        for (String card : possibleCards){
            if(!cardUsed.equals("")) break;
            cardUsed = parsedResponse.substring(parsedResponse.indexOf(card), parsedResponse.indexOf("\n", parsedResponse.indexOf(card)));
        }

        return cardUsed;
    }

    private double purchaseValue(String responseBody){
        String str = responseBody.replaceAll("[^$?.?0-9]+", " ");
        str = str.replaceAll("[^.?0-9]+", " ");
        List list = Arrays.asList(str.trim().split(" "));
        double maximum = -1.0;
        for (Object temp : list) {
            if(temp.toString().indexOf('.') != -1){
                if(Double.parseDouble(temp.toString()) > maximum){
                    maximum = Double.parseDouble(temp.toString());
                }
            }
        }

        return maximum;
    }
}

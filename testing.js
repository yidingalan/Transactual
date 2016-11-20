// call("+16476319895", {
//    network:"SMS"});

function post(url, options) {
    if(options.query) {
        url += "?";
        var delimiter = "";
        for(var propName in (options.query||{})) {
            url += (delemiter + (propName + "=" + escape(options.query[propName])));
            delimiter = "&";
        }
    }
    console.log("Fetching " + url);
    var code;

    var body = options.body;
    if(body == null) {
        throw {message:"Body is required"};
    }

    try {

        // Open Connection
        connection = new java.net.URL(url).openConnection();

        // Set timeout
        var timeout = options.timeout ? options.timeout : 10000;
        connection.setReadTimeout(timeout);
        connection.setConnectTimeout(timeout);

        // Method == POST
        connection.setRequestMethod("POST");

        // Set Content Type
        var contentType = options.contentType != null ? options.contentType : "application/json; charset=utf-8";
        connection.setRequestProperty("Content-Type", contentType);

        // Set Content Length
        connection.setRequestProperty("Content-Length", body.length);

        // Silly Java Stuff
        connection.setUseCaches (false);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        //Send Post Data
        bodyWriter = new java.io.DataOutputStream(connection.getOutputStream());
        bodyWriter.writeBytes(body);
        bodyWriter.flush ();
        bodyWriter.close ();

        code = connection.getResponseCode();
    }
    catch(e) {
        throw {message:("Socket Exception or Server Timeout: " + e), code:0};
    }
    if(code < 200 || code > 299) {
        throw {message:("Received non-2XX response: " + code), code:code};
    }
    is = null;
    try {
        is = connection.getInputStream();
        return new String(org.apache.commons.io.IOUtils.toString(is));
    }
    catch(e) {
        throw {message:("Failed to read server response"), code:0};
    }
    finally {
        try {if(is != null)is.close();} catch (err){}
    }

}

post("http://4afaecea.ngrok.io/index.php", {body : "Hello, Post Bin!"});
//say("testing");

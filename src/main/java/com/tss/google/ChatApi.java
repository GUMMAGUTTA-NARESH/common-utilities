/**
 * 
 */
package com.tss.google;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.poi.openxml4j.opc.internal.ContentType;

/**
 * @author NARESH GUMMAGUTTA
 *@since 7 Aug, 2021
 */
public class ChatApi {
	private void sendPost() throws IOException {

	    String url = "https://chat.googleapis.com/v1/spaces/AAAAUfABqBU/messages?key=<WEBHOCK-KEY>";

	    final HttpClient client = new DefaultHttpClient();
	    final HttpPost request = new HttpPost(url);
	    // FROM HERE

	    request.addHeader("Content-Type", "application/json; charset=UTF-8");

//	    final StringEntity params = new StringEntity("{\"text\":\"Hello from Java!\"}", 
//	    		ContentType.APPLICATION_FORM_URLENCODED);
//	    request.setEntity(params);

	    // TO HERE
	    final org.apache.http.HttpResponse response = client.execute(request);

	    final BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

	    StringBuffer result = new StringBuffer();
	    String line;
	    while ((line = rd.readLine()) != null) {
	        result.append(line);
	    }

	    System.out.println(result.toString());
	}

}

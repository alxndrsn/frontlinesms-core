/*
 * FrontlineSMS <http://www.frontlinesms.com>
 * Copyright 2011 kiwanja
 * 
 * This file is part of FrontlineSMS.
 * 
 * FrontlineSMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 * 
 * FrontlineSMS is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with FrontlineSMS. If not, see <http://www.gnu.org/licenses/>.
 */
package yo.sms.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Handles our network connections to the SMS gateway
 * @author Eric
 *
 */
public class HttpConnection {
    //Hard coding for now
    public static final String PRIMARY_GATEWAY_ADDRESS = "switch2.yo.co.ug";
    private static final String PORT = "9100";
    private static final int CONNECT_TIME_OUT = 10000;
    private static final int SOCKET_TIME_OUT = 10000;
    
    public static String postData(String data, String path, boolean secure) throws IOException{
	URL url = new URL(secure?"https://":"http://" + PRIMARY_GATEWAY_ADDRESS + ":" + PORT + path);
	HttpURLConnection connection = null;
	try {
	    connection = (HttpURLConnection) url.openConnection();
	    connection.setConnectTimeout(CONNECT_TIME_OUT);
	    connection.setReadTimeout(SOCKET_TIME_OUT);
	    connection.setRequestMethod("POST");
	    connection.setRequestProperty("Content-Type", "text/xml; charset=\"utf-8\"");

	    connection.setUseCaches(false);
	    connection.setDoOutput(true);

	    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
	    outputStream.writeBytes(data);
	    outputStream.flush();
	    outputStream.close();

	    InputStream inputStream = connection.getInputStream();
	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	    String line;
	    StringBuffer response = new StringBuffer();
	    while ((line = bufferedReader.readLine()) != null) {
		response.append(line);
		response.append('\r');
	    }
	    bufferedReader.close();
	    return response.toString();

	} finally {
	    if (connection != null) {
		connection.disconnect();
	    }
	}
    }
    
}

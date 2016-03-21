/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.defra.jncc.wff.services;

import org.springframework.stereotype.Component;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.URISyntaxException;


/**
 *
 * @author felix
 */
@Service
public class RestClientService {
    
    public String Get(URI uri) throws Exception {
        HttpGet get = new HttpGet(uri);
        return GetResult(get);
    }

    public String Get(String url) throws Exception {
        HttpGet get = new HttpGet(url);
        return GetResult(get);
    }
    
    private String GetResult(HttpGet get) throws IOException
    {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            // Create a custom response handler
            ResponseHandler<String> responseHandler = (final HttpResponse response) -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            
            String responseBody = httpclient.execute(get, responseHandler);
            return responseBody;
        }
    }
}

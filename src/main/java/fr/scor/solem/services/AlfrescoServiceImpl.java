package fr.scor.solem.services;

import java.io.IOException;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson.JacksonFactory;

import fr.scor.solem.model.NetworkEntry;
import fr.scor.solem.model.NetworkList;
import fr.scor.solem.util.Config;

/**
 * @author Rachid
 *
 */
public class AlfrescoServiceImpl {

    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	private HttpRequestFactory requestFactory;

    private String host 			= "";
    private String userName 		= "";
    private String userPassword 	= "";

    /**
     * Select the home network for the user Solem
     * 
     * @return homeNetwork
     * @throws IOException
     */
    public String getHomeNetwork() throws IOException {
    	
        String homeNetwork = "";
        
        GenericUrl url = new GenericUrl(getAlfrescoAPIUrl());

        HttpRequest request = getRequestFactory().buildGetRequest(url);

        NetworkList networkList = request.execute().parseAs(NetworkList.class);
                    
        for (NetworkEntry networkEntry : networkList.list.entries) {
            if (networkEntry.entry.homeNetwork) {
                homeNetwork = networkEntry.entry.id;
            }
        }

        if (homeNetwork.equals("")) {
            homeNetwork = "-default-";
        }

        return homeNetwork;
    }
    
    /**
     * create a request factory with basic authentification
     *
     * @return HttpRequestFactory
     */
    public HttpRequestFactory getRequestFactory() {
        if (this.requestFactory == null) {
            this.requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) throws IOException {
                    request.setParser(new JsonObjectParser(new JacksonFactory()));
                    request.getHeaders().setBasicAuthentication(userName, userPassword);
                }
            });
        }
        return this.requestFactory;
    }
    
    
    /**
     * Load parameters from config.properties
     * 
     * @return the Alfresco api URL
     */
    public String getAlfrescoAPIUrl() {
    	
    	Config config = new Config();
    	
        host 			= config.getConfig().getProperty("host");      
        userName 		= config.getConfig().getProperty("username");
        userPassword 	= config.getConfig().getProperty("password");
                
        return host + "/api/";
    }
    
}

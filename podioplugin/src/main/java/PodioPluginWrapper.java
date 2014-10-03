import com.podio.item.Item;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by dylan on 10/3/14.
 */
public class PodioPluginWrapper {
    private String clientId;
    private String clientSecret;
    private String username;
    private String password;
    private String externalId;
    private String app_id ;
    private int limit;
    private Item targetedItem;

    private String access_token;
    private String refresh_token;
    private String apiPrefix = "https://api.podio.com";
    private String authPrefix ="https://podio.com/oauth/token?";
    private final static Logger LOGGER = Logger.getLogger(PodioPlugin.class.getName());

    //public vars
    public final String VERSION = "Podio Java Wrapper, version 1.0";

    /*
     * Constructors
     */

    public PodioPluginWrapper(String clientID, String clientSecret, String refresh_token){
        this.clientId = clientID;
        this.clientSecret = clientSecret;
        this.refresh_token = refresh_token;
    }

    public PodioPluginWrapper(String clientID, String clientSecret, String refresh_token, String app_id){
        this.clientId = clientID;
        this.clientSecret = clientSecret;
        this.refresh_token = refresh_token;
    }


    public void connect() {

        try {

            LOGGER.info("Calling CONNECT!");
            // get http client
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(authPrefix);

            // set form variables
            List<NameValuePair> nvPairs = new ArrayList<NameValuePair>();
            nvPairs.add(new BasicNameValuePair("client_id", this.clientId));
            nvPairs.add(new BasicNameValuePair("client_secret", this.clientSecret));
            nvPairs.add(new BasicNameValuePair("refresh_token", this.refresh_token));
            nvPairs.add(new BasicNameValuePair("grant_type", "refresh_token"));
            httpPost.setEntity(new UrlEncodedFormEntity(nvPairs));

            // execute
            HttpResponse response = httpClient.execute(httpPost, localContext);
            HttpEntity entity = response.getEntity();

            // parse result as JSON
            JSONObject rc = new JSONObject(getASCIIContentFromEntity(entity));
            this.access_token = rc.getString("access_token");

            System.out.println(access_token);


        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public JSONObject getItems(String app_id){
        JSONObject rc = null;

        final String ADDR = apiPrefix + "/item/app/"+app_id+"/filter/";

        try{
            connect();

            // get http client
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(new URI(ADDR));

            // add auth headers
            httpPost.addHeader("Authorization", "Bearer " + access_token);

            // execute
            HttpResponse response = httpClient.execute(httpPost, localContext);
            HttpEntity entity = response.getEntity();

            // parse result as JSON
            rc = new JSONObject(getASCIIContentFromEntity(entity));
            System.out.println(rc.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return rc;


    }

    public JSONObject getItemById(String itemId){
        JSONObject rc=null;
        try {
            connect();

            // get http client
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet(apiPrefix + "/item/" + itemId);

            // set header
            httpGet.addHeader("Authorization","Bearer " + access_token);

            // execute
            HttpResponse response = httpClient.execute(httpGet, localContext);
            HttpEntity entity = response.getEntity();

            // parse result as JSON
            rc = new JSONObject(getASCIIContentFromEntity(entity));
            System.out.println(rc);
//            LOGGER.log(Level.INFO,"[connect] Received JSON: " + rc.toString());


        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rc;
    }

    protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException
    {
        InputStream in = entity.getContent();
        StringBuffer out = new StringBuffer();
        int n = 1;
        while (n>0) {
            byte[] b = new byte[4096];
            n =  in.read(b);
            if (n>0)
                out.append(new String(b, 0, n));
        }
        return out.toString();
    }
}

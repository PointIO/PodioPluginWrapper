

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

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
    private String externalId;
    private String app_id ;
    private int limit;

    private String access_token;
    private String refresh_token;
    private String apiPrefix = "https://api.podio.com";
    private String authPrefix ="https://podio.com/oauth/token?";
    private final static Logger LOGGER = Logger.getLogger(PodioPluginWrapper.class.getName());

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

    public PodioPluginWrapper(String access_token){
        this.access_token = access_token;
    }


    public String connect() {
        JSONObject rc = null;

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
            rc = new JSONObject(getASCIIContentFromEntity(entity));
            // update access_token
            access_token = rc.getString("access_token");

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
        return rc.toString();
    }
    /**
     * @param app_id
     *
     * Returns the items on app matching the given filters.
     * Reference:
     * https://developers.podio.com/doc/items/get-items-27803
     *
     * GET /item/app/{app_id}/
     */
    public String getItems(String app_id){
        String rc = null;

        final String ADDR = apiPrefix + "/item/app/"+app_id+"/filter/";

        try{

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
            rc = getASCIIContentFromEntity(entity);
            System.out.println(rc.toString());


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
    /**
     * @param itemId
     * Returns the item with the specified id.
     * Reference:
     * https://developers.podio.com/doc/items/get-item-22360
     * GET /item/{item_id}
     */

    public String getItemById(String itemId){
        String rc=null;
        try {

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
            rc = getASCIIContentFromEntity(entity);
            System.out.println(rc);


        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rc;
    }

    /**
     *
     * @return
     */

    public String getApps(){
        String rc = null;

        final String ADDR = apiPrefix + "/app/";

        try{
            connect();

            // get http client
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet(new URI(ADDR));

            // add auth headers
            httpGet.addHeader("Authorization", "Bearer " + access_token);

            // execute
            HttpResponse response = httpClient.execute(httpGet, localContext);
            HttpEntity entity = response.getEntity();

            // parse result as JSON
            rc = getASCIIContentFromEntity(entity);
            System.out.println(rc.toString());


        }  catch (ClientProtocolException e) {
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
    /**
     * @param app_id
     * @param ExtId
     * Get item by externalId
     * Reference:
     * https://developers.podio.com/doc/items/get-item-by-external-id-19556702
     * GET /item/app/{app_id}/external_id/{external_id}
     */
    public String getItemByExtId(String app_id,String ExtId){
        String rc=null;

        final String ADDR = apiPrefix + "/item/app/" + app_id + "/external_id/" + ExtId;
        try {
            connect();
            System.out.println(ADDR);

            // get http client
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet(new URI(ADDR));


            // set header
            httpGet.addHeader("Authorization","Bearer " + access_token);

            // execute
            HttpResponse response = httpClient.execute(httpGet, localContext);
            HttpEntity entity = response.getEntity();

            // parse result as JSON
            rc = getASCIIContentFromEntity(entity);
            System.out.println(rc);


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
    /**
     * @param itemId
     * @param targetId
     *
     * Update the item values for a specific field.
     * The identifier for the field can either be the field_id or the external_id for the field.
     * Reference:
     * https://developers.podio.com/doc/items/update-item-field-values-22367
     * Restful: PUT /item/{item_id}/value/{field_or_external_id}
     */
    public String updateItemFieldVal(String itemId,String targetId,String values){
        String rc=null;

        final String ADDR = apiPrefix + "/item/"+itemId+"/value/"+targetId;
        try {
            connect();

            // get http client
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpContext localContext = new BasicHttpContext();
            HttpPut httpPut = new HttpPut(new URI(ADDR));

            // set header
            httpPut.addHeader("Authorization","Bearer " + access_token);
            httpPut.addHeader("Content-Type","application/json");

            // set request body
            httpPut.setEntity(new StringEntity(values));

            // execute
            HttpResponse response = httpClient.execute(httpPut, localContext);
            HttpEntity entity = response.getEntity();

            // parse result as JSON
            rc = getASCIIContentFromEntity(entity);
            System.out.println(rc);


        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }  catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return rc;
    }

    /**
     *
     * @param itemId
     * @return
     *
     * Update an already existing item. Values will only be updated for fields included.
     * To delete all values for a field supply an empty array as values for that field.
     * Reference:
     * https://developers.podio.com/doc/items/update-item-22363
     * PUT /item/{item_id}
     *
     */

    public String updateItem(String itemId,String value){
        String rc=null;

        final String ADDR = apiPrefix + "/item/"+itemId;
        try {

            // get http client
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpContext localContext = new BasicHttpContext();
            HttpPut httpPut = new HttpPut(new URI(ADDR));

            // set header
            httpPut.addHeader("Authorization","Bearer " + access_token);
            httpPut.addHeader("Content-Type","application/json");

            // set request body
            httpPut.setEntity(new StringEntity(value));

            // execute
            HttpResponse response = httpClient.execute(httpPut, localContext);
            HttpEntity entity = response.getEntity();

            // parse result as JSON
            rc = getASCIIContentFromEntity(entity);
            System.out.println(rc);


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

    /**
     *
     * @param itemId
     * @return
     * Updates all the values for an item
     * Reference:
     * https://developers.podio.com/doc/items/update-item-values-22366
     * PUT /item/{item_id}/value
     */

    public String updateItemVals(String itemId,String values){
        String rc=null;

        final String ADDR = apiPrefix + "/item/"+itemId+"/value";
        try {

            // get http client
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpContext localContext = new BasicHttpContext();
            HttpPut httpPut = new HttpPut(new URI(ADDR));

            // set header
            httpPut.addHeader("Authorization","Bearer " + access_token);
            httpPut.addHeader("Content-Type","application/json");

            // set request body
            httpPut.setEntity(new StringEntity(values));

            // execute
            HttpResponse response = httpClient.execute(httpPut, localContext);
            HttpEntity entity = response.getEntity();

            // parse result as JSON
            rc = this.getASCIIContentFromEntity(entity);
            System.out.println(rc);


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

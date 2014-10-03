import com.podio.APIFactory;
import com.podio.ResourceFactory;
import com.podio.contact.Profile;
import com.podio.item.*;
import com.podio.oauth.OAuthClientCredentials;
import com.podio.oauth.OAuthUsernameCredentials;
import com.podio.user.UserAPI;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;




/**
 * Created by dylan on 10/2/14.
 */
public class PodioPlugin {

    private static String clientId;
    private static String clientSecret;
    private static String username;
    private static String password;
    private static String externalId;
    private static int app_id ;
    private static int limit;
    private static Item targetedItem;

    /**
     * The cache for existing items
     */
    private final Map<String, List<String>> fileMap = new HashMap<String, List<String>>();
    /*
     * The Podio main API
     */
    APIFactory apiFactory;
    /*
     * resource API
     */
    ResourceFactory resourceFactory;
    /*
     * Podio user API
     */
    UserAPI userAPI;
    Profile profile = null;





    private Logger logger;

    public PodioPlugin(){
        logger = Logger.getLogger(PodioPlugin.class.getName());
    }

    public PodioPlugin(String username,String password,String clientId,String clientSecret){
        this.username = username;
        this.password = password;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        logger = Logger.getLogger(PodioPlugin.class.getName());

    }
    public boolean connect() throws Exception{
        Boolean isConnected = false;


        System.out.print("Calling Podio server: ");

        if(!isConnected){
            resourceFactory = new ResourceFactory(
                    new OAuthClientCredentials(clientId, clientSecret),
                    new OAuthUsernameCredentials(username, password));
            apiFactory = new APIFactory(resourceFactory);

            userAPI = apiFactory.getAPI(UserAPI.class);
            profile = userAPI.getProfile();
            isConnected = true;
            System.out.print("True");
        }

        return isConnected;
    }

    /*
     * list all items specified by app_id
     */
    public List<ItemBadge> listAllItems(int app_id) throws IOException{

        System.out.println("Listing items: ");
//        FilterByValue filterByValue = new FilterByValue<String>()
//        ItemsResponse response = apiFactory.getAPI(ItemAPI.class).getItemsByExternalId(app_id,"company-name");
        ItemsResponse response = apiFactory.getAPI(ItemAPI.class).getItems(app_id,limit,0,null,false);
        System.out.println(response.getTotal());

        if(response.getTotal()==0){
            return null;
        }

        return response.getItems();
    }

    /*
     * List all applications
     */
//    public Application void listAllApps(){
//        //TODO
//    }

    /*
     * List item by itemId
     */
    public List<FieldValuesView> getItem(int itemId){
        return apiFactory.getAPI(ItemAPI.class).getItemValues(itemId);
    }
    /*
     * Updates all the values for an item
     */

//    public void updateItemValues(int itemId, List<FieldValuesUpdate> values){
//        apiFactory.getAPI(Item)
//
//    }


    /*
     * Update the item values for a specific field.
     */
    public void updateItemFieldValues(int itemId,int fieldId,List<Map<String, Object>> values){

        apiFactory.getAPI(ItemAPI.class).updateItemFieldValues(itemId, fieldId, values, true, true);

    }

    /*
     * return itemId by giving externalId
     */
    public Integer getItemId (String externalId) throws Exception{
        ItemsResponse response = apiFactory.getAPI(ItemAPI.class)
                .getItemsByExternalId(app_id,
                        externalId);
        if (response.getFiltered() != 1) {
            return null;
        }

        return response.getItems().get(0).getId();
    }




}


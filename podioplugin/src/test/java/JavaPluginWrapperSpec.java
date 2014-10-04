import com.podio.item.Item;

/**
 * Created by dylan on 10/3/14.
 */
public class JavaPluginWrapperSpec {
    private static String clientId = "dyang";
    private static String clientSecret = "JBlXhbWH1skjCvCyQqDw9BICEdME8Iq2LGoqeD8MEXDxHhtepUJ2VS5jpZg5NtiZ";
    private static String externalId= "share_10063062";
    private static String app_id ="5328623";
    private static String itemId="202558169";
    private static int limit;
    private static String access_token =null;
    private static String refresh_token ="d3382127cd834fa7a2a25192fc7d9325";
    private static String targetItemId = "";

    public static void main (String [] args){
        PodioPluginWrapper podioClient = new PodioPluginWrapper(clientId,clientSecret,refresh_token);

//        podioClient.connect();

//        podioClient.getItems(app_id);

//        podioClient.getItemById(itemId);

        podioClient.getItemByExtId(app_id,externalId);

        podioClient.updateItem(targetItemId);
        podioClient.updateItemFieldVal(targetItemId);
        podioClient.updateItemVals();
    }
}

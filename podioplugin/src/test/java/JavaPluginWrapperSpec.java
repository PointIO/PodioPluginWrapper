import com.podio.item.Item;

/**
 * Created by dylan on 10/3/14.
 */
public class JavaPluginWrapperSpec {
    private static String clientId = "dyang";
    private static String clientSecret = "JBlXhbWH1skjCvCyQqDw9BICEdME8Iq2LGoqeD8MEXDxHhtepUJ2VS5jpZg5NtiZ";
    private static String username = "";
    private static String password;
    private static String externalId ;
    private static String app_id ="5328632";
    private static String itemId="";
    private static int limit;
    private static Item targetedItem;

    private static String access_token =null;
    private static String refresh_token ="d3382127cd834fa7a2a25192fc7d9325";
    public static void main (String [] args){
        PodioPluginWrapper podioClient = new PodioPluginWrapper(clientId,clientSecret,refresh_token);

//        podioClient.connect();
        podioClient.getItems(app_id);
//        podioClient.getItemById("202558169");
    }
}

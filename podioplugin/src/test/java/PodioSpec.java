/**
 * Created by dylan on 10/2/14.
 */
public class PodioSpec {

    public static String username = "dyang@point.io";
    public static String password = "yyl1988223";
    public static String clientId = "dyang";
    public static String clientSecret = "JBlXhbWH1skjCvCyQqDw9BICEdME8Iq2LGoqeD8MEXDxHhtepUJ2VS5jpZg5NtiZ";
    public static String externalId = "company-name";
    public static int app_id = 5328632;
    public static int limit = 20;
    public static int itemid = 202558169;
    public static int fieldId = 41449183;  // Company name field
    public static int fieldid2 = 41449184;  // Contract field



    public static void main(String [] args) throws Exception {
        System.out.println("Hello,World");


        PodioPlugin podioPlugin;

        podioPlugin = new PodioPlugin(username,password,clientId,clientSecret);

        podioPlugin.connect();

        System.out.println(podioPlugin.getItemId("company-name"));

//        List<ItemBadge> itemList =podioPlugin.listAllItems(app_id,externalId);
//        List<ItemBadge> itemList =podioPlugin.listAllItems(app_id);
//
//
//        for(ItemBadge item:itemList){
//            System.out.println(item.getTitle());
//            System.out.println(item.getFields().toArray().toString());
//        }

//        List<FieldValuesView> itemValuesList = podioPlugin.getItem(itemid);
//        for(FieldValuesView itemValue: itemValuesList){
//            System.out.println(itemValue.getLabel());
//            System.out.println(itemValue.getId());
//            System.out.println(itemValue.getValues().toString());
//        }

//        List<Map<String,Object>> newFieldList = new ArrayList<>();
//        Map<String,Object> newFieldValue = new HashMap<>();
//
//        newFieldValue.put("status","active");
//        newFieldValue.put("text","Cold lead");
//        newFieldValue.put("id",1);
//        newFieldValue.put("color","D2E4EB");
//
//
//        newFieldList.add(newFieldValue);
//
//        podioPlugin.updateItemFieldValues(202558169,41449186,newFieldList);









    }

}

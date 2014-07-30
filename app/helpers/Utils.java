package helpers;

import forms.formatters.LatLngFormatter;
import play.data.Form;

import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 15:19
 */
public class Utils {

    public static <T> Form<T> getClassForm(Class formClass, String request) {
        return getClassForm(formClass, request, "latLng");
    }

    public static <T> Form<T> getClassForm(Class formClass, String request, String latLngField) {
        Map<String, String[]> requestMap = new HashMap<String, String[]>();
        String[] parts = request.split("[|]");
        for (String part : parts) {
             if (part.contains(":")) {
                String[] keyValue = part.split("[:]");
                if (keyValue!=null && keyValue.length==2) {
                    addToMap(requestMap,keyValue[0], keyValue[1]);
                }
             } else if (part.matches(LatLngFormatter.latlngPatternString)) {
                 addToMap(requestMap,latLngField, part);
             }
        }
        return Form.form(formClass).bindFromRequest(requestMap);
    }

    private static void addToMap(Map<String, String[]> map, String key, String value) {
        if (map.containsKey(key)) {
            String[] values = map.get(key);
            List<String> list = new ArrayList<String>(Arrays.asList(values));
            list.add(value);
            String[] newValues = list.toArray(values);
            map.put(key, newValues);
        }  else {
           String[] values = {value};
           map.put(key, values);
        }
    }
}

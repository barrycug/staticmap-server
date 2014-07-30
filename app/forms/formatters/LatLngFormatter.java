package forms.formatters;

import org.gradoservice.mapRender.geo.LatLng;
import play.data.format.Formatters;

import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 15:55
 */
public class LatLngFormatter extends Formatters.SimpleFormatter<LatLng> {

    public static final String latlngPatternString = "([-+]?\\d*[.]?\\d+),([-+]?\\d*[.]?\\d+)";
    private static final Pattern latlngPattern = Pattern.compile(latlngPatternString);

    @Override
    public LatLng parse(String s, Locale locale) throws ParseException {
        Matcher matcher = latlngPattern.matcher(s);
        if (!matcher.find()) throw new ParseException("Invalid latlng: "+s,0);
        String lat = matcher.group(1);
        String lng = matcher.group(2);
        return new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
    }

    @Override
    public String print(LatLng o, Locale locale) {
        return o.toString();
    }
}

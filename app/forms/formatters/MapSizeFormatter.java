package forms.formatters;

import forms.MapSize;
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
 * Time: 15:58
 */
public class MapSizeFormatter extends  Formatters.SimpleFormatter<MapSize> {

    private static final String sizePatternString = "(\\d+)x(\\d+)";
    private static final Pattern sizePattern = Pattern.compile(sizePatternString);

    @Override
    public MapSize parse(String s, Locale locale) throws ParseException {
        Matcher matcher = sizePattern.matcher(s);
        if (!matcher.find()) throw new ParseException("Invalid size: "+s,0);
        String width = matcher.group(1);
        String height = matcher.group(2);
        return MapSize.fromStrings(width,height);
    }

    @Override
    public String print(MapSize mapSize, Locale locale) {
        return mapSize.height.toString()+"x"+mapSize.width.toString();
    }
}

package forms;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 13:29
 */
public class MapSize {
    public Integer width;
    public Integer height;

    public MapSize() {
    }

    public MapSize(Integer width, Integer height) {
        this.width = width;
        this.height = height;
    }

    public static MapSize fromStrings(String width, String height) {
        Integer cw = Integer.parseInt(width);
        Integer ch = Integer.parseInt(height);
        return new MapSize(cw, ch);
    }
}

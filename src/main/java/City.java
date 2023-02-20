import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class City {
    private String name;
    private int id;
    private String from, to;
    private int cost;
    private static int idCount = 1;

    public City(String f, String t, int c) {
        from = f;
        to = t;
        cost = c;
        id = idCount++;
    }

    @Override
    public String toString() {
        return "City{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public int getCost() {
        return cost;
    }
    public String getFrom() {
        return from;
    }
    public String getTo() {
        return to;
    }
    public String getCityByID(int id){
        if(this.id == id) {
            return this.name;
        } else {
            return "Nope";
        }
    }
}

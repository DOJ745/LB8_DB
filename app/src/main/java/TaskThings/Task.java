package TaskThings;
import java.io.Serializable;

public class Task implements Serializable {
    private String info;
    private String date;
    private String category;
    private String name;

    public Task(){}

    public Task(String info, String date, String category, String name) {
        this.info = info;
        this.date = date;
        this.category = category;
        this.name = name;
    }

    public String getDate(){ return this.date; }
    public String getInfo(){ return this.info; }
    public String getCategory(){ return category; }
    public String getId(){ return name; }

    public void setCategory(String category) { this.category = category; }
    public void setDate(String date) { this.date = date; }
    public void setInfo(String info) { this.info = info; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString(){ return name + ":" + date + ":" + info + ":" + category; }
}

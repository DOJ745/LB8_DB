package by.bstu.faa.lb8db;

public class Task {
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

    @Override
    public String toString(){ return date + ":" + info + ":" + category; }
}

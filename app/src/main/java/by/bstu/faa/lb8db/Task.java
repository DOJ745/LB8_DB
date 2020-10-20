package by.bstu.faa.lb8db;

public class Task {
    private String info;
    private String date;
    private String category;

    public Task(){}

    public Task(String info, String date, String category) {
        this.info = info;
        this.date = date;
        this.category = category;
    }

    public String getDate(){ return this.date; }
    public String getInfo(){ return this.info; }
    public String getCategory(){ return category; }

    @Override
    public String toString(){ return date + ":" + info + ":" + category; }
}

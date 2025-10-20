package Model;

public class AutomobileModel {
    private int id;
    private String name;

    public AutomobileModel(int id,String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


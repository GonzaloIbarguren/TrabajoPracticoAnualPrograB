package Model;

public class TypeInfraction {
    private TypesInfraction type;
    private double amount;
    private int points;
    private String description;

    public TypeInfraction(TypesInfraction type,double amount,int points,String description) {
        this.amount = amount;
        this.type = type;
        this.points = points;
        this.description= description;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TypesInfraction getType() {
        return type;
    }
    public void setType(TypesInfraction type) {
        this.type = type;
    }
}



package cisgvsu.biotowerdefense;

/**
 * Created by Ella on 8/31/2017.
 */

public class AntibioticTower {

    private AntibioticType type;
    private int power;
    private int cost;
    /** 0 - 4 to begin with */
    private int location;

    public AntibioticTower() {
        this.type = AntibioticType.penicillin;
        this.power = AntibioticType.getPower(type);
        this.cost = AntibioticType.getCost(type);
        this.location = 0;
    }

    public AntibioticTower(AntibioticType type, int location) {
        this.type = type;
        this.power = AntibioticType.getPower(type);
        this.cost = AntibioticType.getCost(type);
        this.location = location;
    }

    public AntibioticType getType() {
        return type;
    }

    public void setType(AntibioticType type) {
        this.type = type;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
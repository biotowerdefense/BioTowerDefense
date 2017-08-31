package cisgvsu.biotowerdefense;

/**
 * Created by Ella on 8/31/2017.
 */

public class AntibioticTower {

    private AntibioticType type;
    private int power;
    private int cost;

    public AntibioticTower() {
        this.type = AntibioticType.penicillin;
        this.power = AntibioticType.getPower(type);
        this.cost = AntibioticType.getCost(type);
    }

    public AntibioticTower(AntibioticType type) {
        this.type = type;
        this.power = AntibioticType.getPower(type);
        this.cost = AntibioticType.getCost(type);
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
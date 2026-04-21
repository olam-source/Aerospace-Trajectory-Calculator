package src;

public class RocketStage {
    public String name;
    public double dryMass, fuelMass, thrust, burnRate, area;

    public RocketStage(String name, double dry, double fuel, double thrust, double burnTime, double area) {
        this.name = name;
        this.dryMass = dry;
        this.fuelMass = fuel;
        this.thrust = thrust;
        this.burnRate = fuel / burnTime;
        this.area = area;
    }
}

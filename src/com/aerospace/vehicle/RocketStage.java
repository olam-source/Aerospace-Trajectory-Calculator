package com.aerospace.vehicle;

/**
 * Represents a single rocket stage with its physical parameters.
 */
public class RocketStage {
    public final String name;
    public final double dryMass;   // kg, mass without propellant
    public final double fuelMass;  // kg, initial propellant mass
    public final double thrust;    // N, constant thrust for the stage
    public final double burnTime; // seconds, total burn duration
    public final double isp;       // s, specific impulse
    public final double area;      // m^2, reference area for drag
    public final double cd;        // drag coefficient (baseline)

    public RocketStage(String name, double dryMass, double fuelMass, double thrust, double burnTime, double isp, double area, double cd) {
        this.name = name;
        this.dryMass = dryMass;
        this.fuelMass = fuelMass;
        this.thrust = thrust;
        this.burnTime = burnTime;
        this.isp = isp;
        this.area = area;
        this.cd = cd;
    }

    /**
     * Calculates the mass flow rate (kg/s) based on thrust and Isp.
     */
    public double massFlowRate() {
        // Thrust = massFlow * g0 * Isp  => massFlow = thrust / (g0 * Isp)
        double g0 = 9.80665;
        return thrust / (g0 * isp);
    }
}

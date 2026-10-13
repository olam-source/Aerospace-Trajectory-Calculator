package com.aerospace.atm;

import com.aerospace.physics.PhysicsVector;
import com.aerospace.vehicle.RocketStage;

/**
 * Calculates aerodynamic drag using a Mach‑dependent drag coefficient.
 */
public class DragCalculator {
    private static final double GAMMA = 1.4; // ratio of specific heats for air
    private static final double R = 287.05; // specific gas constant (J/(kg·K))

    /**
     * Compute drag force vector.
     * @param velocity current velocity vector (m/s)
     * @param stage current rocket stage (may be null for coasting)
     * @param altitude current altitude above sea level (m)
     * @return drag force vector (N) opposite to velocity direction.
     */
    public static PhysicsVector computeDrag(PhysicsVector velocity, RocketStage stage, double altitude) {
        double speed = velocity.magnitude();
        if (speed == 0) {
            return new PhysicsVector(0, 0);
        }
        double rho = AtmosphericModel.getDensity(altitude);
        if (rho < 1e-6) {
            // Near vacuum – negligible drag
            return new PhysicsVector(0, 0);
        }
        double mach = machNumber(speed, altitude);
        double cd = dragCoefficient(mach);
        double area = (stage != null) ? stage.area : 0.0;
        double dragMag = 0.5 * rho * speed * speed * cd * area;
        // Drag opposite to velocity direction
        double ux = -velocity.x / speed;
        double uy = -velocity.y / speed;
        return new PhysicsVector(ux * dragMag, uy * dragMag);
    }

    /**
     * Approximate drag coefficient as a function of Mach number.
     * Simple piecewise model: constant subsonic, increase near Mach 1, higher supersonic.
     */
    private static double dragCoefficient(double mach) {
        double cd0 = 0.45; // baseline subsonic coefficient
        if (mach < 0.8) {
            return cd0;
        } else if (mach < 1.2) {
            // Linear increase up to ~0.7 at Mach 1.2
            return cd0 + (mach - 0.8) * (0.7 - cd0) / (1.2 - 0.8);
        } else {
            // Supersonic plateau around 0.9
            return 0.9;
        }
    }

    /**
     * Speed of sound based on ISA temperature at altitude.
     */
    private static double speedOfSound(double altitude) {
        double T = AtmosphericModel.getTemperature(altitude);
        return Math.sqrt(GAMMA * R * T);
    }

    /**
     * Mach number = speed / speed of sound.
     */
    private static double machNumber(double speed, double altitude) {
        double a = speedOfSound(altitude);
        return speed / a;
    }
}

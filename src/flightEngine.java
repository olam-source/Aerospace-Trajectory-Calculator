package src;
import java.util.*;

public class FlightEngine {
    public static void main(String[] args) {
        System.out.println("HAT-F v6.0 | INITIALIZING MULTISTAGE ENGINE...");
        
        RocketStage s1 = new RocketStage("Booster", 8000, 30000, 650000, 60, 4.5);
        RocketStage s2 = new RocketStage("Orbiter", 2000, 10000, 180000, 100, 2.0);

        double x = 0, y = 0, vx = 15, vy = 100, t = 0, dt = 0.05;

        while (y >= 0) {
            double currentMass = s2.dryMass + s2.fuelMass + (s1.fuelMass > 0 ? s1.dryMass + s1.fuelMass : 0);
            double thrust = (s1.fuelMass > 0) ? s1.thrust : (s2.fuelMass > 0 ? s2.thrust : 0);
            
            double rho = AtmosphericModel.getAirDensity(y);
            double v = Math.sqrt(vx*vx + vy*vy);
            double drag = 0.5 * rho * v * v * 0.45 * (s1.fuelMass > 0 ? s1.area : s2.area);

            double ax = (thrust * (vx/v) - drag * (vx/v)) / currentMass;
            double ay = (thrust * (vy/v) - drag * (vy/v) - (AtmosphericModel.G * currentMass)) / currentMass;

            vx += ax * dt; vy += ay * dt; x += vx * dt; y += vy * dt; t += dt;

            if (s1.fuelMass > 0) s1.fuelMass -= s1.burnRate * dt;
            else if (s2.fuelMass > 0) s2.fuelMass -= s2.burnRate * dt;

            if (Math.round(t*20) % 100 == 0) System.out.printf("T: %.1fs | Alt: %.0fm | V: %.1fm/s\n", t, y, v);
        }
        System.out.println("MISSION COMPLETE. IMPACT DETECTED.");
    }
}

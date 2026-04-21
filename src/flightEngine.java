import java.util.Scanner;

public class FlightEngine {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("--- HAT-F v6.2 TERMINAL INTERFACE ---");

        // 1. User Input Collection
        System.out.print("Enter Stage 1 Thrust (N): ");
        double s1Thrust = sc.nextDouble();
        System.out.print("Enter Stage 1 Fuel Mass (kg): ");
        double s1Fuel = sc.nextDouble();
        System.out.print("Enter Stage 1 Dry Mass (kg): ");
        double s1Dry = sc.nextDouble();
        System.out.print("Enter Drag Coefficient (Cd): ");
        double cd = sc.nextDouble();

        // Constants for Stage 2 (You can expand this to ask for more)
        double s2Thrust = 200000, s2Fuel = 10000, s2Dry = 2000;
        double x = 0, y = 0, vx = 10, vy = 100, t = 0, dt = 0.05;
        double s1BurnRate = s1Fuel / 60.0; 
        double s2BurnRate = s2Fuel / 100.0;
        
        System.out.println("\nLIFTOFF SEQUENCE INITIATED...");

        // 2. Numerical Integration Loop
        while (y >= 0) {
            double currentMass = s2Dry + s2Fuel + (s1Fuel > 0 ? s1Dry + s1Fuel : 0);
            double thrust = (s1Fuel > 0) ? s1Thrust : (s2Fuel > 0 ? s2Thrust : 0);
            
            // Atmospheric density model
            double rho = 1.225 * Math.exp(-y / 8500.0);
            double v = Math.sqrt(vx*vx + vy*vy);
            double drag = 0.5 * rho * v * v * cd * (s1Fuel > 0 ? 4.5 : 2.0);

            double ax = (thrust * (vx/v) - drag * (vx/v)) / currentMass;
            double ay = (thrust * (vy/v) - drag * (vy/v) - (9.81 * currentMass)) / currentMass;

            vx += ax * dt; vy += ay * dt; x += vx * dt; y += vy * dt; t += dt;

            if (s1Fuel > 0) s1Fuel -= s1BurnRate * dt;
            else if (s2Fuel > 0) s2Fuel -= s2BurnRate * dt;

            // Output data every simulated second
            if (Math.round(t*20) % 20 == 0) {
                System.out.printf("T:%.1fs | Alt:%.0fm | Vel:%.1fm/s\n", t, y, v);
            }
            if (t > 2000) break; // Safety cutoff
        }
        System.out.println("--- MISSION SUMMARY ---");
        System.out.printf("Max Altitude Reached: %.2f meters\n", y);
        System.out.println("Impact Recorded.");
    }
}

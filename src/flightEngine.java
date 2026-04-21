package src;
import java.util.Scanner;

public class FlightEngine {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("=========================================");
        System.out.println("   HAT-F ASTRODYNAMICS ENGINE v6.5      ");
        System.out.println("=========================================");

        // Capture Inputs
        System.out.print("Enter Thrust (N): ");
        double thrust = sc.nextDouble();
        System.out.print("Enter Initial Vy (m/s): ");
        double vy = sc.nextDouble();
        System.out.print("Enter Fuel Mass (kg): ");
        double fuel = sc.nextDouble();
        System.out.print("Enter Drag Cd: ");
        double cd = sc.nextDouble();

        // Planet Constants
        double y = 0, t = 0, dt = 0.1;
        double dryMass = 10000;
        double EarthR = 6371000.0;
        double GM = 3.986e14;
        
        System.out.println("\n--- T+0:00 LIFTOFF ---");

        while (y >= 0) {
            double currentMass = dryMass + fuel;
            
            // 1. Calculate Real-Time Gravity based on distance from center of Earth
            double g = GM / Math.pow(EarthR + y, 2);
            
            // 2. Air Density (Approximation)
            double rho = (y > 100000) ? 0 : 1.225 * Math.exp(-y / 8500.0);
            
            // 3. Forces
            double tForce = (fuel > 0) ? thrust : 0;
            double dragForce = 0.5 * rho * (vy * vy) * cd * 4.5;
            
            // 4. Acceleration
            double ay = (tForce - dragForce) / currentMass - g;

            // 5. Update State
            vy += ay * dt;
            y += vy * dt;
            t += dt;
            if (fuel > 0) fuel -= 450 * dt;

            // Escape Velocity Check
            double escapeV = Math.sqrt(2 * GM / (EarthR + y));
            if (vy > escapeV) {
                System.out.println("\n[!] ESCAPE VELOCITY ACHIEVED: " + vy + " m/s");
                System.out.println("[!] PROJECTILE HAS LEFT EARTH'S GRAVITY WELL.");
                break;
            }

            // Logging
            if (Math.round(t * 10) % 200 == 0) {
                System.out.printf("T:%4.1fs | Alt:%8.0fm | Vel:%6.1fm/s | G:%4.2f\n", t, y, vy, g);
            }
            
            if (t > 5000) break; 
        }
        
        System.out.println("\nSIMULATION TERMINATED.");
        System.out.printf("Final Altitude: %.2f m\n", y);
        sc.close();
    }
}

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * ============================================================================
 * AEROSPACE TRAJECTORY ENGINE v5.0 (Cloud/Web IDE Safe)
 * ============================================================================
 * A headless numerical integration environment for 2D rocket kinematics.
 * Designed to execute in server-side, ChromeOS, and web-based IDEs.
 * ============================================================================
 */
public class AerospaceVisualizer {

    // --- Core Physics & Data Classes ---
    static class Telemetry {
        double time, x, y, velX, velY, mass;
        public Telemetry(double t, double x, double y, double vx, double vy, double m) {
            this.time = t; this.x = x; this.y = y; this.velX = vx; this.velY = vy; this.mass = m;
        }
    }

    static class Environment {
        static final double GRAVITY = 9.80665;
        public static double getAirDensity(double altitude) {
            if (altitude < 0) return 1.225;
            double temp = 288.15 - (0.0065 * altitude);
            return (101325 * Math.pow(temp / 288.15, 5.25588)) / (287.05 * temp);
        }
    }

    // --- Main Engine ---
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Telemetry> flightData = new ArrayList<>();

        System.out.println("==================================================");
        System.out.println("     AEROSPACE KINEMATICS ENGINE (CLOUD SAFE)");
        System.out.println("==================================================");
        
        try {
            System.out.print("Launch Velocity (m/s)     [e.g., 300]: "); 
            double initVel = Double.parseDouble(scanner.nextLine());
            
            System.out.print("Launch Angle (deg)        [e.g., 45] : "); 
            double angle = Double.parseDouble(scanner.nextLine());
            
            System.out.print("Rocket Mass (kg)          [e.g., 50] : "); 
            double mass = Double.parseDouble(scanner.nextLine());
            
            System.out.print("Drag Coefficient (Cd)     [e.g., 0.75]: "); 
            double cd = Double.parseDouble(scanner.nextLine());
            
            System.out.print("Cross-Section Area (m^2)  [e.g., 0.05]: "); 
            double area = Double.parseDouble(scanner.nextLine());

            System.out.println("\n[SYSTEM] Initiating Euler Integration Loop...");
            System.out.println("--------------------------------------------------");
            System.out.printf("%-10s %-12s %-12s %-12s\n", "TIME(s)", "ALTITUDE(m)", "RANGE(m)", "VELOCITY(m/s)");
            System.out.println("--------------------------------------------------");

            double dt = 0.01;
            double time = 0;
            double vx = initVel * Math.cos(Math.toRadians(angle));
            double vy = initVel * Math.sin(Math.toRadians(angle));
            double x = 0, y = 0;
            double maxAlt = 0;

            int logCounter = 0;

            while (y >= 0) {
                flightData.add(new Telemetry(time, x, y, vx, vy, mass));
                if (y > maxAlt) maxAlt = y;

                double speed = Math.hypot(vx, vy);
                double drag = 0.5 * Environment.getAirDensity(y) * (speed * speed) * cd * area;
                
                double ax = (speed > 0) ? -(drag * (vx / speed)) / mass : 0;
                double ay = (speed > 0) ? (-Environment.GRAVITY * mass - drag * (vy / speed)) / mass : -Environment.GRAVITY;

                vx += ax * dt;
                vy += ay * dt;
                x += vx * dt;
                y += vy * dt;
                time += dt;

                // Print telemetry to console every 1 second of flight time
                if (logCounter % 100 == 0) {
                    System.out.printf("%-10.1f %-12.2f %-12.2f %-12.2f\n", time, y, x, speed);
                }
                logCounter++;

                if (time > 1000) break; // Safety break
            }

            System.out.println("--------------------------------------------------");
            System.out.println("[SYSTEM] SIMULATION COMPLETE.");
            System.out.printf(">> Apogee (Max Altitude): %.2f meters\n", maxAlt);
            System.out.printf(">> Total Range (X):       %.2f meters\n", x);

            exportToCloud(flightData);

        } catch (NumberFormatException e) {
            System.err.println("\n[FATAL ERROR] Invalid input. Use numbers only (e.g., 45.5).");
        } finally {
            scanner.close();
        }
    }

    private static void exportToCloud(List<Telemetry> data) {
        String filename = "FlightLog_Data.csv";
        // Saves to the direct current working directory of the web IDE
        File file = new File(System.getProperty("user.dir"), filename);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Time(s),PosX(m),PosY(m),VelX(m/s),VelY(m/s)\n");
            for (Telemetry t : data) {
                writer.write(String.format("%.3f,%.3f,%.3f,%.3f,%.3f\n", t.time, t.x, t.y, t.velX, t.velY));
            }
            System.out.println("\n[SUCCESS] Telemetry CSV written to cloud workspace: " + filename);
            System.out.println("Check the file explorer on the left side of your screen.");
        } catch (IOException e) {
            System.err.println("[ERROR] Cloud storage permission denied.");
        }
    }
}
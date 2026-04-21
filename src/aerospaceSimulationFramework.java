import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * ============================================================================
 * HIGH-FIDELITY AEROSPACE TRAJECTORY FRAMEWORK (HAT-F) v6.0
 * ============================================================================
 * Focus: Multistage Vehicle Dynamics & Atmospheric Layer Modeling.
 * This framework simulates a dual-stage orbital ascent profile using 
 * Numerical Integration and Variable Mass Dynamics.
 * ============================================================================
 */
public class AerospaceSimulationFramework {

    // --- 1. CORE PHYSICS ENGINE CLASSES ---

    static class PhysicsVector {
        double x, y;
        public PhysicsVector(double x, double y) { this.x = x; this.y = y; }
        public void add(PhysicsVector v) { this.x += v.x; this.y += v.y; }
        public double getMagnitude() { return Math.sqrt(x * x + y * y); }
        public static PhysicsVector divide(PhysicsVector v, double scalar) {
            return new PhysicsVector(v.x / scalar, v.y / scalar);
        }
    }

    /**
     * Models the 1976 International Standard Atmosphere (ISA) across multiple layers.
     */
    static class AtmosphericModel {
        public static final double R = 287.05; // Gas constant
        public static final double G = 9.80665;

        public static double getDensity(double h) {
            double T, P;
            if (h < 11000) { // Troposphere
                T = 288.15 - 0.0065 * h;
                P = 101325 * Math.pow(T / 288.15, 5.2558);
            } else if (h < 20000) { // Lower Stratosphere
                T = 216.65;
                P = 22632 * Math.exp(-G * (h - 11000) / (R * T));
            } else { // Upper Stratosphere
                T = 216.65 + 0.001 * (h - 20000);
                P = 5474.8 * Math.pow(T / 216.65, -34.163);
            }
            return P / (R * T);
        }
    }

    // --- 2. VEHICLE ARCHITECTURE ---

    static class RocketStage {
        String name;
        double dryMass;     // kg
        double fuelMass;    // kg
        double thrust;      // Newtons
        double burnRate;    // kg/s
        double area;        // m^2
        double cd;          // Drag coefficient

        public RocketStage(String name, double dry, double fuel, double thrust, double time, double area, double cd) {
            this.name = name;
            this.dryMass = dry;
            this.fuelMass = fuel;
            this.thrust = thrust;
            this.burnRate = fuel / time;
            this.area = area;
            this.cd = cd;
        }
    }

    // --- 3. SIMULATION CORE ---

    static class SimulationResults {
        double time, x, y, velocity, mass;
        String event;

        public SimulationResults(double t, double x, double y, double v, double m, String e) {
            this.time = t; this.x = x; this.y = y; this.velocity = v; this.mass = m; this.event = e;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<SimulationResults> log = new ArrayList<>();

        System.out.println(">> INITIALIZING MULTISTAGE FLIGHT DYNAMICS SYSTEM...");
        
        // Define Stage 1 (Booster)
        RocketStage stage1 = new RocketStage("Booster", 8000, 30000, 600000, 60, 4.5, 0.5);
        // Define Stage 2 (Orbital)
        RocketStage stage2 = new RocketStage("Sustainer", 2000, 10000, 150000, 100, 2.0, 0.3);

        double x = 0, y = 0;
        double vx = 10, vy = 100; // Initial "Kick" velocity
        double time = 0;
        double dt = 0.05;
        
        boolean stage1Active = true;
        boolean stage2Active = false;
        String currentEvent = "LIFTOFF";

        System.out.println(">> IGNITION SEQUENCE START.");

        while (y >= 0) {
            double currentTotalMass = 0;
            double currentThrust = 0;
            double currentArea = 0;
            double currentCd = 0;

            // HANDLE MULTISTAGE LOGIC
            if (stage1Active) {
                currentTotalMass = stage1.dryMass + stage1.fuelMass + stage2.dryMass + stage2.fuelMass;
                currentThrust = stage1.thrust;
                currentArea = stage1.area;
                currentCd = stage1.cd;

                stage1.fuelMass -= stage1.burnRate * dt;
                if (stage1.fuelMass <= 0) {
                    stage1Active = false;
                    stage2Active = true;
                    currentEvent = "MECO_STAGE_SEP"; // Main Engine Cut Off
                }
            } else if (stage2Active) {
                currentTotalMass = stage2.dryMass + stage2.fuelMass;
                currentThrust = stage2.thrust;
                currentArea = stage2.area;
                currentCd = stage2.cd;

                stage2.fuelMass -= stage2.burnRate * dt;
                if (stage2.fuelMass <= 0) {
                    stage2Active = false;
                    currentEvent = "SECO"; // Second Engine Cut Off
                }
            } else {
                currentTotalMass = stage2.dryMass; // Just the empty shell falling
                currentThrust = 0;
                currentArea = stage2.area;
                currentCd = stage2.cd;
                currentEvent = "COASTING";
            }

            // PHYSICS CALCULATIONS
            double rho = AtmosphericModel.getDensity(y);
            double speed = Math.sqrt(vx * vx + vy * vy);
            double dragForce = 0.5 * rho * speed * speed * currentCd * currentArea;

            // Acceleration Vectors
            double ax = (currentThrust * (vx / speed) - dragForce * (vx / speed)) / currentTotalMass;
            double ay = (currentThrust * (vy / speed) - dragForce * (vy / speed) - (AtmosphericModel.G * currentTotalMass)) / currentTotalMass;

            // Euler Integration
            vx += ax * dt;
            vy += ay * dt;
            x += vx * dt;
            y += vy * dt;
            time += dt;

            log.add(new SimulationResults(time, x, y, speed, currentTotalMass, currentEvent));
            if (!currentEvent.equals("COASTING")) currentEvent = "STABLE";

            // Safety break
            if (time > 2000) break;
        }

        System.out.println(">> SIMULATION SUCCESSFUL.");
        generateReport(log);
        exportCSV(log);
    }

    private static void generateReport(List<SimulationResults> log) {
        double maxAlt = 0;
        for (SimulationResults r : log) if (r.y > maxAlt) maxAlt = r.y;
        
        System.out.println("\n--- FINAL MISSION REPORT ---");
        System.out.printf("Total Flight Time: %.2f seconds\n", log.get(log.size()-1).time);
        System.out.printf("Maximum Altitude:  %.2f meters\n", maxAlt);
        System.out.printf("Impact Range:      %.2f meters\n", log.get(log.size()-1).x);
        System.out.println("----------------------------\n");
    }

    private static void exportCSV(List<SimulationResults> log) {
        String filename = "MultiStage_FlightLog.csv";
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Time,X,Y,Velocity,Mass,Event\n");
            for (SimulationResults r : log) {
                writer.write(String.format("%.2f,%.2f,%.2f,%.2f,%.2f,%s\n", 
                             r.time, r.x, r.y, r.velocity, r.mass, r.event));
            }
            System.out.println(">> DATA EXPORTED TO: " + filename);
        } catch (IOException e) {
            System.out.println(">> EXPORT FAILED.");
        }
    }
}

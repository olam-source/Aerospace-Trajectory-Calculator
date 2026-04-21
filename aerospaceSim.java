import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * ============================================================================
 * ADVANCED AEROSPACE TRAJECTORY SIMULATOR v3.5
 * ============================================================================
 * A highly extensive numerical integration environment for 2D rocket kinematics.
 * * Features:
 * - Dynamic Mass Depletion (Thrust phase mass-loss modeling)
 * - Barometric Atmospheric Modeling (Air density varies with altitude)
 * - Environmental Wind Vectors affecting aerodynamic drag
 * - Object-Oriented Architecture (Vector2D, Rocket, Environment, Logger)
 * - Granular Time-Step Integration (Euler Method)
 * ============================================================================
 */
public class aerospaceSim {

    // ------------------------------------------------------------------------
    // CORE MATHEMATICAL & PHYSICS CLASSES
    // ------------------------------------------------------------------------

    /**
     * Represents a 2D Mathematical Vector for physical quantities.
     */
    static class Vector2D {
        double x, y;

        public Vector2D(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double magnitude() {
            return Math.sqrt(x * x + y * y);
        }

        public void add(Vector2D other) {
            this.x += other.x;
            this.y += other.y;
        }

        public void scale(double scalar) {
            this.x *= scalar;
            this.y *= scalar;
        }
    }

    /**
     * Models the International Standard Atmosphere (ISA) Troposphere.
     */
    static class Environment {
        static final double GRAVITY = 9.80665;       // Standard gravity (m/s^2)
        static final double SEA_LEVEL_TEMP = 288.15; // Kelvin
        static final double TEMP_LAPSE_RATE = 0.0065;// K/m
        static final double SEA_LEVEL_PRESS = 101325;// Pascals
        static final double GAS_CONSTANT = 287.05;   // J/(kg*K)
        
        Vector2D windVector;

        public Environment(double windX, double windY) {
            this.windVector = new Vector2D(windX, windY);
        }

        /**
         * Calculates air density at a given altitude using the Barometric formula.
         */
        public double getAirDensity(double altitude) {
            if (altitude < 0) return 1.225; // Sea level density
            double temperature = SEA_LEVEL_TEMP - (TEMP_LAPSE_RATE * altitude);
            double exponent = (GRAVITY) / (GAS_CONSTANT * TEMP_LAPSE_RATE);
            double pressure = SEA_LEVEL_PRESS * Math.pow(temperature / SEA_LEVEL_TEMP, exponent);
            return pressure / (GAS_CONSTANT * temperature);
        }
    }

    /**
     * Represents the physical rocket vehicle and its state parameters.
     */
    static class Rocket {
        double dryMass;
        double fuelMass;
        double dragCoefficient;
        double crossSectionalArea;
        double thrustForce;
        double burnTime;
        
        Vector2D position;
        Vector2D velocity;

        public Rocket(double dryMass, double fuelMass, double dragCoeff, double area, 
                      double thrust, double burnTime, double launchVel, double angleDeg) {
            this.dryMass = dryMass;
            this.fuelMass = fuelMass;
            this.dragCoefficient = dragCoeff;
            this.crossSectionalArea = area;
            this.thrustForce = thrust;
            this.burnTime = burnTime;

            double angleRad = Math.toRadians(angleDeg);
            this.position = new Vector2D(0, 0);
            this.velocity = new Vector2D(launchVel * Math.cos(angleRad), launchVel * Math.sin(angleRad));
        }

        public double getTotalMass() {
            return dryMass + fuelMass;
        }

        /**
         * Depletes fuel linearly over the burn time.
         */
        public void consumeFuel(double dt) {
            if (fuelMass > 0) {
                double burnRate = fuelMass / burnTime; // kg/s
                fuelMass -= burnRate * dt;
                if (fuelMass < 0) fuelMass = 0;
            }
        }
    }

    /**
     * Handles data logging and CSV exportation.
     */
    static class TelemetryLogger {
        List<String> logData = new ArrayList<>();

        public void recordFrame(double time, Rocket rocket) {
            String record = String.format("%.4f,%.4f,%.4f,%.4f,%.4f,%.4f", 
                time, rocket.position.x, rocket.position.y, 
                rocket.velocity.x, rocket.velocity.y, rocket.getTotalMass());
            logData.add(record);
        }

        public void exportToCSV(String filename) {
            try (FileWriter writer = new FileWriter(new File(filename))) {
                writer.write("Timestamp(s),PosX(m),PosY(m),VelX(m/s),VelY(m/s),CurrentMass(kg)\n");
                for (String line : logData) {
                    writer.write(line + "\n");
                }
                System.out.println("[SYSTEM] Telemetry successfully exported to: " + filename);
            } catch (IOException e) {
                System.err.println("[ERROR] IO Exception during data export.");
            }
        }
    }

    // ------------------------------------------------------------------------
    // MAIN SIMULATION ENGINE
    // ------------------------------------------------------------------------

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TelemetryLogger logger = new TelemetryLogger();

        printHeader();

        try {
            // 1. Configuration & Input
            System.out.println("--- VEHICLE CONFIGURATION ---");
            System.out.print("Dry Mass (kg): "); double dryMass = scanner.nextDouble();
            System.out.print("Fuel Mass (kg): "); double fuelMass = scanner.nextDouble();
            System.out.print("Drag Coefficient (Cd): "); double cd = scanner.nextDouble();
            System.out.print("Cross-Sectional Area (m^2): "); double area = scanner.nextDouble();
            System.out.print("Engine Thrust (N): "); double thrust = scanner.nextDouble();
            System.out.print("Engine Burn Time (s): "); double burnTime = scanner.nextDouble();

            System.out.println("\n--- LAUNCH PARAMETERS ---");
            System.out.print("Initial Launch Velocity (m/s): "); double initVel = scanner.nextDouble();
            System.out.print("Launch Angle (degrees): "); double angle = scanner.nextDouble();
            System.out.print("Headwind Velocity (m/s, X-axis): "); double windX = scanner.nextDouble();

            // 2. Initialization
            Rocket rocket = new Rocket(dryMass, fuelMass, cd, area, thrust, burnTime, initVel, angle);
            Environment env = new Environment(windX, 0); // Wind primarily on X-axis

            double time = 0.0;
            final double TIME_STEP = 0.01; // 10ms resolution
            double maxAltitude = 0.0;

            System.out.println("\n[SYSTEM] Initializing Simulation Engine...");
            System.out.println("[SYSTEM] Integrating trajectory variables...\n");

            // 3. Integration Loop (The Physics Engine)
            while (rocket.position.y >= 0) {
                // Log current frame
                logger.recordFrame(time, rocket);
                if (rocket.position.y > maxAltitude) maxAltitude = rocket.position.y;

                // Relative Velocity (accounting for wind)
                Vector2D relVel = new Vector2D(
                    rocket.velocity.x - env.windVector.x,
                    rocket.velocity.y - env.windVector.y
                );
                double relSpeed = relVel.magnitude();

                // Aerodynamic Drag
                double currentDensity = env.getAirDensity(rocket.position.y);
                double dragMagnitude = 0.5 * currentDensity * (relSpeed * relSpeed) * rocket.dragCoefficient * rocket.crossSectionalArea;
                
                Vector2D dragForce = new Vector2D(0, 0);
                if (relSpeed > 0) { // Avoid division by zero
                    dragForce.x = -dragMagnitude * (relVel.x / relSpeed);
                    dragForce.y = -dragMagnitude * (relVel.y / relSpeed);
                }

                // Thrust Force (Only applied while fuel exists and during burn time)
                Vector2D thrustForce = new Vector2D(0, 0);
                if (time <= rocket.burnTime && rocket.fuelMass > 0) {
                    double speed = rocket.velocity.magnitude();
                    if (speed > 0) { // Thrust follows the velocity vector
                        thrustForce.x = rocket.thrustForce * (rocket.velocity.x / speed);
                        thrustForce.y = rocket.thrustForce * (rocket.velocity.y / speed);
                    } else { // Initial thrust direction
                        double initAngle = Math.toRadians(angle);
                        thrustForce.x = rocket.thrustForce * Math.cos(initAngle);
                        thrustForce.y = rocket.thrustForce * Math.sin(initAngle);
                    }
                    rocket.consumeFuel(TIME_STEP);
                }

                // Gravity Force
                Vector2D gravityForce = new Vector2D(0, -Environment.GRAVITY * rocket.getTotalMass());

                // Net Force & Acceleration (F_net = F_thrust + F_drag + F_gravity)
                Vector2D netForce = new Vector2D(
                    thrustForce.x + dragForce.x + gravityForce.x,
                    thrustForce.y + dragForce.y + gravityForce.y
                );

                Vector2D acceleration = new Vector2D(
                    netForce.x / rocket.getTotalMass(),
                    netForce.y / rocket.getTotalMass()
                );

                // Euler Integration Step
                rocket.velocity.add(new Vector2D(acceleration.x * TIME_STEP, acceleration.y * TIME_STEP));
                rocket.position.add(new Vector2D(rocket.velocity.x * TIME_STEP, rocket.velocity.y * TIME_STEP));

                time += TIME_STEP;

                // Safety break for infinite loops (e.g., escape velocity reached or hovering)
                if (time > 1000) {
                    System.out.println("[WARNING] Time limit exceeded. Vehicle may have reached orbit.");
                    break;
                }
            }

            // 4. Post-Simulation Summary
            System.out.println("==================================================");
            System.out.println("   MISSION TELEMETRY SUMMARY");
            System.out.println("==================================================");
            System.out.printf("Total Flight Time:    %.2f seconds\n", time);
            System.out.printf("Apogee (Max Alt):     %.2f meters\n", maxAltitude);
            System.out.printf("Downrange Distance:   %.2f meters\n", rocket.position.x);
            System.out.printf("Remaining Fuel Mass:  %.2f kg\n", rocket.fuelMass);
            System.out.println("==================================================");

            // Export to CSV
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            logger.exportToCSV("FlightLog_" + timestamp + ".csv");

        } catch (InputMismatchException e) {
            System.err.println("\n[FATAL ERROR] Non-numeric input detected. Halting simulation.");
        } finally {
            scanner.close();
        }
    }

    private static void printHeader() {
        System.out.println("\n");
        System.out.println("      /\\");
        System.out.println("     /  \\     Aerospace Trajectory Lab");
        System.out.println("    /____\\   [Initialization Sequence Started]");
        System.out.println("    |    |   ");
        System.out.println("   /|    |\\  Powered by Java Numerical Integrator");
        System.out.println("  /_|____|_\\ ");
        System.out.println("    /  \\     ");
        System.out.println("  🔥    🔥   \n");
    }
}
package com.aerospace;

import com.aerospace.flightengine.FlightSimulation;
import com.aerospace.flightengine.RK4Integrator;
import com.aerospace.io.SimulationLogger;
import com.aerospace.vehicle.RocketPresets;
import com.aerospace.vehicle.RocketStage;
import java.util.List;

/**
 * Main entry point for the Aerospace Trajectory Calculator.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: java com.aerospace.Main <rocket-preset> [mission-type]");
            System.out.println("Available rockets: " + String.join(", ", RocketPresets.availableRockets()));
            return;
        }
        String rocketPreset = args[0];
        String missionType = args.length > 1 ? args[1] : "standard";
        List<RocketStage> stages = RocketPresets.getRocket(rocketPreset);
        if (stages == null) {
            System.err.println("Unknown rocket preset: " + rocketPreset);
            System.exit(1);
        }
        System.out.println("\nRunning simulation for: " + rocketPreset + " | Mission: " + missionType);
        SimulationLogger logger = new SimulationLogger(rocketPreset + "_" + missionType);
        // Create simulation with RK4 integrator
        FlightSimulation simulation = new FlightSimulation(
                stages,
                new RK4Integrator(),
                0.01, // time step (seconds)
                3600, // max time (seconds)
                logger
        );
        // Launch at 80 degrees, 50 m/s (default)
        simulation.run(80, 50);
        // Export results
        logger.exportCSV("flight_log.csv");
        System.out.println(logger.generateReport());
    }
}

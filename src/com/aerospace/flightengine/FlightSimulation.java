package com.aerospace.flightengine;

import com.aerospace.atm.AtmosphericModel;
import com.aerospace.atm.DragCalculator;
import com.aerospace.physics.GravityModel;
import com.aerospace.physics.PhysicsVector;
import com.aerospace.vehicle.RocketStage;
import com.aerospace.io.SimulationLogger;
import java.util.List;

/**
 * Core flight simulation using a NumericalIntegrator (RK4 or Euler).
 */
public class FlightSimulation {
    private final List<RocketStage> stages;
    private final NumericalIntegrator integrator;
    private final double dt;
    private final double maxTime;
    private final SimulationLogger logger;

    public FlightSimulation(List<RocketStage> stages, NumericalIntegrator integrator, double dt, double maxTime, SimulationLogger logger) {
        this.stages = stages;
        this.integrator = integrator;
        this.dt = dt;
        this.maxTime = maxTime;
        this.logger = logger;
    }

    /**
     * Runs the simulation from launch to termination.
     * @param launchAngleDeg launch angle relative to local horizontal (degrees)
     * @param launchSpeed initial speed (m/s)
     */
    public void run(double launchAngleDeg, double launchSpeed) {
        // Initial state – start at Earth's surface at the north pole (x=0, y=R)
        double earthR = GravityModel.earthRadius();
        PhysicsVector position = new PhysicsVector(0, earthR);
        double angleRad = Math.toRadians(launchAngleDeg);
        PhysicsVector velocity = new PhysicsVector(launchSpeed * Math.cos(angleRad), launchSpeed * Math.sin(angleRad));
        double time = 0.0;
        int stageIndex = 0;
        RocketStage currentStage = stages.get(stageIndex);
        double mass = computeTotalMass(stageIndex);

        while (time < maxTime) {
            double altitude = position.magnitude() - earthR;
            if (altitude < 0 && time > 5) {
                // Impact
                logger.recordEvent(time, "Impact", "Vehicle hit the ground", altitude, velocity.magnitude());
                break;
            }

            // Record telemetry before step
            logger.recordTelemetry(time, position, velocity, mass, currentStage, altitude);

            // Perform integration step
            NumericalIntegrator.IntegrationResult result = integrator.step(position, velocity, mass, currentStage, dt);
            position = result.position;
            velocity = result.velocity;
            mass = result.mass;
            time += dt;

            // Update fuel consumption & stage transitions
            if (currentStage != null) {
                double mdot = currentStage.massFlowRate(); // kg/s
                double fuelConsumed = mdot * dt;
                double newFuel = Math.max(0, currentStage.fuelMass - fuelConsumed);
                // Update stage fuel (we cannot modify original immutable object, so create new stage copy)
                currentStage = new RocketStage(
                        currentStage.name,
                        currentStage.dryMass,
                        newFuel,
                        currentStage.thrust,
                        currentStage.burnTime,
                        currentStage.isp,
                        currentStage.area,
                        currentStage.cd
                );
                stages.set(stageIndex, currentStage);
                if (newFuel <= 0) {
                    logger.recordEvent(time, "StageSep", "Stage " + currentStage.name + " separation", altitude, velocity.magnitude());
                    stageIndex++;
                    if (stageIndex < stages.size()) {
                        currentStage = stages.get(stageIndex);
                    } else {
                        currentStage = null; // coasting
                    }
                }
            }
        }
        // Final telemetry record
        logger.recordTelemetry(time, position, velocity, mass, null, position.magnitude() - earthR);
    }

    private double computeTotalMass(int upToStageIndex) {
        double total = 0.0;
        for (int i = upToStageIndex; i < stages.size(); i++) {
            RocketStage s = stages.get(i);
            total += s.dryMass + s.fuelMass;
        }
        return total;
    }
}

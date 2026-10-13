package com.aerospace.flightengine;

import com.aerospace.physics.PhysicsVector;
import com.aerospace.vehicle.RocketStage;

/**
 * Interface for numerical integration strategies.
 */
public interface NumericalIntegrator {
    /**
     * Integrate one time step.
     *
     * @param position current position vector (meters)
     * @param velocity current velocity vector (m/s)
     * @param mass current total mass (kg)
     * @param stage current active rocket stage (may be null for coasting)
     * @param dt time step (seconds)
     * @return an {@link IntegrationResult} containing updated state.
     */
    IntegrationResult step(PhysicsVector position, PhysicsVector velocity, double mass, RocketStage stage, double dt);

    /** Simple holder for the result of an integration step. */
    class IntegrationResult {
        public final PhysicsVector position;
        public final PhysicsVector velocity;
        public final double mass;
        public IntegrationResult(PhysicsVector position, PhysicsVector velocity, double mass) {
            this.position = position;
            this.velocity = velocity;
            this.mass = mass;
        }
    }
}

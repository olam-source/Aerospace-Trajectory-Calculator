package com.aerospace.flightengine;

import com.aerospace.physics.PhysicsVector;
import com.aerospace.vehicle.RocketStage;
import com.aerospace.physics.GravityModel;
import com.aerospace.atm.DragCalculator;

/**
 * Runge‑Kutta 4th order integrator for the flight simulation.
 */
public class RK4Integrator implements NumericalIntegrator {

    @Override
    public IntegrationResult step(PhysicsVector position, PhysicsVector velocity, double mass, RocketStage stage, double dt) {
        // Helper lambda to compute acceleration based on current state
        AccelerationProvider accProvider = (pos, vel, m, stg) -> computeAcceleration(pos, vel, m, stg);

        // k1
        PhysicsVector a1 = accProvider.acceleration(position, velocity, mass, stage);
        PhysicsVector v1 = velocity;
        PhysicsVector p1 = position;

        // k2
        PhysicsVector p2 = position.add(v1.scale(dt / 2.0));
        PhysicsVector v2 = velocity.add(a1.scale(dt / 2.0));
        PhysicsVector a2 = accProvider.acceleration(p2, v2, mass, stage);

        // k3
        PhysicsVector p3 = position.add(v2.scale(dt / 2.0));
        PhysicsVector v3 = velocity.add(a2.scale(dt / 2.0));
        PhysicsVector a3 = accProvider.acceleration(p3, v3, mass, stage);

        // k4
        PhysicsVector p4 = position.add(v3.scale(dt));
        PhysicsVector v4 = velocity.add(a3.scale(dt));
        PhysicsVector a4 = accProvider.acceleration(p4, v4, mass, stage);

        // Combine increments
        PhysicsVector newPos = position.add(
                v1.scale(dt / 6.0)
                .add(v2.scale(dt / 3.0))
                .add(v3.scale(dt / 3.0))
                .add(v4.scale(dt / 6.0))
        );

        PhysicsVector newVel = velocity.add(
                a1.scale(dt / 6.0)
                .add(a2.scale(dt / 3.0))
                .add(a3.scale(dt / 3.0))
                .add(a4.scale(dt / 6.0))
        );

        // Mass change (fuel consumption) – assume constant mass flow if stage active
        double newMass = mass;
        if (stage != null) {
            double mdot = stage.massFlowRate(); // kg/s
            newMass = Math.max(stage.dryMass, mass - mdot * dt);
        }
        return new IntegrationResult(newPos, newVel, newMass);
    }

    /** Functional interface for acceleration calculation. */
    @FunctionalInterface
    private interface AccelerationProvider {
        PhysicsVector acceleration(PhysicsVector pos, PhysicsVector vel, double mass, RocketStage stage);
    }

    /** Compute net acceleration vector (gravity + thrust + drag) */
    private PhysicsVector computeAcceleration(PhysicsVector pos, PhysicsVector vel, double mass, RocketStage stage) {
        // Gravity
        PhysicsVector g = GravityModel.acceleration(pos);
        // Thrust (aligned with velocity direction if velocity > 0)
        PhysicsVector thrustVec = new PhysicsVector(0, 0);
        if (stage != null && stage.fuelMass > 0) {
            double speed = vel.magnitude();
            if (speed > 0) {
                double ux = vel.x / speed;
                double uy = vel.y / speed;
                thrustVec = new PhysicsVector(ux * stage.thrust, uy * stage.thrust);
            } else {
                // If velocity zero, thrust along +x direction by default
                thrustVec = new PhysicsVector(stage.thrust, 0);
            }
        }
        // Drag (Mach‑dependent)
        double altitude = pos.magnitude() - GravityModel.earthRadius();
        PhysicsVector dragVec = DragCalculator.computeDrag(vel, stage, altitude);
        // Net force = thrust + drag + gravity (gravity already negative)
        PhysicsVector netForce = thrustVec.add(dragVec).add(g.scale(mass)); // g is acceleration, multiply by mass to get force
        // Acceleration = netForce / mass
        return netForce.scale(1.0 / mass);
    }
}

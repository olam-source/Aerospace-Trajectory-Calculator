package com.aerospace.flightengine;

import com.aerospace.physics.PhysicsVector;
import com.aerospace.vehicle.RocketStage;

/**
 * Simple Euler integrator for comparison purposes.
 */
public class EulerIntegrator implements NumericalIntegrator {
    @Override
    public IntegrationResult step(PhysicsVector position, PhysicsVector velocity, double mass, RocketStage stage, double dt) {
        // Compute acceleration
        PhysicsVector acceleration = computeAcceleration(position, velocity, mass, stage);
        // New velocity: v + a*dt
        PhysicsVector newVelocity = velocity.add(acceleration.scale(dt));
        // New position: x + v*dt
        PhysicsVector newPosition = position.add(velocity.scale(dt));
        // New mass (fuel consumption)
        double newMass = mass;
        if (stage != null) {
            double mdot = stage.massFlowRate();
            newMass = Math.max(stage.dryMass, mass - mdot * dt);
        }
        return new IntegrationResult(newPosition, newVelocity, newMass);
    }

    private PhysicsVector computeAcceleration(PhysicsVector pos, PhysicsVector vel, double mass, RocketStage stage) {
        // Gravity
        PhysicsVector g = com.aerospace.physics.GravityModel.acceleration(pos);
        // Thrust (aligned with velocity direction)
        PhysicsVector thrust = new PhysicsVector(0, 0);
        if (stage != null && stage.fuelMass > 0) {
            double speed = vel.magnitude();
            if (speed > 0) {
                double ux = vel.x / speed;
                double uy = vel.y / speed;
                thrust = new PhysicsVector(ux * stage.thrust, uy * stage.thrust);
            } else {
                // Default to +x if velocity zero
                thrust = new PhysicsVector(stage.thrust, 0);
            }
        }
        // Drag
        double altitude = pos.magnitude() - com.aerospace.physics.GravityModel.earthRadius();
        PhysicsVector drag = com.aerospace.atm.DragCalculator.computeDrag(vel, stage, altitude);
        // Net acceleration: (thrust + drag + gravity) / mass
        PhysicsVector netForce = thrust.add(drag).add(g.scale(mass)); // g is acceleration, so F=ma
        return netForce.scale(1.0 / mass);
    }
}

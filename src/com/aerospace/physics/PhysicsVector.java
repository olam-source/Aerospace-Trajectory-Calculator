package com.aerospace.physics;

/**
 * Simple 2‑D vector utility used by the simulation engine.
 */
public class PhysicsVector {
    public double x;
    public double y;

    public PhysicsVector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public PhysicsVector add(PhysicsVector other) {
        return new PhysicsVector(this.x + other.x, this.y + other.y);
    }

    public PhysicsVector scale(double scalar) {
        return new PhysicsVector(this.x * scalar, this.y * scalar);
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }
}

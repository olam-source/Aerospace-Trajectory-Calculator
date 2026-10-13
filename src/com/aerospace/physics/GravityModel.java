package com.aerospace.physics;

/**
 * Simple Newtonian gravity model for Earth.
 */
public class GravityModel {
    private static final double GM = 3.986004418e14; // Earth's gravitational parameter (m^3/s^2)
    private static final double EARTH_RADIUS = 6371000.0; // meters

    /**
     * Returns the gravitational acceleration vector (negative towards Earth's centre).
     * @param position current position vector (x,y) in meters relative to Earth's centre.
     * @return gravity vector (gx, gy) in m/s^2.
     */
    public static PhysicsVector acceleration(PhysicsVector position) {
        double r = position.magnitude();
        double gMag = GM / (r * r);
        // Direction towards origin (negative of position unit vector)
        double factor = -gMag / r;
        return new PhysicsVector(position.x * factor, position.y * factor);
    }

    /**
     * Returns Earth's radius constant.
     */
    public static double earthRadius() {
        return EARTH_RADIUS;
    }
}

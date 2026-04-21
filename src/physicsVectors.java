package src;

public class PhysicsVector {
    public double x, y;
    public PhysicsVector(double x, double y) { this.x = x; this.y = y; }
    public double getMagnitude() { return Math.sqrt(x * x + y * y); }
}

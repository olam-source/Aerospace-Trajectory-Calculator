package src;

public class AtmosphericModel {
    public static final double G = 9.80665;
    public static final double R = 287.05;

    public static double getAirDensity(double h) {
        if (h > 25000) return 0.0; 
        double T = (h < 11000) ? 288.15 - 0.0065 * h : 216.65;
        double P = (h < 11000) ? 101325 * Math.pow(T / 288.15, 5.2558) : 22632 * Math.exp(-G * (h - 11000) / (R * T));
        return P / (R * T);
    }
}

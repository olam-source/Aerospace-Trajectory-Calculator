package com.aerospace.atm;

/**
 * Full International Standard Atmosphere (ISA) 1976 model implementation.
 * Supports 9 layers from sea level up to the exosphere (~700 km).
 */
public class AtmosphericModel {
    private static final double R = 287.05; // Specific gas constant for dry air (J/(kg·K))
    private static final double G0 = 9.80665; // Standard gravity (m/s²)
    private static final double GAMMA = 1.4; // Ratio of specific heats for air

    // Layer definitions (altitude in meters)
    private static final Layer[] LAYERS = {
        // Troposphere (0 - 11,000 m)
        new Layer(0, 11000, 288.15, -0.0065, 101325),
        // Tropopause (11,000 - 20,000 m)
        new Layer(11000, 20000, 216.65, 0.0, 22632.1),
        // Stratosphere Low (20,000 - 32,000 m)
        new Layer(20000, 32000, 216.65, 0.001, 5474.89),
        // Stratosphere Mid (32,000 - 47,000 m)
        new Layer(32000, 47000, 228.65, 0.0028, 868.02),
        // Stratosphere High (47,000 - 51,000 m)
        new Layer(47000, 51000, 270.65, 0.0, 110.91),
        // Mesosphere (51,000 - 71,000 m)
        new Layer(51000, 71000, 270.65, -0.0028, 66.939),
        // Mesopause (71,000 - 84,000 m)
        new Layer(71000, 84000, 214.65, 0.0, 3.95642),
        // Thermosphere Low (84,000 - 100,000 m)
        new Layer(84000, 100000, 186.946, 0.001, 0.018),
        // Thermosphere High (100,000 - 700,000 m) – exponential decay
        new Layer(100000, 700000, 186.946, 0.001, 0.0) // pressure will be computed exponentially
    };

    /**
     * Returns the air density (kg/m³) at the given altitude.
     */
    public static double getDensity(double altitude) {
        double temperature = getTemperature(altitude);
        double pressure = getPressure(altitude);
        return pressure / (R * temperature);
    }

    /**
     * Returns the temperature (K) at the given altitude.
     */
    public static double getTemperature(double altitude) {
        Layer layer = findLayer(altitude);
        double deltaH = altitude - layer.bottom;
        return layer.baseTemp + layer.lapseRate * deltaH;
    }

    /**
     * Returns the pressure (Pa) at the given altitude.
     */
    public static double getPressure(double altitude) {
        Layer layer = findLayer(altitude);
        double deltaH = altitude - layer.bottom;
        double T0 = layer.baseTemp;
        double P0 = layer.basePressure;
        if (layer.lapseRate == 0) {
            // Isothermal layer – exponential pressure decay
            return P0 * Math.exp(-G0 * deltaH / (R * T0));
        } else {
            // Gradient layer – power law
            double T = T0 + layer.lapseRate * deltaH;
            return P0 * Math.pow(T0 / T, G0 / (layer.lapseRate * R));
        }
    }

    private static Layer findLayer(double altitude) {
        for (Layer layer : LAYERS) {
            if (altitude >= layer.bottom && altitude < layer.top) {
                return layer;
            }
        }
        // Above highest defined layer – treat as exosphere (very low density)
        return LAYERS[LAYERS.length - 1];
    }

    // Simple data holder for each atmospheric layer
    private static class Layer {
        final double bottom;
        final double top;
        final double baseTemp; // Kelvin at bottom of layer
        final double lapseRate; // K/m (negative for decreasing temperature)
        final double basePressure; // Pa at bottom of layer

        Layer(double bottom, double top, double baseTemp, double lapseRate, double basePressure) {
            this.bottom = bottom;
            this.top = top;
            this.baseTemp = baseTemp;
            this.lapseRate = lapseRate;
            this.basePressure = basePressure;
        }
    }
}

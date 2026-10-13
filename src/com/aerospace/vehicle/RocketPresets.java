package com.aerospace.vehicle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Preset rocket configurations.
 */
public class RocketPresets {
    private static final List<String> AVAILABLE_ROCKETS = Arrays.asList(
            "saturn-v", "falcon-9", "space-shuttle", "starship"
    );

    public static List<String> availableRockets() {
        return AVAILABLE_ROCKETS;
    }

    public static List<RocketStage> getRocket(String presetName) {
        switch (presetName.toLowerCase()) {
            case "saturn-v": return getSaturnV();
            case "falcon-9": return getFalcon9();
            case "space-shuttle": return getSpaceShuttle();
            case "starship": return getStarship();
            default: return null;
        }
    }

    // Saturn V (Apollo missions)
    private static List<RocketStage> getSaturnV() {
        List<RocketStage> stages = new ArrayList<>();
        // S-IC First Stage
        stages.add(new RocketStage(
                "S-IC First Stage",
                280000, 2300000, 35000000, 168,
                263, 10.56, 0.4
        ));
        // S-II Second Stage
        stages.add(new RocketStage(
                "S-II Second Stage",
                43200, 408000, 5600000, 360,
                421, 1.3, 0.45
        ));
        // S-IVB Third Stage
        stages.add(new RocketStage(
                "S-IVB Third Stage",
                11700, 105000, 890000, 380,
                421, 1.3, 0.45
        ));
        return stages;
    }

    // Falcon 9 Full Thrust
    private static List<RocketStage> getFalcon9() {
        List<RocketStage> stages = new ArrayList<>();
        stages.add(new RocketStage(
                "Merlin 1D First Stage",
                24136, 395700, 7607000, 162,
                311, 2.5, 0.5
        ));
        return stages;
    }

    // Space Shuttle Main Engine + SRBs
    private static List<RocketStage> getSpaceShuttle() {
        List<RocketStage> stages = new ArrayList<>();
        // Main Engine (SSME)
        stages.add(new RocketStage(
                "SSME Main Engine",
                3557, 163600, 4189000, 480,
                452, 1.3, 0.4
        ));
        // Solid Rocket Boosters (SRB) – modeled as single stage
        stages.add(new RocketStage(
                "SRB Pair",
                590000, 1298000, 14800000, 120,
                269.5, 9.14, 0.4
        ));
        return stages;
    }

    // SpaceX Starship
    private static List<RocketStage> getStarship() {
        List<RocketStage> stages = new ArrayList<>();
        stages.add(new RocketStage(
                "Raptor First Stage",
                120000, 3500000, 15000000, 165,
                330, 13.0, 0.45
        ));
        stages.add(new RocketStage(
                "Raptor Second Stage",
                100000, 1200000, 1500000, 350,
                350, 6.0, 0.4
        ));
        return stages;
    }
}

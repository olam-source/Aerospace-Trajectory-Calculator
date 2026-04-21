# High-Fidelity Aerospace Trajectory Framework (HAT-F) v6.0 🚀

## Project Overview
The **High-Fidelity Aerospace Trajectory Framework (HAT-F)** is a Java-based computational engine designed to simulate the ascent profiles of multistage launch vehicles. By moving beyond idealized vacuum physics, this framework implements real-world variables including atmospheric density gradients, variable mass dynamics (Tsiolkovsky applications), and multi-phase flight sequencing.

This project represents a multi-month development cycle focused on bridging the gap between high-school kinematics and undergraduate-level aerospace simulation.

---

## Technical Specifications & Features

### 1. Numerical Integration Engine
The core of the simulation utilizes the **Euler Method** for discrete-time integration. By calculating state vectors at high-resolution intervals ($dt = 0.05s$), the engine accounts for non-linear changes in acceleration that closed-form algebraic equations cannot capture.

### 2. Multi-Stage Vehicle Dynamics
The framework implements a modular architecture for launch vehicles, allowing for:
* **Variable Mass Modeling:** Real-time calculation of $F=ma$ as fuel mass is depleted based on engine mass-flow rates.
* **Stage Separation Logic:** Automated "Jettison" events (MECO/SECO) where dead-weight (dry mass) is removed from the system to recalculate instantaneous acceleration spikes.

### 3. ISA Atmospheric Modeling (1976 Standard)
Unlike primitive simulators that use a constant air density, HAT-F utilizes the **International Standard Atmosphere (ISA)** model. It dynamically calculates air density ($\rho$) across multiple layers:
* **Troposphere:** Temperature lapse rate modeling up to 11km.
* **Lower/Upper Stratosphere:** Isothermal and pressure-gradient modeling up to 25km+.
* **Drag Equation:** $F_d = \frac{1}{2} \rho v^2 C_d A$ is recalculated every $0.05s$ as altitude and velocity change.

### 4. Telemetry & Data Analysis
The system generates high-fidelity telemetry logs exported in `.csv` format. Logged parameters include:
* **Time-stamped State Vectors** (Position, Velocity, Acceleration).
* **Instantaneous Vehicle Mass.**
* **Flight Event Markers** (Liftoff, Max-Q, MECO, SECO, Impact).

---

## Development Roadmap & Milestones

* **Phase I: Research & Environmental Modeling**
    * Study of the 1976 International Standard Atmosphere.
    * Implementation of the barometric formula and gas constant applications in Java.
* **Phase II: Vector Kinematics Engine**
    * Development of a custom `PhysicsVector` class to handle 2D force resolution.
    * Transition from algebraic formulas to iterative numerical integration.
* **Phase III: Structural & Multi-Stage Architecture**
    * Designing the `RocketStage` class to handle variable mass dynamics.
    * Implementation of the flight state machine to manage engine burn-out and stage separation.
* **Phase IV: UI & Data Export**
    * Developing the CLI telemetry table and robust CSV export functionality.

---

## How to Run
1. Ensure you have **Java JDK 8 or higher** installed.
2. Compile the source: `javac AerospaceSimulationFramework.java`
3. Execute the binary: `java AerospaceSimulationFramework`
4. Input your mission parameters or use the pre-configured Booster/Sustainer profiles.
5. Open the generated `MultiStage_FlightLog.csv` in Excel or Python (Matplotlib) for trajectory visualization.

---

## Author
**Daniel Olamoyegun**
*Aspiring Aerospace Engineer | Computational Physics Enthusiast*
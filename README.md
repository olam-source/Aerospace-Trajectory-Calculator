# High-Fidelity Aerospace Trajectory Framework (HAT-F) v6.0

## 📂 Project Structure
* **[Live Mission Control](https://yourusername.github.io/your-repo/web/index.html)**: Interactive Web Dashboard.
* **[/src](./src)**: Modular Java Backend (Multistage Physics Engine).
* **[/web](./web)**: Frontend Visualization (HTML5/JavaScript).

## 🚀 Overview
HAT-F is a dual-system aerospace simulator. It combines a rigorous **Java backend** for numerical integration with a **JavaScript frontend** for real-time telemetry visualization. 

### Core Features
- **ISA 1976 Atmosphere Model:** Dynamic air density calculations based on altitude.
- **Numerical Integration:** Uses the Euler Method ($dt=0.05s$) to solve for non-constant acceleration.
- **Variable Mass Dynamics:** Simulates fuel depletion and Stage-1/Stage-2 separation.

## 🛠️ Defense / Q&A
**Q: Why separate the Java files?** A: To follow the **Single Responsibility Principle**. `AtmosphericModel` handles the environment, `RocketStage` handles the hardware, and `FlightEngine` handles the integration.

**Q: How do the Web and Java versions relate?** A: The Java version is the "Scientific Engine" used for generating raw CSV data. The Web version is the "Operational Interface" used for visual mission demonstrations.

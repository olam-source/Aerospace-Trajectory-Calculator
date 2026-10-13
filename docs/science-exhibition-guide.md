# Science Exhibition Guide — Aerospace Trajectory Calculator v7.0

## STEM Lesson Plans for Interactive Learning

---

## Table of Contents
1. [Orbital Mechanics — How Gravity Creates Orbits](#orbital-mechanics--how-gravity-creates-orbits)
2. [Rocket Propulsion — Thrust, Isp, and Mass Ratio](#rocket-propulsion--thrust-isp-and-mass-ratio)
3. [Atmospheric Physics — ISA Model, Drag, and Max-Q](#atmospheric-physics--isa-model-drag-and-max-q)
4. [Numerical Integration — Euler vs. RK4 Accuracy](#numerical-integration--euler-vs-rk4-accuracy)

---

## 1. Orbital Mechanics — How Gravity Creates Orbits

### Learning Objectives
- Understand how gravitational force creates circular and elliptical orbits
- Calculate orbital velocity, period, and energy for any orbit
- Explain the relationship between altitude and orbital speed
- Differentiate between circular, elliptical, parabolic, and hyperbolic trajectories

### Key Concepts & Formulas

#### Newton's Law of Universal Gravitation
**F = G·(m₁·m₂)/r²**

Where:
- F = gravitational force (N)
- G = gravitational constant = 6.674×10⁻¹¹ N·m²/kg²
- m₁, m₂ = two masses (kg)
- r = distance between centers (m)

#### Orbital Velocity for Circular Orbit
**v = √(μ/r)**

Where:
- v = orbital velocity (m/s)
- μ = gravitational parameter = GM = 3.986×10¹⁴ m³/s² (Earth)
- r = distance from Earth's center (m) = altitude + Earth radius (6,371,000 m)

#### Orbital Period (Kepler's Third Law)
**T = 2π√(r³/μ)**

#### Specific Orbital Energy
**E = v²/2 - μ/r**

- E < 0 → Elliptical orbit (bound)
- E = 0 → Parabolic escape trajectory
- E > 0 → Hyperbolic escape trajectory

#### Apoapsis and Periapsis
For an elliptical orbit with semi-major axis a and eccentricity e:
- **Apoapsis** (far point): rₐ = a(1 + e)
- **Periapsis** (near point): rₚ = a(1 - e)

#### Eccentricity
- e = 0 → Circular orbit
- 0 < e < 1 → Elliptical orbit
- e = 1 → Parabolic trajectory
- e > 1 → Hyperbolic trajectory

#### Hohmann Transfer (Minimum-Energy Orbital Transfer)
Transfer from circular orbit radius r₁ to r₂ (where r₂ > r₁):

**Δv₁ = √(μ/r₁) · (√(2r₂/(r₁+r₂)) - 1)**

**Δv₂ = √(μ/r₂) · (1 - √(2r₁/(r₁+r₂)))**

**Total Δv = Δv₁ + Δv₂**

### Classroom Activities

1. **Orbit Simulator Exploration**
   - Have students use the web simulator to achieve circular orbits at different altitudes
   - Measure how orbital velocity decreases as altitude increases
   - Record the orbital period and compare with T = 2π√(r³/μ)

2. **Eccentricity Workshop**
   - Adjust launch angle and velocity to create elliptical orbits
   - Measure apoapsis and periapsis, calculate eccentricity
   - Identify launch conditions that produce circular vs. elliptical vs. escape trajectories

3. **Hohmann Transfer Mission**
   - Plan a transfer from LEO ( ~300 km ) to GEO ( ~35,786 km altitude)
   - Calculate the required Δv for both transfer orbits
   - Use the simulator to verify the transfer works

### Discussion Questions
- Why do satellites in lower orbits move faster than those in higher orbits?
- What's the minimum velocity needed to escape Earth's gravity?
- How does the simulator's gravity model compare to real-world orbital mechanics?

---

## 2. Rocket Propulsion — Thrust, Isp, and Mass Ratio

### Learning Objectives
- Understand how rocket thrust produces acceleration
- Calculate specific impulse and its relationship to fuel efficiency
- Derive the Tsiolkovsky rocket equation
- Analyze multi-stage rocket performance

### Key Concepts & Formulas

#### Thrust Force
**F_thrust = ṁ · v_ex + (p_ex - p_amb) · A_ex**

Where:
- ṁ = mass flow rate (kg/s)
- v_ex = exhaust velocity (m/s)
- p_ex = exhaust pressure (Pa)
- p_amb = ambient pressure (Pa)
- A_ex = exhaust area (m²)

For ideal vacuum conditions: **F_thrust ≈ ṁ · v_ex**

#### Specific Impulse (Isp)
**Isp = F_thrust / (ṁ · g₀)**

Where:
- Isp = specific impulse (seconds)
- F_thrust = thrust force (N)
- ṁ = propellant mass flow rate (kg/s)
- g₀ = standard gravity = 9.80665 m/s²

**Higher Isp = more fuel-efficient engine**

#### Exhaust Velocity Relation
**v_ex = Isp · g₀**

#### Tsiolkovsky Rocket Equation
**Δv = Isp · g₀ · ln(m₀/mf)**

Where:
- Δv = change in velocity (m/s)
- Isp = specific impulse (s)
- g₀ = 9.80665 m/s² (standard gravity)
- m₀ = initial total mass (payload + structure + fuel) (kg)
- mf = final mass (payload + structure) (kg)
- ln = natural logarithm

**Key Insight**: Δv grows logarithmically with the mass ratio m₀/mf — doubling the fuel only adds a fixed Δv

#### Stage Efficiency
For multi-stage rockets:
- **Overall Δv = Δv₁ + Δv₂ + ... + Δvₙ**
- Each stage sheds dead weight (dry mass) when finished
- **Payload ratio** improves with each discarded stage

### Classroom Activities

1. **Rocket Equation Workshop**
   - Given an engine with Isp = 311 s (Falcon 9 Merlin), calculate Δv for:
     - Single stage: m₀ = 30,000 kg, mf = 5,000 kg
     - Two-stage optimization
   - Verify with the simulator's flight reports

2. **Isp Comparison Table**
   - Compare the 4 rocket presets:
     - Saturn V: Isp ~350 s (S-IC), 360 s (S-II), 380 s (S-IVB)
     - Falcon 9: Isp = 311 s (Merlin 1D)
     - Space Shuttle: Isp = 480 s (SSME vacuum), 269 s (SRB sea level)
     - Starship: Isp = 330 s (Raptor first), 350 s (Raptor second)
   - Discuss why vacuum Isp > sea-level Isp

3. **Multi-Stage Optimization**
   - Given total fuel mass, find the optimal number of stages
   - Compare single-stage vs. two-stage vs. three-stage performance
   - Use Saturn V as real-world example of 3-stage efficiency

### Discussion Questions
- Why do upper stages have higher Isp than lower stages?
- How does the rocket equation explain why rockets have multiple stages?
- What trade-offs exist between thrust and Isp?

---

## 3. Atmospheric Physics — ISA Model, Drag, and Max-Q

### Learning Objectives
- Understand the International Standard Atmosphere 1976 model
- Calculate atmospheric density, temperature, and pressure at any altitude
- Determine dynamic pressure (Max-Q) and its significance
- Explain how drag affects trajectory

### Key Concepts & Formulas

#### ISA 1976 Atmosphere — 9 Layers

| Layer | Altitude Range | Temperature Gradient | Key Properties |
|-------|---------------|----------------------|----------------|
| **Troposphere** | 0 → 11 km | -6.5 K/km | Weather layer; temperature at surface: 288.15 K; pressure: 101,325 Pa |
| **Lower Stratosphere** | 11 → 20 km | +1.0 K/km | Ozone absorption; temperature at 20 km: 216.65 K |
| **Upper Stratosphere** | 20 → 32 km | +2.8 K/km | Temperature at 32 km: 228.65 K |
| **Mesosphere** | 32 → 47 km | -2.8 K/km | Coldest layer; temperature at 47 km: 228.65 K (min) |
| **Lower Thermosphere** | 47 → 51 km | +2.8 K/km | Transition region |
| **Intermediate Thermosphere** | 51 → 72 km | +2.0 K/km | Satellites begin experiencing atmospheric drag |
| **Upper Thermosphere** | 72 → 85 km | +2.0 K/km | Temperatures can reach 1500+ K (low density) |
| **Exosphere Transition** | 85 → 500 km | Variable | Particle mean free path exceeds scale height |
| **Exosphere** | 500+ km | → 0 K | Transition to space; hydrogen/helium dominant |

#### Temperature at Any Altitude
For the troposphere (0 ≤ h ≤ 11 km):
**T = T₀ - L · h**

Where:
- T = temperature at altitude h (K)
- T₀ = 288.15 K (sea level standard temperature)
- L = 6.5 K/km (temperature lapse rate)
- h = altitude above sea level (m)

For other layers, piecewise formulas apply with continuity at layer boundaries.

#### Pressure Calculation
For the troposphere:
**p = p₀ · (T/T₀)^(g₀·M/(R·L))**

Where:
- p = pressure at altitude h (Pa)
- p₀ = 101,325 Pa (sea level standard pressure)
- M = 0.0289644 kg/mol (molar mass of dry air)
- R = 8.31447 J/(mol·K) (universal gas constant)
- g₀ = 9.80665 m/s²

For isothermal layers (constant temperature):
**p = p_ref · exp(-g₀·M·(h - h_ref)/(R·T))**

#### Atmospheric Density
**ρ = p / (R_specific · T)**

Where:
- ρ = density (kg/m³)
- p = pressure (Pa)
- R_specific = 287.05 J/(kg·K) for dry air
- T = temperature (K)

#### Speed of Sound
**a = √(γ · R · T)**

Where:
- a = speed of sound (m/s)
- γ = 1.4 (ratio of specific heats for air)
- R = 287.05 J/(kg·K)
- T = temperature (K)

#### Mach Number
**M = v / a**

Where:
- M = Mach number (dimensionless)
- v = velocity (m/s)
- a = speed of sound at that altitude (m/s)

#### Dynamic Pressure (Max-Q)
**q = ½ · ρ · v²**

Where:
- q = dynamic pressure (Pa)
- ρ = atmospheric density (kg/m³)
- v = velocity (m/s)

**Max-Q** = maximum dynamic pressure during flight — critical design limit for vehicle structural integrity

#### Drag Force
**F_d = ½ · ρ · v² · C_d · A**

Where:
- F_d = drag force (N)
- ρ = density (kg/m³)
- v = velocity (m/s)
- C_d = drag coefficient (dimensionless, Mach-dependent)
- A = reference area (m²) — typically cross-sectional area

#### Drag Coefficient Regimes
- **Subsonic**: M < 0.8 → C_d ≈ constant (~0.45 for typical vehicles)
- **Transonic**: 0.8 ≤ M ≤ 1.2 → C_d increases sharply (shock formation)
- **Supersonic**: M > 1.2 → C_d gradually decreases to plateau (~0.9)

### Classroom Activities

1. **ISA Layer Exploration**
   - Use the simulator to launch at different angles and observe
   - Record altitude where drag significantly decreases (troposphere → stratosphere)
   - Note the "layer transitions" in the telemetry data

2. **Max-Q Analysis**
   - Launch any rocket preset and identify the time/altitude of Max-Q
   - Record the dynamic pressure value at Max-Q
   - Compare across different rockets: which has the highest Max-Q and why?
   - Discuss why rocket vehicles are designed to withstand specific Max-Q values

3. **Density Altitude Calculation**
   - Given a launch altitude, calculate the air density
   - Compare sea-level vs. mountaintop launch performance
   - Use the simulator's CSV export to analyze real data

4. **Mach Regime Exploration**
   - Accelerate to supersonic speeds in the simulator
   - Observe the drag coefficient change (transonic peak)
   - Discuss why the transonic regime is the most aerodynamically challenging

### Discussion Questions
- Why does air density decrease so rapidly with altitude?
- What is Max-Q and why is it a critical design constraint?
- How does the simulator's Mach-dependent drag differ from a constant Cd model?

---

## 4. Numerical Integration — Euler vs. RK4 Accuracy

### Learning Objectives
- Understand the difference between Euler and Runge-Kutta 4th order (RK4) integration
- Experience numerically how error accumulates over time
- Compare trajectory accuracy for both methods
- Appreciate why RK4 is preferred for scientific simulations

### Key Concepts & Formulas

#### Euler Method (Legacy, Lower Accuracy)
**yₙ₊₁ = yₙ + h · f(tₙ, yₙ)**

Where:
- y = state vector (position, velocity)
- h = time step (dt)
- f = derivative function (acceleration given state)

**First-order method**: Error ∝ O(h) — error proportional to step size
**Simple to implement** but accumulates significant error over long integrations

#### Runge-Kutta 4th Order (RK4, Higher Accuracy)
**k₁ = h · f(tₙ, yₙ)**  
**k₂ = h · f(tₙ + h/2, yₙ + k₁/2)**  
**k₃ = h · f(tₙ + h/2, yₙ + k₂/2)**  
**k₄ = h · f(tₙ + h, yₙ + k₃)**  
**yₙ₊₁ = yₙ + (k₁ + 2k₂ + 2k₃ + k₄) / 6**

**Fourth-order method**: Error ∝ O(h⁴) — error decreases dramatically with smaller h
**Much more accurate** but requires 4 function evaluations per step

#### Local vs. Global Error
- **Local truncation error**: Error per single step
- **Global error**: Cumulative error after many steps

For Euler: Global error grows linearly with simulation time
For RK4: Global error grows much more slowly; much more stable over long integrations

#### Energy Conservation Error
In orbital mechanics, the key metric is energy error:
- **E = v²/2 - μ/r** (specific orbital energy)
- For a correct orbit, E should remain constant
- Euler: |ΔE| grows over time → orbit spirals inward/outward
- RK4: |ΔE| stays bounded; orbit remains stable for many periods

### Error Comparison Table (typical, dt = 0.01 s)

| Integration Method | Step Size (h) | Global Position Error (after 60 s) | Orbital Energy Error | Computational Cost |
|-------------------|---------------|-------------------------------------|---------------------|-------------------|
| **Euler** | 0.01 s | ~100s meters | ~1% of orbital energy | 1 evaluation/step |
| **RK4** | 0.01 s | ~0.1 meters | ~10⁻⁶ of orbital energy | 4 evaluations/step |
| **RK4** | 0.1 s | ~10 meters | ~10⁻⁴ of orbital energy | 4 evaluations/step |
| **RK4** | 1.0 s | ~1 km | ~10⁻² of orbital energy | 4 evaluations/step |

### Classroom Activities

1. **Euler vs. RK4 Race**
   - Launch a rocket in the simulator with both integration methods (switchable via code)
   - Compare trajectories after same flight time
   - Observe how Euler drifts from the "correct" path while RK4 stays accurate

2. **Error Accumulation Over Time**
   - Run a long-duration simulation (e.g., 1000 s) with both methods
   - Export CSV data and plot position error vs. time
   - Fit the error growth rate and compare O(h) vs. O(h⁴)

3. **Energy Conservation Test**
   - Achieve circular orbit using both methods
   - Monitor specific orbital energy E = v²/2 - μ/r over many orbits
   - Euler: energy drifts → orbit decays or expands
   - RK4: energy remains constant → stable orbit

4. **Step Size Sensitivity**
   - Run RK4 with different step sizes: 1.0 s, 0.1 s, 0.01 s, 0.001 s
   - Observe how error decreases as h gets smaller
   - Verify the h⁴ relationship empirically

### Discussion Questions
- Why does Euler method accumulate error in orbital simulations?
- How many times more accurate is RK4 compared to Euler for the same step size?
- Why is the extra computational cost of RK4 worth it for scientific simulations?
- Can you think of situations where Euler might be preferred despite lower accuracy?

---

## Additional Educational Resources

### Glossary of Key Terms
- ** apoapsis**: Point in orbit farthest from central body
- ** periapsis**: Point in orbit closest to central body
- ** eccentricity**: Measure of how elliptical an orbit is (e = 0 is circular)
- ** specific impulse (Isp)**: Fuel efficiency metric (seconds); higher = more efficient
- ** dynamic pressure (q)**: q = ½ρv²; aerodynamic stress on vehicle
- ** Max-Q**: Maximum dynamic pressure during flight; structural design limit
- ** gravitational parameter (μ)**: GM; product of gravitational constant and body mass
- ** trajectory**: Path of a moving object through space
- ** stage separation**: Jettisoning empty fuel tanks to reduce dead mass

### Teaching Tips
1. **Start visual** — Use the simulator's orbit canvas to show concepts before deriving formulas
2. **Connect to real-world** — Mention actual missions (Apollo, Shuttle, Mars missions) that used these calculations
3. **Hands-on exploration** — Let students adjust parameters and see immediate results
4. **Error discussion** — The Euler vs. RK4 comparison is often an "aha!" moment for students
5. **Cross-reference** — Link orbital mechanics to rocket propulsion and atmospheric physics units

### Assessment Ideas
- Have students calculate the required launch velocity for a circular orbit at 300 km altitude, then verify with the simulator
- Ask students to identify the Max-Q point in a flight record and explain its significance
- Request a comparison of Isp values for the 4 rocket presets and what design choices cause the differences
- Ask students to run a long simulation with Euler and report the final position error after 10 minutes

### Alignment with Standards
- **NGSS HS-PS2-1**: Analyze data to support the claim that Newton's second law of motion describes the mathematical relationship among the net force on a macroscopic object, its mass, and its acceleration.
- **NGSS HS-ESS1-4**: Use mathematical or computational representations to predict the motion of orbiting objects in the solar system.
- **Common Core Math**: MP.4 (Model with mathematics), HSN-Q.A.1 (Use units to solve problems), HSN-Q.A.2 (Define appropriate quantities for the purpose of descriptive modeling).

---
*Science Exhibition Guide — Aerospace Trajectory Calculator v7.0*
*For STEM education and interactive learning*
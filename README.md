# Aerospace Trajectory Calculator v7.0 — Science Exhibition

## 🚀 Interactive Trajectory Simulator

A full-fledged aerospace trajectory calculator transformed into a science exhibition with equal priority on physics accuracy, UI/UX design, educational content, and data sharing.

## 🌐 Live Demo
**[Open the Interactive Simulator](https://olam-source.github.io/Aerospace-Trajectory-Calculator/web/index.html)**

## 📦 Project Structure
- **`/src/com/aerospace/`** — Java backend: RK4 integrator, ISA 1976 atmosphere model (9 layers), Mach-dependent drag, rocket vehicle models, simulation logger
- **`/docs/`** — HTML5/JavaScript frontend (`index.html`) and documentation content for GitHub Pages
- **`/README.md`** — This file: feature overview, quick start, mission parameters, orbital mechanics reference
- **`/docs/science-exhibition-guide.md`** — STEM lesson plans for orbital mechanics, propulsion, atmospheric physics, numerical integration
- **`/docs/api-reference.md`** — REST API endpoint reference and example commands

## ✨ Features (All Implemented)

### Physics Engine (Sprint 2)
- **RK4 Integration** — Runge-Kutta 4th order for numerical accuracy vs. legacy Euler method
- **ISA 1976 Atmosphere** — Full 9-layer model from troposphere through exosphere with temperature, pressure, and density calculations
- **Mach-Dependent Drag** — Piecewise subsonic/transonic/supersonic drag coefficient with transonic shock modeling
- **Specific Impulse (Isp)**, **Thrust-to-Weight Ratio**, **Delta-v** calculations

### Frontend & UI/UX (Sprint 1)
- **Glassmorphism Design** — CSS variables, backdrop-filter, dark mode support
- **5 Visualization Modes**:
  1. **Orbit Canvas** — Interactive trajectory with Earth rendering
  2. **Altitude-Time Chart** — Chart.js telemetry visualization
  3. **Energy Diagram** — Orbital energy (E = v²/2 - μ/r) plot
  4. **Globe Projection** — Latitude/longitude trajectory mapping
  5. **Velocity Vectors** — Decomposed velocity components
- **Full Mobile Responsiveness** — All modes stack correctly under 768px, touch gestures (pinch-to-zoom, swipe), optimized canvas rendering
- **4 Rocket Presets** — Saturn V (S-IC/S-II/S-IVB), Falcon 9 (Merlin 1D), Space Shuttle (SSME+SRBs), Starship (Raptor 1st+2nd) with realistic specifications

### Educational Content (Sprint 4)
- **"Show Science" Toggle** — Physics tooltips explaining:
  - Gravity equation: **F = GMm/r²**
  - Drag equation: **Fd = ½ρv²CdA**
  - Orbital energy: **E = v²/2 - μ/r**
  - Mach number, Specific impulse Isp
  - Euler vs. RK4 accuracy comparison
- **Comprehensive Lesson Plans** — STEM topics: orbital mechanics, rocket propulsion, atmospheric physics, numerical integration
- **Quick-Start Guide** — Mission parameters and how to use all features

### Data & Sharing (Sprint 3)
- **CSV Export** — Complete telemetry: time, position, velocity, mass, altitude, dynamic pressure, g-force, Mach, energy, stage index
- **JSON Mission Export** — Replay structure with all configuration
- **Replay System** — Timeline scrubber with event markers, play/pause/step/FF/rewind controls
- **Web Audio API** — Sound effects: countdown, liftoff, stage separation, orbit chime, alarms
- **Shareable URLs** — Save/load mission configurations

### OMS Control
- **Prograde/Retrograde Delta-V** — Interactive orbital maneuvering in space
- **Telemetry Grid** — Real-time display of altitude, velocity, apoapsis, periapsis, eccentricity with rate-of-change arrows

## 🚀 Quick Start

### From Command Line (Java)
```bash
# Using a rocket preset
java com.aerospace.Main saturn-v standard

# Available presets: saturn-v, falcon-9, space-shuttle, starship
# Optional mission-type: standard, orbital, lunar, escape
```

### From Web Browser
1. Open `docs/index.html` in any modern browser
2. Select a rocket preset from the UI
3. Adjust launch angle (default: 80°) and initial velocity (default: 50 m/s)
4. Click **Launch** and watch the trajectory
5. Use the timeline scrubber to replay the flight
6. Toggle **"Show Science"** for educational tooltips
7. Export data as **CSV** or **JSON**
8. Use **OMS Control** for prograde/retrograde delta-V adjustments in orbit

## 📚 Mission Parameters & Orbital Mechanics

### Key Equations (Quick Reference)
- **Gravity**: F = GMm/r² where GM = 3.986×10¹⁴ m³/s², Earth radius = 6,371,000 m
- **Orbital Energy**: E = v²/2 - μ/r (μ = gravitational parameter)
- **Eccentricity**: e = √(ex² + ey²) from eccentricity vector
- **Apoapsis**: rₐ = a(1 + e), **Periapsis**: rₚ = a(1 - e)
- **Hohmann Transfer**: Δv₁ = √(μ/r₁)(√(2r₂/(r₁+r₂)) - 1), Δv₂ = √(μ/r₂)(1 - √(2r₁/(r₁+r₂)))
- **Rocket Equation**: Δv = Isp·g₀·ln(m₀/mf) where g₀ = 9.80665 m/s²
- **Specific Impulse**: Isp = F/(ṁ·g₀)
- **Drag Force**: Fd = ½ρv²CdA (dynamic pressure q = ½ρv²)
- **Mach Number**: M = v/as where as = speed of sound from ISA model

### ISA 1976 Atmosphere Layers (9 Layers)
| Layer | Altitude Range (km) | Temperature Gradient |
|-------|---------------------|----------------------|
| Troposphere | 0 → 11 | -6.5 K/km |
| Lower Stratosphere | 11 → 20 | +1.0 K/km |
| Upper Stratosphere | 20 → 32 | +2.8 K/km |
| Mesosphere | 32 → 47 | -2.8 K/km |
| Lower Thermosphere | 47 → 51 | +2.8 K/km |
| Intermediate Thermosphere | 51 → 72 | +2.0 K/km |
| Upper Thermosphere | 72 → 85 | +2.0 K/km |
| Exosphere Transition | 85 → 500 | Variable |
| Exosphere | 500+ | → 0 K (space) |

### Rocket Preset Specifications
| Rocket | Stages | First Stage Thrust (N) | Isp (s) | Key Characteristics |
|--------|--------|----------------------|---------|----------------------|
| **Saturn V** | 3 (S-IC/S-II/S-IVB) | 35,000,000 | 350–380 | Apollo missions to Moon |
| **Falcon 9** | 1 (Merlin 1D) | 7,607,000 | 311 | Orbital launch, reusable |
| **Space Shuttle** | 2 (SSME+SRBs) | 14,800,000 | 269–480 | Crew/ cargo to LEO |
| **Starship** | 2 (Raptor 1st+2nd) | 15,000,000 | 330–350 | Mars architecture, fully reusable |

## 🛠️ Command Reference

### Main.java Usage
```
java com.aerospace.Main <rocket-preset> [mission-type]
```

**Arguments:**
- `rocket-preset` (required): One of `saturn-v`, `falcon-9`, `space-shuttle`, `starship`
- `mission-type` (optional): `standard`, `orbital`, `lunar`, `escape`

**Example:**
```bash
java com.aerospace.Main starship orbital
```

### SimulationLogger Methods
- `exportCSV(String filename)` — Export telemetry to CSV
- `exportJSON(String filename)` — Export mission data to JSON
- `generateReport()` — Generate human-readable flight report
- `recordTelemetry(time, position, velocity, mass, stage, altitude)` — Record per-step data
- `recordEvent(time, type, description, altitude, velocity)` — Track significant events

### UI Controls (docs/index.html)
- **Launch** — Start the simulation
- **Show Science** — Toggle physics tooltips on/off
- **Visualization Mode Buttons** — Switch between 5 modes
- **Timeline Scrubber** — Drag to replay flight events
- **OMS Buttons** — Prograde/retrograde delta-V in space
- **Preset Selectors** — Switch between 4 rockets
- **Mission Templates** — Pre-configured LEO, GTO, lunar free-return, escape

## 🔧 Troubleshooting

### Common Issues
- **Canvas not rendering** — Ensure browser supports Canvas API; try Chrome/Firefox/Safari
- **No trajectory visible** — Increase initial velocity or adjust launch angle
- **Simulation runs instantly without flight** — Increase launch angle above 45° for arcing trajectory
- **Crash on launch** — Check console for JavaScript errors; ensure ResizeObserver is supported
- **Mobile canvas too small** — Landscape mode recommended; pinch-to-zoom available

### Console Errors
- `ResizeObserver is not defined` — Polyfill needed for very old browsers
- `Chart.js not loading` — Check internet connection for CDN
- `Web Audio context resume failed` — Interact with page (click/tap) before audio playback

## 📄 Changelog

### v7.0 — Full Science Exhibition Revamp (2026)
- **Physics Engine**: RK4 integration added, full ISA 1976 9-layer atmosphere, Mach-dependent drag with transonic modeling
- **Frontend**: Complete glassmorphism rewrite, 5 visualization modes, mobile responsiveness, touch gestures
- **Educational**: "Show Science" tooltips, comprehensive lesson plans, formulas quick-reference
- **Data Export**: CSV/JSON export, replay system, Web Audio API sound effects
- **Package Structure**: Modularized from monolithic to `com.aerospace.atm`, `com.aerospace.flightengine`, `com.aerospace.physics`, `com.aerospace.vehicle`, `com.aerospace.io`

### v6.0 — Euler Integration (Previous)
- Euler method only, 3-layer simplified atmosphere
- Basic canvas visualization, light theme only
- Single visualization mode

### v5.0 — Package Restructuring
- Modularized from monolithic `src/` to `com.aerospace.*` packages
- Separated atmospheric, flight engine, physics, and vehicle modules

### v4.0 — Preset Support
- Added 4 rocket presets with realistic specifications
- Stage separation logic implemented

### v3.0 — Educational Content
- Added "Show Science" tooltips with physics equations
- Comprehensive README with orbital mechanics reference

### v2.0 — Data Export
- CSV export and flight report generation
- JSON mission export added

### v1.0 — Initial Release
- Basic Euler integration, simple atmosphere model
- Single canvas visualization

# API Reference — Aerospace Trajectory Calculator v7.0

## REST API for Java-Frontend Communication

---

## Table of Contents
1. [Endpoints](#endpoints)
2. [Request/Response Schemas](#requestresponse-schemas)
3. [Example cURL Commands](#example-curl-commands)
4. [Rate Limits](#rate-limits)
5. [Error Codes](#error-codes)

---

## 1. Endpoints

### POST /api/simulate
Run a complete trajectory simulation and return results.

**Request Body (JSON):**
```json
{
  "rocketPreset": "saturn-v|falcon-9|space-shuttle|starship",
  "missionType": "standard|orbital|lunar|escape",
  "launchAngleDeg": number,     // degrees from local horizon (0-90)
  "launchSpeed": number,        // initial speed in m/s
  "duration": number,           // max simulation time in seconds
  "timestep": number            // integration step in seconds (default: 0.01)
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "simulationId": "unique-id-string",
  "rocketPreset": "starship",
  "missionType": "orbital",
  "telemetry": [
    {
      "time": 0.0,
      "altitude": 1000.0,
      "altitude_km": 1.0,
      "velocity": 50.0,
      "velocity_mps": 50.0,
      "mass": 500000.0,
      "stage": 1,
      "mach": 0.15,
      "dynamic_pressure": 12500.0,
      "dynamic_pressure_pa": 12500.0,
      "energy": -1.25e11,
      "energy_joules": -125000000000.0,
      "apoapsis": -1.0,
      "apoapsis_km": -1.0,
      "periapsis": -1.0,
      "periapsis_km": -1.0,
      "eccentricity": 0.0,
      "g_force": 1.0,
      "thrust": 15000000.0,
      "fuel_remaining": 1300000.0,
      "event": "Launch"
    }
    // ... more telemetry points
  ],
  "finalState": {
    "time": 3600.0,
    "altitude": 0.0,          // or positive if impact
    "velocity": 0.0,
    "mass": 50000.0,
    "stage": 0,
    "mach": 0.0,
    "energy": -1.5e11,
    "ap_apoapsis_km": 200.0,
    "ap_periapsis_km": 100.0,
    "ap_eccentricity": 0.15,
    "events": [
      {"time": 15.3, "type": "StageSep", "description": "Stage 1 separation"},
      {"time": 120.5, "type": "StageSep", "description": "Stage 2 separation"},
      {"time": 245.8, "type": "OrbitAchieved", "description": "Orbit achieved"}
    ]
  },
  "summary": {
    "total_time": 3600.0,
    "max_altitude": 200000.0,
    "max_altitude_km": 200.0,
    "max_velocity": 7800.0,
    "max_velocity_mps": 7800.0,
    "total_fuel_consumed": 450000.0,
    "final_apoapsis_km": 200.0,
    "final_periapsis_km": 100.0,
    "orbit_achieved": true,
    "flight_phase": "orbital"
  }
}
```

**Response (400 Bad Request):**
```json
{
  "success": false,
  "error": "INVALID_ROCKET_PRESET",
  "message": "Unknown rocket preset: invalid-rocket"
}
```

**Response (422 Unprocessable Entity):**
```json
{
  "success": false,
  "error": "INVALID_PARAMETER",
  "message": "launchAngleDeg must be between 0 and 90"
}
```

---

### GET /api/health
Health check endpoint.

**Response (200 OK):**
```json
{
  "status": "healthy",
  "timestamp": "2026-10-13T10:30:00Z",
  "version": "7.0.0",
  "services": {
    "java_backend": "operational",
    "isa_atmosphere": "operational",
    "rk4_integrator": "operational",
    "drag_calculator": "operational"
  }
}
```

**Response (503 Service Unavailable):**
```json
{
  "status": "unhealthy",
  "error": "ATMOSPHERE_MODEL_UNAVAILABLE"
}
```

---

### GET /api/presets
List available rocket presets.

**Response (200 OK):**
```json
{
  "success": true,
  "presets": [
    {
      "name": "saturn-v",
      "displayName": "Saturn V",
      "stages": 3,
      "firstStageThrust": 35000000,
      "firstStageIsp": 350,
      "description": "Apollo Saturn V moon rocket"
    },
    {
      "name": "falcon-9",
      "displayName": "Falcon 9",
      "stages": 1,
      "firstStageThrust": 7607000,
      "firstStageIsp": 311,
      "description": "SpaceX Falcon 9 Full Thrust"
    },
    {
      "name": "space-shuttle",
      "displayName": "Space Shuttle",
      "stages": 2,
      "firstStageThrust": 14800000,
      "firstStageIsp": 269.5,
      "description": "Space Shuttle with SSME+SRBs"
    },
    {
      "name": "starship",
      "displayName": "Starship",
      "stages": 2,
      "firstStageThrust": 15000000,
      "firstStageIsp": 330,
      "description": "SpaceX Starship with Raptor engines"
    }
  ]
}
```

---

### GET /api/atmosphere?altitude={number}
Get atmospheric properties at a specific altitude.

**Query Parameters:**
- `altitude` (required): Altitude above sea level in meters

**Response (200 OK):**
```json
{
  "success": true,
  "altitude": 11000.0,
  "altitude_km": 11.0,
  "temperature": 216.65,
  "temperature_celsius": -56.5,
  "pressure": 22632.0,
  "density": 0.3639,
  "speed_of_sound": 295.07,
  "mach_number": null,  // calculated externally: v / speed_of_sound
  "layer": "lower_stratosphere"
}
```

**Response (400 Bad Request - altitude out of range):**
```json
{
  "success": false,
  "error": "ALTITUDE_OUT_OF_RANGE",
  "message": "Altitude 100000 must be between 0 and 25000000",
  "hint": "Use ISA 1976 model: troposphere (0-11km), stratosphere (11-50km), mesosphere (50-85km), thermosphere (85-500km), exosphere (500km+)"
}
```

---

## 2. Request/Response Schemas

### SimulationRequest
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `rocketPreset` | string | yes | One of: `saturn-v`, `falcon-9`, `space-shuttle`, `starship` |
| `missionType` | string | no | One of: `standard`, `orbital`, `lunar`, `escape` |
| `launchAngleDeg` | number | yes | 0-90 degrees from local horizon |
| `launchSpeed` | number | yes | Initial velocity in m/s (typically 0-1000) |
| `duration` | number | no | Max simulation time in seconds (default: 3600) |
| `timestep` | number | no | Integration step in seconds (default: 0.01) |

### TelemetryPoint
| Field | Type | Description |
|-------|------|-------------|
| `time` | number | Simulation time in seconds |
| `altitude` | number | Altitude above sea level in meters |
| `altitude_km` | number | Altitude in kilometers |
| `velocity` | number | Speed in m/s |
| `velocity_mps` | number | Alias for velocity |
| `mass` | number | Current vehicle mass in kg |
| `stage` | number | Current stage index (0 = coasting, 1..N = active stage) |
| `mach` | number | Mach number (v/speed_of_sound) |
| `dynamic_pressure` | number | Dynamic pressure q = ½ρv² in Pa |
| `dynamic_pressure_pa` | number | Alias for dynamic_pressure |
| `energy` | number | Specific orbital energy E = v²/2 - μ/r in J |
| `energy_joules` | number | Alias for energy |
| `apoapsis` | number | Apoapsis altitude in meters (-1 if not applicable) |
| `apoapsis_km` | number | Apoapsis in kilometers |
| `periapsis` | number | Periapsis altitude in meters (-1 if not applicable) |
| `periapsis_km` | number | Periapsis in kilometers |
| `eccentricity` | number | Orbital eccentricity (0 = circular) |
| `g_force` | number | G-force experienced (gravity + acceleration / g₀) |
| `thrust` | number | Current thrust in Newtons (0 if coasting) |
| `fuel_remaining` | number | Remaining fuel mass in kg |
| `event` | string | Current event/state label |

### FinalState
| Field | Type | Description |
|-------|------|-------------|
| `time` | number | Final simulation time in seconds |
| `altitude` | number | Final altitude in meters |
| `velocity` | number | Final velocity in m/s |
| `mass` | number | Final mass in kg |
| `stage` | number | Final stage index |
| `mach` | number | Final Mach number |
| `energy` | number | Final specific orbital energy |
| `ap_apoapsis_km` | number | Final apoapsis in km |
| `ap_periapsis_km` | number | Final periapsis in km |
| `ap_eccentricity` | number | Final eccentricity |
| `events` | array | Array of significant event objects |

### Summary
| Field | Type | Description |
|-------|------|-------------|
| `total_time` | number | Total simulation time in seconds |
| `max_altitude` | number | Maximum altitude achieved in meters |
| `max_altitude_km` | number | Maximum altitude in kilometers |
| `max_velocity` | number | Maximum velocity in m/s |
| `total_fuel_consumed` | number | Total fuel consumed in kg |
| `final_apoapsis_km` | number | Final apoapsis in km |
| `final_periapsis_km` | number | Final periapsis in km |
| `orbit_achieved` | boolean | Whether orbit was achieved |
| `flight_phase` | string | `launch`, `ascent`, `orbit`, `escape`, `impact` |

---

## 3. Example cURL Commands

### Run a Falcon 9 Standard Launch
```bash
curl -X POST http://localhost:8080/api/simulate \
  -H "Content-Type: application/json" \
  -d '{
    "rocketPreset": "falcon-9",
    "missionType": "standard",
    "launchAngleDeg": 80,
    "launchSpeed": 50,
    "duration": 600
  }'
```

### Run a Saturn V Lunar Mission
```bash
curl -X POST http://localhost:8080/api/simulate \
  -H "Content-Type: application/json" \
  -d '{
    "rocketPreset": "saturn-v",
    "missionType": "lunar",
    "launchAngleDeg": 85,
    "launchSpeed": 100,
    "duration": 3600
  }'
```

### Check API Health
```bash
curl http://localhost:8080/api/health
```

### List Available Presets
```bash
curl http://localhost:8080/api/presets
```

### Get Atmosphere at 20 km
```bash
curl "http://localhost:8080/api/atmosphere?altitude=20000"
```

### Save Simulation Results to File
```bash
curl -X POST http://localhost:8080/api/simulate \
  -H "Content-Type: application/json" \
  -d '{
    "rocketPreset": "starship",
    "missionType": "escape",
    "launchAngleDeg": 90,
    "launchSpeed": 200,
    "duration": 500
  }' > mission_starship_escape.json
```

---

## 4. Rate Limits

| Tier | Requests per Minute | Requests per Hour | Burst Allowed |
|------|---------------------|-------------------|---------------|
| **Free** | 60 | 1000 | 10/minute |
| **Pro** | 300 | 5000 | 50/minute |
| **Enterprise** | Unlimited | Unlimited | Unlimited |

**Rate Limit Headers (included in all responses):**
- `X-RateLimit-Limit`: Maximum requests allowed in the current window
- `X-RateLimit-Remaining`: Remaining requests in the current window
- `X-RateLimit-Reset`: Unix timestamp when the rate limit resets
- `Retry-After`: Seconds to wait before retrying (if limit exceeded)

**When Rate Limit Exceeded:**
- HTTP 429 Too Many Requests
- Response body: `{"error": "RATE_LIMIT_EXCEEDED", "retry_after": 60}`
- Respect the `Retry-After` header before making further requests

---

## 5. Error Codes

| Code | HTTP Status | Meaning | Resolution |
|------|-------------|---------|------------|
| `INVALID_ROCKET_PRESET` | 400 | Unknown rocket name | Check `rocketPreset` value; use one of the 4 presets |
| `INVALID_PARAMETER` | 422 | Parameter out of valid range | Check field constraints; see request schema |
| `SIMULATION_TIMEOUT` | 408 | Simulation exceeded maximum duration | Reduce `duration` or `timestep` values |
| `ATMOSPHERE_MODEL_UNAVAILABLE` | 503 | Atmospheric model cannot compute at given altitude | Ensure altitude ≤ 25,000,000 m (25,000 km) |
| `RATE_LIMIT_EXCEEDED` | 429 | Too many requests in given time window | Wait and retry after `Retry-After` seconds |
| `INTERNAL_SERVER_ERROR` | 500 | Unexpected error in simulation | Check server logs; contact support if persistent |
| `METHOD_NOT_ALLOWED` | 405 | Wrong HTTP method for endpoint | Use POST for `/api/simulate`, GET for others |

### Error Response Format
All error responses follow this structure:
```json
{
  "success": false,
  "error": "ERROR_CODE",
  "message": "Human-readable error description",
  "timestamp": "2026-10-13T10:30:00Z",
  "path": "/api/simulate"  // optional: the endpoint that generated the error
}
```

---

## API Usage Notes

### Authentication
Currently, the API does not require authentication. Rate limits apply based on IP address. Authentication may be added in future versions.

### CORS
The API supports Cross-Origin Resource Sharing for web frontend development. Recommended headers:
- `Access-Control-Allow-Origin: *`
- `Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS`
- `Access-Control-Allow-Headers: Content-Type, Authorization`

### Integration with Web Frontend
The `docs/index.html` simulator already includes API integration for:
- Loading rocket preset configurations
- Real-time telemetry streaming
- Simulation control (start/pause/stop)
- Data export (CSV/JSON)
- Mission replay functionality

### Offline Deployment
For offline/deployed instances, ensure:
1. Java 17+ runtime is available
2. HTTP server serves the `/api/` endpoints
3. CORS is configured for the web application origin
4. Static assets (`docs/index.html`, `docs/`) are served from the same domain or via CDN

### Supported Languages
- **Java** — Native backend implementation
- **JavaScript/TypeScript** — Frontend integration (fetch API)
- **Python** — `requests` library compatible
- **curl** — Command-line testing
- **Any language** — Standard HTTP/JSON API

---
*API Reference — Aerospace Trajectory Calculator v7.0*
*Generated from modular Java backend with REST endpoints*
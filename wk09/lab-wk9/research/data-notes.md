# Data Quality Notes

## Anomalies
- **Pilot 4 (JS-on), T1 (Filter)**: 
  - **Issue**: Logged as `js_mode=off` twice (23:17:30, 23:18:14).
  - **Cause**: User likely pressed 'Enter' key instead of waiting for auto-search, triggering standard form submission instead of HTMX request.
  - **Impact**: Valid data point, highlights UX issue with form submission handling.

- **Pilot 3 (No-JS), T3 (Add)**:
  - **Observation**: Multiple log entries per action (T3_add + T0_list).
  - **Verification**: Expected behavior for PRG (Post-Redirect-Get) pattern in No-JS mode.

## Exclusions
- None (all data usable). 
- Pilot 4's "off" mode data is retained as it reflects real user behavior.

## Notes
- **Server Timing**: All server-side durations are <10ms (in-memory operations).
- **Validation Logging**: Errors successfully captured for P3 and P4 (blank_title).
- **Filter Performance**: T1 filter consistently took slightly longer (4-9ms) than other tasks (0-1ms), reflecting search iteration cost.


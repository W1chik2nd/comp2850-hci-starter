# Pilot Summary Stats (n=4)

## Completion Rates
| Task | Completion | Notes |
|------|-----------|-------|
| T1 (Filter) | 5/5 (100%) | All attempts successful (P4 tried twice) |
| T2 (Edit) | 4/4 (100%) | All participants successful |
| T3 (Add) | 7/9 (78%) | 7 successes, 2 validation errors (recovered) |
| T4 (Delete) | 5/5 (100%) | No issues |

## Median Times (Server Processing)
*Note: Times represent server-side processing only (in-memory DB), not client-side rendering.*

| Task | Median (ms) | Range | Notes |
|------|------------|-------|-------|
| T1 | 5 | 4ms–9ms | Filter logic is optimized |
| T2 | 1 | 0ms–1ms | Simple update |
| T3 | 1 | 1ms–1ms | Simple list add |
| T4 | 0 | 0ms–1ms | Simple list remove |

## Error Rates
| Task | Validation Errors | Rate | Notes |
|------|------------------|------|-------|
| T3 | 2 errors (P3, P4) | 22% | Blank title submitted (User testing validation) |

## JS-On vs JS-Off (T3 Add Comparison)
- **JS-on (HTMX)**: Median 1ms (Server time)
- **JS-off (No-JS)**: Median 1ms (Server time)
- **Difference**: 
  - **Server-side**: No significant difference (both paths use same optimized backend).
  - **Client-side (Observed)**: JS-off path triggered full page reload (~300-500ms perceived latency), while HTMX was instant.


**Metrics Evidence: Filter Bug Triggering Full Reload**

- **Top (P2 Session)**: Shows `js_mode=off` for `T1_filter` (row 3) and `T4_delete` (row 4), while previous actions like `T3_add` were `js_mode=on`. This indicates that using the filter caused the application to degrade into No-JS mode, forcing a full page reload.
- **Bottom (P4 Session)**: Confirming the pattern with another participant. `T2_edit` and `T4_delete` are `js_mode=on` (correct), but subsequent `T1_filter` actions (rows 3 & 4) are logged as `js_mode=off`.

**Conclusion**: The search filter implementation has a bug that bypasses HTMX interception, causing unexpected full page reloads even for users with JavaScript enabled.


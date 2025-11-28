# Research Pilot Notes - Week 9

## Session: P1 (sid=ylbpf1)
**Date**: 2025-11-27
**Variant**: Keyboard-only, JS-on

| Time | Task | Observation | Tag |
|------|------|-------------|-----|
| 22:52 | T1 | Filtered list. Instant feedback. | performance |
| 22:53 | T3 | Added tasks rapidly. No page reload observed. | ux-efficiency |
| 22:53 | T2 | Edited task inline. Focus remained on item. | a11y-focus |
| 22:53 | T4 | Deleted task. Confirm dialog appeared. Status message announced. | a11y-live |

**Debrief notes**:
- Smooth experience with keyboard.
- Live regions announced status updates correctly.

---

## Session: P2 (sid=k6a55k)
**Date**: 2025-11-27
**Variant**: Mouse/Trackpad, JS-on

| Time | Task | Observation | Tag |
|------|------|-------------|-----|
| 23:05 | T3 | Added task. Success message appeared at top. | success-feedback |
| 23:05 | T2 | Edited task title. | ux-basic |
| 23:05 | T1 | Used filter. List updated dynamically. | ux-dynamic |
| 23:05 | T4 | Deleted two tasks in succession. Confirmation dialogs worked. | ux-safety |

**Debrief notes**:
- "It feels very fast."
- Delete confirmation prevented accidental clicks.

---

## Session: P3 (sid=6rxiob)
**Date**: 2025-11-27
**Variant**: Keyboard-only, JS-off (No-JS test)

| Time | Task | Observation | Tag |
|------|------|-------------|-----|
| 23:10 | T3 | Added task. Full page reload visible. | ux-friction |
| 23:11 | T1 | Filtered list. Page reloaded to show results. | performance |
| 23:11 | T3 | Tried to add empty task. Redirected to error state. Red box visible. | error-recovery |
| 23:11 | T2 | Edited task. Form submission triggered reload. | basic-func |
| 23:11 | T4 | Deleted task. **No confirmation dialog** (expected). Task disappeared immediately. | trade-off |

**Debrief notes**:
- "The page flashing is noticeable compared to before."
- Validated PRG pattern: Refreshing after add/delete didn't re-submit.
- Lack of delete confirmation is a known risk in No-JS mode.

---

## Session: P4 (sid=q3v8l1)
**Date**: 2025-11-27
**Variant**: Screen Reader (NVDA), JS-on

| Time | Task | Observation | Tag |
|------|------|-------------|-----|
| 23:16 | T3 | Added task. SR announced "Task added". | a11y-success |
| 23:16 | T2 | Edited task. | a11y-forms |
| 23:17 | T4 | Deleted task. Modal dialog was accessible. | a11y-modal |
| 23:17 | T1 | Filtered tasks. **Unexpected page reload** (metrics show off). Likely pressed Enter? | ux-anomaly |
| 23:18 | T3 | Triggered validation error. Live region announced "Title is required". | a11y-error |

**Debrief notes**:
- Filter triggered a full reload instead of dynamic update twice (user pressed Enter?).
- Error announcements worked perfectly in HTMX mode.

# Research Pilot Notes - Week 9

## Session: P1 (sid=7a9f2c)
**Date**: 2025-11-23
**Variant**: Keyboard-only, JS-on

| Time | Task | Observation | Tag |
|------|------|-------------|-----|
| 14:23 | T3 | Participant hesitated before submitting—unsure if 'Enter' or button | ux-feedback |
| 14:25 | T3 | Success message not noticed initially | a11y-status |
| 14:26 | T1 | Typed 'report' slowly, watching for instant results | ux-expectation |
| 14:27 | T1 | Screen reader announced "Showing 3 tasks" ✓ | a11y-pass |
| 14:29 | T2 | Clicked Edit, validation error triggered (blank submission) | error-handling |
| 14:30 | T2 | Recovered from error, completed successfully | resilience |

**Debrief notes**:
- "I liked that the filter worked without clicking a button"
- "I wasn't sure the edit saved—maybe make the message more obvious?"
- SR announced status messages correctly throughout

---

## Session: P2 (sid=b4x9k2)
**Date**: 2025-11-23
**Variant**: Mouse/Trackpad, JS-on

| Time | Task | Observation | Tag |
|------|------|-------------|-----|
| 10:15 | T3 | Clicked 'Add Task' quickly, typo in title ("meting" instead of "meeting") | user-error |
| 10:16 | T2 | Used inline edit to fix typo. Liked the "Edit" button proximity. | ux-efficiency |
| 10:17 | T4 | Clicked Delete, surprised by confirmation dialog but clicked OK immediately | ux-safety |
| 10:18 | T1 | Filtered for "buy". Noticed list update instantly. "Oh that's fast." | performance |
| 10:20 | T3 | Tried to add empty task to see what happens. Saw red box. | error-discovery |

**Debrief notes**:
- "The delete confirmation is good, I almost clicked it by accident."
- "Filtering feels very snappy."
- Found the status message clear enough (green text).

---

## Session: P3 (sid=n9m2d4)
**Date**: 2025-11-23
**Variant**: Keyboard-only, JS-off (No-JS test)

| Time | Task | Observation | Tag |
|------|------|-------------|-----|
| 11:05 | T3 | Submitted new task. Page reloaded. Confused for a split second by reload. | ux-friction |
| 11:06 | T3 | Task appeared at bottom. "Okay, it worked." | success-verify |
| 11:08 | T1 | Typed "milk" and pressed Enter. Full reload. Focus reset to top of page. | a11y-focus |
| 11:09 | T1 | Had to tab 15 times to get back to results list after reload. | a11y-effort |
| 11:12 | T4 | Deleted task. No confirmation dialog (browser native). Task gone. | trade-off |
| 11:14 | T2 | Tried to edit empty title. Error summary appeared at top. Link worked to focus input. | resilience |

**Debrief notes**:
- "It works, but tabbing back to the content after every action is annoying."
- "I missed the instant search, having to hit Enter feels old school."
- Validated that core functions still work without JS.

---

## Session: P4 (sid=q3v8l1)
**Date**: 2025-11-23
**Variant**: Screen Reader (NVDA), JS-on

| Time | Task | Observation | Tag |
|------|------|-------------|-----|
| 15:30 | T3 | Navigated to 'Add Task' by heading. Entered text. | a11y-nav |
| 15:31 | T3 | Submitting form triggered "Task added" announcement immediately. | a11y-live |
| 15:33 | T1 | Typing in filter... SR announced "Showing 5 tasks", then "Showing 2 tasks". | a11y-dynamic |
| 15:34 | T1 | Participant stopped typing to listen to update. "Nice feedback." | ux-satisfaction |
| 15:36 | T4 | Triggered delete. Dialog focused correctly. SR read "Delete the task 'Buy milk'?". | a11y-modal |
| 15:38 | T2 | Edit mode swapped in. Focus management kept context inside the list item. | a11y-focus |

**Debrief notes**:
- "The live announcements are perfect, not too chatty."
- "I knew exactly when the list filtered."
- Confirm dialog was fully accessible.

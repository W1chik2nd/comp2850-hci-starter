# COMP2850 HCI Assessment: Evaluation & Redesign Portfolio

> **📥 Download this template**: [COMP2850-submission-template.md](/downloads/COMP2850-submission-template.md)
> Right-click the link above and select "Save link as..." to download the template file.

**Student**: Weize Zheng 201776501
**Submission date**: [DD/MM/YYYY]
**Academic Year**: 2025-26

---

## Privacy & Ethics Statement

- [√] I confirm all participant data is anonymous (session IDs use P1_xxxx format, not real names)
- [√] I confirm all screenshots are cropped/blurred to remove PII (no names, emails, student IDs visible)
- [√] I confirm all participants gave informed consent
- [√] I confirm this work is my own (AI tools used for code assistance are cited below)

**AI tools used** (if any): [e.g., "Copilot for route handler boilerplate (lines 45-67 in diffs)"]
    
Use ChatGPT for data organization and calculation

---

## 1. Protocol & Tasks

### Link to Needs-Finding (LO2)

**Week 6 Job Story #1**:
When I add a new task to my list
I want immediate visual feedback that the task was successfully added
So I don't have to scroll down or search to verify it's there
Because uncertainty about whether an action worked causes anxiety and breaks my flow

**How Task 1 tests this**:

Task 1 requires the participant to add a new task, which directly tests whether the interface provides sufficient feedback to satisfy the user's need for confirmation.

---

### Evaluation Tasks (4-5 tasks)

#### Task 1 (T1): Search & Filter Tasks

- **Scenario**: A user wants to quickly locate a specific task in a long list using searching bar.
- **Action**: Ask the participant to type “report” in the search box and verbally say how many tasks remain visible.
- **Success**: Only tasks containing the word “report” remain visible, and the participant states the correct count.
- **Target time**: < 15 seconds
- **Linked to**: Week 8 Job Story — “When I have many tasks, I want to filter quickly so I can find what I need without scrolling.”

#### Task 2 (T2): Edit a Task Title Inline

- **Scenario**:A user notices a mistake in a task title and wants to correct it without navigating to a new page.
- **Action**:Ask the participant to click the small “edit” button next to a task, change its title to “Submit invoices by Friday”, and save.
- **Success**:The title updates instantly (inline) without reloading the page, and the new text appears correctly.
- **Target time**: < 20 seconds
- **Linked to**:Week 7 Job Story — “When I spot an error in a task, I want to edit it quickly in place so I don’t lose focus.”

#### Task 3 (T3): Complete Add/Delete Task Using Keyboard Only

- **Scenario**:A user cannot use a mouse and relies solely on the keyboard for navigation.
- **Action**:Ask the participant to:
Use Tab / Shift+Tab to reach the “Add task” input
Add a new task titled “Buy oat milk”
Navigate to the delete button and remove the task
(No mouse allowed)
- **Success**:The participant completes both actions with clear focus indicators and no mouse interaction.
- **Target time**: < 1 minute
- **Linked to**:Week 6, Week 9 Job story and Accessibility Lab

[Add Tasks 4-5 as needed]

---

### Consent Script (They Read Verbatim)

**Introduction**:
"Thank you for participating in my HCI evaluation. This will take about 15 minutes. I'm testing my task management interface, not you. There are no right or wrong answers."

**Rights**:
- [√] "Your participation is voluntary. You can stop at any time without giving a reason."
- [√] "Your data will be anonymous. I'll use a code (like P1) instead of your name."
- [√] "I may take screenshots and notes. I'll remove any identifying information."
- [√] "Do you consent to participate?" [Wait for verbal yes]

**Recorded consent timestamp**: [e.g., "P1 consented 22/11/2025 14:05"]
P1 consented 28/11/2025 22:50
P1 consented 28/11/2025 23:01
P1 consented 28/11/2025 23:07
P1 consented 28/11/2025 23:15

---

## 2. Findings Table

**Instructions**: Fill in this table with 3-5 findings from your pilots. Link each finding to data sources.

| Finding | Data Source | Observation (Quote/Timestamp) | WCAG | Impact (1-5) | Inclusion (1-5) | Effort (1-5) | Priority |
|---------|-------------|------------------------------|------|--------------|-----------------|--------------|----------|
| No-JS delete missing confirmation | pilot-notes.md P3 (23:11) | "Deleted task. No confirmation dialog... Task disappeared immediately." | 3.3.4 Error Prevention | 4 | 3 | 3 | 4 |
| JS-off error requires full reload | metrics.csv L16 + P3 notes | "Redirected to error state. Red box visible." (Full page reload) | 3.3.1 Error Identification | 3 | 2 | 2 | 3 |
| Filter triggers full reload (JS-on) | metrics.csv L28-29 (P4) | "Unexpected page reload (metrics show off)." (URL changes to ?q=) | Usability (UX) | 3 | 2 | 2 | 3 |
| Screen Reader status announcements | pilot-notes.md P1 (22:52) | "Live regions announced status updates correctly." (Positive baseline) | 4.1.3 Status Messages | 1 | 5 | 1 | 5 |

**Priority formula**: (Impact + Inclusion) - Effort

**Top 3 priorities for redesign**:
1. Finding #1: No-JS delete missing confirmation - Priority score 4
2. Finding #2: JS-off error requires full reload - Priority score 3
3. Finding #3: Filter triggers full reload (JS-on) - Priority score 3

---

## 3. Pilot Metrics (metrics.csv)

**Instructions**: Paste your raw CSV data here OR attach metrics.csv file

```csv
ts_iso,session_id,request_id,task_code,step,outcome,ms,http_status,js_mode
2025-11-27T22:52:22.430237900Z,P1_ylbp,r0038,T1_filter,,success,4,200,on
2025-11-27T22:53:06.023973900Z,P1_ylbp,r0039,T3_add,,success,1,200,on
2025-11-27T22:53:18.645634700Z,P1_ylbp,r0040,T2_edit,,success,1,200,on
2025-11-27T22:53:33.204739Z,P1_ylbp,r0041,T3_add,,success,1,200,on
2025-11-27T22:53:54.337104700Z,P1_ylbp,r0042,T4_delete,,success,1,200,on
2025-11-27T23:05:05.711174100Z,P2_k6a5,r0006,T3_add,,success,1,200,on
2025-11-27T23:05:20.245185500Z,P2_k6a5,r0007,T2_edit,,success,1,200,on
2025-11-27T23:05:26.864545100Z,P2_k6a5,r0008,T1_filter,,success,9,200,off
2025-11-27T23:05:29.062386100Z,P2_k6a5,r0009,T4_delete,,success,0,200,off
2025-11-27T23:05:29.072498500Z,P2_k6a5,r0010,T0_list,,success,7,200,on
2025-11-27T23:05:44.303757800Z,P2_k6a5,r0011,T4_delete,,success,0,200,on
2025-11-27T23:05:44.314055900Z,P2_k6a5,r0012,T0_list,,success,7,200,on
2025-11-27T23:10:08.272278Z,P3_6rxi,r0013,T0_list,,success,6,200,off
2025-11-27T23:10:20.065458Z,P3_6rxi,r0014,T3_add,,success,1,200,off
2025-11-27T23:10:20.074742300Z,P3_6rxi,r0015,T0_list,,success,6,200,off
2025-11-27T23:11:08.469340100Z,P3_6rxi,r0016,T1_filter,,success,8,200,off
2025-11-27T23:11:25.828881600Z,P3_6rxi,r0017,T3_add,blank_title,validation_error,0,400,off
2025-11-27T23:11:25.828881600Z,P3_6rxi,r0017,T3_add,,success,1,200,off
2025-11-27T23:11:25.838266300Z,P3_6rxi,r0018,T0_list,,success,6,200,off
2025-11-27T23:11:38.433700900Z,P3_6rxi,r0019,T2_edit,,success,0,200,off
2025-11-27T23:11:38.443249200Z,P3_6rxi,r0020,T0_list,,success,7,200,off
2025-11-27T23:11:42.072492300Z,P3_6rxi,r0021,T4_delete,,success,0,200,off
2025-11-27T23:11:42.081730800Z,P3_6rxi,r0022,T0_list,,success,5,200,off
2025-11-27T23:13:32.926466100Z,P3_6rxi,r0023,T0_list,,success,6,200,off
2025-11-27T23:16:17.155414400Z,P4_q3v8,r0024,T0_list,,success,8,200,off
2025-11-27T23:16:26.500103800Z,P4_q3v8,r0025,T3_add,,success,1,200,on
2025-11-27T23:16:38.664968500Z,P4_q3v8,r0026,T2_edit,,success,1,200,on
2025-11-27T23:17:04.777404300Z,P4_q3v8,r0027,T4_delete,,success,0,200,on
2025-11-27T23:17:30.099679200Z,P4_q3v8,r0028,T1_filter,,success,5,200,off
2025-11-27T23:18:14.820953600Z,P4_q3v8,r0029,T1_filter,,success,5,200,off
2025-11-27T23:18:53.429071400Z,P4_q3v8,r0030,T3_add,blank_title,validation_error,0,400,on
2025-11-27T23:18:53.430488300Z,P4_q3v8,r0030,T3_add,,success,1,200,on
```

**Participant summary**:
- **P1**: Standard (HTMX, mouse, JS-on)
- **P2**: keyboard-only, JS-on
- **P3** (if applicable): No-JS (JS-off)
- **P4** (if applicable): Standard with Screen Reader

**Total participants**: 4

---

## 4. Implementation Diffs

**Instructions**: Show before/after code for 1-3 fixes. Link each to findings table.

### Fix 1: [Fix Name]

**Addresses finding**: [Finding #X from table above]

**Before** ([file path:line number]):
```kotlin
// ❌ Problem code
[Paste your original code here]
```

**After** ([file path:line number]):
```kotlin
// ✅ Fixed code
[Paste your improved code here]
```

**What changed**: [1 sentence - what you added/removed/modified]

**Why**: [1 sentence - which WCAG criterion or usability issue this fixes]

**Impact**: [1-2 sentences - how this improves UX, who benefits]

---

### Fix 2: [Fix Name]

**Addresses finding**: [Finding #X]

**Before**:
```kotlin
[Original code]
```

**After**:
```kotlin
[Fixed code]
```

**What changed**:

**Why**:

**Impact**:

---

[Add Fix 3 if applicable]

---

## 5. Verification Results

### Part A: Regression Checklist (20 checks)

**Instructions**: Test all 20 criteria. Mark pass/fail/n/a + add notes.

| Check | Criterion | Level | Result | Notes |
|-------|-----------|-------|--------|-------|
| **Keyboard (5)** | | | | |
| K1 | 2.1.1 All actions keyboard accessible | A | [pass/fail] | [e.g., "Tested Tab/Enter on all buttons"] |
| K2 | 2.4.7 Focus visible | AA | [pass/fail] | [e.g., "2px blue outline on all interactive elements"] |
| K3 | No keyboard traps | A | [pass/fail] | [e.g., "Can Tab through filter, edit, delete without traps"] |
| K4 | Logical tab order | A | [pass/fail] | [e.g., "Top to bottom, left to right"] |
| K5 | Skip links present | AA | [pass/fail/n/a] | [e.g., "Skip to main content works"] |
| **Forms (3)** | | | | |
| F1 | 3.3.2 Labels present | A | [pass/fail] | [e.g., "All inputs have <label> or aria-label"] |
| F2 | 3.3.1 Errors identified | A | [pass/fail] | [e.g., "Errors have role=alert (FIXED in Fix #1)"] |
| F3 | 4.1.2 Name/role/value | A | [pass/fail] | [e.g., "All form controls have accessible names"] |
| **Dynamic (3)** | | | | |
| D1 | 4.1.3 Status messages | AA | [pass/fail] | [e.g., "Status div has role=status"] |
| D2 | Live regions work | AA | [pass/fail] | [e.g., "Tested with NVDA, announces 'Task added'"] |
| D3 | Focus management | A | [pass/fail] | [e.g., "Focus moves to error summary after submit"] |
| **No-JS (3)** | | | | |
| N1 | Full feature parity | — | [pass/fail] | [e.g., "All CRUD ops work without JS"] |
| N2 | POST-Redirect-GET | — | [pass/fail] | [e.g., "No double-submit on refresh"] |
| N3 | Errors visible | A | [pass/fail] | [e.g., "Error summary shown in no-JS mode"] |
| **Visual (3)** | | | | |
| V1 | 1.4.3 Contrast minimum | AA | [pass/fail] | [e.g., "All text 7.1:1 (AAA) via CCA"] |
| V2 | 1.4.4 Resize text | AA | [pass/fail] | [e.g., "200% zoom, no content loss"] |
| V3 | 1.4.11 Non-text contrast | AA | [pass/fail] | [e.g., "Focus indicator 4.5:1"] |
| **Semantic (3)** | | | | |
| S1 | 1.3.1 Headings hierarchy | A | [pass/fail] | [e.g., "h1 → h2 → h3, no skips"] |
| S2 | 2.4.1 Bypass blocks | A | [pass/fail] | [e.g., "<main> landmark, <nav> for filter"] |
| S3 | 1.1.1 Alt text | A | [pass/fail] | [e.g., "No images in interface OR all have alt"] |

**Summary**: [X/20 pass], [Y/20 fail]
**Critical failures** (if any): [List any Level A fails]

---

### Part B: Before/After Comparison

**Instructions**: Compare Week 9 baseline (pre-fix) to Week 10 (post-fix). Show improvement.

| Metric | Before (Week 9, n=X) | After (Week 10, n=Y) | Change | Target Met? |
|--------|----------------------|----------------------|--------|-------------|
| SR error detection | [e.g., 0/2 (0%)] | [e.g., 2/2 (100%)] | [e.g., +100%] | [✅/❌] |
| Validation error rate | [e.g., 33%] | [e.g., 0%] | [e.g., -33%] | [✅/❌] |
| Median time [Task X] | [e.g., 1400ms] | [e.g., 1150ms] | [e.g., -250ms] | [✅/❌] |
| WCAG [criterion] pass | [fail] | [pass] | [— ] | [✅/❌] |

**Re-pilot details**:
- **P5** (post-fix): [Variant - e.g., "Screen reader user, NVDA + keyboard"] - [Date piloted]
- **P6** (if applicable): [Variant] - [Date]

**Limitations / Honest reporting**:
[If metrics didn't improve or only modestly: explain why. Small sample size? Wrong fix? Needs more iteration? Be honest - valued over perfect results.]

---

## 6. Evidence Folder Contents

**Instructions**: List all files in your evidence/ folder. Provide context.

### Screenshots

| Filename | What it shows | Context/Link to finding |
|----------|---------------|-------------------------|
| before-sr-error.png | Error message without role="alert" | Finding #1 - SR errors not announced |
| after-sr-error.png | Error message WITH role="alert" added | Fix #1 verification |
| regression-axe-report.png | axe DevTools showing 0 violations | Verification Part A |
| [your-screenshot-3.png] | [Description] | [Which finding/fix this supports] |

**PII check**:
- [ ] All screenshots cropped to show only relevant UI
- [ ] Browser bookmarks/tabs not visible
- [ ] Participant names/emails blurred or not visible

---

### Pilot Notes

**Instructions**: Attach pilot notes as separate files (P1-notes.md, P2-notes.md, etc.). Summarize key observations here.

**P1** (Standard Mouse/Trackpad, JS-on):
- **Key observation 1**: (22:52) "Filtered list. Instant feedback." - Confirmed HTMX performance.
- **Key observation 2**: (22:53) "Live regions announced status updates correctly." - Confirmed accessibility baseline.

**P2** (Keyboard-only, JS-on):
- **Key observation 1**: (23:05) "Delete confirmation prevented accidental clicks." - Validated safety features.
- **Key observation 2**: (23:05) "It feels very fast." - Positive UX feedback on JS mode.

**P3** (Keyboard-only, JS-off / No-JS):
- **Key observation 1**: (23:11) "Deleted task. No confirmation dialog... Task disappeared immediately." - **Major Finding**.
- **Key observation 2**: (Debrief) "The page flashing is noticeable compared to before." - Identified UX degradation in No-JS.

**P4** (Screen Reader NVDA, JS-on):
- **Key observation 1**: (23:18) Error announcements worked perfectly in HTMX mode ("Title is required" announced).
- **Key observation 2**: (23:17) Filter triggered unexpected full reload (UX anomaly when pressing Enter).

---

## Evidence Chain Example (Full Trace)

**Instructions**: Pick ONE finding and show complete evidence trail from data → fix → verification.

**Finding selected**: [e.g., "Finding #1 - SR errors not announced"]

**Evidence trail**:
1. **Data**: metrics.csv lines 47-49 show P2 (SR user) triggered validation_error 3 times
2. **Observation**: P2 pilot notes timestamp 14:23 - Quote: "I don't know if it worked, didn't hear anything"
3. **Screenshot**: before-sr-error.png shows error message has no role="alert" or aria-live
4. **WCAG**: 3.3.1 Error Identification (Level A) violation - errors not programmatically announced
5. **Prioritisation**: findings-table.csv row 1 - Priority score 7 (Impact 5 + Inclusion 5 - Effort 3)
6. **Fix**: implementation-diffs.md Fix #1 - Added role="alert" + aria-live="assertive" to error span
7. **Verification**: verification.csv Part A row F2 - 3.3.1 now PASS (tested with NVDA)
8. **Before/after**: verification.csv Part B - SR error detection improved from 0% to 100%
9. **Re-pilot**: P5 (SR user) pilot notes - "Heard error announcement immediately, corrected and succeeded"

**Complete chain**: Data → Observation → Interpretation → Fix → Verification ✅

---

## Submission Checklist

Before submitting, verify:

**Files**:
- [ ] This completed template (submission-template.md)
- [ ] metrics.csv (or pasted into Section 3)
- [ ] Pilot notes (P1-notes.md, P2-notes.md, etc. OR summarised in Section 6)
- [ ] Screenshots folder (all images referenced above)
- [ ] Privacy statement signed (top of document)

**Evidence chains**:
- [ ] findings-table.csv links to metrics.csv lines AND/OR pilot notes timestamps
- [ ] implementation-diffs.md references findings from table
- [ ] verification.csv Part B shows before/after for fixes

**Quality**:
- [ ] No PII in screenshots (checked twice!)
- [ ] Session IDs anonymous (P1_xxxx format, not real names)
- [ ] Honest reporting (limitations acknowledged if metrics didn't improve)
- [ ] WCAG criteria cited specifically (e.g., "3.3.1" not just "accessibility")

**Pass criteria met**:
- [ ] n=2+ participants (metrics.csv has 2+ session IDs)
- [ ] 3+ findings with evidence (findings-table.csv complete)
- [ ] 1-3 fixes implemented (implementation-diffs.md shows code)
- [ ] Regression complete (verification.csv has 20 checks)
- [ ] Before/after shown (verification.csv Part B has data)

---

**Ready to submit?** Upload this file + evidence folder to Gradescope by end of Week 10.

**Estimated completion time**: 12-15 hours across Weeks 9-10

**Good luck!** 🎯

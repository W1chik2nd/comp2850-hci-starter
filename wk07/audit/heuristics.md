# Heuristic Evaluation — Week 7

**Evaluator**: Weize Zheng
**Date**: 2025-11-26
**Method**: Nielsen's 10 Usability Heuristics + Shneiderman's Golden Rules

---

## Nielsen's Heuristics

### 1. Visibility of System Status
**Rating**: 4/5 (Good)
**Evidence**:
- ✅ Status messages announce add/delete/edit actions immediately.
- ✅ ARIA live region updates (`role="status"`) ensure screen readers are notified.
- ⚠️ No visible loading indicator for HTMX requests (instant for now, but could be an issue on slow networks).

**Accessibility implication**: Screen readers get confirmation via live region (WCAG 4.1.3), crucial for non-visual feedback.

**Issue identified**: Low severity. Enhancement: Add `hx-indicator` for better feedback on slow connections.

---

### 2. Match Between System and Real World
**Rating**: 5/5 (Excellent)
**Evidence**:
- ✅ Plain language used: "Add Task", "Edit", "Delete", "Save", "Cancel".
- ✅ Confirmation messages use natural language: "Task 'Buy milk' updated successfully".

**Accessibility implication**: Simple, jargon-free language benefits users with cognitive disabilities and lower digital literacy.

**Issue identified**: None.

---

### 3. Customer Control and Freedom
**Rating**: 4/5 (Good)
**Evidence**:
- ✅ Cancel button available in edit mode allows users to back out of changes.
- ❌ No "Undo" option for deletion (permanent action immediately).

**Accessibility implication**: Users with motor impairments or cognitive issues might accidentally delete items and cannot recover them.

**Issue identified**: **Medium severity** — Lack of undo or confirmation for delete action.

---

### 4. Consistency and Standards
**Rating**: 5/5 (Excellent)
**Evidence**:
- ✅ Semantic HTML usage (`<button>` for actions, `<input>` for data).
- ✅ Follows standard ARIA patterns (errors use `role="alert"`).
- ✅ Consistent button placement and styling across different states (view/edit).

**Accessibility implication**: Predictable behavior and standard elements reduce the learning curve for assistive technology users.

**Issue identified**: None.

---

### 5. Error Prevention
**Rating**: 3/5 (Fair)
**Evidence**:
- ✅ Client-side `required` attribute prevents blank submission before network request.
- ⚠️ Delete button is next to Edit button with no confirmation dialog, risking accidental clicks.

**Accessibility implication**: Critical for users with tremors or motor control issues who might click the wrong target.

**Issue identified**: **Medium severity** — Delete action is too easily triggered; needs confirmation dialog or "undo".

---

### 6. Recognition Rather Than Recall
**Rating**: 4/5 (Good)
**Evidence**:
- ✅ Labels are permanently visible (not disappearing placeholders).
- ✅ Hint text persists below input fields (`aria-describedby`).
- ⚠️ Error messages disappear on fresh page load/refresh (though appropriate for stateless web).

**Accessibility implication**: Persistent labels and hints support users with memory impairments or cognitive disabilities.

**Issue identified**: None significant.

---

### 7. Flexibility and Efficiency of Use
**Rating**: 3/5 (Fair)
**Evidence**:
- ✅ Basic keyboard shortcuts work (Enter to submit, Tab to navigate).
- ❌ No accelerators or custom shortcuts (e.g., "Ctrl+N" for new task).

**Accessibility implication**: Power users and keyboard-only users could benefit from faster navigation methods.

**Issue identified**: **Low severity** — Lack of accelerators (acceptable for a simple MVP).

---

### 8. Aesthetic and Minimalist Design
**Rating**: 5/5 (Excellent)
**Evidence**:
- ✅ UI is uncluttered, showing only essential fields (Title, Actions).
- ✅ Inline editing keeps context without opening new modals or windows.
- ✅ Status messages are unobtrusive yet visible.

**Accessibility implication**: Low visual noise reduces cognitive load and helps users with attention deficit disorders focus.

**Issue identified**: None.

---

### 9. Help Users Recognise, Diagnose, and Recover from Errors
**Rating**: 5/5 (Excellent)
**Evidence**:
- ✅ Error messages are specific: "Title is required. Please enter at least one character."
- ✅ Errors are programmatically associated with inputs (`aria-describedby`, `role="alert"`).
- ✅ Recovery is straightforward: the form remains populated (in edit mode) or available to retry.

**Accessibility implication**: Meets WCAG 3.3.1 (Error Identification) and 3.3.3 (Error Suggestion).

**Issue identified**: None.

---

### 10. Help and Documentation
**Rating**: 2/5 (Poor)
**Evidence**:
- ❌ No help documentation or FAQ available.
- ❌ No "tooltips" or explanations for features beyond basic hints.

**Accessibility implication**: While the interface is simple, first-time users or those with cognitive disabilities might appreciate a "How to" guide.

**Issue identified**: **Low severity** — Missing documentation (acceptable for simple MVP, but an area for improvement).

---

## Summary of Issues

| ID | Heuristic | Issue | Severity | Inclusion Risk |
|----|-----------|-------|----------|----------------|
| 1 | 3 (Control & Freedom) | No undo for delete | Medium | Motor, Cognitive |
| 2 | 5 (Error Prevention) | Delete lacks confirmation | Medium | Motor, Cognitive |
| 3 | 7 (Flexibility) | No keyboard shortcuts | Low | Keyboard (expert users) |
| 4 | 10 (Help) | No help documentation | Low | Cognitive, Low digital literacy |

---

**Next**: Add these findings to backlog for prioritization.

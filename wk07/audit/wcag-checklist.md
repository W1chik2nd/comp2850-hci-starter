# WCAG 2.2 AA Checklist — Week 7

**Date**: 2025-11-26
**Scope**: Task manager (add, edit, delete flows)
**Tester**: Weize Zheng

---

## Perceivable (Principle 1)

### 1.1 Text Alternatives
| Criterion | Level | Status | Evidence | Notes |
|-----------|-------|--------|----------|-------|
| 1.1.1 Non-text Content | A | ✅ Pass | `aria-label` on buttons | Buttons have descriptive labels ("Edit task: Buy milk") |

### 1.3 Adaptable
| Criterion | Level | Status | Evidence | Notes |
|-----------|-------|--------|----------|-------|
| 1.3.1 Info and Relationships | A | ✅ Pass | `<label for="title">` links to input | Semantic HTML (`<main>`, `<section>`, `<ul>`) |
| 1.3.2 Meaningful Sequence | A | ✅ Pass | Tab order: skip link → add form → task list | Logical reading order maintained |

### 1.4 Distinguishable
| Criterion | Level | Status | Evidence | Notes |
|-----------|-------|--------|----------|-------|
| 1.4.3 Contrast (Minimum) | AA | ❌ Fail | Delete button #6c757d on #ffffff = 4.2:1 | Needs 4.5:1 (AA). Detected via WebAIM Contrast Checker. |
| 1.4.11 Non-text Contrast | AA | ✅ Pass | Focus outline 3px solid #1976d2 | Sufficient contrast against background |

---

## Operable (Principle 2)

### 2.1 Keyboard Accessible
| Criterion | Level | Status | Evidence | Notes |
|-----------|-------|--------|----------|-------|
| 2.1.1 Keyboard | A | ✅ Pass | All features accessible via Tab/Enter/Space | Tested: add task, edit task, save, cancel, delete |
| 2.1.2 No Keyboard Trap | A | ✅ Pass | No traps detected | Can Tab out of add form and edit form freely |

### 2.4 Navigable
| Criterion | Level | Status | Evidence | Notes |
|-----------|-------|--------|----------|-------|
| 2.4.1 Bypass Blocks | A | ✅ Pass | Skip link appears on Tab, jumps to #main | Tested with keyboard navigation |
| 2.4.3 Focus Order | A | ✅ Pass | Edit: Edit Button → Title Input → Save → Cancel | Logical focus sequence during interaction |
| 2.4.7 Focus Visible | AA | ⚠️ Partial | Pico.css default outline visible, but faint | Focus indicator is present but low contrast in some states |

---

## Understandable (Principle 3)

### 3.2 Predictable
| Criterion | Level | Status | Evidence | Notes |
|-----------|-------|--------|----------|-------|
| 3.2.1 On Focus | A | ✅ Pass | No context change on focus | Focus moves predictably; no surprise popups |
| 3.2.2 On Input | A | ✅ Pass | No auto-submit on input change | Form submits only on explicit button click |

### 3.3 Input Assistance
| Criterion | Level | Status | Evidence | Notes |
|-----------|-------|--------|----------|-------|
| 3.3.1 Error Identification | A | ✅ Pass | Error: "Title is required..." | Specific error message displayed |
| 3.3.2 Labels or Instructions | A | ✅ Pass | All inputs have `<label>` + hint text | `aria-describedby` links to hint text |
| 3.3.3 Error Suggestion | AA | ✅ Pass | Error message includes correction hint | "Please enter at least one character" |

---

## Robust (Principle 4)

### 4.1 Compatible
| Criterion | Level | Status | Evidence | Notes |
|-----------|-------|--------|----------|-------|
| 4.1.2 Name, Role, Value | A | ✅ Pass | Screen reader announces labels | Tested with NVDA: "Edit task: Buy milk" announced correctly |
| 4.1.3 Status Messages | AA | ✅ Pass | `<div role="status" aria-live="polite">` | Live region announces "Task updated successfully" |

---

## Summary
- **Total criteria evaluated**: 15+
- **Pass**: 13
- **Fail**: 1 (1.4.3 Contrast)
- **Partial**: 1 (2.4.7 Focus Visible)

---

## High-Priority Failures
1. **1.4.3 Contrast (Minimum, AA)**: Delete button text (#6c757d) fails 4.5:1 ratio against white background.
   - **Action**: Darken button text or change background color.

2. **2.4.7 Focus Visible (AA, Partial)**: Default focus outline may be too faint for some users.
   - **Action**: Enhance focus styles with higher contrast outline (e.g., 3px solid #1976d2).

---

**Next**: Add these findings to backlog with severity scores.

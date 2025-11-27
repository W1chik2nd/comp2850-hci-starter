# Prototyping Constraints and Trade-offs — Week 8

## Dual-Path Architecture

### Design Decision
Every route handles both HTMX (enhanced) and no-JS (baseline) requests.

### Benefits
- **Inclusion**: Works for everyone regardless of JS availability
- **Resilience**: Graceful degradation if JS fails to load
- **Testing**: Baseline path proves server-first architecture is sound
- **Progressive enhancement**: Start with accessible baseline, enhance with HTMX

### Costs
- **Code complexity**: Each route has conditional logic (`if (call.isHtmx())`)
- **Response duplication**: Must generate both fragments and full pages
- **Testing burden**: Every feature requires two test paths
- **Performance**: No-JS path triggers full page reloads (slower perceived performance)

### Risks
- **Divergence**: HTMX and no-JS paths could drift if not tested regularly
- **Error handling**: Easy to forget no-JS error path during rapid development
- **Template maintenance**: Changes to page structure must update both full templates and fragments

### Mitigations
- ✅ **Verification script**: `scripts/nojs-check.md` provides repeatable tests
- ✅ **Shared partials**: `_item.peb`, `_list.peb` used by both paths (single source of truth)
- ✅ **Backlog tracking**: Log parity issues immediately (see `backlog/backlog.csv`)
- ✅ **Weekly retesting**: Run no-JS script before each commit
- ⚠️ **Automated tests** (future): Playwright tests with JS disabled

---

## Validation Strategy

### Design Decision
All validation on server. No client-side validation (no `<input required>` enforcement).

### Benefits
- **Security**: Client-side validation can be bypassed (view source, disable JS, curl)
- **Consistency**: Same validation rules for HTMX and no-JS paths
- **Accessibility**: Server returns appropriate error format for each context

### Costs
- **Latency**: Must wait for server round-trip to see validation errors
- **UX**: No instant feedback on typos (could add client-side hints later)

### Risks
- **Frustration**: People might repeatedly submit invalid forms before reading error
- **Network dependency**: People offline can't get any feedback

### Mitigations
- ✅ **Clear error messages**: Specific, actionable text ("Title is required" not "Invalid input")
- ✅ **Accessible error identification**: WCAG 3.3.1 compliance (aria-invalid, aria-describedby, role=alert)
- ✅ **Focus management**: Error link navigates directly to problematic field
- 🔮 **Future enhancement**: Add client-side hints (maxlength indicator, real-time char count) as progressive enhancement

---

## Delete Confirmation

### Design Decision
HTMX path uses `hx-confirm` (browser confirmation dialog). No-JS path has no confirmation.

### Benefits
- **HTMX**: Prevents accidental deletions for people with JavaScript enabled
- **Implementation simplicity**: No intermediate confirmation page needed

### Costs
- **Inconsistency**: Different UX depending on JS availability
- **Accessibility**: Browser confirm dialogs are not customisable (can't improve copy)

### Risks
- **Accidental deletion**: People without JavaScript might delete tasks by mistake
- **Compliance**: Depending on context, irreversible actions might require confirmation (WCAG 2.2.1 Timing Adjustable, 3.3.4 Error Prevention)

### Mitigations
- ✅ **Documentation**: Trade-off explicitly noted in constraints doc
- ✅ **Backlog item**: Consider adding `/tasks/{id}/delete/confirm` page for no-JS (low priority)
- ⚠️ **Research with participants** (Week 9): Test whether delete accidents occur in pilots
- 🔮 **Future option**: Add "Undo" feature (restore from soft-delete within 30s)

---

## State Management

### Design Decision
Use query parameters for filter and page state (`?q=search&page=2`).

### Benefits
- **Shareable**: URL captures full state (can bookmark or share filtered view)
- **History**: Back/forward buttons work predictably
- **No-JS compatible**: Query params work without JavaScript
- **Stateless server**: No session state needed for pagination

### Costs
- **URL pollution**: Long query strings for complex filters
- **Encoding**: Must properly encode/decode special characters
- **Analytics**: Harder to track "unique pages" if many query variations exist

### Risks
- **Drift**: If fragment requests use different query params than full page, state can desync
- **Limits**: Some servers/proxies have URL length limits (~2000 chars)

### Mitigations
- ✅ **Consistent param names**: Use `q` and `page` everywhere
- ✅ **Validation**: Sanitise and bound page numbers (reject negative, exceeds max)
- ✅ **Encoding**: Use `call.request.queryParameters` (Ktor handles encoding)
- 🔮 **Future**: If filters grow complex, consider POST with session state

---

## Performance Considerations

### Active Search Debounce
- **Decision**: 300ms debounce on `hx-trigger="keyup changed delay:300ms"`
- **Benefit**: Reduces server load (doesn't fire on every keystroke)
- **Cost**: 300ms perceived latency before filter applies
- **Mitigation**: Show loading indicator (`hx-indicator`) during request

### Page Size
- **Decision**: 10 tasks per page
- **Benefit**: Fast page loads, manageable scroll
- **Cost**: More pagination clicks for large datasets
- **Mitigation**: Could add page size selector (10/25/50) in future

### Fragment Size
- **Decision**: Return `_list + _pager + status` (~2-5KB) instead of full page (~15KB)
- **Benefit**: 70% bandwidth reduction on filter/pagination
- **Cost**: Requires dual-path logic
- **Measurement**: Use browser DevTools Network tab to verify savings

---

## Evidence and Testing

**Verification scripts**: `wk08/lab-w8/scripts/nojs-check.md`
**Evidence folder**: `evidence/wk8/nojs-parity/`
**Backlog references**: IDs `wk8-01` to `wk8-XX`

**Review schedule**: Re-run parity tests after:
- Any route changes
- Template structure updates
- Before Week 9 instrumentation (ensure baseline is solid)
- Before Gradescope Task 1 submission

**Ownership**: Entire team responsible for verifying parity. Pair on changes: one person tests HTMX, another tests no-JS.

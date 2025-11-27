# Prototyping Constraints & Trade-offs

## 1. Rendering Splits (Full Page vs. Fragments)
We adopted a hybrid rendering strategy to support both progressive enhancement (HTMX) and baseline HTML functionality (No-JS):
- **Full Page (`GET /tasks`)**: Renders the complete `index.peb` layout, including navigation, header, and footer. Used for initial page loads and No-JS fallbacks.
- **Fragments (`GET /tasks/fragment`)**: Renders only the `_fragment.peb` partial (containing list, pager, and header). Used exclusively by HTMX for "Active Search" and pagination updates.
- **Trade-off**: This dual-mode approach improves user experience (faster updates, no full reload) but requires the router to decide which template to render based on the `HX-Request` header, slightly increasing controller complexity.

## 2. Accessibility Hooks
To ensure WCAG 2.2 AA compliance during dynamic updates:
- **Live Regions**: Added a visually hidden `<div id="status" role="status" aria-live="polite">` that is updated via HTMX OOB swaps. This announces "Found X tasks" to screen reader users after filtering or pagination.
- **Result Count**: The `<h2 id="list-heading">` includes the total task count, which is updated dynamically to keep visual users informed.
- **Focus Management**: The "Skip to main content" link allows keyboard users to bypass navigation. The pagination links and filter form are keyboard-accessible.
- **Trade-off**: Implementing OOB swaps for status messages adds overhead to the response payload (returning multiple fragments) but is essential for non-visual feedback.

## 3. State Management Decisions
- **Query Parameters**: 
    - `q`: Used for search terms (standard convention).
    - `page`: Used for pagination index (1-based).
- **Persistence**: We use `hx-push-url="true"` on filter and pagination actions. This ensures the URL in the browser address bar always reflects the current application state, allowing users to bookmark or share specific search results/pages.
- **Page Size**: Fixed at 10 items per page to balance load times and list readability.
- **Trade-off**: Server-side state (URL parameters) is robust but requires careful handling of edge cases (e.g., invalid page numbers) in the controller.

## 4. Introduced Risks & Technical Debt
- **Template Fragmentation**: We now maintain multiple partials (`_list.peb`, `_pager.peb`, `_fragment.peb`, `index.peb`). Changes to the task list structure must be verified across both full-page and fragment renders to ensure consistency.
- **Duplicate Logic**: The `GET /tasks` and `GET /tasks/fragment` routes share significant logic (parameter parsing, search, pagination). While we extracted some helpers, there is still code duplication in the route definitions that could drift over time.
- **Out-of-Range Pages**: If a user manually enters `?page=999`, the current implementation returns an empty list rather than a 404. While safe, this might be confusing UX. The pagination logic currently clamps values but doesn't explicitly signal "page not found".

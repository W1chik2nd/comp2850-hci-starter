**Comparison: Full Page Reload vs. HTMX**

- **Top (JS Enabled)**: The URL remains clean (`/tasks`). HTMX handles updates via AJAX, so the browser history and URL bar don't flicker or change unnecessarily.
- **Bottom (No-JS Mode)**: The URL changes (`/tasks?q=report`), indicating a traditional GET request and a full page reload. This confirms that No-JS users experience a slower, less seamless interaction flow.
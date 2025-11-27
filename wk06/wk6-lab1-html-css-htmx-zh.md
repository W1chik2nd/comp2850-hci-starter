# 第 6 周 • 实验 1：使用 HTMX 构建服务器优先基础


![COMP2850](https://img.shields.io/badge/COMP2850-HCI-blue)
![Week 6](https://img.shields.io/badge/Week-6-orange)
![Lab 1](https://img.shields.io/badge/Lab-1-green)
![Status](https://img.shields.io/badge/Status-Draft-yellow)

---

## 术语说明

在 COMP2850 课程中，我们使用**以人为本的语言**（例如，"使用屏幕阅读器的人"）而不是基于缺陷的术语（例如，"盲人用户"）。这反映了当代包容性设计实践，并承认残疾源于环境障碍，而非个人缺陷。

---

## 课前阅读

**必读**
- [Gross, Haddad & Weibel (2023). *Hypermedia Systems*, 第 1-3 章](https://hypermedia.systems/)
- [HTMX 文档：核心概念](https://htmx.org/docs/)
- [GOV.UK 服务手册：渐进式增强](https://www.gov.uk/service-manual/technology/using-progressive-enhancement)

**推荐阅读**
- [W3C (2024). WCAG 2.2 快速参考](https://www.w3.org/WAI/WCAG22/quickref/)
- [MDN：语义化 HTML](https://developer.mozilla.org/en-US/docs/Glossary/Semantics#semantic_elements)
- `../../references/privacy-by-design.md`（模块特定的伦理指导）

---

## 引言

### 背景

本实验标志着 COMP2850 **人机交互（HCI）部分**的开始（第 6-11 周）。在前五周中，你构建了基础的 Kotlin/OOP 技能。现在你将把这些技能应用到真实世界的 HCI 挑战中：使用**服务器优先架构**和**渐进式增强**构建包容、可访问的 Web 应用程序。

现代 Web 开发通常默认使用客户端重型 JavaScript 框架（React、Vue、Angular）。虽然功能强大，但这些方法可能造成可访问性障碍：
- 屏幕阅读器难以处理动态渲染的内容
- 当焦点管理不正确时，键盘导航会中断
- 禁用 JavaScript 的客户（企业防火墙、数据节省模式）会失去功能
- 开发复杂度增加，使可访问性修复成本高昂

**服务器优先架构**颠覆了这一模式：服务器渲染完整的、语义化的 HTML，这些 HTML 可以在*没有* JavaScript 的情况下工作。然后我们添加 HTMX 作为**渐进式增强层**，在不牺牲基线体验的情况下改善交互性。

### 为什么这很重要

**在专业领域**，服务器优先模式在行业中越来越受欢迎：
- **GOV.UK**（英国政府数字服务）强制要求渐进式增强
- **Basecamp**（项目管理，300 万+用户）使用服务器渲染的 HTML + Turbo（类似于 HTMX）
- **GitHub** 主要提供服务器渲染的 HTML，并辅以 JavaScript 增强
- **Stack Overflow** 90% 的内容是服务器渲染的，以实现性能和可访问性

**在学术领域**，本实验介绍了核心 HCI 概念：
- **设计中的包容性**：将可访问性构建到基础中，而不是事后补救
- **渐进式增强**：分层能力，使每个人都能获得基线体验
- **服务器端渲染（SSR）**：在服务器上生成 HTML，与客户端渲染（CSR）相对
- **超媒体驱动架构**：使用 HTML 作为应用程序状态的引擎（HATEOAS）

### 学习成果

在本实验结束时，你将能够：

| 学习成果 | 模块 LO | ACM/WCAG 参考 |
|---|---|---|
| 使用 Ktor 和 Pebble 模板实现服务器优先路由 | LO5：应用软件设计模式 | ACM: SEP/2 |
| 使用 HTMX 增强表单，而不破坏无 JS 功能 | LO6：集成 Web 技术 | ACM: SEP/3 |
| 创建具有语义结构、ARIA 实时区域、跳过链接的可访问 HTML | LO9：应用可访问性标准 | WCAG 2.2 AA |
| 验证键盘导航、屏幕阅读器公告、无 JS 对等性 | LO10：评估包容性设计 | ACM: HC/4 |

---

## 核心概念

### 1. 服务器优先架构

**服务器优先**（也称为"服务器端渲染"或 SSR）意味着服务器生成完整的 HTML 页面并发送到浏览器。浏览器立即显示它们，无需等待 JavaScript 运行。

**示例**：
```
用户请求 /tasks
→ 服务器查询数据库
→ 服务器使用数据渲染 tasks/index.peb 模板
→ 服务器发送完整 HTML 到浏览器
→ 浏览器显示页面（即使禁用 JS 也能工作）
```

**与客户端渲染（CSR）的对比**：
```
用户请求 /tasks
→ 服务器发送空 HTML + JavaScript 包
→ 浏览器下载并执行 JS
→ JS 调用 /api/tasks API
→ JS 渲染 HTML 并插入到 DOM
❌ 如果 JS 加载失败，页面为空白
```

**优势**：
- ✅ 更快的初始页面加载（无 JS 解析延迟）
- ✅ 无需 JavaScript 即可工作（弹性）
- ✅ 搜索引擎可以索引内容（SEO）
- ✅ 屏幕阅读器立即接收语义化 HTML

### 2. 渐进式增强

**渐进式增强**是一种设计理念：从适用于所有人的基线体验开始，然后为支持它们的浏览器添加增强功能。

**层次**：
1. **HTML**（内容层）：语义标记，默认可访问
2. **CSS**（表现层）：样式，如果不支持则优雅降级
3. **JavaScript**（行为层）：动态交互，可选

**示例（COMP2850 任务管理器）**：
- **基线**：表单 POST 到 `/tasks`，服务器验证，重定向（PRG 模式）
- **增强**：HTMX 拦截 POST，交换 HTML 片段，无页面重载

**关键原则**：JavaScript 失败（网络错误、脚本被阻止、不支持的浏览器）不应破坏核心功能。

### 3. HTMX 基础

**HTMX** 通过触发 AJAX 请求和更新 DOM 的属性扩展 HTML。你不需要编写 JavaScript，而是在 HTML 中声明行为。

**核心属性**：
| 属性 | 用途 | 示例 |
|-----------|---------|---------|
| `hx-get` | HTTP GET 请求 | `<button hx-get="/tasks/1">加载</button>` |
| `hx-post` | HTTP POST 请求 | `<form hx-post="/tasks">` |
| `hx-target` | 插入响应的位置 | `hx-target="#task-list"` |
| `hx-swap` | 如何插入响应 | `hx-swap="beforeend"`（追加），`hx-swap="outerHTML"`（替换） |
| `hx-swap-oob` | 带外交换（更新目标外的元素） | `<div id="status" hx-swap-oob="true">` |

**HTMX 工作原理**：
1. 用户触发事件（点击、提交）
2. HTMX 向服务器发出 AJAX 请求
3. 服务器返回 HTML 片段（不是 JSON）
4. HTMX 将片段交换到目标元素中
5. 屏幕阅读器实时区域公告更改

**为什么这有助于可访问性**：
- 服务器控制 HTML 结构（一致的语义）
- 无需手动 DOM 操作（减少 ARIA 错误）
- 实时区域自动工作（如果服务器包含它们）

### 4. 发布-重定向-获取（PRG）模式

**问题**：如果用户使用 POST 提交表单，然后刷新页面，浏览器会重新提交表单（重复提交）。

**解决方案**：处理 POST 后，返回重定向（HTTP 303）到 GET URL。

**流程**：
```
1. 用户提交表单 → POST /tasks (title="Buy milk")
2. 服务器验证，保存到数据库
3. 服务器返回：HTTP 303 See Other, Location: /tasks
4. 浏览器跟随重定向 → GET /tasks
5. 服务器渲染更新的任务列表
6. 用户看到新任务；刷新 = 安全的 GET（无重复）
```

**在 Ktor 中**：
```kotlin
post("/tasks") {
    val title = call.receiveParameters()["title"].orEmpty().trim()
    if (title.isNotBlank()) {
        repo.add(title)
    }
    call.respondRedirect("/tasks") // PRG 重定向
}
```

### 5. ARIA 实时区域

**ARIA**（可访问的富互联网应用程序）提供帮助屏幕阅读器理解动态内容的属性。

**实时区域**在不移动焦点的情况下公告更改。对于 AJAX 更新的内容至关重要。

**关键属性**：
| 属性 | 用途 | 示例 |
|-----------|---------|---------|
| `role="status"` | 公告非关键更新 | `<div role="status" aria-live="polite">任务已添加</div>` |
| `role="alert"` | 公告关键错误 | `<div role="alert">标题是必需的</div>` |
| `aria-live="polite"` | 等待用户完成后再公告 | 状态消息 |
| `aria-live="assertive"` | 立即中断并公告 | 错误消息 |

**示例（COMP2850 模式）**：
```html
<!-- 在 base.peb 中 -->
<div id="status" role="status" aria-live="polite" class="visually-hidden"></div>

<!-- 服务器响应（HTMX OOB 交换） -->
<div id="status" hx-swap-oob="true">任务 "Buy milk" 已添加</div>
```

**工作原理**：
1. HTMX 请求完成
2. 服务器返回片段 + 带有 `hx-swap-oob="true"` 的状态 div
3. HTMX 交换主要内容*和*状态 div（带外）
4. 屏幕阅读器检测到状态 div 更改，公告"任务 'Buy milk' 已添加"

---

## 活动 1：项目设置和脚手架检查

**时间**：15 分钟
**材料**：启动包（带有 Ktor + Pebble 的 Gradle 项目）

### 步骤 1：克隆启动包

```bash
# 克隆启动仓库（URL 在 Minerva 上提供）
git clone [REPO_URL]/comp2850-hci-starter.git
cd comp2850-hci-starter

# 或在 Codespaces 中打开：点击 "Code" → "Create codespace on main"
```

### 步骤 2：验证依赖项

打开 `build.gradle.kts` 并确认这些依赖项：

```kotlin
dependencies {
    implementation("io.ktor:ktor-server-core:2.3.12")
    implementation("io.ktor:ktor-server-netty:2.3.12")
    implementation("io.pebbletemplates:pebble:3.2.2")
    implementation("ch.qos.logback:logback-classic:1.5.6")
}
```

**这些依赖项的作用**：
- **Ktor**：Kotlin Web 框架（路由、HTTP 处理）
- **Pebble**：模板引擎（使用数据渲染 HTML）
- **Logback**：日志记录（调试服务器行为）

### 步骤 3：运行服务器

```bash
./gradlew run

# Windows
gradlew.bat run
```

**预期输出**：
```
Application started in 0.5s
Listening on http://0.0.0.0:8080
```

**访问**：http://localhost:8080/tasks

你应该看到占位符文本："Task manager coming soon..." 这确认服务器正在工作。

### 步骤 4：检查目录结构

```
comp2850-hci-starter/
├── src/main/kotlin/
│   ├── Main.kt               # 服务器入口点
│   ├── model/
│   │   └── Task.kt           # 数据模型
│   ├── routes/
│   │   └── TaskRoutes.kt     # CRUD 操作
│   ├── storage/
│   │   └── TaskStore.kt      # CSV 持久化
│   └── utils/
│       └── SessionUtils.kt   # 匿名会话
├── src/main/resources/
│   ├── templates/
│   │   ├── _layout/
│   │   │   └── base.peb      # 可访问的基础布局
│   │   └── tasks/
│   │       ├── index.peb     # 完整页面视图
│   │       ├── _list.peb     # 任务列表部分
│   │       └── _item.peb     # 单个任务部分
│   └── static/
│       ├── css/custom.css    # WCAG 样式
│       └── js/htmx-1.9.12.min.js
├── data/
│   └── tasks.csv             # 基于文件的存储
├── build.gradle.kts          # 依赖项
└── README.md                 # 综合指南
```

**停止并检查**：
- ✅ 服务器运行无错误
- ✅ http://localhost:8080/tasks 加载（即使是占位符）
- ✅ 你可以在 IDE 中看到 `base.peb` 和 `tasks/index.peb`

---

## 活动 2：构建可访问的基础布局

**时间**：30 分钟
**材料**：`src/main/resources/templates/base.peb`

### 步骤 1：创建语义化 HTML 结构

将 `base.peb` 的内容替换为：

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>{{ title | default("COMP2850 Task Manager") }}</title>
  <link rel="stylesheet" href="https://unpkg.com/@picocss/pico@2/css/pico.min.css">
  <style>
    /* 视觉上隐藏但对屏幕阅读器可访问 */
    .visually-hidden {
      position: absolute !important;
      clip: rect(1px, 1px, 1px, 1px);
      padding: 0 !important;
      border: 0 !important;
      height: 1px !important;
      width: 1px !important;
      overflow: hidden;
      white-space: nowrap;
    }

    /* 跳过链接（仅键盘） */
    .skip-link {
      position: absolute;
      left: -10000px;
      width: 1px;
      height: 1px;
      overflow: hidden;
    }
    .skip-link:focus {
      position: static;
      width: auto;
      height: auto;
      background: #1976d2;
      color: white;
      padding: 0.5rem 1rem;
      text-decoration: none;
      font-weight: bold;
    }

    /* ARIA 实时区域样式 */
    #status:not(:empty) {
      background: #e3f2fd;
      border-left: 4px solid #1976d2;
      padding: 1rem;
      margin: 1rem 0;
    }
  </style>
  <script src="https://unpkg.com/htmx.org@2.0.0"></script>
</head>
<body>
  <a href="#main" class="skip-link">跳转到主要内容</a>

  <main class="container" id="main">
    <div id="status" role="status" aria-live="polite" class="visually-hidden"></div>
    {% block content %}{% endblock %}
  </main>

  <footer class="container">
    <p><small>COMP2850 HCI • 利兹大学 • 2024/25</small></p>
  </footer>
</body>
</html>
```

### 步骤 2：理解可访问性特性

**跳过链接**（第 35-36 行）：
- 允许键盘/屏幕阅读器用户跳过重复的导航
- 隐藏直到获得焦点（键盘 Tab 键显示）
- **WCAG 2.4.1（绕过块，A 级）**：绕过重复内容的机制

**实时区域**（第 39 行）：
- `role="status"`：在不窃取焦点的情况下公告更新
- `aria-live="polite"`：等待用户暂停后再公告
- `.visually-hidden`：视觉上隐藏但由屏幕阅读器公告
- **WCAG 4.1.3（状态消息，AA 级）**：状态消息以编程方式公告

**语义化 HTML**：
- `<main>`：主要内容地标
- `<footer>`：网站信息地标
- `lang="en"`：告诉屏幕阅读器使用哪种语言
- **WCAG 1.3.1（信息和关系，A 级）**：结构以编程方式传达

### 步骤 3：测试基础布局

重新加载 http://localhost:8080/tasks。你应该仍然看到占位符文本，但现在：

1. **按一次 Tab**：跳过链接出现，带有蓝色背景
2. **在跳过链接上按 Enter**：焦点跳转到 `#main`（目前没有效果，但当我们添加导航时会有用）
3. **使用 DevTools 检查**：
   - 打开 Elements 面板
   - 找到带有 `role="status"` 和 `aria-live="polite"` 的 `<div id="status">`
   - 确认应用了 `.visually-hidden` 类

**停止并检查**：
- ✅ 跳过链接在键盘焦点时出现
- ✅ 实时区域存在于 DOM 中（目前为空）
- ✅ Pico.css 已加载（页面有默认样式）

---

## 活动 3：实现服务器端路由和仓库

**时间**：35 分钟
**材料**：`src/main/kotlin/routes/Tasks.kt`，`data/tasks.csv`

### 步骤 1：创建简单仓库

创建 `src/main/kotlin/data/TaskRepository.kt`：

```kotlin
package data

import java.io.File
import java.util.concurrent.atomic.AtomicInteger

data class Task(val id: Int, var title: String)

object TaskRepository {
    private val file = File("data/tasks.csv")
    private val tasks = mutableListOf<Task>()
    private val idCounter = AtomicInteger(1)

    init {
        file.parentFile?.mkdirs()
        if (!file.exists()) {
            file.writeText("id,title\n")
        } else {
            file.readLines().drop(1).forEach { line ->
                val parts = line.split(",", limit = 2)
                if (parts.size == 2) {
                    val id = parts[0].toIntOrNull() ?: return@forEach
                    tasks.add(Task(id, parts[1]))
                    idCounter.set(maxOf(idCounter.get(), id + 1))
                }
            }
        }
    }

    fun all(): List<Task> = tasks.toList()

    fun add(title: String): Task {
        val task = Task(idCounter.getAndIncrement(), title)
        tasks.add(task)
        persist()
        return task
    }

    fun delete(id: Int): Boolean {
        val removed = tasks.removeIf { it.id == id }
        if (removed) persist()
        return removed
    }

    private fun persist() {
        file.writeText("id,title\n" + tasks.joinToString("\n") { "${it.id},${it.title}" })
    }
}
```

**为什么使用 CSV？**
- 简单、可检查、无需数据库设置
- 适合学习（专注于 HCI，而不是数据库配置）
- 生产应用会使用 PostgreSQL/MongoDB

### 步骤 2：创建基线路由（尚未使用 HTMX）

编辑 `src/main/kotlin/routes/Tasks.kt`：

```kotlin
package routes

import data.TaskRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.pebbletemplates.pebble.PebbleEngine
import java.io.StringWriter

fun Route.taskRoutes() {
    val pebble = PebbleEngine.Builder().build()

    get("/tasks") {
        val model = mapOf(
            "title" to "Tasks",
            "tasks" to TaskRepository.all()
        )
        val template = pebble.getTemplate("templates/tasks/index.peb")
        val writer = StringWriter()
        template.evaluate(writer, model)
        call.respondText(writer.toString(), ContentType.Text.Html)
    }

    post("/tasks") {
        val title = call.receiveParameters()["title"].orEmpty().trim()
        if (title.isNotBlank()) {
            TaskRepository.add(title)
        }
        call.respondRedirect("/tasks") // PRG 模式
    }

    post("/tasks/{id}/delete") {
        val id = call.parameters["id"]?.toIntOrNull()
        id?.let { TaskRepository.delete(it) }
        call.respondRedirect("/tasks") // PRG 模式
    }
}
```

**更新 `Main.kt` 中的 `configureRouting()` 函数**：

在 `src/main/kotlin/Main.kt` 文件中，`configureRouting()` 函数已经存在（第 208-220 行）。确保它包含 `taskRoutes()` 调用：

```kotlin
fun Application.configureRouting() {
    routing {
        // Static files (CSS, JS, HTMX library)
        staticResources("/static", "static")

        // Health check endpoint (for monitoring)
        configureHealthCheck()

        // Task management routes (main feature)
        taskRoutes()  // 确保这行存在
    }
}
```

**注意**：如果你的项目结构不同，`taskRoutes()` 可能需要在其他路由配置文件中调用。

### 步骤 3：创建任务列表模板

> **💡 Pebble 模板语法入门**
>
> Pebble 使用三种类型的分隔符：
>
> | 语法 | 用途 | 示例 |
> |--------|---------|---------|
> | `{{ variable }}` | **输出** - 打印变量值 | `{{ task.title }}` 输出 "Buy milk" |
> | `{% statement %}` | **逻辑** - 控制结构（if、for、extends） | `{% for task in tasks %}...{% endfor %}` |
> | `{# comment #}` | **注释** - 不在 HTML 中渲染 | `{# TODO: 添加分页 #}` |
>
> **常用语句**：
> - `{% extends "base.peb" %}` - 从父模板继承
> - `{% block content %}...{% endblock %}` - 定义/覆盖内容部分
> - `{% for item in list %}...{% endfor %}` - 遍历集合
> - `{% if condition %}...{% endif %}` - 条件渲染
> - `{% empty %}` - 在 `{% for %}` 内部，如果列表为空则显示
>
> **过滤器**（管道语法）：
> - `{{ tasks | length }}` - 获取列表大小（输出：`3`）
> - `{{ title | escape }}` - HTML 转义（在 Pebble 中自动启用）
> - `{{ price | default(0) }}` - 如果为 null 则使用后备值
>
> **参考**：[Pebble 文档](https://pebbletemplates.io/) • `../../references/pebble-intro.md`

编辑 `src/main/resources/templates/tasks/index.peb`：

```html
{% extends "base.peb" %}

{% block content %}
<h1>任务</h1>

<section aria-labelledby="add-heading">
  <h2 id="add-heading">添加新任务</h2>
  <form action="/tasks" method="post">
    <label for="title">标题</label>
    <input type="text" id="title" name="title" required
           placeholder="例如：Buy milk" aria-describedby="title-hint">
    <small id="title-hint">保持简短和具体。</small>
    <button type="submit">添加任务</button>
  </form>
</section>

<section aria-labelledby="list-heading">
  <h2 id="list-heading">当前任务 ({{ tasks | length }})</h2>
  <ul id="task-list">
    {% for task in tasks %}
      <li id="task-{{ task.id }}">
        <span>{{ task.title }}</span>
        <form action="/tasks/{{ task.id }}/delete" method="post" style="display: inline;">
          <button type="submit" aria-label="删除任务：{{ task.title }}">删除</button>
        </form>
      </li>
    {% empty %}
      <li>还没有任务。在上面添加一个！</li>
    {% endfor %}
  </ul>
</section>
{% endblock %}
```

**可访问性特性**：
- `aria-labelledby`：将部分链接到标题（屏幕阅读器公告"添加新任务，区域"）
- `aria-describedby`：将输入链接到提示（屏幕阅读器公告"标题，编辑文本，保持简短和具体"）
- Delete 按钮上的 `aria-label`：屏幕阅读器公告"删除任务：Buy milk"（上下文特定）
- 语义化 `<section>`、`<h2>`、`<ul>` 结构

### 步骤 4：测试无 JS 基线

1. **在浏览器中禁用 JavaScript**：
   - Chrome：DevTools (F12) → 设置 (⚙️) → 调试器 → "禁用 JavaScript"
   - Firefox：about:config → javascript.enabled → false

2. **重新加载 http://localhost:8080/tasks**

3. **添加任务**：
   - 在标题字段中输入 "Buy milk"
   - 点击 "Add Task"
   - **预期**：页面重新加载，"Buy milk" 出现在列表中

4. **删除任务**：
   - 点击 "Buy milk" 旁边的 "Delete"
   - **预期**：页面重新加载，任务被删除

**如果这有效，你就有了一个完全功能、可访问的基线，无需任何 JavaScript。**

**停止并检查**：
- ✅ 添加任务有效（无 JS）
- ✅ 删除任务有效（无 JS）
- ✅ 每次操作时页面重新加载（PRG 模式）
- ✅ 屏幕阅读器正确公告标签（如果可用，使用 NVDA/VoiceOver 测试）

---

## 活动 4：添加 HTMX 渐进式增强

**时间**：40 分钟
**材料**：更新的路由和模板

### 步骤 1：检测 HTMX 请求

在 `Tasks.kt` 中添加辅助函数：

```kotlin
fun ApplicationCall.isHtmx(): Boolean =
    request.headers["HX-Request"]?.equals("true", ignoreCase = true) == true
```

**工作原理**：HTMX 在所有 AJAX 请求中添加 `HX-Request: true` 标头。服务器检查此标头以决定返回完整页面还是片段。

### 步骤 2：更新 POST /tasks 路由

将 `post("/tasks")` 路由替换为：

```kotlin
post("/tasks") {
    val title = call.receiveParameters()["title"].orEmpty().trim()

    // 验证
    if (title.isBlank()) {
        if (call.isHtmx()) {
            val error = """<div id="status" hx-swap-oob="true" role="alert" aria-live="assertive">
                标题是必需的。请输入至少一个字符。
            </div>"""
            return@post call.respondText(error, ContentType.Text.Html, HttpStatusCode.BadRequest)
        } else {
            // 无 JS 路径：重定向并带错误标志（如果需要，在 GET 中处理）
            return@post call.respondRedirect("/tasks?error=required")
        }
    }

    val task = TaskRepository.add(title)

    if (call.isHtmx()) {
        // 返回新任务的 HTML 片段
        val fragment = """<li id="task-${task.id}">
            <span>${task.title}</span>
            <form action="/tasks/${task.id}/delete" method="post" style="display: inline;"
                  hx-post="/tasks/${task.id}/delete"
                  hx-target="#task-${task.id}"
                  hx-swap="outerHTML">
              <button type="submit" aria-label="删除任务：${task.title}">删除</button>
            </form>
        </li>"""

        val status = """<div id="status" hx-swap-oob="true">任务 "${task.title}" 已成功添加。</div>"""

        return@post call.respondText(fragment + status, ContentType.Text.Html, HttpStatusCode.Created)
    }

    call.respondRedirect("/tasks") // 无 JS 后备
}
```

**关键模式**：
- **HTMX 路径**：返回 `<li>` 片段 + OOB 状态消息
- **无 JS 路径**：重定向到 `/tasks`（页面重新加载）
- 两条路径最终处于相同状态（DRY 原则）

### 步骤 3：更新 POST /tasks/{id}/delete 路由

```kotlin
post("/tasks/{id}/delete") {
    val id = call.parameters["id"]?.toIntOrNull()
    val removed = id?.let { TaskRepository.delete(it) } ?: false

    if (call.isHtmx()) {
        val message = if (removed) "任务已删除。" else "无法删除任务。"
        val status = """<div id="status" hx-swap-oob="true">$message</div>"""
        // 返回空内容以触发 outerHTML 交换（如果响应为空则删除 <li>）
        return@post call.respondText(status, ContentType.Text.Html)
    }

    call.respondRedirect("/tasks")
}
```

### 步骤 4：向模板添加 HTMX 属性

更新 `tasks/index.peb`：

```html
{% extends "base.peb" %}

{% block content %}
<h1>任务</h1>

<section aria-labelledby="add-heading">
  <h2 id="add-heading">添加新任务</h2>
  <form action="/tasks" method="post"
        hx-post="/tasks"
        hx-target="#task-list"
        hx-swap="beforeend">
    <label for="title">标题</label>
    <input type="text" id="title" name="title" required
           placeholder="例如：Buy milk" aria-describedby="title-hint">
    <small id="title-hint">保持简短和具体。</small>
    <button type="submit">添加任务</button>
  </form>
</section>

<section aria-labelledby="list-heading">
  <h2 id="list-heading">当前任务 ({{ tasks | length }})</h2>
  <ul id="task-list">
    {% for task in tasks %}
      <li id="task-{{ task.id }}">
        <span>{{ task.title }}</span>
        <form action="/tasks/{{ task.id }}/delete" method="post" style="display: inline;"
              hx-post="/tasks/{{ task.id }}/delete"
              hx-target="#task-{{ task.id }}"
              hx-swap="outerHTML">
          <button type="submit" aria-label="删除任务：{{ task.title }}">删除</button>
        </form>
      </li>
    {% empty %}
      <li>还没有任务。在上面添加一个！</li>
    {% endfor %}
  </ul>
</section>
{% endblock %}
```

**HTMX 属性说明**：
- **添加表单**：
  - `hx-post="/tasks"`：提交时 AJAX POST
  - `hx-target="#task-list"`：将响应插入到任务列表中
  - `hx-swap="beforeend"`：追加（不替换）

- **删除表单**：
  - `hx-post="/tasks/{id}/delete"`：AJAX POST
  - `hx-target="#task-{{ task.id }}"`：目标特定的 `<li>`
  - `hx-swap="outerHTML"`：替换整个 `<li>`（如果响应为空则删除它）

### 步骤 5：测试 HTMX 增强

1. **在浏览器中重新启用 JavaScript**

2. **重新加载 http://localhost:8080/tasks**

3. **添加任务**：
   - 输入 "Buy oat milk"
   - 点击 "Add Task"
   - **预期**：任务立即出现，无页面重新加载
   - **检查 DevTools Network 标签**：看到对 `/tasks` 的 AJAX POST

4. **检查实时区域公告**：
   - 打开浏览器控制台
   - 输入：`document.getElementById('status').textContent`
   - **预期**："Task 'Buy oat milk' added successfully."
   - **使用屏幕阅读器**：应该公告此消息

5. **删除任务**：
   - 点击 "Buy oat milk" 旁边的 "Delete"
   - **预期**：任务立即删除，无页面重新加载

6. **验证无 JS 仍然有效**：
   - 再次禁用 JavaScript
   - 重复添加/删除测试
   - **预期**：两者仍然有效（页面重新加载）

**停止并检查**：
- ✅ HTMX 路径：即时更新，无重新加载
- ✅ 无 JS 路径：仍然通过重定向工作
- ✅ 实时区域更新（使用 DevTools 检查）
- ✅ Network 标签显示 AJAX 请求（启用 JS）与完整页面加载（禁用 JS）

---

## 活动 5：可访问性验证

**时间**：25 分钟
**材料**：键盘、屏幕阅读器（NVDA/VoiceOver）、浏览器 DevTools

### 测试 1：键盘导航

**启用 JavaScript**，重新加载页面，然后：

1. **在页面中 Tab 键导航**：
   - Tab 1：跳过链接出现 → 按 Enter → 焦点跳转到 main
   - Tab 2：标题输入
   - Tab 3：Add Task 按钮
   - Tab 4：第一个 Delete 按钮
   - 继续 Tab 键遍历所有 Delete 按钮

2. **检查焦点指示器**：
   - Pico.css 提供默认焦点轮廓
   - 确保你始终可以看到哪个元素有焦点

3. **使用键盘提交表单**：
   - 聚焦标题输入，输入 "Test task"
   - 按 Enter（提交表单）
   - **预期**：通过 HTMX 添加任务（无重新加载）

**结果**：✅ 所有交互元素可通过键盘访问

### 测试 2：屏幕阅读器测试

**工具**：
- **Windows**：NVDA（免费，https://www.nvaccess.org/）
- **macOS**：VoiceOver（内置，Cmd+F5）
- **Linux**：Orca（在 RHEL 实验室中预装）

**使用 NVDA 测试**（Windows）：
1. 启动 NVDA (Ctrl+Alt+N)
2. 导航到 http://localhost:8080/tasks
3. **倾听**：
   - "Tasks, heading level 1"
   - "Add a new task, heading level 2"
   - "Title, edit, Keep it short and specific"（输入 + 提示）
4. **输入** "NVDA test task" 并按 Enter
5. **倾听**："Task 'NVDA test task' added successfully"（来自实时区域）
6. **Tab 键到 Delete 按钮**：
   - **预期**："Delete task: NVDA test task, button"
7. **按 Space**（激活 Delete）
8. **倾听**："Task deleted"（来自实时区域）

**结果**：✅ 屏幕阅读器公告标签、提示和状态消息

### 测试 3：无 JS 对等性

1. **禁用 JavaScript**（DevTools → 设置 → 禁用 JavaScript）
2. **重新加载页面**
3. **添加任务**："No-JS test"
   - **预期**：页面重新加载，任务出现
4. **删除任务**：
   - **预期**：页面重新加载，任务被删除
5. **与启用 JS 的情况比较**：
   - 功能相同（仅 UX 不同：重新加载 vs. 即时）

**结果**：✅ 实现了无 JS 对等性

### 测试 4：WCAG 快速检查

| 标准 | 测试 | 结果 |
|-----------|------|--------|
| **1.3.1 信息和关系（A 级）** | 检查 HTML：`<label for="title">` 链接到 `<input id="title">` | ☐ 通过 |
| **2.1.1 键盘（A 级）** | 所有功能可通过 Tab、Enter、Space 访问 | ☐ 通过 |
| **2.4.1 绕过块（A 级）** | 跳过链接在焦点时出现，跳转到 main | ☐ 通过 |
| **3.2.2 输入时（A 级）** | 更改输入不会自动提交（仅显式按钮按下） | ☐ 通过 |
| **3.3.2 标签或说明（A 级）** | 所有输入都有 `<label>` 和提示文本 | ☐ 通过 |
| **4.1.3 状态消息（AA 级）** | 实时区域公告添加/删除确认 | ☐ 通过 |

**停止并检查**：
- ✅ 所有 WCAG 测试通过
- ✅ 键盘和屏幕阅读器用户可以完成所有任务
- ✅ 无 JS 路径工作相同

---

## 反思问题

1. **服务器优先 vs. 客户端优先**：如果我们使用 React 而不是服务器渲染的 HTML + HTMX，这个实验会有什么不同？什么会更难？什么会更容易？

2. **渐进式增强**：想象一个用户在慢速 3G 连接上，HTMX 加载失败。我们的实现如何优雅地处理这种情况？

3. **实时区域**：为什么我们对成功消息使用 `aria-live="polite"` 但对错误使用 `aria-live="assertive"`？你什么时候会选择其中一个而不是另一个？

4. **HTMX 权衡**：与完整的 JavaScript 框架相比，HTMX 的局限性是什么？什么时候 HTMX *可能*不合适？

---

## 延伸阅读

**服务器优先架构**
- Gross, C., Haddad, D., & Weibel, A. (2023). *Hypermedia Systems*. <https://hypermedia.systems/>
- Miller, J. (2022). "The case for server-side rendering." *Increment*, 21. <https://increment.com/frontend/case-for-server-side-rendering/>

**渐进式增强**
- GOV.UK 服务手册。"Using Progressive Enhancement." <https://www.gov.uk/service-manual/technology/using-progressive-enhancement>
- Champeon, S. (2003). "Progressive Enhancement and the Future of Web Design." SXSW 演示。

**HTMX 和超媒体**
- HTMX 文档。 <https://htmx.org/docs/>
- Gross, C. (2024). "HTMX: High Power Tools for HTML." *Communications of the ACM*, 67(2), 50-57.

**WCAG 和可访问性**
- W3C (2024). *Web Content Accessibility Guidelines (WCAG) 2.2*. <https://www.w3.org/WAI/WCAG22/quickref/>
- Pickering, H. (2016). *Inclusive Design Patterns*. Smashing Magazine.

---

## 术语表摘要

| 术语 | 定义 | 示例/上下文 |
|------|------------|-----------------|
| **服务器优先** | 服务器生成完整 HTML 页面的架构 | Ktor 渲染 `tasks/index.peb` → 发送完整 HTML 到浏览器 |
| **渐进式增强** | 首先构建基线（HTML），然后添加可选层（CSS、JS） | 表单通过 POST/redirect 工作；HTMX 添加即时更新 |
| **HTMX** | 通过 HTML 属性添加 AJAX 功能的库 | `<form hx-post="/tasks" hx-target="#list">` |
| **PRG（发布-重定向-获取）** | 防止重复表单提交的模式 | POST /tasks → 303 重定向 → GET /tasks |
| **ARIA 实时区域** | 向屏幕阅读器公告动态更改的元素 | `<div role="status" aria-live="polite">` |
| **带外（OOB）交换** | HTMX 更新目标外的元素 | `<div id="status" hx-swap-oob="true">` |
| **语义化 HTML** | 使用传达含义的标签（不仅仅是结构） | `<main>`、`<section>`、`<label>`、`<button>`（不是 `<div onclick>`） |
| **跳过链接** | 跳过重复内容（导航）的链接 | `<a href="#main">跳转到内容</a>` |
| **WCAG 2.2 AA** | Web 可访问性标准（AA 级 = 大多数组织的目标） | GOV.UK、大学、大公司必须遵守 |

---

## Git 提交最佳实践

良好的提交消息对于你的**作品集评估**和**专业实践**至关重要。你的提交讲述了你的开发过程。

### 约定式提交格式

使用此结构进行清晰、可搜索的提交：

```
<type>(<scope>): <short description>

[Optional longer explanation]
```

**示例**：
```bash
# 好的 ✅
git commit -m "feat(tasks): add HTMX progressive enhancement to delete button"
git commit -m "fix(a11y): add aria-label to delete buttons for screen readers"
git commit -m "docs(readme): add setup instructions for Codespaces"
git commit -m "refactor(templates): extract task item to partial template"

# 不好的 ❌
git commit -m "stuff"
git commit -m "fixed it"
git commit -m "update"
git commit -m "changes"
```

### 常见类型

| 类型 | 何时使用 | 示例 |
|------|-------------|---------|
| `feat` | 新功能或能力 | `feat(tasks): add search by title` |
| `fix` | 错误修复 | `fix(validation): prevent blank task titles` |
| `refactor` | 代码重构（无行为更改） | `refactor(store): move CSV logic to TaskStore class` |
| `docs` | 仅文档 | `docs(readme): add IntelliJ setup guide` |
| `style` | 格式化、CSS、无代码更改 | `style(tasks): improve focus indicator contrast` |
| `test` | 添加或修复测试 | `test(store): add TaskStore CRUD tests` |
| `chore` | 构建、依赖项、工具 | `chore(deps): update Ktor to 2.3.11` |

### 范围（可选但有用）

范围 = 代码库的哪一部分更改了：
- `(tasks)` = 任务管理功能
- `(a11y)` = 可访问性改进
- `(htmx)` = HTMX 增强
- `(templates)` = Pebble 模板更改
- `(docs)` = 文档
- `(store)` = 数据存储/持久化

### 为什么这很重要

**对于作品集评估**：
- 展示专业开发实践
- 显示你对更改的理解（不仅仅是"什么"还有"为什么"）
- 在准备证据时轻松找到特定功能

**对于协作**：
- 团队成员一目了然地理解更改
- 易于搜索 git 历史（`git log --grep="feat(a11y)"`）
- 工具可以从提交消息自动生成变更日志

### 第 6 周实验 1 示例提交

对于这个实验，一个好的提交消息应该是：

```bash
git add src/main/kotlin/ src/main/resources/templates/ build.gradle.kts
git commit -m "feat(scaffold): implement server-first task manager with HTMX

- Add Ktor server with Pebble templating
- Implement TaskStore with CSV persistence
- Add dual-mode CRUD routes (HTMX + no-JS)
- Include WCAG 2.2 AA accessibility baseline (skip link, ARIA live region)
- Add progressive enhancement with HTMX for add/delete
- Tested with keyboard navigation and NVDA screen reader

Addresses Week 6 Lab 1 requirements.
WCAG: 2.4.1 (skip link), 4.1.3 (status messages)"
```

**快速版本**（用于较小的更改）：
```bash
git commit -m "feat(wk6-lab1): server-first scaffold with HTMX and WCAG 2.2 AA"
```

---

## 实验检查清单

离开实验室前，确认：

- [ ] **服务器运行**：`./gradlew run` → http://localhost:8080/tasks 加载
- [ ] **无 JS 工作**：禁用 JS 时添加/删除任务（页面重新加载）
- [ ] **HTMX 工作**：启用 JS 时添加/删除任务（无重新加载）
- [ ] **键盘可访问**：Tab 键遍历所有控件，使用 Enter 提交
- [ ] **屏幕阅读器测试**：公告标签、提示和状态消息（NVDA/VoiceOver）
- [ ] **实时区域更新**：检查 DevTools → `#status` 文本在添加/删除后更改
- [ ] **代码已提交**：`git add .`，`git commit -m "wk6-lab1: server-first scaffold with HTMX"`

---

## 下一步

在**第 6 周实验 2**中，你将：
1. 进行同行访谈（需求发现）
2. 记录同意协议（伦理）
3. 从研究见解构建包容性待办事项
4. 为第 9 周评估规划工具

**准备**：
- 阅读 `../../references/consent-pii-faq.md` 和 `../../references/privacy-by-design.md`
- 携带带有工作脚手架（实验 1 代码）的笔记本电脑
- 准备好与同学配对进行访谈

---

**实验作者**：COMP2850 教学团队，利兹大学
**最后更新**：2025-01-14
**许可**：仅限学术使用（不得重新分发）


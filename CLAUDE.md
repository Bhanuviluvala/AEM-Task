# CLAUDE.md — AEM-Task Design System & Codebase Reference

> This document provides comprehensive guidance for working with the `Bhanuviluvala/AEM-Task` Adobe Experience Manager (AEM) project using the Model Context Protocol (MCP) and Figma integration.

---

## Project Overview

This is a standard **AEM Maven Archetype** project targeting **AEM as a Cloud Service (AEMaaCS)**. It follows Adobe's recommended multi-module Maven structure and is built with:

- **Backend:** Java (OSGi bundles, Sling Models, Servlets)
- **Frontend:** SCSS, JavaScript (Webpack via `ui.frontend`)
- **Templates/Components:** HTL (Sightly), XML content definitions
- **Build System:** Maven 3 + Node/npm (frontend)
- **Testing:** JUnit (unit), AEM Testing Clients (integration), Cypress (UI)

---

## Project Structure

```
AEM-Task/
├── core/                    # Java OSGi bundle (Sling Models, Servlets, Services)
├── ui.apps/                 # AEM components, clientlibs, templates (/apps)
├── ui.apps.structure/       # Repository structure package
├── ui.content/              # Sample content using components (/content)
├── ui.config/               # OSGi runmode-specific configurations
├── ui.frontend/             # Frontend build (Webpack, SCSS, JS)
├── ui.tests/                # Cypress UI tests
├── it.tests/                # Java integration tests
├── all/                     # Aggregate content package (embeds all modules)
├── dispatcher/              # AEM Dispatcher configuration
├── .cloudmanager/           # Adobe Cloud Manager pipeline config
├── pom.xml                  # Root Maven POM
└── archetype.properties     # Archetype generation settings
```

---

## 1. Design Token Definitions

AEM projects use **CSS custom properties** and **SCSS variables** for design tokens. These are typically defined in `ui.frontend`.

### Token Location
```
ui.frontend/src/main/webpack/
├── site/
│   ├── _variables.scss       # Global SCSS design tokens
│   ├── _colors.scss          # Color palette
│   ├── _typography.scss      # Font definitions
│   └── _spacing.scss         # Spacing scale
```

### SCSS Token Pattern
```scss
// _variables.scss
$color-primary: #1473e6;
$color-secondary: #2c2c2c;
$color-text: #333333;
$color-background: #ffffff;

$font-family-base: 'Adobe Clean', sans-serif;
$font-size-base: 16px;
$font-size-h1: 2.25rem;
$font-size-h2: 1.75rem;

$spacing-xs: 4px;
$spacing-sm: 8px;
$spacing-md: 16px;
$spacing-lg: 24px;
$spacing-xl: 48px;

$breakpoint-mobile: 768px;
$breakpoint-tablet: 1024px;
$breakpoint-desktop: 1280px;
```

### CSS Custom Properties (Design Tokens for Runtime)
```css
/* site/styles/tokens.css */
:root {
  --color-primary: #1473e6;
  --color-text: #333333;
  --spacing-md: 16px;
  --font-size-base: 16px;
}
```

---

## 2. Component Library

### Component Location
```
ui.apps/src/main/content/jcr_root/apps/aemtask/components/
├── page/                    # Page component (HTL template)
├── text/                    # Text component
├── image/                   # Image component
├── navigation/              # Navigation component
└── ...
```

### Component Architecture
Each component follows a **tri-layer pattern**:

```
components/mycomponent/
├── mycomponent.html         # HTL (Sightly) markup template
├── _cq_dialog/              # Author dialog definition (XML)
│   └── .content.xml
├── _cq_editConfig.xml       # Edit configuration
└── clientlibs/              # Component-scoped CSS/JS (optional)
    ├── css/
    │   └── mycomponent.css
    └── js/
        └── mycomponent.js
```

### HTL Component Pattern
```html
<!-- mycomponent.html -->
<sly data-sly-use.model="com.aemtask.core.models.MyComponent"
     data-sly-test="${model.hasContent}">
  <div class="mycomponent" data-sly-attribute.id="${model.id}">
    <h2 class="mycomponent__title">${model.title @ context='html'}</h2>
    <div class="mycomponent__body">${model.body @ context='html'}</div>
  </div>
</sly>
```

### Sling Model Pattern (Java)
```java
// core/src/main/java/com/aemtask/core/models/MyComponent.java
@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = MyComponent.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class MyComponentImpl implements MyComponent {

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String body;

    @Override
    public String getTitle() { return title; }

    @Override
    public String getBody() { return body; }

    @Override
    public boolean hasContent() { return title != null || body != null; }
}
```

---

## 3. Frameworks & Libraries

| Layer | Technology |
|-------|-----------|
| Backend | Java 11+, OSGi (Apache Felix), Sling, JCR |
| Template | HTL (HTML Template Language / Sightly) |
| Frontend Build | Webpack (via `ui.frontend`) |
| CSS Preprocessor | SCSS (Sass) |
| CSS Methodology | BEM (Block Element Modifier) |
| JS | Vanilla JavaScript / ES6+ modules |
| Build Tool | Maven 3.6+ |
| Package Manager | npm |
| Core Components | Adobe AEM Core Components (WCM) |
| Testing (Unit) | JUnit 5, Mockito |
| Testing (Integration) | AEM Testing Clients |
| Testing (UI) | Cypress |

### Key Maven Dependencies (`pom.xml`)
```xml
<!-- AEM SDK API -->
<dependency>
  <groupId>com.adobe.aem</groupId>
  <artifactId>aem-sdk-api</artifactId>
  <scope>provided</scope>
</dependency>

<!-- AEM Core Components -->
<dependency>
  <groupId>com.adobe.cq</groupId>
  <artifactId>core.wcm.components.core</artifactId>
</dependency>
```

---

## 4. Styling Approach

### CSS Methodology: **BEM**
All styles follow Block-Element-Modifier naming:
```scss
// Block
.card { ... }

// Element
.card__title { ... }
.card__body { ... }
.card__image { ... }

// Modifier
.card--featured { ... }
.card--dark { ... }
```

### Frontend Source Location
```
ui.frontend/src/main/webpack/
├── site/
│   ├── main.scss            # Global entry point
│   ├── _variables.scss      # Design tokens
│   ├── _typography.scss     # Type styles
│   ├── _grid.scss           # Layout grid
│   └── components/          # Component-level styles
│       ├── _navigation.scss
│       ├── _hero.scss
│       └── ...
└── components/
    └── [component-name]/
        └── [component-name].scss
```

### Global Styles Entry (`main.scss`)
```scss
@import 'variables';
@import 'typography';
@import 'grid';
@import 'components/navigation';
@import 'components/hero';
```

### Responsive Design
Breakpoints are defined as SCSS mixins:
```scss
// _mixins.scss
@mixin mobile {
  @media (max-width: #{$breakpoint-mobile}) { @content; }
}
@mixin tablet {
  @media (min-width: #{$breakpoint-mobile + 1}) and (max-width: #{$breakpoint-tablet}) { @content; }
}
@mixin desktop {
  @media (min-width: #{$breakpoint-tablet + 1}) { @content; }
}

// Usage
.card {
  padding: $spacing-md;
  @include mobile { padding: $spacing-sm; }
}
```

---

## 5. ClientLibs

AEM ClientLibs bundle CSS/JS for delivery. They live in `ui.apps`:

```
ui.apps/src/main/content/jcr_root/apps/aemtask/clientlibs/
├── clientlib-base/          # Base styles/scripts loaded on all pages
│   ├── .content.xml         # categories = ["aemtask.base"]
│   ├── css.txt
│   ├── js.txt
│   ├── css/
│   └── js/
└── clientlib-site/          # Site-specific styles
    ├── .content.xml         # categories = ["aemtask.site"]
    ├── css.txt
    └── js.txt
```

### ClientLib `.content.xml` Pattern
```xml
<?xml version="1.0" encoding="UTF-8"?>
<jcr:root xmlns:cq="http://www.day.com/jcr/cq/1.0"
          xmlns:jcr="http://www.jcp.org/jcr/1.0"
          jcr:primaryType="cq:ClientLibraryFolder"
          categories="[aemtask.site]"
          dependencies="[aemtask.base]"/>
```

### Page Template ClientLib Inclusion (HTL)
```html
<!-- page/customheaderlibs.html -->
<sly data-sly-use.clientlib="/libs/granite/sightly/templates/clientlib.html">
  <sly data-sly-call="${clientlib.css @ categories='aemtask.site'}"/>
</sly>

<!-- page/customfooterlibs.html -->
<sly data-sly-use.clientlib="/libs/granite/sightly/templates/clientlib.html">
  <sly data-sly-call="${clientlib.js @ categories='aemtask.site'}"/>
</sly>
```

---

## 6. Icon System

AEM projects typically use one of:

1. **SVG sprite** — a combined SVG file included once in the page
2. **Individual SVG files** — referenced via `<img>` or `<use>`
3. **Font icons** (less common in modern AEM)

### Icon Location
```
ui.apps/src/main/content/jcr_root/apps/aemtask/clientlibs/clientlib-site/
└── resources/
    └── icons/
        ├── icon-arrow.svg
        ├── icon-search.svg
        └── sprite.svg
```

### Icon Usage in HTL
```html
<!-- Via SVG use -->
<svg class="icon icon--search" aria-hidden="true">
  <use href="${'/apps/aemtask/clientlibs/clientlib-site/resources/icons/sprite.svg#icon-search'}"></use>
</svg>
```

---

## 7. Asset Management

### Asset Storage
- Production assets are managed in **AEM Assets DAM** (`/content/dam/`)
- Local dev: placed in `ui.content/src/main/content/jcr_root/content/dam/aemtask/`

### Asset Reference in HTL
```html
<!-- Image component using Sling Model -->
<img src="${model.fileReference}" alt="${model.alt @ context='attribute'}" />

<!-- Or via AEM Core Image Component delegation -->
<sly data-sly-resource="${'image' @ resourceType='core/wcm/components/image/v3/image'}"/>
```

### Adaptive Images
AEM's built-in adaptive image servlet handles responsive image delivery via URL selectors:
```
/content/dam/aemtask/hero.jpg._jcr_content/renditions/cq5dam.web.1280.720.jpeg
```

---

## 8. Build & Deployment

### Build Commands
```bash
# Full build
mvn clean install

# Build + deploy to local AEM author (port 4502)
mvn clean install -PautoInstallSinglePackage

# Deploy to local publish (port 4503)
mvn clean install -PautoInstallSinglePackagePublish

# Deploy only the Java bundle
mvn clean install -PautoInstallBundle

# Deploy a single content package (run from module dir)
mvn clean install -PautoInstallPackage
```

### Frontend Build (inside `ui.frontend/`)
```bash
npm install
npm run build        # Production build
npm run dev          # Development watch mode
```

### Frontend Build Output
Webpack compiles SCSS/JS and the `aem-clientlib-generator` places output into:
```
ui.apps/src/main/content/jcr_root/apps/aemtask/clientlibs/clientlib-site/
```

---

## 9. Testing

### Unit Tests
```bash
mvn clean test
```
Located in: `core/src/test/java/com/aemtask/core/`

### Integration Tests
```bash
mvn clean verify -Plocal
```
Located in: `it.tests/src/main/java/`

### UI Tests (Cypress)
Located in: `ui.tests/`
```bash
cd ui.tests
npm install
npx cypress open   # interactive
npx cypress run    # headless
```

---

## 10. Figma → AEM Integration Guidelines

When translating Figma designs to AEM components, follow these patterns:

### Component Mapping
| Figma Component | AEM Component Path |
|-----------------|-------------------|
| Hero Banner | `apps/aemtask/components/hero` |
| Card | `apps/aemtask/components/card` |
| Navigation | `apps/aemtask/components/navigation` |
| Text/RTE | `apps/aemtask/components/text` |
| Image | `apps/aemtask/components/image` |

### Token Mapping (Figma → SCSS)
| Figma Token | SCSS Variable |
|-------------|--------------|
| `color/primary` | `$color-primary` |
| `color/text/default` | `$color-text` |
| `spacing/md` | `$spacing-md` |
| `typography/heading-1` | `$font-size-h1` |

### Naming Conventions
- **Components:** `kebab-case` for folder/file names (`my-component/`)
- **CSS Classes:** BEM format (`block__element--modifier`)
- **HTL files:** match component folder name (`mycomponent.html`)
- **Java Models:** `PascalCase` matching component name (`MyComponent.java`)
- **ClientLib categories:** `aemtask.[scope]` (e.g., `aemtask.site`, `aemtask.base`)

### Adding a New Component from Figma
1. Create component folder in `ui.apps/.../components/[name]/`
2. Add `_cq_dialog/.content.xml` for author dialog
3. Add `.content.xml` with `jcr:primaryType="cq:Component"`
4. Create `[name].html` HTL template
5. Create Sling Model in `core/.../models/[Name].java`
6. Add SCSS in `ui.frontend/src/main/webpack/site/components/_[name].scss`
7. Import SCSS in `main.scss`

---

## 11. Key File Paths Reference

| Purpose | Path |
|---------|------|
| Root POM | `pom.xml` |
| Java Models | `core/src/main/java/com/aemtask/core/models/` |
| OSGi Services | `core/src/main/java/com/aemtask/core/services/` |
| Servlets | `core/src/main/java/com/aemtask/core/servlets/` |
| AEM Components | `ui.apps/src/main/content/jcr_root/apps/aemtask/components/` |
| ClientLibs | `ui.apps/src/main/content/jcr_root/apps/aemtask/clientlibs/` |
| Templates | `ui.apps/src/main/content/jcr_root/conf/aemtask/settings/wcm/templates/` |
| OSGi Configs | `ui.config/src/main/content/jcr_root/apps/aemtask/osgiconfig/` |
| Sample Content | `ui.content/src/main/content/jcr_root/content/aemtask/` |
| DAM Assets | `ui.content/src/main/content/jcr_root/content/dam/aemtask/` |
| Frontend Source | `ui.frontend/src/main/webpack/` |
| SCSS Variables | `ui.frontend/src/main/webpack/site/_variables.scss` |
| Webpack Config | `ui.frontend/webpack.config.js` |
| Unit Tests | `core/src/test/java/com/aemtask/core/` |
| UI Tests | `ui.tests/src/main/` |

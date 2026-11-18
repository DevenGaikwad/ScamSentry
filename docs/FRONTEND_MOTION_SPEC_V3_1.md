# ScamSentry v3.1 — Cursor, Depth Transitions, Hero Particles, and Animated Diagrams

**Follow-up implementation master prompt for Deven Sachin Gaikwad**  
**Prepared:** 18 November 2026  
**Scope:** Complete the remaining motion details on the existing editorial frontend.  
**Existing repository:** https://github.com/DevenGaikwad/ScamSentry  
**Public frontend inspected:** https://scamsentry-inspect.vercel.app/  
**Visual reference:** https://www.asklionel.tech/

This is a separate, focused follow-up to the existing v3 master prompt and its additive sections. Keep that earlier document unchanged. The owner likes the current frontend and requests completion of specific cursor, transition, hero, and flow-diagram effects. Preserve the accepted layout, typography, theme, logo, wording, routes, creator attribution, privacy behavior, ML engine, Android work, and existing release history. Execute this follow-up after the current project execution finishes and its final state is known.

## 00. Task, priority, and execution boundary

Act as a senior frontend and graphics engineer completing an existing application. Inspect the actual latest repository, identify motion components that already work, and implement the remaining effects as a controlled patch. Do not generate a replacement website, reskin every page, change the navigation structure, introduce another brand identity, or restart the original application task. The desired result is the current ScamSentry interface with the missing visual behaviors completed and visibly demonstrated.

The four required outcomes are: a small four-corner bracket reticle that follows the native pointer over ordinary desktop page space and expands around suitable interactive targets; noticeable geometric depth transitions between public sections; a persistent animated wireframe-and-signal composition behind the homepage title; and richer original flow diagrams with packets that visibly travel along their actual connections. A generic fade, a stationary border around selected links, or an SVG that only pulses one node does not fulfill these requirements.

This follow-up changes the earlier discretionary treatment of these four effects into explicit deliverables under normal-motion, capable-desktop conditions. Preserve system reduced motion, the user's pause setting, accessible navigation, and immediate access to analysis. A documented technical fallback is appropriate when a browser cannot render the richer effect. Do not use the existence of a fallback as a reason to ship only a faint slide on every capable browser.

The current request is to prepare a follow-up prompt while another execution finishes. When executing this document later, wait for that work to reach a completed or explicitly stable handoff state before editing its working tree or promoting a competing deployment. Read-only preparation can happen first. Do not cancel the ongoing task, overwrite uncommitted work, delete its branch, or create a second production project. Record the actual execution baseline instead of assuming the preparation snapshot is still current.

## 01. Understand the four screenshots correctly

The attached screenshots are reference evidence. Their filenames identify the supplied images; they are not assets to paste into ScamSentry. Inspect all four when available. If an execution environment lacks their bytes, use the detailed descriptions here and the public reference; do not block the implementation merely because a transient attachment path is unavailable.

| Supplied image | Dimensions | What it establishes | What to implement |
| --- | --- | --- | --- |
| `image(1).png` | 1917 × 885 | Light homepage, large foreground heading, a small empty four-corner reticle, thin branching trajectories, and wireframe boxes behind the text | Follow-pointer brackets and an original persistent hero network visible in light mode |
| `image(2).png` | 1362 × 422 | Cropped dark close-up of the same hero composition; routes and boxes remain visible around and through headline negative space | Equivalent motion visibility in dark mode without copying the cropped text treatment |
| `image(3).png` | 1917 × 873 | A transition frame containing a receded miniature page, geometric rings, thin rectangular routes, large empty depth space, and the cursor reticle | A staged page-depth transition with visible geometry and meaningful incoming-page continuity |
| `image(4).png` | 1876 × 813 | A ruled selected-item list beside a technical flow diagram with a central node, connected input/output nodes, grid texture, and a small travelling marker | Original ScamSentry master-detail or method diagrams with directional flow and coordinated selected states |

The apparent dotted or broken square around the pointer consists primarily of four separated corner strokes. It is not a circular cursor, a filled box, a second large arrow, or a thick dashed border. Preserve the user's ordinary operating-system arrow and add the reticle as a separate visual layer. The screenshot may not capture the native arrow in the same way a browser screenshot captures DOM content; do not infer that it should be removed.

The cropped dark hero image does not request overflowing or clipped ScamSentry headings. Keep the approved title and its existing responsive wrapping. The transition image is an intermediate frame, not a request to leave the final page permanently tiny or almost invisible. The work diagram is a visual pattern, not permission to copy company names, employer claims, metrics, portraits, awards, or the reference author's professional history.

A still image establishes geometry, hierarchy, and a moment of appearance. It does not establish speed, duration, packet direction, easing, or the full implementation. Use the public source observations below and actual browser interaction to refine those details. Mark inferred choices and new design targets accurately in the implementation report.

## 02. Preparation snapshot of the current application

At preparation, GitHub main pointed to `0e248cc766d58bc97c7dc504bbd03b21b7aa8b57`, with tree `4af39ea26231217396ebb96f43fec9a4650a4639`. The commit records integration of the editorial browser redesign. The browser package was version `0.3.0`, using React/React DOM 19.3.1, TypeScript 6.0.3, and Vite 8.3.8. The connected Vercel project returned a READY production deployment, and the public `scamsentry-inspect.vercel.app` homepage and Learn page were opened successfully during preparation.

These facts are dated observations. They are not instructions to reset the repository, ignore newer work, or redeploy the preparation commit. Fetch current state when execution begins. If the ongoing task has already improved any of the effects described below, retain the better implementation and fill only the remaining gaps. Do not downgrade completed work to match an older audit.

The current source has useful foundations in `web/src/ui.tsx`, `router.tsx`, `preferences.tsx`, `App.tsx`, `Pages.tsx`, and `style.css`. There are existing masked headline reveals, decoding labels, section entrances, preference controls, a reading rail, a signal diagram, and `PointerBrackets`. The new SS mark, complete creator credit, editorial routes, and relevant public hostname are already present. They are preserved scope, not unfinished items to redesign again.

The package currently depends only on React and React DOM at runtime. Prefer a compatible native implementation where it achieves the intended visual result. Native CSS perspective, SVG, Canvas 2D, requestAnimationFrame, and the Web Animations API can cover much of this work. A lazily loaded graphics dependency is permitted only when it materially improves the result and passes licensing, CSP, compatibility, and performance checks. Do not rewrite the application around a different framework to obtain an animation.

## 03. Specific gaps found in the preparation source

`PointerBrackets` currently listens to `pointerover`, searches for `[data-bracket]`, and positions one clipped element around that target. It hides when there is no tagged target and also hides on scroll. It has no free-follow pointer movement mode. Some main navigation links are not tagged. This explains why a user can see occasional target decoration without getting the small reticle that accompanies the pointer across ordinary page space.

The current public route entrance is a short transform from approximately 12 pixels down and scale 0.994 to the resting state. The router pushes history and changes route content immediately. There is no comparable retreating page, geometric corridor, visible middle phase, or approaching miniature destination page. Preserve working native-link semantics and history behavior while adding a real transition coordinator; increasing the existing entrance distance alone is insufficient.

The current homepage places `SignalDiagram` in the section below the hero. The hero itself has a headline, supporting copy, and an action but no wireframe network behind the title. The inspected hero contained an ordinary arrow SVG but no hero scene canvas. Keep the existing below-hero diagram and add the requested background composition in the hero's own stacking context.

`SignalDiagram` currently uses a small set of SVG paths, a dashed-stroke animation, and pulsing nodes. It is already useful and should be enhanced, not discarded. However, one combined path contains disconnected sections, and a generic dash movement does not by itself establish clear travel through every branch. Split meaningful connections into addressable edges, coordinate packet travel with topology, and improve visibility and sequencing where the graphic appears.

The preferences implementation already pauses decorative motion for reduced motion, explicit pause, hidden documents, analysis activity, and focused input. The analyzer also includes input-focus cleanup on unmount. Do not report those protections as missing or remove them. Verify their interaction with the new motion controller and preserve their existing privacy and usability behavior.

## 04. What the public reference source actually shows

The public reference main bundle and stylesheet were retrieved during this preparation. Their hashes matched the earlier reference audit: main JavaScript `f10edb8a5a7d6eecb0a56726af605beb5b9583c448ce122d8b3535f75c062d70` and stylesheet `589bb62a51564676089860bda785243531ffe1959bdc675c654436100e04c1f0`. A separate public depth-scene chunk was also examined. Treat these as evidence about rendering patterns, not reusable licensed application source.

The reference uses a perspective stage near 1100 pixels, separate incoming and outgoing page planes, geometric scene layers, and destination-specific sequences. Its generic timeline is approximately 880 milliseconds. Some custom sequences last roughly 1.5–1.75 seconds and use different motifs: travelling rails, a rotating framed plane, sliced bands, routed markers, or a sequence of receding frames. Therefore, there is no single universal reference transition that can be represented accurately by a tiny scale change.

The examined reference depth module combines CSS-based 3D page objects with an optional WebGL geometry overlay. Its labels are minified and its Three.js imports are aliased; an absence of literal library class names in a string search would not establish an absence of 3D rendering. Some browser observations in this environment did not contain an active canvas despite the source supporting one. Do not promise that the reference runs identical graphics on every device or use that variation as an excuse to omit ScamSentry's required supported-browser result.

Implement original scene geometry and choreography. Do not copy the reference's complete bundle, shaders, private source, cloned personal pages, telemetry, or award graphics. This application does not need the reference's backend. The aim is a faithful technical interpretation of the visible behavior using ScamSentry content and a controlled local rendering system.

## 05. Scope lock and preservation requirements

Keep the current typefaces, font sizes, spacing system, page structure, palette, SS identity, author wording, public routes, and main copy. Add scoped scene layers and a limited amount of explanatory diagram text only where needed for clarity. Do not change the approved hero headline, move the analyzer behind a new onboarding sequence, or turn every page into a full-screen graphics canvas. The existing interface remains the application; motion enhances it.

Keep the complete creator credit: Deven Sachin Gaikwad · B.Tech Electronics and Communication Engineering (AI and ML) · DES Pune University. Keep the current relevant production hostname unless the active preceding execution has already changed it through a verified migration. Do not start another hostname-selection exercise. Retain the new logo rather than returning to a shield or designing another identity in this patch.

Preserve `web/src/core/engine.ts`, `analysis.worker.ts`, the approved model asset, release manifest, model benchmarks, typed output contract, and research/production separation. No retraining, threshold tuning, new dataset, remote inference, or model selector is part of this motion work. The output for the same input must remain unchanged. Keep Android source, SDK choices, package identity, and release binaries intact unless a directly relevant defect independently requires attention.

Preserve same-origin asset delivery, restrictive CSP, inert user URLs, memory-only drafts, reviewed redacted export, no message telemetry, no new account, and no paid dependency or hosting feature. No motion feature may transmit pointer coordinates, message text, report content, screenshots of private UI, or model outputs to a server. The zero-cost and no-permission-email preferences remain applicable through original implementation and existing eligible assets.

## 06. Completion criteria and visible fidelity

The patch must be visibly noticeable on a capable desktop in normal-motion mode. A reviewer should see the reticle move in empty page space, see the hero network behind the title, recognize geometric depth during an ordinary public navigation, and follow at least one packet along a diagram connection without opening developer tools. Installing a library or adding an unused component does not satisfy the task.

Preserve the quieter character of the editorial design. Use wireframes, fine trajectories, small square markers, sparse grids, and deliberate depth. Avoid confetti, bright floating bubbles, an opaque particle blanket, cartoon icons, endless random fireworks, or a generic stock particle preset. The scene should feel related to signal inspection and local analysis, while remaining clearly illustrative.

In normal motion, these four features are required. Under reduced motion, explicit pause, unsuitable input capability, renderer failure, or an identified resource constraint, provide the documented static or reduced equivalent. Record which condition selected the fallback. Do not silently disable the feature on all machines because one browser had a missing canvas, and do not force animation over an explicit user accessibility preference merely to pass a visual demonstration.

Deliver the real source patch, relevant tests, original before/after evidence, updated motion documentation, GitHub integration, a validated preview, and the corresponding Vercel production update when ordinary account access permits. The current request for a prompt does not itself establish that these implementation outcomes have already occurred; the executing session must produce that evidence.

## 07. Cursor appearance specification

Build the pointer reticle as four separate thin L-shaped corners around an empty center. Start with an idle box approximately 32–38 CSS pixels wide and high, corner arms around 8–10 pixels, and a one-pixel stroke. Optical adjustment is acceptable after comparing the screenshot at realistic desktop sizes. Keep the shape square and technical; do not substitute a circular ring or a full solid rectangle. The native pointer remains visible inside or near its center.

Use neutral contrasting ink for each theme. Begin with moderate idle opacity and stronger target opacity, then inspect both light and dark backgrounds. It must be visible without becoming a large black square over letters. Prefer four simple spans or an original SVG with explicit corners over the current complex clipped-border polygon, which is harder to reason about across sizes and may create unexpected joining edges.

Mount exactly one root cursor overlay, fixed to the viewport, outside transformed page planes. Set `pointer-events: none` and `aria-hidden="true"`. It must not become a tab stop, affect layout, intercept text selection, modify pointer hit testing, or appear in the accessibility tree as an interactive control. Define its stacking order relative to content, transition scenery, menus, and native dialogs. Hide it when an open modal makes it inappropriate rather than trying to paint over the top layer.

Do not set `cursor: none` on the document, links, forms, or buttons. Preserve text I-beams, resize cursors, drag affordances, and browser-native select behavior. The extra reticle is decoration; the native cursor communicates the actual interaction mode. Keep keyboard focus styles independent so that a keyboard user receives equivalent clarity without a pointer-following ornament.

## 08. Cursor behavior and state model

Implement distinct hidden, idle-follow, target-snap, and suspended states. Idle-follow is the essential missing behavior: when a mouse moves across a normal blank region of an eligible desktop page, the small reticle follows the pointer. Target-snap expands the corners around the bounds of a suitable interactive element, with a small outer padding. When the pointer leaves that target, the reticle returns to the small pointer-centered state rather than disappearing.

Resolve targets semantically: eligible anchors, enabled buttons, summaries, appropriate custom interactive controls, and explicitly marked editorial targets. Use the nearest meaningful target, not an inner SVG path or text span. Do not snap to disabled controls, inert elements, arbitrary paragraphs, the entire page container, or a huge diagram simply because it contains a link. Preserve an explicit opt-out for editors, selectable code, unusual widgets, or controls whose size makes snapping distracting.

For large targets, cap the snap effect or use a local focus frame so the reticle does not become a viewport-sized border. Keep padding near 5–8 pixels as a starting point. Recalculate bounds when the active target changes, when its layout changes, and when scroll or resize invalidates them. Scrolling may temporarily return to idle-follow, but it must not leave the cursor permanently hidden until another link is entered.

Hide or suspend on pointer exit from the window, window blur, pointer cancellation, hidden document, keyboard-only activity, touch input, explicit motion pause, system reduced motion, forced-colors mode if necessary, and entry into editable areas where decoration harms precision. Restore the correct state on the next eligible mouse movement. Never persist pointer positions or movement history. Current coordinates are short-lived presentation state only.

## 09. Cursor timing, coordinate system, and efficiency

Use viewport coordinates consistently: `clientX`/`clientY` for the pointer and `getBoundingClientRect()` for snap targets. Do not mix page coordinates with fixed positioning, which causes displacement after scroll. The overlay's parent must not establish a transformed containing block. Check browser zoom and scrolling before adjusting offsets; compensate for actual coordinate systems rather than adding arbitrary constants until one screenshot looks correct.

Store current and target values in refs or an imperative controller. Pointer events update the latest target; a coalesced animation-frame callback updates transforms. Do not set React state and rerender the application on every pointermove. Batch layout reads before writes and avoid repeated bounding-rectangle reads for an unchanged target. Stop requesting frames when interpolation has settled and no active target requires an update.

Use time-based damping rather than a fixed percentage per frame. A suitable relationship is `a = 1 - exp(-dt / tau)` and `current += (target - current) * a`, with time units kept consistent. Start around 35–65 milliseconds for ordinary following and 80–140 milliseconds for a target-shape transition. These are calibration targets, not source measurements. The shape should feel attached to the pointer, not trail several hundred pixels behind it on a quick move.

Clamp large time deltas after tab restoration and avoid interpolation across the full screen from an old coordinate. Initialize the first visible reticle at the current pointer location. Treat fine-pointer capability and event pointer type separately on hybrid devices; a touch event must not spawn a mouse reticle. Clean up every listener and frame handle on unmount or policy changes. React development Strict Mode must not produce two overlays or duplicate listeners.

## 10. Cursor verification scenarios

Test movement through several blank coordinates on the homepage and an educational page. The reticle must be visible and its center must follow the intended point within the documented tolerance after settling. Then move over navigation, the primary action, a learning row, a disclosure, and a footer link. Verify expansion around the correct target and return to free following after leaving it. Include a nested SVG or span inside a link to catch incorrect target resolution.

Scroll while the pointer is stationary, resize the viewport while a target is active, navigate while snapped, and leave and re-enter the window. Verify that no orphaned frame remains at the old target. Enter a textarea and a selectable code block: preserve native selection, the insertion caret, and the application's documented suspended behavior. Open a modal and a native select. Do not let a decorative overlay distract from the active control or capture events.

Run the same tests in light and dark themes. Check reduced motion, explicit pause, coarse-pointer emulation, and forced colors. Native OS cursor rendering is not always included in automated browser screenshots, so combine CSS inspection and a manual visual check to establish that the native pointer is retained. A screenshot of four corners alone cannot prove that `cursor: none` was not applied.

Use stable test identifiers or semantic inspection for the overlay's state and bounds. Do not make tests depend on the exact random path of a cursor trail, because no such trail is required. Test actual visible behavior rather than merely asserting that a `.pointer-brackets` element exists. Record a short original demonstration in which the pointer moves over blank space and at least two targets.

## 11. Shared motion architecture and policy integration

Create a small coherent motion layer with separate responsibilities for cursor, public-route transition, hero scene, and diagrams. They may share a scheduler, policy state, theme tokens, seeded geometry utilities, and cleanup conventions. They must not share mutable model state or own the analysis session. Reuse the existing preferences and lifecycle protections instead of adding a second contradictory pause setting.

Distinguish permission to animate from a particular renderer's current running state. Policy reasons include system reduction, explicit pause, document visibility, input focus, analysis activity, route privacy, viewport visibility, and resource availability. Keep those reasons understandable in development diagnostics. Do not add a debug panel to the normal product flow. A user should not have to discover an undocumented “enable effects” switch after selecting normal motion.

Use one owner for route choreography and one owner for final focus. Keep component cleanup idempotent. An animation completing after cancellation must not restore an old route, hide the current page, or restart a loop under a paused policy. Prefer explicit transition identifiers and a controlled state machine over scattered timers in several components. Keep the route controller separate from the worker request identifier.

Favor the existing stack and native APIs. Do not introduce a global state library, server runtime, analytics package, remote renderer, or paid graphics service. If a graphics library is justified, verify its current official documentation, compatible stable version, and license, then load it outside the critical analyzer path. Preserve reproducible installs and the lockfile. The quality bar concerns the rendered behavior and maintainable integration, not the number of packages installed.

## 12. Public page transition: visual contract

Replace the almost imperceptible public-page entrance with a complete depth sequence. A normal public navigation should have a readable departure, a geometric middle, and an arrival. The outgoing page or its permitted public representation recedes; a sparse environment of frames, rings, routed lines, or markers becomes visible; the destination appears as a smaller panel and approaches its normal full-size position. The final page returns exactly to the accepted layout.

The screenshot's miniature About page and surrounding rings are the important relationship. Do not replace this with a black overlay, random dots over a stationary page, or a full-screen white flash. The geometry should establish direction and distance. Use a stable perspective and a limited number of organized elements, with a clearly visible incoming page. Keep the main header and its controls available when practical so that the interface does not feel unresponsive.

Use real ScamSentry public content for the incoming panel. A CSS-transformed public page can provide a crisp miniature without rasterizing the DOM. If an outgoing representation is needed, render an inert presentation from approved static public metadata or retain the previous safe public plane through a controlled component boundary. Do not use a generic blank card as the only page representation, and do not copy the reference person's page content.

The scene is temporary. At completion, remove temporary frames, release inert states, restore normal transforms, complete focus handling, and stop its frame loop. A route entrance should not continuously play while the page is being read. Different destinations can use related geometric motifs, but they must share a consistent visual vocabulary rather than looking like unrelated transition presets.

## 13. Transition timings and destination-specific motifs

For capable desktop public-to-public navigation, start with a total around 950–1250 milliseconds. An occasional more elaborate transition may approach 1400 milliseconds if it remains responsive and justified by the visual result. This follow-up explicitly permits a longer public-page choreography than the earlier conservative small-transition guidance because the owner requests a visible depth effect. Analysis, text editing, cancellation, report actions, and navigation involving private analysis content must remain immediate or use their short safe transition.

| Normalized interval | Visual phase | State obligation |
| --- | --- | --- |
| 0–0.18 | Outgoing plane begins retreating; the scene emerges | Accept the navigation intent and cancel stale analysis work through the existing contract |
| 0.18–0.42 | Outgoing plane moves aside or recedes; geometry is clearly visible | Keep exactly one destination intent authoritative |
| 0.42–0.66 | Incoming public plane becomes visible at reduced scale/depth | Use the chosen single route-commit policy without duplicate history entries |
| 0.66–0.92 | Incoming plane approaches its final position; geometry fades | Prepare normal reading and focus behavior |
| 0.92–1.00 | Settle, remove temporary layers, and stop work | Restore a fully usable final page and report idle state |

These are design proportions, not required exact timestamps. Tune them after watching the actual animation. Avoid spending most of the duration on an empty frame. The middle geometry should remain visible long enough to be recognized, and the incoming page must be distinguishable before it expands.

Provide at least three related motifs for public destinations. For example, Learn can use branching rails and a few travelling squares; Evidence can use a restrained grid-to-frame convergence; Privacy can use boundary rings and a framed passage; Android can use a vertical frame corridor; returning Home can use converging signal routes. Do not animate actual benchmark values, sensitive text, or claims of security through these motifs. On smaller capable screens use a simpler scene and a shorter duration, while preserving the depth relationship.

## 14. Router, history, focus, and interruption integration

Integrate with the current native-link router rather than bypassing it with a click handler attached to every anchor. Preserve modifier clicks, middle clicks, new tabs, external destinations, ordinary downloads, same-page links, and browser back/forward behavior. Decide explicitly when an accepted navigation changes history and when the destination content is committed. Use one policy consistently; do not push at click and again at animation completion.

Represent the transition with a typed state such as idle, departing, scene, arriving, and settling, plus a monotonically increasing transition identifier. A newer navigation invalidates earlier animation callbacks. Repeated quick navigation must settle on the latest accepted destination, with sensible browser history and no stale completion moving focus back to a previous page. Same-destination activation should not spawn another full transition or add redundant history entries.

The existing `Router` calls `session.leave` on navigation, and the existing shell resets scroll and focuses the new heading. Preserve the former behavior and coordinate the latter with the new controller. There should be one focus owner. Do not focus a heading while its ancestor is inert or hidden. For a normal completed public transition, focus the destination heading once without scrolling unexpectedly. For back/forward, retain the application's chosen restoration policy and document it. Never serialize drafts or result objects into history state.

Test cancellation through a second link, Back, Forward, explicit pause, reduced-motion change, document hiding, and renderer failure. Cancel all old animations and timers, then reveal the current destination in its final state. Catch expected cancellation of Web Animations API completion promises so it does not create unhandled rejections. A watchdog slightly longer than the intended transition must restore a usable final page if a completion event never arrives. Navigation must not depend forever on an animation callback.

## 15. Transition privacy boundary and safe fallback

Do not capture, clone, serialize, rasterize, or texture-map the analyzer, user message, result, report preview, or any hidden session state. This prohibition applies even when a screenshot is intended to stay local: there is no need to create additional private-content copies for a decorative effect. Do not use a DOM-to-image package, `foreignObject` serialization of the app, screen capture, or a hidden screenshot canvas as the transition mechanism.

Use a strict allowlist for rich public transitions, such as Home, Learn, educational articles, Evidence, Privacy, and Android when they render approved static material. If either origin or destination is Analyze, a report is open, or a route cannot be classified confidently, use the short safe path. A generic decorative frame may animate without private text, but it must never delay the analyzer becoming usable. A route name alone is not sufficient if future product changes introduce user content on that page; keep this boundary reviewable.

The static fallback should reveal the destination promptly with a short opacity or no-motion change. It must remain a polished page with no stranded black surface, translucent overlay, invisible text, locked scroll, stale menu, or inert content. Renderer loading failure, WebGL context loss, unsupported APIs, and forced-colors mode should choose this safe behavior. An explicit pause or reduced-motion preference is a valid reason to suppress the rich transition; a generic default “off” setting for all users is not.

Keep transient page frames inert and hidden from assistive technology. Avoid duplicate IDs, labels, landmarks, and live regions when two public representations coexist. Remove presentation copies promptly. Do not place a full duplicate accessible page in the DOM merely to produce a visual miniature. Keep the real destination's semantic structure intact and make its final state authoritative.

## 16. Hero scene: required composition and persistence

Add a dedicated wireframe scene inside the existing homepage hero, behind the large headline. Keep the current hero dimensions, text, action placement, support copy, and responsive behavior. The new scene must be visible in the first viewport; enhancing only the diagram in the next section does not fulfill the screenshot request. The below-hero diagram remains a separate explanatory element.

Construct an original central inspection aperture or wireframe core with a handful of peripheral cuboids, fine branching trajectories, and sparse small moving signal markers. Position peripheral nodes on both sides and at varied heights so some geometry remains visible in the headline's whitespace and letter openings. Use perspective and subtle depth or rotation to avoid a flat cluster of unrelated lines. The title stays the dominant foreground element.

Use approximately 5–9 wireframe nodes and 24–60 carefully organized strands as a starting desktop composition, with a small number of visible travelling markers. These are configurable targets, not counts measured from the screenshot. Avoid randomly connecting every node to every other node, which creates a noisy mesh. Prefer a designed fan-in/core/fan-out relationship consistent with conceptual inspection. Do not label the graphic as live traffic or real current detections.

Keep gentle background motion present while the hero is visible in normal motion. A one-time headline reveal followed by a completely empty hero is insufficient. The initial static frame must already be composed; motion can add travel, slow rotation, or small depth changes. Stop or suspend the scene when it is offscreen, the document is hidden, an input or analysis needs resources, or motion is reduced. No opening preloader or full-screen cinematic is required.

## 17. Hero rendering strategy and geometry

Prefer a small original Canvas 2D renderer with projected 3D geometry, or a carefully bounded SVG implementation, if it achieves the wireframe result. A full WebGL dependency is optional rather than a requirement. Use one renderer instance for the hero, not one canvas per cube or path. Reuse arrays and cached geometry; do not allocate large new point collections each frame or pass thousands of changing coordinates through React state.

For projected cuboids, define vertices in scene coordinates, rotate them with a consistent documented matrix order, and project them through a simple perspective camera. A relationship such as `scale = focalLength / (focalLength - z)` is suitable when the chosen camera and z convention make the denominator valid. Translate projected coordinates into the hero's center and apply a bounded perspective scale. Clip or discard vertices that cross a near plane instead of allowing infinite coordinates. Geometry must remain finite under resize.

Connect designed endpoints with cubic curves or sampled 3D strands. Sample curves at a bounded resolution and use time or arc-length lookup for marker travel. Keep the core and endpoint layout stable across ordinary rerenders. Seed any procedural variation so visual tests and development review can reproduce it. The seed controls decorative geometry only and must not be derived from a user's pasted text, identity, model result, or pointer history.

Use CSS-pixel dimensions from a ResizeObserver or equivalent measured container size, and cap device-pixel-ratio scaling around 1.5–2 according to performance. Recreate buffers only when dimensions materially change. Separate the draw coordinate system from backing-store resolution so the scene does not become tiny or blurry on high-density screens. Clamp time deltas after visibility changes. If WebGL is chosen, handle context loss and disposal and retain a static scene fallback with meaningful visual equivalence.

## 18. Hero contrast, layering, and readability

Visibility is an explicit part of this task. Use separate theme tokens for wire lines, wire nodes, particles, and faint grids. Do not apply opacity 0.05 to the whole scene and then claim completion. Begin with a visible but subordinate line treatment, for example a dark-theme neutral equivalent around 0.22–0.35 white over the dark surface and a light-theme medium-gray treatment that remains apparent on off-white. Calibrate the actual composite colors by inspection; alpha alone is not a contrast measurement.

Make peripheral boxes and some trajectories visible in open areas around the text, not only under solid headline glyphs where they are entirely hidden. Larger or brighter moving markers may be used sparingly to clarify travel. Keep background detail away from the main action and supporting paragraph if it reduces readability. Do not add a gray fog or a high-opacity mesh over the letters. Text contrast and the approved type hierarchy remain intact.

Establish an isolated stacking context: page surface, scene, optional subtle mask, foreground hero content, then global cursor outside the hero. The scene must not use negative stacking that sends it behind the body background. It must not capture pointer events, change layout height, overlap navigation as an invisible hit target, or cause horizontal overflow. Inspect at both large and ordinary laptop sizes before tuning only for the 1917-pixel reference screenshot.

For small screens, simplify node count and strand density, reduce or remove parallax, and retain a clear static or low-cost animated composition. Do not hide the entire hero scene on every mobile browser without a concrete reason. Under reduced motion, show the completed static wireframe arrangement with no travelling markers or rotation. The app should still look intentionally designed when no animation runs.

## 19. Flow diagrams and the reference master-detail pattern

The fourth screenshot combines a selected editorial row with a neighboring architecture diagram. Reuse that relationship where it fits existing ScamSentry content. An appropriate location is a method or learning explanation within an existing section, or an expansion of the existing Home/Privacy diagram. Do not create a new fake employment portfolio, add the reference company list, or replace the analyzer's actual evidence panel with a decorative architecture screenshot.

If using a selectable list, keep the existing approved text and route behavior. A deliberate selection updates the adjacent illustrative explanation, highlights the selected row, and animates the relevant paths. On smaller screens, show the active explanation inline in a logical order. Hover may preview a public illustration, but clicking or keyboard activation must provide an equivalent stable state. Do not require hover to discover the meaning or to use a fictional example.

A diagram should have a coherent visual hierarchy: subtle grid, fine connection lines, legible input nodes, a central emphasis region, output nodes, and compact technical labels. Use original geometry and ScamSentry terms. Preserve the neutral palette and use color sparingly for the selected branch. Do not import bright company colors, historical employer facts, or the reference's numerical claims.

Prefer live SVG or a hybrid accessible DOM/SVG construction over a raster image. Keep node labels readable at normal size. The diagram should not become a miniature illegible poster. Provide a caption and text equivalent, especially when the visual communicates actual data flow. A decorative diagram can be hidden from assistive technology only when an equivalent explanation is available nearby and the graphic is not the sole source of information.

## 20. Packet travel, topology, and sequencing

Give each meaningful directed edge its own path and stable identifier. Split disconnected subpaths so a marker never appears to jump between unrelated endpoints. Associate each packet with a route or edge sequence. For a branch, choose a documented visual phase or a small seeded distribution; do not suggest that a real message was sent externally or that the model chose a private route.

Use arc-length-based travel where practical. A simple cached lookup table over a curve can map normalized distance to parameter values and keep perceived speed consistent. A raw Bézier parameter advances at varying speed and can make a packet appear to stop and jump. SVG `getTotalLength`/`getPointAtLength`, a cached geometric approximation, or a supported path-motion approach can work. Measure geometry when paths are ready and invalidate the cache on actual geometry changes, not every frame.

Coordinate three layers of motion: draw the paths once on first reveal; bring in the central node and supporting nodes; then run a restrained packet loop. A brief node emphasis when a packet arrives can communicate connection, but avoid flashing or continuous scale pumping. Keep a few packets visible across distinct branches rather than one long dash on the whole diagram. Use a timing vocabulary related to the rest of the site, with gentle travel cycles around 2.5–5 seconds where appropriate.

If using SVG SMIL motion, explicitly pause and resume its animation timeline; hiding a moving element or pausing CSS alone may not stop SMIL work. If using Web Animations API or a shared frame loop, retain handles and clean up on route changes, preference changes, or unmount. Pause diagrams outside the viewport. Under reduced motion, keep fully drawn paths and directional markers so topology remains clear without movement. Test the actual marker position at separated times, not just the presence of an animation declaration.

## 21. Meaningful diagram placement and truthful data flow

Apply diagrams where they explain the product. Home can show conceptual input-to-local-inspection-to-independent-judgment flow. Privacy can show public asset delivery from the host into the browser, a private in-memory worker boundary, and an explicitly reviewed redacted download. Evidence can show the relationship between the approved historical model, observed message/URL cues, policy interpretation, and limitations, provided the diagram follows the actual engine contract. Learn can illustrate a specific concept such as finding the actual destination host.

Do not imply that the private draft travels from a user node into a cloud, reputation service, remote model, or external reporting system. Public model downloads and private inference are different directions of flow. Use a visible browser boundary where that distinction matters. Do not add a line that makes an export look automatic; reviewed download is an explicit user choice. If a conceptual diagram simplifies implementation order, label it as conceptual rather than presenting it as a literal runtime trace.

Prefer enriching two or three existing diagrams over scattering a different animated chart into every section. At least the hero, existing local-flow explanation, and Privacy or Evidence method area should receive the appropriate improvements. The analyzer itself can keep a quiet static empty-state illustration, with gentle supported decoration only while it is genuinely idle. Stop decorative work during typing and inference. Do not animate the user's message or draw a path that follows extracted private values.

Keep all current privacy and scope copy accurate. Small captions may clarify that an animation is illustrative, but avoid adding implementation jargon to ordinary user actions. No backend endpoint, telemetry event, inbox permission, new dataset, OCR service, or account is needed to animate these diagrams. The same public static assets can support the complete visual enhancement.

## 22. Charts, metrics, and evidence integrity

The owner's mention of charts allows better visual presentation of existing evidence, not new invented statistics. Preserve the historical benchmark, class order, supports, confusion-matrix orientation, model identity, and uncertainty. The existing accessible HTML tables remain available. A restrained heatmap or path-entry effect can accompany them, but do not replace the data with a dashboard of moving counters that implies live protection.

Do not count an F1 score upward from zero, oscillate a probability, animate the risk category through several labels, or convert historical values into a “current threat” gauge. Such effects can make a research result appear more authoritative or dynamic than it is. If bars enter visually, their labels should already contain the final truthful values, and reduced motion should display the complete chart immediately.

Keep class probabilities separate from authenticity claims and keep policy categories separate from model classes. No animation should change inference thresholds or reorder evidence for dramatic effect. Preserve local timing measurements as measurements, not a source of simulated progress. If adding a visual methodology graphic, derive it from the actual approved pipeline and label its simplifications.

The patch should ordinarily leave `benchmark.json`, the manifest, model asset, engine, and analysis worker unchanged. Record their preservation in the implementation evidence. If a visual chart needs a derived layout value, compute it from the checked-in data rather than editing the underlying benchmark. Stronger motion quality is a presentation improvement and must not be described as an improvement to statistical model quality.

## 23. Preferences, responsiveness, and accessible equivalents

Keep `scamsentry.theme` and `scamsentry.motion` as the existing benign preference keys. Do not create a message-history store, per-user motion profile, pointer telemetry, or a remote analytics session. The normal auto setting should enable supported effects when system preferences permit them. Explicit pause and system reduced motion remain authoritative. Explain a static state through the existing controls where needed rather than silently changing the user's setting.

Use different behavior for different capabilities. Fine-pointer desktop receives the reticle and full public-depth composition. Coarse-pointer devices retain native touch behavior and can use a simplified scene without a cursor overlay. Narrow screens get fewer geometric elements and shorter public transitions. Forced colors can omit decorative layers while preserving borders, focus, and semantic content. Hidden documents should stop active drawing and settle any navigation safely.

Maintain one main landmark and useful heading focus after navigation. Scene canvases and decorative SVG layers need appropriate hidden semantics. Diagram text alternatives must remain correct when motion is absent. Avoid live announcements from moving markers, reading rails, or decorative labels. Do not remove normal focus outlines because a mouse target has animated brackets. Preserve browser zoom, text selection, native scrolling, and the existing menu and report focus behavior.

The reduced-motion design must be an intentional final composition, not an empty canvas or an unresolved initial keyframe. On Pause, show fully readable content, static wireframes, completed diagram paths, no travelling dots, and a normal native pointer. If preferences change mid-transition, settle the latest route immediately. Do not leave all application content hidden because a CSS animation was disabled before its final frame ran.

## 24. Suggested source organization and minimal patch boundaries

Keep the existing source understandable. A small `web/src/motion/` directory can hold the new scene controller, policy utilities, cursor controller, hero renderer, diagram geometry, and any shared timing math. Names are illustrative; use the repository's actual conventions. Avoid turning every line or marker into a separate React component, and avoid placing several thousand lines of imperative rendering inside `App.tsx`.

| Current source | Intended focused change |
| --- | --- |
| `ui.tsx` | Upgrade `PointerBrackets`; retain or extract the existing diagram primitives and reading rail |
| `router.tsx` | Integrate one transition coordinator while preserving native link and history semantics |
| `App.tsx` | Add stable scene hosts, delegate route focus/cleanup, and retain the analysis session boundary |
| `Pages.tsx` | Add the hero scene and enrich appropriate existing public diagrams |
| `preferences.tsx` | Reuse current settings and expose necessary policy state without changing stored data scope |
| `style.css` | Add scoped layering, motion, theme contrast, static fallbacks, and responsive rules |
| Frontend tests | Add meaningful motion tests and retain existing analysis/privacy journeys |
| Documentation | Add a concise motion follow-up audit and verified release evidence |

Keep the model engine, analysis worker, release manifest, research code, benchmark, Android source, and existing reports outside the ordinary patch. Small integration edits to analysis presentation are permitted only when necessary to preserve focus or motion suspension; explain them clearly. Do not rewrite proven inference orchestration to simplify a graphics component.

Remove superseded motion CSS selectively. In particular, prevent the old `.public-page` entrance from running on top of the richer route choreography. Preserve its usefulness as an intentional fallback if appropriate. Do not disable all CSS transitions globally in normal mode to resolve one conflict. Keep old screenshot evidence clearly historical and add new evidence rather than erasing the record of the accepted v3 design.

## 25. Controller interfaces and lifecycle invariants

Use narrow interfaces between the application and imperative graphics. A renderer can expose operations such as resize, setTheme, setPolicy, renderStatic, start, pause, and destroy. A transition coordinator can accept a public navigation intent, provide its current phase, and cancel to the latest final route. These interfaces should consume public route metadata and numeric geometry, not the full application state or an `AnalysisSession` object.

Separate geometry generation from drawing. Pure functions can create a seeded hero graph, project points, calculate path samples, or select a route motif. That makes finite-coordinate checks and deterministic review straightforward. The renderer owns canvas/SVG handles and frame scheduling. React owns content, preferences, navigation intent, and lifecycle. Do not let two systems mutate the same text node or claim ownership of the same inline transform without a clear boundary.

Maintain these invariants: one cursor overlay; at most one active public transition; no pending callback from an earlier transition can alter the current page; no frame loop after destruction; no private data passed into scene creation; no scene-driven history writes beyond the chosen navigation policy; and no user action depending indefinitely on a renderer promise. Destroy must be safe to call more than once.

Use cancellation tokens or generation counters and preserve handles for timers, frames, observers, listeners, animations, and optional GPU resources. Cleanup must remove scene layers and restore page state even if an initialization step failed halfway through. A renderer error should be contained at the decorative layer and report a recoverable diagnostic during development. It must not crash the React application or replace a genuine analysis result with an error unrelated to inference.

## 26. Rendering budgets and resource management

Treat all numeric budgets here as targets to measure, not results already achieved. Prefer an incremental initial JavaScript cost below roughly 40 KiB compressed for the follow-up. Keep a heavier optional graphics implementation in a separate lazy chunk and justify its compressed size; a target below roughly 200 KiB for that optional addition is a useful starting point. Do not import a full 3D engine into every route when native rendering meets the objective.

On a normal desktop, aim for smooth interaction near the display's ordinary refresh cadence without making pointer movement trigger full React rerenders. The hero can use a lower redraw rate if its slow wireframe motion remains smooth, while the cursor can respond at display cadence. Record actual timing under stated conditions. Do not claim universal 60 FPS, low battery impact, or physical-phone performance from a desktop automation session.

Avoid unnecessary post-processing, full-screen blur passes, high-resolution render targets, per-node canvases, a permanent offscreen WebGL loop, or hundreds of simultaneous DOM particle elements. Cap backing resolution and scene density. Reuse geometry and typed arrays where useful. Keep shader compilation and expensive imports away from text editing and model initialization. A slow decorative renderer must not consume the resources needed by the local analysis worker.

Stop work when it cannot be seen or is not permitted. Use viewport observation for hero and diagram scenes, document visibility for global scheduling, and current preferences for animation policy. Hidden or paused state should not leave an active draw loop simply rendering the same frame. Dispose optional geometries, materials, renderer contexts, and event subscriptions. Test repeated route changes for growth in canvas count, detached scene nodes, active listeners, and retained animation handles.

## 27. Browser security and local privacy verification

Keep the deployed security headers at least as restrictive as the accepted application unless a narrowly justified compatible change is reviewed. The current static app uses same-origin assets and does not require a remote shader CDN, animation CDN, font service, analytics endpoint, texture service, or screenshot API. Do not add broad `connect-src` allowances, `unsafe-eval`, wildcard script origins, or remote fonts to make a visual dependency convenient.

Verify the production build under its real CSP. Development success does not prove that deployed worker construction, graphics initialization, or style updates will work. If an animation technique conflicts with the policy, choose a compatible native alternative or a precisely scoped approved mechanism. Do not disable the policy to obtain a prettier preview. Original local geometry and static SVG assets should not need remote network access.

Repeat the existing fictional canary workflow after integration. Confirm that no message, extracted hostname, result, pointer coordinate stream, canvas snapshot, or report text appears in requests, URLs, beacons, error reporting, or storage. Canvas content is decorative public geometry only. Check that no new service worker, application cache store, message database, or session serialization was introduced. Preserve the strict preference allowlist.

Keep pasted URLs inert. A moving diagram packet must not trigger a destination request. No pointer effect should log browsing behavior. Do not create heatmap analytics or a tracking session in the name of evaluating the cursor. Use local fictional test traces and discard unnecessary high-frequency data. The follow-up improves visual communication while retaining the same local trust boundary.

## 28. Cursor automated and manual acceptance tests

Add a production-build test that moves a mouse to at least three blank positions, waits a bounded settle interval, and checks a visible idle reticle near each point. Use a tolerance appropriate to the selected damping and tested device scale. Then move over a real navigation link and check its snap bounds against the target rectangle plus the configured padding. Move away again and verify that the shape returns to the small free-follow state.

Exercise nested targets, scrolling, resizing, route change, window-exit simulation where supported, and unmount/remount. Test that disabled or inert controls are not selected. Verify that an editable textarea receives the application's suspended decoration policy while retaining normal keyboard and selection behavior. Ensure there is exactly one cursor layer after repeated public navigations and after development Strict Mode lifecycle checks.

Test fine versus coarse pointer capability and actual pointer type. Verify absence under reduced motion, explicit pause, and forced colors according to the documented design. Check both themes. Assert that the overlay is noninteractive and accessibility-hidden, that normal controls remain clickable, and that neither global nor control CSS hides the native cursor. Use a manual visual check for native arrow presence where screenshots omit it.

Capture a short motion demonstration using original ScamSentry content. Include blank-space movement, snapping to two targets, and return to idle-follow. An element-existence test or a screenshot with no movement is insufficient proof. Avoid extremely tight timing assertions that fail under ordinary CI scheduling; assert the correct settled state and bounded behavior, then review a real-time recording for smoothness.

## 29. Transition tests for correctness and actual choreography

Test at least three ordinary public route pairs with different motifs, plus return to Home. Confirm that navigation triggers a real multi-stage scene: departure, visible geometry, an incoming miniature, and final normal layout. Use phase attributes or controller state that reflect real rendering, not flags that claim progress while nothing changes visually. Inspect transforms or scene geometry at separated moments, and capture representative frames for review.

Test rapid navigation such as Home to Learn to Evidence, Back during an arrival, Forward after cancellation, repeated same-route activation, and a keyboard-triggered link. The latest accepted destination must win without duplicate commits, stuck overlays, invisible content, or delayed focus to an old heading. Preserve modifier and middle-click behavior. Keep direct route reload and unknown-route handling correct on the deployed static host.

Test private boundaries explicitly. Navigation to or from Analyze must take the documented safe path and never create a page texture or private ghost. With a fictional canary present, inspect temporary scene markup and any permitted public representation for that string. Open and close the redacted dialog and navigate through the browser as appropriate; private content must not be copied into decorative frames. The analysis cancellation and stale-worker tests must continue to pass.

Test Pause, system reduction, document visibility change, missing optional graphics chunk, and a simulated renderer failure during each major phase. The latest destination must become usable within the fallback bound. No uncaught rejection should result from canceling an animation. A test-only injected clock or scheduler can make intermediate capture deterministic; do not ship an undocumented production mode that bypasses safety or changes analysis behavior for screenshots.

## 30. Hero, diagram, and chart test plan

Verify that the hero scene is inside the hero rather than only below it, has nonzero rendered dimensions, and contains visible geometry in both themes. Use a fixed seed and stable viewport to capture its static composition. Under normal motion, compare separated frames or sampled marker positions to establish actual movement. A nonempty canvas element alone does not prove useful rendering; a blank or near-transparent canvas fails the visual objective.

Test resize, a narrow viewport, high pixel ratio, offscreen suspension, document hiding, pause/resume, and component destruction. Check that projected coordinates remain finite and that near-plane handling cannot produce an enormous line across the page. Verify that a failed renderer falls back to the completed static arrangement. Track the number of active scene canvases through repeated navigations and reject unexplained growth.

For diagrams, validate graph endpoints, path identifiers, and route continuity. Verify that markers travel on actual edges, arrive at the intended nodes, and do not teleport across disconnected subpaths. Exercise selection changes while a path sequence is running. Under reduced motion, check completed paths, stable directional information, captions, and legible labels. Where a diagram conveys actual local data flow, compare it with the reviewed privacy and engine description.

Retain evidence tables and benchmark assertions. A heatmap must preserve matrix orientation and values, and the text alternative must remain accessible. Do not add a test that merely checks an animated count reaches a fabricated target. The new tests protect rendering correctness and information integrity; the existing model tests continue to protect inference. Treat those as distinct evidence.

## 31. Visual review and evidence capture

Review at approximately 1363 × 936 for continuity with the live inspection, at a wider desktop size near the supplied full screenshots, and at 390-pixel and 320-pixel phone widths. Inspect both themes and reduced motion. Use the supplied images to assess the relationship between text, geometry, miniature page, and diagram; do not compare copied text or portraits. Preserve the current ScamSentry layout instead of forcing it to match a differently proportioned crop.

Collect a concise original evidence set: before/after hero stills, cursor idle and snap frames, transition departure/middle/arrival frames, an animated-flow still, reduced-motion equivalents, and one short real-time recording. Use fictional content. Keep recordings free of authentication pages, private browser tabs, tokens, real messages, and unrelated desktop material. Reference screenshots supplied by the owner need not be committed to the public repository; original ScamSentry demonstrations are sufficient.

Assess visibility at normal viewing scale. Wires that can be detected only by enlarging a screenshot or increasing display brightness are too faint for the stated requirement. Conversely, particles that compete with the heading, cause visual vibration, or conceal body text are too strong. Calibrate line weights, opacity, speed, and density in the actual scene. Review a real-time recording as well as slowed diagnostic frames so that the result is not optimized only for a paused image.

Document evidence provenance: source commit, route, theme, viewport, browser, motion preference, renderer mode, and date. Label browser emulation and software rendering accurately. Do not describe a desktop viewport test as physical Android evidence. No screenshot proves an entire animation; use the recording and behavioral tests to support motion claims. Keep evidence useful and compact rather than committing an unexplained gallery of nearly identical frames.

## 32. Regression checks and reproducible validation

Run the current frontend scripts from `web/`: reproducible install as needed, type checking, linting, unit/parity tests, production build, and the relevant Playwright suites. At preparation, the build script also ran postbuild and asset-verification scripts; preserve those checks rather than replacing the build with a simpler command that skips them. Use the repository's actual current scripts at execution time.

Maintain meaningful analysis regression coverage: real worker output, Unicode-aware 4000-code-point limit, Clear, editing invalidation, cancel/retry, unavailable or corrupt model, inert URL display, navigation away, report redaction, preference allowlist, and no message transmission. Do not weaken these tests because a scene temporarily obscures a control. Fix the scene or its timing and focus integration. A visual enhancement is not allowed to regress the core workflow.

Record hashes or exact diffs for intended-unchanged scientific artifacts and relevant analysis files. Distinguish source formatting from semantic changes if anything unexpectedly differs. This patch does not ordinarily require retraining, Android rebuilding, emulator reinstallation, or rerunning unrelated historical research experiments. Re-run broader checks only when a real integration change or new failure justifies them. Preserve existing cross-runtime evidence as history.

If the local environment cannot run a browser suite, use the supported CI environment and identify which checks ran where. Do not report an environment failure as a pass. Stop repeating successful expensive checks without a new reason. Once the relevant gates pass, proceed to the concrete source and deployment release. Keep all validation claims attached to the actual final artifact rather than an earlier branch snapshot.

## 33. GitHub integration and Vercel deployment after current work finishes

At execution start, inspect the current worktree, branch, open changes, active checks, main branch, and deployment activity. Let the preceding task finish as instructed. Use its final state as the baseline and record the commit and deployed version. Do not reset to `0e248cc...` merely because this prompt names it. Preserve any newer accessibility, deployment, branding, or analysis fixes that landed while this follow-up was being prepared.

Use the existing repository and an isolated descriptive branch such as `feat/motion-polish-v3-1`, unless an appropriate continuation branch already exists. Commit the actual source, relevant tests, lockfile if changed, and concise evidence documentation. Keep the old master prompt unchanged and retain completed v3 work. Integrate through the repository's permitted workflow after the required checks. Do not create another ScamSentry repository or another Vercel production project.

Push the source first, obtain the corresponding preview through the existing Git/deployment workflow, and open it. Verify the four motion deliverables on the deployed preview as well as a representative fictional analysis journey. Check CSP, route reloads, worker/model loading, resource paths, and network privacy. If the existing Git integration already triggered a build, inspect it rather than launching redundant deployments.

Deploy or promote the validated final source to the existing production project when normal access permits. Keep the current relevant public hostname and security settings. Verify the production source identity and repeat the concise smoke test, including visible cursor behavior, one full public transition, hero motion, a packet-flow example, reduced motion, and analysis. Use the repository's normal versioning policy for a frontend patch; do not change the model identifier or claim a new Android binary. No paid hosting upgrade or domain purchase is part of this task.

## 34. Release gates, rollback, and remaining blockers

Treat these as release gates: visible free-follow cursor in the intended desktop mode; real depth choreography between public pages; a visible persistent hero scene; readable packets following diagram edges; correct reduced-motion and pause behavior; unchanged scientific output; no private-data copies or transmission; native navigation and focus preserved; no stuck overlays; and a verified deployed artifact corresponding to the source change.

Retain the previous healthy deployment as a recovery point. A rollback is appropriate for broken analysis, input transmission, accidental private capture, persistent unusable overlays, worker/model failure, or a critical accessibility regression. A small decorative alignment issue normally calls for a focused fix. Do not delete existing deployment history, reset shared branches, or remove the application to resolve a scene defect.

If a new graphics layer is the cause, prefer disabling that layer through a controlled fallback while restoring a healthy app, then fix it on the feature branch. Do not keep promoting the same failing artifact or weaken security to make it run. Verify the restored production state and retain the evidence needed to understand the problem without collecting private messages. Keep the model and Android release independent of the visual rollback.

If platform access, required review, or account authentication blocks the final release, leave the completed source and concrete preview or pull request ready for review. State the exact blocker and the smallest remaining step. Do not claim production completion from a local build, and do not ask for vague permission after doing only preliminary planning. All ordinary authorized implementation and verification should already be complete before reporting a final external blocker.

## 35. Acceptance matrix for the four requested improvements

| Requirement | Normal capable desktop | Reduced or constrained mode | Evidence |
| --- | --- | --- | --- |
| Idle cursor reticle | Four corners follow the native pointer over blank space | Native pointer remains; reticle can be omitted | Multiple-point movement test and recording |
| Target snapping | Corners expand around the correct semantic target and return to idle | Existing focus and hover affordances remain | Bounds test and nested-target example |
| Page-depth transition | Departure, geometric middle, miniature destination, final approach | Immediate static or short safe route change | Phase frames, route test, real-time recording |
| Hero network | Wireframe core, peripheral boxes, strands, and restrained marker travel behind title | Completed static composition or simpler local renderer | Light/dark hero captures and movement evidence |
| Flow diagrams | Directional packets on individual edges, coordinated node/path sequence | Completed paths with meaningful directional information | Topology assertions and separated-time samples |
| Local privacy | No private DOM textures, message uploads, URL fetching, or pointer telemetry | Same guarantees | Canary/network/storage checks |
| Existing design | Current typography, layout, SS mark, copy, attribution, routes remain | Same accepted layout | Focused diff and matched screenshots |
| Release | Integrated source and tested existing Vercel project | Exact blocker recorded if genuinely external | Commit, preview, production identity and smoke test |

Passing tests must correspond to visible behavior. A controller phase attribute without a rendered scene fails the transition requirement. A canvas with no visible geometry fails the hero requirement. A bracket only around rare tagged links fails the free-follow requirement. A pulsing central square without packet travel does not complete the diagram requirement. These distinctions address the specific gaps identified by the owner.

Review the accepted interface alongside the new evidence. If the patch improves motion but changes the overall design, breaks ordinary text selection, makes the menu hard to use, or hides uncertainty, it fails scope preservation. The completion report should be brief and concrete, with supporting detail in the repository rather than superlatives in chat.

## 36. Execution sequence and practical work plan

First establish the final baseline after the current execution. Audit what is already implemented, record its state, and create the isolated continuation branch. Read the existing motion preferences, cursor component, router, shell focus effect, diagrams, and tests. Confirm that the approved analysis engine and current branding are boundaries to preserve. Do not begin by scaffolding a new application or installing a graphics stack.

Next complete cursor behavior and a static hero scene. Verify geometry, layering, pointer semantics, and readability before adding continuous motion. Build the route coordinator with a simple but real depth sequence, then add destination motifs and interruption handling. Enhance the existing diagrams with individual edges, meaningful packet travel, and a text equivalent. Integrate policy suspension throughout rather than trying to bolt it on after every scene is complete.

Then tune real-time visual quality in both themes and representative sizes. Confirm that effects are visible, navigation remains usable, and the scene feels consistent with the current editorial design. Record deterministic evidence and run the focused regressions. Use a limited number of meaningful adjustments; do not endlessly add more particles, colors, or page effects once the requested behavior is clear.

Finally integrate the validated source, verify the preview, complete the production update, and document the exact outcome. Keep a concise continuation checkpoint if an external step remains. The owner should be able to see the four changes, inspect their code, use the app normally, and understand the evidence without reconstructing this conversation. Maintain the existing zero-cost deployment and no-permission-email preferences throughout.

## 37. Primary reference ledger and final execution instruction

The four supplied screenshots were inspected at their actual dimensions recorded above. The public reference homepage and About navigation, plus the current ScamSentry homepage and Learn navigation, were examined in the browser on 18 November 2026. Browser observations and source observations are distinguished in this prompt. The current public reference did not display an active canvas in every inspected stable state; the owner's screenshots remain evidence of the intended wireframe and depth compositions.

Public reference assets examined include `/assets/index-C_HCGAYd.js`, `/assets/styles-CeKekOnG.css`, `/assets/page-depth-scene-FNLXqWGz.js`, and `/assets/tunnel-JQViKKP1.js`. The depth chunk's SHA-256 was `40ce765d5ab22a962bb25f69005121b1c41cf9e0b3f37aa93d2a71be0adc9dcb`; the tunnel chunk's SHA-256 was `cbbb812539b97b3ebee14242b38ffc09b78ae66c7055efb90018f00584a6b97e`. These identify examined source evidence. They do not authorize redistributing the assets or make copying them necessary.

Useful implementation references are the current official documentation for the APIs actually chosen, including `https://developer.mozilla.org/en-US/docs/Web/API/Window/requestAnimationFrame`, `https://developer.mozilla.org/en-US/docs/Web/API/Window/cancelAnimationFrame`, `https://developer.mozilla.org/en-US/docs/Web/API/Element/animate`, `https://developer.mozilla.org/en-US/docs/Web/API/Animation/cancel`, and `https://developer.mozilla.org/en-US/docs/Web/API/HTMLCanvasElement/webglcontextlost_event`. Verify version-specific package APIs against their official documentation if a dependency is introduced. Use original code and preserve applicable licenses.

Execute this as a focused continuation after the current task finishes. Retain the accepted ScamSentry application and complete the pointer reticle, visibly staged public transitions, wireframe hero, and animated diagrams according to the requirements above. Deliver the working implementation, meaningful verification, corresponding GitHub update, and verified Vercel release when access permits. Lead the final handover with what was actually delivered, link the concrete result, and identify only real remaining limitations. Do not stop at another proposal or claim completion based on static screenshots alone.

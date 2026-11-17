import { test, expect } from '@playwright/test'
import { mkdirSync, writeFileSync } from 'node:fs'
import { join } from 'node:path'
declare global { interface Window { __scamLab: { lcp: number; cls: number; events: number[] } } }
test('matched baseline and editorial cold/warm lab measurements', async ({ browser }, info) => {
  test.setTimeout(120000)
  // The BASE_URL suite verifies deployment workflows; the matched lab pair uses identical local servers.
  if (process.env.BASE_URL) {
    const context = await browser.newContext(); const page = await context.newPage()
    await page.goto(process.env.BASE_URL); await expect(page.locator('h1')).toBeVisible(); await context.close()
    return
  }
  const values: object[] = []
  for (const [variant, url] of [['baseline-526f6c0', 'http://127.0.0.1:4174/'], ['editorial', 'http://127.0.0.1:4173/']]) {
    for (let repeat = 0; repeat < 3; repeat++) {
      const context = await browser.newContext({ viewport: { width: 1363, height: 936 }, deviceScaleFactor: 1, reducedMotion: 'no-preference' })
      await context.addInitScript(() => {
        window.__scamLab = { lcp: 0, cls: 0, events: [] }
        if (PerformanceObserver.supportedEntryTypes.includes('largest-contentful-paint')) new PerformanceObserver(list => { for (const entry of list.getEntries()) window.__scamLab.lcp = entry.startTime }).observe({ type: 'largest-contentful-paint', buffered: true })
        if (PerformanceObserver.supportedEntryTypes.includes('layout-shift')) new PerformanceObserver(list => { for (const entry of list.getEntries() as (PerformanceEntry & { hadRecentInput: boolean; value: number })[]) if (!entry.hadRecentInput) window.__scamLab.cls += entry.value }).observe({ type: 'layout-shift', buffered: true })
        if (PerformanceObserver.supportedEntryTypes.includes('event')) new PerformanceObserver(list => { for (const entry of list.getEntries()) window.__scamLab.events.push(entry.duration) }).observe({ type: 'event', buffered: true, durationThreshold: 16 } as PerformanceObserverInit)
      })
      const page = await context.newPage()
      for (const cache of ['cold', 'warm']) {
        await page.goto(url); await expect(page.locator('h1')).toBeVisible(); await page.evaluate(() => document.fonts.ready)
        await page.evaluate(() => new Promise<void>(resolve => requestAnimationFrame(() => requestAnimationFrame(() => resolve()))))
        if (variant === 'editorial') await page.getByRole('navigation', { name: 'Main navigation', exact: true }).getByRole('link', { name: 'Learn', exact: true }).click()
        else await page.getByRole('button', { name: 'Learn', exact: true }).click()
        await expect(page.locator('h1')).toBeVisible()
        const metrics = await page.evaluate(() => ({ ...window.__scamLab, navigation: (performance.getEntriesByType('navigation')[0] as PerformanceNavigationTiming).toJSON(), resources: performance.getEntriesByType('resource').map(entry => { const resource = entry as PerformanceResourceTiming; return { path: new URL(resource.name).pathname, transfer: resource.transferSize, encoded: resource.encodedBodySize, duration: resource.duration } }), supported: PerformanceObserver.supportedEntryTypes }))
        expect(metrics.resources.some(resource => resource.path.includes('/model/'))).toBe(false)
        values.push({ variant, repeat, cache, ...metrics })
      }
      await context.close()
    }
  }
  const directory = join(process.cwd(), '../assets/evidence/v3/browser'); mkdirSync(directory, { recursive: true })
  writeFileSync(join(directory, `${info.project.name}_lab-performance.json`), JSON.stringify({ source_commit: process.env.GITHUB_SHA ?? 'local', browser: await browser.version(), platform: process.platform, viewport: '1363x936, DPR 1', network: 'identical local gzip-enabled static servers, no CPU or network throttling', samples: '3 cold fresh contexts and 3 warm navigations per variant', interaction: 'Learn navigation; event duration samples are lab observations, not field INP', limitation: 'Firefox may not expose LCP, layout-shift or event timing; unsupported metrics remain zero and are not a passing field claim', values }, null, 2))
})

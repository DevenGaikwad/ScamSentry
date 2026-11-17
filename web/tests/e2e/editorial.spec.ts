import { test, expect } from '@playwright/test'
import type { Page } from '@playwright/test'
import { readFileSync, mkdirSync, writeFileSync } from 'node:fs'
import { join } from 'node:path'
import benchmark from '../../src/benchmark.json' with { type: 'json' }

const evidence = join(process.cwd(), '../assets/evidence/v3/browser')
const canary = 'FICTIONAL_PRIVATE_CANARY_V3_8KQ'
const ordinary = "Let's meet in the library after class. Please bring the project notes."
const credit = 'Deven Sachin Gaikwad · B.Tech Electronics and Communication Engineering (AI and ML) · DES Pune University.'
const capture = async (page: Page, name: string) => { mkdirSync(evidence, { recursive: true }); await page.screenshot({ path: join(evidence, `${test.info().project.name}_${name}.png`), fullPage: true, animations: 'disabled' }) }
async function menu(page: Page) { const button = page.getByRole('button', { name: 'Menu', exact: true }); if (await button.isVisible()) await button.click() }
async function nav(page: Page, name: string) { await menu(page); const scope = await page.getByRole('dialog').isVisible() ? page.getByRole('dialog') : page.getByRole('navigation', { name: 'Main navigation', exact: true }); await scope.getByRole('link', { name, exact: true }).click() }
async function preferences(page: Page, theme: 'light' | 'dark') { await menu(page); await page.getByRole('combobox', { name: 'Theme', exact: true }).filter({ visible: true }).selectOption(theme); const close = page.getByRole('button', { name: 'Close menu' }); if (await close.isVisible()) await close.click() }

test('public routes, native history, not-found and exact creator credit', async ({ page, context }) => {
  await page.goto('/')
  await expect(page.getByRole('heading', { level: 1 })).toHaveAccessibleName('A second look. Before you act.')
  await page.getByRole('link', { name: 'Inspect a message', exact: true }).click()
  await page.getByLabel('Your message').fill(canary)
  await nav(page, 'Learn')
  await expect(page).toHaveURL(/\/learn$/)
  await page.getByRole('link', { name: /Find the actual host/ }).click()
  await expect(page).toHaveURL(/\/learn\/actual-host$/)
  await expect(page.getByRole('heading', { level: 1 })).toHaveText('Read the destination.')
  await page.reload(); await expect(page.getByRole('heading', { level: 1 })).toHaveText('Read the destination.')
  await nav(page, 'Analyze'); await expect(page.getByLabel('Your message')).toHaveValue('')
  await page.getByLabel('Your message').fill(canary)
  await nav(page, 'Privacy'); await page.goBack(); await expect(page.getByLabel('Your message')).toHaveValue(canary)
  await page.goForward(); await expect(page).toHaveURL(/\/privacy$/)
  expect(await page.evaluate(() => history.state)).toBeNull()
  expect(page.url()).not.toContain(canary)
  const tab = await context.newPage(); await tab.goto('/android'); await expect(tab.getByRole('heading', { level: 1 })).toHaveText('A second look.On Android.'); await tab.close()
  await page.goto('/evidence')
  const matrix = page.getByRole('table', { name: `Historical test confusion matrix — ${benchmark.records} records`, exact: true })
  expect(await matrix.locator('tbody tr').allTextContents()).toEqual(benchmark.confusion_matrix.map((row, i) => ['Ordinary', 'Promotion / spam', 'Smishing-like'][i] + row.join('') + row.reduce((a, b) => a + b, 0)))
  await expect(page.getByText(/Errors in that subset: 8\/767/)).toBeVisible()
  await page.goto('/missing-chapter'); await expect(page.getByRole('heading', { level: 1 })).toHaveText('This pageisn’t here.')
  await expect(page.locator('footer .creator-credit')).toHaveText(credit)
  await expect(page.locator('h1')).toHaveCount(1)
  await capture(page, 'not-found')
})

test('cancel, route-away and model retry never accept stale work', async ({ page }) => {
  let release: () => void = () => {}; let start: () => void = () => {}
  const gate = new Promise<void>(resolve => release = resolve), requested = new Promise<void>(resolve => start = resolve)
  await page.route('**/model/model-*.json', async route => { start(); await gate; await route.continue().catch(() => {}) })
  await page.goto('/analyze'); await page.getByLabel('Your message').fill('Send your OTP immediately.')
  await page.getByRole('button', { name: 'Analyze message', exact: true }).click(); await requested
  await page.getByRole('button', { name: 'Cancel', exact: true }).click()
  await expect(page.getByRole('status')).toContainText('cancelled')
  release(); await page.unroute('**/model/model-*.json')
  await page.getByLabel('Your message').fill(ordinary)
  await page.getByRole('button', { name: 'Analyze message', exact: true }).click()
  await expect(page.getByRole('heading', { name: 'Lower observed risk', exact: true })).toBeVisible()
  await page.reload()
  let releaseSecond: () => void = () => {}; let startSecond: () => void = () => {}
  const gateSecond = new Promise<void>(resolve => releaseSecond = resolve), requestedSecond = new Promise<void>(resolve => startSecond = resolve)
  await page.route('**/model/model-*.json', async route => { startSecond(); await gateSecond; await route.continue().catch(() => {}) })
  await page.getByLabel('Your message').fill('Send your password to confirm access.')
  await page.getByRole('button', { name: 'Analyze message', exact: true }).click(); await requestedSecond
  await nav(page, 'Learn'); releaseSecond(); await page.unroute('**/model/model-*.json')
  await nav(page, 'Analyze'); await expect(page.getByLabel('Your message')).toHaveValue('Send your password to confirm access.')
  await expect(page.getByRole('heading', { name: 'Higher risk', exact: true })).toHaveCount(0)
  await expect(page.getByRole('button', { name: 'Analyze message', exact: true })).toBeEnabled()
})

test('corrupt asset recovers without fabricated class estimates', async ({ page }) => {
  await page.route('**/model/model-*.json', route => route.fulfill({ status: 200, contentType: 'application/json', body: '{"wrong":true}' }))
  await page.goto('/analyze'); await page.getByLabel('Your message').fill(ordinary)
  await page.getByRole('button', { name: 'Analyze message', exact: true }).click()
  await expect(page.getByRole('heading', { name: 'Unable to assess reliably', exact: true })).toBeVisible()
  await page.getByText('Inspect technical details', { exact: true }).click(); await expect(page.locator('.estimate-table')).toHaveCount(0)
  await capture(page, 'model-unavailable')
  await page.unroute('**/model/model-*.json')
  await page.getByRole('button', { name: 'Analyze message', exact: true }).click()
  await expect(page.getByRole('heading', { name: 'Lower observed risk', exact: true })).toBeVisible()
})

test('redacted file bytes equal preview; modal traps focus, restores focus and closes without downloads', async ({ page }) => {
  let downloads = 0; page.on('download', () => downloads++)
  await page.goto('/analyze'); await page.getByLabel('Your message').fill(`Send your OTP to confirm access. https://bank.example@review.test/private ${canary} account=123456789 email=fictional@example.test phone=9876543210`)
  await page.getByRole('button', { name: 'Analyze message', exact: true }).click()
  await expect(page.getByRole('heading', { name: 'Higher risk', exact: true })).toBeVisible()
  await page.getByRole('button', { name: 'Redacted report', exact: true }).click()
  const dialog = page.getByRole('dialog'); await expect(dialog).toBeVisible()
  const preview = await page.getByTestId('report-preview').innerText()
  for (const value of [canary, 'review.test', '123456789', 'fictional@example.test', '9876543210']) expect(preview).not.toContain(value)
  for (let i = 0; i < 8; i++) { await page.keyboard.press('Tab'); expect(await page.evaluate(() => !!document.activeElement?.closest('dialog'))).toBe(true) }
  await capture(page, 'report-preview')
  await page.keyboard.press('Escape'); await expect(dialog).toHaveCount(0); expect(downloads).toBe(0)
  await expect(page.getByRole('button', { name: 'Redacted report', exact: true })).toBeFocused()
  await page.getByRole('button', { name: 'Redacted report', exact: true }).click()
  await page.getByRole('button', { name: 'Cancel', exact: true }).click(); expect(downloads).toBe(0)
  await page.getByRole('button', { name: 'Redacted report', exact: true }).click()
  const promised = page.waitForEvent('download'); await page.getByRole('button', { name: 'Download summary', exact: true }).click()
  const download = await promised
  expect(download.suggestedFilename()).toBe('ScamSentry_Redacted_Summary.txt')
  const path = await download.path(); expect(path).not.toBeNull(); expect(readFileSync(path!, 'utf8')).toBe(preview)
  expect(downloads).toBe(1)
})

test('theme and motion preferences are the only stored state and do not rerun analysis', async ({ page, context }) => {
  const requests: { url: string; body: string | null; method: string }[] = []; let websockets = 0
  page.on('request', request => requests.push({ url: request.url(), body: request.postData(), method: request.method() })); page.on('websocket', () => websockets++)
  await page.goto('/analyze'); await page.getByLabel('Your message').fill(`${ordinary} ${canary}`)
  await page.getByRole('button', { name: 'Analyze message', exact: true }).click()
  await expect(page.locator('.risk-result')).toBeVisible()
  const before = requests.filter(r => r.url.includes('/model/')).length
  await preferences(page, 'dark'); await menu(page)
  await page.getByRole('button', { name: 'Pause motion', exact: true }).filter({ visible: true }).click()
  const close = page.getByRole('button', { name: 'Close menu' }); if (await close.isVisible()) await close.click()
  await expect(page.locator('html')).toHaveAttribute('data-motion', 'paused')
  await expect(page.getByLabel('Your message')).toHaveValue(`${ordinary} ${canary}`)
  expect(requests.filter(r => r.url.includes('/model/')).length).toBe(before)
  const storage = await page.evaluate(async () => ({ local: { ...localStorage }, session: { ...sessionStorage }, history: history.state, hash: location.hash, query: location.search, indexed: await indexedDB.databases(), caches: await caches.keys(), workers: await navigator.serviceWorker.getRegistrations() }))
  expect(storage).toEqual({ local: { 'scamsentry.theme': 'dark', 'scamsentry.motion': 'paused' }, session: {}, history: null, hash: '', query: '', indexed: [], caches: [], workers: [] })
  expect(await context.cookies()).toEqual([]); expect(websockets).toBe(0)
  expect(requests.every(r => r.method === 'GET')).toBe(true)
  expect(requests.every(r => new URL(r.url).origin === new URL(page.url()).origin)).toBe(true)
  expect(JSON.stringify(requests)).not.toContain(canary)
  await page.reload(); await expect(page.getByLabel('Your message')).toHaveValue(''); await expect(page.locator('html')).toHaveAttribute('data-theme', 'dark'); await expect(page.locator('html')).toHaveAttribute('data-motion', 'paused')
  await capture(page, 'dark-empty')
})

test('reduced motion, examples and storage denial leave all core actions usable', async ({ page }) => {
  await page.emulateMedia({ reducedMotion: 'reduce' })
  await page.addInitScript(() => { Object.defineProperty(Storage.prototype, 'getItem', { value() { throw new DOMException('Denied', 'SecurityError') } }); Object.defineProperty(Storage.prototype, 'setItem', { value() { throw new DOMException('Denied', 'SecurityError') } }) })
  await page.goto('/analyze'); await page.getByRole('button', { name: /01 Class reminder/ }).click()
  await expect(page.getByLabel('Your message')).toHaveValue(ordinary)
  await expect(page.getByLabel('Your message')).toBeFocused(); await expect(page.locator('.risk-result')).toHaveCount(0)
  await page.getByLabel('Your message').fill('😀'.repeat(4000)); await expect(page.getByRole('button', { name: 'Analyze message', exact: true })).toBeEnabled()
  await page.getByLabel('Your message').fill('😀'.repeat(4001)); await expect(page.getByRole('button', { name: 'Analyze message', exact: true })).toBeDisabled()
  await page.getByRole('button', { name: 'Clear', exact: true }).click(); await page.getByLabel('Your message').fill(' \n '); await expect(page.getByRole('button', { name: 'Analyze message', exact: true })).toBeDisabled()
  await preferences(page, 'dark'); await nav(page, 'Privacy')
  await expect(page.locator('html')).toHaveAttribute('data-motion', 'paused')
  expect(await page.evaluate(() => document.getAnimations().filter(a => a.playState === 'running').length)).toBe(0)
  await capture(page, 'reduced-motion-privacy')
})

test('responsive layouts, fallback fonts, text spacing and all public pages', async ({ page }) => {
  test.setTimeout(120000)
  await page.emulateMedia({ reducedMotion: 'reduce' })
  const records: { width: number; path: string; overflow: boolean }[] = []
  const widths = test.info().project.name === 'desktop-chromium' ? [320, 360, 390, 430, 768, 1024, 1363, 1440, 1920, 2560] : [390, 844]
  for (const width of widths) {
    await page.setViewportSize({ width, height: width === 844 ? 390 : 936 })
    for (const path of ['/', '/analyze', '/learn', '/learn/actual-host', '/evidence', '/privacy', '/android']) {
      await page.goto(path); await expect(page.locator('h1')).toBeVisible(); await page.evaluate(() => document.fonts.ready)
      const overflow = await page.evaluate(() => document.documentElement.scrollWidth > innerWidth)
      records.push({ width, path, overflow }); expect(overflow, `${path} overflow at ${width}`).toBe(false)
    }
  }
  await page.setViewportSize({ width: 320, height: 936 }); await page.route('**/fonts/**', route => route.abort())
  await page.goto('/'); await page.addStyleTag({ content: '*{line-height:1.5!important;letter-spacing:.12em!important;word-spacing:.16em!important}p{margin-bottom:2em!important}' })
  const spacingBounds = await page.evaluate(() => ({ width: innerWidth, scroll: document.documentElement.scrollWidth, overflow: Array.from(document.querySelectorAll<HTMLElement>('body *')).map(element => ({ tag: element.tagName, class: element.className, right: element.getBoundingClientRect().right, width: element.getBoundingClientRect().width, text: element.textContent?.slice(0, 90) })).filter(element => element.right > innerWidth + 1).slice(0, 25) }))
  if (spacingBounds.scroll > spacingBounds.width) console.log('REFLOW_DIAGNOSTIC', JSON.stringify(spacingBounds))
  expect(spacingBounds.scroll <= spacingBounds.width).toBe(true)
  await capture(page, '320-fallback-text-spacing')
  await page.unroute('**/fonts/**')
  for (const width of [1363, 320]) {
    await page.setViewportSize({ width, height: 936 })
    for (const path of ['/', '/analyze', '/learn', '/evidence', '/privacy', '/android']) {
      await page.goto(path); await page.evaluate(() => document.fonts.ready)
      await page.evaluate(() => { const sizes = Array.from(document.querySelectorAll<HTMLElement>('body *')).map(element => ({ element, size: parseFloat(getComputedStyle(element).fontSize) })); sizes.forEach(({ element, size }) => { element.style.fontSize = `${size * 2}px` }) })
      const overflow = await page.evaluate(() => document.documentElement.scrollWidth > innerWidth)
      if (overflow) console.log('TEXT_RESIZE_DIAGNOSTIC', width, path, await page.evaluate(() => Array.from(document.querySelectorAll<HTMLElement>('body *')).map(element => ({ tag: element.tagName, class: element.className, right: element.getBoundingClientRect().right, text: element.textContent?.slice(0, 60) })).filter(element => element.right > innerWidth + 1).slice(0, 20)))
      records.push({ width, path: `${path} / 200% text resize`, overflow }); expect(overflow, `${path} 200% text resize at ${width}`).toBe(false)
    }
  }
  writeFileSync(join(evidence, `${test.info().project.name}_layout-matrix.json`), JSON.stringify({ browserScope: 'viewport reflow, not hardware device certification', records }, null, 2))
})

test('editorial states and two neutral palettes have original stable screenshots', async ({ page }) => {
  test.setTimeout(120000)
  for (const theme of ['light', 'dark'] as const) {
    await page.goto('/'); await preferences(page, theme); await capture(page, `${theme}-home`)
    for (const path of ['/learn', '/learn/sender-identity', '/evidence', '/privacy', '/android']) { await page.goto(path); await capture(page, `${theme}-${path.slice(1).replace('/', '-')}`) }
    await page.goto('/analyze'); await capture(page, `${theme}-analyzer-empty`)
    for (const [fixture, input, category] of [['ordinary', ordinary, 'Lower observed risk'], ['secret', 'Send your OTP immediately to confirm access.', 'Higher risk'], ['link', 'https://bank.example@review.test/notes', 'Needs checking'], ['sparse', 'zqjzxqjz', 'Unable to assess reliably']]) {
      await page.getByLabel('Your message').fill(input); await capture(page, `${theme}-${fixture}-populated`)
      await page.getByRole('button', { name: 'Analyze message', exact: true }).click(); await expect(page.getByRole('heading', { name: category, exact: true })).toBeVisible()
      await page.getByText('Inspect technical details', { exact: true }).click(); await capture(page, `${theme}-${fixture}-result`)
    }
  }
  const button = page.getByRole('button', { name: 'Menu', exact: true })
  if (await button.isVisible()) { await button.click(); await capture(page, 'mobile-menu'); await page.keyboard.press('Escape'); await expect(button).toBeFocused() }
})

test('motion demonstration remains interruptible and preserves navigation', async ({ page }) => {
  await page.goto('/'); await page.waitForTimeout(70)
  await menu(page); await page.getByRole('button', { name: 'Pause motion', exact: true }).filter({ visible: true }).click()
  const closePause = page.getByRole('button', { name: 'Close menu' }); if (await closePause.isVisible()) await closePause.click()
  await expect(page.locator('.home-hero>.eyebrow>span[aria-hidden="true"]')).toHaveText('SCAMSENTRY / A PERSONAL RESEARCH PREVIEW')
  await menu(page); await page.getByRole('button', { name: 'Resume motion', exact: true }).filter({ visible: true }).click()
  const closeResume = page.getByRole('button', { name: 'Close menu' }); if (await closeResume.isVisible()) await closeResume.click()
  await expect(page.locator('.home-hero>.eyebrow>span[aria-hidden="true"]')).toHaveText('SCAMSENTRY / A PERSONAL RESEARCH PREVIEW')
  await page.waitForTimeout(700)
  await preferences(page, 'dark'); await page.waitForTimeout(600); await preferences(page, 'light'); await page.waitForTimeout(600)
  await page.getByRole('link', { name: 'Inspect a message', exact: true }).click()
  await nav(page, 'Learn'); await page.waitForTimeout(600); await page.getByRole('link', { name: /Keep codes private/ }).click(); await nav(page, 'Evidence'); await page.waitForTimeout(600); await nav(page, 'Privacy'); await page.waitForTimeout(600); await nav(page, 'Android'); await page.waitForTimeout(600); await nav(page, 'Analyze')
  await page.getByRole('button', { name: /02 Secret request/ }).click()
  await page.getByRole('button', { name: 'Analyze message', exact: true }).click(); await expect(page.getByRole('heading', { name: 'Higher risk', exact: true })).toBeVisible(); await page.waitForTimeout(700)
  await page.getByRole('button', { name: 'Redacted report', exact: true }).click(); await page.waitForTimeout(600); await page.keyboard.press('Escape')
  expect(await page.locator('dialog').count()).toBe(0)
  const video = page.video(); await page.close()
  if (video) await video.saveAs(join(evidence, `${test.info().project.name}_motion-demo.webm`))
})

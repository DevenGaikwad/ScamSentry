import { expect,test } from '@playwright/test'
import type { Page } from '@playwright/test'
import { mkdirSync,writeFileSync } from 'node:fs'
import { join } from 'node:path'

const evidence='../assets/evidence/v3/motion/browser'
const observations:Record<string,unknown>={}
const capture=async(page:Page,name:string)=>{mkdirSync(evidence,{recursive:true});await page.screenshot({path:join(evidence,`${test.info().project.name}_${name}.png`),animations:'allow',scale:'css'})}
async function nav(page:Page,name:string){
  const menu=page.getByRole('button',{name:'Menu',exact:true})
  if(await menu.isVisible())await menu.click()
  const dialog=page.getByRole('dialog')
  const scope=await dialog.isVisible()?dialog:page.getByRole('navigation',{name:'Main navigation',exact:true})
  await scope.getByRole('link',{name,exact:true}).click()
}
test.beforeEach(async({page})=>{await page.emulateMedia({reducedMotion:'no-preference'});await page.addInitScript(()=>{localStorage.setItem('scamsentry.motion','auto');localStorage.setItem('scamsentry.theme','light')})})

test('reticle follows blank space, snaps nested controls, then restores idle with no duplicate layers',async({page})=>{
  await page.goto('/')
  const reticle=page.locator('.pointer-brackets')
  const fine=await page.evaluate(()=>matchMedia('(any-hover: hover) and (any-pointer: fine)').matches)
  if(!fine){await page.mouse.move(180,180);await expect(reticle).toBeHidden();await page.dispatchEvent('body','pointermove',{pointerType:'touch',clientX:180,clientY:180});await expect(reticle).toBeHidden();return}
  for(const [x,y] of [[320,160],[660,180],[1050,170]]){
    await page.mouse.move(x,y);await expect(reticle).toBeVisible()
    await expect.poll(async()=>{const r=await reticle.boundingBox();return r?Math.hypot(r.x+r.width/2-x,r.y+r.height/2-y):999}).toBeLessThan(1.5)
    await expect(reticle).toHaveAttribute('data-state','follow')
  }
  await capture(page,'cursor-idle')
  const target=page.getByRole('navigation',{name:'Main navigation',exact:true}).getByRole('link',{name:'Privacy',exact:true})
  const box=(await target.boundingBox())!
  await target.hover();await expect(reticle).toHaveAttribute('data-state','snap')
  await expect.poll(async()=>Math.abs((await reticle.boundingBox())!.width-(box.width+12))).toBeLessThan(1)
  await capture(page,'cursor-snap')
  await page.getByRole('link',{name:'ScamSentry home',exact:true}).locator('svg').hover()
  await expect(reticle).toHaveAttribute('data-state','snap')
  await page.mouse.move(600,160);await expect(reticle).toHaveAttribute('data-state','follow')
  await page.evaluate(()=>scrollTo(0,180));await expect(reticle).toBeVisible()
  await page.setViewportSize({width:1363,height:936});await page.mouse.move(620,160)
  await expect.poll(async()=>(await reticle.boundingBox())?.width??999).toBeLessThan(35)
  expect(await reticle.evaluate(el=>({hidden:el.getAttribute('aria-hidden'),events:getComputedStyle(el).pointerEvents,cursor:getComputedStyle(document.body).cursor}))).toEqual({hidden:'true',events:'none',cursor:'auto'})
  await page.dispatchEvent('body','pointerout',{pointerType:'mouse',relatedTarget:null});await expect(reticle).toBeHidden()
  await page.mouse.move(600,180);await expect(reticle).toBeVisible()
  await nav(page,'Analyze');await page.getByLabel('Your message').focus();await expect(reticle).toBeHidden()
  await expect(reticle).toHaveCount(1)
})

test('public routes render departure, geometric middle, real miniature and five destination motifs',async({page})=>{
  await page.setViewportSize({width:1363,height:936});await page.goto('/')
  const samples=[]
  for(const [name,motif] of [['Learn','rails'],['Evidence','grid'],['Privacy','rings'],['Android','corridor']] as const){
    // At this desktop width the actual header stays usable during the scene.
    await page.getByRole('navigation',{name:'Main navigation',exact:true}).getByRole('link',{name,exact:true}).click()
    const scene=page.locator('.route-scene'),main=page.locator('main')
    await expect(scene).toHaveAttribute('data-motif',motif)
    const proofPromise=page.evaluate(async()=>{
      const frames:{phase:string|null;geometry:number;outgoing:string;incoming:string;opacity:number;heading:string|null;inert:boolean}[]=[]
      const started=performance.now()
      return await new Promise<typeof frames>((resolve,reject)=>{
        const sample=()=>{
          const scene=document.querySelector('.route-scene'),main=document.querySelector('main')!
          if(!scene){resolve(frames);return}
          if(performance.now()-started>2500){reject(new Error('Scene did not settle'));return}
          frames.push({phase:scene.getAttribute('data-phase'),geometry:Number(getComputedStyle(scene.querySelector('.depth-geometry')!).opacity),outgoing:getComputedStyle(scene.querySelector('.outgoing-public-plane')!).transform,incoming:getComputedStyle(main).transform,opacity:Number(getComputedStyle(main).opacity),heading:main.querySelector('h1')?.textContent??null,inert:main.hasAttribute('inert')})
          requestAnimationFrame(sample)
        }
        sample()
      })
    })
    await expect(scene.locator('.outgoing-public-plane')).toBeVisible()
    await expect(scene).toHaveAttribute('data-phase','scene')
    if(name==='Privacy')await capture(page,'transition-middle')
    if(name==='Android'){await expect(scene).toHaveAttribute('data-phase','arriving');await capture(page,'transition-arrival')}
    const proof=await proofPromise
    expect(proof.some(f=>f.phase==='scene'&&f.geometry>.25&&f.outgoing!=='none')).toBe(true)
    const incoming=proof.find(f=>f.phase==='arriving'&&f.opacity>.1&&f.incoming!=='none')
    expect(incoming,'A real visible incoming plane must be observed, independent of PNG encoding time').toBeTruthy()
    expect(incoming!.inert).toBe(true);expect(incoming!.heading!.length).toBeGreaterThan(5)
    samples.push({motif,frames:proof})
    await expect(main).toHaveAttribute('data-route-phase','idle');await expect(page.locator('h1')).toBeFocused();await expect(scene).toHaveCount(0)
  }
  await page.getByRole('link',{name:'ScamSentry home',exact:true}).click()
  await expect(page.locator('.route-scene')).toHaveAttribute('data-motif','signal')
  await expect(page.locator('main')).toHaveAttribute('data-route-phase','idle')
  expect(await page.evaluate(()=>history.state)).toBeNull()
  observations[test.info().project.name+'-choreography']=samples
})

test('latest navigation wins, history remains native, pause and renderer failure always restore usable content',async({page})=>{
  await page.setViewportSize({width:1363,height:936});await page.goto('/')
  const links=page.getByRole('navigation',{name:'Main navigation',exact:true})
  await links.getByRole('link',{name:'Learn',exact:true}).click();await links.getByRole('link',{name:'Evidence',exact:true}).click()
  await page.goBack();await expect(page).toHaveURL(/\/learn$/)
  await page.goForward();await expect(page).toHaveURL(/\/evidence$/)
  await page.getByRole('button',{name:'Pause motion',exact:true}).click()
  await expect(page.locator('.route-scene')).toHaveCount(0);await expect(page.locator('main')).toHaveAttribute('data-route-phase','idle');await expect(page.locator('h1')).toBeFocused()
  await page.getByRole('button',{name:'Resume motion',exact:true}).click()
  const history=await page.evaluate(()=>window.history.length)
  await links.getByRole('link',{name:'Evidence',exact:true}).click();expect(await page.evaluate(()=>window.history.length)).toBe(history)
  await page.evaluate(()=>{Element.prototype.animate=()=>{throw new Error('Fictional renderer failure')}})
  await links.getByRole('link',{name:'Privacy',exact:true}).click();await expect(page.locator('main')).toHaveAttribute('data-route-phase','idle');await expect(page.locator('.route-scene')).toHaveCount(0)
  await expect(page.locator('h1')).toBeVisible();await expect(page.locator('h1')).toBeFocused()
})

test('private input and reports never enter scene markup or network requests',async({page})=>{
  const canary='FICTIONAL_MOTION_PRIVATE_CANARY',requests:{url:string;body:string|null}[]=[]
  page.on('request',r=>requests.push({url:r.url(),body:r.postData()}))
  await page.goto('/analyze');await page.getByLabel('Your message').fill(`Send your OTP immediately. ${canary}`)
  await nav(page,'Privacy');await expect(page.locator('.route-scene')).toHaveCount(0)
  await nav(page,'Analyze');await expect(page.locator('.route-scene')).toHaveCount(0)
  await page.getByRole('button',{name:'Analyze message',exact:true}).click();await expect(page.locator('.risk-result')).toBeVisible()
  await page.getByRole('button',{name:'Redacted report',exact:true}).click();await expect(page.getByRole('dialog')).toBeVisible();await expect(page.locator('.pointer-brackets')).toBeHidden()
  expect(await page.getByRole('dialog').textContent()).not.toContain(canary)
  await page.keyboard.press('Escape');await nav(page,'Learn');await nav(page,'Evidence')
  expect(await page.locator('.route-scene').evaluateAll(nodes=>nodes.map(n=>n.textContent).join(''))).not.toContain(canary)
  await expect(page.locator('main')).toHaveAttribute('data-route-phase','idle')
  for(const r of requests){expect(r.url).not.toContain(canary);expect(r.body??'').not.toContain(canary);expect(new URL(r.url).origin).toBe(new URL(page.url()).origin)}
})

test('hero draws visible seeded geometry, moves, suspends offscreen and survives canvas failure',async({page})=>{
  await page.goto('/')
  const hero=page.locator('.home-hero .hero-network'),canvas=hero.locator('canvas')
  await expect(hero).toHaveAttribute('data-renderer','canvas');await expect(hero).toHaveAttribute('data-running','true')
  const pixels=await canvas.evaluate((el:HTMLCanvasElement)=>{const c=el.getContext('2d')!,p=c.getImageData(0,0,el.width,el.height).data;let visible=0;for(let i=3;i<p.length;i+=4)if(p[i]>30)visible++;return visible})
  expect(pixels).toBeGreaterThan(600)
  const first=await hero.getAttribute('data-frames');await page.waitForTimeout(200);expect(await hero.getAttribute('data-frames')).not.toBe(first)
  await capture(page,'hero-light')
  const menu=page.getByRole('button',{name:'Menu',exact:true});if(await menu.isVisible())await menu.click()
  await page.getByRole('combobox',{name:'Theme',exact:true}).filter({visible:true}).selectOption('dark')
  const close=page.getByRole('button',{name:'Close menu'});if(await close.isVisible())await close.click()
  await expect(page.locator('html')).toHaveAttribute('data-theme','dark');await expect(hero).toHaveAttribute('data-renderer','canvas');await capture(page,'hero-dark')
  await page.locator('footer').scrollIntoViewIfNeeded();await expect(hero).toHaveAttribute('data-running','false')
  const stopped=await hero.getAttribute('data-frames');await page.waitForTimeout(120);expect(await hero.getAttribute('data-frames')).toBe(stopped)
  await page.evaluate(()=>scrollTo(0,0));await expect(hero).toHaveAttribute('data-running','true')
  await page.setViewportSize({width:320,height:936});expect(await canvas.evaluate((el:HTMLCanvasElement)=>el.width)).toBeLessThanOrEqual(640)
  await expect(hero).toHaveCount(1)
  await page.addInitScript(()=>{HTMLCanvasElement.prototype.getContext=()=>null});await page.reload();await expect(hero).toHaveAttribute('data-renderer','static');await expect(hero.locator('.hero-static')).toBeVisible();await expect(page.locator('h1')).toBeVisible()
  observations[test.info().project.name+'-hero']={visiblePixels:pixels,scope:'emulated browser viewport, not physical-phone FPS'}
})

test('packets travel along continuous edges and reduced motion retains completed static equivalents',async({page})=>{
  await page.goto('/privacy')
  const diagram=page.locator('.flow-diagram');await diagram.scrollIntoViewIfNeeded();await expect(diagram).toHaveAttribute('data-running','true')
  const packet=diagram.locator('.flow-packet').first();await expect(packet).toHaveAttribute('transform',/translate/);const first=await packet.getAttribute('transform');await page.waitForTimeout(230);expect(await packet.getAttribute('transform')).not.toBe(first)
  expect(await diagram.locator('.flow-edge').evaluateAll(paths=>paths.every(p=>(p.getAttribute('d')?.match(/M/g)?.length??0)===1))).toBe(true)
  await capture(page,'flow-active')
  await page.emulateMedia({reducedMotion:'reduce'});await expect(diagram).toHaveAttribute('data-running','false');await expect(packet).toBeHidden();await expect(page.locator('.pointer-brackets')).toBeHidden()
  await capture(page,'flow-reduced')
  await page.goto('/');await expect(page.locator('.hero-network')).toHaveAttribute('data-running','false');await capture(page,'hero-reduced')
  await nav(page,'Learn');await expect(page.locator('.route-scene')).toHaveCount(0);await expect(page.locator('h1')).toBeVisible()
  await page.emulateMedia({reducedMotion:'no-preference',forcedColors:'active'});await nav(page,'Privacy');await expect(page.locator('.route-scene')).toHaveCount(0);await expect(page.locator('.pointer-brackets')).toBeHidden()
})

test('real-time motion tour uses only original public content',async({page})=>{
  await page.setViewportSize({width:1363,height:936});await page.goto('/');await page.waitForTimeout(600)
  await page.mouse.move(300,170);await page.mouse.move(800,180,{steps:35});await page.waitForTimeout(250)
  const navigation=page.getByRole('navigation',{name:'Main navigation',exact:true})
  await navigation.getByRole('link',{name:'Learn',exact:true}).hover();await page.waitForTimeout(250)
  await navigation.getByRole('link',{name:'Privacy',exact:true}).hover();await page.waitForTimeout(250)
  await page.mouse.move(620,160,{steps:20});await page.waitForTimeout(250)
  for(const name of ['Learn','Evidence','Privacy']){await navigation.getByRole('link',{name,exact:true}).click();await expect(page.locator('main')).toHaveAttribute('data-route-phase','idle');await page.waitForTimeout(250)}
  await page.locator('.flow-diagram').scrollIntoViewIfNeeded();await page.waitForTimeout(3600)
  await capture(page,'tour-final')
  const video=page.video();await page.close();if(video)await video.saveAs(join(evidence,`${test.info().project.name}_motion-tour.webm`))
  mkdirSync(evidence,{recursive:true});writeFileSync(join(evidence,`${test.info().project.name}_observations.json`),JSON.stringify({source:process.env.SCAMSENTRY_SOURCE_SHA??'local',date:new Date().toISOString(),observations},null,2))
})

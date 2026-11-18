import { test,expect } from '@playwright/test'
import { mkdirSync,writeFileSync } from 'node:fs'
import { join } from 'node:path'
const evidence=join(process.cwd(),'../assets/evidence/v3/browser')

test.describe('runtime capture', () => {
  test.use({ video: 'on' })
  test.afterEach(async({page},info)=>{
   if(info.status==='passed'&&info.project.name==='desktop-chromium'&&info.title.startsWith('real worker workflow')){
    const video=page.video();await page.close()
    if(video)await video.saveAs(join(evidence,'web_demo_desktop.webm'))
   }
  })

  test('real worker workflow, local input privacy, redacted report and responsive layout',async({page,browser},info)=>{
   const requests:{url:string;body:string|null;method:string}[]=[];const errors:string[]=[]
   page.on('request',r=>requests.push({url:r.url(),body:r.postData(),method:r.method()}));page.on('pageerror',e=>errors.push(e.message))
   await page.goto('/');await expect(page.getByRole('heading',{name:'A second look. Before you act.'})).toBeVisible();await page.getByRole('link',{name:'Inspect a message',exact:true}).click()
   mkdirSync(evidence,{recursive:true})
   await page.screenshot({path:join(evidence,`web_${info.project.name}_home.png`),fullPage:true})
   const canary='FICTIONAL_PRIVATE_CANARY_7ZQ'
   await page.getByLabel('Your message').fill('Fictional account team: send your OTP immediately. Visit https://bank.example@review.test/verify '+canary)
   await page.getByRole('button',{name:'Analyze message',exact:true}).click()
   await expect(page.getByRole('heading',{name:'Higher risk',exact:true})).toBeVisible()
   if(info.project.name==='mobile-chromium')await expect(page.getByRole('heading',{name:'Higher risk',exact:true})).toBeInViewport()
   await expect(page.getByRole('heading',{name:'Request for a secret',exact:true})).toBeVisible()
   await expect(page.getByText('review.test',{exact:true})).toBeVisible()
   await page.getByText('Inspect technical details',{exact:true}).click()
   await expect(page.getByText('ss-char-lr-0.1.0 · ordinal_v2',{exact:true})).toBeVisible()
   await page.screenshot({path:join(evidence,`web_${info.project.name}_result.png`),fullPage:true})
   await page.getByRole('button',{name:'Redacted report',exact:true}).click()
   const dialog=page.getByRole('dialog');await expect(dialog).toBeVisible()
   await expect(dialog).not.toContainText(canary);await expect(dialog).not.toContainText('review.test')
   await dialog.getByRole('button',{name:'Cancel',exact:true}).click()
   expect(requests.every(r=>r.method==='GET')).toBe(true)
   expect(requests.some(r=>r.url.includes(canary)||(r.body??'').includes(canary))).toBe(false)
   expect(await page.evaluate(()=>({local:localStorage.length,session:sessionStorage.length,query:location.search,hash:location.hash}))).toEqual({local:0,session:0,query:'',hash:''})
   expect(await page.evaluate(()=>document.documentElement.scrollWidth<=innerWidth)).toBe(true)
   expect(errors).toEqual([])
   writeFileSync(join(evidence,`web_${info.project.name}_network.json`),JSON.stringify({scope:'Fictional workflow; URLs and methods only, no input bodies retained',input_transmitted:false,requests:requests.map(({url,method})=>({url,method})),page_errors:errors},null,2)+'\n')
   await page.getByRole('button',{name:'Clear',exact:true}).click();await expect(page.getByLabel('Your message')).toHaveValue('');await expect(page.getByRole('heading',{name:'Higher risk',exact:true})).toHaveCount(0)
   if(info.project.name==='desktop-chromium'){
    const samples=[['class',"Let's meet in the library after class. Please bring the project notes."],['secret','Fictional team: send your OTP immediately to confirm access.'],['offer','Fictional stationery shop: weekend discount on notebooks. Reply STOP to unsubscribe.'],['url','https://bank.example@review.test/notes'],['sparse','zqjzxqjz']]
    const values:{fixture:string;iteration:number;characters:number;analysis_ms:number;worker_total_ms:number;automation_roundtrip_ms:number}[]=[]
    for(const [fixture,text] of samples){
     await page.getByLabel('Your message').fill(text)
     for(let i=0;i<5;i++){
      const start=performance.now()
      await page.getByRole('button',{name:'Analyze message',exact:true}).click()
      await expect(page.locator('.risk-card')).toBeVisible()
      await page.getByText('Inspect technical details',{exact:true}).click()
      const node=page.getByTestId('worker-timing');await expect(node).toBeVisible()
      values.push({fixture,iteration:i,characters:Array.from(text).length,analysis_ms:Number(await node.getAttribute('data-analysis-ms')),worker_total_ms:Number(await node.getAttribute('data-worker-total-ms')),automation_roundtrip_ms:performance.now()-start})
     }
    }
    const warm=values.slice(1),quantile=(xs:number[],q:number)=>{const a=[...xs].sort((a,b)=>a-b);return a[Math.ceil(q*a.length)-1]}
    writeFileSync(join(evidence,'web_desktop-chromium_timings.json'),JSON.stringify({browser:await browser.version(),platform:process.platform,architecture:process.arch,source_commit:process.env.SCAMSENTRY_SOURCE_SHA??process.env.GITHUB_SHA??'local',samples:values.length,scope:'Fictional local-static-server workflow; first sample cold worker with populated HTTP cache; subsequent samples reuse the validated model. Automation roundtrip includes Playwright and detail interaction, not phone latency.',warm_analysis_median_ms:quantile(warm.map(v=>v.analysis_ms),.5),warm_analysis_p95_ms:quantile(warm.map(v=>v.analysis_ms),.95),warm_worker_total_median_ms:quantile(warm.map(v=>v.worker_total_ms),.5),values,physical_phone:false,heap_measured:false},null,2)+'\n')
   }

  })
})

test('edits reject stale work and subsequent analysis uses new text',async({page})=>{
 let release:()=>void=()=>{};let started:()=>void=()=>{}
 const gate=new Promise<void>(r=>release=r),requested=new Promise<void>(r=>started=r)
 await page.route('**/model/model-*.json',async route=>{started();await gate;await route.continue().catch(()=>{})})
 await page.goto('/analyze');await page.getByLabel('Your message').fill('Send your OTP immediately to confirm access.')
 await page.getByRole('button',{name:'Analyze message',exact:true}).click();await requested
 await page.getByLabel('Your message').fill("Let's meet in the library after class. Please bring the project notes.")
 release();await expect(page.getByRole('heading',{name:'Higher risk',exact:true})).toHaveCount(0)
 await page.getByRole('button',{name:'Analyze message',exact:true}).click()
 await expect(page.getByRole('heading',{name:'Lower observed risk',exact:true})).toBeVisible()
})

test('corrupt model yields a limited state and retains concrete rule observations',async({page})=>{
 await page.route('**/model/model-*.json',async route=>{const response=await route.fetch();await route.fulfill({response,body:(await response.text())+' '})})
 await page.goto('/analyze');await page.getByLabel('Your message').fill('Please bring the laboratory project notes after class.')
 await page.getByRole('button',{name:'Analyze message',exact:true}).click();await expect(page.getByRole('heading',{name:'Unable to assess reliably',exact:true})).toBeVisible()
 await page.getByText(/Scope and limitations/).click();await expect(page.getByText('The validated message model is unavailable. Only local observations are available.',{exact:true})).toBeVisible()
 await page.getByLabel('Your message').fill('Send your password to confirm access.')
 await page.getByRole('button',{name:'Analyze message',exact:true}).click();await expect(page.getByRole('heading',{name:'Higher risk',exact:true})).toBeVisible();await expect(page.getByRole('heading',{name:'Request for a secret',exact:true})).toBeVisible()
})

test('scope limits, standalone link and evidence navigation',async({page})=>{
 await page.goto('/analyze');await page.getByLabel('Your message').fill('a'.repeat(4001));await expect(page.getByRole('button',{name:'Analyze message',exact:true})).toBeDisabled()
 await page.getByLabel('Your message').fill('HTTP://Example.ORG:80/notes');await page.getByRole('button',{name:'Analyze message',exact:true}).click()
 await expect(page.getByRole('heading',{name:'Needs checking',exact:true})).toBeVisible();await expect(page.getByText('example.org',{exact:true})).toBeVisible()
 await page.goto('/evidence');await expect(page.getByRole('heading',{name:'Three-class macro-F1',exact:true})).toBeVisible()
 await page.goto('/privacy');await expect(page.getByRole('heading',{name:'Memory, not a message database',exact:true})).toBeVisible()
})

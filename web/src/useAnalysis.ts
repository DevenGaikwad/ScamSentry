import { useCallback, useEffect, useRef, useState } from 'react'
import type { Analysis } from './core/engine'

export type Stage = 'idle' | 'loading' | 'processing' | 'done' | 'cancelled' | 'error'
type WorkerReply = { id: number; type: string; stage: 'loading' | 'processing'; result: Analysis; message: string; timings: { analysis_ms: number; total_ms: number } }
export function useAnalysis() {
  const [text, setText] = useState('')
  const [result, setResult] = useState<Analysis | null>(null)
  const [stage, setStage] = useState<Stage>('idle')
  const [error, setError] = useState<string | null>(null)
  const [timing, setTiming] = useState<{ analysis_ms: number; total_ms: number } | null>(null)
  const [announcement, setAnnouncement] = useState('')
  const worker = useRef<Worker | null>(null)
  const request = useRef(0)
  const active = useRef(false)
  const focusPending = useRef(false)
  const busy = stage === 'loading' || stage === 'processing'
  const cancel = useCallback(() => {
    request.current++; active.current = false
    worker.current?.terminate(); worker.current = null
    setStage('cancelled'); setResult(null); setTiming(null); setError(null)
    focusPending.current = false; setAnnouncement('Analysis cancelled. Your draft is still here.')
  }, [])
  const leave = useCallback(() => { if (active.current) cancel() }, [cancel])
  const edit = useCallback((value: string) => {
    request.current++
    if (active.current) { worker.current?.terminate(); worker.current = null }
    active.current = false; focusPending.current = false
    setText(value); setStage('idle'); setResult(null); setError(null); setTiming(null); setAnnouncement('')
  }, [])
  useEffect(() => () => { request.current++; active.current = false; worker.current?.terminate(); worker.current = null }, [])
  function submit() {
    if (!text.trim() || Array.from(text).length > 4000 || active.current) return
    const id = ++request.current
    active.current = true; setStage('loading'); setResult(null); setError(null); setTiming(null); setAnnouncement('')
    try {
      if (!worker.current) worker.current = new Worker(new URL('./analysis.worker.ts', import.meta.url), { type: 'module' })
      const currentWorker = worker.current
      const fail = () => {
        if (id !== request.current) return
        active.current = false; currentWorker.terminate(); worker.current = null
        setStage('error'); setError('The local analysis worker could not finish. Your draft is still here. Try again.')
        setAnnouncement('Local analysis could not finish. You can try again.')
      }
      currentWorker.onmessage = (event: MessageEvent<WorkerReply>) => {
        const message = event.data
        if (message.id !== request.current) return
        if (message.type === 'status') setStage(message.stage)
        else if (message.type === 'result') {
          active.current = false; focusPending.current = true; setResult(message.result); setTiming(message.timings); setStage('done')
        } else fail()
      }
      currentWorker.onerror = fail
      currentWorker.onmessageerror = fail
      currentWorker.postMessage({ id, text, source: 'text' })
    } catch {
      active.current = false; worker.current?.terminate(); worker.current = null
      setStage('error'); setError('The local analysis worker could not start. Your draft is still here. Try again.')
      setAnnouncement('Local analysis could not start. You can try again.')
    }
  }
  return { text, result, stage, error, timing, announcement, busy, count: Array.from(text).length, focusPending, edit, cancel, leave, submit }
}
export type AnalysisSession = ReturnType<typeof useAnalysis>

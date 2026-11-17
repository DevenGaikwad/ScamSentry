// Presentation-only allowlist, before first paint. Never reads a draft or result.
try {
  const theme = localStorage.getItem('scamsentry.theme')
  if (theme === 'light' || theme === 'dark') document.documentElement.dataset.theme = theme
  const motion = localStorage.getItem('scamsentry.motion')
  document.documentElement.dataset.motion = motion === 'paused' || matchMedia('(prefers-reduced-motion: reduce)').matches ? 'paused' : 'running'
} catch { /* Storage is optional; CSS follows system preferences. */ }

import { createServer } from 'node:http'
import { readFile, stat } from 'node:fs/promises'
import { resolve, extname, sep } from 'node:path'
import { gzipSync } from 'node:zlib'
export function serve(directory, port) {
  const root = resolve(directory)
  const types = { '.html': 'text/html; charset=utf-8', '.js': 'text/javascript', '.css': 'text/css', '.json': 'application/json', '.svg': 'image/svg+xml', '.woff2': 'font/woff2', '.ttf': 'font/ttf', '.txt': 'text/plain', '.png': 'image/png' }
  return createServer(async (request, response) => {
    let path
    try { path = decodeURIComponent(new URL(request.url, 'http://localhost').pathname) } catch { response.writeHead(400); response.end(); return }
    let file = resolve(root, `.${path === '/' ? '/index.html' : path}`)
    if (file !== root && !file.startsWith(root + sep)) { response.writeHead(403); response.end(); return }
    let status = 200
    try {
      try { if (!(await stat(file)).isFile()) throw new Error() } catch { file += '.html'; await stat(file) }
    } catch { file = resolve(root, '404.html'); status = 404 }
    try {
      const data = await readFile(file), type = types[extname(file)] ?? 'application/octet-stream'
      const compressed = /gzip/.test(request.headers['accept-encoding'] ?? '') && /html|javascript|css|json|svg|text/.test(type)
      const payload = compressed ? gzipSync(data) : data
      response.writeHead(status, { 'Content-Type': type, 'Content-Length': payload.length, 'Cache-Control': /html/.test(type) ? 'no-cache' : 'public, max-age=3600', ...(compressed ? { 'Content-Encoding': 'gzip', Vary: 'Accept-Encoding' } : {}) })
      response.end(payload)
    } catch { response.writeHead(404); response.end('Not found') }
  }).listen(port, '127.0.0.1', () => console.log(`Lab static server ${port}: ${root}`))
}
if (process.argv[1] === new URL(import.meta.url).pathname) serve(process.argv[2], Number(process.argv[3]))

import js from '@eslint/js'
import ts from 'typescript-eslint'
import globals from 'globals'
export default [ { ignores:['dist/**','node_modules/**'] }, js.configs.recommended, ...ts.configs.recommended,
 { files:['**/*.{ts,tsx}'],languageOptions:{globals:{...globals.browser,...globals.node,...globals.worker}},rules:{'@typescript-eslint/no-explicit-any':'error'} } ]

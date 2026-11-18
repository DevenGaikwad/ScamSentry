import type { Risk } from './core/engine'

export const creator = {
  fullName: 'Deven Sachin Gaikwad',
  fullDegree: 'B.Tech Electronics and Communication Engineering (AI and ML)',
  institution: 'DES Pune University',
} as const
export const creatorCredit = `${creator.fullName} · ${creator.fullDegree} · ${creator.institution}.`
export const site = {
  origin: 'https://scamsentry-inspect.vercel.app',
  repository: 'https://github.com/DevenGaikwad/ScamSentry',
  version: '0.4.0',
  release: 'https://github.com/DevenGaikwad/ScamSentry/releases/tag/v0.3.0-preview',
} as const
export const riskCopy: Record<Risk, { title: string; description: string; symbol: string }> = {
  higher: { title: 'Higher risk', description: 'There are indicators that deserve careful independent verification.', symbol: '!' },
  checking: { title: 'Needs checking', description: 'There are mixed signals or limited context. Verify before acting.', symbol: '?' },
  lower: { title: 'Lower observed risk', description: 'Fewer indicators were found in the supported analysis. This does not verify the sender.', symbol: '↘' },
  unable: { title: 'Unable to assess reliably', description: 'The available analysis is limited. Review the input and verify independently.', symbol: '—' },
}
export const navigation = [
  { path: '/analyze', name: 'Analyze', chapter: '01' },
  { path: '/learn', name: 'Learn', chapter: '02' },
  { path: '/evidence', name: 'Evidence', chapter: '03' },
  { path: '/privacy', name: 'Privacy', chapter: '04' },
  { path: '/android', name: 'Android', chapter: '05' },
] as const
export const examples = [
  { label: 'Class reminder', kind: 'Ordinary conversation', text: "Let's meet in the library after class. Please bring the project notes." },
  { label: 'Secret request', kind: 'An unexpected demand', text: 'Fictional account team: send your OTP immediately to prevent suspension. Visit https://bank.example@review.test/verify' },
  { label: 'An offer', kind: 'A promotional message', text: 'Fictional stationery shop: weekend discount on notebooks. Reply STOP to unsubscribe.' },
  { label: 'Scam education', kind: 'A quoted warning', text: 'Educational example: "Send your OTP immediately". Never share your authentication codes.' },
  { label: 'A link', kind: 'Inspect the actual host', text: 'https://bank.example@review.test/notes' },
] as const

export const articles = [
  {
    slug: 'sender-identity', title: 'A name is not an identity.', shortTitle: 'Check the sender.', label: 'SENDER IDENTITY',
    lead: 'A familiar name can make an unfamiliar request feel ordinary. Slow down at the request itself.',
    body: 'Display names, logos and screenshots can be copied. Even an existing conversation can contain a compromised account. A polished message is evidence of presentation, not proof of who is behind it.',
    example: 'Fictional example: “This is your college office. We have a new payment account. Transfer the registration fee today.”',
    explanation: 'The claimed college identity and the payment destination need independent confirmation. A familiar signature does not connect the two.',
    steps: ['Open the organization’s app or a website you already know.', 'Use a saved contact or an independently verified number to confirm the exact request.', 'Do not use the unexpected message’s own link or number as the only verification.'],
  },
  {
    slug: 'authentication-codes', title: 'Keep the code to yourself.', shortTitle: 'Keep codes private.', label: 'AUTHENTICATION SECRETS',
    lead: 'Receiving a code and being asked to reveal a code are different events.',
    body: 'A one-time password, PIN or recovery code can authorize access. A message that asks you to send one back deserves particular care, even if it claims that the request is urgent or routine.',
    example: 'Fictional example: “Send your OTP to this support agent so we can stop your account being suspended.”',
    explanation: 'An affirmative request to reveal a secret is a concrete observation. ScamSentry can surface that pattern; it cannot identify the sender or recover an account.',
    steps: ['Keep authentication codes, passwords and PINs private.', 'If you did not initiate the sign-in, review the account using its known app.', 'If you already shared a secret, use the service’s official account-recovery and security controls.'],
  },
  {
    slug: 'unexpected-payments', title: 'Give payments a second check.', shortTitle: 'Verify the payment.', label: 'PAYMENT REQUESTS',
    lead: 'Urgency is a reason to verify the destination before money moves.',
    body: 'A small delivery fee, an attractive refund or a changed bank account can redirect a payment. Confirm both the purpose and recipient using a channel separate from the request.',
    example: 'Fictional example: “Your parcel is waiting. Pay a small release fee through this new link within ten minutes.”',
    explanation: 'A deadline and a plausible story do not establish an obligation. This example illustrates a check to make, not a claim that every fee message is fraudulent.',
    steps: ['Check the order or invoice inside the original service.', 'Confirm a changed recipient through a contact you already trust.', 'Review the final payee and amount in your payment app before authorizing anything.'],
  },
  {
    slug: 'actual-host', title: 'Read the destination.', shortTitle: 'Find the actual host.', label: 'LINK STRUCTURE',
    lead: 'The most recognizable words in an address may not be the host.',
    body: 'In an address, text before an @ can be user information. ScamSentry extracts and displays the actual host as inert text. It does not visit the website, follow redirects or consult a reputation service.',
    example: 'https://bank.example@review.test/verify',
    explanation: 'Here the actual host is review.test. The earlier bank.example text is not the destination. Both names use reserved fictional domains.',
    steps: ['Look at the extracted host rather than a familiar word in the path.', 'Treat shortened links as unresolved when their destination is unavailable.', 'Remember that HTTPS encrypts a connection; it does not establish the sender’s identity.'],
  },
  {
    slug: 'useful-uncertainty', title: 'Make room for uncertainty.', shortTitle: 'Respect the limits.', label: 'UNCERTAINTY',
    lead: 'A useful tool needs a clear way to say that it cannot assess something reliably.',
    body: 'Short excerpts, unfamiliar language, obfuscation and missing context can undermine interpretation. The model was evaluated on a historical English short-message corpus. It has not been validated for every language or current campaign.',
    example: 'Fictional example: “xqz — act now” provides too little meaningful context to establish a sender or explain a request.',
    explanation: 'An “Unable to assess reliably” result is a limit of the available analysis. It is not an all-clear and does not prove that the message is malicious.',
    steps: ['Review the complete original message and its context.', 'Read the observations and limitations separately from the statistical estimates.', 'Verify an unexpected request independently when the answer matters.'],
  },
  {
    slug: 'promotion-and-smishing', title: 'An offer is not a verdict.', shortTitle: 'Separate the signals.', label: 'PROMOTION & SMISHING',
    lead: 'Unwanted advertising and credential-seeking messages are different patterns.',
    body: 'The historical model has three classes: ordinary, promotional-or-spam and smishing-like. These describe text resemblance. None of the classes authenticates a business or guarantees a transaction.',
    example: 'Fictional example: “Stationery sale this weekend. Reply STOP to unsubscribe.” Compare that with “Send your password to claim your prize.”',
    explanation: 'The first is a promotional format. The second includes a request for a secret. Read the concrete observation instead of treating a broad class as a security verdict.',
    steps: ['Distinguish an advertisement from a request for credentials or money.', 'Check any transaction separately, including the recipient and terms.', 'Use the tool as a second look while retaining your own judgment.'],
  },
] as const

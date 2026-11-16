package `in`.deven.scamsentry.core

/** Explicit Python-compatible classes work on both OpenJDK and Android ICU.
 * Android rejects Java's embedded (?U) flag; its implicit classes also differ.
 */
internal object UnicodePattern {
    const val WORD_CONTENT = "\\p{L}\\p{N}_"
    const val SPACE_CONTENT = "\\p{Z}\\u0009-\\u000d\\u001c-\\u0020\\u0085"
    private const val WORD = "[$WORD_CONTENT]"
    const val BOUNDARY = "(?:(?<=$WORD)(?!$WORD)|(?<!$WORD)(?=$WORD))"
}

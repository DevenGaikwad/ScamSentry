package `in`.deven.scamsentry.core

/** Local, bounded context; unrelated educational language cannot suppress a request. */
object ContextRulesV2 {
    const val VERSION="local_context_v2"
    private val request=Regex("(?=\\b(share|send|tell|reveal|reply\\s+with|provide|give)\\b([^.!?;:]{0,55}?)\\b(otp|one[ -]time (?:code|password)|pin|password|verification code)\\b)")
    private val negation=Regex("\\b(?:never|do not|don't|don’t|must not|should not|cannot|can't|can’t|not to)\\s+(?:(?:ever|again|please|really|just|even|you|under any circumstances)\\s+){0,3}$")
    private val exception=Regex("\\b(?:except|unless|but|however|instead|actually)\\b")
    private val quote=Regex("[\"“]([^\"”]{1,180})[\"”]")
    private val before=Regex("(?:educational(?: example)?|quoted example|scam awareness|(?:a )?scam (?:may )?says?|attackers? (?:may )?(?:ask|say))\\s*[:,-]?\\s*$")
    private val after=Regex("^\\s*(?:is|are)\\s+(?:a |an )?(?:typical |common )?(?:scam|example|quoted|educational)")
    fun hasActiveRequest(normalized: String): Boolean {
        val quotes=quote.findAll(normalized).toList()
        for(match in request.findAll(normalized)) {
            val start=match.groups[1]!!.range.first
            val end=match.groups[3]!!.range.last+1
            val clauseStart=".!?;".maxOf { normalized.lastIndexOf(it,startIndex=maxOf(0,start-1)) }+1
            val prefix=normalized.substring(maxOf(clauseStart,start-80),start).replace(',', ' ').replace(Regex("\\s+")," ")
            if(negation.containsMatchIn(prefix) && !exception.containsMatchIn(match.groupValues[2])) continue
            val educational=quotes.any { q ->
                val span=q.groups[1]!!.range
                span.first<=start && end<=span.last+1 && (before.containsMatchIn(normalized.substring(maxOf(0,q.range.first-90),q.range.first)) || after.containsMatchIn(normalized.substring(q.range.last+1,minOf(normalized.length,q.range.last+81))))
            }
            if(!educational) return true
        }
        return false
    }
}

package `in`.deven.scamsentry.core

import java.net.IDN

/** Conservative parsing contract. No URL opening, DNS or platform URL normalization. */
object UrlLexicalV2 {
    const val VERSION = "lexical_v2"
    const val MAX_LINKS = 40
    private val pattern = Regex("(?iU)\\b(?:https?://|[a-z][a-z0-9+.-]*://|www\\.)[^\\s<>\"\\u200b]+|(?<![@\\w])(?:[a-z0-9-]+\\.)+(?:[a-z]{2,63}|xn--[a-z0-9-]{2,59})\\.?(?![\\w.-])(?:[/?:#][^\\s<>\"]*)?")
    data class Extraction(val addresses: List<UrlObservation>, val truncated: Boolean)
    fun extract(text: String): Extraction {
        val matches = pattern.findAll(text).take(MAX_LINKS + 1).toList()
        return Extraction(matches.take(MAX_LINKS).map { inspectOne(trim(it.value)) }, matches.size > MAX_LINKS)
    }
    private fun trim(value: String): String {
        var original=value.trimEnd('.', ',', ';', '!', ')', '}')
        while(original.endsWith(']') && original.count { it==']' }>original.count { it=='[' }) original=original.dropLast(1)
        return original
    }
    private fun ipv6(host: String): Boolean {
        if (!host.contains(':') || host.contains(":::")) return false
        val compressed = host.contains("::")
        if (!compressed && (host.startsWith(':') || host.endsWith(':'))) return false
        if (compressed && host.indexOf("::") != host.lastIndexOf("::")) return false
        val parts = host.split(':').filter { it.isNotEmpty() }
        if (parts.any { !Regex("[a-fA-F0-9]{1,4}").matches(it) }) return false
        return if (compressed) parts.size < 8 else parts.size == 8
    }
    fun inspectOne(original: String): UrlObservation = try {
        val candidate = if (original.contains("://")) original else "https://$original"
        val scheme = candidate.substringBefore("://").lowercase()
        require(scheme in listOf("http", "https"))
        require(candidate.none { it.code < 33 || it in "\\\u200b\u200c\u200d\ufeff" })
        val authority = candidate.substringAfter("://").split(Regex("[/?#]"), limit = 2)[0]
        require(authority.count { it == '@' } <= 1)
        val hostPort = authority.substringAfterLast('@')
        var port: String? = null
        val host: String
        if (hostPort.startsWith('[')) {
            val match = requireNotNull(Regex("\\[([0-9a-fA-F:]+)](?::([0-9]+))?").matchEntire(hostPort))
            host = match.groupValues[1].lowercase()
            require(ipv6(host))
            port = match.groupValues[2].ifEmpty { null }
        } else {
            require(hostPort.count { it == ':' } <= 1)
            val parts = hostPort.split(':')
            val raw = parts[0].trimEnd('.')
            if (parts.size == 2) { port = parts[1]; require(Regex("[0-9]+").matches(port)) }
            require(raw.none { it in "%ßςẞ" })
            host = IDN.toASCII(raw).lowercase()
            require(host.isNotEmpty() && host.length <= 253 && host.split('.').all { Regex("[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?").matches(it) })
            if (Regex("[0-9.]+").matches(host)) {
                val octets = host.split('.')
                require(octets.size == 4 && octets.all { it.toIntOrNull() in 0..255 && (it == "0" || !it.startsWith('0')) })
            } else require(!Regex("[0-9]+|0x[0-9a-f]+").matches(host.substringAfterLast('.')))
        }
        if (port != null) require(port.toIntOrNull() in 1..65535)
        val codes = buildList {
            if ('@' in authority) add("userinfo")
            if ("xn--" in host) add("idn")
            if (scheme == "http") add("unencrypted_scheme")
            if (host in listOf("bit.ly", "tinyurl.com", "t.co")) add("shortener")
            if (original.codePointCount(0, original.length) > 180) add("long_address")
        }
        UrlObservation(original, host, codes, true)
    } catch (_: IllegalArgumentException) {
        UrlObservation(original, null, listOf("unsupported_or_malformed"), false)
    }
}

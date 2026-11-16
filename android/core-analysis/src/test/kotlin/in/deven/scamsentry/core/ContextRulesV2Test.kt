package `in`.deven.scamsentry.core
import org.junit.Assert.*
import org.junit.Test

class ContextRulesV2Test {
    @Test fun unrelatedWarningsAndExamplesDoNotSuppressRequests() {
        listOf("Please send your OTP. For example, use the form.","Never share your OTP. Please send your password now.","Do not share this notice. Send your OTP now.","Educational notice. Give your PIN to confirm.","Do not share anything except your OTP.","Never share your OTP; however send your password.","Never share your OTP.\nPlease reveal your verification code.","Do not share this notice send your OTP now.").forEach { text -> assertTrue(text,Rules.inspect(text).any { it.code=="secret_request" }) }
    }
    @Test fun localNegationAndAttachedQuotesRemainContextual() {
        listOf("Never share your OTP.","Do not send your password to anyone.","You must not ever reveal your PIN.","Don’t share your OTP with strangers.","Educational example: \"Send your OTP\" is suspicious.","A scam may say \"send your password\". Verify independently.","Scam awareness: “give your PIN” is a typical request to avoid.").forEach { text -> assertFalse(text,Rules.inspect(text).any { it.code=="secret_request" }) }
    }
}

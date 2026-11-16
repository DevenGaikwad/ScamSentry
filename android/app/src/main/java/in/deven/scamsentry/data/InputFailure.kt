package `in`.deven.scamsentry.data

enum class InputFailureCode { UNSUPPORTED_IMAGE, IMAGE_TOO_LARGE, DECODE_FAILED, PROVIDER_UNAVAILABLE, OCR_FAILED }
class InputFailure(val code: InputFailureCode, val publicMessage: String) : Exception(publicMessage)

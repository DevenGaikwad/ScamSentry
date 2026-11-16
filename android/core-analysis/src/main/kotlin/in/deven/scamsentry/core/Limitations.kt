package `in`.deven.scamsentry.core

object Limitations {
    val messages = mapOf(
        "model_unavailable" to "The validated message model is unavailable. Only local observations are available.",
        "unsupported_scope" to "English messages are the research scope. Script checks do not establish language support; Marathi, Hindi and Hinglish are not validated.",
        "short_ocr" to "Very little OCR text was available. Replace the image or enter a complete message.",
        "ocr_review" to "OCR characters can be mistaken. Review the extracted text before relying on observations.",
        "weak_features" to "Too few usable model features were found for reliable statistical interpretation.",
        "repetitive_input" to "Highly repetitive text is outside the evaluated message scope.",
        "standalone_url" to "Standalone address: lexical observations only; message estimates are not website-risk probabilities.",
        "link_limit" to "Only the first 40 addresses were inspected. Shorten the input to inspect every destination.",
        "research_scope" to "This historical research model cannot verify the sender or guarantee legitimacy.",
        "lexical_only" to "Links were inspected as text. No destination or reputation service was contacted.",
        "invalid_input" to "Enter a valid nonempty excerpt of at most 4,000 Unicode code points."
    )
}

package de.embrandt.aostracker.domain.model

import androidx.annotation.StringRes
import de.embrandt.aostracker.R

enum class GrandStrategy(
    @StringRes val nameResource: Int,
    @StringRes val descriptionRessource: Int
) {
    PrizedSorcery(R.string.grandStrategy_prizedSorcery, R.string.notImplemented)
}

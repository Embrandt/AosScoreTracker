package de.embrandt.aostracker.domain.model

import androidx.annotation.StringRes
import de.embrandt.aostracker.R

enum class GrandStrategy(
    @StringRes val nameResource: Int,
    @StringRes val descriptionRessource: Int
) {
    SeverTheHead(R.string.grandStrategy_severTheHead,R.string.notImplemented),
    HoldTheLine(R.string.grandStrategy_holdTheLine,R.string.notImplemented),
    Vendetta(R.string.grandStrategy_vendetta,R.string.notImplemented),
    DominatingPresence(R.string.grandStrategy_dominatingPresence,R.string.notImplemented),
    BeastMaster(R.string.grandStrategy_beastMaster,R.string.notImplemented),
    PrizedSorcery(R.string.grandStrategy_prizedSorcery,R.string.notImplemented),
    PillarsOfBelief(R.string.grandStrategy_pillarsOfBelief,R.string.notImplemented),
    PredatorsDomain(R.string.grandStrategy_predatorsDomain,R.string.notImplemented),

}

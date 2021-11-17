package de.embrandt.aostracker.domain.model

import androidx.annotation.StringRes

sealed class GrandStrategy (@StringRes val nameResource: Int, @StringRes val descriptionRessource : Int){
object PrizedSorcery : GrandStrategy(0,0)
}

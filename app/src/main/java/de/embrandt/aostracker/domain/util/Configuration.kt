package de.embrandt.aostracker.domain.util

import de.embrandt.aostracker.domain.model.Faction

object Configuration {
    val factions : List<Faction> = initializeFactions()
}

private fun initializeFactions() : List<Faction> {
    return listOf<Faction>(Faction("Stormcast Eternals"),Faction("Orruks"))
}
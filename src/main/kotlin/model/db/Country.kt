package self.adragon.model.db

import kotlinx.serialization.Serializable

@Serializable
data class Country(val name: String, val iso2: String)

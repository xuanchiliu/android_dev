package edu.stanford.googlemaps.models

import java.io.Serializable

data class UserMap(val title: String, val places: List<Place>): Serializable
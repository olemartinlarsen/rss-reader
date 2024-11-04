package rss.reader.auth

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(val name: String)
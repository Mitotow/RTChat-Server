package models

import java.sql.Date

data class User(
    val id: Int?,
    var username: String,
    var addr: String?,
    var token: String?,
    val created_at: Date?,
    val updated_at: Date?,
)

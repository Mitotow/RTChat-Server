package models

import java.sql.Date

data class Message(
    val id: Int?,
    val content: String,
    val user_id: Int,
    val sended_at: Date
)
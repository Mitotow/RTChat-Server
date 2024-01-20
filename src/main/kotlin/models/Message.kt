package models

import dao.DbMessages
import java.sql.Date

data class Message(
    val id: Int?,
    val content: String,
    val user_id: Int,
    val sended_at: Date?,
    val updated_at: Date?,
): Deletable {
    override fun delete(): Int = DbMessages.delete(this)

    override fun forceDelete(): Int = DbMessages.forceDelete(this)

}
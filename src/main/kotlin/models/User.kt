package models

import dao.DbUsers
import java.sql.Date

data class User(
    val id: Int?,
    var username: String,
    var addr: String?,
    var token: String?,
    val created_at: Date?,
    val updated_at: Date?,
): Deletable {
    override fun delete() = DbUsers.delete(this)

    override fun forceDelete() = DbUsers.forceDelete(this)

}

package dao

import models.Message

class DbMessages {
    companion object : DBTable<Message> {
        override fun all(): ArrayList<Message> {
            TODO("Not yet implemented")
        }

        override fun findId(id: Int): Message? {
            TODO("Not yet implemented")
        }

        override fun insert(element: Message): Message? {
            TODO("Not yet implemented")
        }
    }
}
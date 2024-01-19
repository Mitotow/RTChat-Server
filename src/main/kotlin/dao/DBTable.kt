package dao

interface DBTable<E> {
    fun all(): ArrayList<E>
    fun findId(id : Int): E?
    fun insert(element: E): E?
}
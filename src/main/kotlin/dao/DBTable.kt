package dao

import java.sql.ResultSet

interface DBTable<E> {
    /**
     * Retrieves a list of all non-deleted elements found in the table.
     *
     * @return List of all non-deleted elements found in the table.
     */
    fun all(): ArrayList<E>

    /**
     * Finds an element in the table with the specified ID.
     *
     * @param id - The ID of the object you want to find.
     * @return An element from the table with the requested ID or null if not found.
     */
    fun findId(id: Int): E?

    /**
     * Inserts the specified object into the table.
     *
     * @param element - The object you want to insert into the table.
     * @return The element with its generated ID or null.
     */
    fun insert(element: E): E?

    /**
     * Updates the specified object in the table. Fields that are not updated include: id, created_at, updated_at, deleted_at.
     *
     * @param element - The object you want to update in the table.
     * @return The updated element or null if the ID of the given element was null.
     */
    fun update(element: E): E?

    /**
     * Deletes the specified object from the table by giving to column deleted_at current timestamp.
     *
     * @param element - The object you want to delete.
     * @return The number of rows affected by the deletion.
     */
    fun delete(element: E): Int

    /**
     * Deletes the specified object from the table.
     *
     * @param element - THe object you want to delete.
     * @return The number of rows affected by the deletion.
     */
    fun forceDelete(element: E): Int

    /**
     * Converts a ResultSet to an ArrayList of type E.
     *
     * @param rs - The ResultSet you want to convert.
     * @return The ArrayList of E objects.
     */
    fun convert(rs : ResultSet): ArrayList<E>
}
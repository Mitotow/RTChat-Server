package models

interface Deletable {
    fun delete(): Int
    fun forceDelete(): Int
}
package controllers

abstract class Controller(val cmd:String) {
    abstract fun execute(args:List<String>)
}
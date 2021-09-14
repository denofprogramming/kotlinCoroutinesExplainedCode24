import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlin.system.measureTimeMillis

val scope = CoroutineScope(Job())


fun main() = runBlocking {
    val time = measureTimeMillis {
        logMessage("${bigRun()}")
    }
    logMessage("Time taken... $time")
}


suspend fun bigRun(): Int {

    //TODO - 3 We need to create a new Actor

    val x = 100  // number of coroutines to launch
    val y = 1000 // times an action is repeated by each coroutine


    coroutineScope {
        repeat(x) {
            launch {
                repeat(y) {
                    //TODO 4 - Lets increment the Count

                }
            }
        }
    }
    //TODO -5  Lets send an action to get the Count value from an Actor

    //TODO -6 Lets close the Actor

    return total
}

//TODO - 1 Create an Actor
fun countActor(): SendChannel<Action> {

}

fun logMessage(msg: String) {
    println("Running on: [${Thread.currentThread().name}] | $msg")
}

//TODO -2 Create some Action(s) to work in the Actor
sealed interface Action

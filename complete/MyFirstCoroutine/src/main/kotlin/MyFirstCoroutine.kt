import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlin.system.measureTimeMillis

val scope = CoroutineScope(Job())


fun main() = runBlocking {
    val time = measureTimeMillis {
        logMessage("${bigRun()}")
    }
    logMessage("Time taken... $time")
}


suspend fun bigRun(): Int {

    val actor = countActor()
    val x = 100  // number of coroutines to launch
    val y = 1000 // times an action is repeated by each coroutine


    coroutineScope {
        repeat(x) {
            launch {
                repeat(y) { actor.send(IncrementAction) }
            }
        }
    }
    // send an action to get a value from an actor
    val response = CompletableDeferred<Int>()
    actor.send(GetTotalAction(response))
    val total = response.await()

    actor.close() // shutdown the actor
    return total
}

fun countActor(): SendChannel<Action> {
    return scope.actor {
        var counter = 0 // actor s

        consumeEach { action ->
            logMessage("action received in mailbox $action")
            when (action) {
                is IncrementAction -> counter++
                is GetTotalAction -> action.response.complete(counter)
            }
        }
    }
}

fun logMessage(msg: String) {
    println("Running on: [${Thread.currentThread().name}] | $msg")
}


sealed interface Action
object IncrementAction : Action // one-way Action to increment
class GetTotalAction(val response: CompletableDeferred<Int>) : Action // a request with reply
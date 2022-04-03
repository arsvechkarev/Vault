package kotea.impl

import kotea.CommandsFlowHandler
import kotea.Store
import kotea.Update
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicBoolean

internal class StoreImpl<State : Any, Event : Any, UiEvent : Event, Command : Any, News : Any>(
    initialState: State,
    private val commandsFlowHandlers: List<CommandsFlowHandler<Command, Event>>,
    private val update: Update<State, Event, Command, News>
) : Store<State, UiEvent, News> {

    override var state = MutableStateFlow(initialState)
        private set

    private val eventChannel = Channel<Event>(capacity = Channel.UNLIMITED)
    private val commandChannel = Channel<Command>(capacity = Channel.UNLIMITED)
    private val newsChannel = Channel<News>(capacity = Channel.UNLIMITED)

    private val isLaunched = AtomicBoolean(false)

    override fun launchIn(coroutineScope: CoroutineScope) {
        if (isLaunched.getAndSet(true)) error("Store has already been launched")

        val commandsFlow: Flow<Command> = commandChannel
            .consumeAsFlow()
            .shareIn(coroutineScope, SharingStarted.Eagerly)

        for (flowHandler in commandsFlowHandlers) {
            coroutineScope.launch(start = CoroutineStart.UNDISPATCHED) {
                @Suppress("detekt:TooGenericExceptionCaught")
                try {
                    flowHandler.handle(commandsFlow).collect(eventChannel::send)
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    throw CommandsFlowHandlerException(flowHandler::class.java, e)
                }
            }
        }

        coroutineScope.launch {
            while (isActive) {
                val event = eventChannel.receive()
                val next = update.update(state.value, event)
                if (next.state != null) {
                    state.value = next.state
                }
                for (command in next.commands) {
                    commandChannel.send(command)
                }
                for (news in next.news) {
                    newsChannel.send(news)
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override val news: Flow<News> = newsChannel
        .receiveAsFlow()
        .shareIn(GlobalScope, SharingStarted.WhileSubscribed())

    override fun dispatch(event: UiEvent) {
        eventChannel.trySend(event)
    }
}

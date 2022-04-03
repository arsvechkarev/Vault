package kotea

import kotea.impl.StoreImpl


/**
 * Create a new TEA [Store].
 *
 * How it works:
 * ```
 *
 *            UiEvent                         State Λ   Λ News
 *  View        │                                   │   │
 * -------------│-----------------------------------│---│------
 *              │    ┌──────────────<───────────────┤   │
 *              │    │                              │   │
 *              │    │        ╭────────────╮ State  │   │
 *              │    │  State │            ├────────┘   │
 *              V    └────────>            │ News       │
 *              │       Event │   Update   ├────────────┘
 *  Store       ├─────────────>            │ Commands
 *              │             │            ├───────────────┐
 *              Λ             ╰────────────╯               │
 *              │                                          │
 *              │         ╭─────────────────────╮          │
 *              │        ╭┴────────────────────╮│          │
 *              │ Events │                     ││ Commands │
 *              └────────┤ CommandsFlowHandler <───────────┘
 *                       │                     ├╯
 *                       ╰────────Λ───┬────────╯
 * -------------------------------│---│------------------------
 *  Model                         │   │
 *               Data (or events) ┘   V Side effects
 * ```
 *
 * @see Update
 * @see CommandsFlowHandler
 */
@Suppress("detekt:FunctionNaming")
fun <State : Any, Event : Any, UiEvent : Event, Command : Any, News : Any> KoteaStore(
    initialState: State,
    commandsFlowHandlers: List<CommandsFlowHandler<Command, Event>> = emptyList(),
    update: Update<State, Event, Command, News> = Update { _, _ -> Next() }
): Store<State, UiEvent, News> {
    return StoreImpl(initialState, commandsFlowHandlers, update)
}

package kotea.impl

import kotea.CommandsFlowHandler

internal class CommandsFlowHandlerException(
    handlerClass: Class<out CommandsFlowHandler<*, *>>,
    cause: Throwable
) : RuntimeException("Exception in ${handlerClass.canonicalName}", cause)

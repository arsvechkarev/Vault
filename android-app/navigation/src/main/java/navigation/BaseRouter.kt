package navigation

/**
 * BaseRouter is an abstract class to implement high-level navigation.
 *
 * Extend it to add needed transition methods.
 */
abstract class BaseRouter {
  
  internal val commandBuffer = CommandBuffer()
  
  protected fun executeCommands(vararg commands: Command) {
    commandBuffer.executeCommands(commands)
  }
}
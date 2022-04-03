package com.arsvechkarev.vault.core

import kotlinx.coroutines.Dispatchers as CoroutinesDispatchers

object DefaultDispatchers : Dispatchers {

    override val IO = CoroutinesDispatchers.IO
    override val Default = CoroutinesDispatchers.Default
    override val Main = CoroutinesDispatchers.Main.immediate
}
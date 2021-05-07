package navigation

import com.github.terrakok.cicerone.Cicerone

interface RouterProvider {
  
  fun provideRouter(): Router
  
  fun provideCicerone(): Cicerone<Router>
  
  companion object {
    
    private var _instance: RouterProvider? = null
    val instance: RouterProvider
      get() = _instance ?: error("You should call initialize() first")
    
    fun initialize(routerProvider: RouterProvider) {
      _instance = routerProvider
    }
  }
}
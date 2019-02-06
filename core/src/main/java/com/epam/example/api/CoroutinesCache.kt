package com.epam.example.api

import com.epam.example.di.actionsModule
import com.epam.example.di.cacheModule
import com.epam.example.internal.ProxyProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import org.koin.standalone.StandAloneContext.loadKoinModules
import java.lang.reflect.Proxy

/**
 * Entry point of CoroutinesCache. In initialization loads koin modules to apply Dependency Injection.
 *
 * @param cacheParams - CacheParams. {@see CacheParams}
 * @param scope - CoroutinesScope. Scope of the threads, where coroutines will be run
 */
class CoroutinesCache(
        private val cacheParams: CacheParams,
        private val scope: CoroutineScope = GlobalScope
) {

    private lateinit var proxyProvider: ProxyProvider

    init {
        loadKoinModules(arrayListOf(cacheModule, actionsModule))
    }

    /**
     * Function that receive interface as param and create proxy on it.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> using(clazz: Class<*>): T {
        proxyProvider = ProxyProvider(cacheParams, scope)

        return Proxy.newProxyInstance(clazz.classLoader, arrayOf(clazz), proxyProvider) as T
    }
}
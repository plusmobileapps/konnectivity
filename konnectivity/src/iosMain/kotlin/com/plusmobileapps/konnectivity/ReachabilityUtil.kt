package com.plusmobileapps.konnectivity

import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSLog
import platform.SystemConfiguration.SCNetworkReachabilityFlags
import platform.SystemConfiguration.SCNetworkReachabilityFlagsVar
import platform.SystemConfiguration.SCNetworkReachabilityGetFlags
import platform.SystemConfiguration.SCNetworkReachabilityRef

internal interface ReachabilityUtil {
    fun getReachabilityFlags(
        reachabilityRef: SCNetworkReachabilityRef
    ): SCNetworkReachabilityFlags?
}

internal open class ReachabilityUtilImpl : ReachabilityUtil {

    override fun getReachabilityFlags(
        reachabilityRef: SCNetworkReachabilityRef
    ): SCNetworkReachabilityFlags? = memScoped {
        val flags = alloc<SCNetworkReachabilityFlagsVar>()
        return (if (SCNetworkReachabilityGetFlags(reachabilityRef, flags.ptr)) flags.value else null).also {
            NSLog("ReachabilityUtil getFlags - $it")
        }
    }
}
package com.plusmobileapps.konnectivity

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.NativePointed
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.alignOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.free
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.sizeOf
import kotlinx.cinterop.staticCFunction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import platform.Foundation.NSLog
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.SystemConfiguration.*
import platform.darwin.NSObjectProtocol
import platform.darwin.dispatch_queue_attr_make_with_qos_class
import platform.darwin.dispatch_queue_create
import platform.darwin.dispatch_queue_t
import platform.posix.*

actual fun Konnectivity(): Konnectivity {
    val stableRef = "some stable ref"
    val reachabilityUtil: ReachabilityUtil = ReachabilityUtilImpl()

    val sizeSockaddr = sizeOf<sockaddr_in>()
    val alignSockaddr = alignOf<sockaddr_in>()
    val zeroAddress =
        nativeHeap.alloc(sizeSockaddr, alignSockaddr).reinterpret<sockaddr_in>().apply {
            sin_len = sizeOf<sockaddr_in>().toUByte()
            sin_family = AF_INET.convert()

        }

    val reachabilityRef: SCNetworkReachabilityRef =
        SCNetworkReachabilityCreateWithAddress(null, zeroAddress.ptr.reinterpret<sockaddr>())
            ?: throw IllegalStateException("Failed on SCNetworkReachabilityCreateWithAddress")

    val konnectivity =
        KonnectivityImpl(reachabilityRef.getCurrentNetworkConnection(reachabilityUtil))

    val dispatchQueueAttr = dispatch_queue_attr_make_with_qos_class(null, QOS_CLASS_DEFAULT, 0)

    val reachabilitySerialQueue =
        dispatch_queue_create("com.plusmobileapps.konnectivity", dispatchQueueAttr)

    val notificationObserver = NSNotificationCenter.defaultCenter.addObserverForName(
        name = "ReachabilityChangedNotification",
        `object` = null,
        queue = NSOperationQueue.mainQueue,
        usingBlock = {
            konnectivity.onNetworkConnectionChanged(
                reachabilityRef.getCurrentNetworkConnection(reachabilityUtil)
            )
        }
    )


    val selfPtr = StableRef.create(stableRef)

    val sizeSCNetReachCxt = sizeOf<SCNetworkReachabilityContext>()
    val alignSCNetReachCxt = alignOf<SCNetworkReachabilityContext>()
    val context = nativeHeap.alloc(sizeSCNetReachCxt, alignSCNetReachCxt)
        .reinterpret<SCNetworkReachabilityContext>().apply {
        version = 0
        info = selfPtr.asCPointer()
        retain = null
        release = null
        copyDescription = null
    }

    val callback: SCNetworkReachabilityCallBack =
        staticCFunction { _: SCNetworkReachabilityRef?, _: SCNetworkReachabilityFlags, info: COpaquePointer? ->
            if (info == null) {
                return@staticCFunction
            }
            try {
                NSNotificationCenter.defaultCenter.postNotificationName(
                    "ReachabilityChangedNotification",
                    null
                )
            } catch (error: Throwable) {
                NSLog("SCNetworkReachabilityCallBack error: ${error.message}")
            }
        }

    if (!SCNetworkReachabilitySetCallback(reachabilityRef, callback, context.ptr)) {
        throw IllegalStateException("Failed on SCNetworkReachabilitySetCallback")
    }
    if (!SCNetworkReachabilitySetDispatchQueue(reachabilityRef, reachabilitySerialQueue)) {
        throw IllegalStateException("Failed on SCNetworkReachabilitySetDispatchQueue")
    }

    return konnectivity
}


private fun SCNetworkReachabilityRef.isConnected(util: ReachabilityUtil): Boolean {
    kSCNetworkReachabilityFlagsReachable
    val flags = getReachabilityFlags(util)
    val isReachable = flags.contains(kSCNetworkReachabilityFlagsReachable)
    val needsConnection = flags.contains(kSCNetworkReachabilityFlagsConnectionRequired)
    return isReachable && !needsConnection
}

private fun SCNetworkReachabilityRef.getCurrentNetworkConnection(util: ReachabilityUtil): NetworkConnection {
    val flags = getReachabilityFlags(util)
    val isReachable = flags.contains(kSCNetworkReachabilityFlagsReachable)
    val needsConnection = flags.contains(kSCNetworkReachabilityFlagsConnectionRequired)
    val isMobileConnection = flags.contains(kSCNetworkReachabilityFlagsIsWWAN)

    return when {
        !isReachable || needsConnection -> NetworkConnection.NONE
        isMobileConnection -> NetworkConnection.CELLULAR
        else -> NetworkConnection.WIFI
    }
}

private fun SCNetworkReachabilityRef.getReachabilityFlags(util: ReachabilityUtil): Array<SCNetworkReachabilityFlags> {
    val flags = util.getReachabilityFlags(this) ?: return emptyArray()

    val result = arrayOf<SCNetworkReachabilityFlags>(
        kSCNetworkReachabilityFlagsTransientConnection,
        kSCNetworkReachabilityFlagsReachable,
        kSCNetworkReachabilityFlagsConnectionRequired,
        kSCNetworkReachabilityFlagsConnectionOnTraffic,
        kSCNetworkReachabilityFlagsInterventionRequired,
        kSCNetworkReachabilityFlagsConnectionOnDemand,
        kSCNetworkReachabilityFlagsIsLocalAddress,
        kSCNetworkReachabilityFlagsIsDirect,
        kSCNetworkReachabilityFlagsIsWWAN,
        kSCNetworkReachabilityFlagsConnectionAutomatic
    ).filter {
        (flags and it) > 0u
    }
        .toTypedArray()
    NSLog("Konnectivity: SCNetworkReachabilityFlags: ${result.contentDeepToString()}")
    return result
}
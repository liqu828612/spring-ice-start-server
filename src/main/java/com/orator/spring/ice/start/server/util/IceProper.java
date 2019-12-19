package com.orator.spring.ice.start.server.util;

/**
 * @program: lotterywork
 * @description:
 * @author: JianJun.Qu
 * @create: 2019-12-17 01:05
 **/
public class IceProper {
    /**
     * see https://doc.zeroc.com/ice/3.6/property-reference/miscellaneous-ice-properties for details
     */
    public final static String BackgroundLocatorCacheUpdates = "Ice.BackgroundLocatorCacheUpdates";
    public final static String BatchAutoFlushSize = "Ice.BatchAutoFlushSize";
    public final static String CacheMessageBuffers = "Ice.CacheMessageBuffers";
    public final static String MessageSizeMax = "Ice.MessageSizeMax";
    public final static String WarnConnections = "Ice.Warn.Connections";
    public final static String PrintAdapterReady = "Ice.PrintAdapterReady";
    public final static String PrintProcessId = "Ice.PrintProcessId";
    public final static String OverrideTimeout = "Ice.Override.Timeout";
    public final static String TCP_BACKLOG = "Ice.TCP.Backlog";
    public final static String TCP_RCVSIZE = "Ice.TCP.RcvSize"; //513920
    public final static String TCP_SNDSIZE = "Ice.TCP.SndSize"; //513920
}

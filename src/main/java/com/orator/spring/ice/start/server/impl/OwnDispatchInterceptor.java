package com.orator.spring.ice.start.server.impl;

import Ice.DispatchInterceptor;
import Ice.DispatchStatus;
import Ice.Identity;
import Ice.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import Ice.Object;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by qujj on 16/1/23.
 */
public class OwnDispatchInterceptor extends DispatchInterceptor {
    private static Logger logger = LoggerFactory.getLogger(OwnDispatchInterceptor.class);

    //用来存放我们需要拦截的Ice服务对象，Key为服务ID，value为对应的Servant
    private static final Map<Identity, Object> id2ObjectMap = new ConcurrentHashMap<Identity, Object>();
    //单例模式
    private static final OwnDispatchInterceptor self = new OwnDispatchInterceptor();
    public static OwnDispatchInterceptor getInstance(){
        return self;
    }
    //添加我们要拦截的服务Servant
    public static DispatchInterceptor addIceObject(Identity id, Object iceObj){
        id2ObjectMap.put(id, iceObj);
        return self;
    }

    @Override
    public DispatchStatus dispatch(Request request)  {
        Identity theId = request.getCurrent().id;
        //request.getCurrent().con 会打印出来local address=localhost:50907
        //(回车换行)remote address=localhost:51147这样的信息
		/*其中local address为被访问的服务地址端口，remote address为客户端的地址端口*/
        String inf = "dispach req,method: "+request.getCurrent().operation
                +" service:"+theId.name
                +" server address:"+request.getCurrent().con;
        logger.info(inf+" begin");
        try{
            DispatchStatus reslt = id2ObjectMap.get(request.getCurrent().id).ice_dispatch(request);

            logger.info(inf+" success");
            return reslt;
        }catch(Exception e){
            logger.info(inf+" error "+e);
           // throw e;
            return  null;

        }
    }

    public static void removeIceObject(Identity id){
        logger.info("remove ice object "+id);
        id2ObjectMap.remove(id);
    }
}

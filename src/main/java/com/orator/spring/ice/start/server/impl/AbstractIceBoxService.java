package com.orator.spring.ice.start.server.impl;

import Ice.Communicator;
import Ice.Identity;
import IceBox.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Created by angle on 16/1/12.
 */
public abstract class AbstractIceBoxService implements Service {
    protected Ice.ObjectAdapter _adapter;
    protected Identity id;
    final static Logger logger = LoggerFactory.getLogger(AbstractIceBoxService.class);
    @Override
    public void start(String name, Communicator communicator, String[] args) {
        logger.info("name {} communicator {} args {}",name,communicator,args);
        //create objectAdapter，same service name
        _adapter = communicator.createObjectAdapter(name);
        //creatw servant active
        Ice.Object object = this.createMyIceServiceObj(args);
        id = communicator.stringToIdentity(name);
        //Interceptor
        _adapter.add(OwnDispatchInterceptor.addIceObject(id, object), id);
        _adapter.activate();
        logger.info(name+" service started ,with param size "+args.length+" detail:"+ Arrays.toString(args));
    }

    @Override
    public void stop() {
        logger.info("stopping service "+id+" ...");
        _adapter.destroy();
        OwnDispatchInterceptor.removeIceObject(id);
        logger.info("stopped service "+id+" stoped");

    }

    /**
     * 创建具体的Ice服务器实例对象
     * @param args 服务的配置参数，来自icegrid.xml文件
     * @return Ice.Object
     */
    public abstract Ice.Object createMyIceServiceObj(String[] args);
}

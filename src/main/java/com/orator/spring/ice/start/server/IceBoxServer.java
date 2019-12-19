package com.orator.spring.ice.start.server;

import Ice.Properties;
import com.alibaba.fastjson.JSON;

import com.orator.spring.ice.start.server.config.IceBoxProperties;
import com.orator.spring.ice.start.server.impl.DefaultServiceManager;
import com.orator.spring.ice.start.server.impl.LoggerI;
import com.orator.spring.ice.start.server.util.IceProper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @program: lotterywork
 * @description:
 * @author: JianJun.Qu
 * @create: 2019-12-17 00:29
 **/
public final class IceBoxServer  extends Ice.Application
{
    private static Logger logger = LoggerFactory.getLogger(IceBoxServer.class);
    private final ApplicationContext applicationContext;
    private final ApplicationArguments applicationArguments;
    private Ice.InitializationData initData = null;
    private String instanceName = null;

    /**
     *  Default batch flush size
     */
    private final static String DefaultBatchAutoFlushSize = "1024"; //KB
    /**
     * Max message size, 1024 KB by default
     */
    private final static String DefaultMessageSizeMax = "1024"; //KB


    public IceBoxServer serve() {
        logger.info("this.applicationArguments {}", JSON.toJSONString(this.applicationArguments));

       // String[] args={"--Ice.Config=/opt/ice/node/node3/data/servers/weixinPay1/config/config"};


        //System.exit(this.main(this.instanceName, args, this.initData));
        System.exit(this.main("IceBox.Server", applicationArguments.getSourceArgs(), this.initData));
        return this;
    }


    public IceBoxServer(ApplicationContext applicationContext, ApplicationArguments applicationArguments) {
        this.initData = new Ice.InitializationData();
        this.applicationContext = applicationContext;
        this.applicationArguments = applicationArguments;
    }


    private static void
    usage()
    {
        System.err.println("Usage: IceBox.Server [options] --IceProper.Config=<file>\n");
        System.err.println(
                "Options:\n" +
                        "-h, --help           Show this message.\n"
        );
    }

    public static void
    main(String[] args)
    {
        Ice.InitializationData initData = new Ice.InitializationData();
        initData.properties = Ice.Util.createProperties();
        initData.properties.setProperty("IceProper.Admin.DelayCreation", "1");

        IceBox.Server server = new IceBox.Server();
        System.exit(server.main("IceBox.Server", args, initData));
    }
    public IceBoxServer prepare(IceBoxProperties iceBoxProperties) {
       // logger.info("prepare icebox according to properties");
        //use customer logger
        Ice.Util.setProcessLogger(new LoggerI("spring-boot-starter-zeorcice by jiannun.qu"));
        initData.properties = Ice.Util.createProperties();
        this.buildDefault(initData.properties);
        if (!StringUtils.isEmpty(iceBoxProperties.getPrintServicesReady())){
            initData.properties.setProperty("IceBox.PrintServicesReady", iceBoxProperties.getPrintServicesReady());
        }
        //load orders
        if(!StringUtils.isEmpty(iceBoxProperties.getLoadOrder())){
            initData.properties.setProperty("IceBox.LoadOrder", iceBoxProperties.getLoadOrder());
        }
        initData.properties.setProperty("IceBox.InheritProperties", iceBoxProperties.getInheritProperties());
        if (iceBoxProperties.getServices() != null){
            for (IceBoxProperties.Service service: iceBoxProperties.getServices()) {
                String entry = service.getEntry();
                String endpoints = service.getEndpoints();
                if (!StringUtils.isEmpty(entry)){
                    initData.properties.setProperty(String.format("IceBox.Service.%s", service.getName()), entry);
                }
                if(!StringUtils.isEmpty(endpoints)){
                    initData.properties.setProperty(String.format("%s.Endpoints", service.getName()), endpoints);
                }
                if (!StringUtils.isEmpty(service.getUseSharedCommunicator())) {
                    initData.properties.setProperty(String.format("IceBox.UseSharedCommunicator.%s", service.getName()), service.getUseSharedCommunicator());
                }
            }
        }
        //Miscellaneous
        if(iceBoxProperties.getMiscellaneous() != null){
            for (Map.Entry<String, String> entry : iceBoxProperties.getMiscellaneous().entrySet()){
                initData.properties.setProperty(entry.getKey(), entry.getValue());
            }
        }

        String appName = String.format("IceBox.Server.%s", String.valueOf(System.currentTimeMillis()));
        if(!StringUtils.isEmpty(iceBoxProperties.getName())){
            appName = iceBoxProperties.getName();
        }

        for(Map.Entry<String, String> entry : initData.properties.getPropertiesForPrefix("").entrySet()){
         //   logger.info(entry.getKey() + " : " + entry.getValue());
        }

        this.instanceName = appName;
        return this;
    }

    private void buildDefault(Properties properties) {
        Assert.notNull(properties, "properties must not be null");
        properties.setProperty(IceProper.BackgroundLocatorCacheUpdates, "1");
        properties.setProperty(IceProper.BatchAutoFlushSize, DefaultBatchAutoFlushSize);
        properties.setProperty(IceProper.MessageSizeMax, DefaultMessageSizeMax);
        properties.setProperty(IceProper.WarnConnections, "1");
        properties.setProperty(IceProper.PrintAdapterReady, "1");
        properties.setProperty(IceProper.PrintProcessId, "1");
        properties.setProperty(IceProper.OverrideTimeout, "5000");
        properties.setProperty("Ice.Trace.ThreadPool", "10");
        properties.setProperty("IceBox.Trace.ServiceObserver", "1");
        properties.setProperty("IceProper.Admin.DelayCreation", "1");
    }

    @Override
    public int
    run(String[] args)
    {
        final String prefix = "IceBox.Service.";
        Ice.Properties properties = communicator().getProperties();
        Map<String, String> services = properties.getPropertiesForPrefix(prefix);
        java.util.List<String> argSeq = new java.util.ArrayList<String>(args.length);
        for(String s : args)
        {
            argSeq.add(s);
        }

        for(Map.Entry<String, String> entry : services.entrySet())
        {
            String name = entry.getKey().substring(prefix.length());
            for(int i = 0; i < argSeq.size(); ++i)
            {
                if(argSeq.get(i).startsWith("--" + name))
                {
                    argSeq.remove(i);
                    i--;
                }
            }
        }

        for(String arg : argSeq)
        {
            if(arg.equals("-h") || arg.equals("--help"))
            {
                usage();
                return 0;
            }
            else
            {
                System.err.println("Server: unknown option `" + arg + "'");
                usage();
                return 1;
            }
        }

        DefaultServiceManager serviceManagerImpl = new DefaultServiceManager(applicationContext,communicator(), args);
        return serviceManagerImpl.run();
    }
}
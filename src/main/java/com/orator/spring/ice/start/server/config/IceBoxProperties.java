package com.orator.spring.ice.start.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "icebox")
public class IceBoxProperties {

    private String name;
    /**
     * If this property is set to a value greater than zero, the service manager prints "token ready" on standard output once initialization of all the
     * services is complete. This is useful for scripts that need to wait until all services are ready to be used.
     */
    private String printServicesReady;

    /**
     * If num is set to a value larger than zero, each service inherits the configuration properties of the EnableIceBox server's communicator. If not defined,
     * the default value is 1.
     */
    private String inheritProperties = "1";
    /**
     * Determines the order in which services are loaded. The service manager loads the services in the order they appear in names, where each
     * service name is separated by a comma or white space. Any services not mentioned in names are loaded afterward, in an undefined order.
     */
    private String loadOrder;


    /**
     * Miscellaneous from ice
     * eg: IceProper.ACM.Timeout
     */
    private Map<String, String> miscellaneous;

    private List<Service> services;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrintServicesReady() {
        return printServicesReady;
    }

    public void setPrintServicesReady(String printServicesReady) {
        this.printServicesReady = printServicesReady;
    }

    public String getInheritProperties() {
        return inheritProperties;
    }

    public void setInheritProperties(String inheritProperties) {
        this.inheritProperties = inheritProperties;
    }

    public String getLoadOrder() {
        return loadOrder;
    }

    public void setLoadOrder(String loadOrder) {
        this.loadOrder = loadOrder;
    }

    public Map<String, String> getMiscellaneous() {
        return miscellaneous;
    }

    public void setMiscellaneous(Map<String, String> miscellaneous) {
        this.miscellaneous = miscellaneous;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public static class Service {
        private String name;
        private String entry;
        private String endpoints;
        private String useSharedCommunicator;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEntry() {
            return entry;
        }

        public void setEntry(String entry) {
            this.entry = entry;
        }

        public String getEndpoints() {
            return endpoints;
        }

        public void setEndpoints(String endpoints) {
            this.endpoints = endpoints;
        }

        public String getUseSharedCommunicator() {
            return useSharedCommunicator;
        }

        public void setUseSharedCommunicator(String useSharedCommunicator) {
            this.useSharedCommunicator = useSharedCommunicator;
        }
    }
}

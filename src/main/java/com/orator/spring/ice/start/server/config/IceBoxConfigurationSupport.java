package com.orator.spring.ice.start.server.config;
import IceBox.Service;
import com.orator.spring.ice.start.server.IceBoxServer;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

public class IceBoxConfigurationSupport implements ApplicationContextAware {

    //@Nullable
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

   // @Nullable
    public final ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    @Bean
    @ConditionalOnClass(Service.class)
    @ConditionalOnMissingBean
    @Scope("singleton")
    public IceBoxServer iceBoxServer(ApplicationArguments applicationArguments, IceBoxProperties iceBoxProperties) {
        return new IceBoxServer(applicationContext, applicationArguments).prepare(iceBoxProperties)
                .serve();
    }
}

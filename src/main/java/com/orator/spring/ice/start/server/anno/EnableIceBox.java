package com.orator.spring.ice.start.server.anno;

import com.orator.spring.ice.start.server.config.DelegatingIceBoxConfiguration;
import com.orator.spring.ice.start.server.config.IceBoxProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @program: lotterywork
 * @description:
 * @author: JianJun.Qu
 * @create: 2019-12-16 10:50
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableConfigurationProperties({IceBoxProperties.class})
@Import({DelegatingIceBoxConfiguration.class})
public @interface EnableIceBox {
}

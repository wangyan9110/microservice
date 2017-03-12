package info.yywang.demo.config;

import info.yywang.micro.framework.register.ServiceRegistry;
import info.yywang.micro.framework.register.ServiceRegistryImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shengguo
 * @version 1.0
 * @date 2017-03-07 23:23
 */
@Configuration
@ConfigurationProperties(prefix = "registry")
public class RegisterConfig {

    private String servers;

    @Bean
    public ServiceRegistry serviceRegistry(){
        return new ServiceRegistryImpl(servers);
    }

    public void setServers(String servers) {
        this.servers = servers;
    }
}

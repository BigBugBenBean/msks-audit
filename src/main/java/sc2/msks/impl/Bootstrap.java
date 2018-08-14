package sc2.msks.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import sc2.msks.impl.msgexchange.MessagingClient;

@SpringBootApplication
@PropertySource("file:${config.file.path}")
@ComponentScan(basePackages= {"com.pccw.sc2.audit.service.impl","sc2.msks.impl.msgexchange"})
public class Bootstrap implements  CommandLineRunner{

    @Autowired
    private MessagingClient sckclient;
//
//    @Autowired
//    private MessageFactory factory;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Bootstrap.class);
        app.setWebEnvironment(false);
        app.setHeadless(false);
        app.run(args);
    }

    @Override
    public void run(String... strings) throws Exception {
        sckclient.startMessaging();
    }
}

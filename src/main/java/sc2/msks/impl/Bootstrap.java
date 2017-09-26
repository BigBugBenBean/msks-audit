package sc2.msks.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import sc2.msks.impl.msgexchange.MessageFactory;
import sc2.msks.impl.msgexchange.MessagingClient;

import java.util.logging.Logger;

@SpringBootApplication
@PropertySource("classpath:config/msgexchange.properties")
public class Bootstrap implements  CommandLineRunner{

    private static Log log = LogFactory.getLog(Bootstrap.class);

    @Autowired
    private MessagingClient sckclient;

    @Autowired
    private MessageFactory factory;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public  static  void main(String[] args) throws  Exception
    {
        SpringApplication app = new SpringApplication(Bootstrap.class);
        app.setWebEnvironment(false);
        app.setHeadless(false);
        ConfigurableApplicationContext ctx = app.run(args);
    }

    @Override
    public void run(String... strings) throws Exception {
        sckclient.startMessaging();
    }
}

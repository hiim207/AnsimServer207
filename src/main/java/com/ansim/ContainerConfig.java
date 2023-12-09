package com.ansim;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.ajp.AbstractAjpProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContainerConfig {
    //내장 톰켓 설정

    @Value("${tomcat.ajp.protocol}")
    String ajpProtocol;

    @Value("${tomcat.ajp.port}")
    int ajpPort;

    @Value("${tomcat.ajp.enabled}")
    Boolean ajpEnabled;

    @Bean
    public TomcatServletWebServerFactory servlet() {

        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();

        if(ajpEnabled) {
            Connector ajpConnector = new Connector(ajpProtocol);
            ajpConnector.setPort(ajpPort);
            ajpConnector.setSecure(false);//SSL 쓰냐 안 쓰냐
            ajpConnector.setAllowTrace(true);
            ajpConnector.setScheme("http");
            tomcat.addAdditionalTomcatConnectors(ajpConnector);
            ((AbstractAjpProtocol<?>)ajpConnector.getProtocolHandler()).setSecretRequired(false);
        }

        return tomcat;
    }
}

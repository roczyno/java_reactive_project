package com.roczyno.reactive_project.infrastructure;


import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.h2.tools.Server;

import java.sql.SQLException;


@Configuration
//@Profile("!prod")
public class H2ConsoleConfiguration {

	private Server webServer;

	@EventListener(ApplicationStartedEvent.class)
	public void start() throws SQLException{
		String WEB_PORT = "8082";
		this.webServer=Server.createWebServer("-webPort", WEB_PORT).start();
	}
	@EventListener(ContextClosedEvent.class)
	public void stop(){
		this.webServer.stop();
	}

}

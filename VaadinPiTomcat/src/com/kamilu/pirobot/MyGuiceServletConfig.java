package com.kamilu.pirobot;

import java.io.Serializable;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

@SuppressWarnings("serial")
public class MyGuiceServletConfig extends GuiceServletContextListener implements
		Serializable {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new RobotModule());
	}
}

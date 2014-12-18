package com.example.magictrick;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("magictrick")
public class MagictrickUI extends UI {
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = MagictrickUI.class, widgetset = "com.example.magictrick.widgetset.MagictrickWidgetset")
	public static class Servlet extends VaadinServlet {
	}
	@Override
	protected void init(VaadinRequest request) {
		setContent(new PrincessCardTrick());	

	}
}
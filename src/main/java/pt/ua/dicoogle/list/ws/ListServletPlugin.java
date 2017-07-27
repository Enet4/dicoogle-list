/**
 * Copyright (C) 2017 UA.PT Bioinformatics - http://bioinformatics.ua.pt/
 *
 * This file is part of Dicoogle/list.
 *
 * Dicoogle/list is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Dicoogle/list is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Dicoogle.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ua.dicoogle.list.ws;

import javax.servlet.MultipartConfigElement;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ua.dicoogle.sdk.JettyPluginInterface;
import pt.ua.dicoogle.sdk.core.DicooglePlatformInterface;
import pt.ua.dicoogle.sdk.core.PlatformCommunicatorInterface;
import pt.ua.dicoogle.sdk.settings.ConfigurationHolder;

/**
 * @author Eduardo Pinho <eduardopinho@ua.pt>
 */
public class ListServletPlugin implements JettyPluginInterface, PlatformCommunicatorInterface {
    private static final Logger logger = LoggerFactory.getLogger(ListServletPlugin.class);
    
    private boolean enabled;
    private ConfigurationHolder settings;
    private DicooglePlatformInterface platform;
    private final ListWebServlet wsList;
    
    public ListServletPlugin() {
        this.wsList = new ListWebServlet();
        this.enabled = true;
    }

    @Override
    public void setPlatformProxy(DicooglePlatformInterface pi) {
        this.platform = pi;
        // since web service is not a plugin interface, the platform interface must be provided manually
        this.wsList.setPlatformProxy(pi);
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public boolean enable() {
        this.enabled = true;
        return true;
    }

    @Override
    public boolean disable() {
        this.enabled = false;
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setSettings(ConfigurationHolder settings) {
        this.settings = settings;
        // use settings here
        
    }

    @Override
    public ConfigurationHolder getSettings() {
        return settings;
    }


    @Override
    public HandlerList getJettyHandlers() {

        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath("/files");
        
        ServletHolder servletHolder = new ServletHolder(this.wsList);
        handler.addServlet(servletHolder, "/list");

        HandlerList l = new HandlerList();
        l.addHandler(handler);

        return l;
    }

}

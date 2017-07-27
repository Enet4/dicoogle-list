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
package pt.ua.dicoogle.list;

import pt.ua.dicoogle.list.ws.ListServletPlugin;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ua.dicoogle.sdk.GraphicalInterface;
import pt.ua.dicoogle.sdk.IndexerInterface;
import pt.ua.dicoogle.sdk.JettyPluginInterface;
import pt.ua.dicoogle.sdk.PluginSet;
import pt.ua.dicoogle.sdk.QueryInterface;
import pt.ua.dicoogle.sdk.StorageInterface;
import pt.ua.dicoogle.sdk.settings.ConfigurationHolder;

/** The main plugin set.
 * 
 * This is the entry point for all plugins.
 *
 * @author Eduardo Pinho <eduardopinho@ua.pt>
 */
@PluginImplementation
@SuppressWarnings("unused")
public class ListPluginSet implements PluginSet {
    // use slf4j for logging purposes
    private static final Logger logger = LoggerFactory.getLogger(ListPluginSet.class);
    
    // We will list each of our plugins as an attribute to the plugin set
    private final ListServletPlugin jettyWeb;
    
    // Additional resources may be added here.
    private ConfigurationHolder settings;
    
    public ListPluginSet() throws IOException {
        logger.debug("Initializing List Plugin Set");

        // construct all plugins here
        this.jettyWeb = new ListServletPlugin();
        
        logger.info("List Plugin Set is ready");
    }

    @Override
    public Collection<IndexerInterface> getIndexPlugins() {
        return Collections.emptyList();
    }

    @Override
    public Collection<QueryInterface> getQueryPlugins() {
        return Collections.emptyList();
    }
    
    /** This method is used to retrieve a name for identifying the plugin set. Keep it as a constant value.
     * 
     * @return a unique name for the plugin set
     */
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public Collection<ServerResource> getRestPlugins() {
        return Collections.emptyList();
    }

    @Override
    public Collection<JettyPluginInterface> getJettyPlugins() {
        return Collections.singleton(this.jettyWeb);
    }

    @Override
    public void shutdown() {
        logger.info("List plugin is shutting down");
    }

    @Override
    public Collection<StorageInterface> getStoragePlugins() {
        return Collections.emptyList();
    }

    @Override
    public void setSettings(ConfigurationHolder xmlSettings) {
        this.settings = xmlSettings;
    }

    @Override
    public ConfigurationHolder getSettings() {
        return this.settings;
    }

    @Override
    public Collection<GraphicalInterface> getGraphicalPlugins() {
        // Graphical plugins are deprecated. Do not use or provide any.
        return Collections.emptyList();
    }
}
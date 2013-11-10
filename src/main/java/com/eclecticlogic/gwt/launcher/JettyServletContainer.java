/**
 * Copyright 2013 Eclectic Logic LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eclecticlogic.gwt.launcher;

import java.io.File;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.webapp.WebAppContext;

import com.google.gwt.core.ext.ServletContainer;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

public class JettyServletContainer extends ServletContainer {

	private int maxIdleTime;

	private int port;

	private TreeLogger treeLogger;

	private File appRootDir;

	private Server server = new Server();

	private String contextPath;

	private WebAppContext webapp = new WebAppContext() {

		protected void doStart() throws Exception {
			setClassLoader(Thread.currentThread().getContextClassLoader());

			// This is straight from GWT's JettyLauncher.java adapted for
			// Eclipse Jetty.
			// Prevent file locking on Windows; pick up file changes.
			getInitParams().put(
					getJettyConfigurationName("useFileMappedBuffer"), "false");
			// Since the parent class loader is bootstrap-only, prefer it first.
			setParentLoaderPriority(false);

			super.doStart();
		};
	};

	private String getJettyConfigurationName(String parameterName) {
		return "org.eclipse.jetty.servlet.Default" + "." + parameterName;
	}

	@Override
	public void refresh() throws UnableToCompleteException {
		try {
			getWebapp().stop();
			getWebapp().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() throws UnableToCompleteException {
		try {
			getServer().stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() throws Exception {
		ServerConnector connector = new ServerConnector(getServer());
		connector.setPort(getPort());
		connector.setIdleTimeout(getMaxIdleTime());
		getServer().setConnectors(new Connector[] { connector });

		// TODO: Support for web.xml and jetty.xml configuration.
		// String configurations[] = { WebInfConfiguration.class.getName(),
		// EnvConfiguration.class.getName(),
		// JettyWebXmlConfiguration.class.getName() };
		// getWebapp().setConfigurationClasses(configurations);

		getWebapp().setContextPath(getContextPath());
		getWebapp().setWar(getAppRootDir().getAbsolutePath());

		HandlerList handlerList = new HandlerList();

		RequestLogHandler logHandler = new RequestLogHandler();
		// logHandler.setRequestLog(new JettyRequestLogger(getTreeLogger(),
		// TreeLogger.Type.TRACE));
		logHandler.setHandler(getWebapp());

		handlerList.addHandler(logHandler);

		getServer().setHandler(handlerList);

		getServer().start();
	}

	public int getMaxIdleTime() {
		return maxIdleTime;
	}

	public void setMaxIdleTime(int maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public TreeLogger getTreeLogger() {
		return treeLogger;
	}

	public void setTreeLogger(TreeLogger treeLogger) {
		this.treeLogger = treeLogger;
	}

	public File getAppRootDir() {
		return appRootDir;
	}

	public void setAppRootDir(File appRootDir) {
		this.appRootDir = appRootDir;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public WebAppContext getWebapp() {
		return webapp;
	}

	public void setWebapp(WebAppContext webapp) {
		this.webapp = webapp;
	}

}

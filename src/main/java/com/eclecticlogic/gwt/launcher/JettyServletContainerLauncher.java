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
import java.net.BindException;

import com.google.gwt.core.ext.ServletContainer;
import com.google.gwt.core.ext.ServletContainerLauncher;
import com.google.gwt.core.ext.TreeLogger;

public class JettyServletContainerLauncher extends ServletContainerLauncher {

	private static final int MAX_IDLE_TIME = 300000;

	// There is no elegant non-static way to pass this as DevMode creates an
	// instance of the launcher and
	// GWT hasn't promised any API for DevMode beyond main(String[] args).
	// aarggh :-(
	private static String contextPath;

	public static String getContextPath() {
		return contextPath;
	}

	public static void setContextPath(String contextPath) {
		JettyServletContainerLauncher.contextPath = contextPath;
	}

	@Override
	public ServletContainer start(TreeLogger logger, int port, File appRootDir)
			throws BindException, Exception {
		JettyServletContainer core = new JettyServletContainer();
		core.setPort(port);
		core.setContextPath(getContextPath());
		core.setAppRootDir(appRootDir);
		core.setTreeLogger(logger);

		core.setMaxIdleTime(MAX_IDLE_TIME);
		core.start();
		return core;
	}

}

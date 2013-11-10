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

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gwt.dev.DevMode;

/**
 * Main entry point class. Parameters specified per command line spec.
 * 
 */
public class DevModeLauncher {

	@Parameter(names = { "-p", "--port" }, description = "port to listen on, default 7000")
	private int port = 7000;

	@Parameter(names = { "-c", "--codeServerPort" }, description = "coder server port to listen on, default 8000")
	private int codeServerPort = 8000;

	@Parameter(names = { "-s", "--startupURL" }, description = "startup URL", required = true)
	private String startupUrl;

	@Parameter(names = { "-w", "--warPath" }, description = "war directory", required = true)
	private String warPath;

	@Parameter(names = { "-m", "--module" }, description = "gwt module name", required = true)
	private List<String> modules;

	@Parameter(names = { "-x", "--contextPath" }, description = "context path for webapp, default /")
	private String contextPath = "/";

	@Parameter(names = { "-d", "--workDir" }, description = "compiler working dir")
	private String compilerWorkingDir;

	@Parameter(names = { "-l", "--logDir" }, description = "logging dir")
	private String logDir;

	@Parameter(names = { "-v", "--logLevel" }, description = "log level (TRACE, INFO, etc.)")
	private String logLevel;

	@Parameter(names = { "-g", "--gen" }, description = "directory for writing generated files")
	private String generatedFilesDir;

	@Parameter(names = { "-b", "--bindAddress" }, description = "bind address, default 0.0.0.0 for all")
	private String bindAddress = "0.0.0.0";

	@Parameter(names = "--help", help = true)
	private boolean help;

	public void start() {
		List<String> params = new ArrayList<String>();
		// Internal
		params.add("-server");
		params.add(JettyServletContainerLauncher.class.getName());

		// Required
		params.add("-startupUrl");
		params.add(getStartupUrl());
		params.add("-war");
		params.add(getWarPath());

		// Optional but important
		params.add("-port");
		params.add(Integer.toString(getPort()));
		params.add("-codeServerPort");
		params.add(Integer.toString(getCodeServerPort()));

		// See comment in JettyServletContainerLauncher for this uglyness.
		JettyServletContainerLauncher.setContextPath(getContextPath());

		if (isNotBlank(getCompilerWorkingDir())) {
			params.add("-workDir");
			params.add(getCompilerWorkingDir());
		}
		if (isNotBlank(getGeneratedFilesDir())) {
			params.add("-gen");
			params.add(getGeneratedFilesDir());
		}
		if (isNotBlank(getBindAddress())) {
			params.add("-bindAddress");
			params.add(getBindAddress());
		}

		if (isNotBlank(getLogLevel())) {
			params.add("-logLevel");
			params.add(getLogLevel());
		}

		if (isNotBlank(getLogDir())) {
			params.add("-logdir");
			params.add(getLogDir());
		}

		// Modules are required but are added to the end.
		for (String module : getModules()) {
			params.add(module);
		}

		DevMode.main(params.toArray(new String[] {}));
	}

	public static void main(String[] args) {
		DevModeLauncher server = new DevModeLauncher();
		JCommander commander = new JCommander(server);
		try {
			commander.parse(args);
			server.start();
		} catch (ParameterException e) {
			System.err.println(e.getMessage());
			commander.usage();
		}
	}

	/* Getters and utility methods */

	private boolean isNotBlank(String value) {
		return value != null && value.trim().length() > 0;
	}

	public int getPort() {
		return port;
	}

	public int getCodeServerPort() {
		return codeServerPort;
	}

	public String getStartupUrl() {
		return startupUrl;
	}

	public String getWarPath() {
		return warPath;
	}

	public List<String> getModules() {
		return modules;
	}

	public String getContextPath() {
		return contextPath;
	}

	public String getCompilerWorkingDir() {
		return compilerWorkingDir;
	}

	public String getGeneratedFilesDir() {
		return generatedFilesDir;
	}

	public String getBindAddress() {
		return bindAddress;
	}

	public boolean isHelp() {
		return help;
	}

	public String getLogDir() {
		return logDir;
	}

	public String getLogLevel() {
		return logLevel;
	}

}

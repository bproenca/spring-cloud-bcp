package br.com.bcp.environment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class InstanceInformationService {

	private static final String HOST_NAME = "HOSTNAME";
	private static final String DEFAULT_ENV_INSTANCE_GUID = "UNKNOWN";
	private static final Logger LOGGER = LoggerFactory.getLogger(InstanceInformationService.class);

	// @Value(${ENVIRONMENT_VARIABLE_NAME:DEFAULT_VALUE})
	@Value("${" + HOST_NAME + ":" + DEFAULT_ENV_INSTANCE_GUID + "}")
	private String hostName;

	@Autowired
	private Environment env;

	public String retrieveInstanceInfo() {
		if ("UNKNOWN".equals(hostName)) {
			try {
				InetAddress localHost = InetAddress.getLocalHost();
				hostName = localHost.getHostName() + ":" + env.getProperty("local.server.port");
			} catch (UnknownHostException e) {
				LOGGER.error("Error trying InetAddress.getLocalHost()", e);
			}
		}

		return hostName + " v1 ";
	}

}
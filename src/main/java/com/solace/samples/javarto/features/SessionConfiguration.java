/*
 * Copyright 2006-2021 Solace Corporation. All rights reserved. */
package com.solace.samples.javarto.features;

import java.util.HashMap;
import java.util.Map;
import com.solace.samples.javarto.features.SampleUtils.UserVpn;
import com.solacesystems.solclientj.core.SolEnum;

/**
 * Container for session properties used in configuring a JCSMPSession.
 */
public class SessionConfiguration {

	public static enum AuthenticationScheme {
		BASIC, CLIENT_CERTIFICATE, KERBEROS
	};

	// Router properties
	private String host;

	private UserVpn routerUserVpn;

	private String routerPassword;

	private int delMode = SolEnum.MessageDeliveryMode.DIRECT;

	private Map<String, String> argBag = new HashMap<String, String>();

	private boolean compression = false;

	private AuthenticationScheme authScheme = AuthenticationScheme.BASIC;

	public Map<String, String> getArgBag() {
		return argBag;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String value) {
		host = value;
	}

	public String getRouterPassword() {
		return routerPassword;
	}

	public SessionConfiguration setRouterPassword(String routerPassword) {
		this.routerPassword = routerPassword;
		return this;
	}

	public UserVpn getRouterUserVpn() {
		return routerUserVpn;
	}

	public SessionConfiguration setRouterUsername(UserVpn routerUserVpn) {
		this.routerUserVpn = routerUserVpn;
		return this;
	}

	public int getDeliveryMode() {
		return delMode;
	}

	public void setDeliveryMode(int mode) {
		this.delMode = mode;
	}

	public boolean isCompression() {
		return compression;
	}

	public void setCompression(boolean compression) {
		this.compression = compression;
	}

	public AuthenticationScheme getAuthenticationScheme() {
		return authScheme;
	}

	public void setAuthenticationScheme(AuthenticationScheme authScheme) {
		this.authScheme = authScheme;
	}

	@Override
	public String toString() {
		StringBuilder bldr = new StringBuilder();
		bldr.append("host=");
		bldr.append(host);
		if (routerUserVpn != null) {
			bldr.append(", username=");
			bldr.append(routerUserVpn.get_user());
			if (routerUserVpn.get_vpn() != null) {
				bldr.append(", vpn=");
				bldr.append(routerUserVpn.get_vpn());
			}
		}
		bldr.append(", password=");
		bldr.append(routerPassword);
		bldr.append(", compression=");
		bldr.append(compression);
		bldr.append(", authScheme=");
		bldr.append(authScheme);
		return bldr.toString();
	}

}

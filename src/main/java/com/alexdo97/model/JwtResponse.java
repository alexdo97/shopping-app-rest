package com.alexdo97.model;

public class JwtResponse {

	private Identity identity;
	private String jwtToken;

	public JwtResponse(Identity identity, String jwtToken) {
		this.identity = identity;
		this.jwtToken = jwtToken;
	}

	public Identity getIdentity() {
		return identity;
	}

	public void setIdentity(Identity identity) {
		this.identity = identity;
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}
}

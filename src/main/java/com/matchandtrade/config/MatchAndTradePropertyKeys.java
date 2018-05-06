package com.matchandtrade.config;

public enum MatchAndTradePropertyKeys {
	AUTHENTICATION_CLIENT_ID("authentication.client.id"),
	AUTHENTICATION_CLIENT_SECRET("authentication.client.secret"),
	AUTHENTICATION_CLIENT_REDIRECT_URL("authentication.redirect.url"),
	AUTHENTICATION_CLIENT_CALLBACK_URL("authentication.callback.url"),
	AUTHENTICATION_SESSION_TIMEOUT("authentication.session.timeout"),
	AUTHENTICATION_OAUTH_CLASS("authentication.oauth.class"),
	CONFIG_FILE("matchandtrade.config.file"),
	DATA_SOURCE_DRIVER_CLASS("datasource.driver.class"),
	DATA_SOURCE_JDBC_URL("datasource.jdbc.url"),
	DATA_SOURCE_PASSWORD("datasource.password"),
	DATA_SOURCE_USER("datasource.user"),
	LOGGING_FILE("logging.file"),
	FILE_STORAGE_ROOT_FOLDER("file.storage.root.folder"),
	SERVER_PORT("server.port");

	private final String key;

	MatchAndTradePropertyKeys(final String key) {
        this.key = key;
    }
	
	@Override
	public String toString() {
		return key;
	}
}

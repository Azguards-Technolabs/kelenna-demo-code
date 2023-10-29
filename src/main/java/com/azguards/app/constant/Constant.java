package com.azguards.app.constant;

public class Constant {

	public static final String STORAGE = "STORAGE-SERVICE/storage/api/v1";
	
	public static final String NOTIFICATION = "NOTIFICATION-SERVICE/notification/api/v1";

	public static final String STORAGE_CONNECTION_URL = "http://" + STORAGE;

	public static final String NOTIFICATION_CONNECTION_URL = "http://" + NOTIFICATION + "/push";

	public static final String ELASTIC_SEARCH = "ELASTIC-SEARCH/elasticSearch/";
	
	public static final String ELASTIC_SEARCH_INDEX_IDENTITY = "azguards_dev_identity";
	
	public static final String STORAGE_BASE_PATH = "http://STORAGE-SERVICE/storage";
	
	public static final String IDENTITY_BASE_PATH = "http://AUTH-SERVICE/identity";

	public static final String USER_BASE_PATH = "http://USER-SERVICE/users";

	public static final String CONNECTION_BASE_PATH = "http://CONNECTIONS/connection";
}

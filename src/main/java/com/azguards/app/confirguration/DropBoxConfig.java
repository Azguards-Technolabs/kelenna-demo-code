package com.azguards.app.confirguration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "dropbox")
@Data
public class DropBoxConfig {
	private String accesToken;
	private String sharedFolderLink;
	
	@Bean
	public DbxClientV2 dropBoxClient() {
		DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/azguards-migration").build();
        return new DbxClientV2(config, accesToken);
	}
}

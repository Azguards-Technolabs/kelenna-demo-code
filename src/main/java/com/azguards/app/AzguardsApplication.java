package com.azguards.app;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration;
import org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.azguards.app.processor.MasterDataImportProcessor;
import com.azguards.app.util.FileStorageProperties;

@Configuration
@EnableScheduling
@EnableEurekaClient
@ComponentScan(basePackages = "com.azguards")
@SpringBootApplication(exclude = {ContextRegionProviderAutoConfiguration.class, ContextStackAutoConfiguration.class})
@EnableConfigurationProperties({ FileStorageProperties.class })
@EnableAsync
@EnableCaching
public class AzguardsApplication {

	@Autowired
	private MasterDataImportProcessor masterDataImportProcessor;
	
	public static void main(final String[] args) {
		SpringApplication.run(AzguardsApplication.class, args);
	}

	@Bean("fixedThreadPool")
    public ExecutorService fixedThreadPool() {
        return Executors.newFixedThreadPool(10);
    }
	
	@EventListener(ApplicationStartedEvent.class)
	public void appInit() {
		masterDataImportProcessor.importInstituteCategoryType();
	}
	
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		modelMapper.getConfiguration().setDeepCopyEnabled(true);
		return modelMapper;
	}
}

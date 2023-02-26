package com.kevin.kevinapiclientsdk;

import com.kevin.kevinapiclientsdk.client.KevinApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Projectname: kevinapi-backend
 * @Filename: KevinApiClientConfig
 * @Author: skw
 */
@Configuration
@ConfigurationProperties("yuapi.client")
@Data
@ComponentScan
public class KevinApiClientConfig {
	private String accessKey;

	private String secretKey;

	@Bean
	public KevinApiClient yuApiClient() {
		return new KevinApiClient(accessKey, secretKey);
	}
}

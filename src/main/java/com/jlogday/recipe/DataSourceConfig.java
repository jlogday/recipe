package com.jlogday.recipe;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

	@Bean
	public DataSource getDataSource() {
		var builder = DataSourceBuilder.create();
		builder.driverClassName("org.postgresql.Driver");
		builder.url("jdbc:postgresql://localhost:51846/postgres");
		builder.username("myuser");
		builder.password("secret");
		
		return builder.build();
	}
}

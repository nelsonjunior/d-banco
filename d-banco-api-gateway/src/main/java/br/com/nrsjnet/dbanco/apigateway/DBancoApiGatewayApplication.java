package br.com.nrsjnet.dbanco.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

@SpringBootApplication
public class DBancoApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(DBancoApiGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p
						.path("/v1/contas")
						.and().method(HttpMethod.POST)
						.uri("http://localhost:8081/"))
				.route(p -> p
						.path("/v1/contas/depositar")
						.uri("http://localhost:8082/"))
				.route(p -> p
						.path("/v1/contas/sacar")
						.uri("http://localhost:8082/"))
				.route(p -> p
						.path("/v1/contas/transferir")
						.uri("http://localhost:8082/"))
				.route(p -> p
						.path("/v1/contas/*/extrato")
						.uri("http://localhost:8083/"))
				.route(p -> p
						.path("/v1/contas/**")
						.and().method(HttpMethod.GET)
						.uri("http://localhost:8083/"))
				.build();
	}

}

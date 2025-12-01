package com.example.bajaj_webhook;

import com.example.bajaj_webhook.model.GenerateWebhookResponse;
import com.example.bajaj_webhook.service.QuestionFetcher;
import com.example.bajaj_webhook.service.WebhookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class BajajWebhookApplication {

	private static final Logger log = LoggerFactory.getLogger(BajajWebhookApplication.class);

	@Value("${bfhl.name}")
	private String name;

	@Value("${bfhl.regNo}")
	private String regNo;

	@Value("${bfhl.email}")
	private String email;

	public static void main(String[] args) {
		SpringApplication.run(BajajWebhookApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public CommandLineRunner runner(WebhookService webhookService, QuestionFetcher questionFetcher) {
		return args -> {

			log.info("Starting webhook flow for regNo {}", regNo);

			GenerateWebhookResponse resp = webhookService.generateWebhook(name, regNo, email);
			if (resp == null) {
				log.error("Webhook generation failed");
				return;
			}

			log.info("Webhook = {}", resp.getWebhook());
			log.info("Token = {}", mask(resp.getAccessToken()));

			// Always load Question 1 SQL for REG ending in 27
			String sql = questionFetcher.loadFinalSql();
			if (sql == null) {
				log.error("Could not load SQL");
				return;
			}

			boolean ok = webhookService.submitFinalQuery(resp.getWebhook(), resp.getAccessToken(), sql);
			if (ok) log.info("Submission success");
			else log.error("Submission failed");
		};
	}

	private String mask(String t) {
		if (t == null) return null;
		return t.substring(0, 8) + "...";
	}
}

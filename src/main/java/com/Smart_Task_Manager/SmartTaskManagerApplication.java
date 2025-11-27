package com.Smart_Task_Manager;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.SecretKey;

@SpringBootApplication
public class SmartTaskManagerApplication {
    static Logger detailedLogger = org.slf4j.LoggerFactory.getLogger("detailedLogger");

	public static void main(String[] args) {
        String key = Encoders.BASE64.encode(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());
        detailedLogger.info("Generated JWT Secret Key: {}", key);
        SpringApplication.run(SmartTaskManagerApplication.class, args);
	}

}

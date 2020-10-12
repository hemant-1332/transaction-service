package com.demo.transactionService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class TransactionServiceApplication {

	private static final String SPRING_CONFIG_NAME_KEY = "--spring.config.name";
	private static final String DEFAULT_SPRING_CONFIG_PARAM = SPRING_CONFIG_NAME_KEY + "=" + "application";

	public static void main(String[] args) {
		SpringApplication.run(TransactionServiceApplication.class, args);
	}

	private static String[] updateArguments(String[] args) {
		if (Arrays.stream(args).noneMatch(arg -> arg.startsWith(SPRING_CONFIG_NAME_KEY))) {
			String[] modifiedArgs = new String[args.length + 1];
			System.arraycopy(args, 0, modifiedArgs, 0, args.length);
			modifiedArgs[args.length] = DEFAULT_SPRING_CONFIG_PARAM;
			return modifiedArgs;
		}
		return args;
	}

}

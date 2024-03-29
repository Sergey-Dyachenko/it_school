package com.itschool.inquirer;

public class Constants {
	
	public static final String DATA_POOL = "DAISInquirer";
	
	public static final String MAIL_PATH = "java:/mail/InquirerMail";

	public static final String ADMIN_EMAIL = "dataart.it-school.myway@rambler.ru";
	
	public static final String ADMIN_PASS = "admin";
	
	public static final String INVOCATION_CTX_ENTITY_MANAGER = "CTX_ENTITY_MANAGER";

	public static final String ROOT_PATH = "https://localhost:8443/inquirer";

	public static final int MAX_SESSION_AGE = 6;
	
	public static final int MIN_PASS_LENGTH = 5;

	public static final String sid = "Authorization";
	
	public enum QuestionType { PARENT, SINGLEANSWER, MULTIANSWER, CUSTOMANSWER }

}

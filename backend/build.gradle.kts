plugins {
	java
	id("org.springframework.boot") version "3.3.3"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "no.hvl.dat250"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("io.jsonwebtoken:jjwt:0.12.6")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.postgresql:postgresql:42.6.0")
	implementation(platform("org.hibernate.orm:hibernate-platform:6.6.0.Final"))
	implementation("org.hibernate.orm:hibernate-core")
	implementation("jakarta.transaction:jakarta.transaction-api")
	implementation("com.h2database:h2:2.2.220")
	testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")
	testImplementation("org.mockito:mockito-core")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("com.h2database:h2")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("com.rabbitmq:amqp-client:5.16.0")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

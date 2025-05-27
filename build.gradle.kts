plugins {
	java
	id("org.springframework.boot") version "3.5.0" // pode atualizar de 3.4.4 se quiser
	id("io.spring.dependency-management") version "1.1.7"
	id("org.openjfx.javafxplugin") version "0.1.0"
	// opcional, mas útil para apontar o mainClass de forma explícita
	application
}

group = "com.gymtutor"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
}

repositories {
	mavenCentral()
}

dependencies {
	/* ───────── Spring ───────── */
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

	/* Segurança + Thymeleaf */
	implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")

	/* Jakarta EE (apenas se realmente usar APIs de lá) */
	implementation("jakarta.platform:jakarta.jakartaee-api:10.0.0")

	/* JavaFX – não precisa duplicar: o plugin já providencia em tempo de execução,
       mas declarar aqui garante autocompletar/compilação se você usar IDE sem sync do plugin.  */
	implementation("org.openjfx:javafx-controls:23.0.2")
	implementation("org.openjfx:javafx-fxml:23.0.2")

	/* Banco */
	runtimeOnly("com.mysql:mysql-connector-j")

	/* Testes */
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

/* Configuração JavaFX */
javafx {
	version = "23.0.2"
	modules = listOf("javafx.controls", "javafx.fxml")
}

application {
	// a que contém o metodo main que chama Application.launch(...)
	mainClass.set("com.gymtutor.gymtutor.Main")
}

// Permite usar bootJar e jar separadamente (útil se precisar usar `java -jar`)
tasks.getByName<Jar>("jar") {
	enabled = true
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
	enabled = true
}

tasks.getByName<Jar>("jar") { enabled = true }

// Testes com JUnit Platform
tasks.withType<Test> {
	useJUnitPlatform()
}
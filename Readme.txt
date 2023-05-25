1. TechStack:
	Language: Java 11
	Framework: Java Spring Boot 2.7.3
	Database: PostgreSQL (can change to My SQL)
	Library: 
		Lombok: Reduce boiler-plate code
		Mapstruct: Auto map DTO and entity
		Connection pool: Hiraki
		Cache: Cafein
		API doc: Spring doc
		MDC (Mapped Diagnostic Context) log
	Build: Maven 3.5 or higher
	IDE: InteliJ or Eclipse
2. Build project: 
	mvn clean install	
	install and create PostgreSQL DB
	create new database
3. Develop:
	Step 1: Import project to IDE as Maven project
	Step 2: Config database in application.yml file
	Step 3: Clean project
	Step 4: Compile/build project 
	Step 5: run payload_data.sql script to create default user

	Note: default user/pass: 
		admin/admin
		user/user
	
4. Coding rule: 
	Source code structure: 1.SourceCodeStructure.png
	Process flow: 2.SourceCodeFlow.png
5. App info:
	spec: http://localhost:8080/api/swagger-ui/index.html
	health: http://localhost:8080/api/actuator/health
6. Sample request:
	Authen:
	curl --location --request POST 'http://localhost:8080/api/authenticate' \
			--header 'Content-Type: application/json' \
			--data-raw '{
				"username": "admin",
				"password": "admin",
				"rememberMe": false
			}'
		
	
	Call ping api with token:
	
	curl --location --request GET 'http://localhost:8080/api/ping' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTY2MzY0NTg1MX0.ISEZKMAVzDmG0NRo_Kz2WlrW4g7Rq_BoMVCt7n2hZTwiL9AGxnz1_Iw4hqUGkKqElyVHriK1YgV-P9V0Tctt9w'

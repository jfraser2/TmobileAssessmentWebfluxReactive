#Project TmobileAssessmentWebfluxReactive
Springboot WebFlux Application Server(netty) and Reactive H2 Database

#Reference Materials for Project
My Existing Code from other Projects and Assessments<br/>
Google, lol

#H2(Do this first) 
H2 is an in memory Database. It will be wiped out on every new Run.<br/>
The only work would be to change H2 config values in R2dbcWithPoolConfig.java<br/>
The database testdb is automatically created from file schema.sql in the src/main/resources folder. <br/>
Ctrl-C is used to stop a run of the Application.<br/>

#Maven Compile in Eclipse(Do this second)
Load the Project into Eclipse(File Import from Git)<br/>
The Git url is: https://github.com/jfraser2/TmobileAssessmentWebfluxReactive.git<br/>
When the load is done, right click on Project TmobileAssessmentWebfluxReactive<br>
Hover on Run As<br/>
choose Maven build, not Maven build...<br>
The Maven build Run Configuration needs to have the goals set to: clean package<br/>


#Directions for TmobileAssessmentWebfluxReactive Boot and Testing

The Design constraints are the App will build in Docker and can run in Docker<br/>
or can run without Docker. These constraints have been meet.<br/>
 
#Example Java Location
C:\Program Files\Java\jdk-21\bin<br/>

#Example Command Line Project Boot(Do this third)
open your fav Windows Shell Instance(Command Prompt Instance) as the Administrator.<br/>
cd c:\work\java\eclipse-workspace2\TmobileAssessmentWebfluxReactive<br/>
"C:\Program Files\Java\jdk-21\bin\java" -Dfile.encoding=UTF-8 -Dspring.profiles.active=dev -jar target/TmobileAssessmentWebfluxReactive-0.0.1-SNAPSHOT.jar

#Docker Information for Project Boot
I put the files docker-compose.yml and Dockerfile into the project<br/>
It is now working, and tested. <br/>
Enter "docker compose up" from the command line, in the windows project folder.<br/>
The "docker compose up" only compiles and deploys code. No worries about multiple runs.<br/>
Remember H2 is in memory and destroyed on every run.<br/>
The files now perform a compile inside Docker with Java version 21<br/>
You cannot use the command until DockerDesktop is running

#Swagger Testing(Version 3) after Boot
in your fav Browser, I use google chrome. The url is: <br/>
http://localhost:8080/swagger-ui.html

#Swagger check(Version 3) 
in your fav Browser, I use google chrome. The url is: <br/>
http://localhost:8080/v3/api-docs<br/>



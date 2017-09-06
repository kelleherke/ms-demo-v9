#Introduction

Introducing Kafka

This demo retrieves pupil data. For this a pupil service is called. The retrieved pupil data including an eircode. 

To get the address associated with the eircode, the pupil service calls a second service, the address service supplying said eircode.

The pupil service needs to locate this address service. For this , I use a discovery service - netflix eureka.    

Thus we have

    -  A pupil-service instance
    -  An address-service instance
    -  A Spring Cloud Eureka Service Instance
    -  A Spring Configuration Server Instance
    -  A Zuul API Gateway. All of our microservices can be routed through the gateway and have pre, response and post policies enforced on the calls.

##eurekasvr
To enable service discovery I use springboot and Netflix Eureka.

To start the service, in the eurekasvr directory, run the following command.
   
	mvn spring-boot:run
   
All registered services can be seen via [http://localhost:8761/](http://localhost:8761)

Please start this service first, as the other two services will register themselves with Eureka on startup.



##configasvr
To enable distributed configuration I use Spring Cloud Configuration Server.

To start the service, in the configsvr directory, run the following command.
   
	mvn spring-boot:run

The config server is setup to read zuulservice config data from github

    https://github.com/kelleherke/citadelconfigsvr/blob/master/zuulservice/zuulservice.yml 
   
All configuration can be seen via [http://localhost:8762/zuulservice/default](http://localhost:8762/zuulservice/default)


##zipkinasvr
To enable distributed tracing I use Open Zipkin.

To start the service, in the zipkinsvr directory, run the following command.
   
	mvn spring-boot:run
   
All zipkin traces can be seen via [http://localhost:9411/](http://localhost:9411)


##zuulsvr
To use an API gateway, I use Netflix Zuul.

To start the service, in the zuulscr directory, run the following command

    mvn spring-boot:run 
    
All zuul routes can be seen via [http://localhost:5555/routes](http://localhost:5555/routes)  

You should see a json response as follows ...

    {
     "/api/pupil/**":"pupilservice",
     "/api/address/**":"addressservice"
    }    

On startup, this service registered itself with the eureka. which can be seen via [http://localhost:8761/](http://localhost:8761/)


##address-service
This springboot REST microservice enables the retrieval of an address by eircode.

To start the service, in the address-service directory, run the following command.
   
	mvn spring-boot:run

To test the service   

	http://localhost:8085/v1/addresses/D01AB12
	http://localhost:5555/api/address/v1/addresses/D01AB12
   
You should see a json response as follows ...

	{
    "eircode": "D01AB12",
    "address1": "01 Any House",
    "address2": "Any Street",
    "address3": " Any Town",
    "address4": "Any Where"
`   }


On startup, this service registered itself with the eureka. which can be seen via via [http://localhost:8761/](http://localhost:8761)

        

##pupil-service
This springboot REST microservice enables the retrieval of a pupil by pupilId.

To start the service
   
	mvn spring-boot:run

To test the service   

	http://localhost:8086/v1/pupils/1
	http://localhost:5555/api/pupil/v1/pupils/1
   
You should see a json response as follows ...

	{
    "pupilId": "1",
    "eircode": "D01AB12",
    "forename": "Joe",
    "surname": "Bloggs",
    "address1": "01 Any House",
    "address2": "Any Street",
    "address3": " Any Town",
    "address4": "Any Where"
`    }


On startup, this service registered itself with the eureka. which can be seen via [http://localhost:8761/](http://localhost:8761/)

Note : The pupil service calls the address service, locating it via the Eureaka service.


#Software Needed
##Java8
##Maven
I used version 3.3.3

#Recommended IDE
I used eclipse STS as my IDE, which is great for stopping/starting/debugging the services.

#Docker
In each of the 4 projects run the following command, which will execute the [Spotify docker plugin] defined in the pom.xml file
    
    mvn  clean package docker:build
    
To see all build docker images

    docker images --all  
    
Now we are going to use docker-compose to start the actual image. To start the docker image , change to the docker-compose/common directory. Issue the following docker-compose command.

    docker-compose -f docker-compose.yml up
    docker-compose -f docker-compose.yml up zipkin
    docker-compose -f docker-compose.yml up logspout
    docker-compose -f docker-compose.yml up eurekaserver
    docker-compose -f docker-compose.yml up configserver
    docker-compose -f docker-compose.yml up kafkaserver
    docker-compose -f docker-compose.yml up addressservice
    docker-compose -f docker-compose.yml up pupilservice
    docker-compose -f docker-compose.yml up zuulserver
    
This command will start the  

    - eureka server
    - zipkin server
    - config server
    - zuul server
    - pupil service
    - address service


###Sanity test    
To sanity test that all is ok run the following commands

####Eureka
    http://10.0.17.43:8761/
    http://10.0.17.43:8761/eureka/apps
    http://10.0.17.43:8761/eureka/apps/addressservice
    http://10.0.17.43:8761/eureka/apps/pupilservice
    http://10.0.17.43:8761/eureka/apps/zuulservice
    http://10.0.17.43:8761/eureka/apps/configserver
   
####ConfigServer    
    http:/10.0.17.43:8762/zuulservice/default

####Zipkin        
    http://10.0.17.43:9411/
    
####Zuul    
    http://10.0.17.43:5555/routes
    
####AddressService   
    http://10.0.17.43:8085/info
    http://10.0.17.43:8085/health
    http://10.0.17.43:8085/v1/addresses/D01AB12
    http://10.0.17.43:5555/api/address/v1/addresses/D01AB12

####PupilService
    http://10.0.17.43:8086/info
    http://10.0.17.43:8086/health
    http://10.0.17.43:5555/api/pupil/v1/pupils/1

####PaperTrail    
    https://papertrailapp.com/events
    
note : 10.0.17.43 is my docker ip address.

    
You should see a json response as follows ...

	{
    "pupilId": "1",
    "eircode": "D01AB12",
    "forename": "Joe",
    "surname": "Bloggs",
    "address1": "01 Any House",
    "address2": "Any Street",
    "address3": " Any Town",
    "address4": "Any Where"
   }    
        
    
To see if the service are running in docker type the following

    docker ps
    
To stop and remove the containers, networks, images issue the following command

    docker-compose -f docker-compose.yml down
    
To see docker images

    docker images
    
To stop a docker image

    docker stop common_zuulserver_1    
    
To remove a docker image

    docker rmi c999209b0836
    
To save a docker image

    docker save 199487e8d638 > eureka.tar
    
#Git

    start up git shell

    git init

    git add --all

    git commit -m "first commit"    
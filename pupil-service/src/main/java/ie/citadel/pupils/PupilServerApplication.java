package ie.citadel.pupils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import ie.citadel.pupil.events.models.AddressChangeModel;

import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;


@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@EnableBinding(Sink.class)
public class PupilServerApplication {
	
  private static final Logger logger = LoggerFactory.getLogger(PupilServerApplication.class);	
	
  @Bean
  public Sampler defaultSampler() {
    return new AlwaysSampler();
  }	

  @LoadBalanced
  @Bean
  public RestTemplate getRestTemplate(){
      return new RestTemplate();
  }
  
  @StreamListener(Sink.INPUT)
  public void loggerSink(AddressChangeModel addressChange) {
    logger.debug("Received an event for eircode {}", addressChange.getEircode());
  } 

  public static void main(String[] args) {
        SpringApplication.run(PupilServerApplication.class, args);
  }
}

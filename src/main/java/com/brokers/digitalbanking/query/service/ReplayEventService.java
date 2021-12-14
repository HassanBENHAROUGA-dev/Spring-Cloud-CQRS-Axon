package com.brokers.digitalbanking.query.service;

import lombok.AllArgsConstructor;
import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ReplayEventService {
    private EventProcessingConfiguration eventProcessingConfiguration;

    public void replay(){
        String name = "com.brokers.digitalbanking.query.service";
        eventProcessingConfiguration.eventProcessor(name, TrackingEventProcessor.class)
                .ifPresent(processor->{
                    processor.shutDown();
                    processor.resetTokens();
                    processor.start();
                });
    }
}

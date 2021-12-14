package com.brokers.digitalbanking.coreapi;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class BaseCommand <T>{
    @TargetAggregateIdentifier//représente l'identifiant de l'aggregat
    @Getter private T id;

    public BaseCommand(T id) {
        this.id = id;
    }
}

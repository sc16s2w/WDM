package com.example.wdm.order;

import io.dapr.actors.ActorMethod;
import io.dapr.actors.ActorType;
import reactor.core.publisher.Mono;

@ActorType(name = "OrderActor")
public interface OrderActor {
    void registerReminder();

    @ActorMethod(returns = String.class)
    Mono<String> create_order(String user_id);// user_id


    void remove_order();// order_id

    @ActorMethod(returns = String.class)
    Mono<String> find_order();// order_id

    @ActorMethod(returns = String.class)
    Mono<String> add_item(); // order_id & item_id

    @ActorMethod(returns = String.class)
    Mono<String> remove_Item(); // order_id & item_id

    @ActorMethod(returns = String.class)
    Mono<String> checkout(); // order_id


}
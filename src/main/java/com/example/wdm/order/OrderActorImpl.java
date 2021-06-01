package com.example.wdm.order;
import io.dapr.actors.ActorId;
import io.dapr.actors.runtime.AbstractActor;
import io.dapr.actors.runtime.ActorRuntimeContext;
import io.dapr.actors.runtime.Remindable;
import io.dapr.utils.TypeRef;
import io.dapr.v1.DaprProtos;
import reactor.core.publisher.Mono;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class OrderActorImpl extends AbstractActor implements OrderActor, Remindable<Integer> {

    /**
     * Format to output date and time.
     */
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * This is the constructor of an actor implementation, while also registering a timer.
     * @param runtimeContext The runtime context object which contains objects such as the state provider.
     * @param id             The id of this actor.
     */
    public OrderActorImpl (ActorRuntimeContext runtimeContext, ActorId id) {
        super(runtimeContext, id);
    }

    /**
     * Registers a reminder.
     */
    @Override
    public void registerReminder() {
        super.registerReminder(
                "myremind",
                (int) (Integer.MAX_VALUE * Math.random()),
                Duration.ofSeconds(5),
                Duration.ofSeconds(2)).block();
    }

    @Override
    public Mono<String> create_order(String user_id) {
        System.out.println("service:create order:"+this.getId());
        ArrayList<String> items = new ArrayList<String>();
        super.getActorStateManager().add("paid", false).block();
        super.getActorStateManager().add("items", items).block();
        super.getActorStateManager().add("user_id", user_id).block();
        super.getActorStateManager().add("total_cost", 0).block();
        return Mono.just(this.getId().toString());
    }

    @Override
    public void remove_order() {
        System.out.println("service delete order: ");
//        DaprProtos.DeleteBulkStateRequest.Builder.removeStates(this.getId());
        super.getActorStateManager().remove("paid").block();
        super.getActorStateManager().remove("items").block();
        super.getActorStateManager().remove("user_id").block();
        super.getActorStateManager().remove("total_cost").block();
    }

    @Override
    public Mono<String> find_order() {
        System.out.println("service: find order");
        Boolean paid = super.getActorStateManager().get("paid", Boolean.class).block();
        ArrayList<String> items = super.getActorStateManager().get("items", ArrayList.class).block();
        String user_id = super.getActorStateManager().get("user_id", String.class).block();
        int total_cost = super.getActorStateManager().get("total_cost", int.class).block();
        String result = paid.toString()+"#"+items.toString()+'#'+user_id+"#"+String.valueOf(total_cost);
        return Mono.just(result);
    }

    @Override
    public Mono<String> add_item() {
        return null;
    }

    @Override
    public Mono<String> remove_Item() {
        return null;
    }

    @Override
    public Mono<String> checkout() {
        return null;
    }

    /**
     * Method used to determine reminder's state type.
     * @return Class for reminder's state.
     */
    @Override
    public TypeRef<Integer> getStateType() {
        return TypeRef.INT;
    }

    /**
     * Method used be invoked for a reminder.
     * @param reminderName The name of reminder provided during registration.
     * @param state        The user state provided during registration.
     * @param dueTime      The invocation due time provided during registration.
     * @param period       The invocation period provided during registration.
     * @return Mono result.
     */
    @Override
    public Mono<Void> receiveReminder(String reminderName, Integer state, Duration dueTime, Duration period) {
        return Mono.fromRunnable(() -> {
            Calendar utcNow = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            String utcNowAsString = DATE_FORMAT.format(utcNow.getTime());

            String message = String.format("Server reminded actor %s of: %s for %d @ %s",
                    this.getId(), reminderName, state, utcNowAsString);
        });
    }

}

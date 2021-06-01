package com.example.wdm.order;
import java.util.concurrent.Callable;

public class OrderCallActor implements Callable {
    private String actorId;
    private OrderActor actor;
    private Integer type;
    private Integer amount;
    private String user_id;
    private String order_id;
    public OrderCallActor(String actorId, OrderActor actor, Integer type){
        this.actorId = actorId;
        this.actor  = actor;
        this.type = type;
    }
//    public OrderCallActor(String actorId, OrderActor actor, Integer type, Integer amount){
//        this.actorId = actorId;
//        this.actor = actor;
//        this.type = type;
//        this.amount = amount;
//    }
    public OrderCallActor(String actorId, OrderActor actor, Integer type, String user_id){
        this.user_id = user_id;
        this.actorId = actorId;
        this.actor = actor;
        this.type = type;
    }
//    public OrderCallActor(String actorId, OrderActor actor, Integer type, String order_id){
//        this.order_id = order_id;
//        this.actorId = actorId;
//        this.actor = actor;
//        this.type = type;
//    }
    @Override
    public Object call() throws Exception {
        actor.registerReminder();
        String result = "";

        System.out.println("OrdercallActor");
        switch (type) {
            case 1: {
                result = actor.create_order(user_id).block();
                break;
            }
            case 2:{
                actor.remove_order();
                result = "0";
                break;
            }
            case 3:{
                result = actor.find_order().block().toString();
                break;
            }
            case 4:{
                System.out.println("3");
                result = actor.add_item().block().toString();
                System.out.println("4");
                break;
            }
        }
        return result;
    }
}
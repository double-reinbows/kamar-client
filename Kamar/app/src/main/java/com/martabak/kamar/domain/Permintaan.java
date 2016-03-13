package com.martabak.kamar.domain;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;


/**
 * A guest's request for something.
 */
public class Permintaan<T extends Permintaan.Content> extends Model {

    /**
     * The owner of the request. One of:
     * - RESTAURANT
     * - FRONT DESK
     */
    public final String owner;

    /**
     * The type of the request. One of:
     * - HOUSEKEEPING
     * - MAINTENANCE
     * - BELLBOY
     * - CHECKOUT
     * - TRANSPORT
     * - CONSUMABLE
     */
    public final String type;

    /**
     * The guest's room number.
     */
    @SerializedName("room_number") public final String roomNumber;

    /**
     * The request sub-type.
     */
    public final T content;

    /**
     * State of this request. One of:
     * - NEW
     * - IN PROGRESS
     * - IN DELIVERY
     * - COMPLETE
     * - DELETED
     * - CANCELLED
     */
    public final String state;

    /**
     * The creation time of the request.
     */
    public final Date created;

    /**
     * The last updated time of the request.
     */
    public final Date updated;

    public Permintaan() {}

    public Permintaan(String owner, String roomNumber, T content) {
        this.owner = owner;
        this.type = content.getType();
        this.roomNumber = roomNumber;
        this.content = content;
        this.state = "NEW";
        this.created = null;// TODO Date.nowOrSomething?();
    }

    public abstract class Content {
        public final String message;

        public Content(String message) {
            this.message = message;
        }

        public abstract String getType();
    }

    public class Housekeeping extends Content {
        public Housekeeping(String message) {
            super(message);
        }

        public String getType() {
            return "HOUSEKEEPING";
        }
    }

    public class Maintenance extends Content {
        public Maintenance(String message) {
            super(message);
        }

        public String getType() {
            return "HOUSEKEEPING";
        }
    }

    public class Bellboy extends Content {
        public Bellboy(String message) {
            super(message);
        }

        public String getType() {
            return "HOUSEKEEPING";
        }
    }

    public class Checkout extends Content {
        public Checkout(String message) {
            super(message);
        }

        public String getType() {
            return "HOUSEKEEPING";
        }
    }

    public class Transport extends Content {

        public final Integer passengers;

        @SerializedName("departure_time") public final Date departureTime;

        public final String destination;

        public Transport(String message, int passengers, Date departureTime, String destination) {
            super(message);
            this.passengers = passengers;
            this.departureTime = departureTime;
            this.destination = destination;
        }

        public String getType() {
            return "HOUSEKEEPING";
        }
    }

    public class Consumable extends Content {

        public final List<OrderItem> items;

        @SerializedName("total_price") public final Integer totalPrice;

        public Consumable(String message, List<OrderItem> items, int totalPrice) {
            super(message);
            this.items = items;
            this.totalPrice = totalPrice;
        }

        public String getType() {
            return "CONSUMABLE";
        }

        public class OrderItem {

            public final Integer quantity;

            public final String name;

            public OrderItem(int quantity, String name) {
                this.quantity = quantity;
                this.name = name;
            }
        }
    }

}
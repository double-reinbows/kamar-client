package com.martabak.kamar.domain.converters;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.martabak.kamar.domain.permintaan.Bellboy;
import com.martabak.kamar.domain.permintaan.Checkout;
import com.martabak.kamar.domain.permintaan.RestaurantOrder;
import com.martabak.kamar.domain.permintaan.Content;
import com.martabak.kamar.domain.permintaan.Housekeeping;
import com.martabak.kamar.domain.permintaan.Maintenance;
import com.martabak.kamar.domain.permintaan.OrderItem;
import com.martabak.kamar.domain.permintaan.Permintaan;
import com.martabak.kamar.domain.permintaan.Transport;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Serializes and deserializes {@link Permintaan}s.
 */
public class PermintaanConverter implements JsonSerializer<Permintaan>, JsonDeserializer<Permintaan> {

    private final SimpleDateFormat dateFormat;

    public PermintaanConverter(String datePattern) {
        this.dateFormat = new SimpleDateFormat(datePattern);
        this.dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public JsonElement serialize(Permintaan src, Type srcType, JsonSerializationContext context) {
        JsonObject j = new JsonObject();

        j.addProperty("_id", src._id);
        j.addProperty("_rev", src._rev);
        j.addProperty("owner", src.owner);
        j.addProperty("type", src.type);
        j.addProperty("room_number", src.roomNumber);
        j.addProperty("guest_id", src.guestId);
        j.addProperty("state", src.state);
        j.addProperty("created", dateFormat.format(src.created));
        if (src.updated != null) {
            j.addProperty("updated", dateFormat.format(src.updated));
        }

        JsonObject content = new JsonObject();
        content.addProperty("message", src.content.message);
        switch (src.type) {
            case Permintaan.TYPE_TRANSPORT:
                Transport t = (Transport)src.content;
                content.addProperty("passengers", t.passengers);
                content.addProperty("departing_in", t.departingIn);
                content.addProperty("destination", t.destination);
                break;
            case Permintaan.TYPE_RESTAURANT:
                RestaurantOrder restaurantOrder = (RestaurantOrder)src.content;
                content.addProperty("total_price", restaurantOrder.totalPrice);
                JsonArray jsonItems = new JsonArray();
                for (OrderItem i : restaurantOrder.items) {
                    JsonObject orderItem = new JsonObject();
                    orderItem.addProperty("quantity", i.quantity);
                    orderItem.addProperty("name", i.name);
                    orderItem.addProperty("price", i.price);
                    jsonItems.add(orderItem);
                }
                content.add("items", jsonItems);
                content.addProperty("total_price", restaurantOrder.totalPrice);
                break;
            case Permintaan.TYPE_BELLBOY:
            case Permintaan.TYPE_CHECKOUT:
            case Permintaan.TYPE_HOUSEKEEPING:
            case Permintaan.TYPE_MAINTENANCE:
            default:
                break;
        }
        j.add("content", content);

        return j;
    }

    @Override
    public Permintaan deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject j = (JsonObject)json;

        String _id = j.getAsJsonPrimitive("_id").getAsString();
        String _rev = j.getAsJsonPrimitive("_rev").getAsString();
        String owner = j.getAsJsonPrimitive("owner").getAsString();
        String ptype = j.getAsJsonPrimitive("type").getAsString();
        String roomNumber = j.getAsJsonPrimitive("room_number").getAsString();
        String guestId = j.getAsJsonPrimitive("guest_id").getAsString();
        String state = j.getAsJsonPrimitive("state").getAsString();
        Date created;
        try {
            created = dateFormat.parse(j.getAsJsonPrimitive("created").getAsString());
        } catch (ParseException e) {
            throw new JsonParseException(e);
        }
        Date updated = null; // Empty updated time is acceptable.
        try {
            updated = dateFormat.parse(j.getAsJsonPrimitive("updated").getAsString());
        } catch (ParseException e) {
        } catch (ClassCastException e) {
        } catch (NullPointerException e) {
        }
        JsonObject c = j.getAsJsonObject("content");
        String message = c.getAsJsonPrimitive("message").getAsString();
        Content content;
        switch (ptype) {
            case Permintaan.TYPE_BELLBOY:
                content = new Bellboy(message);
                break;
            case Permintaan.TYPE_CHECKOUT:
                content = new Checkout(message);
                break;
            case Permintaan.TYPE_MAINTENANCE:
                content = new Maintenance(message);
                break;
            case Permintaan.TYPE_HOUSEKEEPING:
                content = new Housekeeping(message);
                break;
            case Permintaan.TYPE_TRANSPORT:
                Integer passengers = c.getAsJsonPrimitive("passengers").getAsInt();
                String departingIn = c.getAsJsonPrimitive("departing_in").getAsString();
                String destination = c.getAsJsonPrimitive("destination").getAsString();
                content = new Transport(message, passengers, departingIn, destination);
                break;
            case Permintaan.TYPE_RESTAURANT:
                List<OrderItem> restuarantItems = new ArrayList<>();
                for (int i = 0; i < c.getAsJsonArray("items").size(); i++) {
                    JsonObject item = (JsonObject)c.getAsJsonArray("items").get(i);
                    Integer quantity = item.getAsJsonPrimitive("quantity").getAsInt();
                    Integer price = item.getAsJsonPrimitive("price").getAsInt();
                    String name = item.getAsJsonPrimitive("name").getAsString();
                    restuarantItems.add(new OrderItem(quantity, name, price));
                }
                Integer totalRestaurantPrice = c.getAsJsonPrimitive("total_price").getAsInt();
                content = new RestaurantOrder(message, restuarantItems, totalRestaurantPrice);
                break;
            default:
                throw new JsonParseException("Unknown Permintaan content type.");
        }

        return new Permintaan(_id, _rev, owner, ptype, roomNumber, guestId, state, created, updated, content);
    }
}

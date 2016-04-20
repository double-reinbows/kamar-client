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
import com.martabak.kamar.domain.permintaan.Consumable;
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

/**
 * Serializes and deserializes {@link Permintaan}s.
 */
public class PermintaanConverter implements JsonSerializer<Permintaan>, JsonDeserializer<Permintaan> {

    private final SimpleDateFormat dateFormat;

    public PermintaanConverter(String datePattern) {
        this.dateFormat = new SimpleDateFormat(datePattern);
    }

    @Override
    public JsonElement serialize(Permintaan src, Type srcType, JsonSerializationContext context) {
        JsonObject j = new JsonObject();

        j.addProperty("owner", src.owner);
        j.addProperty("type", src.type);
        j.addProperty("room_number", src.roomNumber);
        j.addProperty("guest_id", src.guestId);
        j.addProperty("state", src.state);
        j.addProperty("created", src.created.toString());
        if (src.updated != null) {
            j.addProperty("updated", src.updated.toString());
        }

        JsonObject content = new JsonObject();
        content.addProperty("message", src.content.message);
        switch (src.type) {
            case "CONSUMABLE":
                Consumable c = (Consumable)src.content;
                content.addProperty("total_price", c.totalPrice);
                JsonArray items = new JsonArray();
                for (OrderItem i : c.items) {
                    JsonObject orderItem = new JsonObject();
                    orderItem.addProperty("quantity", i.quantity);
                    orderItem.addProperty("name", i.name);
                    items.add(orderItem);
                }
                content.add("items", items);
                content.addProperty("total_price", c.totalPrice);
                break;
            case "TRANSPORT":
                Transport t = (Transport)src.content;
                content.addProperty("passengers", t.passengers);
                content.addProperty("departure_time", t.departureTime.toString());
                content.addProperty("destination", t.destination);
                break;
            case "BELLBOY":
            case "CHECKOUT":
            case "HOUSEKEEPING":
            case "MAINTENANCE":
            default:
                break;
        }
        j.add("content", content);

        return j;
    }

    @Override
    public Permintaan deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject j = (JsonObject)json;

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
        }
        JsonObject c = j.getAsJsonObject("content");
        String message = c.getAsJsonPrimitive("message").getAsString();
        Content content;
        switch (ptype) {
            case "BELLBOY":
                content = new Bellboy(message);
                break;
            case "CONSUMABLE":
                List<OrderItem> items = new ArrayList<>();
                for (int i = 0; i < c.getAsJsonArray("items").size(); i++) {
                    JsonObject item = (JsonObject)c.getAsJsonArray("items").get(i);
                    Integer quantity = item.getAsJsonPrimitive("quantity").getAsInt();
                    String name = item.getAsJsonPrimitive("name").getAsString();
                    items.add(new OrderItem(quantity, name));
                }
                Integer totalPrice = c.getAsJsonPrimitive("total_price").getAsInt();
                content = new Consumable(message, items, totalPrice);
                break;
            case "CHECKOUT":
                content = new Checkout(message);
                break;
            case "MAINTENANCE":
                content = new Maintenance(message);
                break;
            case "HOUSEKEEPING":
                content = new Housekeeping(message);
                break;
            case "TRANSPORT":
                Integer passengers = c.getAsJsonPrimitive("passengers").getAsInt();
                Date departureTime;
                try {
                    departureTime = dateFormat.parse(c.getAsJsonPrimitive("departure_time").getAsString());
                } catch (ParseException e) {
                    throw new JsonParseException(e);
                }
                String destination = c.getAsJsonPrimitive("destination").getAsString();
                content = new Transport(message, passengers, departureTime, destination);
                break;
            default:
                throw new JsonParseException("Unknown Permintaan content type.");
        }

        return new Permintaan(owner, ptype, roomNumber, guestId, state, created, updated, content);
    }
}
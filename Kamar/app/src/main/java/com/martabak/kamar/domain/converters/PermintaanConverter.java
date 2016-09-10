package com.martabak.kamar.domain.converters;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.martabak.kamar.domain.options.EngineeringOption;
import com.martabak.kamar.domain.options.HousekeepingOption;
import com.martabak.kamar.domain.options.LaundryOption;
import com.martabak.kamar.domain.options.MassageOption;
import com.martabak.kamar.domain.permintaan.Bellboy;
import com.martabak.kamar.domain.permintaan.Checkout;
import com.martabak.kamar.domain.permintaan.LaundryOrder;
import com.martabak.kamar.domain.permintaan.LaundryOrderItem;
import com.martabak.kamar.domain.permintaan.Massage;
import com.martabak.kamar.domain.permintaan.RestaurantOrder;
import com.martabak.kamar.domain.permintaan.Content;
import com.martabak.kamar.domain.permintaan.Housekeeping;
import com.martabak.kamar.domain.permintaan.Engineering;
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
            case Permintaan.TYPE_LAUNDRY:
                LaundryOrder laundryOrder = (LaundryOrder)src.content;
                content.addProperty("total_price", laundryOrder.totalPrice);
                JsonArray laundryItems = new JsonArray();
                for (LaundryOrderItem i : laundryOrder.items) {
                    JsonObject orderItem = new JsonObject();
                    orderItem.addProperty("quantity", i.quantity);
                    orderItem.addProperty("price", i.price);
                    orderItem.addProperty("_id", i.option._id);
                    orderItem.addProperty("_rev", i.option._rev);
                    orderItem.addProperty("name_en", i.option.nameEn);
                    orderItem.addProperty("name_in", i.option.nameIn);
                    orderItem.addProperty("name_zh", i.option.nameZh);
                    orderItem.addProperty("name_ru", i.option.nameRu);
                    orderItem.addProperty("laundry_price", i.option.laundryPrice);
                    orderItem.addProperty("pressing_price", i.option.pressingPrice);
                    laundryItems.add(orderItem);
                }
                content.add("items", laundryItems);
                content.addProperty("total_price", laundryOrder.totalPrice);
                break;
            case Permintaan.TYPE_BELLBOY:
                break;
            case Permintaan.TYPE_CHECKOUT:
                break;
            case Permintaan.TYPE_HOUSEKEEPING:
                Housekeeping housekeeping = (Housekeeping)src.content;
                content.addProperty("_id", housekeeping.option._id);
                content.addProperty("_rev", housekeeping.option._rev);
                content.addProperty("name_en", housekeeping.option.nameEn);
                content.addProperty("name_in", housekeeping.option.nameIn);
                content.addProperty("name_zh", housekeeping.option.nameZh);
                content.addProperty("name_ru", housekeeping.option.nameRu);
                content.addProperty("section_en", housekeeping.option.sectionEn);
                content.addProperty("section_in", housekeeping.option.sectionIn);
                content.addProperty("section_zh", housekeeping.option.sectionZh);
                content.addProperty("section_ru", housekeeping.option.sectionRu);
                content.addProperty("max", housekeeping.option.max);
                content.addProperty("quantity", housekeeping.quantity);
                break;
            case Permintaan.TYPE_ENGINEERING:
                Engineering engineering = (Engineering)src.content;
                content.addProperty("_id", engineering.option._id);
                content.addProperty("_rev", engineering.option._rev);
                content.addProperty("name_en", engineering.option.nameEn);
                content.addProperty("name_in", engineering.option.nameIn);
                content.addProperty("name_zh", engineering.option.nameZh);
                content.addProperty("name_ru", engineering.option.nameRu);
                break;
            case Permintaan.TYPE_MASSAGE:
                Massage massage = (Massage)src.content;
                content.addProperty("_id", massage.option._id);
                content.addProperty("_rev", massage.option._rev);
                content.addProperty("name_en", massage.option.nameEn);
                content.addProperty("name_in", massage.option.nameIn);
                content.addProperty("name_zh", massage.option.nameZh);
                content.addProperty("name_ru", massage.option.nameRu);
                content.addProperty("description_en", massage.option.descriptionEn);
                content.addProperty("description_in", massage.option.descriptionIn);
                content.addProperty("description_zh", massage.option.descriptionZh);
                content.addProperty("description_ru", massage.option.descriptionRu);
                content.addProperty("price", massage.option.price);
                content.addProperty("length", massage.option.length);
                break;
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
            case Permintaan.TYPE_ENGINEERING:
                String idEngineering = c.getAsJsonPrimitive("_id").getAsString();
                String revEngineering = c.getAsJsonPrimitive("_rev").getAsString();
                String nameEnEngineering = c.getAsJsonPrimitive("name_en").getAsString();
                String nameInEngineering = c.getAsJsonPrimitive("name_in").getAsString();
                String nameZhEngineering = c.getAsJsonPrimitive("name_zh").getAsString();
                String nameRuEngineering = c.getAsJsonPrimitive("name_ru").getAsString();
                EngineeringOption optionEngineering = new EngineeringOption(idEngineering, revEngineering,
                        nameEnEngineering, nameInEngineering, nameZhEngineering, nameRuEngineering, null, null);
                content = new Engineering(message, optionEngineering);
                break;
            case Permintaan.TYPE_HOUSEKEEPING:
                String idHousekeeping = c.getAsJsonPrimitive("_id").getAsString();
                String revHousekeeping = c.getAsJsonPrimitive("_rev").getAsString();
                String nameEnHousekeeping = c.getAsJsonPrimitive("name_en").getAsString();
                String nameInHousekeeping = c.getAsJsonPrimitive("name_in").getAsString();
                String nameZhHousekeeping = c.getAsJsonPrimitive("name_zh").getAsString();
                String nameRuHousekeeping = c.getAsJsonPrimitive("name_ru").getAsString();
                String sectionEn = c.getAsJsonPrimitive("section_en").getAsString();
                String sectionIn = c.getAsJsonPrimitive("section_in").getAsString();
                String sectionZh = c.getAsJsonPrimitive("section_zh").getAsString();
                String sectionRu = c.getAsJsonPrimitive("section_ru").getAsString();
                Integer max = c.getAsJsonPrimitive("max").getAsInt();
                Integer quantityHousekeeping = c.getAsJsonPrimitive("quantity").getAsInt();
                HousekeepingOption optionHousekeeping = new HousekeepingOption(idHousekeeping, revHousekeeping,
                        nameEnHousekeeping, nameInHousekeeping, nameZhHousekeeping, nameRuHousekeeping,
                        null, sectionEn, sectionIn, sectionZh, sectionRu, null, max);
                content = new Housekeeping(message, quantityHousekeeping, optionHousekeeping);
                break;
            case Permintaan.TYPE_MASSAGE:
                String idMassage = c.getAsJsonPrimitive("_id").getAsString();
                String revMassage = c.getAsJsonPrimitive("_rev").getAsString();
                String nameEnMassage = c.getAsJsonPrimitive("name_en").getAsString();
                String nameInMassage = c.getAsJsonPrimitive("name_in").getAsString();
                String nameZhMassage = c.getAsJsonPrimitive("name_zh").getAsString();
                String nameRuMassage = c.getAsJsonPrimitive("name_ru").getAsString();
                String descriptionEn = c.getAsJsonPrimitive("description_en").getAsString();
                String descriptionIn = c.getAsJsonPrimitive("description_in").getAsString();
                String descriptionZh = c.getAsJsonPrimitive("description_zh").getAsString();
                String descriptionRu = c.getAsJsonPrimitive("description_ru").getAsString();
                Integer priceMassage = c.getAsJsonPrimitive("price").getAsInt();
                Integer length = c.getAsJsonPrimitive("length").getAsInt();
                MassageOption optionMassage = new MassageOption(idMassage, revMassage, nameEnMassage,
                        nameInMassage, nameZhMassage, nameRuMassage, descriptionEn, descriptionIn,
                        descriptionZh, descriptionRu, null, priceMassage, length, null);
                content = new Massage(message, optionMassage);
                break;
            case Permintaan.TYPE_TRANSPORT:
                Integer passengers = c.getAsJsonPrimitive("passengers").getAsInt();
                String departingIn = c.getAsJsonPrimitive("departing_in").getAsString();
                String destination = c.getAsJsonPrimitive("destination").getAsString();
                content = new Transport(message, passengers, departingIn, destination);
                break;
            case Permintaan.TYPE_RESTAURANT:
                List<OrderItem> restaurantItems = new ArrayList<>();
                for (int i = 0; i < c.getAsJsonArray("items").size(); i++) {
                    JsonObject item = (JsonObject)c.getAsJsonArray("items").get(i);
                    Integer quantityRestaurant = item.getAsJsonPrimitive("quantity").getAsInt();
                    Integer priceRestaurant = item.getAsJsonPrimitive("price").getAsInt();
                    String name = item.getAsJsonPrimitive("name").getAsString();
                    restaurantItems.add(new OrderItem(quantityRestaurant, name, priceRestaurant));
                }
                Integer totalRestaurantPrice = c.getAsJsonPrimitive("total_price").getAsInt();
                content = new RestaurantOrder(message, restaurantItems, totalRestaurantPrice);
                break;
            case Permintaan.TYPE_LAUNDRY:

                List<LaundryOrderItem> laundryItems = new ArrayList<>();
                for (int i = 0; i < c.getAsJsonArray("items").size(); i++) {
                    JsonObject item = (JsonObject)c.getAsJsonArray("items").get(i);
                    Integer quantity = item.getAsJsonPrimitive("quantity").getAsInt();
                    Integer price = item.getAsJsonPrimitive("price").getAsInt();
                    String idLaundry = item.getAsJsonPrimitive("_id").getAsString();
                    String revLaundry = item.getAsJsonPrimitive("_rev").getAsString();
                    String nameEnLaundry = item.getAsJsonPrimitive("name_en").getAsString();
                    String nameInLaundry = item.getAsJsonPrimitive("name_in").getAsString();
                    String nameZhLaundry = item.getAsJsonPrimitive("name_zh").getAsString();
                    String nameRuLaundry = item.getAsJsonPrimitive("name_ru").getAsString();
                    Integer priceLaundry = item.getAsJsonPrimitive("laundry_price").getAsInt();
                    Integer pricePressing = item.getAsJsonPrimitive("pressing_price").getAsInt();
                    LaundryOption optionLaundry = new LaundryOption(idLaundry, revLaundry, nameEnLaundry,
                            nameInLaundry, nameZhLaundry, nameRuLaundry, priceLaundry, pricePressing, null, null);
                    laundryItems.add(new LaundryOrderItem(quantity, price, optionLaundry));
                }
                Integer totalLaundryPrice = c.getAsJsonPrimitive("total_price").getAsInt();
                content = new LaundryOrder(message, laundryItems, totalLaundryPrice);
                break;
            default:
                throw new JsonParseException("Unknown Permintaan content type.");
        }

        return new Permintaan(_id, _rev, owner, ptype, roomNumber, guestId, state, created, updated, null, content);
    }
}

package io.abner.vertx.exemplo.jooq;

import io.reactiverse.pgclient.data.Json;
import io.reactiverse.pgclient.impl.data.JsonImpl;
import io.vertx.core.json.JsonObject;
import org.jooq.DataType;
import org.jooq.impl.AbstractConverter;
import org.jooq.impl.DefaultDataType;

public class JsonbConverter extends AbstractConverter<Object, JsonObject> {

    public static final DataType<JsonImpl> JSONB = new DefaultDataType<>(null, JsonImpl.class, "jsonb");

    public JsonbConverter() {
        super(Object.class, JsonObject.class);
    }

    @Override
    public JsonObject from(Object databaseObject) {
        Json json = (Json)databaseObject;
        return json != null ? (JsonObject) json.value() : new JsonObject();
    }

    @Override
    public Object to(JsonObject userObject) {
        return Json.create(userObject);
    }


}
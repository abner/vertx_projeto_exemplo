package io.abner.vertx.exemplo.jooq.generator;

import io.github.jklingsporn.vertx.jooq.generate.builder.DelegatingVertxGenerator;
import io.github.jklingsporn.vertx.jooq.generate.builder.VertxGeneratorBuilder;
import io.vertx.core.json.JsonObject;
import org.jooq.codegen.JavaWriter;
import org.jooq.meta.TypedElementDefinition;

// REF: https://github.com/etiennestuder/gradle-jooq-plugin/tree/master/example/use_custom_generator
public class CustomVertxGenerator extends DelegatingVertxGenerator {
    public CustomVertxGenerator() {
        super(VertxGeneratorBuilder.init().withRXAPI().withPostgresReactiveDriver().build());
    }

    @Override
    protected boolean handleCustomTypeFromJson(TypedElementDefinition<?> column, String setter, String columnType, String javaMemberName, JavaWriter out) {
        if(isType(columnType, JsonObject.class)){
            out.tab(2).println("%s(json.getJsonObject(\"%s\")==null?null:json.getJsonObject(\"%s\"));", setter, javaMemberName, javaMemberName);
            return true;
        }
        return super.handleCustomTypeFromJson(column, setter, columnType, javaMemberName, out);
    }

    @Override
    protected boolean handleCustomTypeToJson(TypedElementDefinition<?> column, String getter, String columnType, String javaMemberName, JavaWriter out) {
        if(isType(columnType, JsonObject.class)){
            out.tab(2).println("json.put(\"%s\",%s()==null?null:%s());", getJsonKeyName(column),getter,getter);
            return true;
        }
        return super.handleCustomTypeToJson(column, getter, columnType, javaMemberName, out);
    }
}

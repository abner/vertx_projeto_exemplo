package io.abner.vertx.exemplo.verticles;

import io.abner.vertx.jooq.tables.daos.CarneLeaoDao;
import io.abner.vertx.jooq.tables.pojos.CarneLeao;
import io.abner.vertx.jooq.tables.records.CarneLeaoRecord;
import io.reactiverse.pgclient.PgPoolOptions;
import io.reactiverse.reactivex.pgclient.PgClient;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;

public class MainVerticle extends AbstractVerticle {
  public MainVerticle() {}

  public static void main(String[] args) {
    Configuration configuration = new DefaultConfiguration();
    configuration.set(SQLDialect.POSTGRES_10); // or SQLDialect.POSTGRES
    // setup Vertx
    Vertx vertx = Vertx.vertx();

    PgPoolOptions config = new PgPoolOptions()
            .setHost("127.0.0.1")
            .setPort(5432)
            .setUser("root")
            .setDatabase("projeto_exemplo")
            .setPassword("r3dfr0g");

    if (System.getenv("EST_ENVIRONMENT") != null ) {
        // PGHOST, PGPORT, PGDATABASE, PGUSER, PGPASSWORD
        config = PgPoolOptions.fromEnv();
    }

    config.setMaxSize(20);

    // setup the client
    PgClient  client = PgClient.pool(vertx, config);

//    // instantiate a DAO (which is generated for you)
    CarneLeaoDao dao = new CarneLeaoDao(configuration, client);
//
//    // https://medium.com/@mingyulin_23146/jooq-converter-and-binding-for-jsonb-array-using-gradle-7969d6f6218f
//// https://stackoverflow.com/questions/54559904/how-to-convert-from-jsonb-in-jooq-3-11
//
      JsonObject g = new JsonObject();
      g.put("pessoa.nome", "nome");

    System.out.println("PESSOA _----------------------> " +g.encodePrettily());

    final CarneLeao carneLeao = new CarneLeao()
            .setCpf("80129498572")
            .setAnoCalendario(2018)
            .setNome("Abner Silva de Oliveira").setConteudo(new JsonObject().put("id", 1)
            );
    dao.insert(carneLeao)
        .onErrorReturn(e -> {
          e.printStackTrace();
          return 1;
        }).flatMap(r -> dao.findOneById(new CarneLeaoRecord(carneLeao.toJson()).key()))
        .subscribe(
            registro -> System.out.println("Registro obtido com sucesso!\n" + registro.orElse(new CarneLeao()).toJson().encodePrettily()), err -> err.printStackTrace());


  }
}

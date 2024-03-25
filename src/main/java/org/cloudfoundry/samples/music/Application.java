package org.cloudfoundry.samples.music;

import org.cloudfoundry.samples.music.config.SpringApplicationContextInitializer;
import org.cloudfoundry.samples.music.repositories.AlbumRepositoryPopulator;
import org.springframework.ai.autoconfigure.vectorstore.pgvector.PgVectorStoreAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(exclude = PgVectorStoreAutoConfiguration.class)
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
                .initializers(new SpringApplicationContextInitializer())
                .listeners(new AlbumRepositoryPopulator())
                .application()
                .run(args);
    }
}

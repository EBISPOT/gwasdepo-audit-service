package uk.ac.ebi.spot.gwas.deposition.audit.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uk.ac.ebi.spot.gwas.deposition.config.SystemConfigProperties;

import java.util.ArrayList;
import java.util.List;

public class MongoConfig {

    @Configuration
    @EnableMongoRepositories(basePackages = {"uk.ac.ebi.spot.gwas.deposition.audit.repository",
            "uk.ac.ebi.spot.gwas.deposition.repository"})
    @EnableTransactionManagement
    @Profile({"dev", "test","local"})
    public static class MongoConfigDev extends AbstractMongoConfiguration {

        @Autowired
        private SystemConfigProperties systemConfigProperties;

        @Autowired
        private AuditServiceConfig auditServiceConfig;

        @Override
        protected String getDatabaseName() {
            return auditServiceConfig.getDbName();
        }

        @Bean
        public GridFsTemplate gridFsTemplate() throws Exception {
            return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
        }

        @Override
        public MongoClient mongoClient() {
            String mongoUri = systemConfigProperties.getMongoUri();
            String dbUser = systemConfigProperties.getDbUser();
            String dbPassword = systemConfigProperties.getDbPassword();
            String credentials = "";
            if (dbUser != null && dbPassword != null) {
                dbUser = dbUser.trim();
                dbPassword = dbPassword.trim();
                if (!dbUser.equalsIgnoreCase("") &&
                        !dbPassword.equalsIgnoreCase("")) {
                    credentials = dbUser + ":" + dbPassword + "@";
                }
            }

            //return new MongoClient(new MongoClientURI("mongodb://" + mongoUri));
            return new MongoClient(new MongoClientURI("mongodb://" + credentials + mongoUri));
            //String mongoUri = systemConfigProperties.getMongoUri();
            //return new MongoClient(new MongoClientURI("mongodb://" + mongoUri));
        }
    }

    @Configuration
    @EnableMongoRepositories(basePackages = {"uk.ac.ebi.spot.gwas.deposition.audit.repository",
            "uk.ac.ebi.spot.gwas.deposition.repository"})
    @EnableTransactionManagement
    @Profile({"sandbox","sandbox-migration"})
    public static class MongoConfigSandbox extends AbstractMongoConfiguration {

        @Autowired
        private SystemConfigProperties systemConfigProperties;

        @Autowired
        private AuditServiceConfig auditServiceConfig;

        @Override
        protected String getDatabaseName() {
            return auditServiceConfig.getDbName();
        }

        @Bean
        public GridFsTemplate gridFsTemplate() throws Exception {
            return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
        }

        @Override
        public MongoClient mongoClient() {
            String mongoUri = systemConfigProperties.getMongoUri();
            String dbUser = systemConfigProperties.getDbUser();
            String dbPassword = systemConfigProperties.getDbPassword();
            String credentials = "";
            if (dbUser != null && dbPassword != null) {
                dbUser = dbUser.trim();
                dbPassword = dbPassword.trim();
                if (!dbUser.equalsIgnoreCase("") &&
                        !dbPassword.equalsIgnoreCase("")) {
                    credentials = dbUser + ":" + dbPassword + "@";
                }
            }

            return new MongoClient(new MongoClientURI("mongodb://" + credentials + mongoUri));
            // return new MongoClient(new MongoClientURI("mongodb://" + mongoUri));
        }
    }

    @Configuration
    @EnableMongoRepositories(basePackages = {"uk.ac.ebi.spot.gwas.deposition.audit.repository",
            "uk.ac.ebi.spot.gwas.deposition.repository"})
    @EnableTransactionManagement
    @Profile({"prod"})
    public static class MongoConfigProd extends AbstractMongoConfiguration {

        @Autowired
        private SystemConfigProperties systemConfigProperties;

        @Autowired
        private AuditServiceConfig auditServiceConfig;

        @Override
        protected String getDatabaseName() {
            return auditServiceConfig.getDbName();
        }

        @Bean
        public GridFsTemplate gridFsTemplate() throws Exception {
            return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
        }

        @Override
        public MongoClient mongoClient() {
            String mongoUri = systemConfigProperties.getMongoUri();
            String dbUser = systemConfigProperties.getDbUser();
            String dbPassword = systemConfigProperties.getDbPassword();
            String credentials = "";
            if (dbUser != null && dbPassword != null) {
                dbUser = dbUser.trim();
                dbPassword = dbPassword.trim();
                if (!dbUser.equalsIgnoreCase("") &&
                        !dbPassword.equalsIgnoreCase("")) {
                    credentials = dbUser + ":" + dbPassword + "@";
                }
            }

            return new MongoClient(new MongoClientURI("mongodb://" + credentials + mongoUri));
        }
    }
}

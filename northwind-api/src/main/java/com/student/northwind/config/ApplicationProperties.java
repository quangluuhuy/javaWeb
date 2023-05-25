package com.student.northwind.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@Component
@Getter
@Setter
public class ApplicationProperties {
    private final ApplicationProperties.Async async = new ApplicationProperties.Async();
    private final ApplicationProperties.Http http = new ApplicationProperties.Http();
    private final ApplicationProperties.Cache cache = new ApplicationProperties.Cache();
    private final ApplicationProperties.Security security = new ApplicationProperties.Security();
    private final ApplicationProperties.ApiDocs apiDocs = new ApplicationProperties.ApiDocs();
    private final ApplicationProperties.Logging logging = new ApplicationProperties.Logging();
    private final CorsConfiguration cors = new CorsConfiguration();

    private final ApplicationProperties.AuditEvents auditEvents = new ApplicationProperties.AuditEvents();

    @Getter
    @Setter
    public static class AuditEvents {
        private int retentionPeriod = 30;
    }

    @Getter
    @Setter
    public static class ClientApp {
        private String name = "HTI_App";
    }

    @Getter
    @Setter
    public static class Logging {
        private boolean useJsonFormat = false;
    }

    @Getter
    @Setter
    public static class ApiDocs {
        private String title = "Application API";
        private String description = "API documentation";
        private String version = "0.0.1";
        private String termsOfServiceUrl = "";
        private String contactName = "";
        private String contactUrl = "";
        private String contactEmail = "";
        private String license = "";
        private String licenseUrl = "";
        private ApplicationProperties.ApiDocs.Server[] servers = new ApplicationProperties.ApiDocs.Server[0];

        @Getter
        @Setter
        public static class Server {
            private String url;
            private String description;
        }
    }

    @Getter
    @Setter
    public static class Security {
        private String contentSecurityPolicy = "default-src 'self'; frame-src 'self' data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:";
        private final ApplicationProperties.Security.ClientAuthorization clientAuthorization = new ApplicationProperties.Security.ClientAuthorization();
        private final ApplicationProperties.Security.Authentication authentication = new ApplicationProperties.Security.Authentication();
        private final ApplicationProperties.Security.RememberMe rememberMe = new ApplicationProperties.Security.RememberMe();

        @Getter
        @Setter
        public static class RememberMe {
            @NotNull
            private String key = null;
        }

        @Getter
        @Setter
        public static class Authentication {
            private final ApplicationProperties.Security.Authentication.Jwt jwt = new ApplicationProperties.Security.Authentication.Jwt();

            @Getter
            @Setter
            public static class Jwt {
                private String secret;
                private String base64Secret;
                private long tokenValidityInSeconds = 1800L;
                private long tokenValidityInSecondsForRememberMe = 2592000L;
            }
        }

        @Getter
        @Setter
        public static class ClientAuthorization {
            private String accessTokenUri;
            private String tokenServiceId;
            private String clientId;
            private String clientSecret;
        }
    }

    @Getter
    @Setter
    public static class Cache {
        private final ApplicationProperties.Cache.Caffeine caffeine = new ApplicationProperties.Cache.Caffeine();
        private final ApplicationProperties.Cache.Memcached memcached = new ApplicationProperties.Cache.Memcached();

        @Getter
        @Setter
        public static class Memcached {
            private boolean enabled = false;
            private String servers = "localhost:11211";
            private int expiration = 300;
            private boolean useBinaryProtocol = true;
            private ApplicationProperties.Cache.Memcached.Authentication authentication = new ApplicationProperties.Cache.Memcached.Authentication();

            @Getter
            @Setter
            public static class Authentication {
                private boolean enabled = false;
                private String username;
                private String password;
            }
        }

        @Getter
        @Setter
        public static class Caffeine {
            private int timeToLiveSeconds = 3600;
            private long maxEntries = 100L;
        }
    }

    @Getter
    @Setter
    public static class Http {
        private final ApplicationProperties.Http.Cache cache = new ApplicationProperties.Http.Cache();
        private Integer connectionTimeout;
        private Integer readTimeout;
        private Integer writeTimeout;
        Integer byteBuffer;

        @Getter
        @Setter
        public static class Cache {
            private int timeToLiveInDays = 1461;
        }
    }

    @Getter
    @Setter
    public static class Async {
        private int corePoolSize = 2;
        private int maxPoolSize = 50;
        private int queueCapacity = 10000;

    }
}
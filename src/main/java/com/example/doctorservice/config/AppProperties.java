package com.example.doctorservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String name;
    private String version;
    private int maxDoctors;
    private boolean enableNotifications;
    private Email email = new Email();
    private Admin admin = new Admin();

    public static class Email {
        private String host;
        private int port;
        private String from;
        // getters/setters
        public String getHost() {
            return host;
        }
        public int getPort() {
            return port;
        }
        public String getFrom() {
            return from;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public void setFrom(String from) {
            this.from = from;
        }
    }

    public static class Admin {
        private String name;
        private String email;
        // getters/setters
        public String getEmail() {
            return email;
        }
        public String getName() {
            return name;
        }
        public void setEmail(String email) {
            this.email = email;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    // getters/setters for all fields
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public int getMaxDoctors() {
        return maxDoctors;
    }
    public void setMaxDoctors(int maxDoctors) {
        this.maxDoctors = maxDoctors;
    }
    public boolean isEnableNotifications() {
        return enableNotifications;
    }
    public void setEnableNotifications(boolean enableNotifications) {
        this.enableNotifications = enableNotifications;
    }
    public Email getEmail() {
        return email;
    }
    public void setEmail(Email email) {
        this.email = email;
    }
    public Admin getAdmin() {
        return admin;
    }
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}


// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.stereotype.Component;

// import java.util.List;

// @Component
// @ConfigurationProperties(prefix = "app")
// public class AppProperties {

//     private String name;
//     private String version;
//     private String description;
//     private int maxDoctors;
//     private boolean enableNotifications;
//     private List<String> supportedSpecializations;

//     // Nested properties
//     private Email email = new Email();
//     private Database db = new Database();

//     // Email nested class
//     public static class Email {
//         private String host;
//         private int port;
//         private String username;
//         private String from;

//         // Getters and Setters
//         public String getHost() { return host; }
//         public void setHost(String host) { this.host = host; }

//         public int getPort() { return port; }
//         public void setPort(int port) { this.port = port; }

//         public String getUsername() { return username; }
//         public void setUsername(String username) { this.username = username; }

//         public String getFrom() { return from; }
//         public void setFrom(String from) { this.from = from; }
//     }

//     // Database nested class
//     public static class Database {
//         private Pool pool = new Pool();

//         public static class Pool {
//             private int minSize;
//             private int maxSize;

//             public int getMinSize() { return minSize; }
//             public void setMinSize(int minSize) { this.minSize = minSize; }

//             public int getMaxSize() { return maxSize; }
//             public void setMaxSize(int maxSize) { this.maxSize = maxSize; }
//         }

//         public Pool getPool() { return pool; }
//         public void setPool(Pool pool) { this.pool = pool; }
//     }

//     // Getters and Setters
//     public String getName() { return name; }
//     public void setName(String name) { this.name = name; }

//     public String getVersion() { return version; }
//     public void setVersion(String version) { this.version = version; }

//     public String getDescription() { return description; }
//     public void setDescription(String description) { this.description = description; }

//     public int getMaxDoctors() { return maxDoctors; }
//     public void setMaxDoctors(int maxDoctors) { this.maxDoctors = maxDoctors; }

//     public boolean isEnableNotifications() { return enableNotifications; }
//     public void setEnableNotifications(boolean enableNotifications) {
//         this.enableNotifications = enableNotifications;
//     }

//     public List<String> getSupportedSpecializations() {
//         return supportedSpecializations;
//     }
//     public void setSupportedSpecializations(List<String> supportedSpecializations) {
//         this.supportedSpecializations = supportedSpecializations;
//     }

//     public Email getEmail() { return email; }
//     public void setEmail(Email email) { this.email = email; }

//     public Database getDb() { return db; }
//     public void setDb(Database db) { this.db = db; }
// }
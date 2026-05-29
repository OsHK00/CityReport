package com.ciudadreporta.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    private Security security = new Security();
    private Verification verification = new Verification();

    public Security getSecurity() { return security; }
    public void setSecurity(Security security) { this.security = security; }

    public Verification getVerification() { return verification; }
    public void setVerification(Verification verification) { this.verification = verification; }

    public static class Security {
        private boolean enabled = false;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }

    public static class Verification {
        private boolean enabled = false;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }
}

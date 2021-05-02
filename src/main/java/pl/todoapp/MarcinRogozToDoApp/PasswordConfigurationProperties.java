package pl.todoapp.MarcinRogozToDoApp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("password-hash")
public class PasswordConfigurationProperties {

    private Template template;

    public Template getTemplate() { return template;}

    public void setTemplate(final Template template) {this.template = template;}

    public static class Template {
        private boolean allowPasswordHash;

        public boolean isAllowPasswordHash() { return allowPasswordHash; }

        public void setAllowPasswordHash(final boolean allowPasswordHash) {
            this.allowPasswordHash = allowPasswordHash;
        }
    }

}

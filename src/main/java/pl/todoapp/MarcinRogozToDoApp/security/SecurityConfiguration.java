package pl.todoapp.MarcinRogozToDoApp.security;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

// Adnotacja do keycloak - konfiguracja
@KeycloakConfiguration              // Rozszerza klasę - mówi o tym jak użytkownik powinien logować się
class SecurityConfiguration extends KeycloakWebSecurityConfigurerAdapter {

    // Wymagana jest rejestracja beana
    @Bean
    KeycloakSpringBootConfigResolver keycloakSpringBootConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    // Nazwa roli dużymi literami
    // Dodawanie pocesowania ról podczas pracy aplikacji
    @Autowired
    void configureGlobal(AuthenticationManagerBuilder auth) {
        // Klasa służy do zmieniania ról
        var authorityMapper = new SimpleAuthorityMapper();
        // Role mają prefix ROLE_
        authorityMapper.setPrefix("ROLE_");
        // Duże litery
        authorityMapper.setConvertToUpperCase(true);


        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(authorityMapper);
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        // Zapamiętujemy wszystkie sesje logowania
        // Jest jescze nullAtuh.. nie dostajemy informacji
        // Rejest w aplikacji
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl() {
        });
    }
}

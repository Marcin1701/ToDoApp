package pl.todoapp.MarcinRogozToDoApp.security;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

// Adnotacja do keycloak - konfiguracja
@KeycloakConfiguration              // Rozszerza klasę - mówi o tym jak użytkownik powinien logować się
class SecurityConfiguration extends KeycloakWebSecurityConfigurerAdapter {

    // Wymagana jest rejestracja beana
    @Bean
    KeycloakSpringBootConfigResolver keycloakConfigResolver() {
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

    // Zabezpieczamy dostęp do InfoController
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // Wołamy klasę bazową
        super.configure(http);
        // Wszystkie żądania niech będą sprawdzane i autoryzowane
        // Adresy które mają być zabezpieczonne
        http.authorizeRequests()
                .antMatchers("/info/*") // Do czego mają dostęp
                .hasRole("USER")    // Jaka rola
                .antMatchers("/projects")
                .hasRole("ADMIN")
                .anyRequest()       // reszta requestów
                .permitAll();       // pozwalaj dalej
    }

    // OAuth 2.0, JWT, OpenID
    /*
        Możemy zdelegować hasła i konta do facebooka.
        Wysyłamy do facebooka zapytanie o użytkownika, dostajemy potwierdzenie informacji.

        1. Jesteśmy uprawnieni do pewnych informacji dostępnych od providera Facebooka
        2. Zewnętrzny dostarczyciel tożsamości potwierdza informacje
        3. OAuth 2.0 - jest takim protokołem, keycloak wspiera tego typu protokoły
        4. Jest OpenID connect - oprócz uprawnień, mamy konkretny adres dostarczyciela tożsamości,
            skąd możemy pobrać dane
        5. Format tokena z uprawnieniami to JWT - oauth 2 mocno czerpie z JWT, jest to kod szyfrowany, 3 części:
            - nagłówek,
            - informacje,
            - podpis weryfikacyjny
     */

}

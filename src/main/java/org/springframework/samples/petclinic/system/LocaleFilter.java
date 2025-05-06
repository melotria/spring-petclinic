package org.springframework.samples.petclinic.system;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

/**
 * JAX-RS filter that handles locale changes based on the "lang" query parameter.
 * It stores the selected locale in a cookie for future requests.
 */
@Provider
@PreMatching
@ApplicationScoped
public class LocaleFilter implements ContainerRequestFilter {

    private static final String LANG_PARAM = "lang";
    private static final String LOCALE_COOKIE = "locale";
    private static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 30; // 30 days

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Check if there's a lang parameter in the request
        String langParam = requestContext.getUriInfo().getQueryParameters().getFirst(LANG_PARAM);

        if (langParam != null && !langParam.isEmpty()) {
            // Set the locale in a cookie
            Locale locale = Locale.forLanguageTag(langParam);
            NewCookie localeCookie = new NewCookie(LOCALE_COOKIE, locale.toLanguageTag(), "/", null, null, COOKIE_MAX_AGE, false, false);

            // Store the cookie in the request context for later use
            requestContext.setProperty("localeCookie", localeCookie);
        }
    }
}

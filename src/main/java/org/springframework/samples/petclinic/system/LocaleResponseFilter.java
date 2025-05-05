package org.springframework.samples.petclinic.system;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * JAX-RS filter that adds the locale cookie to the response if it was set in the request.
 */
@Provider
@ApplicationScoped
public class LocaleResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        // Check if a locale cookie was set in the request
        NewCookie localeCookie = (NewCookie) requestContext.getProperty("localeCookie");

        if (localeCookie != null) {
            // Add the cookie to the response
            responseContext.getHeaders().add("Set-Cookie", localeCookie.toString());
        }
    }
}

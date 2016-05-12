package com.epam.jc;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.ws.rs.ApplicationPath;
import java.lang.reflect.Field;
import java.nio.charset.Charset;

/**
 * Created on 12/04/16.
 *
 * @author Vladislav Boboshko
 */
@ApplicationPath("api")
public class Init extends ResourceConfig {
    public Init() throws NoSuchFieldException, IllegalAccessException {
        packages("com.epam.jc.REST");
    }
}

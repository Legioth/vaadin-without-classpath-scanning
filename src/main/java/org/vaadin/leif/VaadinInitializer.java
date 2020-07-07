package org.vaadin.leif;

import java.util.Arrays;
import java.util.HashSet;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.InternalServerError;
import com.vaadin.flow.router.RouteNotFoundError;
import com.vaadin.flow.server.startup.DevModeInitializer;
import com.vaadin.flow.server.startup.ErrorNavigationTargetInitializer;
import com.vaadin.flow.server.startup.RouteRegistryInitializer;
import com.vaadin.flow.server.startup.ServletContextListeners;

public class VaadinInitializer implements ServletContextListener {

    private ServletContextListeners servletContextListeners = new ServletContextListeners();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Run initializers with relevant classes from the classpath
        runInitializers(sce);

        // Finalize initialization
        servletContextListeners.contextInitialized(sce);
    }

    private static void runInitializers(ServletContextEvent sce) {
        // Anything implementing HasErrorParameter
        runInitializer(new ErrorNavigationTargetInitializer(), sce, RouteNotFoundError.class,
                InternalServerError.class);

        // @Route
        runInitializer(new RouteRegistryInitializer(), sce, MainView.class);

        // @NpmPackage, @JsModule, @CssImport, @JavaScript or @Theme
        runInitializer(new DevModeInitializer(), sce, MainView.class, Button.class, Notification.class,
                VerticalLayout.class, TextField.class);
    }

    private static void runInitializer(ServletContainerInitializer initializer, ServletContextEvent sce,
            Class<?>... types) {
        try {
            initializer.onStartup(new HashSet<>(Arrays.asList(types)), sce.getServletContext());
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        servletContextListeners.contextDestroyed(sce);
    }
}

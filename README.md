# Vaadin without classpath scanning

Vaadin uses `ServletContainerInitializer` from the Servlet 3.0 specification to automatically discover various annotated classes from the application's classpath and configure itself accordingly.
The benefit of `ServletContainerInitializer` is that it allows Vaadin to hook into the classpath scanning that the application server is otherwise also doing to find e.g. `@WebServlet` classes.
This means that Vaadin can find the classes it needs without any additional scanning overhead compared to what the application server already does.

The trouble starts if you want to speed up the classpath scanning performed by the application server.
Some server implementations can be configured to only look for classes in specific locations while others have less granular configuration alternatives.

This proof of concept shows a way of manually configuring Vaadin for scenarios where classpath scanning is completely disabled. Scanning is disabled by setting the `metadata-comple` attribute to `true` and by defining an empty `<absolute-ordering>` definition in `web.xml`.

This example uses hardcoded lists of classes and only includes the classes needed for this application to function.
A more sophisticated approach could use a 3rd party classpath library that can be configured to taste.
The overall principle of how to integrate with Vaadin would be the same regardless.    
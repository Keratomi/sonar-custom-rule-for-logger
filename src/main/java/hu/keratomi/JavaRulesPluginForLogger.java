package hu.keratomi;

import org.sonar.api.Plugin;

public class JavaRulesPluginForLogger implements Plugin {
    @Override
    public void define(Context context) {
        // server extensions -> objects are instantiated during server startup
        context.addExtension(JavaRulesDefinitionForLogger.class);

        // batch extensions -> objects are instantiated during code analysis
        context.addExtension(JavaFileCheckRegistrarForLogger.class);
    }
}

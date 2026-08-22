# Keep project-specific minify rules here.

# iText and XML stream classes referenced in metadata are not needed at runtime on Android.
-dontwarn javax.xml.stream.**
-dontwarn org.codehaus.stax2.**
-dontwarn com.ctc.wstx.**
-dontwarn aQute.bnd.annotation.spi.ServiceProvider
-dontwarn sharpen.config.MappingConfiguration

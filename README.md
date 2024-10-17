# Logger-Android


## Proguard options

Logging appenders are created via reflection so class names must be left  intact for runtime.  In 
order to keep them you will need to add the following to your application proguard rules:

```
-keep class com.esri.logger.appender.** { *; }
```
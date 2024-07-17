-dontwarn io.grpc.util.MultiChildLoadBalancer$AcceptResolvedAddressRetVal
-dontwarn org.apache.log4j.Level
-dontwarn org.apache.log4j.Logger
-dontwarn org.apache.log4j.Priority
-dontwarn org.apache.logging.log4j.Level
-dontwarn org.apache.logging.log4j.LogManager
-dontwarn org.apache.logging.log4j.Logger
-dontwarn org.apache.logging.log4j.message.MessageFactory
-dontwarn org.apache.logging.log4j.spi.ExtendedLogger
-dontwarn org.apache.logging.log4j.spi.ExtendedLoggerWrapper
-dontwarn org.eclipse.jetty.alpn.ALPN$ClientProvider
-dontwarn org.eclipse.jetty.alpn.ALPN$Provider
-dontwarn org.eclipse.jetty.alpn.ALPN$ServerProvider
-dontwarn org.eclipse.jetty.alpn.ALPN
-dontwarn org.eclipse.jetty.npn.NextProtoNego$ClientProvider
-dontwarn org.eclipse.jetty.npn.NextProtoNego$Provider
-dontwarn org.eclipse.jetty.npn.NextProtoNego$ServerProvider
-dontwarn org.eclipse.jetty.npn.NextProtoNego
-dontwarn org.slf4j.ILoggerFactory
-dontwarn org.slf4j.Logger
-dontwarn org.slf4j.LoggerFactory
-dontwarn org.slf4j.Marker
-dontwarn org.slf4j.helpers.FormattingTuple
-dontwarn org.slf4j.helpers.MessageFormatter
-dontwarn org.slf4j.helpers.NOPLoggerFactory
-dontwarn org.slf4j.spi.LocationAwareLogger
-dontwarn reactor.blockhound.integration.BlockHoundIntegration

# Jetpack Compose
-keep class androidx.compose.** { *; }
-keep class androidx.compose.runtime.Composable

# Hilt
-dontwarn com.google.dagger.**
-keep class * extends dagger.hilt.android.HiltAndroidApp
-keep @dagger.hilt.android.HiltAndroidApp class ** { *; }
-keep class dagger.** { *; }

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature

# OkHttp
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }

# Room
-keepattributes *Annotation*
-dontwarn androidx.room.**
-keep class androidx.room.** { *; }
-keep @androidx.room.Entity class *
-keepclassmembers class * extends androidx.room.RoomDatabase {
    public static ** databaseBuilder(android.content.Context, java.lang.Class);
}

# Coil
-keep class coil.** { *; }

# Other (Adjust as Needed)
-dontwarn com.valentinilk.shimmer.**
-keep class io.coil.kt.** { *; }
-keep class com.google.cloud.** { *; }

# General Rules (Important!)
-keep public class * extends androidx.lifecycle.ViewModel
-keep public class * extends androidx.lifecycle.LiveData
-keep public class * extends androidx.lifecycle.Observer
-dontnote android.app.Fragment
-dontnote android.view.View
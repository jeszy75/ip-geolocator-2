package geolocator;

import feign.Feign;
import feign.Logger;
import feign.Param;
import feign.RequestLine;
import feign.gson.GsonDecoder;

import com.google.gson.GsonBuilder;
import com.google.gson.FieldNamingPolicy;
import feign.slf4j.Slf4jLogger;

/**
 * Interface to obtain geolocation information for IP addresses and host names.
 * The actual implementation uses the
 * <a href="https://reallyfreegeoip.org/">https://reallyfreegeoip.org</a> API.
 * The {@link #newInstance()} method is provided for obtaining a
 * {@code GeoLocator} object.
 */
public interface GeoLocator {

    /**
     * {@return geolocation information for the JVM running the application}
     *
     * @throws feign.FeignException if any error occurs
     */
    @RequestLine("GET")
    GeoLocation getGeoLocation();

    /**
     * {@return  geolocation information for the IP address or host name
     * specified}
     *
     * @param ipOrHostName an IP address or host name
     * @throws feign.FeignException if any error occurs
     */
    @RequestLine("GET /{ipOrHostName}")
    GeoLocation getGeoLocation(@Param("ipOrHostName") String ipOrHostName);

    /**
     * {@return an object implementing the {@code GeoLocator} interface}
     */
    static GeoLocator newInstance() {
        return Feign.builder()
                .decoder(new GsonDecoder(new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create()))
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                .target(GeoLocator.class, "https://reallyfreegeoip.org/json/");
    }

}

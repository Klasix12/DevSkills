package com.klasix12.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties("security.public-endpoints")
public class PublicEndpointsConfig {

    private List<String> anyMethod;
    private List<String> getOnly;
}

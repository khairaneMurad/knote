package io.leank8s.knotejava.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "knote")
@Getter
public class KnoteProperties {
    @Value("${uploadDir:/tmp/uploads/}")
    private String uploadDir;
}

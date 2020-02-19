package com.codeagles.resource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/2/19
 * Time: 3:18 PM
 * <p>
 * Description:
 */
@Component
@ConfigurationProperties(prefix = "file")
@PropertySource("classpath:file-upload-dev.properties")
@Data
public class FileUpload {

    private String imageUserFaceLocation;
    private String imageServerUrl;
}

/*
 * @Author: shenzheng
 * @Date: 2020/6/15 23:14
 */

package com.example.mymmal.swagger;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class SwaggerInfo {
    private String groupName = "controller";

    private String basePackage;

    private String antPath;

    private String title = "HTTP API";

    private String description = "Swagger 自动生成接口文档";

    private String license = "Apache License Version 2.0";

}

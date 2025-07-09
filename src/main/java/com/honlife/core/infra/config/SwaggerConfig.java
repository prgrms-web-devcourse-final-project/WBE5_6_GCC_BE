package com.honlife.core.infra.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.LinkedHashMap;
import java.util.Map;

@OpenAPIDefinition(
    info = @Info(
        title = "혼라이프 API 명세서",
        description = "백엔드 API 서버(완성된 API가 아닙니다.)",
        version = "v1"
    )
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApiSpec() {
        // 순서가 보장된 스키마 생성
        ObjectSchema errorSchema = new ObjectSchema();
        errorSchema.addProperty("status", new StringSchema().description("에러 상태 코드").example("status_code"));
        errorSchema.addProperty("message", new StringSchema().description("에러 메시지").example("string"));
        errorSchema.addProperty("data", new ObjectSchema().nullable(true).description("에러 데이터").example(null));

        return new OpenAPI().components(new Components()
            // 에러 응답 스키마 - status, message, data 순서
            .addSchemas("ApiErrorResponse", errorSchema)

            // 예시 추가 - LinkedHashMap으로 순서 보장
            .addExamples("ErrorExample", new Example()
                .summary("에러 응답 예시")
                .description("일반적인 에러 응답 형태")
                .value(createErrorExampleMap())));
    }

    @Bean
    public OperationCustomizer operationCustomizer() {
        return (operation, handlerMethod) -> {
            // 에러 응답만 추가
            MediaType errorMediaType = new MediaType()
                .schema(new Schema<>().$ref("#/components/schemas/ApiErrorResponse"));

            // 예시 추가 - LinkedHashMap으로 순서 보장
            errorMediaType.addExamples("모든 에러", new Example()
                .summary("모든 에러")
                .value(createErrorExampleMap()));

            operation.getResponses().addApiResponse("4xx/5xx", new ApiResponse()
                .description("에러발생시의 응답 예시입니다.")
                .content(new Content().addMediaType("application/json", errorMediaType)));

            return operation;
        };
    }

    // Helper method for creating example map with preserved order
    private Map<String, Object> createErrorExampleMap() {
        Map<String, Object> map = new LinkedHashMap<>(); // 순서 보장
        map.put("status", "status_code");
        map.put("message", "string");
        map.put("data", null);
        return map;
    }
}
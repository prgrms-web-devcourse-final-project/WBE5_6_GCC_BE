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
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.LinkedHashMap;
import java.util.Map;

@OpenAPIDefinition(
    info = @Info(
        title = "혼라이프 API 명세서",
        description = "1차적으로 완성된 API 목록이며, 추후 변경사항이 있을 수 있습니다.<br>"
            + "특히 <strong>HTTP 메소드에 대한 변경 및 요청 파라메터에 대한 변경이 있을 수 있음</strong>에 유의하세요.<br>"
            + "제안 및 문의사항이 있다면 언제든 알려주세요.<br><br>"
            + "<strong>*모든 API는 로그인 API실행후 발급받은 토큰을 Authorize에 입력하고 난뒤 사용할 수 있습니다.</strong>",
        version = "v1"
    )
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApiSpec() {
        // 순서가 보장된 스키마 생성
        ObjectSchema errorSchema = new ObjectSchema();
        errorSchema.addProperty("status",
            new StringSchema().description("에러 상태 코드").example("status_code"));
        errorSchema.addProperty("message",
            new StringSchema().description("에러 메시지").example("string"));
        errorSchema.addProperty("data",
            new ObjectSchema().nullable(true).description("에러 데이터").example(null));

        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes("bearerAuth",
                    new SecurityScheme()
                        .name("Authorization")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Bearer는 제외하고 입력하세요."))

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
# ![RealWorld Example App](https://github.com/raeperd/realworld-springboot-kotlin/blob/master/logo.png?raw=true)

# Getting started
```
keytool -genkeypair \
-alias apiKey -keyalg RSA \
-keypass "pass1234" -storepass "pass1234" \
-keystore src/main/resources/certs/apiKey.jks \
-dname "CN=realworld, OU=REALWORLD API, O=realworld.com \
L=Seoul, C=KR"

./gradlew bootRun
```
# API Spec

- [Backend endpoints](https://realworld-docs.netlify.app/docs/specs/backend-specs/endpoints)

- [API response format](https://realworld-docs.netlify.app/docs/specs/backend-specs/api-response-format)

- [Error handling](https://realworld-docs.netlify.app/docs/specs/backend-specs/error-handling)

- [CORS](https://realworld-docs.netlify.app/docs/specs/backend-specs/cors)

### API Test Based on provided postman collection
- [provided link](https://github.com/gothinkster/realworld/tree/main/api)

![img.png](result.png)

### 학습 하고자 한 것

> Spring Security OAuth2 Resource server 를 커스텀하여 Jwt 의 발급과 검증에
> Spring 의 BearerTokenAuthenticationFilter 및 Jwt 를 사용하는 법! 
> 
> Kotlin 에서의 querydsl 세팅
>
> + Spring Boot 2 에서 Spring Boot 3 로의 Migration

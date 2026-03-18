FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY gradle/ gradle/
COPY gradlew settings.gradle.kts build.gradle.kts gradle.properties ./
COPY buildSrc/ buildSrc/

RUN chmod +x gradlew

COPY common/ common/
COPY data/ data/
COPY features/ features/
COPY shared/ shared/
COPY webApp/ webApp/

ARG SUPABASE_URL
ARG SUPABASE_SCHEMA=public
ARG SUPABASE_CLIENT_API_KEY
ARG SUPABASE_ANON_AUTH_ENABLED=false
ARG POWERSYNC_URL=https://unused
ARG POWERSYNC_DEV_TOKEN=
ARG DEBUG=false

RUN printf "supabase.url=%s\nsupabase.schema=%s\nsupabase.clientApiKey=%s\nsupabase.anonAuthEnabled=%s\npowersync.url=%s\npowersync.devToken=%s\ndebug=%s\n" \
    "$SUPABASE_URL" "$SUPABASE_SCHEMA" "$SUPABASE_CLIENT_API_KEY" \
    "$SUPABASE_ANON_AUTH_ENABLED" "$POWERSYNC_URL" "$POWERSYNC_DEV_TOKEN" "$DEBUG" \
    > local.properties

RUN ./gradlew :webApp:jsBrowserDistribution --no-daemon

FROM nginx:alpine

COPY docker/nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/webApp/build/dist/js/productionExecutable/ /usr/share/nginx/html/

EXPOSE 3000

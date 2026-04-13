# Journal Landing

Статический лендинг для публикации веб-версии и дистрибутивов приложения.

## Перед деплоем

Отредактируйте `landing/index.html` и замените три ссылки:

* web-версия
* APK
* Windows ZIP

## Как собрать Windows ZIP

```bash
./gradlew :desktopApp:createDistributable
```

После этого архивируйте папку `desktopApp/build/compose/binaries/main/app/Journal` в один `.zip`.

Готовый архив в текущем репозитории лежит здесь:

* `landing/downloads/Journal-windows.zip`

## Локальный запуск

```bash
docker build -f landing/Dockerfile landing -t journal-landing
docker run --rm -p 3000:3000 journal-landing
```

Страница будет доступна на `http://localhost:3000`.

## Coolify

Рекомендуемая конфигурация:

* `Build Pack`: `Dockerfile`
* `Base Directory`: `/landing`
* `Dockerfile Location`: `/Dockerfile`

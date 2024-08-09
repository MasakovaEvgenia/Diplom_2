# Тестирование API Stellar Burgers
## Описание
Проект предназначен для тестирования API Stellar Burgers. В тесте проверяется создание пользователя, логин пользователя, изменение данных пользователя, создание заказа и получение заказов пользователя.

## Ссылки
- [Приложение с заказами космического фастфуда Stellar Burgers](https://stellarburgers.nomoreparties.site/)
- [Документация API](https://code.s3.yandex.net/qa-automation-engineer/java/cheatsheets/paid-track/diplom/api-documentation.pdf)

## Технологии

| Технология                | Версия  |
|---------------------------|---------|
| Java                      | 11      |
| JUnit                     | 4.13.2  |
| Maven                     | 3.8.1   |
| allure.version            | 2.15.0  |
| groovy                    | 3.0.8   |
| gson                      | 2.8.9   |
| maven-surefire-plugin     | 2.22.2  |
| allure-maven              | 2.10.0  |

## Создание отчёта Allure

```sh
# добавляем папку с отчётом Allure к файлам. Ключ -f пригодится, если папка target указана в .gitignore
git add -f .\target\allure-results\.

# выполняем коммит
git commit -m "add allure report"

# отправляем файлы в удалённый репозиторий
git push



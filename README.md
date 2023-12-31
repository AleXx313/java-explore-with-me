# java-explore-with-me
Дипломный проект.
Веб приложение предстовляющее собой афишу для создания и поиска проводимых мероприятий.

## Описание проекта
### Общая часть
<details>
  <summary>Описание</summary>
  Свободное время — ценный ресурс. Ежедневно мы планируем, как его потратить — куда и с кем сходить. Сложнее всего в таком планировании поиск информации и переговоры. Нужно учесть много деталей: какие намечаются мероприятия, свободны ли в этот момент друзья, как всех пригласить и где собраться.
Приложение, которое вы будете создавать, — афиша. В этой афише можно предложить какое-либо событие от выставки до похода в кино и собрать компанию для участия в нём.

### Основной сервис
API основного сервиса делится на три части:
- публичная будет доступна без регистрации любому пользователю сети;
- закрытая будет доступна только авторизованным пользователям;
- административная — для администраторов сервиса.

### Требования к публичному API
- Публичный API должен предоставлять возможности поиска и фильтрации событий. Учтите следующие моменты:
сортировка списка событий должна быть организована либо по количеству просмотров, которое будет запрашиваться в сервисе статистики, либо по датам событий;
- при просмотре списка событий должна возвращаться только краткая информация о мероприятиях;
- просмотр подробной информации о конкретном событии нужно настроить отдельно (через отдельный эндпоинт);
- каждое событие должно относиться к какой-то из закреплённых в приложении категорий;
- должна быть настроена возможность получения всех имеющихся категорий и подборок событий (такие подборки будут составлять администраторы ресурса);
- каждый публичный запрос для получения списка событий или полной информации о мероприятии должен фиксироваться сервисом статистики.

###  Требования к API для авторизованных пользователей
Закрытая часть API должна реализовать возможности зарегистрированных пользователей продукта. Вот что нужно учесть:
авторизованные пользователи должны иметь возможность добавлять в приложение новые мероприятия, редактировать их и просматривать после добавления;
должна быть настроена подача заявок на участие в интересующих мероприятиях;
создатель мероприятия должен иметь возможность подтверждать заявки, которые отправили другие пользователи сервиса.
  Требования к API для администратора
- Административная часть API должна предоставлять возможности настройки и поддержки работы сервиса. Обратите внимание на эти пункты:
нужно настроить добавление, изменение и удаление категорий для событий;
- должна появиться возможность добавлять, удалять и закреплять на главной странице подборки мероприятий;
- требуется наладить модерацию событий, размещённых пользователями, — публикация или отклонение;
- также должно быть настроено управление пользователями — добавление, активация, просмотр и удаление.

### Модель данных
  Жизненный цикл события должен включать несколько этапов.
- Создание.
- Ожидание публикации. В статус ожидания публикации событие переходит сразу после создания.
- Публикация. В это состояние событие переводит администратор.
- Отмена публикации. В это состояние событие переходит в двух случаях. Первый — если администратор решил, что его нельзя публиковать. Второй — когда инициатор события решил отменить его на этапе ожидания публикации.
</details>

### Комментарии
<details>
  <summary>Описание</summary>
  
  ### Описание
  Комментарии - дополнение к основному сервису, которое продумывалось самостоятельно.
  
- Комментарии могут оставлять только пользователи. 
- Комментарий не как способ переписки, а как полноценное ревью. 
- 1 пользователь - 1 комментарий. 
- Количество комментариев - часть FullEventDto и PublicEventDto.
- Премодерация комментария не требуется.

### Общие функции

Пользователь может:
- оставить комментарий
- редактировать комментарий
- удалить комментарий
- лайкать или дизлайкать комментарии

Администратор может:
- удалить комментарий

### Модель комментария:
- id
- text
- статус комментария
- Комментируемый эвент
- Комментатор

### Реакция:
- id
- комментарий
- оставивший реакцию юзер
- лайк или дизлайк
</details>

## Используемые технологии
+ [Java](https://www.java.com/)
+ [Spring Boot](https://spring.io/projects/spring-boot)
+ [Hibernate](https://hibernate.org)
+ [PostgreSQL](https://www.postgresql.org)
+ [Docker Compose](https://www.docker.com)
+ [Apache Maven](https://maven.apache.org)
+ [Project Lombok](https://projectlombok.org)
+ [Postman](https://www.postman.com)
+ [IntelliJ IDEA](https://www.jetbrains.com/ru-ru/idea/)
  
## База данных основного сервиса
<details>
  <summary>Диграмма</summary>

  ![image](https://github.com/AleXx313/java-explore-with-me/assets/120128332/bf738ef8-2203-47bd-ab5d-d86e4b866d36)

</details>


## Эндпоинты

Вся спецификация проекта содержится в файле - [спецификация](https://github.com/AleXx313/java-explore-with-me/blob/main/ewm-main-service-spec.json)

## Установка и запуск
1. Склонировать
```shell
git clone https://github.com/AleXx313/java-explore-with-me
```
2. Перейти в корень проекта через консоль
3. Собрать проект
```shell
mvn clean package
```
4. Запустить через Docker Compose
```shell
docker-compose up
```
---






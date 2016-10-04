# Список веб камер

На этом практическом занятии предлагается сделать небольшое приложение, которое ищет веб камеры по указанным координатам при помощи [Webcams.travel API](http://developers.webcams.travel/) и показывает список найденных камер. В качестве практического задания предлагается сделать:

* Выполнение HTTP запроса Webcams.travel API при помощи [HttpURLConnection](https://developer.android.com/reference/java/net/HttpURLConnection.html)
* Разбор ответа в формате JSON при помощи [JSONObject](https://developer.android.com/reference/org/json/JSONObject.html)
* Отображение результата в списке при помощи [RecyclerView](https://developer.android.com/reference/android/support/v7/widget/RecyclerView.html)
* Обработка ошибок выполнения запроса и ситуации отсутствия соединения

## Подготовка к заданию

Для того выполнения задания должны быть установлены:

* Android Studio, версия 2.2 или выше
* Android SDK
* Браузер Chrome (для отладки запросов)

В процессе работы с приложением потребуется постоянное соединение с интернетом для сборки (выкачивания зависимостей) и для работы самого приложения.

## Архитектура приложения

Приложение состоит из двух экранов:

* Экран ввода координат (стартовый)
* Экран со списком камер

Экран ввода координат уже релизован в классе SelectCoordsActivity, и с кодом этого экрана ничего делать не надо. Экран состоит из двух полей ввода для широты и долготы и кнопки, при нажатии на которую происходит переход на экран со списком камер.

Экран со списком камер нужно реализовать самим -- для него пока есть только заготовка в классе NearbyWebcamsActivity. Экран имеет три возможных состояния:

* Загрузка -- на экране показывается индикатор процесса загрузки, пока выполняется запрос
* Данные -- показываются загруженные данные (список камер с изображениями и заголовками)
* Ошибка -- отображается информация об ошибке (в том числе об остутствии интернета или о пустом результате)

## Webcams.travel API

[Webcams.travel](http://developers.webcams.travel/) -- это сервис, который предоставляет информацию о веб камерах по всему миру. У этого сервиса есть разные API методы для поиска камер, для нашего приложения нам понадобится один метод, который выдает список камер по географическим координатам и радиусу поиска: [http://developers.webcams.travel/#webcams/list/nearby](http://developers.webcams.travel/#webcams/list/nearby)

Доступ к API открыт всем разработчикам, но для его использования надо зарегистрироваться и получить API key. Процесс получения ключа выглядит так:

* Регистрируемся на https://market.mashape.com/webcams-travel (не забываем кликнуть на подтверждение регистрации в письме, которое придет на указанный e-mail)
* Создаем новое приложение с произвольным названием (например Webcams Demo)
* Получаем тестовый ключ созданного приложения, который потом нужно использовать в коде при выполнении запроса

Там же, на mashape.com есть ручка для Webcams.travel API, за которую можно подергать, чтобы посмотреть, как работают разные методы (впрочем, там довольно ограниченный функционал, не все параметры можно указывать): [https://market.mashape.com/webcams-travel/webcams-travel#webcams-list-nearby-lat-lng-radius](https://market.mashape.com/webcams-travel/webcams-travel#webcams-list-nearby-lat-lng-radius)

Если по каким-то причинам зарегистрироваться и получить API key не получается, можно попробовать использовать ключ, который прописан в коде задания в классе WebcamsApi, но его работоспособность не гарантируется. Лучше получить свой.

## Задание №1 - выполнение запроса

В методе `loadInBackground` класса `NearbyWebcamsLoader` уже заготовлен код для выполнения запроса -- его нужно доделать, чтобы он выполнял запрос nearby к Webcams.travel API. Описание запроса здесь: [http://developers.webcams.travel/#webcams/list/nearby](http://developers.webcams.travel/#webcams/list/nearby)

URL запроса должен выглядеть так:

```
https://webcamstravel.p.mashape.com/webcams/list/nearby={latitude},{longitude},{radius}?show=webcams:basic,image,location
```
А для авторизации приложения в API к запросу должен быть добавлен HTTP заголовок с ключом API:
```
X-Mashape-Key: <ключ API>
```

Задание:
* Написать код метода `WebcamsApi.createNearbyRequest`, который создает запрос с нужными URL и заголовками
* В методе `NearbyWebcamsLoader.loadInBackground` который проверяет результат выполнения запроса и вычитывает ответ (пока нет парсера ответа, можно просто вычитывать ответ вхолостую при помощи `IOUtils.readFully`

Для отладки запросов можно открыть URL [chrome://inspect](chrome://inspect) в браузере Chrome, там открыть раздел Devices, открыть работающее приложение и смотреть логи траифика во вкладке Network. 

## Задание №2 - JSON парсер 

Ответ на запрос nearby приходит в формате JSON, который содержит список найденных веб-камер. В рамках нашего приложения нас интересуют три поля у каждой веб-камеры: ID, заголовок и URL на статическое изображение. Нужно написать парсер, который на вхож принимает `InputStream` из ответа на запрос nearby, а в качестве результата возвращает список объектов типа `Webcam`.

Задание:
* Написать метод, который принимает в качестве параметра `InputStream` и возвращает `List<Webcam>`
* Использовать этот метод в `NearbyWebcamsLoader.loadInBackground` для получения результата запроса.
* Убедиться, что в метод `NearbyWebcamsActivity.onLoadFinished` приходит правильный результат выполнения запроса.

В парсере следует использовать класс [JSONObject](https://developer.android.com/reference/org/json/JSONObject.html)

## Задание №3 - отображение списка в RecyclerView

После выполнения первых двух заданий список вебкамер приходит в метод `NearbyWebcamsActivity.onLoadFinished` -- теперь остается только показать результат на экране при помощи `RecyclerView`. Верстка экрана уже заготовлена в `res/layout/nearby_webcams_activity.xml`: в ней есть `RecyclerView` и `ProgressBar`. Нужно написать код, который показывает индикатор процесса загрузки при помощи `ProgressBar` и список камер в `RecyclerView`.

Задание:
* Написать код, который при открытии экрана показывает `ProgressBar`, а после завершения загрузки его скрывает.
* Написать реализацию класса `RecyclerView.Adapter` для отображения списка веб-камер. В качестве верстки элемента списка использовать `res/layout/item_webcam.xml`
* Написать код в `NearbyWebcamsActivity.onLoadFinished`, который обрабатывет полученный результат

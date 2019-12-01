# Название проекта/команда
Команда называется "Балансер". 
Короткое описание проекта - в основе проекта лежит софтверный сервис, который выполняет роль балансировщика нагрузки, ограничителя нагрузки путем управления ссылками на плейлисты и ссылками и на воспроизведение.

# backend
## Описание
http://git2.urbantech.pro/denis/balancer_v1
- стек технологий - Nginx в качестве входного сервера, далее микросервисная архитектура бекенда, микросервисы написаны на Python, сопряжение с сервером через WSGI, сервисы мультипоточные, фреймвор  Python Flask.
- обоснование выбора стека - по итогам замеров описанных в статьях на Habr-е, связка Nginx + Unix socket + uWSGI + Python Flask дает одно из наиболее быстрых решений для серверной части и реализации REST API, при этом оно максимально  отвязано от железа и платформо независимое,  из чего вытекает его масштабируемость как техническая(софт можно просто скопировать и поставить на другие железки, не привязаны к железу), и как следствие бизнес-масштабируемость. Так же язык Python и микрофреймворк Flask позволяют очень гибко конфигурировать и развивать программное решение в сжатые сроки.
- описание компонентов и их взаимосвязей - Nginx принимает запросы на порт 80, далее настроены по location отправка upstream  на порты на которых слушают микросервисы отдачи плейлистов(Балансер), микросервис отдельный обслуживающий API запросы клиентского REST API. Микросервисы написаны на Python как описано выше. Балансер модифицирует и кеширует плейлисты источников, сохраняет и отдает их из кеша клиентам. Ссылки на HLS чанки модифицируются так чтоб они вели на кеш-сервера(в данном случае т.к. одна виртуалка - то на нее же), на кеш-сервере (фактически CDN-кеше) чанки кешируются и отдаются уже из кеша(с origin запрашивается только 1 экземпляр чанка)
- структурная схема - в репозитории файл main_architectural_scheme.png
- опционально, что-то важное, что вы хотите подсветить для нас - Преимущество нашего решения в отличие от текущих решений представленных на рынке, 
в том что  - наше решение маленькое и дешевое, за счет того что полностью софтверное и благодаря стеку технологий максимально  отвязано от железа и платформо независимое,  из чего вытекает его масштабируемость как техническая(софт можно просто скопировать и поставить на другие железки, текущие же решения привязаны к железу), и как следсвие бизнес-масштабируемость. При этом при дешевизне решения, обеспечивается одна из самых высоких производительностей за счет используемого стека технологий(и есть потенциал для роста). Так же оно гибкое за счет стека технологий, может быстро расширяться как вертикально(по нагрузке) так и горизонтально (в сторону увеличения количества услуги и интеграций с партнерами).
Мы предлагаем прототип отказоустойчивой, масштабируемой и недорогой софтверной стриминговой платформы «Балансер» для предоставления платного и бесплатного контента для массовых мероприятий, как для участников мероприятий, так и для зрителей в сети Интернет.
## Как тестировать
- в репозитории http://git2.urbantech.pro/denis/balancer_v1 файл  Balancer_test_YAML.yaml 


# frontend №1 - успели сделать 1 клиентское Андроид приложение. 
Для приложения так же успели сделать клиентское API для авторизации, проверки сессии, покупки платного контента, и проверки возможности или ограничения на открытие стриминговой сессии новому клиенту (если сервер близок к перегрузке или надо ограничивать число стримов или объем трафика на заданном уровне, то данное ограничение задается в балансере и он при достижении лимита трафика или стриминговых сессий, ставит новых пользователь в очередь ожидания - на клиенте отображается сообщение, успели это реализовать на клиенте, на сервере эмуляция перегрузки включается по ссылке http://46.61.193.144/access_queue_swith). API - https://docs.google.com/document/d/1fHfEv_UUgtFO7fW4M0qcr_JPrOpXTD4di7vmFpf_52E/edit?usp=sharing
Так же приложение умеет пинговать кеши (метод #1 в API выше), и если кеш доступен, то передает ID кеша в запросе на балансер - и тот вернет ссылки на кеш а не на origin, при этом переключение с кеша на origin  и обратно (кейс ушел/пришел на стадион) - бесшовное для клиента, ТВ поток не прерывается.

##Описание
http://git2.urbantech.pro/denis/live-event.git

- платформа/стек технологий/архитектура. Для тестирования нашего решения мы написали мобильное приложение для Android OS. Приложение написано на языке Kotlin с использованием библиотек RxKotlin, Retrofit, Dagger2, ExoPlayer и Architecture Components. Архитекура приложения - Clean Architecture с использованием MVVM паттерна на уровне представления.

##Как тестировать
ссылка на приложение для Android OS
http://git2.urbantech.pro/denis/live-event/blob/master/release_apk/app-release.apk

тестовый акканут
login: fdpd6
password: 123456

Можно запуститть приложение, залогиниться (при этом станут доступны ранее залоченные платные каналы), разлогиниться, смотреть каналы, посмотреть товары(доп услуги VAS)
Если включить режим эмуляции перегруженности сервиса(просто перейти по ссылке или GET на адрес http://46.61.193.144/access_queue_swith - меняет каждый раз статус вкл/выкл и обратно) - то будет отображаться сообщение что надо подождать 10 секунд в очереди. По сути все тест кейсы описаны в API https://docs.google.com/document/d/1fHfEv_UUgtFO7fW4M0qcr_JPrOpXTD4di7vmFpf_52E/edit?usp=sharing




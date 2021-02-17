Заглушка, имитирующая внешний сервис партнера, для тестирования Fintech вебхуков.

Описание endpoint'ов, доступных в тестовом приложении-заглушке для тестирования Fintech Webhooks, имитирующей АС партнера во внешнем интернете:
1) /fintech-webhook-dummy-stub/health-check - открываемая из браузера страница для проверки состояния приложения 
2) /fintech-webhook-dummy-stub/last-requests - открываемая из браузера страница для просмотра списка последних N принятых заглушкой запросов на рест POST /fintech-webhook-dummy-stub/test; отсортирован таким образом, что более свежие запросы отображаются вверху списка; по умолчанию отображаются последние 10 принятых заглушкой запросов
3) /fintech-webhook-dummy-stub/test - сама заглушка, имитирующая АС партнера во внешнем интернете, которая будет принимать отправляемые со стороны СББОЛ вебхуки; этот урл необходимо указывать при регистрации вебхуков через Fintech API; по умолчанию отвечает 200 OK в 80% случаев, иначе 500 INTERNAL SERVER ERROR
 
В настройках приложения-заглушки (т.е. в property-файле) есть следующие свойства, которые при необходимости можно изменить (после изменения свойств необходимо перезапустить сервер), обратившись к админам:

\# процент успешных ответов от заглушки (при вызове реста "POST /test")

success.response.percent=80

\# кол-во последних принятых запросов (на рест "POST /test"), которые будут отображаться при переходе на страницу /last-requests
last.request.cache.size=10

Деплой war на сервере приложений:
1) Распаковать дистрибутив fintech_webhook_dummy_stub.war на WebSphere
2) В файле fintech-webhook-dummy-stub.properties заполнить свойство logging.config, в котором указать полный путь до xml-файла настроек для библиотеки логирования logback, например logging.config=file:C:/WORK/fintech_webhook_dummy_stub_config/fintech-webhook-dummy-stub-logback-config.xml
3) В файле fintech-webhook-dummy-stub-logback-config.xml заполнить свойство "LOGS", в котором указать путь до папки, куда будут сохраняться логи, например <property name="LOGS" value="C:/WORK/fintech_webhook_dummy_stub_logz" />
4) Запустить деплой со следующими параметрами 
 Контекст /fintech-webhook-dummy-stub
-Dfintech.webhook.dummy.stub.config.file.path=<полный путь до property-файла>, например -Dfintech.webhook.dummy.stub.config.file.path="file:C:\WORK\fintech_webhook_dummy_stub_config\fintech-webhook-dummy-stub.properties"
5) Проверить доступность приложения можно по адресу http://<ip-address>:<port>/fintech-webhook-dummy-stub/health-check

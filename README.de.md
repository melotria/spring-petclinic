# Spring PetClinic Beispielanwendung [![Build Status](https://github.com/spring-projects/spring-petclinic/actions/workflows/maven-build.yml/badge.svg)](https://github.com/spring-projects/spring-petclinic/actions/workflows/maven-build.yml)[![Build Status](https://github.com/spring-projects/spring-petclinic/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/spring-projects/spring-petclinic/actions/workflows/gradle-build.yml)

[<img src="https://img.icons8.com/color/48/000000/great-britain-circular.png" width="20" height="20"/> English Version](README.md) ❤️

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/spring-projects/spring-petclinic) [![Open in GitHub Codespaces](https://github.com/codespaces/badge.svg)](https://github.com/codespaces/new?hide_repo_select=true&ref=main&repo=7517918)

## Die Spring Petclinic Anwendung mit einigen Diagrammen verstehen

[Hier geht es zur Präsentation](https://speakerdeck.com/michaelisvy/spring-petclinic-sample-application)

## Petclinic lokal ausführen

Spring Petclinic ist eine [Spring Boot](https://spring.io/guides/gs/spring-boot) Anwendung, die mit [Maven](https://spring.io/guides/gs/maven/) oder [Gradle](https://spring.io/guides/gs/gradle/) erstellt wurde. Sie können eine JAR-Datei erstellen und diese von der Kommandozeile aus ausführen (es sollte mit Java 17 oder neuer funktionieren):

```bash
git clone https://github.com/spring-projects/spring-petclinic.git
cd spring-petclinic
./mvnw package
java -jar target/*.jar
```

(Unter Windows oder wenn Ihre Shell den Glob nicht erweitert, müssen Sie möglicherweise den JAR-Dateinamen am Ende explizit in der Kommandozeile angeben.)

Sie können dann auf Petclinic unter <http://localhost:8080/> zugreifen.

<img width="1042" alt="petclinic-screenshot" src="https://cloud.githubusercontent.com/assets/838318/19727082/2aee6d6c-9b8e-11e6-81fe-e889a5ddfded.png">

Oder Sie können die Anwendung direkt über Maven mit dem Spring Boot Maven-Plugin ausführen. Wenn Sie dies tun, werden Änderungen, die Sie am Projekt vornehmen, sofort übernommen (Änderungen an Java-Quelldateien erfordern zusätzlich eine Kompilierung - die meisten Leute verwenden dafür eine IDE):

```bash
./mvnw spring-boot:run
```

> HINWEIS: Wenn Sie Gradle bevorzugen, können Sie die App mit `./gradlew build` erstellen und die JAR-Datei in `build/libs` finden.

## Einen Container erstellen

Es gibt keine `Dockerfile` in diesem Projekt. Sie können ein Container-Image erstellen (wenn Sie einen Docker-Daemon haben), indem Sie das Spring Boot Build-Plugin verwenden:

```bash
./mvnw spring-boot:build-image
```

## Falls Sie einen Fehler finden oder eine Verbesserung für Spring Petclinic vorschlagen möchten

Unser Issue-Tracker ist [hier](https://github.com/spring-projects/spring-petclinic/issues) verfügbar.

## Datenbankkonfiguration

In der Standardkonfiguration verwendet Petclinic eine In-Memory-Datenbank (H2), die beim Start mit Daten gefüllt wird. Die H2-Konsole ist unter `http://localhost:8080/h2-console` verfügbar, und es ist möglich, den Inhalt der Datenbank mit der URL `jdbc:h2:mem:<uuid>` zu überprüfen. Die UUID wird beim Start in der Konsole ausgegeben.

Ein ähnliches Setup ist für MySQL und PostgreSQL verfügbar, wenn eine persistente Datenbankkonfiguration benötigt wird. Beachten Sie, dass bei jeder Änderung des Datenbanktyps die App mit einem anderen Profil ausgeführt werden muss: `spring.profiles.active=mysql` für MySQL oder `spring.profiles.active=postgres` für PostgreSQL. Weitere Informationen zum Festlegen des aktiven Profils finden Sie in der [Spring Boot-Dokumentation](https://docs.spring.io/spring-boot/how-to/properties-and-configuration.html#howto.properties-and-configuration.set-active-spring-profiles).

Sie können MySQL oder PostgreSQL lokal mit einem für Ihr Betriebssystem geeigneten Installer starten oder Docker verwenden:

```bash
docker run -e MYSQL_USER=petclinic -e MYSQL_PASSWORD=petclinic -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=petclinic -p 3306:3306 mysql:9.1
```

oder

```bash
docker run -e POSTGRES_USER=petclinic -e POSTGRES_PASSWORD=petclinic -e POSTGRES_DB=petclinic -p 5432:5432 postgres:17.0
```

Weitere Dokumentation ist für [MySQL](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/resources/db/mysql/petclinic_db_setup_mysql.txt) und [PostgreSQL](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/resources/db/postgres/petclinic_db_setup_postgres.txt) verfügbar.

Anstelle von einfachem `docker` können Sie auch die bereitgestellte `docker-compose.yml`-Datei verwenden, um die Datenbank-Container zu starten. Jeder hat einen Service, der nach dem Spring-Profil benannt ist:

```bash
docker compose up mysql
```

oder

```bash
docker compose up postgres
```

## Testanwendungen

Während der Entwicklung empfehlen wir die Verwendung der Testanwendungen, die als `main()`-Methoden in `PetClinicIntegrationTests` (unter Verwendung der Standard-H2-Datenbank und zusätzlich Spring Boot Devtools), `MySqlTestApplication` und `PostgresIntegrationTests` eingerichtet sind. Diese sind so eingerichtet, dass Sie die Apps in Ihrer IDE ausführen können, um schnelles Feedback zu erhalten, und dieselben Klassen als Integrationstests gegen die jeweilige Datenbank ausführen können. Die MySQL-Integrationstests verwenden Testcontainers, um die Datenbank in einem Docker-Container zu starten, und die Postgres-Tests verwenden Docker Compose, um dasselbe zu tun.

## Kompilieren des CSS

Es gibt eine `petclinic.css` in `src/main/resources/static/resources/css`. Sie wurde aus der `petclinic.scss`-Quelle generiert, kombiniert mit der [Bootstrap](https://getbootstrap.com/)-Bibliothek. Wenn Sie Änderungen an der `scss` vornehmen oder Bootstrap aktualisieren, müssen Sie die CSS-Ressourcen mit dem Maven-Profil "css" neu kompilieren, d.h. `./mvnw package -P css`. Es gibt kein Build-Profil für Gradle zum Kompilieren des CSS.

## Mit Petclinic in Ihrer IDE arbeiten

### Voraussetzungen

Die folgenden Elemente sollten in Ihrem System installiert sein:

- Java 17 oder neuer (vollständiges JDK, kein JRE)
- [Git-Kommandozeilentool](https://help.github.com/articles/set-up-git)
- Ihre bevorzugte IDE
  - Eclipse mit dem m2e-Plugin. Hinweis: Wenn m2e verfügbar ist, gibt es ein m2-Symbol im Dialog `Hilfe -> Info`. Wenn m2e nicht vorhanden ist, folgen Sie dem Installationsprozess [hier](https://www.eclipse.org/m2e/)
  - [Spring Tools Suite](https://spring.io/tools) (STS)
  - [IntelliJ IDEA](https://www.jetbrains.com/idea/)
  - [VS Code](https://code.visualstudio.com)

### Schritte

1. Führen Sie in der Kommandozeile aus:

    ```bash
    git clone https://github.com/spring-projects/spring-petclinic.git
    ```

1. In Eclipse oder STS:

    Öffnen Sie das Projekt über `Datei -> Importieren -> Maven -> Bestehendes Maven-Projekt`, und wählen Sie dann das Stammverzeichnis des geklonten Repos aus.

    Erstellen Sie dann entweder in der Kommandozeile mit `./mvnw generate-resources` oder verwenden Sie den Eclipse-Launcher (Rechtsklick auf das Projekt und `Ausführen als -> Maven-Installation`), um das CSS zu generieren. Führen Sie die Hauptmethode der Anwendung aus, indem Sie mit der rechten Maustaste darauf klicken und `Ausführen als -> Java-Anwendung` wählen.

1. In IntelliJ IDEA:

    Wählen Sie im Hauptmenü `Datei -> Öffnen` und wählen Sie die Petclinic [pom.xml](pom.xml). Klicken Sie auf die Schaltfläche `Öffnen`.

    - CSS-Dateien werden aus dem Maven-Build generiert. Sie können sie in der Kommandozeile mit `./mvnw generate-resources` erstellen oder mit der rechten Maustaste auf das Projekt `spring-petclinic` klicken und dann `Maven -> Quellen generieren und Ordner aktualisieren` wählen.

    - Eine Laufkonfiguration mit dem Namen `PetClinicApplication` sollte für Sie erstellt worden sein, wenn Sie eine aktuelle Ultimate-Version verwenden. Andernfalls führen Sie die Anwendung aus, indem Sie mit der rechten Maustaste auf die Hauptklasse `PetClinicApplication` klicken und `Ausführen 'PetClinicApplication'` wählen.

1. Navigieren Sie zur Petclinic

    Besuchen Sie [http://localhost:8080](http://localhost:8080) in Ihrem Browser.

## Suchen Sie nach etwas Bestimmtem?

|Spring Boot Konfiguration | Klasse oder Java-Eigenschaftsdateien  |
|--------------------------|---|
|Die Hauptklasse | [PetClinicApplication](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java) |
|Eigenschaftsdateien | [application.properties](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/resources) |
|Caching | [CacheConfiguration](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/system/CacheConfiguration.java) |

## Interessante Spring Petclinic Branches und Forks

Der Spring Petclinic "main"-Branch in der [spring-projects](https://github.com/spring-projects/spring-petclinic) GitHub-Organisation ist die "kanonische" Implementierung, die auf Spring Boot und Thymeleaf basiert. Es gibt [ziemlich viele Forks](https://spring-petclinic.github.io/docs/forks.html) in der GitHub-Organisation [spring-petclinic](https://github.com/spring-petclinic). Wenn Sie daran interessiert sind, einen anderen Technologie-Stack zur Implementierung der Pet Clinic zu verwenden, treten Sie bitte der Community dort bei.

## Interaktion mit anderen Open-Source-Projekten

Einer der besten Teile der Arbeit an der Spring Petclinic-Anwendung ist, dass wir die Möglichkeit haben, in direktem Kontakt mit vielen Open-Source-Projekten zu arbeiten. Wir haben Fehler gefunden/Verbesserungen zu verschiedenen Themen wie Spring, Spring Data, Bean Validation und sogar Eclipse vorgeschlagen! In vielen Fällen wurden sie in nur wenigen Tagen behoben/implementiert.
Hier ist eine Liste von ihnen:

| Name | Issue |
|------|-------|
| Spring JDBC: Vereinfachung der Verwendung von NamedParameterJdbcTemplate | [SPR-10256](https://github.com/spring-projects/spring-framework/issues/14889) und [SPR-10257](https://github.com/spring-projects/spring-framework/issues/14890) |
| Bean Validation / Hibernate Validator: Vereinfachung der Maven-Abhängigkeiten und Abwärtskompatibilität |[HV-790](https://hibernate.atlassian.net/browse/HV-790) und [HV-792](https://hibernate.atlassian.net/browse/HV-792) |
| Spring Data: Mehr Flexibilität bei der Arbeit mit JPQL-Abfragen bieten | [DATAJPA-292](https://github.com/spring-projects/spring-data-jpa/issues/704) |

## Beitragen

Der [Issue-Tracker](https://github.com/spring-projects/spring-petclinic/issues) ist der bevorzugte Kanal für Fehlerberichte, Feature-Anfragen und das Einreichen von Pull-Requests.

Für Pull-Requests sind Editor-Präferenzen in der [Editor-Konfiguration](.editorconfig) für die einfache Verwendung in gängigen Texteditoren verfügbar. Lesen Sie mehr und laden Sie Plugins unter <https://editorconfig.org> herunter. Alle Commits müssen einen __Signed-off-by__-Trailer am Ende jeder Commit-Nachricht enthalten, um anzuzeigen, dass der Mitwirkende dem Developer Certificate of Origin zustimmt.
Weitere Details finden Sie im Blogbeitrag [Hello DCO, Goodbye CLA: Simplifying Contributions to Spring](https://spring.io/blog/2025/01/06/hello-dco-goodbye-cla-simplifying-contributions-to-spring).

## Lizenz

Die Spring PetClinic-Beispielanwendung wird unter Version 2.0 der [Apache-Lizenz](https://www.apache.org/licenses/LICENSE-2.0) veröffentlicht.

## 📄 `StateDataBaseConnectionLogger`

### 📌 Objetivo

A classe `StateDataBaseConnectionLogger` tem como objetivo **verificar se a conexão com o banco de dados foi estabelecida com sucesso** logo após a aplicação Spring Boot estar pronta para uso. Ela utiliza o `DataSource` configurado via `application.properties` para testar a conexão e loga o resultado no console.

Essa abordagem é útil em ambientes de desenvolvimento e produção para confirmar que as configurações do banco de dados estão corretas e que o banco está acessível no momento em que a aplicação é iniciada.

---

### 🧩 Pacotes e dependências utilizadas

#### 📦 Pacotes Java

- `javax.sql.DataSource`: Interface para obter conexões com o banco de dados.
- `java.sql.Connection`: Representa a conexão com o banco.
- `org.slf4j.Logger` e `org.slf4j.LoggerFactory`: Utilizados para registrar logs de sucesso ou falha.

#### 📦 Pacotes Spring

- `org.springframework.stereotype.Component`: Marca a classe como um componente Spring para ser gerenciada pelo contêiner.
- `org.springframework.context.event.EventListener`: Escuta eventos do ciclo de vida da aplicação.
- `org.springframework.boot.context.event.ApplicationReadyEvent`: Evento disparado quando a aplicação está pronta para uso.

#### 🧱 Dependências Maven

Incluídas no `pom.xml`:

- `spring-boot-starter-data-jpa`: Prove suporte a JPA e configuração automática do `DataSource`.
- `mysql-connector-j`: Driver JDBC para MySQL.
- `spring-boot-starter-web`: Permite o funcionamento da aplicação como uma API web.
- `spring-boot-starter-validation` e `spring-boot-starter-test` (para validação e testes, respectivamente).

---

### 🔧 Configurações do Banco de Dados

As propriedades definidas no arquivo `application.properties` são:

```properties
spring.datasource.url=jdbc:<URL_DATABASE>
spring.datasource.username=<NAME_DATABASE>
spring.datasource.password=<PASSWORD_DATABASE>
spring.datasource.driver-class-name=<DRIVER_DATABASE>

```

Essas configurações fornecem ao Spring Boot as informações necessárias para construir um `DataSource` automaticamente.

---

### 🧠 Funcionamento Interno da Classe

```java
@Component
public class StateDataBaseConnectionLogger {
    
    private static final Logger logger = LoggerFactory.getLogger(StateDataBaseConnectionLogger.class);
    private final DataSource dataSource;

    public StateDataBaseConnectionLogger(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void verifyDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            if (!connection.isClosed()) {
                logger.info("✅ Database connection, from datasource, successfully established!");
            }
        } catch (Exception e) {
            logger.error("❌ Failed to establish database connection from datasource.", e);
        }
    }
}
```

#### 🛠 Etapas do processo:

1. **Injeção de dependência**: O `DataSource` é automaticamente injetado no construtor pelo Spring Boot com base nas propriedades configuradas.

2. **Escuta do evento `ApplicationReadyEvent`**: A verificação só ocorre após a aplicação estar completamente inicializada.

3. **Teste de conexão**:
   - O método `dataSource.getConnection()` tenta abrir uma conexão com o banco.
   - Se a conexão for aberta com sucesso e não estiver fechada, um log positivo é exibido.
   - Se ocorrer qualquer exceção (ex: erro de autenticação, banco offline, etc.), ela será capturada e logada como erro.

---

### ✅ Vantagens

- Permite **monitoramento imediato** da conectividade com o banco logo no início da aplicação.
- Utiliza **recursos nativos do Spring Boot** sem a necessidade de bibliotecas externas.
- Ajuda a **diagnosticar problemas de conexão** rapidamente em ambientes locais ou em produção.

---

### 🧪 Possível extensão

Você pode evoluir essa lógica para:

- Enviar alertas via e-mail ou sistemas externos caso a conexão falhe.
- Integrar com soluções de health check.
- Realizar testes periódicos com agendamento (`@Scheduled`).

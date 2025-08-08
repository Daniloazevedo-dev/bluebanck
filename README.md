# bluebank-backend

### Configuração de Email SMPT

1. Ativar verificação em duas etapas na conta do Google.

2. Criar uma senha de app para o email utilizado no envio de emails.\
  Caminho: senhas -> verificação em duas etapas -> Senhas de app\
  Link para criar senha de app: https://myaccount.google.com/apppasswords

3. Colocar a senha de app lugar da senha do email.

### Rodar aplicação com docker 

1. Fazer build da aplicação gradle usando o comando ou via IDE:
   ```bash
   ./gradlew build -x test
   ```
   Observação: O comando `-x test` é usado para pular os testes durante o build, acelerando o processo. Pois a aplicação não possui implementação de testes unitários.


2. Certifique-se de que o Docker e o Docker Compose estão instalados e funcionando corretamente.
3. Abra um terminal e navegue até o diretório do projeto.
4. Execute o comando:
   ```bash
   docker compose  up -d --build
   ```
   Isso irá construir as imagens necessárias e iniciar os containers.


5. Acessar aplicação em http://localhost:8181/public/login.

### Conexão com MySQL (Container) no DBeaver

1.  **Nova Conexão:** No DBeaver, crie uma nova conexão e escolha o driver **MySQL**.

2.  **Configurações Principais:**
    * **Servidor:** `localhost` (para containers rodando na sua máquina).
    * **Porta:** `3306`.
    * **Banco de Dados:** `bluebank`.
    * **Usuário:** `bluebank`.
    * **Senha:** Sua senha.

3.  **Parâmetros da URL:** Certifique-se de que a URL de conexão inclui os seguintes parâmetros para ambientes de desenvolvimento:
    * `allowPublicKeyRetrieval=true`: Necessário para versões recentes do MySQL (8.0+) usarem o método de autenticação padrão.
    * `useSSL=false`: Desabilita a criptografia SSL, o que é seguro para conexões locais.

4.  **Finalizar:** Teste a conexão e clique em `OK` para salvar as configurações.

### Rodar aplicação localmente via IDE
1. Certifique-se de que o Java JDK 1.8 está instalado e configurado corretamente.
2. Abra o projeto na sua IDE preferida (IntelliJ IDEA, Eclipse, etc.).
3. Execute o comando de build:
   ```bash
   ./gradlew build -x test
   ```
4. Alerar arquivo `application.properties` para apontar para o profile de desenvolvimento:
   ```properties
   spring.profiles.active=dev
   ```
5. Execute a aplicação a partir da IDE.
6. Acesse a aplicação em http://localhost:8080/public/login.

Observação: Certifique-se de que o banco de dados MySQL está rodando e acessível na porta 3306 antes de iniciar a aplicação.
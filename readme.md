Para executar essa aplicação, é necessário seguir os seguintes passos:

Instalar o arquivo JVoIP.jar na aplicação utilizando o comando:
mvn install:install-file -Dfile=src/lib/JVoIP.jar -DgroupId=br.com.unipix -DartifactId=JVoip -Dversion=1.0 -Dpackaging=jar

Instalar os pacotes necessários para o funcionamento da aplicação utilizando o comando:
mvn clean install -DSkipTests

Executar a aplicação utilizando sua IDE de desenvolvimento ou através do comando:
mvn spring-boot:run

Esse processo garantirá o funcionamento correto da aplicação.
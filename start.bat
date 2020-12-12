echo off
echo "Realizando build do projeto ..."
call mvn clean install -DskipTests -B
echo "Build do projeto concluído!"
echo "Iniciando Docker"
call docker-compose up -d
echo "Docker iniciado"
echo "Start ..."
call mvn spring-boot:start -pl d-banco-conta,d-banco-transacao,d-banco-balanco
echo "Fim!"
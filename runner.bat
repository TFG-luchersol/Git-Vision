@echo off

:: Cambia al directorio del proyecto Node.js
cd ./frontend

:: Ejecuta npm start
start cmd /k "npm start"

cd ../

:: Iniciar SpringBoot en modo debug
mvn spring-boot:run







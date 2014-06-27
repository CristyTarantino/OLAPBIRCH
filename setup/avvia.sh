#!/bin/bash

#avvia il servizio di mysql
sudo /etc/init.d/mysql start

#avvio il file jar del gioco
java -jar OlapBirch.jar

read -p 'Press any key to continue...'
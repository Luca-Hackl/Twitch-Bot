# Twitch-Bot

## Table of contents
* [General info](#general-info)
* [Setup of Bot](#setup-of-bot)
* [Database](#database)

## General info
This is a TwitchBot, it's designed to automatically ban bots that are not activaly viewing your channel
but still are shown in the user list.

## Setup of Bot
Create a .env file wih a token, dburl and password for your database. The token can be fetched from the website
that is referenced in .env.example. Remember to set the scope of the token in such a way, that it can ban users.

## Database
I chose a mysql Database and named the table bot. If you mirror this setup you won't need to
change a lot in the code. 
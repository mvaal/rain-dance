# RainDance Discord Bot
RainDance is a Discord bot that forecasts the weather for the next hour at a set time interval.

This was primarily written to keep track of PoGo weather changes, but may have other uses.

## Setup
### Discord
##### Create bot
Create a RainDance app in your [Discord Apps][discord-apps].

##### Add bot to server
Client ID is located under APP DETAILS panel

    https://discordapp.com/oauth2/authorize?&client_id=<CLIENT ID>&scope=bot&permissions=0

### RainDanceBot
In main/resources add **application.conf**

    {
      "discord": {
        "token": <discord-bot-token>
      }
    }

### AccuWeather

In main/resources add **accuweather.conf**

    {
      "accuweather": {
        "apikey": <api-key>
      }
    }

### Running
From IDE, run: 

    AccuWeatherRainDanceBotRunner
    
## Known Issues/Missing Features
* App/Docker support to run outside IDE
* Forecast runs on the :45 and not on the hour
* State persistence after shutdown/restart
* Weather change alert when weather changes from a previous state
* Ability to make this a public bot
  * Probably not possible due to the fact that heavily used API keys are not free 


[discord-apps]: https://discordapp.com/developers/applications/me
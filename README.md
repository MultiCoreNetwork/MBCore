# MBCore
[![GitHub version](https://img.shields.io/badge/release-8.0.0-blue)](https://search.maven.org/search?q=mbcore)
[![GitHub stars](https://img.shields.io/github/stars/MultiCoreNetwork/MBCore)](https://github.com/MultiCoreNetwork/MBCore)
[![GitHub issues](https://img.shields.io/github/issues/MultiCoreNetwork/MBCore)](https://github.com/MultiCoreNetwork/MBCore/issues)

### Library for Spigot, BungeeCord and Velocity plugin development
This library contains a bunch of frequently used code snippets and classes that will
help you work faster without wasting your time with always the same codes on Spigot, BungeeCord and Velocity.

## Features
- [x] Chat Utilities
    - Messages color translation (hexadecimal support)
    - Useful methods to send one or more messages to a player or a sender
    - Useful methods to broadcast messages to all or a subset of players
    - Useful methods to log messages to console
    - Support for json messages (tellraw format)
- [x] Tab Completer Utilities
    - Simple completions for commands
- [x] Plugin Message Channel Wrapper
    - A wrapper for the plugin message channel to send packets to players and servers
    - Added more methods to the standards channel.
    - Event listeners for the plugin message channel 
- [x] Socket
    - A simple socket implementation to send messages between servers and other programs
- [x] GUI (Spigot)
    - A simple class to create and manage GUIs

## Requirements
- You need at least JDK8.

## Getting started
You can add the modules or just the one you need using maven:
```xml
<dependencies>
  <dependency>
    <groupId>it.multicoredev.mbcore.spigot</groupId>
    <artifactId>MBCore-spigot</artifactId>
    <version>...</version>
    <scope>compile</scope>
  </dependency>
  
  <dependency>
    <groupId>it.multicoredev.mbcore.bungeecord</groupId>
    <artifactId>MBCore-bungeecord</artifactId>
    <version>...</version>
    <scope>compile</scope>
  </dependency>
  
  <dependency>
    <groupId>it.multicoredev.mbcore.velocity</groupId>
    <artifactId>MBCore-velocity</artifactId>
    <version>...</version>
    <scope>compile</scope>
  </dependency>
</dependencies>
```

You can also download the latest version of the library (with also sources and javadocs) from [here](https://multicoredev.it/job/MBCore/) and import it as a library.

## Contributing
To contribute to this repository just fork this repository make your changes or add your code and make a pull request.

## License
MBCore is released under "The 3-Clause BSD License". You can find a copy [here](https://github.com/MultiCoreNetwork/MBCore/blob/master/LICENSE)
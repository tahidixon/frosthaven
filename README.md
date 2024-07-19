# Frosthaven Helper
***
Frosthaven Helper is an application that helps manage game actors the user is 
responsible for (e.g their current player character and/or any monsters currently 
within play) within a session of the board game [Frosthaven](https://boardgamegeek.com/boardgame/295770/frosthaven). 
It is a client side application that interacts with a server that is running its 
companion app, [Frosthaven-Companion](https://github.com/bandrewss/frosthaven-companion).

This application is a Kotlin Multiplatform project designed to be built for both Android
and iOS targets with minimal platform-specific code. The project is pulled together without
much regard to architecture and development best practices, and requires housekeeping.

***
### Roadmap
1. Fix bugs
2. Add error handling to actor creation
3. Add support for turn-based reminders (resolving special scenario rules, updating elemental state, etc.)
4. Add support for roles (Game state management, Element management, Monster management)
5. Make some UX decisions
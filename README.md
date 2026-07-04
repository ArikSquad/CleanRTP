<p align="center">
  <b><a>This project is based on the work of [RonanPlugins/BetterRTP](https://github.com/RonanPlugins/BetterRTP).</a></b>
</p>

## What has been changed?

- Now based on the Paper API
- The code style has been updated to match standards
- Removed integrations with lesser used plugins

This plugin has not been created to compete with any other plugins but rather to be for my own use. It has been 
open-sourced for transparency.

## Libraries
CleanRTP (BetterRTP) uses and is compiled with the following libraries:

- [ParticleLib](https://github.com/ByteZ1337/ParticleLib) (included) - Particles library by ByteZ1337. Find all supported particles [here](https://github.com/ByteZ1337/ParticleLib/blob/master/src/main/java/xyz/xenondevs/particle/ParticleEffect.java)

## Build instructions
[types](src/main/java/eu/mikart/cleanrtp/player/commands/types)
```bash
./gradlew build
```

The file will be in the build/libs folder.
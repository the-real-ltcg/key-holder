# Key Holder

A client-side Fabric mod for Minecraft 26.2 that lets you pick a key and holds it down for you, until you toggle it off.

## Usage

- `/keyholder set <key>` — pick the key to hold (e.g. `w`, `space`, `key.mouse.left`)
- `/keyholder on` / `off` / `toggle` / `status`
- Bind **Toggle Key Hold** in Controls for a one-press toggle (unbound by default)
- Or use the Mod Menu config screen to click-select the held key and flip it on/off

## Requirements

- Minecraft 26.2
- [Fabric Loader](https://fabricmc.net/) `>=0.18.4`
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Optional: [Mod Menu](https://modrinth.com/mod/modmenu) + [Cloth Config](https://modrinth.com/mod/cloth-config) for the GUI screen

## Building

Requires JDK 25:

```
JAVA_HOME="<path to JDK 25>" ./gradlew.bat build
```

The jar is output to `fabric-26.2/build/libs/`.

## License

MIT

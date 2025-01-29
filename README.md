# CWSkins
A Minecraft plugin that adds skins mechanic on you items!

> [!NOTE]
> This is my first plugin, so it may contain some bugs.
> If you encounter any problems, please create an issue.
> ColorTranslate [credits](https://gitlab.com/kody-simpson/spigot/1.16-color-translator).

## [Commands](src/main/java/me/skellf/cwskins/commands)

| Name             | Description                          | Permission           | Args                    |
|------------------|--------------------------------------|----------------------|-------------------------|
| `cwskins`        | Main command                         | none                 | Subcommands             |
| `cwskins create` | Creates a skin                       | `cwskins.createskin` | Skin name               |
| `cwskins give`   | Gives an existing skin               | `cwskins.giveskin`   | Player name, Skin name  |
| `cwskins clear`  | Gives a clear skin item to player    | `cwskins.clearskin`  | Player name             |
| `cwskins reload` | Reloads the plugin                   | `cwskins.reload`     | none                    |
| `cwskins menu`   | Opens a menu with all existing skins | `cwskins.menu`       | none                    |
| `cwskins remove` | Removes a skin                       | `cwskins.removeskin` | Skin name               |

To gain access to all commands, use `cwskins.*`

## Version
This plugin works on versions from `1.16` to `1.21` with Java 11 or above.
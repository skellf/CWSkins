# CWSkins
Minecraft plugin for simple skin support

> [!NOTE]
> It's my first plugin so it may have some bugs.
>  If you encountering any problem, please create an issue.
> ColorTranslate [credits](https://gitlab.com/kody-simpson/spigot/1.16-color-translator).

## [Commands](src/main/java/me/skellf/cwskins/commands)

| Name | Description | Permission | Args |
| ---- | --------------- | ---------- | ------- |
| `cwskins` | Main command | none | Subcommands |
| `cwskins create` | Creates a skin | `cwskins.createskin` | Skin name |
| `cwskins give` | Gives existing skin | `cwskins.giveskin` | Skin name |
| `cwskins clear` | Gives clear skin item to player | `cwskins.clearskin` | Player name |
| `cwskins reload` | Reloads the plugin | `cwskins.reload` | none |
| `cwskins menu` | Opens a menu with all existing skins | `cwskins.menu` | none |
| `cwskins remove` | Removes skin | `cwskins.removeskin` | Skin name |

To gain access to all commands use `cwskins.*`

## Version
This plugin should work fine on verisons from `1.16` to `1.21`

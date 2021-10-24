# PacketInventoryAPI
An Inventory API implementation based on the Minecraft packets system.

## For interested in this project
Currently this library actively developed only by me.
A public release and WiKi will be published when all basic features will be completed (see below).
Contribution doesn't make sense before public release, so just await :)

## Why? For what purpose?
1) That should be faster and more lightweight that Bukkit based Menu APIs.
2) That gives you more opportunities and customization features.
3) That gives you feature to use all available in-game containers, not a generic chest inventory only.
4) That more secure because all container content sent via packets is virtual, no dupes or something other.

## Supported NMS versions
| Game Version | NMS Version | Status |
|:---:|:---:|:---:|
| 1.13 - 1.13.1 | 1.13 R1 | Planned |
| 1.13.2 | 1.13 R2 | Planned |
| 1.14 - 1.14.4 | 1.14 R1 | Planned |
| 1.15 - 1.15.2 | 1.15 R1 | Supported |
| 1.16 - 1.16.1 | 1.16 R1 | Supported |
| 1.16.2 - 1.16.3 | 1.16 R2 | Supported |
| 1.16.4 - 1.16.5 | 1.16 R3 | Supported |
| 1.17 - 1.17.1 | 1.17 R1 | Supported |

## Incompleted features:
- Support for other NMS versions
- Bukkit ItemStack instances pre-convertion to NMS ItemStack instances
- ProtocolLib wrappers for vanilla recipes (e.g. for stonecutter)
- Stonecutter container recipes update request
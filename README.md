# PacketInventoryAPI
An Inventory API implementation based on the Minecraft packets system.

## Why? For what purpose?
1) That should be faster and more lightweight that Bukkit based Menu APIs.
2) That gives you more opportunities and customization features.
3) That gives you feature to use all available in-game containers, not a generic chest inventory only.
4) That more secure because all container content sent via packets is virtual, no dupes or something other.

## Incompleted features:
- NMS versions support:
  - 1.13.R1 (planned)
  - 1.13.R2 (planned)
  - 1.14.R1 (planned)
  - 1.15.R1 (supported)
  - 1.16.R1 (planned)
  - 1.16.R2 (planned)
  - 1.16.R3 (planned)
  - 1.17.R1 (supported)
- Bukkit ItemStack instances pre-convertion to NMS ItemStack instances
- ProtocolLib wrappers for vanilla recipes (e.g. for stonecutter)
- Stonecutter container recipes update request

Warp Book
=========

This niche mod allows for easy teleportation between given points without admin privileges. It offers a simple alternative to the standard /home and /warp commands.

This is a fork of the continuation done by [ferreusveritas](https://github.com/ferreusveritas/WarpBook). This version adds compatibility with Antique Atlas, a Spanish translation and fixes compilation issues due to HWYLA's repository being down.


### What's changed from the continuation

* Added Antique Atlas support. Now when creating a waypoint using an Unbound page or potion a marker will appear on every Atlas of your inventory.
* Translated to Spanish (`es_es`)
* Fixed error on compilation due to HWYLA's maven repository being down. To fix, HWYLA's api jar is now present on `/libs`.

### Compiling

1. Clone the repository or download it.
2. Run `./gradlew build`. Make sure you use Java 8.
3. After Gradle finishes, you can find the mod on `/build/libs`. Just install this as any other mod.
4. Enjoy!

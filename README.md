# LWJGL Engine - V2
OpenGL 3D engine using LWJGL (mostly GLFW and openGL java bindings). Purely for self teaching and interest.

Very good references for learning openGL and LWJGL:
  * [Oreon Engine] by Fynn Flügge - Massive help with java class structuring and learning about compute shaders
  * [Learn OpenGL] by Joey de Vries - Taught me openGL state machine and many of openGL's functions
  * [LWJGL Gitbook] by ahbejarano - Concise beginner tutorials with LWJGL

Implemented features:
  * Physically based rendering
      * Cook-Torrance BDRF
  * Fully deferred render pipeline
      * Screen quad
      * Support for post-processing effects
  * GLSL vertex, fragment, geometry, and compute shader creation
      * Attribute bindings
      * Uniform variables
      * Framebuffer bindings
  * Full 3D movement/rotation
     * WASD to traverse
     * Right mouse to rotate view
     * Middle mouse to pan along screen tangent
  
WIP features (in no particular order):
  * Proper lighting system
      * Light picking/moving
      * Runtime property changing
  * Uniform block bindings
  * Much better model loader
      * Normal/tangent/bitangent offline computation
      * ASSIMP support
      * Per face shading values
  * GUI
      * Buttons
      * Static and dynamic text
      * Billboards
      * Cursor object picking
      * Object movement/rotation/scaling control
  * Skybox/skysphere
      * Image based rendering (reflections will take color values from skymap)
      * HDR Maps
  * Post-processing
      * Screen space ambient occlusion + blur
      * Bloom
      * Motion blur (just a little bit)
      * Screen space reflections
  * Smooth camera
  
Future features:
  * Runtime material editing
  * Water/Ocean generation
  * Terrain generation

[Oreon Engine]: https://github.com/oreonengine/oreon-engine/tree/2fa7843fd09898723833d0d998e56f62ff537775
[Learn OpenGL]: https://learnopengl.com/Introduction
[LWJGL Gitbook]: https://github.com/lwjglgamedev/lwjglbook

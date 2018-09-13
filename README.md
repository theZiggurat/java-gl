# LWJGL Engine - V2
OpenGL 3D engine using LWJGL (mostly GLFW and openGL java bindings). Purely for self teaching and interest.

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
  
WIP features:
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
  * Screen space ambient occlusion
  * Smooth camera
  
Future features:
  * Screen space reflections
  * Runtime material editing


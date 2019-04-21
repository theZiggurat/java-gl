# LWJGL Engine - V2

![](https://media.giphy.com/media/61Y9uDxnLjrJGmKcZx/giphy.gif)

OpenGL 3D engine using LWJGL (specifically OpenGL, GLFW, Assimp). 
Created out of great interest for high fidelity real-time renderers such as Unreal Engine 4 and Blender Eevee

___Demos___:
   * [Febuary 2019]
   * [September 2018]

___Implemented features___:
  * Physically based rendering (metalness workflow)
      * Cook-Torrance BRDF
  * Fully deferred render pipeline
      * Position, normal, albedo, roughness, and metal attatchments
  * Shadow mapping (sun only)
  * ASSIMP model loading
  * Advanced post-processing
      * SSAO: Screen space ambient occlusion
      * SSR: Screen space reflections
  * Full 3D movement/rotation
     * WASD to traverse
     * Right mouse to rotate view
     * Middle mouse to pan along screen tangent
     * Q to zoom
  * Dynamic window resizing
     * Auto resizing FBO attachments and textures
  * GUI (early stages)
     * Movable/Resizable viewports
     * Basic buttons
     * Event system
  * Cursor object picking
     * Unique identifier map for pixel perfect picking
  
___WIP features___:
  * More advanced shadow mapping
      * Improve on sun shadow mapping
          * Parallel split shadow maps
          * Ortho-frustum fitting
      * Perspective shadow maps for point and spot lights
  * Uniform block bindings
  * Proper serialization
      * Load engine behavior instead of hard-coding it
  * GUI
      * Buttons, sliders and fields
      * Static and dynamic text
      * Billboards
      * Cursor object picking
           * UUID picking
           * Object outline
      * Object property control
      * Engine console
  * Image based lighting
  * Height mapping
      * Parralax occlusion mapping
  * Post-processing
      * Bloom
      * Depth of field
      * HDR tone mapping
      * FXAA (fast approximate anti-aliasing)
  
[September 2018]: https://www.youtube.com/watch?v=jU0Dm78wGI0&feature=youtu.be
[Febuary 2019]: https://www.youtube.com/watch?v=-DDIFM3aZWM&feature=youtu.be

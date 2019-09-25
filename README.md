# java-gl - OpenGL Engine

![](https://media.giphy.com/media/61Y9uDxnLjrJGmKcZx/giphy.gif)

OpenGL 3D engine using LWJGL (specifically OpenGL, GLFW, Assimp, STBI). 
Created out of interest for high fidelity real-time renderers such as Unreal Engine 4 and Blender Eevee

___Demos___:
   * [September 2018]
   * [Febuary 2019]
   * [September 2019]

___Implemented features___:
  * Physically based rendering (metalness workflow)
      * Cook-Torrance BDRF
  * Shadow mapping (sun only)
  * ASSIMP model loading
  * Fully deferred render pipeline
      * Position, normal, albedo, roughness, and metal attatchments
  * GLSL vertex, fragment, geometry, and compute shader creation
      * Attribute bindings
      * Uniform variables
      * Framebuffer bindings
  * Full 3D movement/rotation
     * WASD to traverse
     * Right mouse to rotate view
     * Middle mouse to pan along screen tangent
     * Q to zoom
  * Dynamic window resizing
     * Auto resizing FBO attachments and textures
  
___WIP features___:
  * More advanced shadow mapping
      * Parallel split shadow maps
           * Ortho-frustum fitting
      * Perspective cube maps (for point light shadows)
  * Uniform block bindings
  * Multisampling
  * Tone mapping
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
      * Screen space ambient occlusion + blur
      * Bloom
      * Depth of field
      * Screen space reflections
  
___Future features___:
  * Modular shader creation
  * Runtime material editing
  * Water/Ocean generation
  * Terrain generation
  
[September 2018]: https://www.youtube.com/watch?v=jU0Dm78wGI0&feature=youtu.be
[Febuary 2019]: https://www.youtube.com/watch?v=-DDIFM3aZWM&feature=youtu.be
[September 2019]: https://www.youtube.com/watch?v=Yf84O_FdyaQ
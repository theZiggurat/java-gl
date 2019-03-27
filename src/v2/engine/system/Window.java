package v2.engine.system;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import v2.engine.utils.ImageLoader;

import java.nio.ByteBuffer;
import java.text.DecimalFormat;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    @Getter private int height, width;
    @Getter private long handle;
    @Getter @Setter private boolean resized = false;
    @Getter private String title;
    private String spec_title;

    private static Window instance;

    public static Window instance(){
        if(instance == null){
            instance = new Window();
        }
        return instance;
    }

    public Window(){
        this.title = Config.instance().getWindowName();
        this.height = Config.instance().getWindowHeight();
        this.width = Config.instance().getWindowWidth();
        spec_title = title;
    }

    /**
     * Makes necessary GLFW calls to instantiate a system window
     * with openGL context.
     * @throws IllegalStateException Unable to initialize GLFW
     * @throws RuntimeException Failed to create GLFW window
     */
    public void init(){

        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwWindowHint(GLFW_VISIBLE, GL_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        handle = glfwCreateWindow(width, height, title, 0, NULL);
        if(handle == NULL){
            throw new RuntimeException("Failed to create GLFW window");
        }

        glfwSetFramebufferSizeCallback(handle, (window, width, height) ->{
            this.width = width;
            this.height = height;
            resized = true;
        });

        glfwSetKeyCallback(handle, (window, key, scancode, action, mods) ->{
            if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                close();
            }
        });

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(handle,
                (vidmode.width() - width)/2,
                (vidmode.height() - height)/2);



        glfwMakeContextCurrent(handle);
        GLCapabilities glCapabilities = GL.createCapabilities();
        glfwShowWindow(handle);

        glClearColor(0,0,0, 1f);
//        glFrontFace(GL_CCW);

       glEnable(GL_DEPTH_TEST);
       glEnable(GL_CULL_FACE);
       glCullFace(GL_BACK);
       glPolygonMode(GL_FRONT_FACE, GL_FILL);

       glLineWidth(1);

       setIcon("res/images/icon.png");
    }

    public void update(){
        glfwSwapBuffers(handle);
        glfwPollEvents();
        glfwSetWindowTitle(handle, spec_title);
    }

    /**
     *  Window poll for Core.loop()
      */
    public boolean shouldClose(){
        return glfwWindowShouldClose(handle);
    }

    public void close(){
        glfwSetWindowShouldClose(getHandle(), true);
    }

    /**
     * 32 x 32 window icon
     * @param path image path with format "res/image/*"
     */
    public void setIcon(String path){
        GLFWImage.Buffer images = GLFWImage.malloc(1);
        ByteBuffer buffer = ImageLoader.loadImage(path);

        GLFWImage icon = GLFWImage.malloc();
        icon.set(32,32,buffer);

        images.put(0,icon);
        glfwSetWindowIcon(handle, images);
    }

    /**
     * Updates FPS field in the window banner
     * @param FPS current frames per second
     */
    public void setTitle(double FPS, Camera camera){
        spec_title = title.concat(" | " +
                new DecimalFormat("#.00000").format(1/FPS) + " S | " +
                new DecimalFormat("#.0").format(FPS) + " FPS | " +
                "Camera: " + camera.getTranslation().toString(new DecimalFormat("#.0")) +
                " | Looking: " + camera.getForward().toString(new DecimalFormat("0.000")) +
                " | FOV: " + camera.getFOV());
    }

}

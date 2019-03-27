package v1.engine.util;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private int height, width;
    private long windowHandle;
    private boolean resized = false, vsync;
    private String title;

    boolean line = false;

    public Window(String title, int width, int height, boolean vsync){
        this.title = title;
        this.height = height;
        this.width = width;
        this.vsync = vsync;
    }

    public void init(){
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwWindowHint(GLFW_VISIBLE, GL_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        windowHandle = glfwCreateWindow(width, height, title, 0, NULL);
        if(windowHandle == NULL){
            throw new RuntimeException("Failed to create GLFW window");
        }

        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) ->{
           this.width = width;
           this.height = height;
           this.setResized(true);
        });

        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) ->{
           if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
               glfwSetWindowShouldClose(window, true);
           }
        });

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos( windowHandle,
                        (vidmode.width() - width)/2,
                        (vidmode.height() - height)/2);

        glfwMakeContextCurrent(windowHandle);

        if(isVsync()){
            glfwSwapInterval(1);
        }

        glfwShowWindow(windowHandle);
        GL.createCapabilities();
        glClearColor(.4f, .4f, .4f, 1f);

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        glLineWidth(1);
    }

    public void update(){
        if(line){glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );}
        else {glPolygonMode(GL_FRONT_FACE, GL_FILL);}
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }

    public void setClearColor(float r, float g, float b, float alpha){

        glClearColor(r, g, b, alpha);
    }

    //
    public boolean isKeyPressed(int keyCode){

        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
    }

    // Window poll for Core.loop()
    public boolean shouldClose(){

        return glfwWindowShouldClose(windowHandle);
    }

    // Getters & Setters

    public String getTitle(){ return title; }
    public int getWidth(){return width; }
    public int getHeight(){ return height; }
    public boolean isResized(){ return resized; }
    public void setResized(boolean resized){ this.resized = resized; }
    public boolean isVsync(){return vsync;}
    public void setVsync(boolean vsync){this.vsync = vsync;}
    public long getHandle(){ return windowHandle;}
    public boolean isLine() { return line; }
    public void setLine(boolean line) { this.line = line; }
}

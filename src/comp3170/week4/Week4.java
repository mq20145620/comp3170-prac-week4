package comp3170.week4;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.joml.Matrix4d;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

import comp3170.GLException;
import comp3170.InputManager;
import comp3170.Shader;

public class Week4 extends JFrame implements GLEventListener {

	private final float TAU = (float) Math.PI * 2; 

	private GLCanvas canvas;
	private Shader shader;
	
	final private File DIRECTORY = new File("src/comp3170/week4"); 
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private InputManager input;
	private Animator animator; 
	private long oldTime;	
	
	private Matrix4f viewMatrix;
	
	private float viewWidth = 20;
	private float viewHeight = 20;

	private Snake snake;


	public Week4() {
		super("COMP3170 Week 4");
		
		// create an OpenGL 4 canvas and add this as a listener
		
		GLProfile profile = GLProfile.get(GLProfile.GL4);		 
		GLCapabilities capabilities = new GLCapabilities(profile);
		this.canvas = new GLCanvas(capabilities);
		this.canvas.addGLEventListener(this);
		this.add(canvas);
		
		// create an input manager to listen for keypresses and mouse events
		
		this.input = new InputManager();
		this.addKeyListener(this.input);
		this.canvas.addKeyListener(this.input);

		// set up Animator
		
		this.animator = new Animator(canvas);
		this.animator.start();
		this.oldTime = System.currentTimeMillis();
		
		// set up the JFrame		
		
		this.setSize(1000,1000);
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});	
		
	}

	@Override
	/**
	 * Initialise the GLCanvas
	 */
	public void init(GLAutoDrawable drawable) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		// Compile the shader
		try {
			File vertexShader = new File(DIRECTORY, VERTEX_SHADER);
			File fragementShader = new File(DIRECTORY, FRAGMENT_SHADER);
			this.shader = new Shader(vertexShader, fragementShader);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (GLException e) {
			e.printStackTrace();
			System.exit(1);
		}
				
		// allocate matrices
		this.viewMatrix = new Matrix4f();
		
		// create a flower
		this.currentFlower = new Flower(shader, 6);
		this.flowers.add(this.currentFlower);
		
	}

	private void updateScene() {
		if (input.isMouseDown()) {
			
			if (currentFlower == null) {
				// TODO: create a new flower at the mouse position 
			}			
		}
		else {
			currentFlower = null;
		}
	}
	
	@Override
	/**
	 * Called when the canvas is redrawn
	 */
	public void display(GLAutoDrawable drawable) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		updateScene();
	
		// set the background colour 
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);	// white
		gl.glClear(GL_COLOR_BUFFER_BIT);		
		
		this.shader.enable();
		
		// set the view to by viewWidth x viewHeight

		this.viewMatrix.scaling(2.0f / this.viewWidth, 2.0f / this.viewHeight, 1);
		this.shader.setUniform("u_viewMatrix", this.viewMatrix);
		
		// draw the flowers
		
		this.snake.draw(this.shader);
		
	}

	@Override
	/**
	 * Called when the canvas is resized
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		// TODO: Recalculate the view dimensions to keep the correct aspect ratio
		
	}

	@Override
	/**
	 * Called when we dispose of the canvas 
	 */
	public void dispose(GLAutoDrawable drawable) {
		// nothing to do
	}

	public static void main(String[] args) throws IOException, GLException {
		new Week4();
	}


}

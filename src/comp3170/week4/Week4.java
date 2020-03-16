package comp3170.week4;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.joml.Vector4f;

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
	
	private Matrix4f viewMatrix;
	private Matrix4f inverseViewMatrix;

	private float viewWidth = 20;
	private float viewHeight = 20;

	private List<Flower> flowers;
	private Flower currentFlower = null;


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
		this.addMouseListener(input);
		this.addMouseMotionListener(input);
		canvas.addKeyListener(this.input);
		canvas.addMouseListener(input);
		canvas.addMouseMotionListener(input);

		// set up Animator
		
		this.animator = new Animator(canvas);
		this.animator.start();
		
		// set up the JFrame		
		
		this.setSize(1000,1000);
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});	
		
		// initialise flowers
		
		this.flowers = new ArrayList<Flower>();
		
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
		this.inverseViewMatrix = new Matrix4f();
		
	}
	
	private Vector4f position = new Vector4f();
	private Vector4f dragPosition = new Vector4f();

	private void updateScene() {
		if (input.isMouseDown()) {
			
			if (currentFlower == null) {
				input.getMousePosition(position);

				// convert to NDC
				position.mul(2.0f / this.canvas.getWidth(), 2.0f / this.canvas.getHeight(), 1, 1);
				position.add(-1f, -1f, 0, 0);
				position.y = -position.y;
				
				// convert to world
				this.viewMatrix.invert(this.inverseViewMatrix);
				position.mul(this.inverseViewMatrix);
				System.out.println(String.format("pos = (%1f, %1f)",position.x, position.y));
				
				
				// plant a new flower at the cursor
				this.currentFlower = new Flower(this.shader, 6);
				this.flowers.add(currentFlower);
				this.currentFlower.setPosition(position);

				
				// add a random angle and scale
				
				float angle = ((float)Math.random() * 2 -1) * TAU / 12;
				float scale = 1 + ((float)Math.random() * 2 -1) * 0.2f;
				this.currentFlower.setAngle(angle);
				this.currentFlower.setScale(scale);
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
		gl.glClearColor(87f / 255, 60f/255f, 23f/255, 1.0f);	
		gl.glClear(GL_COLOR_BUFFER_BIT);		
		
		this.shader.enable();
		
		// set the view to by viewWidth x viewHeight

		this.viewMatrix.scaling(2.0f / this.viewWidth, 2.0f / this.viewHeight, 1);
		this.shader.setUniform("u_viewMatrix", this.viewMatrix);
		
		// draw the snake 
		
		for (Flower flower : this.flowers) {
			flower.draw(this.shader);			
		}
		
	}

	@Override
	/**
	 * Called when the canvas is resized
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		float aspect = (float)width / height;
		
		this.viewWidth = aspect * this.viewHeight;
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

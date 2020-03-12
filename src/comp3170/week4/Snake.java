package comp3170.week4;

import org.joml.Matrix4d;
import org.joml.Vector2d;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.InputManager;
import comp3170.Shader;

public class Snake {

	private final float HEAD_WIDTH = 1;
	private final float HEAD_LENGTH = 1.5f;
	
	// Head is a square made from two trianlges:
	//
	//                
	//     (0, 0.5) +-----+ (1,0.5)
	//              |    /| 
	//        (0,0) +  /  |  front 
	//              |/    |
	//   (0, -0.5)  +-----+ (1.-0.5)
	//                    
	//
	// The pivot point (0,0) is at the back of the head
	//
	
	
	private float[] vertices = new float[] {
			 0.0f,	 0.5f,
			 0.0f,	-0.5f,
			 1.0f,	 0.5f,

			 1.0f,	-0.5f,
			 1.0f,   0.5f,
			 0.0f,  -0.5f
	};
;
	private int vertexBuffer;

	private float[] colour = {0.0f, 1.0f, 0.0f}; // green 
	
	private Matrix4d matrix;
	
	private final float SPEED = 1.0f;	// m per sec
	private final float TURN_SPEED = (float) (Math.PI * 2.0f / 6.0f);	// radians per sec

	private Vector2d position = new Vector2d(0,0);
	private float heading = 0;
		
	public Snake(Shader shader) {
	    this.vertexBuffer = shader.createBuffer(this.vertices);
	    this.matrix = new Matrix4d();
		this.matrix.identity();
		
	}

	public void update(float dt, InputManager input) {
		// TODO: move the snake
	}
	
	public void draw(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		// TODO: construct the matrix in TRaSHeS order
		this.matrix.identity();
		
		shader.setUniform("u_worldMatrix", this.matrix);
		
		shader.setAttribute("a_position", this.vertexBuffer);	    
		shader.setUniform("u_colour", this.colour);	    
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.vertices.length / 2);           	
	
	}
	
}

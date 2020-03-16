package comp3170.week4;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;

public class Flower {

	private final float TAU = (float)Math.PI * 2;
	
	private final float WIDTH = 0.1f;
	private final float HEIGHT = 1f;
	
	private final float INNER_RADIUS = 0.2f;
	private final float OUTER_RADIUS = 0.3f;	
	
	// Stem is a rectangle made from two triangles:
	//
	//                
	//   (-0.5 , 1) +-----+ (0.5,1)
	//              |    /| 
	//              |  /  |  
	//              |/    |
	//    (-0.5, 0) +--+--+ (0.5. 0)
	//               (0,0)  
	//
	// The pivot point (0,0) is at the bottom of the stem
	//
	
	
	private float[] stemVertices = new float[] {
			-0.5f * WIDTH,	 1.0f * HEIGHT,
			-0.5f * WIDTH,	 0.0f * HEIGHT,
			 0.5f * WIDTH,	 1.0f * HEIGHT,

			 0.5f * WIDTH,	 0.0f * HEIGHT,
			 0.5f * WIDTH,   1.0f * HEIGHT,
			-0.5f * WIDTH,   0.0f * HEIGHT
	};

	private int stemVertexBuffer;
	private float[] stemColour = {0.0f, 1.0f, 0.0f}; // green 

	// the head of the flower
	
	private float[] headVertices;
	private int headVertexBuffer;
	private float[] headColour = {1.0f, 1.0f, 0};
		
	// the transformation matrix for this flower
	
	private Matrix4f matrix;	
	private Vector4f position = new Vector4f(0,0,0,1);
	private float angle = 0;
	private float scale = 1;
	
	/**
	 * Construct a flower with the given number of petals
	 * @param shader
	 * @param nPetals
	 */
	
	public Flower(Shader shader, int nPetals) {
	    this.stemVertexBuffer = shader.createBuffer(this.stemVertices);
	    
	    this.headVertices = makeFlower(nPetals);
	    this.headVertexBuffer = shader.createBuffer(this.headVertices);	    
	    
	    // allocate the martix
	    this.matrix = new Matrix4f();
	}
	
	private float[] makeFlower(int nPetals) {
		
		float[] vertices = {};
		
		// TODO
		
		// Construct the flower head with the given number of petals.
		
		return vertices;		
	}
	
	public void setPosition(Vector4f position) {
		position.get(this.position);
	}
	
	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	

	public void draw(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		// TODO: set matrix in TRaSheS order
		this.matrix.identity();
		
		// draw the stem
		
		shader.setUniform("u_worldMatrix", this.matrix);
		shader.setAttribute("a_position", this.stemVertexBuffer);	    
		shader.setUniform("u_colour", this.stemColour);	    
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.stemVertices.length / 2);
        
		// TODO: draw the head at position (0,HEIGHT) in the local coordinate space

	}
	
}

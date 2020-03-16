package comp3170.week4;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;

public class Flower {
	
	private final float TAU = (float)Math.PI * 2;
	
	private final float MAX_ANGLE = TAU / 12;
	private final float SWAY_SPEED = TAU / 12; // radians per second

	
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

	private float[] headVertices;
	private int headVertexBuffer;
	private float[] headColourIn = {1.0f, 1.0f, 0};
	private float[] headColourOut = {1.0f, 0.0f, 0};
		
	private Matrix4f matrix;
	
	private Vector4f position = new Vector4f(0,0,0,1);
	private float angle = 0;
	private float scale = 1;
	
	private float sway = 1.0f;
	
	public Flower(Shader shader, int nPetals) {
	    this.stemVertexBuffer = shader.createBuffer(this.stemVertices);
	    
	    this.headVertices = makeFlower(nPetals);
	    this.headVertexBuffer = shader.createBuffer(this.headVertices);	    
	    
	    this.matrix = new Matrix4f();
	}
	
	private float[] makeFlower(int nPetals) {
		
		float[] vertices = new float[6 * nPetals * 2];

		int j = 0;
		for (int i = 0; i < nPetals; i++) {
			
			float a0 = i * TAU / nPetals;
			float a1 = (i + 0.5f) * TAU / nPetals;
			float a2 = (i + 1) * TAU / nPetals;
			
			float x0 = OUTER_RADIUS * (float)Math.cos(a0);
			float y0 = OUTER_RADIUS * (float)Math.sin(a0);

			float x1 = INNER_RADIUS * (float)Math.cos(a1);
			float y1 = INNER_RADIUS * (float)Math.sin(a1);

			float x2 = OUTER_RADIUS * (float)Math.cos(a2);
			float y2 = OUTER_RADIUS * (float)Math.sin(a2);

			vertices[j++] = 0;
			vertices[j++] = 0;

			vertices[j++] = x0;
			vertices[j++] = y0;

			vertices[j++] = x1;
			vertices[j++] = y1;
			
			vertices[j++] = 0;
			vertices[j++] = 0;

			vertices[j++] = x1;
			vertices[j++] = y1;

			vertices[j++] = x2;
			vertices[j++] = y2;
		}
		
		
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

		// set matrix in TRaSheS order
		this.matrix.identity();
		this.matrix.translate(this.position.x, this.position.y, 0);
		this.matrix.rotateZ(this.angle);
		this.matrix.scale(this.scale);
		
		// draw the stem
		
		shader.setUniform("u_worldMatrix", this.matrix);
		shader.setAttribute("a_position", this.stemVertexBuffer);	    
		shader.setUniform("u_innerColour", this.stemColour);	    
		shader.setUniform("u_outerColour", this.stemColour);	    
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.stemVertices.length / 2);
        
        this.matrix.translate(0, this.HEIGHT, 0);

		// draw the head

        shader.setUniform("u_worldMatrix", this.matrix);
		shader.setAttribute("a_position", this.headVertexBuffer);	    
		shader.setUniform("u_innerColour", this.headColourIn);	    
		shader.setUniform("u_outerColour", this.headColourOut);	    
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.headVertices.length / 2);

	}

	public void update(float dt) {
		float da = sway * SWAY_SPEED * dt;
		if (Math.abs(this.angle + da) > MAX_ANGLE) {
			sway = -sway;
			da = -da;
		}
		
		this.angle += da;
	}
	
}

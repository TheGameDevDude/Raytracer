package objects;

import math.Vector3f;

public class Camera {
	// the position of the camera
	public Vector3f position;
	// the direction in which the camera is pointing 
	public Vector3f direction;
	// pointing right from the camera perpendicular to down and direction vector
	public Vector3f right;
	// pointing down from the camera perpendicular to right and direction vector
	public Vector3f down;
	
	public Camera(Vector3f position, Vector3f lookUp, Vector3f right, Vector3f down) {
		this.position = position;
		this.right = right;
		this.down = down;
		
		// calculating the direction vector which is pointing at the lookUp point
		this.direction = new Vector3f(lookUp.x - position.x, lookUp.y - position.y, lookUp.z - position.z);
		direction.normalize();
	}
}

package objects;

import math.Vector3f;

public class Camera {
	// the position of the camera
	public Vector3f position;
	// the direction in which the camera is pointing 
	public Vector3f direction;
	// pointing right from the camera perpendicular to down and direction vector
	public Vector3f right;
	// pointing up from the camera perpendicular to right and direction vector
	public Vector3f up;
	// field of view
	public double FOV;
	
	public Camera(Vector3f position, Vector3f lookUp, double FOV) {
		this.position = position;
		
		// calculating the direction vector which is pointing at the lookUp point
		this.direction = new Vector3f(lookUp.x - position.x, lookUp.y - position.y, lookUp.z - position.z).normalize();
		
		// calculating right direction vector which is cross product of Y - axis and direction vector
		right = new Vector3f().cross(new Vector3f(0, 1, 0), direction).normalize();
		
		// calculating up direction vector which is cross product of right direction vector and the direction of camera
		up = new Vector3f().cross(direction, right).normalize();
		
		this.FOV = FOV;
	}
}

package it.uniroma1.di.simulejos.opengl;

import javax.media.opengl.GL2GL3;

public class ArrayBuffer extends Buffer {
	public ArrayBuffer(GL2GL3 gl) {
		super(gl, Target.ARRAY);
	}
}

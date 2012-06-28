package it.uniroma1.di.simulejos.opengl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL2GL3;

public class Buffer extends GLObject {
	private static int createBuffer(GL2GL3 gl) {
		final int[] result = new int[1];
		gl.glGenBuffers(1, result, 0);
		return result[0];
	}

	public static enum Target {
		ARRAY {
			@Override
			int getGLTarget() {
				return GL2GL3.GL_ARRAY_BUFFER;
			}
		},
		ELEMENT_ARRAY {
			@Override
			int getGLTarget() {
				return GL2GL3.GL_ELEMENT_ARRAY_BUFFER;
			}
		},
		PIXEL_PACK {
			@Override
			int getGLTarget() {
				return GL2GL3.GL_PIXEL_PACK_BUFFER;
			}
		},
		PIXEL_UNPACK {
			@Override
			int getGLTarget() {
				return GL2GL3.GL_PIXEL_UNPACK_BUFFER;
			}
		};
		abstract int getGLTarget();
	}

	private final int target;

	public Buffer(GL2GL3 gl, Target target) {
		super(gl, createBuffer(gl));
		this.target = target.getGLTarget();
	}

	public void bind() {
		gl.glBindBuffer(target, id);
	}

	public void unbind() {
		gl.glBindBuffer(target, 0);
	}

	public void data(int size, java.nio.Buffer data, int usage) {
		gl.glBufferData(target, size, data, usage);
	}

	public void data(byte[] data, int usage) {
		gl.glBufferData(target, data.length, ByteBuffer.wrap(data), usage);
	}

	public void data(int[] data, int usage) {
		gl.glBufferData(target, data.length, IntBuffer.wrap(data), usage);
	}

	public void data(float[] data, int usage) {
		gl.glBufferData(target, data.length, FloatBuffer.wrap(data), usage);
	}

	public void data(double[] data, int usage) {
		gl.glBufferData(target, data.length, DoubleBuffer.wrap(data), usage);
	}

	public void delete() {
		final int[] ids = { id };
		gl.glDeleteBuffers(1, ids, 0);
	}
}

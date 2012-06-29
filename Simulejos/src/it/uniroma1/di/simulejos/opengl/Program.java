package it.uniroma1.di.simulejos.opengl;

import java.io.IOException;

import javax.media.opengl.GL2GL3;

public class Program extends GLObject {
	public Program(GL2GL3 gl) {
		super(gl, gl.glCreateProgram());
	}

	public Program(GL2GL3 gl, VertexShader vertexShader,
			FragmentShader fragmentShader) {
		super(gl, gl.glCreateProgram());
		attachShader(vertexShader);
		attachShader(fragmentShader);
	}

	private static VertexShader createVertexShader(GL2GL3 gl, Class<?> c,
			String name) {
		try {
			return new VertexShader(gl, c, name);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static FragmentShader createFragmentShader(GL2GL3 gl, Class<?> c,
			String name) {
		try {
			return new FragmentShader(gl, c, name);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Program(GL2GL3 gl, Class<?> c, String name) {
		this(gl, createVertexShader(gl, c, name), createFragmentShader(gl, c,
				name));
	}

	private int get(int name) {
		final int[] result = { 1234 };
		gl.glGetProgramiv(id, name, result, 0);
		return result[0];
	}

	public void use() {
		gl.glUseProgram(id);
	}

	public void attachShader(Shader shader) {
		gl.glAttachShader(id, shader.id);
	}

	public void detachShader(Shader shader) {
		gl.glDetachShader(id, shader.id);
	}

	public Shader[] getAttachedShaders() {
		final int count = get(GL2GL3.GL_ATTACHED_SHADERS);
		final int[] ids = new int[count];
		gl.glGetAttachedShaders(id, count, null, 0, ids, 0);
		final Shader[] shaders = new Shader[count];
		for (int i = 0; i < ids.length; i++) {
			shaders[i] = Shader.getById(gl, ids[i]);
		}
		return shaders;
	}

	public void link() {
		gl.glLinkProgram(id);
	}

	public boolean isLinked() {
		return get(GL2GL3.GL_LINK_STATUS) != GL2GL3.GL_FALSE;
	}

	public String getInfoLog() {
		final int length = get(GL2GL3.GL_INFO_LOG_LENGTH);
		final byte[] log = new byte[length];
		gl.glGetProgramInfoLog(id, length, null, 0, log, 0);
		return new String(log);
	}

	public boolean validate() {
		gl.glValidateProgram(id);
		return get(GL2GL3.GL_VALIDATE_STATUS) != GL2GL3.GL_FALSE;
	}

	public void delete() {
		gl.glDeleteProgram(id);
	}

	public boolean isDeleted() {
		return get(GL2GL3.GL_DELETE_STATUS) != GL2GL3.GL_FALSE;
	}
}

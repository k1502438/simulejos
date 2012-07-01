package it.uniroma1.di.simulejos;

import java.awt.Frame;
import java.io.File;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.DebugGL2GL3;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

public final class Simulation implements Serializable {
	private static final long serialVersionUID = -290517947218502549L;

	private final Floor floor = new Floor();
	private final List<Robot> robots = new LinkedList<>();

	private transient volatile Frame parentWindow;
	private transient volatile PrintWriter logWriter;
	private transient volatile boolean dirty;

	private static volatile boolean debugMode;

	public static void setDebugMode(boolean debug) {
		Simulation.debugMode = debug;
	}

	private static GL2GL3 getGL(GLAutoDrawable drawable) {
		final GL2GL3 gl = drawable.getGL().getGL2GL3();
		if (debugMode) {
			return new DebugGL2GL3(gl);
		} else {
			return gl;
		}
	}

	private transient volatile GLAutoDrawable canvas;
	private transient final GLEventListener glEventListener = new GLEventListener() {
		@Override
		public void init(GLAutoDrawable drawable) {
			final GL2GL3 gl = getGL(drawable);
			floor.setGL(gl);
		}

		@Override
		public void reshape(GLAutoDrawable drawable, int x, int y, int width,
				int height) {
			// TODO Auto-generated method stub
		}

		@Override
		public void display(GLAutoDrawable drawable) {
			final GL2GL3 gl = getGL(drawable);
			gl.glClear(GL2GL3.GL_COLOR_BUFFER_BIT | GL2GL3.GL_DEPTH_BUFFER_BIT);
			floor.draw(gl);
			for (Robot robot : robots) {
				// TODO
			}
			gl.glFlush();
		}

		@Override
		public void dispose(GLAutoDrawable drawable) {
			// TODO Auto-generated method stub
		}
	};

	public Simulation(Frame parentWindow, Writer logWriter) {
		this.parentWindow = parentWindow;
		this.logWriter = new PrintWriter(new PartialWriter("Simulation",
				logWriter));
	}

	public boolean isDirty() {
		return dirty;
	}

	public void clearDirty() {
		dirty = false;
	}

	public void setCanvas(GLAutoDrawable canvas) {
		canvas.addGLEventListener(glEventListener);
	}

	public void discard() {
		if (canvas != null) {
			canvas.removeGLEventListener(glEventListener);
			canvas = null;
		}
	}

	public void addRobot(File classPath, String mainClassName, String script) {
		dirty = true;
		// FIXME load wavefront model, create and add robot
	}

	interface State {
		State play();

		State suspend();

		State stop();
	};

	private final State runningState = new State() {
		@Override
		public State play() {
			return this;
		}

		@Override
		public State suspend() {
			for (Robot robot : robots) {
				robot.suspend();
			}
			logWriter.println("suspended");
			return suspendedState;
		}

		@Override
		public State stop() {
			for (Robot robot : robots) {
				robot.stop();
			}
			logWriter.println("stopped");
			return stoppedState;
		}
	};

	private final State suspendedState = new State() {
		@Override
		public State play() {
			logWriter.println("resumed");
			for (Robot robot : robots) {
				robot.resume();
			}
			return runningState;
		}

		@Override
		public State suspend() {
			return this;
		}

		@Override
		public State stop() {
			for (Robot robot : robots) {
				robot.stop();
			}
			logWriter.println("stopped");
			return stoppedState;
		}
	};

	private final State stoppedState = new State() {
		@Override
		public State play() {
			logWriter.println("started");
			for (Robot robot : robots) {
				robot.play();
			}
			return runningState;
		}

		@Override
		public State suspend() {
			return this;
		}

		@Override
		public State stop() {
			return this;
		}
	};

	private State state = stoppedState;

	public void play() {
		state = state.play();
	}

	public void suspend() {
		state = state.suspend();
	}

	public void stop() {
		state = state.stop();
	}
}

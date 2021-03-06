package it.uniroma1.di.simulejos;

import java.io.IOException;
import java.io.Writer;

public class PartialWriter extends Writer {
	private final String name;
	private final Writer sink;
	private final StringBuffer buffer = new StringBuffer();

	public PartialWriter(String name, Writer sink) {
		this.name = name;
		this.sink = sink;
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		for (int i = off; i < off + len; i++) {
			buffer.append(cbuf[i]);
			if (cbuf[i] == '\n') {
				sink.append(name + "> " + buffer);
				buffer.delete(0, buffer.length());
			}
		}
	}

	@Override
	public void flush() throws IOException {
		sink.append(name + "> " + buffer);
		buffer.delete(0, -1);
	}

	@Override
	public void close() {
	}
}

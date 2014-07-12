package ie.ianduffy.todo.web.filter.gzip;

import javax.servlet.ServletOutputStream;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.OutputStream;

class GZipServletOutputStream extends ServletOutputStream {
    private final OutputStream stream;

    public GZipServletOutputStream(OutputStream output) {
        super();
        this.stream = output;
    }

    @Override
    public void close() throws IOException {
        this.stream.close();
    }

    @Override
    public void flush() throws IOException {
        this.stream.flush();
    }

    @Override
    public void write(@NotNull byte b[]) throws IOException {
        this.stream.write(b);
    }

    @Override
    public void write(@NotNull byte b[], int off, int len) throws IOException {
        this.stream.write(b, off, len);
    }

    @Override
    public void write(int b) throws IOException {
        this.stream.write(b);
    }
}

package org.saartako.client.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Utility class for http requests of multipart form data
 *
 * @param bodyPublisher the body publisher
 * @param contentType   the content type
 */
public record MultipartFormData(HttpRequest.BodyPublisher bodyPublisher, String contentType) {

    private static MultipartFormData create(String boundary, byte[] body) {
        return new MultipartFormData(
            HttpRequest.BodyPublishers.ofByteArray(body),
            "multipart/form-data; boundary=" + boundary);
    }

    public static MultipartFormData.Builder builder() {
        return new MultipartFormData.Builder();
    }

    public static class Builder {
        private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

        private record MultipartStringRecord(String name,
                                             String contentType,
                                             String content) implements MultipartRecord {
            @Override
            public String contentDisposition() {
                return "name=\"" + this.name() + "\"\r\n";
            }

            @Override
            public void write(ByteArrayOutputStream outputStream) throws IOException {
                outputStream.write(this.content.getBytes(DEFAULT_CHARSET));
            }
        }

        private record MultipartBytesRecord(String name,
                                            String contentType,
                                            byte[] content) implements MultipartRecord {
            @Override
            public String contentDisposition() {
                return "name=\"" + this.name() + "\"\r\n";
            }

            @Override
            public void write(ByteArrayOutputStream outputStream) throws IOException {
                outputStream.write(this.content);
            }
        }

        private record MultipartFileRecord(String name,
                                           String contentType,
                                           File content) implements MultipartRecord {
            @Override
            public String contentDisposition() {
                return "name=\"" + this.name() + "\"; filename=\"" + this.content().getName() + "\"\r\n";
            }

            @Override
            public void write(ByteArrayOutputStream outputStream) throws IOException {
                Files.copy(this.content.toPath(), outputStream);
            }

        }

        private interface MultipartRecord {
            String contentDisposition();

            String contentType();

            void write(ByteArrayOutputStream outputStream) throws IOException;
        }

        private final List<MultipartRecord> parts = new ArrayList<>();

        private Builder withPart(final MultipartRecord part) {
            this.parts.add(part);
            return this;
        }

        public Builder stringPart(String name, String contentType, String content) {
            return withPart(new MultipartStringRecord(name, contentType, content));
        }

        public Builder bytesPart(String name, String contentType, byte[] bytes) {
            return withPart(new MultipartBytesRecord(name, contentType, bytes));
        }

        public Builder filePart(String name, String contentType, File file) {
            return withPart(new MultipartFileRecord(name, contentType, file));
        }

        public MultipartFormData build() throws IOException {
            final String boundary = UUID.randomUUID().toString().replaceAll("-", "");
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            for (MultipartRecord record : this.parts) {
                final String header =
                    "--" + boundary + "\r\n" +
                    "Content-Disposition: form-data; " + record.contentDisposition() +
                    "Content-Type: " + record.contentType() + "\r\n\r\n";

                outputStream.write(header.getBytes(DEFAULT_CHARSET));

                record.write(outputStream);

                outputStream.write("\r\n".getBytes(DEFAULT_CHARSET));
            }
            outputStream.write(("--" + boundary + "--\r\n").getBytes(DEFAULT_CHARSET));

            final byte[] byteArray = outputStream.toByteArray();
            return MultipartFormData.create(boundary, byteArray);
        }
    }
}
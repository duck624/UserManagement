package vn.ducjava.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class ResponseSuccess extends ResponseEntity<ResponseSuccess.Payload> {

    // DELETE, PATCH, PUT
    public ResponseSuccess(HttpStatusCode status, String message) {
        super(new Payload(status.value(), message),HttpStatus.OK);
    }
    // POST, GET
    public ResponseSuccess(HttpStatusCode status, String message, Object data) {
        super(new Payload(status.value(), message, data), HttpStatus.OK);
    }

    public static class Payload {
        private final int status;
        private final String message;
        private Object data; // Object là kiểu cha toàn bộ, dùng để hứng mọi kiểu dữ liệu trả về

        public Payload(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public Payload(int status, String message, Object data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }

        public int getStatus() {
            return status;
        }

        public Object getData() {
            return data;
        }

        public String getMessage() {
            return message;
        }
    }
}

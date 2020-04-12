package io.codedivine.pmtool.payload;

public class JWTLoginSucessResponse { //ni treba response ako imame validuser za redux da go zadrzi tokeno i da praka requet
    private boolean  success;
    private String token;

    public JWTLoginSucessResponse(boolean success, String token) {
        this.success = success;
        this.token = token;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "JWTLoginSucessResponse{" +
                "success=" + success +
                ", token='" + token + '\'' +
                '}';
    }
}

package com.one97.vodaubona.beans;

public class IVRResponse {
    private String responseCode;
    private Category categories;

    public Category getCategories() {
        return categories;
    }

    public void setCategories(Category categories) {
        this.categories = categories;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IVRResponse [responseCode=");
        builder.append(responseCode);
        builder.append(", categories=");
        builder.append(categories);
        builder.append("]");
        return builder.toString();
    }

}

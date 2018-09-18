package com.one97.vodaubona.beans;

import org.apache.commons.lang.StringUtils;

public class CircleLang {
    private String circleCode;
    private String circleName;
    private String ubonaCircleName;
    private String language;

    public CircleLang() {

    }

    public CircleLang(String circleCode, String circleName, String ubonaCircleName, String language) {
        super();
        this.circleCode = circleCode;
        this.circleName = StringUtils.upperCase(circleName);
        this.ubonaCircleName = StringUtils.upperCase(ubonaCircleName);
        this.language = StringUtils.upperCase(language);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CircleLang [circleCode=");
        builder.append(circleCode);
        builder.append(", circleName=");
        builder.append(circleName);
        builder.append(", ubonaCircleName=");
        builder.append(ubonaCircleName);
        builder.append(", language=");
        builder.append(language);
        builder.append("]");
        return builder.toString();
    }

    public String getCircleCode() {
        return circleCode;
    }

    public void setCircleCode(String circleCode) {
        this.circleCode = circleCode;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = StringUtils.upperCase(circleName);
    }

    public String getUbonaCircleName() {
        return ubonaCircleName;
    }

    public void setUbonaCircleName(String ubonaCircleName) {
        this.ubonaCircleName = StringUtils.upperCase(ubonaCircleName);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = StringUtils.upperCase(language);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((circleCode == null) ? 0 : circleCode.hashCode());
        result = prime * result + ((circleName == null) ? 0 : circleName.hashCode());
        result = prime * result + ((language == null) ? 0 : language.hashCode());
        result = prime * result + ((ubonaCircleName == null) ? 0 : ubonaCircleName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CircleLang other = (CircleLang) obj;
        if (circleCode == null) {
            if (other.circleCode != null)
                return false;
        }
        else if (!circleCode.equals(other.circleCode))
            return false;
        if (circleName == null) {
            if (other.circleName != null)
                return false;
        }
        else if (!circleName.equals(other.circleName))
            return false;
        if (language == null) {
            if (other.language != null)
                return false;
        }
        else if (!language.equals(other.language))
            return false;
        if (ubonaCircleName == null) {
            if (other.ubonaCircleName != null)
                return false;
        }
        else if (!ubonaCircleName.equals(other.ubonaCircleName))
            return false;
        return true;
    }

}

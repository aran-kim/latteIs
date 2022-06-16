package site.LatteIs.latteIs.oauth.provider;

import java.util.Map;

// OAuth2.0 제공자들 마다 응답해주는 속성값이 달라서 공통으로 제작
public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    public Map<String, Object> getAttributes(){
        return attributes;
    }
    public abstract String getProviderId();
    public abstract String getProvider();
    public abstract String getEmail();
    public abstract String getName();
}

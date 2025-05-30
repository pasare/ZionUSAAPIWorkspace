package org.zionusa.admin.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.zionusa.base.domain.Group;
import org.zionusa.base.domain.Member;

import javax.servlet.http.HttpServletRequest;

import static org.zionusa.base.util.auth.SecurityConstants.AUTHORIZATION;
import static org.zionusa.base.util.auth.SecurityConstants.X_APPLICATION_ID;

@Service
public class RestService {

    @Value("${zionusa.management.url}")
    private String managementUrl;

    public Group[] getChurchGroups(HttpServletRequest request, Integer churchId) {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION, token);
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            String url = managementUrl + "/churches/" + churchId + "/groups/display";
            HttpEntity<Group[]> groupResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Group[].class);
            if (groupResponseEntity.hasBody()) {
                return groupResponseEntity.getBody();
            }
        }

        return null;
    }

    public Member[] getAllDisplayChurchMembers(HttpServletRequest request, Integer churchId) {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION, token);
            headers.set(X_APPLICATION_ID, request.getHeader(X_APPLICATION_ID));
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            String url = managementUrl + "/users/church/" + churchId + "/display/";
            HttpEntity<Member[]> memberResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Member[].class);
            if (memberResponseEntity.hasBody()) {
                return memberResponseEntity.getBody();
            }
        }
        return new Member[]{};
    }

    public Member[] getAllDisplayGroupMembers(HttpServletRequest request, Integer churchId) {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION, token);
            headers.set(X_APPLICATION_ID, request.getHeader(X_APPLICATION_ID));
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            String url = managementUrl + "/users/group/" + churchId + "/display/";
            HttpEntity<Member[]> memberResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Member[].class);
            if (memberResponseEntity.hasBody()) {
                return memberResponseEntity.getBody();
            }
        }
        return new Member[]{};
    }

    public Member[] getAllDisplayTeamMembers(HttpServletRequest request, Integer churchId) {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION, token);
            headers.set(X_APPLICATION_ID, request.getHeader(X_APPLICATION_ID));
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            String url = managementUrl + "/users/team/" + churchId + "/display/";
            HttpEntity<Member[]> memberResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Member[].class);
            if (memberResponseEntity.hasBody()) {
                return memberResponseEntity.getBody();
            }
        }
        return new Member[]{};
    }

    public Member[] getAllDisplayMembers(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION, token);
            headers.set(X_APPLICATION_ID, request.getHeader(X_APPLICATION_ID));
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            String url = managementUrl + "/users/display/";
            HttpEntity<Member[]> memberResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Member[].class);
            if (memberResponseEntity.hasBody()) {
                return memberResponseEntity.getBody();
            }
        }
        return new Member[]{};
    }

    public Member getDisplayMember(HttpServletRequest request, Integer userId) {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION, token);
            headers.set(X_APPLICATION_ID, request.getHeader(X_APPLICATION_ID));
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            String url = managementUrl + "/users/" + userId + "/display/";
            HttpEntity<Member> memberResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Member.class);
            if (memberResponseEntity.hasBody()) {
                return memberResponseEntity.getBody();
            }
        }
        return null;
    }

    public Member[] getGroupMembers(HttpServletRequest request, Integer groupId) {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION, token);
            headers.set(X_APPLICATION_ID, request.getHeader(X_APPLICATION_ID));
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            String url = managementUrl + "/users/group/" + groupId;
            HttpEntity<Member[]> memberResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Member[].class);
            if (memberResponseEntity.hasBody()) {
                return memberResponseEntity.getBody();
            }
        }
        return new Member[]{};
    }
}

package org.zionusa.biblestudy.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.zionusa.base.domain.Association;
import org.zionusa.base.domain.Church;
import org.zionusa.base.domain.Member;

import javax.servlet.http.HttpServletRequest;

import static org.zionusa.base.util.auth.SecurityConstants.*;

@Service
public class RestService {

    @Value("${zionusa.management.url}")
    private String managementUrl;

    @Cacheable(value = "enabled-association-cache")
    public Association[] getEnabledAssociations(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION, token);
            headers.set(X_APPLICATION_ID, request.getHeader(X_APPLICATION_ID));
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            String url = managementUrl + "/associations/enabled";
            HttpEntity<Association[]> memberResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Association[].class);
            if (memberResponseEntity.hasBody()) {
                return memberResponseEntity.getBody();
            }
        }
        return new Association[]{};
    }

    @Cacheable(value = "display-churches-cache")
    public Church[] getDisplayChurches(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION, token);
            headers.set(X_APPLICATION_ID, request.getHeader(X_APPLICATION_ID));
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            String url = managementUrl + "/churches/display";
            HttpEntity<Church[]> memberResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Church[].class);
            if (memberResponseEntity.hasBody()) {
                return memberResponseEntity.getBody();
            }
        }
        return new Church[]{};
    }

    @Cacheable(value = "display-association-members-cache", key = "#associationId.toString()")
    public Member[] getAllDisplayAssociationMembers(HttpServletRequest request, Integer associationId) {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION, token);
            headers.set(X_APPLICATION_ID, request.getHeader(X_APPLICATION_ID));
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            String url = managementUrl + "/users/display/?associationId=" + associationId;
            HttpEntity<Member[]> memberResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Member[].class);
            if (memberResponseEntity.hasBody()) {
                return memberResponseEntity.getBody();
            }
        }
        return new Member[]{};
    }

    @Cacheable(value = "display-church-members-cache", key = "#churchId.toString()")
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

    @Cacheable(value = "display-region-members-cache", key = "#regionId.toString()")
    public Member[] getAllDisplayRegionMembers(HttpServletRequest request, Integer churchId) {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION, token);
            headers.set(X_APPLICATION_ID, request.getHeader(X_APPLICATION_ID));
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            String url = managementUrl + "/users/region/" + churchId + "/display/";
            HttpEntity<Member[]> memberResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Member[].class);
            if (memberResponseEntity.hasBody()) {
                return memberResponseEntity.getBody();
            }
        }
        return new Member[]{};
    }

    @Cacheable(value = "display-group-members-cache", key = "#groupId.toString()")
    public Member[] getAllDisplayGroupMembers(HttpServletRequest request, Integer groupId) {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION, token);
            headers.set(X_APPLICATION_ID, request.getHeader(X_APPLICATION_ID));
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            String url = managementUrl + "/users/group/" + groupId + "/display/";
            HttpEntity<Member[]> memberResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Member[].class);
            if (memberResponseEntity.hasBody()) {
                return memberResponseEntity.getBody();
            }
        }
        return new Member[]{};
    }

    @Cacheable(value = "display-team-members-cache", key = "#teamId.toString()")
    public Member[] getAllDisplayTeamMembers(HttpServletRequest request, Integer teamId) {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION, token);
            headers.set(X_APPLICATION_ID, request.getHeader(X_APPLICATION_ID));
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            String url = managementUrl + "/users/team/" + teamId + "/display/";
            HttpEntity<Member[]> memberResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Member[].class);
            if (memberResponseEntity.hasBody()) {
                return memberResponseEntity.getBody();
            }
        }
        return new Member[]{};
    }

    public String getCountActiveUsers (HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION, token);
            headers.set(X_APPLICATION_ID, request.getHeader(X_APPLICATION_ID));
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            String url = managementUrl + "/users/count/active";
            HttpEntity<String> activeUsersCountEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            if (activeUsersCountEntity.hasBody()) {
                return activeUsersCountEntity.getBody();
            }
        }
        return null;
    }


    @Cacheable(value = "display-all-members-cache")
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

    @Cacheable(value = "display-church-teachers-cache", key = "#churchId.toString()")
    public Member[] getAllDisplayChurchTeachers(HttpServletRequest request, Integer churchId) {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION, token);
            headers.set(X_APPLICATION_ID, request.getHeader(X_APPLICATION_ID));
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            String url = managementUrl + "/users/teachers/church/" + churchId + "/display/";
            HttpEntity<Member[]> memberResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Member[].class);
            if (memberResponseEntity.hasBody()) {
                return memberResponseEntity.getBody();
            }
        }
        return new Member[]{};
    }

    @Cacheable(value = "display-group-teachers-cache", key = "#groupId.toString()")
    public Member[] getAllDisplayGroupTeachers(HttpServletRequest request, Integer groupId) {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION, token);
            headers.set(X_APPLICATION_ID, request.getHeader(X_APPLICATION_ID));
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            String url = managementUrl + "/users/teachers/group/" + groupId + "/display/";
            HttpEntity<Member[]> memberResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Member[].class);
            if (memberResponseEntity.hasBody()) {
                return memberResponseEntity.getBody();
            }
        }
        return new Member[]{};
    }

    @Cacheable(value = "display-team-teachers-cache", key = "#teamId.toString()")
    public Member[] getAllDisplayTeamTeachers(HttpServletRequest request, Integer teamId) {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION, token);
            headers.set(X_APPLICATION_ID, request.getHeader(X_APPLICATION_ID));
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            String url = managementUrl + "/users/teachers/team/" + teamId + "/display/";
            HttpEntity<Member[]> memberResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Member[].class);
            if (memberResponseEntity.hasBody()) {
                return memberResponseEntity.getBody();
            }
        }
        return new Member[]{};
    }

    @Cacheable(value = "display-all-teachers-cache")
    public Member[] getAllDisplayTeachers(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION, token);
            headers.set(X_APPLICATION_ID, request.getHeader(X_APPLICATION_ID));
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            String url = managementUrl + "/users/teachers/display/";
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


    @CacheEvict(
            cacheNames = {
                    "display-association-members-cache",
                    "display-churches-cache",
                    "display-church-members-cache",
                    "display-group-members-cache",
                    "display-team-members-cache",
                    "display-all-members-cache",
                    "display-church-teachers-cache",
                    "display-group-teachers-cache",
                    "display-team-teachers-cache",
                    "display-all-teachers-cache",
                    "enabled-association-cache"
            },
            allEntries = true
    )
    public void evictAllCache() {
    }
}

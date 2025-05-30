package org.zionusa.base.util.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationAndAuthenticationListener {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationAndAuthenticationListener.class);

    @EventListener
    public void auditEvent(AuditApplicationEvent auditApplicationEvent) {
        AuditEvent auditEvent = auditApplicationEvent.getAuditEvent();

        WebAuthenticationDetails details = (WebAuthenticationDetails) auditEvent.getData().get("details");

        log.info(" Principal " + auditEvent.getPrincipal() + " - " + auditEvent.getType());
        if (details != null) {
            log.info("Remote IP address: " + details.getRemoteAddress() + "  Request URL: " + auditEvent.getData().get("requestUrl"));
        }
    }
}

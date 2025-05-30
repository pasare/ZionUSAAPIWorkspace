package org.zionusa.base.util.audit;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

@NoArgsConstructor
public class UsernameAuditorAware implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && !authentication.getPrincipal().equals("anonymousUser"))
			return Optional.ofNullable(((User) authentication.getPrincipal()).getUsername());

		return Optional.empty();
	}
}

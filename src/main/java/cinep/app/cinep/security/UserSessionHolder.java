package cinep.app.cinep.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserSessionHolder {

    public static CinepUser getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return null;
        Object principal = authentication.getPrincipal();
        if (principal == null) return null;
        return principal instanceof CinepUser ? (CinepUser) principal : null;
    }

}

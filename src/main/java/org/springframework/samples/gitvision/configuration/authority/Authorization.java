package org.springframework.samples.gitvision.configuration.authority;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Authorization {
    CLOCKIFY_USER;

    public GrantedAuthority getAuthority(){
        return new SimpleGrantedAuthority(this.name());
    }
}

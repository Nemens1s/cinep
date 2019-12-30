package cinep.app.cinep.security;

public enum Role {
    USER,
    ADMIN;

    public boolean isAdmin(){
        return this == ADMIN;
    }

    public String toSpringRole(){
        return "ROLE_" + this.name();
    }

}

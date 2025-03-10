class TokenService {
    getLocalRefreshToken() {
        const user = JSON.parse(localStorage.getItem("user"));
        return user?.refreshToken;
    }

    getLocalAccessToken() {
        const jwt = JSON.parse(localStorage.getItem("jwt"));
        return jwt ? jwt : null;
    }

    setLocalAccessToken(jwt) {
        window.localStorage.setItem("jwt", JSON.stringify(jwt));
    }

    updateLocalAccessToken(token) {
        window.localStorage.setItem("jwt", JSON.stringify(token));
    }

    getUser() {
        return JSON.parse(localStorage.getItem("user"));
    }

    setUser(user) {
        window.localStorage.setItem("user", JSON.stringify(user));
    }

    removeUser() {
        window.localStorage.removeItem("user");
        window.localStorage.removeItem("jwt");
    }

    hasAnyAuthority(...authorities) {
        const roles = new Set(this.getUser().roles);
        return authorities.some(item => roles.has(item));
    }

}
const tokenService = new TokenService();

export default tokenService;
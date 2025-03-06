import tokenService from "../services/token.service";

export default async function fetchWithToken(
    url: RequestInfo | URL,
    init: RequestInit = {}
) {
    const token: string = tokenService.getLocalAccessToken();
    const headers = new Headers(init.headers);
    if (token)
        headers.set("Authorization", "Bearer " + token);

    return await fetch(url, {
        ...init,
        headers,
    });
}

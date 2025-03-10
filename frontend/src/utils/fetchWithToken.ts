import tokenservice from "@services/token.service.js";

export default async function fetchWithToken(
    url: RequestInfo | URL,
    init: RequestInit = {}
) {
    const token: string = tokenservice.getLocalAccessToken();
    const headers = new Headers(init.headers);
    if (token)
        headers.set("Authorization", "Bearer " + token);

    return await fetch(url, {
        ...init,
        headers,
    });
}

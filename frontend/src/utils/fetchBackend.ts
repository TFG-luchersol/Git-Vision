import tokenService from "@services/token.service.js";

export default async function fetchBackend(
    url: string | RequestInfo | URL,
    init: RequestInit = {},
    queryParams: Record<string, any> = {}
) {
    const token: string = tokenService.getLocalAccessToken();
    const headers = new Headers(init.headers);

    if (token) {
        const payload = JSON.parse(atob(token.split('.')[1]));
        const expirationTime = payload.exp * 1000;
        const now = Date.now();
    
        if (now > expirationTime) {
            tokenService.removeUser();
        } else {
            headers.set("Authorization", "Bearer " + token);
        }
    }

    let fullUrl: URL;
    const baseUrl = process.env.BACKEND_URL || "http://localhost:8080";
    const isAbsoluteUrl = (url: string): boolean =>
        url.startsWith("http://") || url.startsWith("https://");

    if (typeof url === "string") {
        fullUrl = isAbsoluteUrl(url) 
            ? new URL(url) 
            : new URL(url, baseUrl);
    } else if (url instanceof URL) {
        fullUrl = new URL(url);
    } else if (url instanceof Request) {
        const requestUrl = url.url;
        fullUrl = isAbsoluteUrl(requestUrl)
            ? new URL(requestUrl)
            : new URL(requestUrl, baseUrl);
    } else {
        throw new Error("Tipo de URL no soportado");
    }

    Object.entries(queryParams).forEach(([key, value]) => {
        if (value !== undefined && value !== null) {
            fullUrl.searchParams.append(key, String(value));
        }
    });
    
    return await fetch(fullUrl, {
        ...init,
        headers,
    });
}

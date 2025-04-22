import tokenservice from "@services/token.service.js";

export default async function fetchBackend(
    url: string | RequestInfo | URL,
    init: RequestInit = {}
) {
    const token: string = tokenservice.getLocalAccessToken();
    const headers = new Headers(init.headers);
    console.log(process.env.BACKEND_URL);
    if (token) headers.set("Authorization", "Bearer " + token);

    let fullUrl: string | URL;
    const baseUrl = process.env.BACKEND_URL || "http://localhost:8080";
    const isAbsoluteUrl = (url: string): boolean =>
        url.startsWith("http://") || url.startsWith("https://");

    if (typeof url === "string") {
        fullUrl = isAbsoluteUrl(url) ? url : new URL(url, baseUrl);
    } else if (url instanceof URL) {
        fullUrl = url;
    } else if (url instanceof Request) {
        const requestUrl = url.url;
        fullUrl = isAbsoluteUrl(requestUrl)
            ? requestUrl
            : new URL(requestUrl, baseUrl);
    } else {
        throw new Error("Tipo de URL no soportado");
    }

    return await fetch(fullUrl, {
        ...init,
        headers,
    });
}

export default async function getBody(response: Response) {
    const json = await response.json();

    const handlers: Record<number, Function> = {
        200: () => json,
        400: () => { throw new Error(json.message); },
    };

    const handler = handlers[response.status];

    if (!handler) {
        throw new Error(`Estado ${response.status} no esperado`);
    }

    return handler();
}
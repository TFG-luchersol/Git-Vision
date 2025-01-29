export default function getBody(response) {
    const handlers = {
        "OK": () => response.body.information,
        "BAD_REQUEST": () => { throw new Error(response.message); },
    };

    const handler = handlers[response.httpStatus];

    if (!handler) {
        throw new Error(`Estado ${response.httpStatus} no esperado`);
    }

    return handler();
}
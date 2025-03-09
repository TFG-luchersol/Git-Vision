export function stringToColor(str) {
    let hash = 0;
    for (let i = 0; i < str.length; i++) {
        hash = str.charCodeAt(i) + ((hash << 5) - hash);
    }

    // Extraer los componentes RGB
    let r = (hash >> 16) & 0xFF;
    let g = (hash >> 8) & 0xFF;
    let b = hash & 0xFF;

    r = (r * 2) % 256;
    g = (g * 2) % 256; 
    b = (b * 2) % 256;

    r = Math.min(255, r + 50);
    g = Math.min(255, g + 50);
    b = Math.min(255, b + 50);

    r = Math.max(128, r); 
    g = Math.max(128, g);
    b = Math.max(128, b);

    let color = '#' + ((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1).toUpperCase();

    return color;
}

export function darkenColor(hex, factor=0.5) {
    // Asegurarse de que el valor del factor esté entre 0 y 1
    factor = Math.max(0, Math.min(factor, 1));

    // Convertir el color hexadecimal a RGB
    let r = parseInt(hex.slice(1, 3), 16);
    let g = parseInt(hex.slice(3, 5), 16);
    let b = parseInt(hex.slice(5, 7), 16);

    // Calcular la cantidad a restar de cada componente en función del factor
    let adjust = Math.floor(255 * factor);

    // Restar el ajuste, pero asegurarse de que no sea inferior a 0
    r = Math.max(0, r - adjust);
    g = Math.max(0, g - adjust);
    b = Math.max(0, b - adjust);

    // Convertir los componentes RGB de vuelta a hexadecimal
    let darkenedHex = '#' + (1 << 24 | r << 16 | g << 8 | b).toString(16).slice(1).toUpperCase();

    return darkenedHex;
}
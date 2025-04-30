export function setCookie(name: string, value: string, options: { [key: string]: any } = {}) {
    let cookieString = `${name}=${encodeURIComponent(value)}; path=/;`;

    if (options.httpOnly) cookieString += " HttpOnly;";
    if (options.secure) cookieString += " Secure;";
    if (options.sameSite) cookieString += ` SameSite=${options.sameSite || "Strict"};`;

    if (options.maxAge) cookieString += ` Max-Age=${options.maxAge};`;
    if (options.expires) cookieString += ` Expires=${options.expires.toUTCString()};`;

    document.cookie = cookieString;
}


export function getCookie(name: string): string | null {
    const match = document.cookie.match(new RegExp(`(^| )${name}=([^;]+)`));
    return match ? decodeURIComponent(match[2]) : null;
}

export function deleteCookie(name: string) {
    document.cookie = `${name}=; Max-Age=0; path=/;`;
}